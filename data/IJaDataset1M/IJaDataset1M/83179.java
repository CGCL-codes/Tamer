package org.dinnermate.jcalendar;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import java.beans.*;
import javax.swing.plaf.metal.MetalComboBoxUI;
import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;
import java.text.*;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.accessibility.*;

public class JCalendarCombo extends JComboBox {

    /**
 * Used to indicate that this component should display the date.
 */
    public static final int DISPLAY_DATE = JCalendar.DISPLAY_DATE;

    /**
 * Used to indicate that this component should display the time.
 */
    public static final int DISPLAY_TIME = JCalendar.DISPLAY_TIME;

    static JCalendarCombo currentPopup = null;

    private Locale locale;

    private DateFormat dateFormat;

    private DateFormat parseFormat[];

    private DateFormat timePatternFormat;

    private Window parentWindow;

    private JWindow oldWindow;

    private JWindow calendarWindow;

    private JCalendar calendarPanel;

    private boolean isCalendarDisplayed = false;

    private Date originalDate = null;

    private Calendar cacheCalendar = null;

    private static InputMap inputMap = new InputMap();

    private static InputMap spinnerInputMap = new InputMap();

    private static ActionMap actionMap = new ActionMap();

    private static Action setNullDate = new AbstractAction("setNullDate") {

        public void actionPerformed(ActionEvent e) {
            ((JCalendar) e.getSource()).setDate(null);
            ((JCalendar) e.getSource()).getJCalendarComboParent().hideCalendar();
        }
    };

    private static Action cancel = new AbstractAction("cancel") {

        public void actionPerformed(ActionEvent e) {
            JCalendar cal = (JCalendar) e.getSource();
            JCalendarCombo calCombo = cal.getJCalendarComboParent();
            cal.setDate(calCombo.originalDate);
            calCombo.firePopupMenuCanceled();
            calCombo.hideCalendar();
        }
    };

    private static Action apply = new AbstractAction("apply") {

        public void actionPerformed(ActionEvent e) {
            JCalendar cal = (JCalendar) e.getSource();
            JCalendarCombo calCombo = cal.getJCalendarComboParent();
            calCombo.hideCalendar();
        }
    };

    static {
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "apply");
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "apply");
        inputMap.setParent(JCalendar.inputMap);
        actionMap.put("cancel", cancel);
        actionMap.put("apply", apply);
        actionMap.setParent(JCalendar.actionMap);
    }

    /**
 * Create an instance of JCalendarCombo using the default calendar and
 * locale. Display the date but not the time. Don't display today's
 * date.
 */
    public JCalendarCombo() {
        this(Calendar.getInstance(), Locale.getDefault(), DISPLAY_DATE, false, null);
    }

    /**
 * Create an instance of JCalendarCombo using the default calendar and
 * locale. Display a calendar and a time spinner as requested (to
 * display both use DISPLAY_DATE | DISPLAY_TIME). Display today's
 * date if requested. Use the default pattern to display the time in the
 * time spinner field (if there is one).
 *
 * @param selectedComponents Use DISPLAY_DATE, DISPLAY_TIME or
 * 	(DISPLAY_DATE | DISPLAY_TIME).
 * @param isTodayDisplayed True if today's date should be displayed.
 */
    public JCalendarCombo(int selectedComponents, boolean isTodayDisplayed) {
        this(Calendar.getInstance(), Locale.getDefault(), selectedComponents, isTodayDisplayed, null);
    }

    /**
 * Create an instance of JCalendarCombo using the given calendar and
 * locale. Display a calendar and a time spinner as requested (to
 * display both use DISPLAY_DATE | DISPLAY_TIME). Display today's
 * date if requested. Use the default pattern to display the time in the
 * time spinner field (if there is one).
 *
 * @param calendar The calendar to use.
 * @param locale The locale to use.
 * @param selectedComponents Use DISPLAY_DATE, DISPLAY_TIME or
 * 	(DISPLAY_DATE | DISPLAY_TIME).
 * @param isTodayDisplayed True if today's date should be displayed.
 */
    public JCalendarCombo(Calendar calendar, Locale locale, int selectedComponents, boolean isTodayDisplayed) {
        this(calendar, locale, selectedComponents, isTodayDisplayed, null);
    }

    /**
 * Create an instance of JCalendarCombo using the given calendar and
 * locale. Display a calendar and a time spinner as requested (to
 * display both use DISPLAY_DATE | DISPLAY_TIME). Display today's
 * date if requested.  Use the default pattern to display the time in the
 * time spinner field (if there is one).
 *
 * @param calendar The calendar to use.
 * @param locale The locale to use.
 * @param selectedComponents Use DISPLAY_DATE, DISPLAY_TIME or
 * 	(DISPLAY_DATE | DISPLAY_TIME).
 * @param isTodayDisplayed True if today's date should be displayed.
 * @see DateFormat
 * @see SimpleDateFormat
 */
    public JCalendarCombo(Calendar calendar, Locale locale, int selectedComponents, boolean isTodayDisplayed, String timePattern) {
        super();
        calendarPanel = new JCalendar(calendar, locale, selectedComponents, isTodayDisplayed, timePattern);
        this.locale = locale;
        parseFormat = new DateFormat[12];
        parseFormat[0] = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        parseFormat[1] = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
        parseFormat[2] = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        parseFormat[3] = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale);
        parseFormat[4] = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        parseFormat[5] = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        parseFormat[6] = DateFormat.getDateInstance(DateFormat.LONG, locale);
        parseFormat[7] = DateFormat.getDateInstance(DateFormat.FULL, locale);
        parseFormat[8] = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
        parseFormat[9] = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
        parseFormat[10] = DateFormat.getTimeInstance(DateFormat.LONG, locale);
        parseFormat[11] = DateFormat.getTimeInstance(DateFormat.FULL, locale);
        if (timePattern != null) {
            timePatternFormat = new SimpleDateFormat(timePattern, locale);
        }
        if (selectedComponents == DISPLAY_DATE) {
            setDateFormat(DateFormat.getDateInstance(DateFormat.FULL, locale));
        } else if (selectedComponents == DISPLAY_TIME) {
            setDateFormat(DateFormat.getTimeInstance(DateFormat.FULL, locale));
            if (timePattern != null) {
                timePatternFormat = new SimpleDateFormat(timePattern, locale);
            }
        } else {
            setDateFormat(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale));
        }
        super.setModel(new CalendarComboBoxModel());
        super.setKeySelectionManager(new CalendarKeySelectionManager());
        createCalendarComponents();
    }

    /**
 * Add a date listener. This listener will receive events each time
 * the selected date changes.
 *
 * @param listener The date listener to add.
 */
    public void addDateListener(DateListener listener) {
        calendarPanel.addDateListener(listener);
    }

    /**
 * Remove a date listener.
 *
 * @param listener The date listener to remove.
 */
    public void removeDateListener(DateListener listener) {
        calendarPanel.removeDateListener(listener);
    }

    /**
 * Get whether a null date is allowed.
 *
 * @return The whether a null date is allowed.
 */
    public boolean isNullAllowed() {
        return calendarPanel.isNullAllowed();
    }

    /**
 * Set whether a null date is allowed. A null date means that no date
 * is selected. The user can select a null date by pressing DELETE
 * anywhere within the calendar.
 * <p>
 * If nulls are not allowed, a setDate(null) will be ignored without
 * error. The DELETE key will do nothing.
 * <p>
 * If you switch from allowing nulls to not allowing nulls and the
 * current date is null, it will remain null until a date is selected.
 * <p>
 * The component default is to allow nulls.
 *
 * @param isNullAllowed The whether a null date is allowed.
 */
    public void setNullAllowed(boolean isNullAllowed) {
        calendarPanel.setNullAllowed(isNullAllowed);
    }

    /**
 * Get the date currently displayed by the calendar panel. If null,
 * then no date was selected.
 *
 * @return The date currently displayed.
 * @see #getCalendar
 */
    public Date getDate() {
        return calendarPanel.getDate();
    }

    /**
 * Set the calendar panel to display the given date. This will fire a
 * DateEvent.
 *
 * @param date The date to set.
 */
    public void setDate(Date date) {
        calendarPanel.setDate(date);
    }

    /**
 * Get the date format used to display the selected date in the combo
 * box's text field.
 *
 * @return The date format used to display the selected date in the
 *	combo box's text field.
 */
    public DateFormat getDateFormat() {
        return dateFormat;
    }

    /**
 * Set the date format used to display the selected date in the combo
 * box's text field. Nulls are not allowed.
 *
 * @param dateFormat The date format used to display the selected date
 * 	in the combo box's text field.
 * @throws java.lang.NullPointerException
 */
    public void setDateFormat(DateFormat dateFormat) throws NullPointerException {
        if (dateFormat == null) {
            throw new NullPointerException("Date format cannot be null.");
        }
        this.dateFormat = dateFormat;
    }

    /**
 * Get a copy of the calendar used by this JCalendar. This calendar
 * will be set to the currently selected date, so it is an alternative
 * to the getDate() method.
 *
 * @return A copy of the calendar used by JCalendar.
 * @see #getDate
 */
    public Calendar getCalendar() {
        return calendarPanel.getCalendar();
    }

    /**
 * Return the locale used by this JCalendar.
 *
 * @return The locale used by this JCalendar.
 */
    public Locale getLocale() {
        return locale;
    }

    /**
 * Returns true if today's date is displayed at the bottom of the
 * calendar.
 *
 * @return True if today's date is displayed at the bottom of the
 * 	calendar.
 */
    public boolean isTodayDisplayed() {
        return calendarPanel.isTodayDisplayed();
    }

    /**
 * Get the title font.
 *
 * @return The title font.
 */
    public Font getTitleFont() {
        return calendarPanel.getTitleFont();
    }

    /**
 * If the font is set to null, then the title font (for the Month Year
 * title) will default to the L&amp;F's Label default font.
 * <p>
 * Otherwise, the title font is set as given.
 *
 * @param font The font to set.
 */
    public void setTitleFont(Font font) {
        calendarPanel.setTitleFont(font);
    }

    /**
 * Get the day-of-week font (Mon, Tue, etc.).
 *
 * @return The day-of-week font.
 */
    public Font getDayOfWeekFont() {
        return calendarPanel.getDayOfWeekFont();
    }

    /**
 * If the font is set to null, then the day-of-week font (Mon, Tue,
 * etc.) will default to 9/11th's of the L&amp;F's Label default font.
 * <p>
 * Otherwise, the day-of-week font is set as given.
 *
 * @param font The font to set.
 */
    public void setDayOfWeekFont(Font font) {
        calendarPanel.setDayOfWeekFont(font);
    }

    /**
 * Get the day font.
 *
 * @return The day font.
 */
    public Font getDayFont() {
        return calendarPanel.getDayFont();
    }

    /**
 * If the font is set to null, then the day font will default to
 * 9/11th's of the L&amp;F's Button default font.
 * <p>
 * Otherwise, the day font is set as given.
 *
 * @param font The font to set.
 */
    public void setDayFont(Font font) {
        calendarPanel.setDayFont(font);
    }

    /**
 * Get the time spinner font.
 *
 * @return The time spinner font.
 */
    public Font getTimeFont() {
        return calendarPanel.getTimeFont();
    }

    /**
 * If the font is set to null, then the time spinner font will default
 * to the L&amp;F's Spinner default font.
 * <p>
 * Otherwise, the time spinner font is set as given.
 *
 * @param font The font to set.
 */
    public void setTimeFont(Font font) {
        calendarPanel.setTimeFont(font);
    }

    /**
 * Get the font used to display today's date as text.
 *
 * @return The font used to display today's date.
 */
    public Font getTodayFont() {
        return calendarPanel.getTodayFont();
    }

    /**
 * If the font is set to null, then the font used to display today's
 * date as text will default to the L&amp;F's Label default font.
 * <p>
 * Otherwise, the font used to display today's date is set as given.
 *
 * @param font The font to set.
 */
    public void setTodayFont(Font font) {
        calendarPanel.setTodayFont(font);
    }

    /**
 * Sets the selected item in the combo box display area to the object
 * in the argument. The object should be a String representation of a
 * date.
 * <p>
 * If this constitutes a change in the selected item, ItemListeners
 * added to the combo box will be notified with one or two ItemEvents.
 * If there is a current selected item, an ItemEvent will be fired and
 * the state change will be ItemEvent.DESELECTED. If anObject is in
 * the list and is not currently selected then an ItemEvent will be
 * fired and the state change will be ItemEvent.SELECTED.
 * <p>ActionListeners added to the combo box will be notified with an
 * ActionEvent when this method is called (assuming the date actually
 * changed).
 *
 * @param anObject The object to select.
 */
    public void setSelectedItem(Object anObject) {
        getModel().setSelectedItem(anObject);
    }

    /**
 * This method is ignored. You cannot change the KeySelectionManager
 * for JCalendarCombo.
 *
 * @param aManager The new key selection manager.
 */
    public void setKeySelectionManager(JComboBox.KeySelectionManager aManager) {
    }

    /**
 * Resets the UI property to a value from the current look and feel.
 * Read the class documentation for instructions on how to override
 * this to make the JCalendarCombo support a new Look-and-Feel.
 */
    public void updateUI() {
        ComboBoxUI cui = (ComboBoxUI) UIManager.getUI(this);
        if (cui instanceof WindowsComboBoxUI) {
            cui = new WindowsDateComboBoxUI();
        } else if (cui instanceof MetalComboBoxUI) {
            cui = new MetalDateComboBoxUI();
        } else if (cui instanceof MotifComboBoxUI) {
            cui = new MotifDateComboBoxUI();
        } else {
            cui = new MetalDateComboBoxUI();
        }
        setUI(cui);
    }

    /**
 * {@inheritDoc}
 */
    protected String paramString() {
        int selectedComponents = calendarPanel.getSelectedComponents();
        String curDate;
        if ((selectedComponents & DISPLAY_DATE) == DISPLAY_DATE) {
            curDate = DateFormat.getDateInstance(DateFormat.FULL, locale).format(getDate());
        } else if ((selectedComponents & DISPLAY_TIME) == DISPLAY_TIME) {
            curDate = DateFormat.getTimeInstance(DateFormat.FULL, locale).format(getDate());
        } else {
            curDate = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale).format(getDate());
        }
        return super.paramString() + ",selectedDate=" + curDate;
    }

    /**
 * Given a date in String form, convert it to a Date object. If no
 * conversion is possible, return null. This method tries to parse
 * the string using DateFormat and SHORT, MEDIUM, LONG and FULL forms.
 * If none of these work, a null date is returned.
 *
 * @param string The date in String form.
 * @return The equivalent Date object or null.
 */
    protected Date stringToDate(String string) {
        if (string == null || string.length() < 1) return null;
        Date date = null;
        if (timePatternFormat != null) {
            try {
                date = timePatternFormat.parse(string);
            } catch (ParseException e) {
            }
        }
        try {
            date = dateFormat.parse(string);
        } catch (ParseException e) {
        }
        if (date == null) {
            for (int i = 0; i < parseFormat.length; i++) {
                try {
                    date = parseFormat[i].parse(string);
                } catch (ParseException e) {
                }
                if (date != null) break;
            }
        }
        return date;
    }

    /**
 * Set up the calendar combo box layout and components. These are not
 * date specific.
 */
    private void createCalendarComponents() {
        calendarPanel.setJCalendarComboParent(this);
        calendarPanel.setInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);
        calendarPanel.setActionMap(actionMap);
        if ((calendarPanel.getSelectedComponents() & DISPLAY_TIME) > 0) {
            InputMap sim = new InputMap();
            sim.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
            sim.put(KeyStroke.getKeyStroke("ENTER"), "apply");
            ActionMap sam = new ActionMap();
            sam.put("cancel", new AbstractAction("cancel") {

                public void actionPerformed(ActionEvent e) {
                    JCalendarCombo calCombo = JCalendarCombo.this;
                    calendarPanel.setDate(calCombo.originalDate);
                    calCombo.firePopupMenuCanceled();
                    calCombo.hideCalendar();
                }
            });
            sam.put("apply", new AbstractAction("apply") {

                public void actionPerformed(ActionEvent e) {
                    JCalendarCombo calCombo = JCalendarCombo.this;
                    calCombo.hideCalendar();
                }
            });
            calendarPanel.addSpinnerMaps(sim, sam);
        }
        Border border = (Border) UIManager.get("PopupMenu.border");
        if (border == null) {
            border = new BevelBorder(BevelBorder.RAISED);
        }
        calendarPanel.setBorder(border);
        calendarPanel.addDateListener(new CalDateListener());
        addAncestorListener(new ComboAncestorListener());
    }

    /**
 * Make the calendar panel invisible.
 */
    private void hideCalendar() {
        if (isCalendarDisplayed) {
            firePopupMenuWillBecomeInvisible();
            calendarWindow.setVisible(false);
            isCalendarDisplayed = false;
            requestFocus();
            if (currentPopup == this) currentPopup = null;
        }
    }

    /**
 * Make the calendar panel visible.
 */
    private void showCalendar() {
        if (!isCalendarDisplayed) {
            if (currentPopup != null) currentPopup.hideCalendar();
            firePopupMenuWillBecomeVisible();
            if (isEditable()) {
                setSelectedItem(getEditor().getItem());
                getEditor().selectAll();
            }
            Window oldParentWindow = parentWindow;
            parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow == null) return;
            if (calendarWindow == null || parentWindow != oldParentWindow) {
                if (calendarWindow != null) calendarWindow.dispose();
                calendarWindow = new JWindow(parentWindow);
                calendarWindow.getContentPane().add(calendarPanel);
                calendarWindow.pack();
            }
            Point fieldLocation = getLocationOnScreen();
            Dimension fieldSize = getSize();
            Dimension windowSize = calendarWindow.getSize();
            Dimension screenSize = getToolkit().getScreenSize();
            int x = fieldLocation.x + (fieldSize.width - windowSize.width);
            int y = fieldLocation.y + fieldSize.height;
            if (x + windowSize.width > screenSize.width) {
                x = screenSize.width - windowSize.width;
            }
            if (x < 0) x = 0;
            if (y + windowSize.height > screenSize.height) {
                y = fieldLocation.y - windowSize.height;
            }
            originalDate = calendarPanel.getDate();
            calendarPanel.setDisplayDate(originalDate);
            cacheCalendar = calendarPanel.getCalendar();
            calendarWindow.setLocation(x, y);
            calendarWindow.setVisible(true);
            isCalendarDisplayed = true;
            currentPopup = this;
        }
    }

    /**
 * Toggle the display of the calendar panel.
 */
    private void toggleCalendar() {
        if (isCalendarDisplayed) {
            hideCalendar();
        } else {
            showCalendar();
        }
    }

    private class MetalDateComboBoxUI extends MetalComboBoxUI {

        protected ComboPopup createPopup() {
            return new CalendarComboPopup();
        }
    }

    class WindowsDateComboBoxUI extends WindowsComboBoxUI {

        protected ComboPopup createPopup() {
            return new CalendarComboPopup();
        }
    }

    class MotifDateComboBoxUI extends MotifComboBoxUI {

        protected ComboPopup createPopup() {
            return new CalendarComboPopup();
        }
    }

    private class CalDateListener implements DateListener {

        public void dateChanged(DateEvent e) {
            Calendar cal = e.getSelectedDate();
            if (cal == null) {
                setSelectedItem(null);
            } else {
                setSelectedItem(dateFormat.format(e.getSelectedDate().getTime()));
            }
            if (cal == null && cacheCalendar == null) return;
            if (cal != null && cacheCalendar != null) {
                if (cal.get(Calendar.YEAR) == cacheCalendar.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == cacheCalendar.get(Calendar.MONTH) && cal.get(Calendar.DATE) == cacheCalendar.get(Calendar.DATE)) {
                    return;
                }
            }
            hideCalendar();
        }
    }

    protected class CalendarComboBoxModel implements ComboBoxModel {

        protected EventListenerList listenerList = new EventListenerList();

        public void addListDataListener(ListDataListener l) {
            listenerList.add(ListDataListener.class, l);
        }

        public void removeListDataListener(ListDataListener l) {
            listenerList.remove(ListDataListener.class, l);
        }

        public Object getElementAt(int index) {
            return getSelectedItem();
        }

        public int getSize() {
            return 1;
        }

        public Object getSelectedItem() {
            Date date = calendarPanel.getDate();
            if (date == null) return "";
            return dateFormat.format(date);
        }

        public void setSelectedItem(Object anItem) {
            Date date = null;
            if (anItem != null) date = stringToDate(anItem.toString());
            boolean fireEvent = false;
            Date calDate = calendarPanel.getDate();
            if (date == null && calDate != null || date != null && !date.equals(calDate)) {
                fireEvent = true;
                calendarPanel.setDate(date);
            }
            if (isEditable()) {
                Object editorItem = getEditor().getItem();
                Date editorDate = null;
                if (editorItem != null) {
                    editorDate = stringToDate(editorItem.toString());
                }
                if (date == null && editorDate != null || date != null && !date.equals(editorDate)) {
                    fireEvent = true;
                    if (date == null) {
                        getEditor().setItem("");
                    } else {
                        getEditor().setItem(dateFormat.format(date));
                    }
                }
            }
            if (fireEvent) {
                fireContentsChanged(this, -1, -1);
            }
        }

        private void fireContentsChanged(Object source, int index0, int index1) {
            Object[] listeners = listenerList.getListenerList();
            ListDataEvent e = null;
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ListDataListener.class) {
                    if (e == null) {
                        e = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index0, index1);
                    }
                    ((ListDataListener) listeners[i + 1]).contentsChanged(e);
                }
            }
        }
    }

    private class CalendarKeySelectionManager implements JComboBox.KeySelectionManager {

        public int selectionForKey(char aKey, ComboBoxModel aModel) {
            return -1;
        }
    }

    protected class CalendarComboPopup implements ComboPopup {

        private JList list = new JList();

        private MouseListener mouseListener = null;

        public KeyListener getKeyListener() {
            return null;
        }

        public JList getList() {
            return list;
        }

        public MouseListener getMouseListener() {
            if (mouseListener == null) {
                mouseListener = new InvocationMouseListener();
            }
            return mouseListener;
        }

        public MouseMotionListener getMouseMotionListener() {
            return null;
        }

        public boolean isVisible() {
            return isCalendarDisplayed;
        }

        public void hide() {
            hideCalendar();
        }

        public void show() {
            showCalendar();
        }

        public void uninstallingUI() {
        }
    }

    private class ComboAncestorListener implements AncestorListener {

        public void ancestorAdded(AncestorEvent e) {
            hideCalendar();
        }

        public void ancestorRemoved(AncestorEvent e) {
            hideCalendar();
        }

        public void ancestorMoved(AncestorEvent e) {
            hideCalendar();
        }
    }

    private class InvocationMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e) || !isEnabled()) return;
            toggleCalendar();
        }
    }
}
