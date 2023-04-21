package org.xmlcml.cml.tools;

import org.junit.Test;
import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.element.CMLArrayList;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLScalar;
import org.xmlcml.cml.element.CMLTableContent;
import org.xmlcml.cml.element.CMLTableRowList;
import org.xmlcml.cml.test.TableFixture;
import org.xmlcml.cml.testutil.JumboTestUtils;

/**
 * test TableTool.
 * 
 * @author pmr
 * 
 */
public class TableRowListToolTest {

    TableFixture fixture = new TableFixture();

    /**
	 * Test method for
	 * {@link org.xmlcml.cml.element.CMLTableRowList#createTableContent()}.
	 */
    @Test
    public final void testCreateTableContent() {
        CMLTableContent tableContent0 = TableRowListTool.getOrCreateTool(fixture.tableRowList).createTableContent();
        boolean stripWhite = true;
        JumboTestUtils.assertEqualsCanonically("table content", fixture.tableContent, tableContent0, stripWhite);
    }

    /**
	 * Test method for
	 * {@link org.xmlcml.cml.element.CMLTableRowList#addColumn(org.xmlcml.cml.element.CMLList)}
	 * .
	 */
    @Test
    public final void testAddColumnCMLList() {
        CMLList cmlList = new CMLList();
        cmlList.appendChild(new CMLScalar(10.1));
        cmlList.appendChild(new CMLScalar(20.2));
        cmlList.appendChild(new CMLScalar(30.3));
        CMLTableRowList tableRowList1 = new CMLTableRowList(fixture.tableRowList);
        TableRowListTool.getOrCreateTool(tableRowList1).addColumn(cmlList);
        String ss = "" + "<tableRowList " + CMLConstants.CML_XMLNS + ">" + "<tableRow>" + "<tableCell>1</tableCell>" + "<tableCell>a</tableCell>" + "<tableCell><scalar dataType='xsd:double'>10.1</scalar></tableCell>" + "</tableRow>" + "<tableRow>" + "<tableCell>2</tableCell>" + "<tableCell>b</tableCell>" + "<tableCell><scalar dataType='xsd:double'>20.2</scalar></tableCell>" + "</tableRow>" + "<tableRow>" + "<tableCell>3</tableCell>" + "<tableCell>c</tableCell>" + "<tableCell><scalar dataType='xsd:double'>30.3</scalar></tableCell>" + "</tableRow>" + "</tableRowList>";
        CMLTableRowList expected = (CMLTableRowList) JumboTestUtils.parseValidString(ss);
        JumboTestUtils.assertEqualsCanonically("tablerow", expected, tableRowList1, true);
    }

    /**
	 * Test method for
	 * {@link org.xmlcml.cml.element.CMLTableRowList#createArrayList(int, int, org.xmlcml.cml.element.CMLTableHeader)}
	 * .
	 */
    @Test
    public final void testCreateArrayList() {
        CMLArrayList arrayList1 = TableRowListTool.getOrCreateTool(fixture.tableRowList).createArrayList(3, 2, fixture.tableHeader);
        JumboTestUtils.assertEqualsCanonically("tablerow", fixture.arrayList, arrayList1, true);
    }
}
