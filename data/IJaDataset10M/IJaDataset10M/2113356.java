package org.ujac.util.table.test;

import java.util.Iterator;
import org.ujac.util.table.Column;
import org.ujac.util.table.CountReportFunctionExecutor;
import org.ujac.util.table.DataTable;
import org.ujac.util.table.LayoutHints;
import org.ujac.util.table.ReportFunction;
import org.ujac.util.table.ReportGroup;
import org.ujac.util.table.ReportTable;
import org.ujac.util.table.Row;
import org.ujac.util.table.SumReportFunctionExecutor;
import org.ujac.util.table.Table;
import org.ujac.util.table.TableConstants;
import org.ujac.util.table.TableException;
import junit.framework.TestCase;

/**
 * Name: ReportTableTest<br>
 * Description: A JUnit test for report tables.
 * <br>Log: $Log$
 * <br>Log: Revision 1.2  2004/10/18 15:06:12  lauerc
 * <br>Log: Added more test cases.
 * <br>Log:
 * <br>Log: Revision 1.1  2004/10/16 01:12:54  lauerc
 * <br>Log: Initial revision.
 * <br>Log:
 * @author $Author: lauerc $
 * @version $Revision: 1836 $
 */
public class ReportTableTest extends TestCase {

    /** The data table to use. */
    private Table dataTable = null;

    /**
   * @see junit.framework.TestCase#setUp()
   */
    protected void setUp() throws Exception {
        this.dataTable = new DataTable();
        Column idCol = dataTable.addColumn("ID", TableConstants.TYPE_INT);
        idCol.setLayoutHints(new LayoutHints(10, "ID", LayoutHints.LEFT));
        int idID = idCol.getIndex();
        Column depotCol = dataTable.addColumn("DEPOT", TableConstants.TYPE_STRING);
        depotCol.setLayoutHints(new LayoutHints(12, "Depot", LayoutHints.LEFT));
        int depotID = depotCol.getIndex();
        Column typeCol = dataTable.addColumn("TYPE", TableConstants.TYPE_STRING);
        typeCol.setLayoutHints(new LayoutHints(12, "Type", LayoutHints.LEFT));
        int typeID = typeCol.getIndex();
        Column nameCol = dataTable.addColumn("NAME", TableConstants.TYPE_STRING);
        nameCol.setLayoutHints(new LayoutHints(40, "Name", LayoutHints.LEFT));
        int nameID = nameCol.getIndex();
        Column valueCol = dataTable.addColumn("VALUE", TableConstants.TYPE_DOUBLE);
        valueCol.setLayoutHints(new LayoutHints(25, "Value", LayoutHints.RIGHT));
        int valueID = valueCol.getIndex();
        Row row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 1");
        row.setString(typeID, "S");
        row.setString(nameID, "SAP");
        row.setDouble(valueID, 2725.80);
        row = dataTable.addRow();
        row.setInt(idID, 2);
        row.setString(depotID, "Depot 1");
        row.setString(typeID, "S");
        row.setString(nameID, "ZOOM TECHNLGIES");
        row.setDouble(valueID, 1235.65);
        row = dataTable.addRow();
        row.setInt(idID, 3);
        row.setString(depotID, "Depot 1");
        row.setString(typeID, "S");
        row.setString(nameID, "MICRO$OFT");
        row.setDouble(valueID, 455.33);
        row = dataTable.addRow();
        row.setInt(idID, 4);
        row.setString(depotID, "Depot 1");
        row.setString(typeID, "S");
        row.setString(nameID, "COMTECH TELCOM");
        row.setDouble(valueID, 5465.56);
        row = dataTable.addRow();
        row.setInt(idID, 5);
        row.setString(depotID, "Depot 1");
        row.setString(typeID, "S");
        row.setString(nameID, "DAIMLER CRYSLER");
        row.setDouble(valueID, 7655.23);
        row = dataTable.addRow();
        row.setInt(idID, 6);
        row.setString(depotID, "Depot 1");
        row.setString(typeID, "F");
        row.setString(nameID, "NASDAQ 100 TRUST");
        row.setDouble(valueID, 135.50);
        row = dataTable.addRow();
        row.setInt(idID, 7);
        row.setString(depotID, "Depot 1");
        row.setString(typeID, "F");
        row.setString(nameID, "NORDASIA.COM");
        row.setDouble(valueID, 435.23);
        row = dataTable.addRow();
        row.setInt(idID, 8);
        row.setString(depotID, "Depot 1");
        row.setString(typeID, "F");
        row.setString(nameID, "MERRILL LIIF WLD MINING A");
        row.setDouble(valueID, 1567.54);
        row = dataTable.addRow();
        row.setInt(idID, 9);
        row.setString(depotID, "Depot 1");
        row.setString(typeID, "F");
        row.setString(nameID, "SPAENGLER LIFE SCIENCE TRUST");
        row.setDouble(valueID, 545.45);
        row = dataTable.addRow();
        row.setInt(idID, 10);
        row.setString(depotID, "Depot 2");
        row.setString(typeID, "F");
        row.setString(nameID, "MMF (Most Miserable Fund)");
        row.setDouble(valueID, 12326.74);
        row = dataTable.addRow();
        row.setInt(idID, 10);
        row.setString(depotID, "Depot 3");
        row.setString(typeID, "S");
        row.setString(nameID, "CISCO SYSTEMS");
        row.setDouble(valueID, 5546.74);
        row = dataTable.addRow();
        row.setInt(idID, 11);
        row.setString(depotID, "Depot 3");
        row.setString(typeID, "S");
        row.setString(nameID, "WHEELING PITTS");
        row.setDouble(valueID, 2835.21);
        row = dataTable.addRow();
        row.setInt(idID, 12);
        row.setString(depotID, "Depot 3");
        row.setString(typeID, "S");
        row.setString(nameID, "NATIONAL VISION");
        row.setDouble(valueID, 7675.12);
        row = dataTable.addRow();
        row.setInt(idID, 13);
        row.setString(depotID, "Depot 3");
        row.setString(typeID, "S");
        row.setString(nameID, "COMTECH TELCOM");
        row.setDouble(valueID, 865.23);
        row = dataTable.addRow();
        row.setInt(idID, 14);
        row.setString(depotID, "Depot 3");
        row.setString(typeID, "S");
        row.setString(nameID, "IMMUNOMEDICS");
        row.setDouble(valueID, 876.21);
        row = dataTable.addRow();
        row.setInt(idID, 15);
        row.setString(depotID, "Depot 3");
        row.setString(typeID, "S");
        row.setString(nameID, "INTEL CORP");
        row.setDouble(valueID, 673.45);
        row = dataTable.addRow();
        row.setInt(idID, 16);
        row.setString(depotID, "Depot 3");
        row.setString(typeID, "F");
        row.setString(nameID, "Wasatch Ultra Growth");
        row.setDouble(valueID, 435.23);
        row = dataTable.addRow();
        row.setInt(idID, 17);
        row.setString(depotID, "Depot 3");
        row.setString(typeID, "F");
        row.setString(nameID, "Mutual Discovery A");
        row.setDouble(valueID, 1567.54);
        row = dataTable.addRow();
        row.setInt(idID, 18);
        row.setString(depotID, "Depot 3");
        row.setString(typeID, "F");
        row.setString(nameID, "Vanguard Global Equity");
        row.setDouble(valueID, 425.53);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "S");
        row.setString(nameID, "QUIRKS");
        row.setDouble(valueID, 3456.54);
        row = dataTable.addRow();
        row.setInt(idID, 2);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "S");
        row.setString(nameID, "ZOORO");
        row.setDouble(valueID, 455.25);
        row = dataTable.addRow();
        row.setInt(idID, 3);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "S");
        row.setString(nameID, "ROBOTRON");
        row.setDouble(valueID, 3545.37);
        row = dataTable.addRow();
        row.setInt(idID, 4);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "S");
        row.setString(nameID, "PARODENTRON");
        row.setDouble(valueID, 7567.76);
        row = dataTable.addRow();
        row.setInt(idID, 5);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "S");
        row.setString(nameID, "FORZA");
        row.setDouble(valueID, 3621.21);
        row = dataTable.addRow();
        row.setInt(idID, 5);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "S");
        row.setString(nameID, "CUPTEC");
        row.setDouble(valueID, 541.34);
        row = dataTable.addRow();
        row.setInt(idID, 6);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "ZOCKER.COM");
        row.setDouble(valueID, 521.12);
        row = dataTable.addRow();
        row.setInt(idID, 7);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "GAAGLI");
        row.setDouble(valueID, 5464.12);
        row = dataTable.addRow();
        row.setInt(idID, 8);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "HANSO INC");
        row.setDouble(valueID, 554.34);
        row = dataTable.addRow();
        row.setInt(idID, 9);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "LOCKER");
        row.setDouble(valueID, 145.32);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "KOCKEL");
        row.setDouble(valueID, 654.24);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "FARBIC");
        row.setDouble(valueID, 4564.34);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "HEGON");
        row.setDouble(valueID, 235.31);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "REHAKLES");
        row.setDouble(valueID, 1432.32);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "PATZIKI");
        row.setDouble(valueID, 324.12);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "GRINSO");
        row.setDouble(valueID, 228.53);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "GONZOLES");
        row.setDouble(valueID, 458.33);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "HASTENIX");
        row.setDouble(valueID, 4528.33);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "ALLESKAPUTT");
        row.setDouble(valueID, 556.54);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "MIES");
        row.setDouble(valueID, 764.45);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "MIESER SPEZIAL");
        row.setDouble(valueID, 438.26);
        row = dataTable.addRow();
        row.setInt(idID, 1);
        row.setString(depotID, "Depot 4");
        row.setString(typeID, "F");
        row.setString(nameID, "AMMIESESTEN");
        row.setDouble(valueID, 342.31);
    }

    /**
   * Performs the group block test for the given table.
   * @param testName The name of the test.
   * @param reportTable The table to test.
   */
    private void doGroupBlockTest(String testName, Table reportTable) {
        boolean blockOpen = false;
        boolean blockClosed = false;
        Iterator reportIterator = reportTable.iterator();
        while (reportIterator.hasNext()) {
            Row row = (Row) reportIterator.next();
            System.out.println(row);
            if (!blockOpen) {
                if (row.isStartsBlock()) {
                    blockOpen = true;
                    blockClosed = false;
                }
                if (blockClosed) {
                    if (!row.getType().equals("footer")) {
                        System.out.println(testName + " failed ;-(\n");
                        fail("A block close must be flowed by the opening of the next block opening");
                    }
                }
            } else {
                if (row.isEndsBlock()) {
                    blockOpen = false;
                    blockClosed = true;
                }
            }
        }
        System.out.println(testName + " succeeded!\n");
    }

    /**
   * Tests the report output.
   * @throws TableException In case a table access problem occured.
   */
    public void testGroupBlockWithoutFunctionsAndTitles() throws TableException {
        ReportTable reportTable = new ReportTable(dataTable);
        reportTable.addGroup(new ReportGroup("DEPOT", false, false, true, false, true));
        reportTable.addGroup(new ReportGroup("TYPE", false, false, true, false));
        reportTable.orderColumns(new String[] { "DEPOT", "TYPE", "ID", "NAME", "VALUE" });
        doGroupBlockTest("testGroupBlockWithoutFunctionsAndTitles", reportTable);
    }

    /**
   * Tests the report output.
   * @throws TableException In case a table access problem occured.
   */
    public void testGroupBlockWithoutFunctionsAndTitlesAndEnforcedResults() throws TableException {
        ReportTable reportTable = new ReportTable(dataTable);
        reportTable.addGroup(new ReportGroup("DEPOT", false, false, true, false, true).setEnforceResultRow(true));
        reportTable.addGroup(new ReportGroup("TYPE", false, false, true, false).setEnforceResultRow(true));
        reportTable.orderColumns(new String[] { "DEPOT", "TYPE", "ID", "NAME", "VALUE" });
        doGroupBlockTest("testGroupBlockWithoutFunctionsAndTitlesAndEnforcedResults", reportTable);
    }

    /**
   * Tests the report output.
   * @throws TableException In case a table access problem occured.
   */
    public void testGroupBlockWithoutTitleRow() throws TableException {
        ReportTable reportTable = new ReportTable(dataTable);
        ReportFunction f = new ReportFunction("NAME", new CountReportFunctionExecutor(), true, new String[] { "DEPOT", "TYPE" });
        f.setAlign(LayoutHints.RIGHT);
        reportTable.addFunction(f);
        reportTable.addFunction(new ReportFunction("VALUE", new SumReportFunctionExecutor(), true, new String[] { "DEPOT", "TYPE" }));
        reportTable.addGroup(new ReportGroup("DEPOT", false, false, true, false, true));
        reportTable.addGroup(new ReportGroup("TYPE", false, false, true, false));
        reportTable.orderColumns(new String[] { "DEPOT", "TYPE", "ID", "NAME", "VALUE" });
        doGroupBlockTest("testGroupBlockWithoutTitleRow", reportTable);
    }

    /**
   * Tests the report output.
   * @throws TableException In case a table access problem occured.
   */
    public void testGroupBlockWithTitleRows() throws TableException {
        ReportTable reportTable = new ReportTable(dataTable);
        ReportFunction f = new ReportFunction("NAME", new CountReportFunctionExecutor(), true, new String[] { "DEPOT", "TYPE" });
        f.setAlign(LayoutHints.RIGHT);
        reportTable.addFunction(f);
        reportTable.addFunction(new ReportFunction("VALUE", new SumReportFunctionExecutor(), true, new String[] { "DEPOT", "TYPE" }));
        reportTable.addGroup(new ReportGroup("DEPOT", true, false, true, false, true));
        reportTable.addGroup(new ReportGroup("TYPE", false, false, true, false));
        reportTable.orderColumns(new String[] { "DEPOT", "TYPE", "ID", "NAME", "VALUE" });
        doGroupBlockTest("testGroupBlockWithTitleRows", reportTable);
    }

    /**
   * Tests the report output.
   * @throws TableException In case a table access problem occured.
   */
    public void testGroupBlockWithTitleRowResults() throws TableException {
        ReportTable reportTable = new ReportTable(dataTable);
        ReportFunction f = new ReportFunction("NAME", new CountReportFunctionExecutor(), true, new String[] { "DEPOT", "TYPE" });
        f.setAlign(LayoutHints.RIGHT);
        reportTable.addFunction(f);
        reportTable.addFunction(new ReportFunction("VALUE", new SumReportFunctionExecutor(), true, new String[] { "DEPOT", "TYPE" }));
        reportTable.addGroup(new ReportGroup("DEPOT", true, true, true, false, true));
        reportTable.addGroup(new ReportGroup("TYPE", false, false, true, false));
        reportTable.orderColumns(new String[] { "DEPOT", "TYPE", "ID", "NAME", "VALUE" });
        doGroupBlockTest("testGroupBlockWithTitleRows", reportTable);
    }

    /**
   * Tests the report output.
   * @throws TableException In case a table access problem occured.
   */
    public void testGroupBlockWithTitleRows2() throws TableException {
        ReportTable reportTable = new ReportTable(dataTable);
        ReportFunction f = new ReportFunction("NAME", new CountReportFunctionExecutor(), true, new String[] { "DEPOT", "TYPE" });
        f.setAlign(LayoutHints.RIGHT);
        reportTable.addFunction(f);
        reportTable.addFunction(new ReportFunction("VALUE", new SumReportFunctionExecutor(), true, new String[] { "DEPOT", "TYPE" }));
        reportTable.addGroup(new ReportGroup("DEPOT", true, true, true, false, true));
        reportTable.addGroup(new ReportGroup("TYPE", true, false, true, false));
        reportTable.orderColumns(new String[] { "DEPOT", "TYPE", "ID", "NAME", "VALUE" });
        doGroupBlockTest("testGroupBlockWithTitleRows2", reportTable);
    }

    /**
   * Tests the report output.
   * @throws TableException In case a table access problem occured.
   */
    public void testGroupBlockWithTitleRowResults2() throws TableException {
        ReportTable reportTable = new ReportTable(dataTable);
        ReportFunction f = new ReportFunction("NAME", new CountReportFunctionExecutor(), true, new String[] { "DEPOT", "TYPE" });
        f.setAlign(LayoutHints.RIGHT);
        reportTable.addFunction(f);
        reportTable.addFunction(new ReportFunction("VALUE", new SumReportFunctionExecutor(), true, new String[] { "DEPOT", "TYPE" }));
        reportTable.addGroup(new ReportGroup("DEPOT", true, true, true, false, true));
        reportTable.addGroup(new ReportGroup("TYPE", true, true, true, false));
        reportTable.orderColumns(new String[] { "DEPOT", "TYPE", "ID", "NAME", "VALUE" });
        doGroupBlockTest("testGroupBlockWithTitleRows2", reportTable);
    }
}
