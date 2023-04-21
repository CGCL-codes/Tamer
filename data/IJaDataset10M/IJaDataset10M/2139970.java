package wizworld.awt;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import wizworld.util.DateTime;
import wizworld.util.Resources;

/** Date time control
 * @author (c) Stephen Denham 2003
 * @version 0.1
 */
public final class DateTimeDialog extends Dialog implements ActionListener {

    private static final long serialVersionUID = -1147934822933942484L;

    /** Locale specific month names */
    private static String[] monthNames = new String[DateTime.MONTHS_IN_YEAR];

    /** Locale specific day names */
    private static String[] dayNames = new String[DateTime.DAYS_IN_WEEK];

    /** Parent frame */
    private JFrame parent;

    /** Date/time displayed */
    private GregorianCalendar dateTime;

    /** Date/time original */
    private GregorianCalendar dateTimeStart;

    /** Date time clock */
    private DateTime clock;

    /** Label showing month */
    private DateTimeLabel monthLabel;

    /** Label showing year */
    private JLabel yearLabel;

    /** Label showing time */
    private DateTimeLabel timeLabel;

    /** Grid of days in month */
    private JPanel dayGrid;

    /** Date spin buttons */
    private Button[] dateButton = new Button[4];

    /** Time spin buttons */
    private SpinButton[] timeButton = new SpinButton[6];

    /** Focus listener */
    private FocusListener focusListener;

    /** Key listener */
    private KeyListener keyListener;

    /** Mouse listener */
    private MouseListener mouseListener;

    /** Spin button listener */
    private ActionListener spinListener;

    /** Key press */
    private int keyCode;

    /**
   * Constructor
   * @param   parent   Frame instance, owner of dialog
   * @param   now   GregorianCalendar instance that will be the initial date for this dialog
   */
    public DateTimeDialog(JFrame parent, GregorianCalendar now) {
        super(parent, DateTime.getTimeZone().getID(), true);
        this.parent = parent;
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                cancel();
            }
        });
        this.focusListener = (new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                dateTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(((Button) e.getSource()).getLabel()));
                setTime();
            }
        });
        this.keyListener = (new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER && keyCode == KeyEvent.VK_ENTER) {
                    processAction(e);
                }
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    cancel();
                }
            }

            public void keyPressed(KeyEvent ke) {
                keyCode = ke.getKeyCode();
            }
        });
        this.spinListener = (new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().compareTo(SpinButton.COMMAND) == 0) {
                    processAction(e);
                }
            }
        });
        this.mouseListener = (new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                processAction(e);
            }
        });
        this.dateTimeStart = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
        this.dateTime = now;
        this.clock = new DateTime(now.getTime().getTime());
        this.addKeyListener(this.keyListener);
        setLayout(new BorderLayout(0, 2));
        GridPanel yearPane = new GridPanel();
        GridPanel monthPane = new GridPanel();
        GridBagConstraints yearPaneConstraints = monthPane.getConstraints();
        GridBagConstraints monthPaneConstraints = monthPane.getConstraints();
        this.setMonthNames();
        this.setDayNames();
        monthPaneConstraints.fill = GridBagConstraints.NONE;
        monthPaneConstraints.anchor = GridBagConstraints.CENTER;
        monthPaneConstraints.weightx = 1;
        monthPane.setConstraints(monthPaneConstraints);
        monthPane.addItem(dateButton[0] = new Button("<"), 0, 0);
        monthPane.addItem(this.monthLabel = new DateTimeLabel(DateTimeDialog.monthNames[this.dateTimeStart.get(GregorianCalendar.MONTH)], JLabel.CENTER, this.parent), 1, 0);
        monthPane.addItem(dateButton[1] = new Button(">"), 2, 0);
        yearPaneConstraints.fill = GridBagConstraints.NONE;
        yearPaneConstraints.anchor = GridBagConstraints.CENTER;
        yearPaneConstraints.weightx = 1;
        yearPane.setConstraints(yearPaneConstraints);
        yearPane.addItem(dateButton[2] = new Button("<"), 0, 0);
        yearPane.addItem(this.yearLabel = new DateTimeLabel(String.valueOf(dateTime.get(GregorianCalendar.YEAR)), JLabel.CENTER, this.parent), 1, 0);
        yearPane.addItem(dateButton[3] = new Button(">"), 2, 0);
        for (int i = 0; i < this.dateButton.length; i++) {
            this.dateButton[i].addKeyListener(keyListener);
            this.dateButton[i].addMouseListener(mouseListener);
        }
        GridPanel topPane = new GridPanel();
        GridBagConstraints topPaneConstraints = topPane.getConstraints();
        topPaneConstraints.fill = GridBagConstraints.NONE;
        topPaneConstraints.anchor = GridBagConstraints.CENTER;
        topPaneConstraints.weightx = 1;
        topPane.setConstraints(topPaneConstraints);
        topPane.addItem(monthPane, 0, 0);
        topPane.addItem(yearPane, 1, 0);
        GridPanel timePane = new GridPanel();
        GridBagConstraints timePaneConstraints = timePane.getConstraints();
        timePaneConstraints.fill = GridBagConstraints.HORIZONTAL;
        timePaneConstraints.anchor = GridBagConstraints.CENTER;
        timePaneConstraints.weightx = 1;
        timePaneConstraints.insets = new Insets(0, 0, 0, 0);
        timePane.setConstraints(timePaneConstraints);
        timePane.addItem(this.timeButton[0] = new SpinButton(SpinButton.DECREMENT, "<<<"), 0, 0);
        timePane.addItem(this.timeButton[1] = new SpinButton(SpinButton.DECREMENT, "<<"), 1, 0);
        timePane.addItem(this.timeButton[2] = new SpinButton(SpinButton.DECREMENT, "<"), 2, 0);
        this.clock.setTime(now.getTime().getTime());
        String dst = (DateTime.getTimeZone().inDaylightTime(this.clock) ? " " + Resources.getString("dst") : "");
        timePaneConstraints.insets = new Insets(0, 5, 0, 5);
        timePane.setConstraints(timePaneConstraints);
        timePane.addItem(this.timeLabel = new DateTimeLabel(DateTime.timeFormatMedium.format(this.dateTime.getTime()) + dst, JLabel.CENTER, this.parent), 3, 0);
        timePaneConstraints.insets = new Insets(0, 0, 0, 0);
        timePane.setConstraints(timePaneConstraints);
        timePane.addItem(this.timeButton[3] = new SpinButton(SpinButton.INCREMENT, ">"), 4, 0);
        timePane.addItem(this.timeButton[4] = new SpinButton(SpinButton.INCREMENT, ">>"), 5, 0);
        timePane.addItem(this.timeButton[5] = new SpinButton(SpinButton.INCREMENT, ">>>"), 6, 0);
        this.timeButton[0].setOtherButton(this.timeButton[5]);
        this.timeButton[5].setOtherButton(this.timeButton[0]);
        this.timeButton[1].setOtherButton(this.timeButton[4]);
        this.timeButton[4].setOtherButton(this.timeButton[1]);
        this.timeButton[2].setOtherButton(this.timeButton[3]);
        this.timeButton[3].setOtherButton(this.timeButton[2]);
        for (int i = 0; i < this.timeButton.length; i++) {
            this.timeButton[i].addActionListener(this.spinListener);
        }
        dayGrid = new JPanel(new GridLayout(7, 7));
        this.setDayGrid();
        this.add(topPane, BorderLayout.NORTH);
        this.add(dayGrid, BorderLayout.CENTER);
        this.add(timePane, BorderLayout.SOUTH);
        this.pack();
        GuiFrame.setCenter(this);
        this.setDayGrid();
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
   * Action handler for this dialog, which handles all the button presses
   * @param evt ActionEvent
   */
    public void actionPerformed(ActionEvent evt) {
        this.processAction(evt);
    }

    private void processAction(AWTEvent event) {
        Object source = event.getSource();
        if (source == this.dateButton[0]) {
            int m = this.getMonthIndex(monthLabel.getText());
            m = prevMonth(m);
            this.monthLabel.setText(DateTimeDialog.monthNames[m]);
            this.setDayGrid();
        } else if (source == this.dateButton[1]) {
            int m = this.getMonthIndex(this.monthLabel.getText());
            m = nextMonth(m);
            this.monthLabel.setText(DateTimeDialog.monthNames[m]);
            this.setDayGrid();
        } else if (source == this.dateButton[2]) {
            int y = Integer.parseInt(this.yearLabel.getText());
            this.yearLabel.setText(String.valueOf(--y));
            this.setDayGrid();
        } else if (source == this.dateButton[3]) {
            int y = Integer.parseInt(this.yearLabel.getText());
            yearLabel.setText(String.valueOf(++y));
            this.setDayGrid();
        } else if (source == this.timeButton[0]) {
            if (this.dateTime.get(Calendar.HOUR_OF_DAY) > 0) {
                this.dateTime.setTime(new Date(this.dateTime.getTime().getTime() - DateTime.ONE_HOUR));
                this.setTime();
            }
        } else if (source == this.timeButton[5]) {
            if (this.dateTime.get(Calendar.HOUR_OF_DAY) < DateTime.HOURS_IN_DAY - 1) {
                this.dateTime.setTime(new Date(this.dateTime.getTime().getTime() + DateTime.ONE_HOUR));
                this.setTime();
            }
        } else if (source == this.timeButton[1]) {
            if (this.dateTime.get(Calendar.MINUTE) > 0) {
                this.dateTime.setTime(new Date(this.dateTime.getTime().getTime() - DateTime.ONE_MINUTE));
                this.setTime();
            }
        } else if (source == this.timeButton[4]) {
            if (this.dateTime.get(Calendar.MINUTE) < DateTime.MINUTES_IN_HOUR - 1) {
                this.dateTime.setTime(new Date(this.dateTime.getTime().getTime() + DateTime.ONE_MINUTE));
                this.setTime();
            }
        } else if (source == this.timeButton[2]) {
            if (this.dateTime.get(Calendar.SECOND) > 0) {
                this.dateTime.setTime(new Date(this.dateTime.getTime().getTime() - DateTime.MILLISECONDS_IN_SECOND));
                this.setTime();
            }
        } else if (source == this.timeButton[3]) {
            if (this.dateTime.get(Calendar.SECOND) < DateTime.SECONDS_IN_MINUTE - 1) {
                this.dateTime.setTime(new Date(this.dateTime.getTime().getTime() + DateTime.MILLISECONDS_IN_SECOND));
                this.setTime();
            }
        } else {
            int m = this.getMonthIndex(this.monthLabel.getText());
            int d = Integer.parseInt(((Button) source).getLabel());
            int y = Integer.parseInt(this.yearLabel.getText());
            this.dateTime.set(y, m, d);
            this.setTime();
            if ((event.getID() == MouseEvent.MOUSE_CLICKED && ((MouseEvent) event).getClickCount() > 1) || (event.getID() == KeyEvent.KEY_TYPED && ((KeyEvent) event).getKeyChar() == KeyEvent.VK_ENTER && this.keyCode == KeyEvent.VK_ENTER)) {
                this.dispose();
            }
        }
    }

    /**
   * This method is used to calculate and display days of
   * the month in the correct format for the month currently displayed.
   * Days of the months are displayed as Buttons that the user can select.
   * DateTimeDialog's current day is higlighted.
   */
    private void setDayGrid() {
        this.dayGrid.removeAll();
        int m = getMonthIndex(this.monthLabel.getText());
        int y = Integer.parseInt(this.yearLabel.getText());
        this.dateTime.set(y, m, 1);
        this.dateTime.setLenient(false);
        int offset = this.dateTime.get(GregorianCalendar.DAY_OF_WEEK) - this.dateTime.getFirstDayOfWeek();
        if (offset < 0) {
            offset = DateTime.DAYS_IN_WEEK - this.dateTime.get(GregorianCalendar.DAY_OF_WEEK);
        }
        for (int i = 0; i < DateTime.DAYS_IN_WEEK; i++) {
            this.dayGrid.add(new Label(DateTimeDialog.dayNames[i], JLabel.CENTER));
        }
        for (int i = 1; i <= offset; i++) {
            this.dayGrid.add(new Label(""));
        }
        Button day;
        for (int i = 1; i <= this.getLastDay(); i++) {
            this.dayGrid.add(day = new Button(String.valueOf(i)));
            day.addMouseListener(this.mouseListener);
            day.addFocusListener(this.focusListener);
            day.addKeyListener(this.keyListener);
            if (i == this.dateTimeStart.get(Calendar.DATE) && m == this.dateTimeStart.get(Calendar.MONTH) && y == this.dateTimeStart.get(Calendar.YEAR)) {
                day.setForeground(Color.RED);
            }
        }
        for (int i = (offset + this.getLastDay() + 1); i <= 42; i++) {
            this.dayGrid.add(new Label(""));
        }
        this.dayGrid.validate();
        this.setTime();
    }

    /** Convert string month to int
   * @param month Month
   * @return Month as int
   * */
    private int getMonthIndex(String month) {
        for (int i = 0; i < DateTimeDialog.monthNames.length; i++) {
            if (DateTimeDialog.monthNames[i].compareTo(month) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
   * Return the month following the one passed in as an argument.
   * If the argument is the last month of the year, return the first month.
   * @param month Current month expressed as an integer (0 to 11).
   * @return Next month as int
   */
    private int nextMonth(int month) {
        if (month == 11) {
            return (0);
        }
        return (++month);
    }

    /**
   * Return the month preceding the one passed in as an argument.
   * If the argument is the first month of the year, return the last month.
   * @param month Current month expressed as an integer (0 to 11).
   * @return Previous month as int
   */
    private int prevMonth(int month) {
        if (month == 0) {
            return (11);
        }
        return (--month);
    }

    /**
   * Return the value of the last day in the currently selected month
   * @return Last day of month
   */
    private int getLastDay() {
        int m = this.getMonthIndex(this.monthLabel.getText());
        int y = Integer.parseInt(this.yearLabel.getText());
        return (new DateTime(y, m + 1, 1, 12, 0, 0)).getDaysInMonth();
    }

    /** Sets the time label (including DST indication)
   */
    private void setTime() {
        this.clock.setTime(this.dateTime.getTime().getTime());
        String dst = (DateTime.getTimeZone().inDaylightTime(clock) ? " " + Resources.getString("dst") : "");
        this.timeLabel.setText(DateTime.timeFormatMedium.format(clock) + dst);
        this.dayGrid.validate();
    }

    /** Initliase month strings (locale specific)
   */
    private void setMonthNames() {
        final SimpleDateFormat monthFormat = new SimpleDateFormat("MMMMMM");
        DateTime month;
        for (int i = 0; i < DateTimeDialog.monthNames.length; i++) {
            month = new DateTime(this.dateTime.get(Calendar.YEAR), i + 1, 1, 12, 0, 0);
            DateTimeDialog.monthNames[i] = monthFormat.format(month);
        }
    }

    /** Initliase day strings (locale specific)
   */
    private void setDayNames() {
        final SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        DateTime day;
        int dom = this.dateTime.get(Calendar.DAY_OF_MONTH);
        GregorianCalendar cal = new GregorianCalendar(this.dateTime.get(Calendar.YEAR), this.dateTime.get(Calendar.MONTH), dom);
        cal.setLenient(true);
        while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
            cal.setTime(new Date(cal.getTime().getTime() - DateTime.ONE_DAY));
        }
        dom = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        for (int i = 0; i < DateTimeDialog.dayNames.length; i++, dom++) {
            day = new DateTime(cal.get(Calendar.YEAR), month + 1, dom, 0, 0, 0);
            DateTimeDialog.dayNames[i] = dayFormat.format(day);
        }
    }

    /** Cancel the dialog */
    private void cancel() {
        this.dateTime.set(Calendar.YEAR, this.dateTimeStart.get(Calendar.YEAR));
        this.dateTime.set(Calendar.MONTH, this.dateTimeStart.get(Calendar.MONTH));
        this.dateTime.set(Calendar.DAY_OF_MONTH, this.dateTimeStart.get(Calendar.DAY_OF_MONTH));
        this.dateTime.set(Calendar.HOUR, this.dateTimeStart.get(Calendar.HOUR));
        this.dateTime.set(Calendar.MINUTE, this.dateTimeStart.get(Calendar.MINUTE));
        this.dateTime.set(Calendar.SECOND, this.dateTimeStart.get(Calendar.SECOND));
        this.dispose();
    }

    /** Correctly sized date time label */
    private class DateTimeLabel extends JLabel {

        private static final long serialVersionUID = 550191471791280400L;

        private Frame parent;

        public DateTimeLabel(String text, int alignment, Frame f) {
            super(text, alignment);
            parent = f;
        }

        public Dimension getPreferredSize() {
            int longestMonthString = 0;
            for (int i = 0; i < monthNames.length; i++) {
                if (monthNames[i].length() > longestMonthString) {
                    longestMonthString = i;
                }
            }
            String timeLabel = DateTime.timeFormatMedium.format(dateTime.getTime()) + " " + Resources.getString("dst");
            FontMetrics fontMetrics = parent.getFontMetrics(parent.getFont());
            int h = Math.max(fontMetrics.stringWidth(monthNames[longestMonthString]), fontMetrics.stringWidth(timeLabel));
            return new Dimension(h, fontMetrics.getHeight());
        }
    }
}
