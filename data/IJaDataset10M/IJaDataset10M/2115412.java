package net.sourceforge.eclipsetrader.charts.events;

import net.sourceforge.eclipsetrader.core.db.ChartTab;
import org.eclipse.jface.viewers.ISelection;

public class TabSelection implements ISelection {

    private ChartTab chartTab;

    public TabSelection(ChartTab chartTab) {
        this.chartTab = chartTab;
    }

    public boolean isEmpty() {
        return chartTab == null;
    }

    public ChartTab getChartTab() {
        return chartTab;
    }
}
