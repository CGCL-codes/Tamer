package org.rjam.gui.analysis;

import org.rjam.gui.DecoratedSplitPane;
import org.rjam.gui.analysis.chart.GcChart;
import org.rjam.gui.api.IChartContainer;
import org.rjam.gui.beans.Field;

public class GcAnalysisPane extends ReportPane {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "GcAnalysis";

    public static final String DESC = "An analysis of the Applciation heap management";

    public GcAnalysisPane() {
        super(NAME, NAME, DESC);
    }

    @Override
    public IChartContainer getChartContainer() {
        if (this.chartContainer == null) {
            this.chartContainer = new DecoratedSplitPane(new GcChart());
        }
        return this.chartContainer;
    }

    @Override
    public String[] getFields() {
        String[] tmp = super.getFields();
        String[] ret = new String[tmp.length + 1];
        for (int idx = 0; idx < tmp.length; idx++) {
            ret[idx] = tmp[idx];
        }
        ret[tmp.length] = Field.FLD_NAME_GC_TIME;
        return ret;
    }
}
