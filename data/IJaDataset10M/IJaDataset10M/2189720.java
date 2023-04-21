package ca.ucalgary.cpsc.ebe.fitClipse.refactoring.parse.wiki;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import aaftt.RefactoringException;

/**
 * The Class WikiDocumentParser.
 */
public class WikiDocumentParser implements ITestDefinitionParser {

    /**
	 * The tables.
	 */
    private List<TestDefinitionTable> tables = new LinkedList<TestDefinitionTable>();

    /**
	 * The document.
	 */
    private String document;

    /**
	 * Instantiates a new wiki document parser.
	 * 
	 * @param document
	 *            the document
	 */
    public WikiDocumentParser(String document) {
        this.document = document;
        parseDocument(document);
    }

    /**
	 * Parses the document.
	 * 
	 * @param document
	 *            the document
	 */
    private void parseDocument(String document) {
        this.tables = new LinkedList<TestDefinitionTable>();
        if (!document.endsWith("\r\n")) {
            document = document.concat("\r\n");
        }
        List<String> tokens = tokenizeByNewLines(document);
        String currentPart = "";
        TestDefinitionRow row = null;
        TestDefinitionCell cell = null;
        int offsetBegin;
        int offsetEnd;
        int offsetMark = 0;
        int cellIndex = 0;
        int rowIndex = 0;
        int tableIndex = 0;
        int absoluteCellIndex = 0;
        int absoluteRowIndex = 0;
        boolean tableStarted = false;
        List<TestDefinitionRow> rows = new LinkedList<TestDefinitionRow>();
        List<TestDefinitionCell> cells = new LinkedList<TestDefinitionCell>();
        for (String token : tokens) {
            if (token.endsWith("\r")) {
                token = token.substring(0, token.length() - 1);
            }
            token = token.trim();
            if ((token.startsWith("|") && token.endsWith("|")) || (token.startsWith("!|") && token.endsWith("|"))) {
                tableStarted = true;
                offsetBegin = document.indexOf(token, offsetMark);
                offsetEnd = document.indexOf(token, offsetMark) + token.length();
                row = new TestDefinitionRow(offsetBegin, offsetEnd, token, rowIndex, tableIndex, absoluteRowIndex);
                absoluteRowIndex++;
                List<String> pipeParts = tokenizeByPipes(row.getWikiString());
                for (String part : pipeParts) {
                    offsetBegin = document.indexOf(part, offsetMark);
                    offsetEnd = document.indexOf(part, offsetMark) + part.length();
                    offsetMark = offsetEnd;
                    Pattern pattern = Pattern.compile("[a-zA-Z0-9\\(\\)\\., ]+");
                    Matcher matcher = pattern.matcher(part);
                    String value = part;
                    if (matcher.find()) value = matcher.group();
                    cell = new TestDefinitionCell(offsetBegin, offsetEnd, value, part, cellIndex, rowIndex, tableIndex, absoluteCellIndex);
                    absoluteCellIndex++;
                    cells.add(cell);
                    cellIndex++;
                }
                row.addCells(cells);
                cellIndex = 0;
                cells = new LinkedList<TestDefinitionCell>();
                rows.add(row);
                rowIndex++;
                token = token.concat("\r\n");
                currentPart += token;
            } else {
                if (tableStarted && !((token.startsWith("|") && token.endsWith("|")) || (token.startsWith("!|") && token.endsWith("|")))) {
                    tableStarted = false;
                    currentPart = currentPart.trim();
                    offsetBegin = document.indexOf(currentPart);
                    offsetEnd = document.indexOf(currentPart) + currentPart.length();
                    if (!token.equals("")) offsetMark = offsetEnd;
                    TestDefinitionTable table = new TestDefinitionTable(currentPart, offsetBegin, offsetEnd, tableIndex);
                    tableIndex++;
                    rowIndex = 0;
                    cellIndex = 0;
                    table.addRows(rows);
                    this.tables.add(table);
                    rows = new LinkedList<TestDefinitionRow>();
                    cells = new LinkedList<TestDefinitionCell>();
                    currentPart = "";
                }
            }
        }
        assert (offsetMark == document.length());
        for (TestDefinitionTable testDefinitionTable : this.tables) {
            for (TestDefinitionRow testDefinitionRow : testDefinitionTable.getRows()) {
                for (TestDefinitionCell testDefinitionCell : testDefinitionRow.getCells()) {
                    System.out.println("Tableindex: " + testDefinitionCell.getTableIndex() + " RowIndex: " + testDefinitionCell.getRowIndex() + " CellIndex: " + testDefinitionCell.getCellIndex() + "Value: " + testDefinitionCell.getValue());
                }
            }
        }
    }

    /**
	 * Tokenize by new lines.
	 * 
	 * @param in
	 *            the in
	 * 
	 * @return the list< string>
	 */
    private static List<String> tokenizeByNewLines(String in) {
        List<String> result = new LinkedList<String>();
        StringBuffer line = new StringBuffer();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (c == '\n') {
                result.add(line.toString());
                line = new StringBuffer();
            } else {
                line.append(c);
            }
        }
        result.add(line.toString());
        return result;
    }

    /**
	 * Tokenize by pipes.
	 * 
	 * @param inputString
	 *            the input string
	 * 
	 * @return the list< string>
	 */
    private static List<String> tokenizeByPipes(String inputString) {
        List<String> result = new LinkedList<String>();
        StringBuffer line = new StringBuffer();
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (c == '|') {
                if ((i != 0) && (i != inputString.length())) {
                    String lineString = line.toString();
                    lineString = lineString.trim();
                    result.add(lineString);
                    line = new StringBuffer();
                }
            } else {
                line.append(c);
            }
        }
        return result;
    }

    public int getRowCount(int tableIndex) {
        for (TestDefinitionTable table : this.tables) {
            if (table.getTableIndex() == tableIndex) return table.getRows().size();
        }
        return -1;
    }

    /**
	 * Gets the wiki code.
	 * 
	 * @return the wiki code
	 */
    public String getWikiCode() {
        String returnValue = "";
        for (TestDefinitionTable testDefinitionTable : this.tables) {
            if (!returnValue.equals("")) returnValue = returnValue.concat(System.getProperty("line.separator"));
            for (TestDefinitionRow testDefinitionRow : testDefinitionTable.getRows()) {
                returnValue = returnValue.concat(testDefinitionRow.getWikiCode());
                returnValue = returnValue.concat(System.getProperty("line.separator"));
            }
        }
        return returnValue;
    }

    public String getDocument() {
        return this.document;
    }

    public List<TestDefinitionCell> getAllCellsOfColumn(TestDefinitionCell testDefinitionCell) {
        List<TestDefinitionCell> returnCells = getColumn(testDefinitionCell);
        returnCells.remove(0);
        return returnCells;
    }

    /**
	 * Gets the cell by index.
	 * 
	 * @param tableIndex
	 *            the table index
	 * @param rowIndex
	 *            the row index
	 * @param cellIndex
	 *            the cell index
	 * 
	 * @return the cell by index
	 */
    public TestDefinitionCell getCellByIndex(int tableIndex, int rowIndex, int cellIndex) {
        for (TestDefinitionTable testDefinitionTable : this.tables) {
            if (testDefinitionTable.getTableIndex() != tableIndex) continue;
            for (TestDefinitionRow testDefinitionRow : testDefinitionTable.getRows()) {
                if (testDefinitionRow.getRowIndex() != rowIndex) continue;
                for (TestDefinitionCell testDefinitionCell : testDefinitionRow.getCells()) {
                    if (testDefinitionCell.getCellIndex() == cellIndex) return testDefinitionCell;
                }
            }
        }
        return null;
    }

    public List<TestDefinitionRow> getRowsByTableIndex(int tableIndex) {
        for (TestDefinitionTable testDefinitionTable : this.tables) {
            if (testDefinitionTable.getTableIndex() != tableIndex) {
                continue;
            }
            return testDefinitionTable.getRows();
        }
        return null;
    }

    public TestDefinitionRow getRowByRelativeIndex(int tableIndex, int rowIndex) {
        for (TestDefinitionTable testDefinitionTable : this.tables) {
            if (testDefinitionTable.getTableIndex() != tableIndex) continue;
            for (TestDefinitionRow testDefinitionRow : testDefinitionTable.getRows()) {
                if (testDefinitionRow.getRowIndex() == rowIndex) return testDefinitionRow;
            }
        }
        return null;
    }

    public List<TestDefinitionTable> getTables() {
        return this.tables;
    }

    public TestDefinitionRow getRowByAbsoluteIndex(int absoluteIndex) {
        for (TestDefinitionTable table : this.tables) {
            for (TestDefinitionRow testDefinitionRow : table.getRows()) {
                if (testDefinitionRow.getAbsoluteIndex() != absoluteIndex) {
                    continue;
                } else return testDefinitionRow;
            }
        }
        return null;
    }

    public int getCellCount(TestDefinitionRow testDefinitionRow) {
        for (TestDefinitionTable table : this.tables) {
            if (table.getTableIndex() != testDefinitionRow.getTableIndex()) continue;
            for (TestDefinitionRow row : table.getRows()) {
                if (row.getRowIndex() != row.getRowIndex()) continue;
                return row.getCells().size();
            }
        }
        return -1;
    }

    public List<TestDefinitionCell> getColumn(TestDefinitionCell testDefinitionCell) {
        List<TestDefinitionCell> returnCells = new LinkedList<TestDefinitionCell>();
        for (int i = testDefinitionCell.getRowIndex(); i < getRowCount(testDefinitionCell.getTableIndex()); i++) {
            returnCells.add(getCellByIndex(testDefinitionCell.getTableIndex(), i, testDefinitionCell.getCellIndex()));
        }
        return returnCells;
    }

    public TestDefinitionCell getCellByAbsoluteIndex(int absoluteCellIndex) {
        for (TestDefinitionTable table : this.tables) {
            for (TestDefinitionRow row : table.getRows()) {
                for (TestDefinitionCell cell : row.getCells()) {
                    if (cell.getAbsoluteIndex() != absoluteCellIndex) {
                        continue;
                    } else return cell;
                }
            }
        }
        return null;
    }

    public TestDefinitionCell getCellByRelativeIndex(int tableIndex, int rowIndex, int cellIndex) {
        for (TestDefinitionTable table : this.tables) {
            if (table.getTableIndex() != tableIndex) continue;
            for (TestDefinitionRow row : table.getRows()) {
                if (row.getRowIndex() != rowIndex) continue;
                for (TestDefinitionCell cell : row.getCells()) {
                    if (cell.getCellIndex() != cellIndex) {
                        continue;
                    } else return cell;
                }
            }
        }
        return null;
    }

    public List<TestDefinitionCell> getCellByRelativeIndex(int rowIndex, int cellIndex) {
        List<TestDefinitionCell> cells = new LinkedList<TestDefinitionCell>();
        for (int i = 0; i < this.tables.size(); i++) {
            TestDefinitionCell cell = getCellByRelativeIndex(i, rowIndex, cellIndex);
            if (cell != null) cells.add(cell);
        }
        return cells;
    }
}
