package net.sf.dz.view.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.dz.util.Data2Display;
import net.sf.dz.util.Round;

/**
 * Scheduler panel.
 *
 * Displays the schedule for an individual zone and allows to edit it.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2001-2004
 * @version $Id: SchedulePanel.java,v 1.36 2007-03-01 07:08:10 vtt Exp $
 */
public class SchedulePanel extends JPanel implements ListSelectionListener {

    /**
     * Two letter day names.
     */
    public static final String dayName[] = { "SU", "MO", "TU", "WE", "TH", "FR", "SA" };

    /**
     * Full day names.
     */
    public static final String longDayName[] = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

    /**
     * Font to be used for the schedule panel.
     *
     * Defaults to Times New Roman 10pt.
     */
    public static final Font smallFont = new Font("Times", Font.PLAIN, 10);

    private GridBagLayout layout;

    private GridBagConstraints cs;

    private ScheduleListener listener;

    private WeekPanel weekPanel;

    private DayPanel dayPanel;

    private ControlPanel controlPanel;

    private boolean displayFahrenheit;

    private SortedSet eventSet[] = new SortedSet[7];

    private ScheduleEvent currentEvent = null;

    /**
     * This variable controls how deeply nested we are.
     *
     * If the nesting level is non-zero, no notifications will be sent
     * because it means that the events we're parsing are secondary, caused
     * by enable/disable/select actions up the stack.
     *
     * <p>
     *
     * <strong>NOTE:</strong> Use {@link #push push()} and {@link #pop
     * pop()} to change the value - it will protect from stack mismatches.
     */
    int nestingLevel = 0;

    boolean enabled = true;

    public SchedulePanel(ScheduleListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener can't be null");
        }
        this.listener = listener;
        for (int i = 0; i < 7; i++) {
            eventSet[i] = new TreeSet<ScheduleEvent>();
        }
        layout = new GridBagLayout();
        cs = new GridBagConstraints();
        setLayout(layout);
        cs.fill = GridBagConstraints.HORIZONTAL;
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 4;
        cs.weightx = 1;
        cs.weighty = 0;
        weekPanel = new WeekPanel();
        layout.setConstraints(weekPanel, cs);
        add(weekPanel);
        cs.gridx += 4;
        cs.gridwidth = 2;
        cs.gridheight = 6;
        cs.weightx = 0;
        cs.weighty = 1;
        cs.fill = GridBagConstraints.BOTH;
        controlPanel = new ControlPanel(this);
        controlPanel.setEnabled(false);
        layout.setConstraints(controlPanel, cs);
        add(controlPanel);
        cs.gridx = 0;
        cs.gridy++;
        cs.gridwidth = 4;
        cs.gridheight = 5;
        cs.weighty = 1;
        dayPanel = new DayPanel();
        dayPanel.list.addListSelectionListener(this);
        layout.setConstraints(dayPanel, cs);
        add(dayPanel);
    }

    private void push() {
        nestingLevel++;
    }

    private void pop() {
        nestingLevel--;
        if (nestingLevel < 0) {
            throw new IllegalStateException("Nesting level is " + nestingLevel);
        }
    }

    /**
     * Parse the remaining part of the command.
     *
     * @param target Target selector. May be either "event", which means the
     * rest denotes the content of a particular event, or "current", which
     * means that the rest denotes the event that is currently running.
     *
     * @param st String tokenizer containing the event content.
     *
     * <ul>
     * 
     *     <li> First token should contain the two letter day name.
     *     <li> Second token should contain the event name.
     *
     * </ul>
     *
     * @exception IllegalArgumentException if there is a syntax violation or
     * unknown target.
     *
     * @see #parseEvent
     */
    public void parse(String target, StringTokenizer st) throws Throwable {
        push();
        try {
            String day = st.nextToken();
            String name = st.nextToken();
            int dayOffset = -1;
            for (int idx = 0; idx < 7; idx++) {
                if (dayName[idx].equals(day)) {
                    dayOffset = idx;
                    break;
                }
            }
            if (dayOffset == -1) {
                throw new IllegalArgumentException("Invalid day: '" + day + "'");
            }
            if ("event".equals(target)) {
                ScheduleEvent se = parseEvent(dayOffset, name, st);
                refreshEventSet(se);
            } else if ("current".equals(target)) {
                ScheduleEvent se = parseEvent(dayOffset, name, st);
                setCurrent(se);
            } else {
                throw new IllegalArgumentException("Unsupported target: '" + target + "'");
            }
            weekPanel.setSelectedDay(weekPanel.getSelectedDay());
        } finally {
            pop();
        }
    }

    /**
     * Parse the parameters and instantiate the event.
     *
     * @param dayOffset Day offset, with 0 being Sunday.
     *
     * @param name Event name.
     *
     * @param st Rest of the event parameters.
     *
     * <ul>
     *
     *     <li> First and second tokens should contain the event start time
     *          in 24 hours format.
     *
     *     <li> Third token should contain the setpoint as double.
     *
     *     <li> Fourth token should contain information about whether the
     *          zone is enabled during this particular event ("on" means
     *          yes, anything else means no).
     *
     *     <li> Fifth token should contain information about whether the
     *          zone is voting during this particular event ("voting" means
     *          yes, anything else means no).
     *
     *     <li> Sixth token should contain the dump priority as integer.
     *
     * </ul>
     */
    private ScheduleEvent parseEvent(int dayOffset, String name, StringTokenizer st) throws Throwable {
        ScheduleEvent e = new ScheduleEvent();
        e.dayOffset = dayOffset;
        e.name = name;
        String hour = st.nextToken();
        String minute = st.nextToken();
        if ("0".equals(minute)) {
            minute = "00";
        }
        e.time = hour + ":" + minute;
        e.setpoint = Double.parseDouble(st.nextToken());
        e.enabled = st.nextToken().equals("on");
        e.voting = st.nextToken().equals("voting");
        e.dumpPriority = Integer.parseInt(st.nextToken());
        return e;
    }

    /**
     * Add the event to the event set, or replace an existing one if it is
     * determined that the parameter represents a modified version of an
     * existing event.
     *
     * @param event Event to match against the known event set.
     */
    private void refreshEventSet(ScheduleEvent event) {
        try {
            ScheduleEvent current = null;
            SortedSet<ScheduleEvent> set = eventSet[event.dayOffset];
            for (Iterator<ScheduleEvent> i = set.iterator(); i.hasNext(); ) {
                ScheduleEvent se = i.next();
                if (event.name.equals(se.name)) {
                    current = se;
                    break;
                }
            }
            if (current == null) {
                set.add(event);
                return;
            } else {
                set.remove(current);
                set.add(event);
            }
        } finally {
            if (event.dayOffset == weekPanel.getSelectedDay()) {
                SortedSet events = eventSet[event.dayOffset];
                ScheduleEvent selectedEvent = (ScheduleEvent) dayPanel.list.getSelectedValue();
                if (selectedEvent != null) {
                    if ((event.dayOffset != selectedEvent.dayOffset) || (event.dumpPriority != selectedEvent.dumpPriority) || (event.enabled != selectedEvent.enabled) || (event.setpoint != selectedEvent.setpoint) || (event.voting != selectedEvent.voting) || !event.time.equals(selectedEvent.time)) {
                        push();
                        dayPanel.list.setListData(set2vector(events));
                        for (Iterator i = events.iterator(); i.hasNext(); ) {
                            ScheduleEvent se = (ScheduleEvent) i.next();
                            if (se.name.equals(selectedEvent.name)) {
                                dayPanel.list.setSelectedValue(se, false);
                                break;
                            }
                        }
                        pop();
                    }
                }
            }
        }
    }

    private void setCurrent(ScheduleEvent currentEvent) throws Throwable {
        this.currentEvent = currentEvent;
        weekPanel.setCurrentDay(currentEvent.dayOffset);
        weekPanel.setSelectedDay(currentEvent.dayOffset);
    }

    /**
     * Process the event list selection status change.
     *
     * If there is a selection, {@link #controlPanel control panel} is
     * enabled, otherwise it is disabled.
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        JList source = (JList) e.getSource();
        int index = source.getSelectedIndex();
        if (index == -1) {
            controlPanel.setEnabled(false);
            return;
        }
        ScheduleEvent se = (ScheduleEvent) source.getSelectedValue();
        controlPanel.enable(se);
    }

    /**
     * Convert a set into a vector.
     *
     * Sorted set is a proper way to keep data in our application, and the
     * vector is the way Swing wants it.
     *
     * <p>
     *
     * VT: NOTE: This method will cause a compilation warning. This is
     * unavoidable (within my JDK 1.5 generic knowledge, that is), and is a
     * necessary evil - otherwise, these warnings will be all over.
     */
    private Vector set2vector(SortedSet source) {
        return new Vector(source);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        weekPanel.setEnabled(enabled);
        dayPanel.setEnabled(enabled);
        controlPanel.setEnabled(enabled && dayPanel.list.getSelectedIndex() != -1);
    }

    public void setTemperatureUnits(String units) {
        displayFahrenheit = "F".equals(units);
    }

    /**
     * Weekday list panel.
     *
     * Displays the list of days of week and allows to select a day to
     * display on the {@link #dayPanel day panel}.
     *
     * <p>
     *
     * Current day is displayed in blue text. Selected day is displayed with
     * raised border around the day name.
     */
    protected class WeekPanel extends JPanel implements MouseListener {

        private GridBagLayout layout;

        private GridBagConstraints cs;

        private JLabel dayList[] = new JLabel[7];

        int selected = 0;

        int current = -1;

        boolean enabled = true;

        public WeekPanel() {
            System.err.println("FIXME: subclass JLabel to fixed width");
            setBorder(BorderFactory.createLoweredBevelBorder());
            layout = new GridBagLayout();
            cs = new GridBagConstraints();
            setLayout(layout);
            cs.fill = GridBagConstraints.HORIZONTAL;
            cs.gridx = 0;
            cs.gridy = 0;
            cs.gridwidth = 1;
            cs.weightx = 1;
            cs.weighty = 0;
            for (int day = 0; day < 7; day++) {
                JLabel b = new JLabel(dayName[day], JLabel.CENTER);
                b.addMouseListener(this);
                b.setToolTipText("Click to view " + longDayName[day] + " schedule");
                dayList[day] = b;
                layout.setConstraints(b, cs);
                add(b);
                cs.gridx++;
            }
            dayList[selected].setBorder(BorderFactory.createRaisedBevelBorder());
            if (current != -1) {
                dayList[current].setForeground(Color.blue);
            }
        }

        /**
         * Set the day to select.
         *
         * @param day Day to select, 0 being Sunday.
         */
        public synchronized void setSelectedDay(int day) {
            if (day < 0 || day > 6) {
                throw new IllegalArgumentException("Illegal day: " + day);
            }
            if (day == selected) {
            }
            dayList[selected].setBorder(BorderFactory.createEmptyBorder());
            dayList[day].setBorder(BorderFactory.createRaisedBevelBorder());
            selected = day;
            Object eventObject = dayPanel.list.getSelectedValue();
            String eventName = null;
            if (eventObject != null) {
                eventName = ((ScheduleEvent) eventObject).name;
            }
            dayPanel.list.setListData(set2vector(eventSet[day]));
            if (eventName != null) {
                SortedSet events = eventSet[day];
                for (Iterator i = events.iterator(); i.hasNext(); ) {
                    ScheduleEvent e = (ScheduleEvent) i.next();
                    if (eventName.equals(e.name)) {
                        dayPanel.list.setSelectedValue(e, true);
                        break;
                    }
                }
            }
        }

        /**
         * Get the selected day.
         *
         * @return Index of the selected day, 0 being Sunday.
         */
        public synchronized int getSelectedDay() {
            return selected;
        }

        /**
         * Set the day to display as current.
         *
         * @param day Day to highlight, 0 being Sunday.
         */
        public synchronized void setCurrentDay(int day) {
            if (day < 0 || day > 6) {
                throw new IllegalArgumentException("Illegal day: " + day);
            }
            if (day == current) {
            }
            if (current != -1) {
                dayList[current].setForeground(Color.black);
            }
            dayList[day].setForeground(Color.blue);
            current = day;
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (!enabled) {
                return;
            }
            Object source = e.getSource();
            for (int day = 0; day < 7; day++) {
                if (dayList[day] == source) {
                    setSelectedDay(day);
                    return;
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            for (int offset = 0; offset < dayList.length; offset++) {
                dayList[offset].setEnabled(enabled);
            }
        }
    }

    /**
     * Panel displaying the event list for the day.
     *
     * <p>
     *
     * Current event is displayed in blue text. Selected event is displayed
     * with a default list selection color background.
     */
    protected class DayPanel extends JPanel {

        private GridBagLayout layout;

        private GridBagConstraints cs;

        JList list;

        public DayPanel() {
            setBorder(BorderFactory.createLoweredBevelBorder());
            layout = new GridBagLayout();
            cs = new GridBagConstraints();
            setLayout(layout);
            cs.fill = GridBagConstraints.BOTH;
            cs.gridx = 0;
            cs.gridy = 0;
            cs.gridwidth = 1;
            cs.weightx = 1;
            cs.weighty = 1;
            JScrollPane sp = new JScrollPane();
            list = new JList(set2vector(eventSet[0]));
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setCellRenderer(new ScheduleItemRenderer());
            list.setToolTipText("Select period to edit");
            JLabel l = new JLabel("XX");
            Dimension d = l.getPreferredSize();
            sp.setMinimumSize(new Dimension(d.width * 12, d.height * 4));
            sp.setPreferredSize(new Dimension(d.width * 12, d.height * 4));
            sp.getViewport().setView(list);
            layout.setConstraints(sp, cs);
            add(sp);
        }

        public ScheduleEvent getSelectedEvent() {
            return (ScheduleEvent) list.getSelectedValue();
        }

        public void setEnabled(boolean enabled) {
            list.setEnabled(enabled);
        }
    }

    /**
     * Controls for the schedule panel.
     *
     * Allow to modify the selected day/event's start time, setpoint,
     * whether it is disabled and/or voting, and dump priority.
     */
    protected class ControlPanel extends JPanel implements ActionListener, ItemListener {

        private GridBagLayout layout;

        private GridBagConstraints cs;

        private final String[] dumpValues = { "Off", "High", "Medium", "Low" };

        private JButton timeUp = new JButton(">>");

        private JButton timeDown = new JButton("<<");

        private JButton tempUp = new JButton("+");

        private JButton tempDown = new JButton("-");

        private JCheckBox offBox = new JCheckBox("Off");

        private JCheckBox votingBox = new JCheckBox("Voting");

        private JComboBox dumpSelector = new JComboBox(dumpValues);

        private SchedulePanel owner;

        public ControlPanel(SchedulePanel owner) {
            this.owner = owner;
            timeUp.addActionListener(this);
            timeDown.addActionListener(this);
            tempUp.addActionListener(this);
            tempDown.addActionListener(this);
            offBox.addItemListener(this);
            votingBox.addItemListener(this);
            dumpSelector.addActionListener(this);
            timeUp.setFont(smallFont);
            timeDown.setFont(smallFont);
            tempUp.setFont(smallFont);
            tempDown.setFont(smallFont);
            offBox.setFont(smallFont);
            votingBox.setFont(smallFont);
            dumpSelector.setFont(smallFont);
            timeUp.setToolTipText("10 min forward");
            timeDown.setToolTipText("10 min back");
            tempUp.setToolTipText("Warmer");
            tempDown.setToolTipText("Cooler");
            offBox.setToolTipText("Is zone disabled?");
            votingBox.setToolTipText("Is zone voting?");
            dumpSelector.setToolTipText("Dump priority");
            setBorder(BorderFactory.createLoweredBevelBorder());
            layout = new GridBagLayout();
            cs = new GridBagConstraints();
            setLayout(layout);
            cs.fill = GridBagConstraints.BOTH;
            cs.gridx = 0;
            cs.gridy = 0;
            cs.gridwidth = 1;
            cs.weightx = 0;
            cs.weighty = 1;
            layout.setConstraints(timeUp, cs);
            add(timeUp);
            cs.gridx++;
            layout.setConstraints(tempUp, cs);
            add(tempUp);
            cs.gridx = 0;
            cs.gridy++;
            layout.setConstraints(timeDown, cs);
            add(timeDown);
            cs.gridx++;
            layout.setConstraints(tempDown, cs);
            add(tempDown);
            cs.gridx = 0;
            cs.gridy++;
            cs.gridwidth = 1;
            cs.weightx = 1;
            cs.weighty = 0;
            cs.fill = GridBagConstraints.HORIZONTAL;
            layout.setConstraints(votingBox, cs);
            add(votingBox);
            cs.gridx++;
            layout.setConstraints(offBox, cs);
            add(offBox);
            cs.gridx = 0;
            cs.gridy++;
            cs.gridwidth = 2;
            layout.setConstraints(dumpSelector, cs);
            add(dumpSelector);
            setEnabled(false);
        }

        public void enable(ScheduleEvent se) {
            push();
            try {
                setEnabled(true);
                offBox.setSelected(!se.enabled);
                votingBox.setSelected(se.voting);
                dumpSelector.setSelectedIndex(se.dumpPriority);
            } finally {
                pop();
            }
        }

        /**
         * Set the enabled/disabled state of the controls.
         *
         * This is required in case of empty list selection.  Default state
         * is disabled.
         */
        public void setEnabled(boolean enabled) {
            push();
            timeUp.setEnabled(enabled);
            timeDown.setEnabled(enabled);
            tempUp.setEnabled(enabled);
            tempDown.setEnabled(enabled);
            offBox.setEnabled(enabled);
            votingBox.setEnabled(enabled);
            dumpSelector.setEnabled(enabled);
            if (enabled && currentEvent != null) {
                offBox.setSelected(!currentEvent.enabled);
                votingBox.setSelected(currentEvent.voting);
                dumpSelector.setSelectedIndex(currentEvent.dumpPriority);
            } else {
                offBox.setSelected(false);
                votingBox.setSelected(false);
                dumpSelector.setSelectedIndex(0);
            }
            pop();
        }

        /**
         * Handless >>, <<, +, - buttons and dump priority combo.
         */
        public void actionPerformed(ActionEvent e) {
            ScheduleEvent origin = dayPanel == null ? null : dayPanel.getSelectedEvent();
            if (origin == null) {
                return;
            }
            ScheduleEvent clone = new ScheduleEvent(origin);
            Object source = e.getSource();
            if (source == timeUp) {
                clone.setTime((clone.getTimeAsInt().intValue() + 10) % (60 * 24));
            } else if (source == timeDown) {
                clone.setTime((clone.getTimeAsInt().intValue() - 10 + (60 * 24)) % (60 * 24));
            } else if (source == tempUp) {
                double displaySetpoint = Data2Display.data2display(clone.setpoint, displayFahrenheit) + 0.5;
                clone.setpoint = Round.round1(Data2Display.display2data(displaySetpoint, displayFahrenheit));
            } else if (source == tempDown) {
                double displaySetpoint = Data2Display.data2display(clone.setpoint, displayFahrenheit) - 0.5;
                clone.setpoint = Round.round1(Data2Display.display2data(displaySetpoint, displayFahrenheit));
            } else if (source == dumpSelector) {
                clone.dumpPriority = ((JComboBox) source).getSelectedIndex();
            }
            sendEventChangeNotification(clone);
        }

        /**
         * Handles "off" and "voting" checkboxes.
         */
        public void itemStateChanged(ItemEvent e) {
            ScheduleEvent origin = dayPanel.getSelectedEvent();
            if (origin == null) {
                return;
            }
            ScheduleEvent clone = new ScheduleEvent(origin);
            JCheckBox source = (JCheckBox) e.getSource();
            boolean selected = source.isSelected();
            if (source == offBox) {
                clone.enabled = !selected;
                tempUp.setEnabled(!selected);
                tempDown.setEnabled(!selected);
                votingBox.setEnabled(!selected);
                dumpSelector.setEnabled(!selected);
            } else if (source == votingBox) {
                clone.voting = selected;
            }
            sendEventChangeNotification(clone);
        }

        public void sendEventChangeNotification(ScheduleEvent target) {
            if (nestingLevel != 0) {
                return;
            }
            if (target != null) {
                listener.scheduleEventChanged(owner, target.toString());
            }
        }
    }

    /**
     * A renderer for the schedule event.
     */
    protected class ScheduleItemRenderer extends JPanel implements ListCellRenderer {

        private JLabel period;

        private JLabel time;

        private JLabel temp;

        private final DateFormat sourceDateFormat;

        private final DateFormat localeDateFormat;

        ScheduleItemRenderer() {
            sourceDateFormat = new SimpleDateFormat("HH:mm");
            localeDateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
            setLayout(new GridLayout());
            period = new JLabel("", JLabel.LEFT);
            time = new JLabel("", JLabel.RIGHT);
            temp = new JLabel("", JLabel.CENTER);
            add(period);
            add(time);
            add(temp);
        }

        public Component getListCellRendererComponent(JList list, Object entry, int index, boolean isSelected, boolean cellHasFocus) {
            ScheduleEvent se = (ScheduleEvent) entry;
            period.setText(se.name);
            String ignore = System.getProperty("display.ignore.time.locale");
            if (ignore != null) {
                time.setText(se.time);
            } else {
                try {
                    Date d = sourceDateFormat.parse(se.time);
                    time.setText(localeDateFormat.format(d));
                } catch (ParseException pex) {
                    time.setText(se.time);
                    System.err.println("Incredible! Bad time format? (time: '" + se.time + "')");
                    pex.printStackTrace();
                }
            }
            if (se.enabled) {
                temp.setText(Round.round1(Data2Display.data2display(se.setpoint, displayFahrenheit)) + "°" + (displayFahrenheit ? "F" : "C"));
            } else {
                temp.setText("OFF");
            }
            period.setFont(list.getFont());
            time.setFont(list.getFont());
            temp.setFont(list.getFont());
            Color displayBg;
            Color displayFg;
            if (isSelected) {
                displayBg = list.getSelectionBackground();
                displayFg = list.getSelectionForeground();
            } else {
                displayBg = list.getBackground();
                displayFg = list.getForeground();
            }
            if (weekPanel.current == weekPanel.selected && currentEvent != null && se.name.equals(currentEvent.name)) {
                displayFg = Color.blue;
            }
            setForeground(displayFg);
            setBackground(displayBg);
            setEnabled(list.isEnabled());
            period.setForeground(displayFg);
            period.setBackground(displayBg);
            period.setEnabled(list.isEnabled());
            time.setForeground(displayFg);
            time.setBackground(displayBg);
            time.setEnabled(list.isEnabled());
            temp.setForeground(displayFg);
            temp.setBackground(displayBg);
            temp.setEnabled(list.isEnabled());
            return this;
        }
    }

    /**
     * Schedule event description.
     */
    protected class ScheduleEvent implements Comparable {

        /**
         * Minimum allowed temperature, C°.
         */
        public final double TEMP_MIN = 15;

        /**
         * Maximum allowed temperature, C°.
         */
        public final double TEMP_MAX = 32;

        /**
         * Day of week, 0 being Sunday.
         */
        int dayOffset;

        /**
         * Event name.
         */
        String name;

        /**
         * Start time.
         */
        String time;

        /**
         * Temperature to hold.
         */
        double setpoint;

        /**
         * True if the zone is not shut off.
         */
        boolean enabled;

        /**
         * True if the zone is voting.
         */
        boolean voting;

        /**
         * Dump priority of the zone in the current period.
         */
        int dumpPriority;

        public ScheduleEvent() {
        }

        public ScheduleEvent(ScheduleEvent template) {
            this.dayOffset = template.dayOffset;
            this.name = template.name;
            this.time = template.time;
            this.setpoint = template.setpoint;
            this.enabled = template.enabled;
            this.voting = template.voting;
            this.dumpPriority = template.dumpPriority;
        }

        public int compareTo(Object other) {
            if (other == null) {
                throw new IllegalArgumentException("Other can't be null");
            }
            if (!(other instanceof ScheduleEvent)) {
                throw new IllegalArgumentException("Expected " + getClass().getName() + ", got " + other.getClass().getName());
            }
            ScheduleEvent otherEvent = (ScheduleEvent) other;
            int match = getTimeAsInt().compareTo(otherEvent.getTimeAsInt());
            if (match != 0) {
                return match;
            }
            return name.compareTo(otherEvent.name);
        }

        public Integer getTimeAsInt() {
            StringTokenizer st = new StringTokenizer(time, ":");
            String hour = st.nextToken();
            String minute = st.nextToken();
            return new Integer((Integer.parseInt(hour) * 60) + Integer.parseInt(minute));
        }

        public void setTime(int value) {
            int minute = value % 60;
            int hour = (value - minute) / 60;
            time = "" + hour + ":" + minute;
        }

        public String toString() {
            String message = dayName[dayOffset] + ":" + name + ":" + time + ":" + setpoint + ":" + (enabled ? "on" : "-") + ":" + (voting ? "voting" : "-") + ":" + dumpPriority;
            return message;
        }
    }
}
