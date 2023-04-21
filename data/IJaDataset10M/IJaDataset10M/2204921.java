package org.taddei.jemv.gui;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import visad.CellImpl;
import visad.ControlEvent;
import visad.ControlListener;
import visad.Data;
import visad.DataReference;
import visad.Display;
import visad.PlotText;
import visad.Real;
import visad.RealType;
import visad.RemoteCell;
import visad.RemoteCellImpl;
import visad.RemoteDataReference;
import visad.ScalarMap;
import visad.ScalarMapControlEvent;
import visad.ScalarMapEvent;
import visad.ScalarMapListener;
import visad.ValueControl;
import visad.VisADException;
import visad.util.Util;

/** VisADSlider combines a JSlider and a JLabel and links them to either a
 *  Real (via a DataReference) or a ScalarMap that maps to
 *  Display.SelectValue.  Changes in the slider will reflect the Real or
 *  ScalarMap linked to it.  If no bounds are specified, they will be
 *  detected from the ScalarMap and the slider will auto-scale.  Note that
 *  a slider linked to a Real cannot auto-scale, because it has no way to
 *  detect the bounds.<br>
 * <br>
 * {@link javax.swing.BoxLayout BoxLayout} doesn't handle a mixture
 * of the standard center-aligned widgets and VisADSliders, which
 * are left-aligned by default.  If you have problems with widgets
 * being too wide, you may want to change the other widgets in
 * the {@link javax.swing.JPanel JPanel} to align on the left
 * (e.g. <tt>widget.setAlignmentX(BoxLayout.LEFT_ALIGNMENT)</tt>)
 */
public class VisADInputSlider extends JPanel implements ChangeListener, ControlListener, ScalarMapListener {

    /** The default number of ticks the slider should have */
    private static final int D_TICKS = 1000;

    /** Default width of the slider in pixels */
    private static final int SLIDER_WIDTH = 150;

    /** Default width of the label in pixels */
    private static final int LABEL_WIDTH = 200;

    /** The JSlider that forms part of the VisADInputSlider's UI */
    private JSlider slider;

    /** The JLabel that forms part of the VisADInputSlider's UI */
    private JLabel label;

    private JFormattedTextField inputValue;

    /** The ScalarMap that is linked to this VisADInputSlider (null if none) */
    private ScalarMap map;

    /** The ValueControl that this VisADInputSlider utilizes (null if none) */
    private ValueControl control;

    /** The DataReference that is linked to this VisADInputSlider (null if none) */
    private DataReference sRef;

    /** The type of the linked Real (null if none) */
    private RealType realType;

    /** The name of the variable being modified by this VisADInputSlider */
    private String sName;

    /** The minimum allowed slider value */
    private double sMinimum;

    /** The maximum allowed slider value */
    private double sMaximum;

    /** The current slider value */
    private double sCurrent;

    /** The number of ticks in the slider */
    private int sTicks;

    /** <CODE>true</CODE> if the widget will auto-scale */
    private boolean autoScale;

    /** <CODE>true</CODE> if the slider ticks should be integers */
    private boolean integralValues;

    /** <CODE>true</CODE> if the label width should be dynamically scaled */
    private boolean dynamicLabelWidth;

    /** JSlider values range between <tt>low</tt> and <tt>hi</tt>
   *  (with initial value <tt>st</tt>) and are multiplied by
   *  <tt>scale</tt> to create Real values of RealType <tt>rt</tt>
   *  referenced by <tt>ref</tt>.
   */
    public VisADInputSlider(String n, int lo, int hi, int st, double scale, DataReference ref, RealType rt) throws VisADException, RemoteException {
        this(ref, null, (float) (lo * scale), (float) (hi * scale), (float) (st * scale), hi - lo, (ref == null || ref.getData() instanceof Real) ? null : rt, n, false, false);
    }

    /** JSlider values range between <tt>low</tt> and <tt>hi</tt>
   *  (with initial value <tt>st</tt>) and are multiplied by
   *  <tt>scale</tt> to create Real values of RealType <tt>rt</tt>
   *  referenced by <tt>ref</tt>.  The slider label has a
   *  dynamically sized width if <tt>dynamicLabelWidth</tt> is <tt>true</tt>.
   */
    public VisADInputSlider(String n, int lo, int hi, int st, double scale, DataReference ref, RealType rt, boolean dynamicLabelWidth) throws VisADException, RemoteException {
        this(ref, null, (float) (lo * scale), (float) (hi * scale), (float) (st * scale), hi - lo, (ref == null || ref.getData() instanceof Real) ? null : rt, n, false, dynamicLabelWidth);
    }

    /** construct a VisADInputSlider from a ScalarMap that maps to
   *  Display.SelectValue, with auto-scaling minimum and maximum bounds,
   *  non-integral values, and a statically sized label.
   */
    public VisADInputSlider(ScalarMap smap) throws VisADException, RemoteException {
        this(null, smap, Float.NaN, Float.NaN, Float.NaN, D_TICKS, null, null, false, false);
    }

    /** construct a VisADInputSlider from a ScalarMap that maps to
   *  Display.SelectValue, with auto-scaling minimum and maximum bounds,
   *  either integer or floating-point values, depending on the setting
   *  of <tt>integralTicks</tt>, and a statically sized label.
   */
    public VisADInputSlider(ScalarMap smap, boolean integralTicks) throws VisADException, RemoteException {
        this(null, smap, Float.NaN, Float.NaN, Float.NaN, D_TICKS, null, null, integralTicks, false);
    }

    /** construct a VisADInputSlider from a ScalarMap that maps to
   *  Display.SelectValue, with auto-scaling minimum and maximum bounds,
   *  either integer or floating-point values (depending on the setting
   *  of <tt>integralTicks</tt>, and either a static or dynamically
   *  sized label (depending on the setting of <tt>dynamicLabelWidth</tt>.
   */
    public VisADInputSlider(ScalarMap smap, boolean integralTicks, boolean dynamicLabelWidth) throws VisADException, RemoteException {
        this(null, smap, Float.NaN, Float.NaN, Float.NaN, D_TICKS, null, null, integralTicks, dynamicLabelWidth);
    }

    /** construct a VisADInputSlider from a ScalarMap that maps to
   *  Display.SelectValue, with minimum and maximum bounds min and max,
   *  no auto-scaling, non-integer values, and a static label width.
   */
    public VisADInputSlider(ScalarMap smap, float min, float max) throws VisADException, RemoteException {
        this(null, smap, min, max, Float.NaN, D_TICKS, null, null, false, false);
    }

    /** construct a VisADInputSlider by creating a Real and linking it to r,
      using RealType rt and name n, with minimum and maximum bounds
      min and max, and starting value start */
    public VisADInputSlider(DataReference ref, float min, float max, float start, RealType rt, String n) throws VisADException, RemoteException {
        this(ref, null, min, max, start, D_TICKS, rt, n, false, false);
    }

    /** construct a VisADInputSlider from an existing Real pointed to by r,
      with minimum and maximum bounds min and max */
    public VisADInputSlider(DataReference ref, float min, float max) throws VisADException, RemoteException {
        this(ref, null, min, max, Float.NaN, D_TICKS, null, null, false, false);
    }

    /** complete constructor */
    private VisADInputSlider(DataReference ref, ScalarMap smap, float min, float max, float start, int sliderTicks, RealType rt, String n, boolean integralValues, boolean dynamicLabelWidth) throws VisADException, RemoteException {
        this.integralValues = integralValues;
        this.dynamicLabelWidth = dynamicLabelWidth;
        setAlignmentX(LEFT_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        sTicks = sliderTicks;
        Dimension d;
        slider = new JSlider(0, sTicks, sTicks / 2);
        d = slider.getMinimumSize();
        slider.setMinimumSize(new Dimension(SLIDER_WIDTH, d.height));
        d = slider.getPreferredSize();
        slider.setPreferredSize(new Dimension(SLIDER_WIDTH, d.height));
        d = slider.getMaximumSize();
        slider.setMaximumSize(new Dimension(SLIDER_WIDTH, d.height));
        autoScale = false;
        if (ref == null) {
            if (smap == null) {
                throw new VisADException("VisADInputSlider: must specify either a " + "DataReference or a ScalarMap!");
            }
            if (smap.getDisplayScalar() != Display.SelectValue) {
                throw new VisADException("VisADInputSlider: ScalarMap must be to " + "Display.SelectValue!");
            }
            if (!(smap.getScalar() instanceof RealType)) {
                throw new VisADException("VisADInputSlider: ScalarMap must be from " + "a RealType!");
            }
            map = smap;
            control = (ValueControl) smap.getControl();
            if (control == null) {
                throw new VisADException("VisADInputSlider: ScalarMap must be addMap'ed " + "to a Display");
            }
            sRef = null;
            sName = smap.getScalarName();
            start = (float) control.getValue();
            if (min == min && max == max && start == start) {
                sMinimum = min;
                sMaximum = max;
                if (integralValues) {
                    int tmp = (int) (sMaximum - sMinimum);
                    if (tmp != sTicks) {
                        sTicks = tmp;
                        slider.setMaximum(sTicks);
                    }
                }
                sCurrent = start;
                initLabel();
                smap.setRange(min, max);
                if (start < min || start > max) {
                    start = (min + max) / 2;
                    control.setValue(start);
                }
            } else {
                autoScale = true;
                initLabel();
            }
            control.addControlListener(this);
            smap.addScalarMapListener(this);
        } else {
            map = null;
            control = null;
            if (ref == null) {
                throw new VisADException("VisADInputSlider: DataReference " + "cannot be null!");
            }
            sRef = ref;
            Data data = ref.getData();
            if (data == null) {
                if (rt == null) {
                    throw new VisADException("VisADInputSlider: RealType cannot be null!");
                }
                if (n == null) {
                    throw new VisADException("VisADInputSlider: name cannot be null!");
                }
                realType = rt;
                if (min != min || max != max || start != start) {
                    throw new VisADException("VisADInputSlider: min, max, and start " + "cannot be NaN!");
                }
                sMinimum = min;
                sMaximum = max;
                sCurrent = (start < min || start > max) ? (min + max) / 2 : start;
                sRef.setData(new Real(realType, sCurrent));
            } else {
                if (!(data instanceof Real)) {
                    throw new VisADException("VisADInputSlider: DataReference " + "must point to a Real!");
                }
                Real real = (Real) data;
                realType = (RealType) real.getType();
                sCurrent = (float) real.getValue();
                if (min != min || max != max) {
                    throw new VisADException("VisADInputSlider: minimum and maximum " + "cannot be NaN!");
                }
                sMinimum = min;
                sMaximum = max;
                if (sCurrent < min || sCurrent > max) sCurrent = (min + max) / 2;
            }
            sName = (n != null) ? n : realType.getName();
            initLabel();
            CellImpl cell = new CellImpl() {

                public void doAction() throws VisADException, RemoteException {
                    if (sRef != null) {
                        double val;
                        try {
                            val = ((Real) sRef.getData()).getValue();
                            if (!Util.isApproximatelyEqual(sCurrent, val)) updateSlider(val);
                        } catch (RemoteException re) {
                            if (visad.collab.CollabUtil.isDisconnectException(re)) {
                                sRef = null;
                            }
                            throw re;
                        }
                    }
                }
            };
            if (ref instanceof RemoteDataReference) {
                RemoteCell remoteCell = new RemoteCellImpl(cell);
                remoteCell.addReference(ref);
            } else cell.addReference(ref);
        }
        add(slider);
        add(label);
        add(inputValue);
        slider.addChangeListener(this);
        updateSlider(start);
    }

    /** sets up the JLabel */
    private void initLabel() {
        String str = sName + " = " + PlotText.shortString(sCurrent);
        Dimension d;
        if (dynamicLabelWidth) {
            label = new JLabel(str);
        } else {
            label = new JLabel(str + "         ");
            d = label.getMinimumSize();
            label.setMinimumSize(new Dimension(LABEL_WIDTH, d.height));
            d = label.getPreferredSize();
            label.setPreferredSize(new Dimension(LABEL_WIDTH, d.height));
            d = label.getMaximumSize();
            label.setMaximumSize(new Dimension(LABEL_WIDTH, d.height));
        }
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        inputValue = new JFormattedTextField(new Float(sCurrent));
        inputValue.setColumns(4);
        inputValue.addPropertyChangeListener("value", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                sCurrent = ((Number) inputValue.getValue()).doubleValue();
            }
        });
    }

    /**
   * Hardcode the preferred size of the slider after increasing
   * the current width by the specified percentage (or decreasing
   * it if <tt>percent</tt> is negative.)<br>
   * <br>
   * This method is primarily useful to keep changes in the
   * label (of a VisADInputSlider with <tt>dynamicLabelWidth</tt>
   * set to <tt>true</t>)
   * from causing the rest of the window to be redrawn.
   *
   * @param percent percent of current size to use for hardcoded
   *                size.  (e.g. to keep the current size, specify
   *                <tt>100</tt>; to increase the size a bit,
   *                specify <tt>115</tt>, to decrease it a bit,
   *                specify <tt>85</tt>, etc.)
   */
    public void hardcodeSizePercent(int percent) {
        Dimension d = getPreferredSize();
        int newWidth = d.width + (d.width * (percent - 100)) / 100;
        setPreferredSize(new Dimension(newWidth, d.height));
    }

    /** called when slider is adjusted */
    public synchronized void stateChanged(ChangeEvent e) {
        try {
            double val = slider.getValue();
            double cur = (sMaximum - sMinimum) * (val / sTicks) + sMinimum;
            if (integralValues) {
                cur = Math.floor(cur + 0.5);
            }
            if (!Util.isApproximatelyEqual(sCurrent, cur)) {
                if (control != null) control.setValue(cur); else if (sRef != null) {
                    try {
                        sRef.setData(new Real(realType, cur));
                    } catch (RemoteException re) {
                        if (visad.collab.CollabUtil.isDisconnectException(re)) {
                            sRef = null;
                        }
                        throw re;
                    }
                }
            }
        } catch (VisADException exc) {
            exc.printStackTrace();
        } catch (RemoteException exc) {
            exc.printStackTrace();
        }
    }

    /** used for auto-scaling the minimum and maximum */
    public void mapChanged(ScalarMapEvent e) {
        if (!autoScale) return;
        double[] range = map.getRange();
        sMinimum = (float) range[0];
        sMaximum = (float) range[1];
        if (integralValues) {
            int tmp = (int) (sMaximum - sMinimum);
            if (tmp != sTicks) {
                sTicks = tmp;
                slider.setMaximum(sTicks);
            }
        }
        sCurrent = (float) control.getValue();
        if (sCurrent < sMinimum || sCurrent > sMaximum) {
            sCurrent = (sMinimum + sMaximum) / 2;
        }
        updateSlider(sCurrent);
    }

    /**
   * ScalarMapListener method used to detect new control.
   */
    public void controlChanged(ScalarMapControlEvent evt) {
        int id = evt.getId();
        if (id == ScalarMapEvent.CONTROL_REMOVED || id == ScalarMapEvent.CONTROL_REPLACED) {
            control = null;
        }
        if (id == ScalarMapEvent.CONTROL_REPLACED || id == ScalarMapEvent.CONTROL_ADDED) {
            control = (ValueControl) evt.getScalarMap().getControl();
        }
    }

    /** Update slider when value of linked ValueControl changes */
    public void controlChanged(ControlEvent e) throws VisADException, RemoteException {
        double cur = control.getValue();
        if (!Util.isApproximatelyEqual(sCurrent, cur)) {
            updateSlider(control.getValue());
        }
    }

    /** Update the slider's value */
    private synchronized void updateSlider(double value) {
        if (integralValues) {
            value = Math.floor(value + 0.5);
        }
        int ival = (int) (sTicks * ((value - sMinimum) / (sMaximum - sMinimum)));
        if (Math.abs(slider.getValue() - ival) > 1) {
            slider.removeChangeListener(this);
            slider.setValue(ival);
            slider.addChangeListener(this);
        }
        sCurrent = value;
        label.setText(sName + " = " + PlotText.shortString(sCurrent));
        inputValue.setText(PlotText.shortString(sCurrent));
        invalidate();
    }

    public double getCurrent() {
        return sCurrent;
    }
}
