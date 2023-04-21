package watij.finders.table;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cell {

    Element webTable;

    List grid;

    Element td;

    int currentRow = 0;

    int currentCol = -1;

    int numRows, numCols;

    int rowIndex, colIndex;

    public Cell(Element td) {
        this.td = resolveTd(td);
        this.webTable = getParentTable(td);
        initTable();
    }

    private Element resolveTd(Element element) {
        if ("th".equalsIgnoreCase(element.getTagName())) {
            return element;
        }
        return findAncestorWithTagName(element, "td");
    }

    private void initTable() {
        grid = new ArrayList();
        numRows = getRowCount();
        numCols = getColumnCount();
        for (int i = 0; i < numRows; i++) {
            grid.add(new ArrayList(Arrays.asList(new Object[numCols])));
        }
        Element[] tableRows = rows();
        for (int i = 0; i < tableRows.length; i++) {
            Element tableRow = tableRows[i];
            Element[] tableCells = cells(tableRow);
            for (int j = 0; j < tableCells.length; j++) {
                Element tableCell = tableCells[j];
                addCell(tableCell);
            }
        }
    }

    private int getRowCount() {
        return webTable.getElementsByTagName("tr").getLength();
    }

    private Element[] rows() {
        List list = new ArrayList();
        NodeList nodeList = webTable.getElementsByTagName("tr");
        for (int i = 0; i < nodeList.getLength(); i++) {
            list.add(nodeList.item(i));
        }
        return (Element[]) list.toArray(new Element[0]);
    }

    private void addCell(Element tableCell) {
        findNextAvailableRowAndColumn();
        List columns = (List) grid.get(currentRow);
        columns.set(currentCol, tableCell);
        if (tableCell == td) {
            colIndex = currentCol;
            rowIndex = currentRow;
        }
        int rowspan = rowspan(tableCell);
        int colspan = colspan(tableCell);
        for (int i = 0; i < rowspan; i++) {
            for (int j = (i == 0) ? 1 : 0; j < colspan; j++) {
                ((List) grid.get(currentRow + i)).set(currentCol + j, new TableSpan(tableCell));
            }
        }
    }

    private void findNextAvailableRowAndColumn() {
        while (currentRow < grid.size()) {
            while (currentCol < numCols - 1) {
                currentCol++;
                List columns = (List) grid.get(currentRow);
                Object component = columns.get(currentCol);
                if (!(component instanceof TableSpan)) {
                    return;
                }
            }
            currentCol = -1;
            currentRow++;
        }
    }

    public List above() {
        List list = new ArrayList();
        for (int i = rowIndex; i >= 0; i--) {
            List columns = (List) grid.get(i);
            Object o = columns.get(colIndex);
            if (o instanceof TableSpan) {
                o = ((TableSpan) o).getTableCell();
            }
            if (!list.contains(o)) {
                list.add(o);
            }
        }
        return list;
    }

    public List below() {
        List list = new ArrayList();
        for (int i = rowIndex; i < numRows; i++) {
            List columns = (List) grid.get(i);
            Object o = columns.get(colIndex);
            if (o instanceof TableSpan) {
                o = ((TableSpan) o).getTableCell();
            }
            if (!list.contains(o)) {
                list.add(o);
            }
        }
        return list;
    }

    public List right() {
        List list = new ArrayList();
        for (int i = colIndex; i < numCols; i++) {
            List columns = (List) grid.get(rowIndex);
            Object o = columns.get(i);
            if (o instanceof TableSpan) {
                o = ((TableSpan) o).getTableCell();
            }
            if (!list.contains(o)) {
                list.add(o);
            }
        }
        return list;
    }

    public List left() {
        List list = new ArrayList();
        for (int i = colIndex; i >= 0; i--) {
            List columns = (List) grid.get(rowIndex);
            Object o = columns.get(i);
            if (o instanceof TableSpan) {
                o = ((TableSpan) o).getTableCell();
            }
            if (!list.contains(o)) {
                list.add(o);
            }
        }
        return list;
    }

    private class TableSpan {

        Element tableCell;

        public TableSpan(Element tableCell) {
            this.tableCell = tableCell;
        }

        public Element getTableCell() {
            return tableCell;
        }
    }

    private int getColumnCount() {
        Element[] rows = rows();
        int maxCount = 0;
        for (int i = 0; i < rows.length; i++) {
            Element row = rows[i];
            Element[] cells = cells(row);
            int columnCount = 0;
            for (int j = 0; j < cells.length; j++) {
                Element cell = cells[j];
                columnCount += colspan(cell);
            }
            if (columnCount > maxCount) {
                maxCount = columnCount;
            }
        }
        return maxCount;
    }

    private int colspan(Element element) {
        try {
            return new Integer(element.getAttribute("colspan")).intValue();
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private int rowspan(Element element) {
        try {
            return new Integer(element.getAttribute("rowspan")).intValue();
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private Element[] cells(Element element) {
        List list = new ArrayList();
        NodeList nodeList = element.getElementsByTagName("td");
        for (int i = 0; i < nodeList.getLength(); i++) {
            list.add(nodeList.item(i));
        }
        nodeList = element.getElementsByTagName("th");
        for (int i = 0; i < nodeList.getLength(); i++) {
            list.add(nodeList.item(i));
        }
        return (Element[]) list.toArray(new Element[0]);
    }

    protected Element getParentTable(Element element) {
        return findAncestorWithTagName(element, "table");
    }

    protected Element findAncestorWithTagName(Node node, String tagName) {
        while (node != null && !tagName.equalsIgnoreCase(node.getNodeName())) {
            node = node.getParentNode();
        }
        return (Element) node;
    }
}
