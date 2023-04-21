package org.ujac.web.demo.actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.ujac.print.DocumentPrinter;
import org.ujac.util.UjacException;
import org.ujac.util.table.Column;
import org.ujac.util.table.CountFunctionExecutor;
import org.ujac.util.table.DataTable;
import org.ujac.util.table.LayoutHints;
import org.ujac.util.table.ReportFunction;
import org.ujac.util.table.ReportGroup;
import org.ujac.util.table.ReportTable;
import org.ujac.util.table.Row;
import org.ujac.util.table.SumFunctionExecutor;
import org.ujac.util.table.Table;
import org.ujac.util.table.TableConstants;
import org.ujac.web.demo.actions.BaseTestCaseAction;

/**
 * Name: ReportTest2Action<br>
 * Description: Another test case for report output.
 * @author lauerc
 */
public class ReportTest2Action extends BaseReportTestAction {

    /**
   * Constructs a ReportTest2Action instance with no specific attributes.
   */
    public ReportTest2Action() {
        super("report-test2");
    }

    /**
   * Gets a description for the test case.
   * @return The description for the test case.
   */
    public String getDescription() {
        return "Tests the output of report tables with enforced result rows.";
    }

    /**
   * Gets the template file to use.
   * @param request The action context.
   * @return The template file to use.
   * @exception UjacException In case an UJAC related problem occurred.
   * @exception IOException In case an I/O problem occurred.
   */
    public Map getProperties(HttpServletRequest request) throws UjacException, IOException {
        Table dataTable = getDataTable();
        ReportTable reportTable = new ReportTable(dataTable);
        ReportFunction f = new ReportFunction("NAME", new CountFunctionExecutor(), true, new String[] { "DEPOT" });
        f.setAlign(LayoutHints.RIGHT);
        reportTable.addFunction(f);
        reportTable.addFunction(new ReportFunction("VALUE", new SumFunctionExecutor(), true, new String[] { "DEPOT" }));
        reportTable.addGroup(new ReportGroup("DEPOT", true, true, true, false, true).setEnforceResultRow(true));
        reportTable.addGroup(new ReportGroup("TYPE", false, false, true, false).setEnforceResultRow(true));
        reportTable.orderColumns(new String[] { "DEPOT", "TYPE", "ID", "NAME", "VALUE" });
        Map documentProperties = new HashMap();
        documentProperties.put("table", reportTable);
        return documentProperties;
    }
}
