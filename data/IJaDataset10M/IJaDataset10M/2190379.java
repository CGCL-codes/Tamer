package org.rjam.gui.analysis;

import org.rjam.gui.DecoratedSplitPane;
import org.rjam.gui.analysis.chart.ThreadGroupChart;
import org.rjam.gui.api.IChartContainer;
import org.rjam.gui.beans.Row;

public class ThreadGroupAnalysisPane extends ReportPane {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "Thread Group";

    public static final String QUERY_TYPE = "ThreadGroup";

    public static final String DESC = "List method exec by thread";

    public ThreadGroupAnalysisPane() {
        super(NAME, QUERY_TYPE, DESC);
    }

    @Override
    public IChartContainer getChartContainer() {
        if (this.chartContainer == null) {
            this.chartContainer = new DecoratedSplitPane(new ThreadGroupChart());
        }
        return this.chartContainer;
    }

    @Override
    public void addRow(Row row, String[] order) {
        super.addRow(row, order);
    }
}
