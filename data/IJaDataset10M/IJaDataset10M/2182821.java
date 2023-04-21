package edu.stanford.hci.flowmap.prefuse.render;

import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.model.UserOptions;

/**
 * Abstract class that scales a flow from its real width to a width suitable for display.
 * 
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public abstract class FlowScale {

    protected double displayMin, displayMax;

    protected UserOptions userOptions;

    protected QueryRecord queryRecord;

    public FlowScale(UserOptions userOptions, QueryRecord qRecord) {
        this.userOptions = userOptions;
        this.queryRecord = qRecord;
    }

    public double getDisplayWidth(double flowCurr, String columnName) {
        displayMin = userOptions.getDouble(UserOptions.MIN_DISPLAY_WIDTH);
        displayMax = userOptions.getDouble(UserOptions.MAX_DISPLAY_WIDTH);
        double width = displayMin + (f(flowCurr, columnName) * (displayMax - displayMin));
        if ((width < 0) || Double.isNaN(width) || Double.isInfinite(width)) width = displayMin;
        return width;
    }

    protected abstract double f(double flowCurr, String columnName);

    public static class Linear extends FlowScale {

        public Linear(UserOptions userOptions, QueryRecord qRecord) {
            super(userOptions, qRecord);
        }

        protected double f(double flowCurr, String columnName) {
            return flowCurr / queryRecord.getTotalValue(columnName);
        }
    }

    public static class Log extends FlowScale {

        private double biggestLog;

        public Log(UserOptions userOptions, QueryRecord qRecord) {
            super(userOptions, qRecord);
        }

        protected double f(double flowCurr, String columnName) {
            double flowMin, flowMax;
            flowMin = queryRecord.getMinValue(columnName);
            flowMax = queryRecord.getTotalValue(columnName);
            return Math.log(flowCurr / flowMin) / Math.log(flowMax / flowMin);
        }
    }

    public static class Poly extends FlowScale {

        private double exponent;

        public Poly(UserOptions userOptions, QueryRecord qRecord) {
            super(userOptions, qRecord);
        }

        protected double f(double flowCurr, String columnName) {
            double flowMin, flowMax;
            flowMin = queryRecord.getMinValue(columnName);
            flowMax = queryRecord.getTotalValue(columnName);
            double flowMagnitude = flowMax / flowMin;
            double displayMagnitude = displayMax / displayMin;
            exponent = Math.log(displayMagnitude) / Math.log(flowMagnitude);
            if (exponent > 1) exponent = 1;
            return Math.pow(flowCurr / flowMax, exponent);
        }
    }
}
