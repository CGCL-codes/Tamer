package au.edu.swin.jmetric.ui.util;

import kersoft.chart.*;
import java.awt.event.*;
import au.edu.swin.jmetric.util.Stats;

/**
* This is the frequency chart for the data from the majority of the charts.
*/
public class JMetricChartModel extends DefaultChartModel {

    /**
    * This constructs the chart model based on the data grid from the 
    * Stats.
    *
    * @param stats  The Stats to get the data grid from.
    */
    public JMetricChartModel(Stats stats) {
        if (stats != null) setDataGrid(stats.getDataGrid());
    }
}
