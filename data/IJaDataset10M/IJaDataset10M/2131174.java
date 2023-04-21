package com.mockturtlesolutions.snifflib.graphics;

import java.util.Vector;
import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;

public class AxesRangeSelection {

    private Vector axesRangeSelectionListeners;

    private DblMatrix breakpoint;

    private SLAxes axes;

    private int dimension;

    public AxesRangeSelection(SLAxes ax, int dim) {
        this.axes = ax;
        this.axesRangeSelectionListeners = new Vector();
        this.dimension = dim;
        this.breakpoint = this.getLimits();
        this.breakpoint = this.breakpoint.getDblAt(1).minus(this.breakpoint.getDblAt(0)).divideBy(2.0);
    }

    public int getDimension() {
        return (this.dimension);
    }

    public SLAxes getAxes() {
        return (this.axes);
    }

    public DblMatrix getLimits() {
        DblMatrix limits = null;
        switch(this.dimension) {
            case (Axes.X_AXIS):
                {
                    limits = this.axes.getXLim();
                    break;
                }
            case (Axes.Y_AXIS):
                {
                    limits = this.axes.getYLim();
                    break;
                }
            case (Axes.Z_AXIS):
                {
                    limits = this.axes.getZLim();
                    break;
                }
            default:
                {
                    throw new RuntimeException("Unrecognized axis " + this.dimension + ".");
                }
        }
        return (limits);
    }

    public void setValue(DblMatrix val) {
        DblMatrix limits = this.getLimits();
        if (DblMatrix.test(this.breakpoint.lt(limits.getDblAt(0)))) {
            throw new RuntimeException("Selected value is less than the lower limit.");
        }
        if (DblMatrix.test(this.breakpoint.gt(limits.getDblAt(1)))) {
            throw new RuntimeException("Selected value is greater than the upper limit.");
        }
        this.breakpoint = val;
    }

    public DblMatrix getValue() {
        return (this.breakpoint);
    }

    public void fireAxesRangeSelectionChanged(AxesRangeSelectionEvent ev) {
        for (int i = 0; i < this.axesRangeSelectionListeners.size(); i++) {
            AxesRangeSelectionListener l = (AxesRangeSelectionListener) this.axesRangeSelectionListeners.get(i);
            l.stateChanged(ev);
        }
    }

    public void addAxesRangeSelectionListener(AxesRangeSelectionListener l) {
        this.axesRangeSelectionListeners.add(l);
    }

    public void removeAxesRangeSelectionListener(AxesRangeSelectionListener l) {
        this.axesRangeSelectionListeners.remove(l);
    }
}
