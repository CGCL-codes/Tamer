package org.rjam.gui.analysis.subchart;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.statistics.Regression;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.rjam.gui.beans.Comment;
import org.rjam.gui.beans.Field;
import org.rjam.gui.beans.Row;
import org.rjam.gui.beans.Value;

/**
 * 
 * @author Tony Bringardner
 * 
 * This class is not used.  The logic needs to be completed. 
 *
 */
public class HeapRunningAveView extends SubChartView {

    private XYSeries data = new XYSeries("GcData");

    private XYDataset dataSet = new XYSeriesCollection(data);

    private class Slope {

        public static final int NONE = 0;

        public static final int OK = 1;

        public static final int WARN = 2;

        double lastHeap = 0.0;

        int comment = NONE;

        public double init;

        public double slope = 0.0;
    }

    private Map<String, Slope> slopes = new HashMap<String, Slope>();

    public HeapRunningAveView() {
        super("Heap Trend", Field.FLD_NAME_HEAP_TREND, 1, false);
    }

    @Override
    public void addRow(Row row, RegularTimePeriod period, String key) {
        try {
            double heap = row.getHeapPerc();
            double time = data.getItemCount() + 1;
            data.add(time, heap);
            if (data.getItemCount() > 1) {
                double[] coefficients = Regression.getPowerRegression(this.dataSet, 0);
                Function2D curve = new LineFunction2D(coefficients[0], coefficients[1]);
                double x = curve.getValue(time);
                Value value = new Value(Field.FLD_HEAP_TREND, new Double(x));
                row.add(value);
            }
        } catch (Throwable e) {
            logError("Error processing Row", e);
        }
        super.addRow(row, period, key);
    }

    @SuppressWarnings("unused")
    private void generateComment() {
        StringBuffer txt = new StringBuffer();
        int type = Slope.NONE;
        for (Iterator<String> it = slopes.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            Slope slope = slopes.get(key);
            type = Math.max(type, slope.comment);
            switch(slope.comment) {
                case Slope.OK:
                    if (slope.lastHeap <= slope.init) {
                        txt.append("Current Heap size for " + formatKey(key) + " is less that or equal to the initial heap size. This is an indication that there are no memory leaks.\n");
                    } else {
                        txt.append("The slope of the average variance for " + formatKey(key) + " (" + slope.slope + ") is less than or equal to zero(0).  This is an indication that there are no memory leaks.\n");
                    }
                    break;
                case Slope.WARN:
                    txt.append("The current average variance for " + formatKey(key) + " (" + slope.slope + ") is an indication that there might be a memory leak.\n");
                    break;
            }
        }
        if (type == Slope.WARN) {
            updateComment(Comment.WARNING, txt.toString());
        } else {
            updateComment(Comment.COMMENT, txt.toString());
        }
    }

    private String formatKey(String key) {
        String ret = key;
        String[] parts = key.split("~");
        if (parts.length > 1) {
            ret = parts[0] + "(" + parts[1] + ")";
        }
        return ret;
    }

    @Override
    public void clear() {
        super.clear();
        data = new XYSeries("GcData");
        dataSet = new XYSeriesCollection(data);
        slopes = new HashMap<String, Slope>();
    }
}
