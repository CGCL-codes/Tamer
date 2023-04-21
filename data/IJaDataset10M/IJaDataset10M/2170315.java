package writer2latex.office;

import java.util.LinkedList;
import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import writer2latex.util.Misc;

/**
 * <p> This class reads a table from a table:table or table:sub-table element
 * and presents it as an n by m grid. In addition it gives access to the
 * absolute and relative widths of tables, columns and cells.</p>
 */
public class TableReader {

    private Element tableNode;

    private LinkedList<TableLine> cols = new LinkedList<TableLine>();

    private LinkedList<TableLine> rows = new LinkedList<TableLine>();

    private LinkedList<LinkedList<Element>> cells = new LinkedList<LinkedList<Element>>();

    private int nMaxCols = 1;

    private int nMaxRows = 1;

    private String[] sColWidth;

    private String[] sRelColWidth;

    private String sTableWidth;

    private String sRelTableWidth;

    private Vector<TableRange> printRanges;

    private int nRowCount;

    private int nEmptyRowCount;

    /**
     * <p> The constructor reads a table from a table:table or table:sub-table
     * node.</p>
     * @param ofr the OfficeReader object to get style information from
     * @param tableNode the table node
	 */
    public TableReader(OfficeReader ofr, Element tableNode) {
        this.tableNode = tableNode;
        if (!tableNode.hasChildNodes()) {
            return;
        }
        countTableRows(tableNode);
        NodeList nl = tableNode.getChildNodes();
        int nLen = nl.getLength();
        for (int i = 0; i < nLen; i++) {
            Node child = nl.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String sName = child.getNodeName();
                if (sName.equals(XMLString.TABLE_TABLE_COLUMN)) {
                    readTableColumn(child, false, false);
                } else if (sName.equals(XMLString.TABLE_TABLE_COLUMNS)) {
                    readTableColumns(child, false, false);
                } else if (sName.equals(XMLString.TABLE_TABLE_COLUMN_GROUP)) {
                    readTableColumnGroup(child, false, false);
                } else if (sName.equals(XMLString.TABLE_TABLE_HEADER_COLUMNS)) {
                    readTableHeaderColumns(child, false, false);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROW)) {
                    readTableRow(child, false, false);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROWS)) {
                    readTableRows(child, false, false);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROW_GROUP)) {
                    readTableRowGroup(child, false, false);
                } else if (sName.equals(XMLString.TABLE_TABLE_HEADER_ROWS)) {
                    readTableHeaderRows(child, false, false);
                }
            }
        }
        StyleWithProperties tableStyle = ofr.getTableStyle(getTableStyleName());
        if (tableStyle != null) {
            sTableWidth = tableStyle.getProperty(XMLString.STYLE_WIDTH);
            sRelTableWidth = tableStyle.getProperty(XMLString.STYLE_REL_WIDTH);
        }
        int nCols = cols.size();
        sColWidth = new String[nCols];
        sRelColWidth = new String[nCols];
        int[] nRelColWidth = new int[nCols];
        boolean bHasRelWidth = true;
        int nColSum = 0;
        for (int nCol = 0; nCol < nCols; nCol++) {
            StyleWithProperties style = ofr.getColumnStyle(cols.get(nCol).getStyleName());
            if (style != null) {
                sColWidth[nCol] = style.getProperty(XMLString.STYLE_COLUMN_WIDTH);
                String s = style.getProperty(XMLString.STYLE_REL_COLUMN_WIDTH);
                if (s != null && s.endsWith("*")) {
                    nRelColWidth[nCol] = Misc.getPosInteger(s.substring(0, s.length() - 1), 1);
                }
            }
            if (sColWidth[nCol] == null) {
                sColWidth[nCol] = "2cm";
            }
            if (nRelColWidth[nCol] == 0) {
                bHasRelWidth = false;
            }
            nColSum += nRelColWidth[nCol];
        }
        for (int nCol = 0; nCol < nCols; nCol++) {
            if (bHasRelWidth) {
                sRelColWidth[nCol] = (100.0F * nRelColWidth[nCol] / nColSum) + "%";
            } else {
                sRelColWidth[nCol] = Misc.divide(sColWidth[nCol], sTableWidth, true);
            }
        }
        int nRows = cells.size();
        for (int nRow = 0; nRow < nRows; nRow++) {
            LinkedList<Element> row = cells.get(nRow);
            nCols = row.size();
            int nMaxCol = 0;
            int nMaxRow = 0;
            for (int nCol = 0; nCol < nCols; nCol++) {
                Element cell = (Element) row.get(nCol);
                if (cell.hasChildNodes()) {
                    nMaxRow = nRow + Misc.getPosInteger(cell.getAttribute(XMLString.TABLE_NUMBER_ROWS_SPANNED), 1);
                    if (nMaxRow > nMaxRows) {
                        nMaxRows = nMaxRow;
                    }
                    nMaxCol = nCol + Misc.getPosInteger(cell.getAttribute(XMLString.TABLE_NUMBER_COLUMNS_SPANNED), 1);
                    if (nMaxCol > nMaxCols) {
                        nMaxCols = nMaxCol;
                    }
                }
            }
        }
        printRanges = new Vector<TableRange>();
        if (!"false".equals(tableNode.getAttribute(XMLString.TABLE_PRINT))) {
            TableRangeParser parser = new TableRangeParser(tableNode.getAttribute(XMLString.TABLE_PRINT_RANGES));
            while (parser.hasMoreRanges()) {
                int[] nRange = parser.getRange();
                TableRange range = new TableRange(this);
                range.setFirstCol(nRange[0]);
                range.setFirstRow(nRange[1]);
                range.setLastCol(nRange[2]);
                range.setLastRow(nRange[3]);
                printRanges.add(range);
            }
        }
    }

    private void readTableColumn(Node node, boolean bHeader, boolean bDisplay) {
        int nRepeat = Misc.getPosInteger(Misc.getAttribute(node, XMLString.TABLE_NUMBER_COLUMNS_REPEATED), 1);
        while (nRepeat-- > 0) {
            cols.add(new TableLine(node, bHeader, bDisplay));
        }
    }

    private void readTableColumns(Node node, boolean bHeader, boolean bDisplay) {
        if (!node.hasChildNodes()) {
            return;
        }
        NodeList nl = node.getChildNodes();
        int nLen = nl.getLength();
        for (int i = 0; i < nLen; i++) {
            Node child = nl.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String sName = child.getNodeName();
                if (sName.equals(XMLString.TABLE_TABLE_COLUMN)) {
                    readTableColumn(child, bHeader, bDisplay);
                } else if (sName.equals(XMLString.TABLE_TABLE_COLUMN_GROUP)) {
                    readTableColumnGroup(child, bHeader, bDisplay);
                }
            }
        }
    }

    private void readTableColumnGroup(Node node, boolean bHeader, boolean bDisplay) {
        bDisplay = !"false".equals(Misc.getAttribute(node, XMLString.TABLE_DISPLAY));
        if (!node.hasChildNodes()) {
            return;
        }
        NodeList nl = node.getChildNodes();
        int nLen = nl.getLength();
        for (int i = 0; i < nLen; i++) {
            Node child = nl.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String sName = child.getNodeName();
                if (sName.equals(XMLString.TABLE_TABLE_HEADER_COLUMNS)) {
                    readTableHeaderColumns(child, bHeader, bDisplay);
                } else if (sName.equals(XMLString.TABLE_TABLE_COLUMN)) {
                    readTableColumn(child, bHeader, bDisplay);
                } else if (sName.equals(XMLString.TABLE_TABLE_COLUMN_GROUP)) {
                    readTableColumnGroup(child, bHeader, bDisplay);
                }
            }
        }
    }

    private void readTableHeaderColumns(Node node, boolean bHeader, boolean bDisplay) {
        readTableColumns(node, true, bDisplay);
    }

    private void readTableRow(Node node, boolean bHeader, boolean bDisplay) {
        int nRepeat = Misc.getPosInteger(Misc.getAttribute(node, XMLString.TABLE_NUMBER_ROWS_REPEATED), 1);
        while (nRepeat-- > 0 && rows.size() < nRowCount) {
            rows.add(new TableLine(node, bHeader, bDisplay));
            LinkedList<Element> row = new LinkedList<Element>();
            if (node.hasChildNodes()) {
                NodeList nl = node.getChildNodes();
                int nLen = nl.getLength();
                for (int i = 0; i < nLen; i++) {
                    Node child = nl.item(i);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        Element cell = (Element) child;
                        String sName = cell.getTagName();
                        if (sName.equals(XMLString.TABLE_TABLE_CELL)) {
                            int nColRepeat = Misc.getPosInteger(cell.getAttribute(XMLString.TABLE_NUMBER_COLUMNS_REPEATED), 1);
                            while (nColRepeat-- > 0) {
                                row.add(cell);
                            }
                        } else if (sName.equals(XMLString.TABLE_COVERED_TABLE_CELL)) {
                            int nColRepeat = Misc.getPosInteger(cell.getAttribute(XMLString.TABLE_NUMBER_COLUMNS_REPEATED), 1);
                            while (nColRepeat-- > 0) {
                                row.add(cell);
                            }
                        }
                    }
                }
            }
            cells.add(row);
        }
    }

    private void readTableRows(Node node, boolean bHeader, boolean bDisplay) {
        if (!node.hasChildNodes()) {
            return;
        }
        NodeList nl = node.getChildNodes();
        int nLen = nl.getLength();
        for (int i = 0; i < nLen; i++) {
            Node child = nl.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String sName = child.getNodeName();
                if (sName.equals(XMLString.TABLE_TABLE_ROW)) {
                    readTableRow(child, bHeader, bDisplay);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROW_GROUP)) {
                    readTableRowGroup(child, bHeader, bDisplay);
                }
            }
        }
    }

    private void readTableRowGroup(Node node, boolean bHeader, boolean bDisplay) {
        bDisplay = !"false".equals(Misc.getAttribute(node, XMLString.TABLE_DISPLAY));
        if (!node.hasChildNodes()) {
            return;
        }
        NodeList nl = node.getChildNodes();
        int nLen = nl.getLength();
        for (int i = 0; i < nLen; i++) {
            Node child = nl.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String sName = child.getNodeName();
                if (sName.equals(XMLString.TABLE_TABLE_HEADER_ROWS)) {
                    readTableHeaderRows(child, bHeader, bDisplay);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROW)) {
                    readTableRow(child, bHeader, bDisplay);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROW_GROUP)) {
                    readTableRowGroup(child, bHeader, bDisplay);
                }
            }
        }
    }

    public boolean isSubTable() {
        return XMLString.TABLE_SUB_TABLE.equals(tableNode.getTagName()) || "true".equals(Misc.getAttribute(tableNode, XMLString.TABLE_IS_SUB_TABLE));
    }

    private void readTableHeaderRows(Node node, boolean bHeader, boolean bDisplay) {
        readTableRows(node, true, bDisplay);
    }

    private void countTableRows(Element table) {
        nRowCount = 0;
        nEmptyRowCount = 0;
        Node child = table.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String sName = child.getNodeName();
                if (sName.equals(XMLString.TABLE_TABLE_ROW)) {
                    countTableRow(child);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROWS)) {
                    countTableRows(child);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROW_GROUP)) {
                    countTableRowGroup(child);
                } else if (sName.equals(XMLString.TABLE_TABLE_HEADER_ROWS)) {
                    countTableHeaderRows(child);
                }
            }
            child = child.getNextSibling();
        }
        if (nEmptyRowCount > 1) {
            nRowCount -= nEmptyRowCount - 1;
        }
    }

    private void countTableRow(Node node) {
        int nRepeat = Misc.getPosInteger(Misc.getAttribute(node, XMLString.TABLE_NUMBER_ROWS_REPEATED), 1);
        nRowCount += nRepeat;
        if (isEmptyRow(node)) {
            nEmptyRowCount += nRepeat;
        } else {
            nEmptyRowCount = 0;
        }
    }

    private boolean isEmptyRow(Node node) {
        Node child = node.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element cell = (Element) child;
                String sName = cell.getTagName();
                if (sName.equals(XMLString.TABLE_TABLE_CELL)) {
                    if (cell.hasChildNodes()) {
                        return false;
                    }
                }
            }
            child = child.getNextSibling();
        }
        return true;
    }

    private void countTableRows(Node node) {
        Node child = node.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String sName = child.getNodeName();
                if (sName.equals(XMLString.TABLE_TABLE_ROW)) {
                    countTableRow(child);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROW_GROUP)) {
                    countTableRowGroup(child);
                }
            }
            child = child.getNextSibling();
        }
    }

    private void countTableRowGroup(Node node) {
        Node child = node.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String sName = child.getNodeName();
                if (sName.equals(XMLString.TABLE_TABLE_HEADER_ROWS)) {
                    countTableHeaderRows(child);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROW)) {
                    countTableRow(child);
                } else if (sName.equals(XMLString.TABLE_TABLE_ROW_GROUP)) {
                    countTableRowGroup(child);
                }
            }
            child = child.getNextSibling();
        }
    }

    private void countTableHeaderRows(Node node) {
        countTableRows(node);
    }

    public String getTableName() {
        return tableNode.getAttribute(XMLString.TABLE_NAME);
    }

    public String getTableStyleName() {
        return tableNode.getAttribute(XMLString.TABLE_STYLE_NAME);
    }

    public String getTableWidth() {
        return sTableWidth;
    }

    public String getRelTableWidth() {
        return sRelTableWidth;
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getMaxRowCount() {
        return nMaxRows;
    }

    public int getFirstBodyRow() {
        int nRows = rows.size();
        for (int nRow = 0; nRow < nRows; nRow++) {
            if (!getRow(nRow).isHeader()) {
                return nRow;
            }
        }
        return nRows;
    }

    public int getColCount() {
        return cols.size();
    }

    public int getMaxColCount() {
        return nMaxCols;
    }

    public String getColumnWidth(int nCol) {
        return 0 <= nCol && nCol <= cols.size() ? sColWidth[nCol] : null;
    }

    public String getRelColumnWidth(int nCol) {
        return 0 <= nCol && nCol <= cols.size() ? sRelColWidth[nCol] : null;
    }

    public Element getCell(int nRow, int nCol) {
        if (nRow < 0 || nRow >= cells.size()) {
            return null;
        }
        LinkedList<Element> row = cells.get(nRow);
        if (nCol < 0 || nCol >= row.size()) {
            return null;
        }
        return (Element) row.get(nCol);
    }

    public String getCellStyleName(int nRow, int nCol) {
        Element cell = (Element) getCell(nRow, nCol);
        if (cell == null) {
            return null;
        }
        String s = cell.getAttribute(XMLString.TABLE_STYLE_NAME);
        if (s != null && s.length() > 0) {
            return s;
        }
        s = getRow(nRow).getDefaultCellStyleName();
        if (s != null && s.length() > 0) {
            return s;
        }
        s = getCol(nCol).getDefaultCellStyleName();
        return s;
    }

    public String getCellWidth(int nRow, int nCol) {
        Element cell = (Element) getCell(nRow, nCol);
        if (cell == null) {
            return null;
        }
        int nCols = Misc.getPosInteger(cell.getAttribute(XMLString.TABLE_NUMBER_COLUMNS_SPANNED), 1);
        String sWidth = sColWidth[nCol];
        for (int i = nCol + 1; i < nCol + nCols; i++) {
            sWidth = Misc.add(sWidth, sColWidth[i]);
        }
        return sWidth;
    }

    public TableLine getRow(int nRow) {
        if (nRow < 0 || nRow >= rows.size()) {
            return null;
        }
        return rows.get(nRow);
    }

    public TableLine getCol(int nCol) {
        if (nCol < 0 || nCol >= cols.size()) {
            return null;
        }
        return cols.get(nCol);
    }

    public int getPrintRangeCount() {
        return printRanges.size();
    }

    public TableRange getPrintRange(int nIndex) {
        if (0 <= nIndex && nIndex < printRanges.size()) {
            return printRanges.get(nIndex);
        } else {
            return null;
        }
    }
}
