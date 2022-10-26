package tracko.logic.parser;

import static tracko.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static logic.commands.CommandTestUtil.COST_PRICE_DESC_DEFAULT;
import static logic.commands.CommandTestUtil.COST_PRICE_DESC_SECOND;
import static logic.commands.CommandTestUtil.INVALID_COST_PRICE_DESC;
import static logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static logic.commands.CommandTestUtil.INVALID_ITEM_NAME_DESC;
import static logic.commands.CommandTestUtil.INVALID_QUANTITY_DESC;
import static logic.commands.CommandTestUtil.INVALID_SELL_PRICE_DESC;
import static logic.commands.CommandTestUtil.ITEM_DESCRIPTION_DESC_DEFAULT;
import static logic.commands.CommandTestUtil.ITEM_DESCRIPTION_DESC_SECOND;
import static logic.commands.CommandTestUtil.ITEM_NAME_DESC_AMY;
import static logic.commands.CommandTestUtil.ITEM_NAME_DESC_DEFAULT;
import static logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static logic.commands.CommandTestUtil.QUANTITY_DESC_AMY;
import static logic.commands.CommandTestUtil.QUANTITY_DESC_DEFAULT;
import static logic.commands.CommandTestUtil.SELL_PRICE_DESC_DEFAULT;
import static logic.commands.CommandTestUtil.SELL_PRICE_DESC_SECOND;
import static logic.commands.CommandTestUtil.VALID_DEFAULT_COST_PRICE;
import static logic.commands.CommandTestUtil.VALID_DEFAULT_DESCRIPTION;
import static logic.commands.CommandTestUtil.VALID_DEFAULT_ITEM_NAME;
import static logic.commands.CommandTestUtil.VALID_DEFAULT_QUANTITY;
import static logic.commands.CommandTestUtil.VALID_DEFAULT_SELL_PRICE;
import static logic.parser.CommandParserTestUtil.assertParseFailure;
import static logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import tracko.logic.commands.item.AddItemCommand;
import tracko.logic.parser.item.AddItemCommandParser;
import tracko.model.item.Description;
import tracko.model.item.Item;
import tracko.model.item.ItemName;
import tracko.model.item.Price;
import tracko.model.item.Quantity;
import tracko.testutil.ItemBuilder;

public class AddItemCommandParserTest {
    private AddItemCommandParser parser = new AddItemCommandParser();

    @Test
    public void parseInitial_allFieldsPresent_success() {
        Item expectedItem = new ItemBuilder().build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT + COST_PRICE_DESC_DEFAULT,
                new AddItemCommand(expectedItem));

        // multiple item names - last item name accepted
        assertParseSuccess(parser, ITEM_NAME_DESC_AMY + ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT + COST_PRICE_DESC_DEFAULT,
                new AddItemCommand(expectedItem));

        // multiple quantities - last quantity accepted
        assertParseSuccess(parser, ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_AMY + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT + COST_PRICE_DESC_DEFAULT,
                new AddItemCommand(expectedItem));

        // multiple descriptions - last description accepted
        assertParseSuccess(parser, ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_SECOND + ITEM_DESCRIPTION_DESC_DEFAULT
                + SELL_PRICE_DESC_DEFAULT + COST_PRICE_DESC_DEFAULT, new AddItemCommand(expectedItem));

        // multiple sell price - last sell price accepted
        assertParseSuccess(parser, ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_SECOND + SELL_PRICE_DESC_DEFAULT
                + COST_PRICE_DESC_DEFAULT, new AddItemCommand(expectedItem));

        // multiple cost price - last cost price accepted
        assertParseSuccess(parser, ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT + COST_PRICE_DESC_SECOND
                + COST_PRICE_DESC_DEFAULT, new AddItemCommand(expectedItem));
    }

    @Test
    public void parseInitial_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddItemCommand.MESSAGE_USAGE);

        // missing item name prefix
        assertParseFailure(parser, VALID_DEFAULT_ITEM_NAME + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT
                + COST_PRICE_DESC_DEFAULT, expectedMessage);

        // missing quantity prefix
        assertParseFailure(parser, ITEM_NAME_DESC_DEFAULT + VALID_DEFAULT_QUANTITY
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT
                + COST_PRICE_DESC_DEFAULT, expectedMessage);

        // missing description prefix
        assertParseFailure(parser, ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + VALID_DEFAULT_DESCRIPTION + SELL_PRICE_DESC_DEFAULT + COST_PRICE_DESC_DEFAULT, expectedMessage);

        // missing sell price prefix
        assertParseFailure(parser, ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + VALID_DEFAULT_SELL_PRICE + COST_PRICE_DESC_DEFAULT, expectedMessage);

        // missing cost price prefix
        assertParseFailure(parser, ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT + VALID_DEFAULT_COST_PRICE, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_DEFAULT_ITEM_NAME + VALID_DEFAULT_QUANTITY
                + VALID_DEFAULT_DESCRIPTION + VALID_DEFAULT_SELL_PRICE + VALID_DEFAULT_COST_PRICE, expectedMessage);
    }

    @Test
    public void parseInitial_invalidValue_failure() {
        // invalid item name
        assertParseFailure(parser, INVALID_ITEM_NAME_DESC + QUANTITY_DESC_DEFAULT
                        + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT
                        + COST_PRICE_DESC_DEFAULT, ItemName.MESSAGE_CONSTRAINTS);

        // invalid quantity
        assertParseFailure(parser, ITEM_NAME_DESC_DEFAULT + INVALID_QUANTITY_DESC
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT
                + COST_PRICE_DESC_DEFAULT, Quantity.MESSAGE_CONSTRAINTS);

        // invalid description
        assertParseFailure(parser, ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                        + INVALID_DESCRIPTION_DESC + SELL_PRICE_DESC_DEFAULT
                        + COST_PRICE_DESC_DEFAULT, Description.MESSAGE_CONSTRAINTS);

        // invalid sell price
        assertParseFailure(parser, ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + INVALID_SELL_PRICE_DESC
                + COST_PRICE_DESC_DEFAULT, Price.MESSAGE_CONSTRAINTS);

        // invalid cost price
        assertParseFailure(parser, ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT
                + INVALID_COST_PRICE_DESC, Price.MESSAGE_CONSTRAINTS);


        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_ITEM_NAME_DESC + INVALID_QUANTITY_DESC
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT
                + COST_PRICE_DESC_DEFAULT, ItemName.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + ITEM_NAME_DESC_DEFAULT + QUANTITY_DESC_DEFAULT
                + ITEM_DESCRIPTION_DESC_DEFAULT + SELL_PRICE_DESC_DEFAULT + COST_PRICE_DESC_DEFAULT,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddItemCommand.MESSAGE_USAGE));
    }
}
