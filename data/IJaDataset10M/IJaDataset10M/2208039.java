package org.dinopolis.timmon.frontend.treetable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.beans.*;
import java.text.*;
import java.util.*;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.KeyStroke;
import org.dinopolis.util.Resources;
import org.dinopolis.util.resource.AbstractResources;

/**
 * This property editor allows the user to edit date objects.
 *
 * @author Dieter Freismuth
 * @version $Revision: 1.4 $
 */
public class DateTimeEditor extends PropertyEditorSupport implements ActionListener, FocusListener, InteractableEditor {

    /** the editing component */
    private JPanel editing_component_;

    /** the date field */
    private JButton date_field_;

    /** the date field */
    private Date date_;

    /** the time field */
    private JTextField time_field_;

    /** the reference to the resource boundle */
    private Resources resources_;

    /** the Date Formater */
    private DateFormat date_formater_;

    /** the Time Formater */
    private DateFormat time_formater_;

    /** the minute step interval, used for up, down actions of the
   * time field */
    public static final int STEP_INTERVAL = 5 * 60;

    /** the could not set value title key */
    private static final String KEY_COULD_NOT_SET_VALUE_TITLE = "could_not_set_value.title";

    /** the could not set value text key */
    private static final String KEY_COULD_NOT_SET_VALUE_TEXT = "could_not_set_value.text";

    private static final DateFormat INTERNAL_DATE_FORMATER = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    /** the key for the pattern period resource */
    static final String TIME_PATTERN = "time.pattern";

    /**
   * Constructor taking a resource file as its argument.
   *
   * @param resources the resources.
   */
    public DateTimeEditor(Resources resources) {
        super();
        resources_ = resources;
        date_formater_ = DateFormat.getDateInstance(DateFormat.SHORT);
        time_formater_ = new SimpleDateFormat(resources.getString(TIME_PATTERN, "HH:mm"));
    }

    /**
   * Returns the set values text representation.
   *
   * @return the set values text representation.
   */
    public String getAsText() {
        if (getValue() == null) return ("");
        return (INTERNAL_DATE_FORMATER.format(((Date) getValue())));
    }

    /**
   * Sets the value according to its given text representation
   * <code>text</code>. 
   *
   * @param text the text representation to be set.
   * @exception IllegalArgumentException if the given value is not
   * valid. 
   */
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(INTERNAL_DATE_FORMATER.parse(text));
            return;
        } catch (Exception e) {
        }
        throw new IllegalArgumentException(text);
    }

    /**
   * Returns a custom editor for a date object.
   *
   * @return the editing component.
   */
    public Component getCustomEditor() {
        if (editing_component_ == null) createEditingComponent();
        return (editing_component_);
    }

    /**
   * Creates and sets the editing component.
   */
    protected void createEditingComponent() {
        editing_component_ = new JPanel() {

            /**
		 * 
		 */
            private static final long serialVersionUID = 1L;

            protected boolean processKeyBinding(javax.swing.KeyStroke ks, java.awt.event.KeyEvent e, int condition, boolean pressed) {
                javax.swing.InputMap map;
                javax.swing.ActionMap am;
                map = time_field_.getInputMap(condition);
                am = time_field_.getActionMap();
                if (map != null && am != null && time_field_.isEnabled()) {
                    Object binding = map.get(ks);
                    javax.swing.Action action = (binding == null) ? null : am.get(binding);
                    if (action != null) {
                        return javax.swing.SwingUtilities.notifyAction(action, ks, e, time_field_, e.getModifiers());
                    }
                }
                return (false);
            }
        };
        editing_component_.setLayout(new GridLayout(0, 2));
        date_field_ = new JButton();
        Insets button_insets = new Insets(1, 1, 1, 1);
        date_field_.setBorder(new EmptyBorder(button_insets));
        date_field_.setActionCommand("date");
        date_field_.addActionListener(this);
        JButton button = new NoFocusButton(TreeTableConstants.UP_ICON);
        button.setActionCommand("date_up");
        button.addActionListener(this);
        button.setMargin(new Insets(0, 0, 0, 0));
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.add(button, BorderLayout.NORTH);
        button = new NoFocusButton(TreeTableConstants.DOWN_ICON);
        button.setActionCommand("date_down");
        button.addActionListener(this);
        button.setMargin(new Insets(0, 0, 0, 0));
        panel2.add(button, BorderLayout.SOUTH);
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.add(date_field_, BorderLayout.CENTER);
        panel1.add(panel2, BorderLayout.EAST);
        editing_component_.add(panel1);
        time_field_ = new JTextField();
        time_field_.addFocusListener(this);
        time_field_.setActionCommand("time");
        time_field_.addActionListener(this);
        EscapeAction escape = new EscapeAction();
        time_field_.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), escape.getValue(Action.NAME));
        time_field_.getActionMap().put(escape.getValue(Action.NAME), escape);
        button = new NoFocusButton(TreeTableConstants.UP_ICON);
        button.setActionCommand("time_up");
        button.addActionListener(this);
        button.setMargin(new Insets(0, 0, 0, 0));
        panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.add(button, BorderLayout.NORTH);
        button = new NoFocusButton(TreeTableConstants.DOWN_ICON);
        button.setActionCommand("time_down");
        button.addActionListener(this);
        button.setMargin(new Insets(0, 0, 0, 0));
        panel2.add(button, BorderLayout.SOUTH);
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.add(time_field_, BorderLayout.CENTER);
        panel1.add(panel2, BorderLayout.EAST);
        editing_component_.add(panel1);
        updateValue();
    }

    /**
   * Stops the editing, this will update the value to the current
   * given value of the editing component. If the value of the editing
   * component is not valid an IllegalStateException will be thrown.
   * This method simply does nothing if no editing component is
   * present.
   *
   * @exception IllegalStateException if the editing component is not
   * in a legal state to set its value. 
   */
    public void stopEditing() throws IllegalStateException {
        setValueFromTextFields();
    }

    /**
   * Cancels the editing. This will reset the value of the editing
   * component to the stored value.
   */
    public void cancelEditing() {
        updateValue();
    }

    /**
   * Updates the value according to the stored value
   */
    protected void updateValue() {
        updateValue((Date) super.getValue());
    }

    /**
   * Updates the value according to the given value
   * <code>value</code>. 
   *
   * @param value the value to be set.
   */
    protected void updateValue(Date date) {
        if (date_field_ == null) return;
        if (date == null) date = new Date();
        date_ = date;
        date_field_.setText(date_formater_.format(date));
        time_field_.setText(time_formater_.format(date));
    }

    /**
   * Sets the value according to the stored value.
   */
    public void setValue() {
        setValue(getValue());
    }

    /**
   * Sets the value according to the stored value.
   */
    public Object getValue() {
        return (new Date(((Date) super.getValue()).getTime()));
    }

    /**
   * Sets the value according to the given value.
   *
   * @param value the value to be set.
   */
    public void setValue(Object value) {
        Date old_value = (Date) super.getValue();
        try {
            updateValue((Date) value);
            super.setValue(value);
        } catch (RuntimeException exc) {
            super.setValue(old_value);
            updateValue(old_value);
            JOptionPane.showMessageDialog(editing_component_, exc.getCause().getMessage(), "Changing Value Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
   * Gets the value according to the strings given within the text
   * fields.
   *
   * @return the value as given within the text fields.
   * @exception ParseException in case of a parsing error.
   */
    protected Date getValueFromTextFields() throws ParseException {
        Date day = date_;
        if (day == null) day = new Date();
        String time = "";
        if (time_field_ != null) time = time_field_.getText().toLowerCase();
        if (time.length() <= 0) time = "now";
        if ((time.length() <= 0) || (time.equals("now")) || time.equals("jetzt") || time.equals("t") || time.equals("n") || time.equals("h")) {
            time = time_formater_.format(new Date());
            day = new Date();
        } else time = completeTime(time);
        Calendar cal = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        cal.setTime(day);
        cal2.setTime(time_formater_.parse(time));
        cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal2.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal2.get(Calendar.MILLISECOND));
        return (new Date(cal.getTimeInMillis()));
    }

    /**
   * Sets the value according to the strings given within the text
   * fields.
   */
    private void setValueFromTextFields() {
        try {
            setValue(getValueFromTextFields());
        } catch (ParseException e) {
            if (editing_component_ == null) throw (new IllegalArgumentException(e.getMessage())); else {
                TreeMap map = new TreeMap();
                String time = "";
                if (time_field_ != null) time = time_field_.getText();
                map.put("value", time);
                String text = resources_.getString(KEY_COULD_NOT_SET_VALUE_TEXT);
                updateValue((Date) super.getValue());
                text = AbstractResources.replace(text, map);
                JOptionPane.showMessageDialog(editing_component_, text, resources_.getString(KEY_COULD_NOT_SET_VALUE_TITLE), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
   * Sets the value as displayed.
   * Invoked when the a text field loses its focus.
   *
   * @param event the focus event.
   */
    public void focusLost(FocusEvent event) {
        if (!event.isTemporary()) setValueFromTextFields();
    }

    /**
   * Does nothing.
   * Invoked when the a text field gained its focus.
   *
   * @param event the focus event.
   */
    public void focusGained(FocusEvent event) {
    }

    /**
   * Sets the value as displayed.
   * Invoked when the a text field changed its value.
   *
   * @param event the action event
   */
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("time")) {
            setValueFromTextFields();
            return;
        }
        Date date = null;
        try {
            date = getValueFromTextFields();
        } catch (ParseException exc) {
            date = (Date) getValue();
        }
        if (date == null) date = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (action.equals("date")) {
            Calendar set_value = DayChooser.showDayChooser(editing_component_, resources_, calendar);
            if (set_value != null) setValue(set_value.getTime());
            return;
        }
        if (action.equals("date_up")) {
            if (!date_field_.hasFocus()) date_field_.requestFocus();
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
            setValue(date);
            return;
        }
        if (action.equals("date_down")) {
            if (!date_field_.hasFocus()) date_field_.requestFocus();
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();
            setValue(date);
            return;
        }
        if (action.equals("time_up")) {
            if (!time_field_.hasFocus()) time_field_.requestFocus();
            calendar.add(Calendar.SECOND, STEP_INTERVAL);
            date = calendar.getTime();
            setValue(date);
            return;
        }
        if (action.equals("time_down")) {
            if (!time_field_.hasFocus()) time_field_.requestFocus();
            calendar.add(Calendar.SECOND, -STEP_INTERVAL);
            date = calendar.getTime();
            setValue(date);
            return;
        }
    }

    /**
   * Completes the time inserted in the text field.
   * e.g.: :3 -> 00:03
   *
   * @param time the time to complete 
   * @return the completed time.
   */
    public static String completeTime(String time) {
        String ret = time.trim();
        if (ret.indexOf(':') < 0) {
            try {
                int plain_time = Integer.parseInt(ret);
                if (plain_time < 24) ret += ":00"; else if (plain_time < 100) ret = "00:" + ret; else {
                    int ret_length = ret.length();
                    switch(ret_length) {
                        case 2:
                        case 3:
                            ret = ret.substring(0, 1) + ":" + ret.substring(1);
                            break;
                        case 4:
                            ret = ret.substring(0, 2) + ":" + ret.substring(2);
                            break;
                    }
                }
            } catch (NumberFormatException exc) {
                ret += ":00";
            }
        }
        return (ret);
    }

    /**
   * A Button that does not allow to have a focus.
   */
    class NoFocusButton extends JButton {

        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        /**
     * Constructor taking an ImageIcon as its argument.
     *
     * @param icon the icon.
     */
        public NoFocusButton(ImageIcon icon) {
            super(icon);
            setRequestFocusEnabled(false);
        }

        /**
     * Allways returns false.
     *
     * @return allways false
     */
        public boolean isFocusTraversable() {
            return (false);
        }
    }

    /**
   * An Action Implementation that restores the internal store value.
   *
   * @author Dieter Freismuth
   * @version $Revision: 1.4 $
   */
    class EscapeAction extends AbstractAction {

        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        /**
     * Default Constructor.
     */
        public EscapeAction() {
            super("reset-value");
        }

        /**
     * Restores the internal stored value.
     * 
     * @param event the ActionEvent.
     */
        public void actionPerformed(ActionEvent event) {
            updateValue();
        }
    }
}
