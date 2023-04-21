package com.openthinks.woms.report;

import java.util.Collection;
import com.openthinks.woms.account.Account;

/**
 * Model of a style report
 * 
 * @author Zhang Junlong
 * 
 */
public class StyleReport {

    private Account account;

    private Collection<StyleReportItem> styleReportItems;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Collection<StyleReportItem> getStyleReportItems() {
        return styleReportItems;
    }

    public void setStyleReportItems(Collection<StyleReportItem> styleReportItems) {
        this.styleReportItems = styleReportItems;
    }
}
