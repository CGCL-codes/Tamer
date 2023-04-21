package jgnash.ui.report.compiled;

import java.util.ArrayList;
import java.util.List;
import jgnash.engine.AccountGroup;

/**
 * Net Worth Report
 *
 * @author Craig Cavanaugh
 * @version $Id: NetWorthReport.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
public class NetWorthReport extends AbstractSumByTypeReport {

    @Override
    protected List<AccountGroup> getAccountGroups() {
        List<AccountGroup> groups = new ArrayList<AccountGroup>();
        groups.add(AccountGroup.ASSET);
        groups.add(AccountGroup.INVEST);
        groups.add(AccountGroup.LIABILITY);
        return groups;
    }

    @Override
    public String getReportName() {
        return rb.getString("Word.NetWorth");
    }

    /**
     * Returns the legend for the grand total
     *
     * @return report name
     */
    @Override
    public String getGrandTotalLegend() {
        return getReportName();
    }

    @Override
    public String getGroupFooterLabel() {
        return rb.getString("Word.Subtotal");
    }
}
