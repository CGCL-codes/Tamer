package au.edu.swin.jmetric.model;

import javax.swing.table.*;
import javax.swing.ImageIcon;
import java.util.*;
import java.io.*;
import au.edu.swin.jmetric.ui.util.JStatsTableModel;
import au.edu.swin.jmetric.ui.util.Warning;

public abstract interface DisplaysMetrics {

    public Vector getMetrics();

    public boolean hasMetricsForExport();

    public String getMetricsForExport();

    public void exportMetrics(DataOutputStream out);

    public TableModel getChildTableModel();

    public ImageIcon getIcon();

    public JStatsTableModel getStatsTableInformation();

    public double getMetricValue(int index);

    public String getMetricName(int index);

    public int getMetricsCount();

    public double getExpectedMetricsValue(int index);

    public String getChildMetricName(int index);

    public Vector getChildTableData();

    public Vector getChildTableHeadings();

    public double[] getChildMetricValue(int index);

    public String[] getDrillMetrics();

    public TableModel getDrillMetric(int index);

    public Warning getDrillWarning(int index);
}
