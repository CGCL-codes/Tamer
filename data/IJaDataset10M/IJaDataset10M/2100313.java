package edu.cp.cal.gui.components;

import java.awt.Adjustable;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import edu.cp.cal.Cal;
import edu.cp.cal.gui.event.CalendarPanelSelectionListener;
import edu.cp.cal.gui.util.DisplayedDate;
import edu.cp.cal.gui.util.DisplayedEvent;
import edu.cp.cal.gui.util.SmoothLabel;
import edu.cp.cal.lgc.calendar.DateYMD;
import edu.cp.cal.lgc.util.font.Fonts;
import edu.cp.ical.Date;
import edu.cp.ical.Event;

/**
 * The CalendarPanel class is a Swing component for displaying a monthly
 * calendar with events. The calling application must implement the
 * CalendarDataRepository interface in order for this class to obtain events to
 * display. Note that this class does not cache any event information outside of
 * what is currently on the screen. So, the calling application should implement
 * an efficient methods for the CalendarDataRepository interface. (For example,
 * it would be a bad idea to query a database each time.) Note: this class is
 * coded using Java 1.2. So, don't add any Java 1.4/1.5/1.6 dependencies in here
 */
public class CalendarPanel extends JPanel implements MouseWheelListener {

    /** The font to use in the header */
    private static final Font headerFont = Fonts.FONT_BOLD_14;

    /** The font to use for the events */
    private static final Font eventFont = Fonts.FONT_NORMAL_10;

    /** The font to use for the month hint */
    private static final Font hintFont = Fonts.FONT_BOLD_36;

    /** The range of years to display initially */
    private static final int YEAR_RANGE = 5;

    /** Constant to locate this week in the panel */
    private static final int THIS_WEEK = 0;

    /** The number of weeks to display vertically */
    private static final int NUM_WEEKS_TO_DISPLAY = 5;

    /** Names of all the weekdays */
    private static final String[] weekdays = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

    /** Translation of names to calendar days */
    private static final int[] weekdayTranslation = { Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY };

    /** Long names of all the months */
    private static final String[] monthLongNames = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };

    /** Short names of all the months */
    private static final String[] monthNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    /** Label to display the title of panel */
    private SmoothLabel title;

    /** The panel in which the calendar is displayed */
    private JPanel drawArea;

    /** Scrollbar used to scroll the weeks in the calendar */
    private JScrollBar scrollBar;

    /** The first date that is displayed */
    private Calendar startDate;

    /** The day that weeks start on */
    private int firstDayOfWeek;

    /** Color to use as the background */
    private Color backgroundColor = Color.WHITE;

    /** Color to use as the background for today's cell */
    private Color todayBackgroundColor = new Color(240, 240, 240);

    /** Color to use for day before today */
    private Color previousDateBackgroundColor = new Color(230, 230, 230);

    /** The color to use when drawing the grid */
    private Color gridColor = Color.LIGHT_GRAY;

    /** The color to display the date number */
    private Color dateColor = Color.BLACK;

    /** The color to use for the selected date */
    private Color selectionColor = SystemColor.BLACK;

    /** The color to use for the foreground of the header */
    private Color headerForeground = SystemColor.textText;

    /** The color to use for the background of the window */
    private Color headerBackground = SystemColor.window;

    /** The color to use as the background for the month hint */
    private Color hintBackground = Color.DARK_GRAY;

    /** The color to use as the foreground for the month hint */
    private Color hintForeground = Color.WHITE;

    /** The last width */
    private int lastWidth = -1;

    /** The last height */
    private int lastHeight = -1;

    /** The width of a cell */
    private double cellWidth = 100;

    /** The height of a cell */
    private double cellHeight = 100;

    /** The height of the header */
    private int headerHeight = 25;

    /** Array of x positions of the columns */
    private int[] columnX;

    /** Array of y positions of the rows */
    private int[] rowY;

    /** Whether the scrollbar is currently changing */
    private boolean changingScrollbar = false;

    /** The margins for cells */
    private int CELL_MARGIN = 4;

    /** List of displayed events */
    private Vector<DisplayedEvent> displayedEvents = new Vector<DisplayedEvent>();

    /** List of displayed dates */
    private Vector<DisplayedDate> displayedDates = new Vector<DisplayedDate>();

    /** Timer for showing the month hint */
    private Timer timer = null;

    /** Whether to draw the month hint */
    private boolean drawDateHint = false;

    /** The current step of the month hint fading */
    private int fadeStep = 0;

    /** Whether to show the time of the events */
    private boolean showTime = true;

    /** Whether events can be selected */
    private boolean allowsEventSelection = true;

    /** The currently selected date */
    private DateYMD selectedDate = null;

    /** The index of the currently selected item */
    private int selectedItemInd = -1;

    /** List of calendar panel selection listeners */
    private Vector<CalendarPanelSelectionListener> selectionListeners = new Vector<CalendarPanelSelectionListener>();

    /**
    * Constructs a new calendar panel
    */
    public CalendarPanel() {
        super();
        firstDayOfWeek = getFirstDayOfWeek();
        initLayout();
        setWeekOffset(THIS_WEEK);
    }

    /**
    * Initializes the layout of the components in the panel
    */
    protected void initLayout() {
        setLayout(new BorderLayout());
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titlePanel.add(new JButton("Today") {

            {
                addActionListener(new ActionListener() {

                    /**
                            * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
                            */
                    public void actionPerformed(ActionEvent event) {
                        changingScrollbar = true;
                        scrollBar.setValue(0);
                        setWeekOffset(THIS_WEEK);
                        changingScrollbar = false;
                    }
                });
            }
        }, BorderLayout.EAST);
        title = new SmoothLabel("Calendar", SwingConstants.CENTER);
        title.setFont(new Font(title.getFont().getFamily(), Font.BOLD, 18));
        titlePanel.add(title, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);
        scrollBar = new JScrollBar(Adjustable.VERTICAL, 0, 5, -52 * YEAR_RANGE, 52 * YEAR_RANGE);
        scrollBar.addAdjustmentListener(new AdjustmentListener() {

            /**
          * @see java.awt.event.AdjustmentListener#adjustmentValueChanged(java.awt.event.AdjustmentEvent)
          */
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (changingScrollbar) {
                    return;
                }
                int val = e.getValue();
                changingScrollbar = true;
                if (val <= scrollBar.getMinimum()) {
                    scrollBar.setMinimum(scrollBar.getMinimum() - 1);
                    scrollBar.setMaximum(scrollBar.getMaximum() - 1);
                }
                if (val >= scrollBar.getMaximum() - 5) {
                    scrollBar.setMinimum(scrollBar.getMinimum() + 1);
                    scrollBar.setMaximum(scrollBar.getMaximum() + 1);
                }
                drawDateHint = true;
                fadeStep = 0;
                if (timer != null) {
                    timer.stop();
                    timer = null;
                }
                timer = new Timer(3000, new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        fadeStep++;
                        if (fadeStep > 9) {
                            drawDateHint = false;
                        } else {
                            drawDateHint = true;
                            timer.setInitialDelay(50);
                            timer.restart();
                        }
                        drawArea.repaint();
                    }
                });
                timer.start();
                changingScrollbar = false;
                setWeekOffset(val);
            }
        });
        add(this.scrollBar, BorderLayout.EAST);
        drawArea = new MonthPanel();
        add(drawArea, BorderLayout.CENTER);
        addMouseWheelListener(this);
    }

    /**
    * Sets the week that is currently viewed (zero being the initial week)
    * @param weekOffset the week offset to view
    */
    public void setWeekOffset(int weekOffset) {
        Calendar c = Calendar.getInstance();
        c.setLenient(true);
        firstDayOfWeek = getFirstDayOfWeek();
        int currentWeek = c.get(Calendar.WEEK_OF_YEAR);
        c.set(Calendar.DAY_OF_WEEK, weekdayTranslation[this.firstDayOfWeek]);
        c.set(Calendar.WEEK_OF_YEAR, currentWeek + weekOffset - 1);
        startDate = Calendar.getInstance();
        startDate.setTimeInMillis(c.getTimeInMillis());
        String label = monthNames[c.get(Calendar.MONTH)] + " " + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.YEAR) + " - ";
        c.add(Calendar.DAY_OF_YEAR, 34);
        label += monthNames[c.get(Calendar.MONTH)] + " " + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.YEAR);
        title.setText(label);
        repaint();
    }

    /**
    * Set the background color of the cell for the current date.
    * @param color The new background color
    */
    public void setTodayBackgroundColor(Color color) {
        todayBackgroundColor = color;
    }

    /**
    * The the text and background colors for the header where weekdays are
    * displayed.
    * @param foreground New color for header text
    * @param background New background color for header
    */
    public void setHeaderColors(Color foreground, Color background) {
        headerForeground = foreground;
        headerBackground = background;
    }

    /**
    * Handles a resize of the panel to adjust the cell sizes and repaint the panel
    * @param g the graphics context for the resizing event
    */
    private void handleResize(Graphics g) {
        lastWidth = drawArea.getWidth();
        lastHeight = drawArea.getHeight();
        headerHeight = g.getFontMetrics(headerFont).getHeight();
        cellWidth = (double) this.lastWidth / (double) 7;
        cellHeight = (double) (this.lastHeight - this.headerHeight) / (double) NUM_WEEKS_TO_DISPLAY;
        columnX = new int[7];
        rowY = new int[5];
        for (int col = 0; col < 7; col++) {
            double x = this.cellWidth * col;
            columnX[col] = (int) Math.floor(x);
        }
        for (int row = 0; row < 5; row++) {
            double y = this.cellHeight * row;
            rowY[row] = this.headerHeight + (int) Math.floor(y);
        }
    }

    /**
    * Draws the month and all of the days that fall in that month
    * @param g the graphics context to draw in
    */
    public void paintMonth(Graphics g) {
        Color defaultColor = g.getColor();
        displayedEvents.clear();
        displayedDates.clear();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(eventFont);
        if (lastWidth != drawArea.getWidth() || lastHeight != drawArea.getHeight()) {
            handleResize(g);
        }
        g.setFont(headerFont);
        for (int i = 0; i < 7; i++) {
            g.setColor(this.headerBackground);
            g.fillRect(columnX[i], 0, i < 6 ? columnX[i + 1] - columnX[i] : (int) cellWidth, headerHeight);
            String text = weekdays[(firstDayOfWeek + i) % 7];
            int xOffset = (int) Math.floor((this.cellWidth - g.getFontMetrics(headerFont).stringWidth(text)) / 2);
            g.setColor(this.headerForeground);
            g.drawString(text, columnX[i] + xOffset, g.getFontMetrics(headerFont).getAscent());
        }
        g.setColor(gridColor);
        int maxX = columnX[6] + (int) this.cellWidth;
        int maxY = rowY[4] + (int) this.cellHeight;
        g.drawRect(0, 0, maxX, maxY);
        for (int wday = 1; wday < 7; wday++) {
            g.drawLine(columnX[wday], 0, columnX[wday], maxY);
        }
        for (int row = 0; row < 5; row++) {
            g.drawLine(0, rowY[row], maxX, rowY[row]);
        }
        g.setColor(defaultColor);
        Calendar c = Calendar.getInstance();
        c.setLenient(true);
        c.setTimeInMillis(startDate.getTimeInMillis());
        g.setFont(eventFont);
        g.setColor(dateColor);
        for (int week = 0; week < 5; week++) {
            for (int col = 0; col < 7; col++) {
                g.setColor(dateColor);
                int w = col < 6 ? columnX[col + 1] - columnX[col] : (int) cellWidth;
                int h = week < 4 ? rowY[week + 1] - rowY[week] : (int) cellHeight;
                boolean includeMonthName = c.get(Calendar.DAY_OF_MONTH) == 1 || week == 0 && col == 0;
                DateYMD d = new DateYMD(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                displayedDates.addElement(new DisplayedDate(d, new Rectangle(columnX[col], rowY[week], w, h)));
                drawDayOfMonth(g, c, includeMonthName, columnX[col], rowY[week], w, h);
                c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
            }
        }
        if (drawDateHint) {
            StringBuffer hintBuf = new StringBuffer();
            if (this.startDate.get(Calendar.DAY_OF_MONTH) == 1) {
                hintBuf.append(monthLongNames[this.startDate.get(Calendar.MONTH)]);
                hintBuf.append(' ');
                hintBuf.append(this.startDate.get(Calendar.YEAR));
            } else {
                int mon = this.startDate.get(Calendar.MONTH) + 1;
                hintBuf.append(monthLongNames[mon % 12]);
                hintBuf.append(' ');
                if (mon == 12) {
                    hintBuf.append(this.startDate.get(Calendar.YEAR) + 1);
                } else {
                    hintBuf.append(this.startDate.get(Calendar.YEAR));
                }
            }
            String hint = hintBuf.toString();
            g.setFont(hintFont);
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(hint) + 10;
            int h = fm.getHeight() + 10;
            int x = (this.getWidth() - w) / 2;
            int y = (this.getHeight() - h) / 2;
            if (fadeStep < 10) {
                Graphics2D g2d = (Graphics2D) g;
                Composite oldComp = g2d.getComposite();
                float alpha = 0.5f - fadeStep * 0.05f;
                Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                g2d.setComposite(alphaComp);
                g.setColor(this.hintBackground);
                g.fillRoundRect(x, y, w, h, 10, 10);
                g.setColor(this.hintForeground);
                g.drawString(hint, x + 5, y + 5 + fm.getAscent());
                g2d.setComposite(oldComp);
            }
        }
    }

    /**
    * Draws a single day in a month (i.e. a day cell)
    * @param g the graphics context to draw in
    * @param day the day being drawn
    * @param showMonthName whether the name of the month should appear before the numeric date
    * @param x the x location in which to draw the day
    * @param y the y location in which to draw the day
    * @param w the width of the cell for the day
    * @param h the height of the cell for the day
    */
    protected void drawDayOfMonth(Graphics g, Calendar day, boolean showMonthName, int x, int y, int w, int h) {
        Font savedFont = g.getFont();
        FontMetrics fm = g.getFontMetrics();
        String label;
        Color fg = g.getColor();
        Calendar today = Calendar.getInstance();
        Color bgColor;
        if (today.get(Calendar.YEAR) == day.get(Calendar.YEAR) && today.get(Calendar.MONTH) == day.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH)) {
            bgColor = todayBackgroundColor;
            g.setFont(g.getFont().deriveFont(Font.BOLD));
            fm = g.getFontMetrics();
        } else if (day.compareTo(today) < 0) {
            bgColor = previousDateBackgroundColor;
            fm = g.getFontMetrics();
        } else {
            bgColor = backgroundColor;
        }
        g.setColor(bgColor);
        g.fillRect(x + 1, y + 1, w - 1, h - 1);
        g.setColor(fg);
        if (showMonthName) {
            label = monthNames[day.get(Calendar.MONTH)] + " " + day.get(Calendar.DAY_OF_MONTH);
        } else {
            label = "" + day.get(Calendar.DAY_OF_MONTH);
        }
        int labelW = fm.stringWidth(label);
        g.drawString(label, x + w - labelW - 1, y + fm.getAscent());
        List<Event> events = new ArrayList<Event>(Cal.getLogic().getCalendarManager().getEventsForDate(day.get(Calendar.YEAR), day.get(Calendar.MONTH) + 1, day.get(Calendar.DAY_OF_MONTH)));
        Collections.sort(events);
        boolean dateIsSelected = this.selectedDate != null && this.selectedDate.year == day.get(Calendar.YEAR) && this.selectedDate.month == day.get(Calendar.MONTH) + 1 && this.selectedDate.day == day.get(Calendar.DAY_OF_MONTH);
        int startY = y + fm.getHeight();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            edu.cp.cal.lgc.calendar.Calendar cal = Cal.getLogic().getCalendarManager().getCalendar(event);
            if (Cal.getView().isActive(cal)) {
                Rectangle rect = new Rectangle(x + CELL_MARGIN, startY, w - 2 * CELL_MARGIN, fm.getHeight());
                startY += fm.getHeight() + 1 * CELL_MARGIN;
                drawMonthViewEvent(g, rect, event, dateIsSelected && i == this.selectedItemInd);
                DisplayedEvent de = new DisplayedEvent(event, rect, i);
                displayedEvents.addElement(de);
            }
        }
        g.setFont(savedFont);
        g.setColor(fg);
    }

    /**
    * Formats the given time and returns a prettier string representation
    * @param hour the hour value (0 - 24)
    * @param minute the minute value (0 - 60)
    * @param second the second value (0 - 60)
    * @return String representation of the given time
    */
    protected String formatTime(int hour, int minute, int second) {
        StringBuffer sb = new StringBuffer();
        if (hour == 0 || hour == 12) {
            sb.append("12");
        } else if (hour > 12) {
            sb.append(hour % 12);
        } else {
            sb.append(hour);
        }
        sb.append(':');
        if (minute < 10) {
            sb.append('0');
        }
        sb.append(minute);
        if (hour < 12) {
            sb.append("am");
        } else {
            sb.append("pm");
        }
        return sb.toString();
    }

    /**
    * Draws an event in the month view for a cell
    * @param g the graphics context in which to draw
    * @param r bounding box for the area to draw in
    * @param event the event to draw the details for
    * @param isSelected whether the given event is selected
    */
    protected void drawMonthViewEvent(Graphics g, Rectangle r, Event event, boolean isSelected) {
        Graphics2D g2 = (Graphics2D) g;
        Color c = g.getColor();
        edu.cp.cal.lgc.calendar.Calendar cal = Cal.getLogic().getCalendarManager().getCalendar(event);
        g.setColor(cal.getBackground());
        int arclen = r.height;
        if (isSelected) {
            BasicStroke dashed = new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[] { 6.0f }, 0.0f);
            g2.setStroke(dashed);
            g.setColor(selectionColor);
            g.drawRoundRect(r.x, r.y, r.width, r.height, arclen, arclen);
        }
        g.setColor(cal.getBackground());
        g.fillRoundRect(r.x, r.y, r.width, r.height, arclen, arclen);
        g.setClip(r.x + 1, r.y + 1, r.width - 2, r.height - 3);
        g.setColor(cal.getForeground());
        String text;
        if (event.hasTime() && showTime) {
            text = formatTime(event.getStartDate().getHour(), event.getStartDate().getMinute(), event.getStartDate().getSecond()) + " " + event.getSummary().getValue();
        } else {
            text = event.getSummary().getValue();
        }
        StringBuilder overflowText = new StringBuilder();
        FontMetrics metrics = g.getFontMetrics();
        int maxWidth = new Double(r.getWidth()).intValue();
        int currentWidth = 0;
        for (char character : text.toCharArray()) {
            currentWidth += metrics.charWidth(character);
            if (currentWidth < maxWidth) {
                overflowText.append(character);
            } else {
                overflowText.delete(overflowText.length() - 4, overflowText.length());
                overflowText.append("...");
                break;
            }
        }
        g.drawString(overflowText.toString(), r.x + 7, r.y + metrics.getAscent());
        g.setColor(c);
        g.setClip(null);
    }

    /**
    * Adds the given listener to the list of selection listeners
    * @param listener the listener to add
    */
    public void addSelectionListener(CalendarPanelSelectionListener listener) {
        selectionListeners.add(listener);
    }

    /**
    * Removes the given listener from the list of selection listeners
    * @param listener the listener to remove
    */
    public void removeSelectionListener(CalendarPanelSelectionListener listener) {
        selectionListeners.remove(listener);
    }

    /**
    * Returns the selected event, if one exists, or null
    * @return the selected event, or null
    */
    public Event getSelectedEvent() {
        if (selectedDate == null) {
            return null;
        }
        List<Event> eventsForDate = new ArrayList<Event>(Cal.getLogic().getCalendarManager().getEventsForDate(selectedDate.year, selectedDate.month, selectedDate.day));
        if (eventsForDate != null && selectedItemInd >= 0 && selectedItemInd < eventsForDate.size()) {
            Collections.sort(eventsForDate);
            return eventsForDate.get(selectedItemInd);
        }
        return null;
    }

    /**
    * Clear any user selection. This should be done anytime the contents of what
    * is being displayed is modified. For example, if a calendar is added to the
    * display or removed, this should be called. The event selected by the user
    * is internally stored by date and index number for that date, so anything
    * that may change the number of events displayed on a particular date could
    * cause the selection to "move", so the app should call this method to clear
    * the selection.
    */
    public void clearSelection() {
        boolean doRepaint = selectedDate != null && selectedItemInd >= 0;
        selectedDate = null;
        selectedItemInd = -1;
        if (doRepaint) {
            repaint();
        }
    }

    /**
    * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
    */
    public void mouseWheelMoved(MouseWheelEvent e1) {
        int notches = e1.getWheelRotation();
        scrollBar.setValue(scrollBar.getValue() + notches);
    }

    /**
    * Component to display a month in a panel
    */
    private class MonthPanel extends JPanel implements MouseListener {

        /** TODO */
        private static final long serialVersionUID = 1000L;

        /**
       * TODO
       */
        public MonthPanel() {
            super();
            setToolTipText("Month View");
            MouseListener[] listeners = this.getMouseListeners();
            for (int i = 0; listeners != null && i < listeners.length; i++) {
                if (listeners[i] instanceof ToolTipManager) {
                    ToolTipManager ttm = (ToolTipManager) listeners[i];
                    ttm.setInitialDelay(1);
                }
            }
            addMouseListener(this);
        }

        /**
       * Formats the given time
       * @param hour the hour value (0 - 24)
       * @param minute the minute value (0 - 60)
       * @return String representation of the given time
       */
        public String formattedTime(int hour, int minute) {
            StringBuffer ret = new StringBuffer();
            String ampm = null;
            if (hour < 12) {
                ampm = "am";
            } else {
                hour %= 12;
                ampm = "pm";
            }
            if (hour == 0) {
                hour = 12;
            }
            ret.append(hour);
            ret.append(':');
            if (minute < 10) {
                ret.append('0');
            }
            ret.append(minute);
            ret.append(ampm);
            return ret.toString();
        }

        /**
       * Gets the event at the specified position in the calendar
       * @param x the x location of the position
       * @param y the y location of the position
       * @return the event located at the given position, or null if there is none
       */
        protected DisplayedEvent getEventForPosition(int x, int y) {
            for (int i = 0; displayedEvents != null && i < displayedEvents.size(); i++) {
                DisplayedEvent de = (DisplayedEvent) displayedEvents.elementAt(i);
                Rectangle rect = de.getRect();
                if (x >= rect.x && x <= rect.x + rect.width && y >= rect.y && y <= rect.y + rect.height) {
                    return de;
                }
            }
            return null;
        }

        /**
       * @see javax.swing.JComponent#paint(java.awt.Graphics)
       */
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            paintMonth(g);
        }

        /**
       * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
       */
        public void mouseClicked(MouseEvent e1) {
            boolean wasSelected = selectedDate != null;
            boolean doRepaint = false;
            selectedDate = null;
            selectedItemInd = -1;
            DisplayedEvent selectedEvent = null;
            for (int i = 0; displayedEvents != null && i < displayedEvents.size(); i++) {
                DisplayedEvent de = (DisplayedEvent) displayedEvents.elementAt(i);
                Date startDate = de.getEvent().getStartDate();
                Rectangle rect = de.getRect();
                if (e1.getX() >= rect.x && e1.getX() <= rect.x + rect.width && e1.getY() >= rect.y && e1.getY() <= rect.y + rect.height) {
                    selectedDate = new DateYMD(startDate.getYear(), startDate.getMonth(), startDate.getDay());
                    if (allowsEventSelection) {
                        selectedItemInd = de.getEventNoForDay();
                        selectedEvent = de;
                    }
                    break;
                }
            }
            if (selectedEvent == null) {
                for (int i = 0; displayedDates != null && i < displayedDates.size(); i++) {
                    DisplayedDate dd = (DisplayedDate) displayedDates.elementAt(i);
                    Rectangle rect = dd.getRect();
                    if (e1.getX() >= rect.x && e1.getX() <= rect.x + rect.width && e1.getY() >= rect.y && e1.getY() <= rect.y + rect.height) {
                        selectedDate = dd.getDate();
                    }
                }
            }
            if (wasSelected) {
                for (int i = 0; i < selectionListeners.size(); i++) {
                    CalendarPanelSelectionListener l = (CalendarPanelSelectionListener) selectionListeners.elementAt(i);
                    l.eventUnselected();
                }
                doRepaint = true;
            }
            if (SwingUtilities.isRightMouseButton(e1) || (e1.isControlDown() && SwingUtilities.isLeftMouseButton(e1))) {
                if (selectedDate != null && selectedEvent == null) {
                    for (int i = 0; i < selectionListeners.size(); i++) {
                        CalendarPanelSelectionListener l = (CalendarPanelSelectionListener) selectionListeners.elementAt(i);
                        l.dateRightClicked(e1, selectedDate.year, selectedDate.month, selectedDate.day);
                    }
                } else if (selectedEvent != null) {
                    for (int i = 0; i < selectionListeners.size(); i++) {
                        CalendarPanelSelectionListener l = (CalendarPanelSelectionListener) selectionListeners.elementAt(i);
                        l.eventRightClicked(e1, selectedEvent.getEvent(), selectedDate.year, selectedDate.month, selectedDate.day);
                    }
                }
            }
            if (selectedDate != null && selectedEvent != null) {
                for (int i = 0; i < selectionListeners.size(); i++) {
                    CalendarPanelSelectionListener l = (CalendarPanelSelectionListener) selectionListeners.elementAt(i);
                    l.eventSelected(selectedEvent.getEvent());
                }
                doRepaint = true;
            }
            if (e1.getClickCount() == 2 && selectedDate != null && selectedEvent != null) {
                for (int i = 0; i < selectionListeners.size(); i++) {
                    CalendarPanelSelectionListener l = (CalendarPanelSelectionListener) selectionListeners.elementAt(i);
                    l.eventDoubleClicked(selectedEvent.getEvent());
                }
            } else if (e1.getClickCount() == 2 && selectedDate != null && selectedEvent == null) {
                for (int i = 0; i < selectionListeners.size(); i++) {
                    CalendarPanelSelectionListener l = (CalendarPanelSelectionListener) selectionListeners.elementAt(i);
                    l.dateDoubleClicked(selectedDate.year, selectedDate.month, selectedDate.day);
                }
            } else {
                for (int i = 0; i < selectionListeners.size(); i++) {
                    CalendarPanelSelectionListener l = (CalendarPanelSelectionListener) selectionListeners.elementAt(i);
                    l.dateClicked(selectedDate.year, selectedDate.month, selectedDate.day);
                }
            }
            if (doRepaint) {
                repaint();
            }
        }

        /**
       * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
       */
        public void mouseEntered(MouseEvent e1) {
        }

        /**
       * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
       */
        public void mouseExited(MouseEvent e1) {
        }

        /**
       * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
       */
        public void mousePressed(MouseEvent e1) {
        }

        /**
       * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
       */
        public void mouseReleased(MouseEvent e1) {
        }
    }

    /**
    * Gets the first day of the current week
    * @return the integer representation of the first day of this week
    */
    private int getFirstDayOfWeek() {
        switch(Calendar.getInstance().getFirstDayOfWeek()) {
            case Calendar.SUNDAY:
                return 0;
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
        }
        return -1;
    }
}
