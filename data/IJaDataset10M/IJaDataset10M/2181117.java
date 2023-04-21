package jpicedt.widgets;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * A JComponent that lump together a JSpinner (with a +/- arrow field that allows the user to increment/decrement)
 * and prefix/postfix labels.
 * the field's value.
 * @author Sylvain Reynal
 * @since jpicedt 1.3
 * @version $Id: IncrementableTextField.java,v 1.7 2011/07/23 05:35:56 vincentb1 Exp $
 */
public class IncrementableTextField extends JPanel {

    private JSpinner sp;

    private SpinnerNumberModel model;

    private JLabel prefixLabel, postfixLabel;

    private String dialogTitle = new String();

    private String actionCommand = null;

    private Double minimum = new Double(0.0);

    private Double maximum = new Double(100.0);

    /**
	 * Creates a new IncrementableTextField with an etched border drawn around it.
	 * @param initialValue the initial value of the text field
	 * @param increment increment by which value get in-/de-cremented when a click occurs on one of the two arrows
	 * @param icon if non-null, icon gets added to the left of the component
	 * @param postFix if non-null, this string gets added just right of the testfield (it can be use to specify a unit)
	 */
    public IncrementableTextField(double initialValue, double increment, Icon icon, String postFix) {
        this(initialValue, increment, icon, postFix, true);
    }

    /**
	 * @param initialValue the initial value of the text field
	 * @param increment increment by which value get in-/de-cremented when a click occurs on one of the two arrows
	 * @param icon if non-null, gets added to the left of the component
	 * @param postFix if non-null, gets added just right of the component (it can be use to specify a unit)
	 * @param drawBorder if TRUE, draw a border around the component
	 */
    public IncrementableTextField(double initialValue, double increment, Icon icon, String postFix, boolean drawBorder) {
        this(initialValue, increment, (icon == null ? null : new JLabel(icon)), (postFix == null ? null : new JLabel(postFix)), drawBorder);
    }

    /**
	 * @param initialValue the initial value of the text field
	 * @param increment increment by which value get in-/de-cremented when a click occurs on one of the two arrows
	 * @param prefix if non-null, gets added to the left of the component
	 * @param postfix if non-null, gets added just right of the component (it can be use to specify a unit)
	 * @param drawBorder if TRUE, draw a border around the component
	 */
    public IncrementableTextField(double initialValue, double increment, JLabel prefix, JLabel postfix, boolean drawBorder) {
        super(new FlowLayout(FlowLayout.LEFT, 3, 3));
        Box b = new Box(BoxLayout.X_AXIS);
        this.postfixLabel = postfix;
        this.prefixLabel = prefix;
        if (prefix != null) {
            b.add(prefix);
            prefix.setAlignmentY(CENTER_ALIGNMENT);
        }
        b.add(Box.createHorizontalStrut(3));
        EventHandler handler = new EventHandler();
        this.model = new SpinnerNumberModel(new Double(initialValue), null, null, new Double(increment));
        b.add(this.sp = new JSpinner(this.model));
        this.sp.addChangeListener(handler);
        if (sp.getEditor() instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) sp.getEditor();
            editor.getTextField().addActionListener(handler);
            editor.getTextField().setColumns(5);
        }
        sp.setAlignmentY(CENTER_ALIGNMENT);
        if (postfix != null) {
            b.add(postfix);
            postfix.setAlignmentY(CENTER_ALIGNMENT);
        }
        add(b);
        if (drawBorder) this.setBorder(BorderFactory.createEtchedBorder());
    }

    /**
	 * sets minimum and maximum bounds for "value" to the given doubles
	 */
    public void setBoundValues(double min, double max) {
        this.minimum = new Double(min);
        this.maximum = new Double(max);
        if (this.isLowBounded()) model.setMinimum(this.minimum);
        if (this.isHighBounded()) model.setMaximum(this.maximum);
    }

    /**
	 * sets minimum bound for "value" to the given double
	 */
    public void setMinimum(double min) {
        this.minimum = new Double(min);
        if (this.isLowBounded()) model.setMinimum(this.minimum);
    }

    /**
	 * sets maximum bound for "value" to the given double
	 */
    public void setMaximum(double max) {
        this.maximum = new Double(max);
        if (this.isHighBounded()) model.setMaximum(this.maximum);
    }

    /**
	 * @return the maximum bound
	 */
    public double getMaximum() {
        return maximum.doubleValue();
    }

    /**
	 * @return the minimum bound
	 */
    public double getMinimum() {
        return minimum.doubleValue();
    }

    /**
	 * @param state if TRUE, DecimalNumberField's value is low-bounded
	 */
    public void setLowBounded(boolean state) {
        if (state) model.setMinimum(this.minimum); else model.setMinimum(null);
    }

    /**
	 * @return TRUE if "value" is low-bounded
	 */
    public boolean isLowBounded() {
        return model.getMinimum() != null;
    }

    /**
	 * @param state if TRUE, DecimalNumberField's value is high-bounded
	 */
    public void setHighBounded(boolean state) {
        if (state) model.setMaximum(maximum); else model.setMaximum(null);
    }

    /**
	 * @return TRUE if "value" is high-bounded
	 */
    public boolean isHighBounded() {
        return model.getMaximum() != null;
    }

    /**
	 * sets the title of the JDialog that opens when a NumberFormatException occurs
	 */
    public void setDialogTitle(String title) {
        this.dialogTitle = title;
    }

    /**
	 * sets the actionCommand for this component to the given string
	 */
    public void setActionCommand(String s) {
        this.actionCommand = s;
    }

    /**
	 * @return this component's action command
	 */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
	 * @return the current double value contained in DecimalNumberField
	 */
    public double getValue() {
        return ((Number) sp.getValue()).doubleValue();
    }

    /**
	 * sets the DecimalNumberField double value, as well as the internal copy, to the given value, possibly modifying it so that it fits within the limits.
	 */
    public void setValue(double value) {
        sp.setValue(new Double(value));
    }

    public void setValue(Number n) {
        sp.setValue(n);
    }

    /**
	 * set the enable state of this component
	 */
    public void setEnabled(boolean b) {
        sp.setEnabled(b);
        if (prefixLabel != null) prefixLabel.setEnabled(b);
        if (postfixLabel != null) postfixLabel.setEnabled(b);
    }

    /**
	 * Return the "enabled" state of this component
	 */
    public boolean isEnabled() {
        return sp.isEnabled();
    }

    /**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance 
	 * is lazily created using the parameters passed into 
	 * the fire method.
	 */
    protected void fireActionPerformed() {
        Object[] listeners = listenerList.getListenerList();
        ActionEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                if (e == null) {
                    e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getActionCommand(), 0);
                }
                ((ActionListener) listeners[i + 1]).actionPerformed(e);
            }
        }
    }

    /**
	 * adds an ActionListener to the component
	 */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
	 * removes an ActionListener from the component
	 */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    private void checkValueInsideBounds() {
        double val = ((Number) sp.getValue()).doubleValue();
        if (isLowBounded()) {
            if (val > maximum.doubleValue()) sp.setValue(maximum);
        }
        if (isHighBounded()) {
            if (val < minimum.doubleValue()) sp.setValue(minimum);
        }
    }

    private class EventHandler implements ActionListener, ChangeListener {

        public void actionPerformed(ActionEvent e) {
            checkValueInsideBounds();
            sp.setValue(sp.getValue());
            fireActionPerformed();
        }

        public void stateChanged(ChangeEvent e) {
            checkValueInsideBounds();
            sp.setValue(sp.getValue());
            fireActionPerformed();
        }
    }
}
