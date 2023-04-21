package com.meterware.httpunit.dom;

import junit.framework.TestSuite;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLCollection;

/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
public class HTMLTableTest extends AbstractHTMLElementTest {

    private Element _body;

    private HTMLTableElement _mainTable;

    private HTMLTableRowElement[] _htmlMainTableRows = new HTMLTableRowElement[3];

    public static TestSuite suite() {
        return new TestSuite(HTMLTableTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        _body = _htmlDocument.createElement("body");
        _htmlDocument.appendChild(_body);
        _mainTable = (HTMLTableElement) _htmlDocument.createElement("table");
        _body.appendChild(_mainTable);
        for (int i = 0; i < _htmlMainTableRows.length; i++) {
            _htmlMainTableRows[i] = (HTMLTableRowElement) _htmlDocument.createElement("tr");
            _mainTable.appendChild(_htmlMainTableRows[i]);
            for (int j = 0; j < 2; j++) {
                _htmlMainTableRows[i].appendChild(_htmlDocument.createElement("td"));
            }
        }
    }

    /**
     * Verify the construction of table nodes with their attributes.
     */
    public void testTableNodeCreation() throws Exception {
        doElementTest("td", HTMLTableCellElement.class, new Object[][] { { "abbr", "lots" }, { "align", "center" }, { "axis", "age" }, { "bgColor", "red" }, { "char", ",", "." }, { "charoff", "20" }, { "colspan", new Integer(3), new Integer(1) }, { "headers", "time,age" }, { "height", "20" }, { "nowrap", Boolean.TRUE, Boolean.FALSE }, { "rowspan", new Integer(15), new Integer(1) }, { "scope", "row" }, { "valign", "top", "middle" }, { "width", "10" } });
        doElementTest("th", HTMLTableCellElement.class, new Object[][] { { "abbr", "lots" } });
        doElementTest("tr", HTMLTableRowElement.class, new Object[][] { { "align", "center" }, { "bgColor", "red" }, { "char", ",", "." }, { "charoff", "20" }, { "valign", "top", "middle" } });
        doElementTest("table", HTMLTableElement.class, new Object[][] { { "align", "right", "center" }, { "bgColor", "red" }, { "border", "2" }, { "cellpadding", "20" }, { "cellspacing", "20" }, { "frame", "above", "void" }, { "rules", "groups", "none" }, { "summary", "blah blah" }, { "width", "5" } });
    }

    public void testReadTable() throws Exception {
        HTMLCollection rows = _mainTable.getRows();
        assertEquals("Number of rows in table", 3, rows.getLength());
        for (int i = 0; i < 3; i++) {
            Node node = rows.item(i);
            assertTrue("Row " + (i + 1) + " is not a table row", node instanceof HTMLTableRowElement);
            HTMLCollection cells = ((HTMLTableRowElement) node).getCells();
            assertEquals("Number of cells in row", 2, cells.getLength());
            for (int j = 0; j < 2; j++) {
                assertTrue("Cell (" + (i + 1) + "," + (j + 1) + ") is not a table cell", cells.item(j) instanceof HTMLTableCellElement);
            }
        }
    }
}
