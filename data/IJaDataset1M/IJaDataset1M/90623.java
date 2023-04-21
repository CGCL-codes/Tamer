package bizcal.swing;

import bizcal.common.DayViewConfig;
import bizcal.common.Event;
import bizcal.swing.util.FrameArea;
import bizcal.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import bizcal.common.DayViewConfig;
import bizcal.common.Event;
import bizcal.swing.util.FrameArea;
import bizcal.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DayViewProject extends CalendarView {

    public static int PIXELS_PER_HOUR = 80;

    private static final int CAPTION_ROW_HEIGHT0 = 20;

    public static final int PREFERRED_DAY_WIDTH = 10;

    public static final Integer GRID_LEVEL = new Integer(0);

    private List<List<FrameArea>> frameAreaCols = new ArrayList<List<FrameArea>>();

    private List<List<Event>> eventColList = new ArrayList<List<Event>>();

    private List<Date> _dateList = new ArrayList<Date>();

    private Map<Tuple, JLabel> timeLines = new HashMap<Tuple, JLabel>();

    private Map hourLabels = new HashMap();

    private Map minuteLabels = new HashMap();

    private List<JLabel> vLines = new ArrayList<JLabel>();

    private List<JPanel> calBackgrounds = new ArrayList<JPanel>();

    private ColumnHeaderPanel columnHeader;

    private int dayCount;

    private JScrollPane scrollPane;

    private JLayeredPane calPanel;

    private boolean firstRefresh = true;

    private DayViewConfig config;

    private List<JLabel> dateFooters = new ArrayList<JLabel>();

    /**
     * @param desc
     * @throws Exception
     */
    public DayViewProject(DayViewConfig desc) throws Exception {
        this(desc, null);
    }

    /**
     * @param desc
     * @param upperLeftCornerComponent component that is displayed in the upper left corner of the scrollpaine
     * @throws Exception
     */
    public DayViewProject(DayViewConfig desc, Component upperLeftCornerComponent) throws Exception {
        super(desc);
        this.config = desc;
        calPanel = new JLayeredPane();
        calPanel.setLayout(new Layout());
        ThisMouseListener mouseListener = new ThisMouseListener();
        ThisKeyListener keyListener = new ThisKeyListener();
        calPanel.addMouseListener(mouseListener);
        calPanel.addMouseMotionListener(mouseListener);
        calPanel.addKeyListener(keyListener);
        scrollPane = new JScrollPane(calPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setCursor(Cursor.getDefaultCursor());
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        if (upperLeftCornerComponent != null) {
            scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, upperLeftCornerComponent);
        }
        columnHeader = new ColumnHeaderPanel(desc);
        columnHeader.setShowExtraDateHeaders(desc.isShowExtraDateHeaders());
        scrollPane.setColumnHeaderView(columnHeader.getComponent());
    }

    public void refresh0() throws Exception {
        if (calPanel == null) return;
        dayCount = (int) (getModel().getInterval().getDuration() / (24 * 3600 * 1000));
        calPanel.removeAll();
        calPanel.setBackground(Color.GRAY);
        frameAreaCols.clear();
        eventColList.clear();
        timeLines.clear();
        hourLabels.clear();
        minuteLabels.clear();
        calBackgrounds.clear();
        vLines.clear();
        dateFooters.clear();
        addDraggingComponents(calPanel);
        Font hourFont = getDesc().getFont().deriveFont((float) 12);
        hourFont = hourFont.deriveFont(Font.BOLD);
        long pos = getFirstInterval().getStartDate().getTime();
        while (pos < getFirstInterval().getEndDate().getTime()) {
            Date date = new Date(pos);
            int timeSlots = 1;
            Color color = getDesc().getLineColor();
            Color hlineColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), getDesc().getGridAlpha());
            for (int i = 1; i <= timeSlots; i++) {
                JLabel line = new JLabel();
                line.setOpaque(true);
                line.setBackground(Color.red);
                calPanel.add(line, GRID_LEVEL);
                timeLines.put(new Tuple(date, "" + (60 / timeSlots) * i), line);
                addHorizontalLine(line);
            }
            pos += 3600 * 1000;
        }
        createColumns();
        Iterator i = getSelectedCalendars().iterator();
        while (i.hasNext()) {
            bizcal.common.BCalendar cal = (bizcal.common.BCalendar) i.next();
            JPanel calBackground = new JPanel();
            calBackground.setBackground(cal.getColor());
            calBackgrounds.add(calBackground);
            calPanel.add(calBackground);
        }
        columnHeader.setModel(getModel());
        columnHeader.setPopupMenuCallback(popupMenuCallback);
        columnHeader.refresh();
        if (firstRefresh) initScroll();
        firstRefresh = false;
        calPanel.validate();
        calPanel.repaint();
        for (JLabel l : timeLines.values()) {
            calPanel.setComponentZOrder(l, calPanel.getComponents().length - 1);
        }
        scrollPane.validate();
        scrollPane.repaint();
    }

    private int getColCount() throws Exception {
        return dayCount * getSelectedCalendars().size();
    }

    /**
     * Returns the first interval to show. Start day plus one.
     *
     * @return
     * @throws Exception
     */
    private DateInterval getFirstInterval() throws Exception {
        Date start = getInterval().getStartDate();
        return new DateInterval(DateUtil.round2Hour(start, this.config.getDayStartHour()), DateUtil.round2Hour(start, this.config.getDayEndHour()));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void createColumns() throws Exception {
        DateInterval interval = getFirstInterval();
        int cols = getColCount();
        frameAreaHash.clear();
        List events = null;
        DateInterval interval2 = null;
        for (int it = 0; it < cols; it++) {
            int iCal = it / dayCount;
            bizcal.common.BCalendar cal = (bizcal.common.BCalendar) getSelectedCalendars().get(iCal);
            Object calId = cal.getId();
            events = broker.getEvents(calId);
            Collections.sort(events);
            if (it % dayCount == 0) {
                interval2 = new DateInterval(interval);
            }
            _dateList.add(interval2.getStartDate());
            Calendar startdate = DateUtil.newCalendar();
            startdate.setTime(interval2.getStartDate());
            Color vlColor = getDesc().getLineColor();
            int vlAlpha = getDesc().getGridAlpha() + 50;
            if (vlAlpha > 255) {
                vlAlpha = 255;
            }
            Color vlAlphaColor = new Color(vlColor.getRed(), vlColor.getGreen(), vlColor.getBlue(), vlAlpha);
            if (it > 0) {
                JLabel verticalLine = new JLabel();
                verticalLine.setOpaque(true);
                verticalLine.setBackground(vlAlphaColor);
                if (startdate.get(Calendar.DAY_OF_WEEK) == startdate.getFirstDayOfWeek()) {
                    verticalLine.setBackground(getDescriptor().getLineColor2());
                }
                if (getSelectedCalendars().size() > 1 && it % dayCount == 0) {
                    verticalLine.setBackground(getDescriptor().getLineColor3());
                }
                calPanel.add(verticalLine, GRID_LEVEL);
                vLines.add(verticalLine);
            }
            List<FrameArea> frameAreas = new ArrayList<FrameArea>();
            frameAreaCols.add(frameAreas);
            if (calId == null) continue;
            Interval currDayInterval = getInterval(it % dayCount);
            List<Event> colEvents = new ArrayList<Event>();
            eventColList.add(colEvents);
            int iEvent = 0;
            if (events == null) events = new ArrayList();
            Iterator j = events.iterator();
            while (j.hasNext()) {
                Event event = (Event) j.next();
                DateInterval eventInterv = new DateInterval(event.getStart(), event.getEnd());
                if (!currDayInterval.overlap(eventInterv)) continue;
                FrameArea area = createFrameArea(calId, event);
                area.setBackground(config.getPrimaryColor());
                frameAreas.add(area);
                colEvents.add(event);
                calPanel.add(area, new Integer(event.getLevel()));
                iEvent++;
                if (!frameAreaHash.containsKey(event)) frameAreaHash.put(event, area); else {
                    frameAreaHash.get(event).addChild(area);
                }
            }
            if (dayCount > 1) interval2 = incDay(interval2);
        }
    }

    private int getYPos(Date aDate, int dayNo) throws Exception {
        long time = aDate.getTime();
        return getYPos(time, dayNo);
    }

    private int getYPos(long time, int dayNo) throws Exception {
        DateInterval interval = getInterval(dayNo);
        time -= interval.getStartDate().getTime();
        double viewPortHeight = getHeight() - getCaptionRowHeight() - getFooterHeight();
        double timeSpan = this.config.getHours() * 3600 * 1000;
        double dblTime = time;
        int ypos = (int) (dblTime / timeSpan * viewPortHeight);
        ypos += getCaptionRowHeight();
        return ypos;
    }

    protected Date getDate(int xPos, int yPos) throws Exception {
        int colNo = getColumn(xPos);
        int dayNo = 0;
        if (dayCount != 0) dayNo = colNo % dayCount;
        DateInterval interval = getInterval(dayNo);
        yPos -= getCaptionRowHeight();
        double ratio = ((double) yPos) / ((double) getTimeHeight());
        long time = (long) (interval.getDuration() * ratio);
        time += interval.getStartDate().getTime();
        return new Date(time);
    }

    private DateInterval getInterval(int dayNo) throws Exception {
        DateInterval interval = getFirstInterval();
        for (int i = 0; i < dayNo; i++) interval = incDay(interval);
        return interval;
    }

    private int getColumn(int xPos) throws Exception {
        xPos -= getXOffset();
        int width = getWidth() - getXOffset();
        double ratio = ((double) xPos) / ((double) width);
        return (int) (ratio * getColCount());
    }

    private Object getCalendarId(int colNo) throws Exception {
        int pos = 0;
        if (dayCount != 0) {
            pos = colNo / dayCount;
        }
        if (pos > getSelectedCalendars().size()) {
            pos = 1;
        }
        bizcal.common.BCalendar cal = (bizcal.common.BCalendar) getSelectedCalendars().get(pos);
        return cal.getId();
    }

    protected int getXOffset() {
        return 0;
    }

    private int getXPos(int colno) throws Exception {
        double x = getWidth();
        x = x - getXOffset();
        double ratio = ((double) colno) / ((double) getColCount());
        return ((int) (x * ratio)) + getXOffset();
    }

    private int getWidth() {
        return calPanel.getWidth();
    }

    private int getHeight() {
        return calPanel.getHeight();
    }

    private int getTimeHeight() throws Exception {
        return getHeight() - getCaptionRowHeight() - getFooterHeight();
    }

    private int getFooterHeight() {
        if (config.isShowDateFooter()) return PIXELS_PER_HOUR / 2;
        return 0;
    }

    private class Layout implements LayoutManager {

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            try {
                int width = dayCount * getModel().getSelectedCalendars().size() * PREFERRED_DAY_WIDTH;
                return new Dimension(width, getPreferredHeight());
            } catch (Exception e) {
                throw BizcalException.create(e);
            }
        }

        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(50, 100);
        }

        @SuppressWarnings("unchecked")
        public void layoutContainer(Container parent0) {
            try {
                DayViewProject.this.resetVerticalLines();
                int width = getWidth();
                int height = getHeight();
                DateInterval day = getFirstInterval();
                int numberOfCols = getColCount();
                if (numberOfCols == 0) numberOfCols = 1;
                for (int i = 0; i < eventColList.size(); i++) {
                    int dayNo = i % dayCount;
                    int xpos = getXPos(i);
                    int captionYOffset = getCaptionRowHeight() - CAPTION_ROW_HEIGHT0;
                    int colWidth = getXPos(i + 1) - getXPos(i);
                    int vLineTop = captionYOffset + CAPTION_ROW_HEIGHT0 + 2;
                    if (dayNo == 0 && (getSelectedCalendars().size() > 1)) {
                        vLineTop = 0;
                        day = getFirstInterval();
                    }
                    Calendar startinterv = Calendar.getInstance(Locale.getDefault());
                    startinterv.setTime(day.getStartDate());
                    if (i > 0) {
                        JLabel verticalLine = (JLabel) vLines.get(i - 1);
                        int vLineHeight = height - vLineTop;
                        verticalLine.setBounds(xpos, vLineTop, 1, vLineHeight);
                        addVerticalLine(verticalLine);
                    }
                    if (config.isShowDateFooter()) {
                        JLabel dayFooter = (JLabel) dateFooters.get(i);
                        dayFooter.setBounds(xpos, getTimeHeight(), colWidth, getFooterHeight());
                    }
                    DateInterval currIntervall = getInterval(dayNo);
                    FrameArea prevArea = null;
                    int overlapCol = 0;
                    int overlapColCount = 0;
                    List events = (List) eventColList.get(i);
                    List<FrameArea> areas = frameAreaCols.get(i);
                    for (int j = 0; j < events.size(); j++) {
                        FrameArea area = (FrameArea) areas.get(j);
                        Event event = (Event) events.get(j);
                        Date startTime = event.getStart();
                        if (startTime.before(currIntervall.getStartDate())) startTime = currIntervall.getStartDate();
                        Date endTime = event.getEnd();
                        if (endTime.after(currIntervall.getEndDate())) endTime = currIntervall.getEndDate();
                        int y1 = getYPos(startTime, dayNo);
                        if (y1 < getCaptionRowHeight()) y1 = getCaptionRowHeight();
                        int y2 = getYPos(endTime, dayNo);
                        int dy = y2 - y1;
                        int x1 = xpos;
                        area.setBounds(x1, y1, colWidth, dy);
                        if (!event.isBackground()) {
                            if (prevArea != null) {
                                Rectangle r = prevArea.getBounds();
                                int prevY2 = r.y + r.height;
                                if (prevY2 > y1) {
                                    overlapCol++;
                                    if (prevY2 < y2) {
                                        prevArea = area;
                                    }
                                } else {
                                    overlapCol = 0;
                                    prevArea = area;
                                }
                            } else prevArea = area;
                            if (overlapCol > overlapColCount) overlapColCount = overlapCol;
                        }
                    }
                }
                if (dayCount > 1) day = incDay(day);
                Iterator i = timeLines.keySet().iterator();
                while (i.hasNext()) {
                    Tuple key = (Tuple) i.next();
                    Date date = (Date) key.elementAt(0);
                    int minutes = Integer.parseInt((String) key.elementAt(1));
                    JLabel line = (JLabel) timeLines.get(key);
                    Date date1 = new Date(date.getTime() + minutes * 60 * 1000);
                    int y1 = getYPos(date1, 0);
                    int x1 = 0;
                    int lineheight = 1;
                    if (minutes > 0) {
                        lineheight = 1;
                    }
                    line.setBounds(x1, y1, width, lineheight);
                }
                for (int iCal = 0; iCal < calBackgrounds.size(); iCal++) {
                    int x1 = getXPos(iCal * dayCount);
                    int x2 = getXPos((iCal + 1) * dayCount);
                    JPanel calBackground = (JPanel) calBackgrounds.get(iCal);
                    calBackground.setBounds(x1, getCaptionRowHeight(), x2 - x1, getHeight());
                }
            } catch (Exception e) {
                throw BizcalException.create(e);
            }
        }
    }

    /**
     * Finds the smalles width of a framearea and its children
     *
     * @param fa
     * @return
     */
    private int findSmallestFrameArea(FrameArea fa) {
        if (fa.getChildren() == null || fa.getChildren().size() < 1) return fa.getBounds().width; else {
            int smallest = fa.getBounds().width;
            for (FrameArea child : fa.getChildren()) {
                if (child.getBounds().width < smallest) smallest = child.getBounds().width;
            }
            return smallest;
        }
    }

    protected Object getCalendarId(int x, int y) throws Exception {
        return getCalendarId(getColumn(x));
    }

    private DayViewConfig getDesc() throws Exception {
        DayViewConfig result = (DayViewConfig) getDescriptor();
        if (result == null) {
            result = new DayViewConfig();
            setDescriptor(result);
        }
        return result;
    }

    public DayViewConfig getDayViewConfig() throws Exception {
        return getDesc();
    }

    protected int getInitYPos() throws Exception {
        double viewStart = getModel().getViewStart().getValue();
        double ratio = viewStart / (24 * 3600 * 1000);
        return (int) (ratio * this.config.getHours() * PIXELS_PER_HOUR);
    }

    private int getPreferredHeight() {
        return this.config.getHours() * PIXELS_PER_HOUR + getFooterHeight();
    }

    public JComponent getComponent() {
        return scrollPane;
    }

    public void initScroll() throws Exception {
        scrollPane.getViewport().setViewPosition(new Point(0, getInitYPos()));
    }

    public void addListener(CalendarListener listener) {
        super.addListener(listener);
        columnHeader.addCalendarListener(listener);
    }
}
