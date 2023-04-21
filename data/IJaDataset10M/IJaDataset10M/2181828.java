package edu.ucsd.ncmir.jinx.events.gui.object_info;

import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;
import edu.ucsd.ncmir.jinx.gui.object_info.JxObjectInfo;
import edu.ucsd.ncmir.jinx.gui.object_info.JxOutliers;

/**
 *
 * @author spl
 */
public class JxSetOutlierThresholdEvent extends AsynchronousEvent {

    /** Creates a new instance of JxSetOutlierThresholdEvent */
    public JxSetOutlierThresholdEvent(JxObjectInfo object_info) {
        super(object_info);
    }

    private JxOutliers.DataType data_type;

    private double minimum;

    private double maximum;

    public void setDataType(JxOutliers.DataType data_type) {
        this.data_type = data_type;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public JxOutliers.DataType getDataType() {
        return this.data_type;
    }

    public double getMinimum() {
        return this.minimum;
    }

    public double getMaximum() {
        return this.maximum;
    }
}
