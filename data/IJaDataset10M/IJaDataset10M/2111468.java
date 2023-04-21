package org.posterita.decorator;

import org.posterita.beans.POSHistoryBean;

public class HistoryCsvExportWrapper extends HistoryWrapper {

    public String getLinkPartnerName() {
        POSHistoryBean bean = (POSHistoryBean) this.getCurrentRowObject();
        return bean.getPartnerName();
    }
}
