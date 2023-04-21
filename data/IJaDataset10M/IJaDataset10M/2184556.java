package dakside.hacc.modules.accounting.reports;

import dakside.hacc.reports.XARReport;

/**
 * Income Statement Report (renderer)
 * @author takaji
 */
public class IncomeStatementReport extends XARReport {

    private IncomeStatementReport() {
        super("income_statement_template", new IncomeStatementDataRetriever());
    }

    private static IncomeStatementReport instance = null;

    public static synchronized IncomeStatementReport getInstance() {
        if (instance == null) {
            instance = new IncomeStatementReport();
        }
        return instance;
    }
}
