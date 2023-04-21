package com.matrixbi.adansi.client.report.ranking;

import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.matrixbi.adansi.client.report.widget.ReportChart;

public class ReportChartRanking extends ReportChart {

    @Override
    public Widget asWidget() {
        return new HTML("");
    }

    @Override
    protected void handleEvent(AppEvent event) {
    }

    @Override
    protected ChartModel getChartModel() {
        return null;
    }
}
