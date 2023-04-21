package net.sourceforge.webcompmath.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sourceforge.webcompmath.data.Constant;
import net.sourceforge.webcompmath.data.NumUtils;
import net.sourceforge.webcompmath.data.Parser;
import net.sourceforge.webcompmath.data.QueueableValue;
import net.sourceforge.webcompmath.data.QueueableValueUpdateEvent;
import net.sourceforge.webcompmath.data.QueueableValueUpdateListener;
import net.sourceforge.webcompmath.data.Value;
import net.sourceforge.webcompmath.data.Variable;

/**
 * A VariableJSlider is a slider (implemented as a JSlider) whose position
 * represents the value of an associated variable. The range of values
 * represented by the slider is given by a pair of Value objects. They can be
 * specified in the constructor or later with the setMin and setMax methods. A
 * VariableJSlider has an associated variable that represents the value of the
 * slider. Note that the value of the variable can change only when the
 * setInput() or checkInput() method is called. If you want the value of the
 * variable to track the position of the slider as it is is manipulated by the
 * user, add the slider to a Controller and set the Controller as the Slider's
 * "onUserAction" property. This allows other objects that depend on the values
 * of the slider to be recomputed by the controller when the value changes, as
 * long as they are also registered with the Controller.
 * 
 * <p>
 * Some points to note:
 * <p>
 * 1) setVal() can set a value outside the range from min to max, which will
 * persist until the next time checkInput() or setVal() is called again.
 * <p>
 * 2) If the value of min or max changes, the value of this variable will not
 * change EXCEPT that it is clamped to the range between min and max.
 * <p>
 * 3) Min does not have to be less than max.
 * <p>
 * 4) The checkInput() routine only sets the needValueCheck flag to true. (The
 * setVal() and getVal() routines both set this flag to false.) This "lazy
 * evaluation" is used because checkInput() can't compute the new value itself.
 * (The max and min might depend on Values that are themselves about to change
 * when some other object's checkInput() mehtod is called.)
 * <p>
 * 5) getVal() returns the current value, as stored in the variable, UNLESS
 * needValueCheck is true. In that case, it recomputes the value first.
 * getSerialNumber() works similarly.
 * <p>
 * 6) A VariableJSlider never throws WCMErrors. If an error occurs when min or
 * max is evaluated, the value of the variable associated with this
 * VariableJSlider becomes undefined. (The point is, it doesn't generate any
 * errors of its own. The error would be caused by other InputObjects which
 * should throw their own errors when their checkInput() methods are called.)
 * <p>
 * You can also display value labels for the slider by using
 * <code> setPaintLabels(true)</code>. This labels the two ends and the middle
 * of the slider. If you want more labels or you want tick marks, you will need
 * to dig into using the JSlider methods directly.
 */
public class VariableJSlider extends JSlider implements InputObject, Tieable, Value, AdjustmentListener, QueueableValueUpdateListener {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 3689066257433767993L;

    /**
	 * The variable associated with this VariableJSlider. VS is a nested private
	 * class, defined below.
	 */
    protected VS variable;

    /**
	 * The Values that specify the range of values represented by the slider.
	 * min does not have to be less than max.
	 */
    protected Value min;

    /**
	 * The max value.
	 */
    protected Value max;

    private Controller onUserAction;

    /**
	 * If this is true, then the value of the variable associated with this
	 * slider is an integer. Furthermore, the number of intervals on the slider
	 * is set to be the same as the range of possible values (unless this range
	 * is too big).
	 */
    protected boolean integerValued;

    /**
	 * The number of possible value of the slider (Unless integerValued is
	 * true.)
	 */
    protected int intervals;

    /**
	 * The number of possible values of the slider, represented as a Value.
	 */
    protected Value intervalsValue;

    /**
	 * This increases every time the value of the variable changes.
	 */
    protected long serialNumber;

    /**
	 * This is set to true when checkInput() is called to indicate that the min
	 * and max values must be checked the next time getVal() is called.
	 */
    protected boolean needsValueCheck;

    /**
	 * This is the position of the slider the last time getVal() or setVal() was
	 * called. It is used to check whether the user has repositioned the slider.
	 */
    protected int oldPosition;

    /**
	 * The value found for min the last time checkInput() was called.
	 */
    protected double minVal = Double.NaN;

    /**
	 * The value found for max the last time checkInput() was called.
	 */
    protected double maxVal;

    private Hashtable<Integer, Component> labelTable;

    private Integer zero;

    private Integer intervalsInt;

    private Integer halfIntervalsInt;

    private int sliderLabelOffset = 0;

    private double oldMinval = -5.0;

    private double oldMaxval = 5.0;

    /**
	 * Create a horizontal variable slider with no name and with a default value
	 * range of -5 to 5.
	 */
    public VariableJSlider() {
        this(null, null, null, null);
    }

    /**
	 * Create a horizontal variable slider with no name and with the specified
	 * range of values. If min is null, a default value -5 is used. If max is
	 * null, a default value 5 is used.
	 * 
	 * @param min
	 *            min value for range
	 * @param max
	 *            max value for range
	 */
    public VariableJSlider(Value min, Value max) {
        this(null, min, max, null);
    }

    /**
	 * Create a horizontal variable slider with the given name and range of
	 * values, and register it with the given parser (but only if both name and
	 * p are non-null). If min is null, a default value -5 is used. If max is
	 * null, a default value 5 is used.
	 * 
	 * @param name
	 *            name of slider
	 * @param min
	 *            min value for range
	 * @param max
	 *            max value for range
	 * @param p
	 *            parser to use
	 */
    public VariableJSlider(String name, Value min, Value max, Parser p) {
        this(name, min, max, p, 1000, JSlider.HORIZONTAL);
    }

    /**
	 * Create a variable slider with the given name and range of values, and
	 * register it with the given parser (but only if both name and p are
	 * non-null). The "intervals" parameter specifes how many different
	 * positions there are on the slider. (The value of the slider ranges from 0
	 * to intervals.) If intervals is <= 0, it will be set to 1000. If it is
	 * between 1 and 9, it will be set to 10. The orientation must be either
	 * slider.HORIZONTAL or slider.VERTICAL. It specifies whether this is a
	 * horizontal or vertical slider. If min is null, a default value -5 is
	 * used. If max is null, a default value 5 is used.
	 * 
	 * @param name
	 *            name for this VariableJSlider.
	 * @param min
	 *            minimum value for slider.
	 * @param max
	 *            maximum value for slider.
	 * @param p
	 *            register VariableJSlider with this Parser.
	 * @param intervals
	 *            discrete positions on slider.
	 * @param orientation
	 *            slider.HORIZONTAL or slider.VERTICAL.
	 */
    @SuppressWarnings("unchecked")
    public VariableJSlider(String name, Value min, Value max, Parser p, int intervals, int orientation) {
        super(orientation);
        if (intervals <= 0) {
            intervals = 1000;
        }
        if (intervals <= 10) {
            intervals = 10;
        }
        this.intervals = intervals;
        setMinimum(0);
        setMaximum(intervals);
        setValue(intervals / 2);
        setBackground(Color.lightGray);
        setMin(min);
        setMax(max);
        variable = new VS(name);
        if (name != null) {
            super.setName(name);
        }
        if (p != null && name != null) {
            p.add(variable);
        }
        needsValueCheck = true;
        oldPosition = -1;
        getVal();
    }

    /**
	 * Set the name of the associated variable. You shouldn't do this if it has
	 * been added to a parser. If name is non-null, then the name of this
	 * Component is also set to the specified name.
	 * 
	 * @param name
	 *            name of the variable
	 */
    @Override
    public void setName(String name) {
        variable.setName(name);
        if (name != null) {
            super.setName(name);
        }
    }

    /**
	 * A convenience method that registers this VariableJSlider's variable with
	 * p (but only if both p and the name of the variable are non-null).
	 * 
	 * @param p
	 *            parser to use
	 */
    public void addTo(Parser p) {
        if (p != null && variable.getName() != null) {
            p.add(variable);
        }
    }

    /**
	 * Return the variable associated with this VariableJSlider.
	 * 
	 * @return the associated variable
	 */
    public Variable getVariable() {
        return variable;
    }

    /**
	 * If set to true, restrict the values of the variable associated with this
	 * slider to be integers. Furthermore, the number of intervals on the slider
	 * will be set to be the same as the size of the range from min to max
	 * (unless this range is too big). The setVal() method can still set the
	 * value of the variable to be a non-integer.
	 * 
	 * @param b
	 *            true or false
	 */
    public void setIntegerValued(boolean b) {
        integerValued = b;
        if (b && !Double.isNaN(minVal) && !Double.isNaN(maxVal)) {
            checkIntegerLimits(minVal, maxVal);
        }
        needsValueCheck = true;
    }

    /**
	 * Return a boolean which is true if the VariableJSlider restricts ranges of
	 * values to integers, false otherwise.
	 * 
	 * @return true or false
	 */
    public boolean getIntegerValued() {
        return integerValued;
    }

    /**
	 * Set the value that the variable has when the slider is at the left (or
	 * bottom) of the slider. If v is null, -5 is used as the default value.
	 * 
	 * @param v
	 *            value to set
	 */
    public void setMin(Value v) {
        if (min instanceof QueueableValue) {
            QueueableValue qv = (QueueableValue) min;
            qv.removeQueueableValueUpdateListener(this);
        }
        min = (v == null) ? new Constant(-5) : v;
        if (min instanceof QueueableValue) {
            QueueableValue qv = (QueueableValue) min;
            qv.addQueueableValueUpdateListener(this);
        }
        makeLabels();
    }

    /**
	 * Set the value that the variable has when the slider is at the right (or
	 * top) of the slider. If v is null, 5 is used as the default value.
	 * 
	 * @param v
	 *            value to set
	 */
    public void setMax(Value v) {
        if (max instanceof QueueableValue) {
            QueueableValue qv = (QueueableValue) max;
            qv.removeQueueableValueUpdateListener(this);
        }
        max = (v == null) ? new Constant(5) : v;
        if (max instanceof QueueableValue) {
            QueueableValue qv = (QueueableValue) max;
            qv.addQueueableValueUpdateListener(this);
        }
        makeLabels();
    }

    /**
	 * Get the Value object that gives the value of the variable when the slider
	 * is at the left (or bottom) of the slider. The Value is always non-null.
	 * 
	 * @return value
	 */
    public Value getMin() {
        return min;
    }

    /**
	 * Get the Value object that gives the value of the variable when the slider
	 * is at the right (or top) of the slider. The Value is always non-null.
	 * 
	 * @return value
	 */
    public Value getMax() {
        return max;
    }

    /**
	 * If the Controller, c, is non-null, then its compute method will be called
	 * whenever the user adjusts the position of the scroll bar.
	 * 
	 * @param c
	 *            the controller to use
	 */
    public void setOnUserAction(Controller c) {
        onUserAction = c;
        addChangeListener(new ChangeListener() {

            @SuppressWarnings("unchecked")
            public void stateChanged(ChangeEvent arg0) {
                double minval;
                if (min instanceof QueueableValue) {
                    minval = ((QueueableValue) min).getQVal();
                } else {
                    minval = min.getVal();
                }
                double maxval;
                if (max instanceof QueueableValue) {
                    maxval = ((QueueableValue) max).getQVal();
                } else {
                    maxval = max.getVal();
                }
                double halfMinMax = minval + (maxval - minval) * 0.5;
                if (integerValued) {
                    halfMinMax = Math.round(halfMinMax);
                }
                if ((minval != oldMinval) || (maxval != oldMaxval)) {
                    oldMinval = minval;
                    oldMaxval = maxval;
                    makeLabels();
                }
                if (oldPosition != -1) {
                    onUserAction.compute();
                }
            }
        });
    }

    /**
	 * Method required by InputObject interface; in this class, it simply calls
	 * setOnUserAction(c). This is meant to be called by
	 * JCMPanel.gatherInputs().
	 * 
	 * @param c
	 *            controller to notify
	 */
    public void notifyControllerOnChange(Controller c) {
        setOnUserAction(c);
    }

    /**
	 * Return the Controller, if any, that is notified when the user adjusts the
	 * position of the scroll bar.
	 * 
	 * @return the controller
	 */
    public Controller getOnUserAction() {
        return onUserAction;
    }

    /**
	 * Return this object's serial number, which is increased every time the
	 * value changes.
	 * 
	 * @return the serial number
	 */
    public long getSerialNumber() {
        if (needsValueCheck) {
            getVal();
        }
        return serialNumber;
    }

    /**
	 * Change the value and serial number of this object to match those of
	 * newest. See the Tie class for more information. This is not meant to be
	 * called directly
	 * 
	 * @param tie
	 *            the tie that connects the objects
	 * @param newest
	 *            the object to synch with
	 */
    public void sync(Tie tie, Tieable newest) {
        if (newest != this) {
            if (!(newest instanceof Value)) {
                throw new IllegalArgumentException("Internal Error:  A VariableJSlider can only sync with Value objects.");
            }
            double oldVal = getVal();
            double newVal = ((Value) newest).getVal();
            if (oldVal != newVal) {
                setVal(newVal);
            }
            serialNumber = newest.getSerialNumber();
        }
    }

    /**
	 * Get the value of this VariableJSlider. (If needsValueCheck is true, then
	 * the value is recomputed. Otherwise, the current value is returned.)
	 * 
	 * @return value of the slider
	 */
    public double getVal() {
        if (needsValueCheck) {
            double newMinVal = Double.NaN;
            double newMaxVal = Double.NaN;
            boolean maxMinChanged = false;
            boolean intervalsChanged = false;
            double value = variable.getVariableValue();
            try {
                if (min instanceof QueueableValue) {
                    newMinVal = ((QueueableValue) min).getQVal();
                } else {
                    newMinVal = min.getVal();
                }
                if (max instanceof QueueableValue) {
                    newMaxVal = ((QueueableValue) max).getQVal();
                } else {
                    newMaxVal = max.getVal();
                }
                if (!Double.isNaN(newMinVal) && !Double.isNaN(newMaxVal) && (newMinVal != minVal || newMaxVal != maxVal)) {
                    if (integerValued) {
                        checkIntegerLimits(newMinVal, newMaxVal);
                    }
                    minVal = newMinVal;
                    maxVal = newMaxVal;
                    maxMinChanged = true;
                }
            } catch (WcmError e) {
            }
            if (intervalsValue != null) {
                int newIntervals;
                if (intervalsValue instanceof QueueableValue) {
                    newIntervals = (int) ((QueueableValue) intervalsValue).getQVal();
                } else {
                    newIntervals = (int) intervalsValue.getVal();
                }
                newIntervals = fixIntervals(newIntervals);
                if (newIntervals != intervals) {
                    intervals = newIntervals;
                    setMinimum(0);
                    setMaximum(intervals);
                    intervalsChanged = true;
                }
            }
            if (maxMinChanged || intervalsChanged) {
                makeLabels();
            }
            if (Double.isNaN(minVal) || Double.isNaN(maxVal) || Double.isInfinite(minVal) || Double.isInfinite(maxVal)) {
                variable.setVariableValue(Double.NaN);
                if (!Double.isNaN(value)) {
                    serialNumber++;
                }
                setValue(0);
            } else if (oldPosition != getValue()) {
                double newVal = minVal + ((maxVal - minVal) * getValue()) / intervals;
                newVal = clamp(newVal, minVal, maxVal);
                if (integerValued) {
                    newVal = Math.round(newVal);
                }
                if (newVal != value) {
                    variable.setVariableValue(newVal);
                    serialNumber++;
                }
            } else if (!Double.isNaN(value) && maxMinChanged) {
                double newVal = clamp(value, minVal, maxVal);
                if (newVal != value) {
                    variable.setVariableValue(newVal);
                    serialNumber++;
                }
                if (minVal != maxVal) {
                    int pos = (int) ((value - minVal) / (maxVal - minVal) * intervals);
                    oldPosition = -1;
                    setValue(pos);
                }
            }
            oldPosition = getValue();
            needsValueCheck = false;
        }
        return variable.getVariableValue();
    }

    /**
	 * Set the value of the variable to x. If possible, set the value on the
	 * scroll bar to match.
	 * 
	 * @param x
	 *            the value to set
	 */
    public void setVal(double x) {
        try {
            double minVal, maxVal;
            if (min instanceof QueueableValue) {
                minVal = ((QueueableValue) min).getQVal();
            } else {
                minVal = min.getVal();
            }
            if (max instanceof QueueableValue) {
                maxVal = ((QueueableValue) max).getQVal();
            } else {
                maxVal = max.getVal();
            }
            if (Double.isNaN(x) || Double.isNaN(minVal) || Double.isNaN(maxVal) || Double.isInfinite(x) || Double.isInfinite(minVal) || Double.isInfinite(maxVal)) {
            } else {
                if (integerValued) {
                    minVal = Math.round(minVal);
                    maxVal = Math.round(maxVal);
                }
                double xpos = clamp(x, minVal, maxVal);
                int pos = (int) Math.round((xpos - minVal) / (maxVal - minVal) * intervals);
                setValue(pos);
            }
        } catch (WcmError e) {
        }
        variable.setVariableValue(x);
        needsValueCheck = false;
        oldPosition = getValue();
        serialNumber++;
    }

    /**
	 * From the InputObject interface. This will force the slider to recompute
	 * its max and min values, and possibly clamp its value between these two
	 * extremes) the next time the value or serial number is checked. This is
	 * ordinarily called by a Controller.
	 */
    public void checkInput() {
        needsValueCheck = true;
        getVal();
    }

    /**
	 * Modify getPreferredSize to return a width of 200, if the slider is
	 * horzontal, or a height of 200, if it is vertical. This is not meant to be
	 * called directly.
	 * 
	 * @return the dimension
	 */
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        if (getOrientation() == JSlider.HORIZONTAL) {
            return new Dimension(200, d.height);
        } else {
            return new Dimension(d.width, 200);
        }
    }

    private void checkIntegerLimits(double minVal, double maxVal) {
        int oldpos = getValue();
        minVal = Math.round(minVal);
        maxVal = Math.round(maxVal);
        double value = Math.round(variable.getVariableValue());
        double range = Math.abs(minVal - maxVal);
        if (range > 0 && range != intervals) {
            intervals = (int) Math.min(range, 10000);
            double v = clamp(value, minVal, maxVal);
            int pos = (int) ((v - minVal) / (maxVal - minVal) * intervals);
            setMaximum(intervals);
            makeLabels();
            setValue(pos);
        }
        if (oldpos == oldPosition) {
            oldPosition = getValue();
        } else {
            oldPosition = -1;
        }
    }

    private double clamp(double val, double minVal, double maxVal) {
        double newVal = val;
        if (minVal < maxVal) {
            if (newVal < minVal) {
                newVal = minVal;
            } else if (newVal > maxVal) {
                newVal = maxVal;
            }
        } else {
            if (newVal < maxVal) {
                newVal = maxVal;
            } else if (newVal > minVal) {
                newVal = minVal;
            }
        }
        return newVal;
    }

    /**
	 * Overridden to call onUserAction.compute() if onUserAction is non-null.
	 * This is not meant to be called directly.
	 * 
	 * @param evt
	 *            the event created when user adjusts the slider
	 */
    public void adjustmentValueChanged(AdjustmentEvent evt) {
        if (onUserAction != null) onUserAction.compute();
    }

    private class VS extends Variable {

        /**
		 * Comment for <code>serialVersionUID</code>
		 */
        private static final long serialVersionUID = 3762532321538355250L;

        VS(String name) {
            super(name);
        }

        /**
		 * Get the value.
		 * 
		 * @see net.sourceforge.webcompmath.data.Constant#getVal()
		 */
        @Override
        public double getVal() {
            return VariableJSlider.this.getVal();
        }

        /**
		 * Set the value.
		 * 
		 * @see net.sourceforge.webcompmath.data.Variable#setVal(double)
		 */
        @Override
        public void setVal(double x) {
            VariableJSlider.this.setVal(x);
        }

        void setVariableValue(double x) {
            super.setVal(x);
        }

        double getVariableValue() {
            return super.getVal();
        }
    }

    /**
	 * Get the number of discrete slider steps
	 * 
	 * @return the intervals
	 */
    public int getIntervals() {
        return intervals;
    }

    /**
	 * Set the number of discrete slider steps
	 * 
	 * @param intervals
	 *            the intervals to set
	 */
    @SuppressWarnings("unchecked")
    public void setIntervals(int intervals) {
        intervals = fixIntervals(intervals);
        if (this.intervals != intervals) {
            this.intervals = intervals;
            setMinimum(0);
            setMaximum(intervals);
            makeLabels();
            setVal(variable.getVal());
        }
    }

    private int fixIntervals(int intervals) {
        if (intervals <= 0) {
            intervals = 1000;
        }
        if (intervals <= 2) {
            intervals = 2;
        }
        return intervals;
    }

    /**
	 * Get the number of slider steps as a Value object.
	 * 
	 * @return the intervals as a Value
	 */
    public Value getIntervalsValue() {
        return intervalsValue;
    }

    /**
	 * Set the number of slider steps as a Value object.
	 * 
	 * @param v
	 *            the Value for the number of steps
	 * 
	 */
    public void setIntervalsValue(Value v) {
        if (v != null) {
            if (intervalsValue instanceof QueueableValue) {
                QueueableValue qv = (QueueableValue) intervalsValue;
                qv.removeQueueableValueUpdateListener(this);
            }
            intervalsValue = v;
            if (intervalsValue instanceof QueueableValue) {
                QueueableValue qv = (QueueableValue) intervalsValue;
                qv.addQueueableValueUpdateListener(this);
            }
            int i;
            if (intervalsValue instanceof QueueableValue) {
                i = (int) ((QueueableValue) intervalsValue).getQVal();
            } else {
                i = (int) intervalsValue.getVal();
            }
            setIntervals(i);
        }
    }

    private void makeLabels() {
        if (min != null && max != null) {
            labelTable = new Hashtable<Integer, Component>();
            zero = new Integer(0 + sliderLabelOffset);
            intervalsInt = new Integer(intervals - sliderLabelOffset);
            halfIntervalsInt = new Integer(intervals / 2);
            double minval, maxval;
            if (min instanceof QueueableValue) {
                minval = ((QueueableValue) min).getQVal();
            } else {
                minval = min.getVal();
            }
            if (max instanceof QueueableValue) {
                maxval = ((QueueableValue) max).getQVal();
            } else {
                maxval = max.getVal();
            }
            String s = NumUtils.realToString(minval);
            labelTable.put(zero, new JLabel(s));
            s = NumUtils.realToString((maxval - minval) / 2 + minval);
            labelTable.put(halfIntervalsInt, new JLabel(s));
            s = NumUtils.realToString(maxval);
            labelTable.put(intervalsInt, new JLabel(s));
            this.setLabelTable(labelTable);
        }
    }

    /**
	 * 
	 * @see net.sourceforge.webcompmath.data.QueueableValueUpdateListener#valueUpdated(net.sourceforge.webcompmath.data.QueueableValueUpdateEvent)
	 */
    public void valueUpdated(QueueableValueUpdateEvent evt) {
        needsValueCheck = true;
        getVal();
    }
}
