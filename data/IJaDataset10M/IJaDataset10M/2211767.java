package org.vikamine.swing.discretization;

/**
 * @author lemmerich
 * @date 04/2009
 */
public class BarInformation {

    double positives;

    double size;

    double maxSize;

    /** the minimum value of the interval */
    double minInterval;

    /** the maximum value of the interval */
    double maxInterval;

    double completeIntervalWidth;

    public BarInformation(double positives, double size, double maxSize, double minInterval, double maxInterval, double completeIntervalWidth) {
        super();
        this.positives = positives;
        this.size = size;
        this.maxSize = maxSize;
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        this.completeIntervalWidth = completeIntervalWidth;
    }

    public double getPositives() {
        return positives;
    }

    public void setPositives(double positives) {
        this.positives = positives;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(double maxSize) {
        this.maxSize = maxSize;
    }

    public double getMinInterval() {
        return minInterval;
    }

    public void setMinInterval(double minInterval) {
        this.minInterval = minInterval;
    }

    public double getMaxInterval() {
        return maxInterval;
    }

    public void setMaxInterval(double maxInterval) {
        this.maxInterval = maxInterval;
    }

    public double getCompleteIntervalWidth() {
        return completeIntervalWidth;
    }

    public void setCompleteIntervalWidth(double completeIntervalWidth) {
        this.completeIntervalWidth = completeIntervalWidth;
    }

    public double getWidthPercentage() {
        return (maxInterval - minInterval) / getCompleteIntervalWidth();
    }
}
