package jgnash.ui.report.compiled;

import java.util.ArrayList;
import java.util.List;
import jgnash.engine.AccountGroup;

/**
 * Balance Sheet Report
 *
 * @author Craig Cavanaugh
 * @version $Id: BalanceSheetReport.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
public class BalanceSheetReport extends AbstractSumByTypeReport {

    @Override
    protected List<AccountGroup> getAccountGroups() {
        List<AccountGroup> groups = new ArrayList<AccountGroup>();
        groups.add(AccountGroup.ASSET);
        groups.add(AccountGroup.INVEST);
        groups.add(AccountGroup.LIABILITY);
        groups.add(AccountGroup.EQUITY);
        return groups;
    }

    /**
     * Returns the name of the report
     *
     * @return report name
     */
    @Override
    public String getReportName() {
        return rb.getString("Title.BalanceSheet");
    }

    /**
     * Returns the legend for the grand total
     *
     * @return report name
     */
    @Override
    public String getGrandTotalLegend() {
        return rb.getString("Word.Difference");
    }

    @Override
    public String getGroupFooterLabel() {
        return rb.getString("Word.Subtotal");
    }
}
