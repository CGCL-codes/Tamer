package uk.ac.ed.ph.snuggletex.dombuilding;

import uk.ac.ed.ph.snuggletex.ErrorCode;
import uk.ac.ed.ph.snuggletex.SnuggleLogicException;
import uk.ac.ed.ph.snuggletex.conversion.DOMBuilder;
import uk.ac.ed.ph.snuggletex.conversion.SnuggleParseException;
import uk.ac.ed.ph.snuggletex.definitions.GlobalBuiltins;
import uk.ac.ed.ph.snuggletex.tokens.ArgumentContainerToken;
import uk.ac.ed.ph.snuggletex.tokens.CommandToken;
import uk.ac.ed.ph.snuggletex.tokens.EnvironmentToken;
import uk.ac.ed.ph.snuggletex.tokens.ErrorToken;
import uk.ac.ed.ph.snuggletex.tokens.FlowToken;
import uk.ac.ed.ph.snuggletex.tokens.TokenType;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

/**
 * Handles the (rather complex) <tt>tabular</tt> environment.
 * It also handles the <tt>\\hline</tt> command.
 * 
 * TODO: This is legal inside $\mbox{...}$ so needs to output MathML in this case. Eeek!!!
 * TODO: No support for \vline and friends!!!
 * 
 * @author  David McKain
 * @version $Revision: 179 $
 */
public final class TabularBuilder implements CommandHandler, EnvironmentHandler {

    /**
     * The command matched here is <tt>\\hline</tt>.
     * <p>
     * This case will have been dealt with explicitly by the tabular environment when
     * used correctly so, if we end up here, then client has made a boo-boo. 
     */
    public void handleCommand(DOMBuilder builder, Element parentElement, CommandToken token) throws SnuggleParseException {
        builder.appendOrThrowError(parentElement, token, ErrorCode.TDETB3, token.getCommand().getTeXName());
    }

    public void handleEnvironment(DOMBuilder builder, Element parentElement, EnvironmentToken token) throws SnuggleParseException {
        int[] geometry = computeTableDimensions(token.getContent());
        int numColumns = geometry[1];
        List<List<String>> columnClasses = new ArrayList<List<String>>();
        ArgumentContainerToken specToken = token.getArguments()[0];
        CharSequence specData = token.getArguments()[0].getSlice().extract();
        char c;
        String cellAlign = null;
        boolean borderFlag = false;
        for (int i = 0; i < specData.length(); i++) {
            c = specData.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }
            switch(c) {
                case 'c':
                    cellAlign = "align-center";
                    break;
                case 'l':
                    cellAlign = "align-left";
                    break;
                case 'r':
                    cellAlign = "align-right";
                    break;
                case '|':
                    borderFlag = true;
                    break;
                default:
                    builder.appendOrThrowError(parentElement, specToken, ErrorCode.TDETB1, String.valueOf(c));
                    break;
            }
            if (cellAlign != null) {
                List<String> classes = new ArrayList<String>();
                classes.add(cellAlign);
                if (borderFlag) {
                    classes.add("left-border");
                }
                columnClasses.add(classes);
                cellAlign = null;
                borderFlag = false;
            }
        }
        if (columnClasses.isEmpty()) {
            builder.appendOrThrowError(parentElement, specToken, ErrorCode.TDETB2);
            return;
        }
        if (borderFlag) {
            columnClasses.get(columnClasses.size() - 1).add("right-border");
        }
        if (columnClasses.size() < numColumns) {
            builder.appendOrThrowError(parentElement, specToken, ErrorCode.TDETB0, Integer.valueOf(columnClasses.size()), Integer.valueOf(numColumns));
        }
        Element tableElement = builder.appendXHTMLElement(parentElement, "table");
        builder.applyCSSStyle(tableElement, "tabular");
        Element tbodyElement = builder.appendXHTMLElement(tableElement, "tbody");
        Element trElement, tdElement;
        List<String> tdClasses = new ArrayList<String>(3);
        int columnIndex, columnsInRow;
        int rowIndex;
        FlowToken rowToken;
        List<FlowToken> tableContents = token.getContent().getContents();
        boolean topBorderFlag = false;
        boolean bottomBorderFlag = false;
        for (rowIndex = 0; rowIndex < tableContents.size(); rowIndex++) {
            rowToken = tableContents.get(rowIndex);
            if (rowToken.isCommand(GlobalBuiltins.CMD_HLINE)) {
                topBorderFlag = true;
                continue;
            } else if (rowToken.isCommand(GlobalBuiltins.CMD_TABLE_ROW)) {
                bottomBorderFlag = false;
                for (int i = rowIndex + 1; i < tableContents.size(); i++) {
                    if (tableContents.get(i).isCommand(GlobalBuiltins.CMD_HLINE)) {
                        bottomBorderFlag = true;
                    } else {
                        bottomBorderFlag = false;
                        break;
                    }
                }
                List<FlowToken> columns = ((CommandToken) rowToken).getArguments()[0].getContents();
                trElement = builder.appendXHTMLElement(tbodyElement, "tr");
                columnsInRow = columns.size();
                for (columnIndex = 0; columnIndex < numColumns; columnIndex++) {
                    tdElement = builder.appendXHTMLElement(trElement, "td");
                    tdClasses.clear();
                    tdClasses.add("tabular");
                    if (columnIndex < columnClasses.size()) {
                        tdClasses.addAll(columnClasses.get(columnIndex));
                    }
                    if (topBorderFlag) {
                        tdClasses.add("top-border");
                    }
                    if (bottomBorderFlag) {
                        tdClasses.add("bottom-border");
                    }
                    builder.applyCSSStyle(tdElement, tdClasses.toArray(new String[tdClasses.size()]));
                    if (columnIndex < columnsInRow) {
                        builder.handleTokens(tdElement, ((CommandToken) columns.get(columnIndex)).getArguments()[0].getContents(), true);
                    }
                }
                topBorderFlag = false;
                if (bottomBorderFlag) {
                    break;
                }
            } else if (rowToken.getType() == TokenType.ERROR) {
                trElement = builder.appendXHTMLElement(tbodyElement, "tr");
                tdElement = builder.appendXHTMLElement(trElement, "td");
                builder.appendErrorElement(tdElement, (ErrorToken) rowToken);
            } else {
                throw new SnuggleLogicException("Expected table contents to be \\hline or table rows");
            }
        }
    }

    /**
     * Computes the dimensions of the table by looking at its content.
     * 
     * @param tableContent content of the <tt>tabular</tt> environment.
     * 
     * @return { rowCount, columnCount } pair
     */
    protected static int[] computeTableDimensions(ArgumentContainerToken tableContent) {
        int maxColumns = 0;
        int rowCount = 0;
        int colCountWithinRow = 0;
        for (FlowToken contentToken : tableContent) {
            if (contentToken.isCommand(GlobalBuiltins.CMD_HLINE) || contentToken.getType() == TokenType.ERROR) {
                continue;
            } else if (contentToken.isCommand(GlobalBuiltins.CMD_TABLE_ROW)) {
                rowCount++;
                colCountWithinRow = 0;
                CommandToken rowToken = (CommandToken) contentToken;
                ArgumentContainerToken rowContents = rowToken.getArguments()[0];
                for (FlowToken rowContentToken : rowContents) {
                    if (rowContentToken.isCommand(GlobalBuiltins.CMD_TABLE_COLUMN)) {
                        colCountWithinRow++;
                    } else {
                        throw new SnuggleLogicException("Did not expect to find token " + rowContentToken + " within a table row");
                    }
                }
                if (colCountWithinRow > maxColumns) {
                    maxColumns = colCountWithinRow;
                }
            } else {
                throw new SnuggleLogicException("Did not expect to find token " + contentToken + " within a top-level table content");
            }
        }
        return new int[] { rowCount, maxColumns };
    }
}
