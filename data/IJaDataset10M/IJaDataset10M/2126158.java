package com.tcmj.pm.conflicts.jfreechart;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Administrator
 */
public class BarToolTipGenerator extends StandardXYToolTipGenerator {

    public BarToolTipGenerator(String formatString, DateFormat xFormat, DateFormat yFormat) {
        super(formatString, xFormat, yFormat);
    }

    public BarToolTipGenerator(String formatString, DateFormat xFormat, NumberFormat yFormat) {
        super(formatString, xFormat, yFormat);
    }

    public BarToolTipGenerator() {
        super("{1}: Start=({2,date,yyyy-MM-dd HH:mm}) End=({3,date,yyyy-MM-dd HH:mm})   {4,number,percent}", new SimpleDateFormat("yyyyMMdd"), new SimpleDateFormat("yyyyMMdd"));
    }

    @Override
    public String generateToolTip(XYDataset dataset, int series, int item) {
        return generateString(dataset, series, item);
    }

    public String generateString(XYDataset dataset, int series, int item) {
        String result = null;
        Object[] items = createItemArray(dataset, series, item);
        result = MessageFormat.format(getFormatString(), items);
        return result;
    }

    protected Object[] createItemArray(XYDataset dataset, int series, int item) {
        Object[] result = new Object[5];
        XYTaskDataset taskdataset = (XYTaskDataset) dataset;
        TaskSeriesCollection tcol = taskdataset.getTasks();
        TaskSeries taskseries = tcol.getSeries(series);
        Task task = taskseries.get(item);
        result[0] = dataset.getSeriesKey(series).toString();
        result[1] = task.getDescription();
        result[2] = task.getDuration().getStart();
        result[3] = task.getDuration().getEnd();
        result[4] = task.getPercentComplete();
        return result;
    }
}
