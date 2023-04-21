package com.tcmj.pm.conflicts.jfreechart;

import java.text.MessageFormat;
import java.text.NumberFormat;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Administrator
 */
public class MyXYItemLabelGenerator implements XYItemLabelGenerator {

    private String formatString = "{4,number,percent} \n{1}";

    public String generateLabel(XYDataset dataset, int series, int item) {
        return generateLabelString(dataset, series, item);
    }

    public String generateLabelString(XYDataset dataset, int series, int item) {
        String result = null;
        Object[] items = createItemArray(dataset, series, item);
        result = MessageFormat.format(this.formatString, items);
        return result;
    }

    protected Object[] createItemArray(XYDataset dataset, int series, int item) {
        XYTaskDataset taskdataset = (XYTaskDataset) dataset;
        TaskSeriesCollection tcol = taskdataset.getTasks();
        TaskSeries taskseries = tcol.getSeries(series);
        Task task = taskseries.get(item);
        Object[] result = new Object[5];
        result[0] = dataset.getSeriesKey(series).toString();
        result[1] = task.getDescription();
        result[2] = task.getDuration().getStart();
        result[3] = task.getDuration().getEnd();
        result[4] = task.getPercentComplete();
        return result;
    }
}
