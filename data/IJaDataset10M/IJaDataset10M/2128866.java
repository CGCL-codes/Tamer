package com.gface.date;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import com.gface.custom.GLabel;

public final class DatePicker extends Composite {

    private Button todayButton;

    private Calendar calendar = Calendar.getInstance();

    private DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();

    private Label monthLabel;

    private Composite headerSection;

    public final int NUM_OF_WEEKS = 6;

    private GLabel[] calendarLabels = new GLabel[NUM_OF_WEEKS * 7];

    private GLabel[] headerLabels = new GLabel[7];

    private Composite calendarSection;

    private final CurrentMonthMouseListener currentMonthMouseListner = new CurrentMonthMouseListener();

    private final NextMonthMouseListener nextMonthMouseListener = new NextMonthMouseListener();

    private final PreviousMonthMouseListener previousMonthMouseListener = new PreviousMonthMouseListener();

    /** background color */
    private Color backgroundColor;

    /** foreground color */
    private Color foregroundColor;

    /** Toaday label bg color */
    private Color todayLabelBackgroundColor;

    /** enabled date foreground color */
    private Color enabledForeground;

    /** disabled date foreground color */
    private Color disabledForeground;

    /** title background color */
    private Color titleBackground;

    /** title foreground color */
    private Color titleForeground;

    /** selected date background */
    private Color selectedDateBackground;

    /** selected date foreground */
    private Color selectedDateForeground;

    /** focused date background */
    private Color focusedDateBackground;

    /** focused date foreground */
    private Color focusedDateForeground;

    private GLabel todayLabel;

    private Composite bottomSection;

    private Label topSeparator;

    private Label buttomSeparator;

    private LinkedList dateSelectionListeners = new LinkedList();

    private Date selectedDate;

    private GLabel selectedLabel, focusedLabel;

    private Locale locale = Locale.getDefault();

    private boolean buttonsLocationInHeader = true;

    private boolean showPrevNextMonthButtons = true;

    private boolean showPrevNextYearButtons = false;

    private boolean show10YearButtons = false;

    private boolean showTodayButton = true;

    private boolean invisibleOnFocusOut = false;

    private boolean singleClickSelection = false;

    private boolean weekStartsOnSunday = true;

    public DatePicker(Composite parent, int swtStyle) {
        super(parent, checkStyle(swtStyle));
        initialize();
    }

    public DatePicker(Composite parent, int swtStyle, int dpStyle) {
        super(parent, checkStyle(swtStyle));
        if ((dpStyle & DatePickerStyle.BUTTONS_ON_BOTTOM) != 0) buttonsLocationInHeader = false;
        if ((dpStyle & DatePickerStyle.DISABLE_MONTH_BUTTONS) != 0) showPrevNextMonthButtons = false;
        if ((dpStyle & DatePickerStyle.YEAR_BUTTONS) != 0) showPrevNextYearButtons = true;
        if ((dpStyle & DatePickerStyle.TEN_YEARS_BUTTONS) != 0) show10YearButtons = true;
        if ((dpStyle & DatePickerStyle.NO_TODAY_BUTTON) != 0) showTodayButton = false;
        if ((dpStyle & DatePickerStyle.HIDE_WHEN_NOT_IN_FOCUS) != 0) invisibleOnFocusOut = true;
        if ((dpStyle & DatePickerStyle.SINGLE_CLICK_SELECTION) != 0) singleClickSelection = true;
        if ((dpStyle & DatePickerStyle.WEEKS_STARTS_ON_MONDAY) != 0) weekStartsOnSunday = false;
        initialize();
    }

    protected void widgetDisposed(DisposeEvent e) {
        disposeColors(e);
        headerSection.dispose();
        calendarSection.dispose();
        bottomSection.dispose();
        disposeComposite(calendarLabels);
        disposeComposite(headerLabels);
        dateSelectionListeners.clear();
    }

    private void disposeComposite(Composite[] composites) {
        for (int i = 0; i < composites.length; i++) {
            composites[i].dispose();
        }
    }

    private static int checkStyle(int style) {
        int mask = SWT.BORDER | SWT.READ_ONLY | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
        return style & mask;
    }

    private void initialize() {
        checkWidget();
        if (!weekStartsOnSunday) {
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
        } else {
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        }
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.horizontalSpacing = layout.verticalSpacing = 0;
        layout.verticalSpacing = layout.horizontalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        createColors(this);
        createHeaderSection(this);
        createCalendarSection(this);
        createBottomSection(this);
        setBackground(backgroundColor);
        initKeyboardNavigation();
        pack();
        setDate(new Date());
        updateAll();
        setFocus();
        addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                DatePicker.this.widgetDisposed(e);
            }
        });
        if (invisibleOnFocusOut) DatePicker.this.getShell().addShellListener(new ShellListener() {

            public void shellDeiconified(ShellEvent e) {
                ;
            }

            public void shellIconified(ShellEvent e) {
                ;
            }

            public void shellClosed(ShellEvent e) {
                ;
            }

            public void shellDeactivated(ShellEvent e) {
                DatePicker.this.getShell().setVisible(false);
            }

            public void shellActivated(ShellEvent e) {
                ;
            }
        });
    }

    private void createColors(Composite parent) {
        Display display = getDisplay();
        backgroundColor = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
        foregroundColor = display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
        todayLabelBackgroundColor = display.getSystemColor(SWT.COLOR_LIST_SELECTION);
        enabledForeground = display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
        disabledForeground = display.getSystemColor(SWT.COLOR_DARK_GRAY);
        titleBackground = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
        titleForeground = display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
        selectedDateBackground = display.getSystemColor(SWT.COLOR_DARK_YELLOW);
        selectedDateForeground = display.getSystemColor(SWT.COLOR_WHITE);
        focusedDateBackground = display.getSystemColor(SWT.COLOR_GRAY);
        focusedDateForeground = display.getSystemColor(SWT.COLOR_WHITE);
    }

    private void initKeyboardNavigation() {
        Listener keyNavigator = new Listener() {

            public void handleEvent(Event evt) {
                if (!(evt.widget instanceof Composite)) {
                    setFocus();
                }
                Date focusedDate = (Date) focusedLabel.getData();
                Calendar calculator = Calendar.getInstance();
                calculator.setTime(focusedDate);
                switch(evt.keyCode) {
                    case SWT.ARROW_UP:
                        calculator.add(Calendar.DAY_OF_MONTH, -7);
                        break;
                    case SWT.ARROW_DOWN:
                        calculator.add(Calendar.DAY_OF_MONTH, 7);
                        break;
                    case SWT.ARROW_LEFT:
                        calculator.add(Calendar.DAY_OF_MONTH, -1);
                        break;
                    case SWT.ARROW_RIGHT:
                        calculator.add(Calendar.DAY_OF_MONTH, 1);
                        break;
                    case SWT.PAGE_UP:
                        calculator.add(Calendar.MONTH, -1);
                        break;
                    case SWT.PAGE_DOWN:
                        calculator.add(Calendar.MONTH, 1);
                        break;
                    case SWT.CR:
                        dateSelected(focusedLabel);
                        break;
                    case SWT.ESC:
                        if (invisibleOnFocusOut) DatePicker.this.getShell().setVisible(false);
                }
                focusedDate = calculator.getTime();
                calendar = calculator;
                updateAll();
                dateFocused(getLabel(focusedDate));
            }
        };
        addKeyListenerToAll(this, keyNavigator);
    }

    private GLabel getLabel(Date date) {
        for (int i = 0; i < calendarLabels.length; ++i) {
            Date labelDate = (Date) calendarLabels[i].getData();
            if (labelDate != null && datesEqual(date, labelDate)) {
                return calendarLabels[i];
            }
        }
        return null;
    }

    private void addKeyListenerToAll(Control ctrl, Listener keyNavigator) {
        if (ctrl instanceof Composite) {
            Control[] children = ((Composite) ctrl).getChildren();
            for (int i = 0; i < children.length; ++i) {
                addKeyListenerToAll(children[i], keyNavigator);
            }
        }
        ctrl.addListener(SWT.KeyUp, keyNavigator);
    }

    private Label createSeperator(Composite parent, int cols) {
        GridData gd;
        Label sepearator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 7;
        sepearator.setLayoutData(gd);
        return sepearator;
    }

    private void updateDaysOfWeek() {
        String days[] = dateFormatSymbols.getShortWeekdays();
        for (int i = 1; i <= headerLabels.length; i++) {
            headerLabels[i - 1].setText(days[i].substring(0, 1));
        }
        if (!weekStartsOnSunday) {
            if (!days[Calendar.MONDAY].substring(0, 1).equals(headerLabels[0].getText().substring(0, 1))) {
                String keep = headerLabels[0].getText();
                for (int i = 0; i < headerLabels.length - 1; i++) {
                    headerLabels[i].setText(headerLabels[i + 1].getText());
                }
                headerLabels[headerLabels.length - 1].setText(keep);
            }
        }
    }

    private void createHeaderSection(Composite parent) {
        headerSection = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 7;
        layout.horizontalSpacing = layout.verticalSpacing = 0;
        headerSection.setLayout(layout);
        if (buttonsLocationInHeader) {
            if (showPrevNextMonthButtons) {
                Button prevMonthButton = new Button(headerSection, SWT.FLAT | SWT.ARROW | SWT.LEFT);
                prevMonthButton.setForeground(titleForeground);
                prevMonthButton.setBackground(titleBackground);
                prevMonthButton.setToolTipText("Previous Month");
                prevMonthButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
                prevMonthButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        previousMonth();
                    }
                });
            }
            if (showPrevNextYearButtons) {
                Button prevYearButton = new Button(headerSection, SWT.FLAT | SWT.ARROW | SWT.LEFT);
                prevYearButton.setForeground(titleForeground);
                prevYearButton.setToolTipText("Previous Year");
                prevYearButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
                prevYearButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        previousYear();
                    }
                });
            }
            if (show10YearButtons) {
                Button prev10YearButton = new Button(headerSection, SWT.FLAT | SWT.ARROW | SWT.LEFT);
                prev10YearButton.setForeground(titleForeground);
                prev10YearButton.setToolTipText("10 Years Back");
                prev10YearButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
                prev10YearButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        previous10Year();
                    }
                });
            }
        }
        monthLabel = new Label(headerSection, SWT.NONE);
        monthLabel.setText(getMonthText());
        monthLabel.setForeground(titleForeground);
        monthLabel.setBackground(titleBackground);
        monthLabel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
        monthLabel.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
            }

            public void mouseDown(MouseEvent e) {
                dropDown(true);
            }

            public void mouseUp(MouseEvent e) {
                dropDown(false);
            }
        });
        if (buttonsLocationInHeader) {
            if (show10YearButtons) {
                Button next10YearButton = new Button(headerSection, SWT.FLAT | SWT.ARROW | SWT.RIGHT);
                next10YearButton.setForeground(titleForeground);
                next10YearButton.setToolTipText("10 Years Forward");
                next10YearButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END));
                next10YearButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        next10Year();
                    }
                });
            }
            if (showPrevNextYearButtons) {
                Button nextYearButton = new Button(headerSection, SWT.FLAT | SWT.ARROW | SWT.RIGHT);
                nextYearButton.setForeground(titleForeground);
                nextYearButton.setToolTipText("Next Year");
                nextYearButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
                nextYearButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        nextYear();
                    }
                });
            }
            if (showPrevNextMonthButtons) {
                Button nextMonthButton = new Button(headerSection, SWT.FLAT | SWT.ARROW | SWT.RIGHT);
                nextMonthButton.setForeground(titleForeground);
                nextMonthButton.setBackground(titleBackground);
                nextMonthButton.setToolTipText("Next Month");
                nextMonthButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
                nextMonthButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        nextMonth();
                    }
                });
            }
        }
        headerSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        headerSection.setBackground(titleBackground);
        createPopup();
        headerSection.redraw();
    }

    /**
	 * Create the caledar section Initialize day labels and the layout
	 * 
	 * @param parent
	 */
    private void createCalendarSection(Composite parent) {
        calendarSection = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 7;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = layout.marginWidth = 0;
        layout.makeColumnsEqualWidth = true;
        calendarSection.setLayout(layout);
        calendarSection.setBackground(backgroundColor);
        GridData gd = new GridData(GridData.GRAB_VERTICAL | GridData.GRAB_HORIZONTAL | GridData.FILL_BOTH);
        for (int i = 0; i < headerLabels.length; i++) {
            headerLabels[i] = new GLabel(calendarSection, SWT.RIGHT);
            headerLabels[i].setBackground(backgroundColor);
            headerLabels[i].setForeground(foregroundColor);
            headerLabels[i].setLayoutData(gd);
        }
        updateDaysOfWeek();
        topSeparator = createSeperator(calendarSection, 7);
        for (int i = 0; i < calendarLabels.length; i++) {
            calendarLabels[i] = new GLabel(calendarSection, SWT.RIGHT);
            calendarLabels[i].setBackground(backgroundColor);
            calendarLabels[i].setForeground(foregroundColor);
            calendarLabels[i].setLayoutData(gd);
            calendarLabels[i].setText(String.valueOf(i));
        }
        buttomSeparator = createSeperator(calendarSection, 7);
        calendarSection.setLayoutData(gd);
    }

    private void createBottomSection(Composite parent) {
        bottomSection = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 7;
        layout.horizontalSpacing = layout.verticalSpacing = 0;
        bottomSection.setLayout(layout);
        if (!buttonsLocationInHeader) {
            if (showPrevNextMonthButtons) {
                Button prevMonthButton = new Button(bottomSection, SWT.FLAT | SWT.ARROW | SWT.LEFT);
                prevMonthButton.setForeground(titleForeground);
                prevMonthButton.setToolTipText("Previous Month");
                prevMonthButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
                prevMonthButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        previousMonth();
                    }
                });
            }
            if (showPrevNextYearButtons) {
                Button prevYearButton = new Button(bottomSection, SWT.FLAT | SWT.ARROW | SWT.LEFT);
                prevYearButton.setForeground(titleForeground);
                prevYearButton.setToolTipText("Previous Year");
                prevYearButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
                prevYearButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        previousYear();
                    }
                });
            }
            if (show10YearButtons) {
                Button prev10YearButton = new Button(bottomSection, SWT.FLAT | SWT.ARROW | SWT.LEFT);
                prev10YearButton.setForeground(titleForeground);
                prev10YearButton.setToolTipText("10 Years Back");
                prev10YearButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
                prev10YearButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        previous10Year();
                    }
                });
            }
        }
        if (showTodayButton) {
            todayButton = new Button(bottomSection, SWT.FLAT);
            todayButton.setText("Today");
            todayButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
            todayButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    Date today = new Date();
                    calendar.setTime(today);
                    updateAll();
                    dateFocused(getLabel(today));
                }
            });
        }
        if (!buttonsLocationInHeader) {
            if (show10YearButtons) {
                Button next10YearButton = new Button(bottomSection, SWT.FLAT | SWT.ARROW | SWT.RIGHT);
                next10YearButton.setForeground(titleForeground);
                next10YearButton.setToolTipText("10 Years Forward");
                next10YearButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END));
                next10YearButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        next10Year();
                    }
                });
            }
            if (showPrevNextYearButtons) {
                Button nextYearButton = new Button(bottomSection, SWT.FLAT | SWT.ARROW | SWT.RIGHT);
                nextYearButton.setForeground(titleForeground);
                nextYearButton.setToolTipText("Next Year");
                nextYearButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
                nextYearButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        nextYear();
                    }
                });
            }
            if (showPrevNextMonthButtons) {
                Button nextMonthButton = new Button(bottomSection, SWT.FLAT | SWT.ARROW | SWT.RIGHT);
                nextMonthButton.setForeground(titleForeground);
                nextMonthButton.setToolTipText("Next Month");
                nextMonthButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
                nextMonthButton.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        nextMonth();
                    }
                });
            }
        }
        bottomSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
	 * Very basic i18n support. Allow setting "today" text of button for other
	 * languages.
	 * 
	 * @param text
	 *            button text
	 */
    public void setTodayText(String text) {
        todayButton.setText(text);
    }

    private String getMonthText() {
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);
        String month = dateFormatSymbols.getMonths()[m];
        return month + " " + String.valueOf(y);
    }

    public void setDate(Date date) {
        if (date == null) date = new Date();
        calendar.setTime(date);
        selectedDate = date;
        updateAll();
        dateFocused(getLabel(date));
    }

    private boolean datesEqual(Date dt1, Date dt2) {
        return dt1.getTime() / 86400 == dt2.getTime() / 86400;
    }

    private void updateAll() {
        updateMonth();
        updateCalendar();
        setFocus();
    }

    /**
	 * Update the main area of the caledar control
	 */
    private void updateCalendar() {
        updateDaysOfWeek();
        Calendar calendarIterator = (Calendar) calendar.clone();
        calendarIterator.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = calendarIterator.get(Calendar.DAY_OF_WEEK);
        int compansation = 1;
        if (!weekStartsOnSunday) {
            compansation = 2;
        }
        if (firstDayOfMonth - compansation < 0) {
            firstDayOfMonth = 7 - (firstDayOfMonth - compansation);
        }
        calendarIterator.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth + compansation);
        int calendarLabelCounter = 0;
        for (int i = 1; i <= firstDayOfMonth - compansation; i++) {
            String value = String.valueOf(calendarIterator.get(Calendar.DAY_OF_MONTH));
            calendarIterator.add(Calendar.DAY_OF_MONTH, 1);
            GLabel l = calendarLabels[calendarLabelCounter];
            l.setText(value);
            l.setForeground(disabledForeground);
            removeMouseListeners(l);
            l.addMouseListener(previousMonthMouseListener);
            calendarLabelCounter++;
        }
        Calendar today = Calendar.getInstance();
        if (((today.get(Calendar.MONTH)) == (calendarIterator.get(Calendar.MONTH))) && (today.get(Calendar.YEAR)) == (calendarIterator.get(Calendar.YEAR))) {
            todayLabel = calendarLabels[calendarLabelCounter + today.get(Calendar.DAY_OF_MONTH) - 1];
        } else {
            todayLabel = null;
        }
        int thisMonth = calendar.get(Calendar.MONTH);
        do {
            int day = calendarIterator.get(Calendar.DAY_OF_MONTH);
            String value = String.valueOf(day);
            GLabel l = calendarLabels[calendarLabelCounter];
            l.setForeground(enabledForeground);
            l.setBackground(backgroundColor);
            l.setText(value);
            removeMouseListeners(l);
            l.addMouseListener(currentMonthMouseListner);
            l.setData(calendarIterator.getTime());
            calendarLabelCounter++;
            calendarIterator.add(Calendar.DAY_OF_MONTH, 1);
        } while (thisMonth == calendarIterator.get(Calendar.MONTH));
        if (todayLabel != null) todayLabel.setBackground(todayLabelBackgroundColor);
        setSelectedDate(getLabel(selectedDate));
        int i = 1;
        while (calendarLabelCounter < calendarLabels.length) {
            String value = String.valueOf(i);
            GLabel l = calendarLabels[calendarLabelCounter];
            l.setForeground(disabledForeground);
            l.setText(value);
            removeMouseListeners(l);
            l.addMouseListener(nextMonthMouseListener);
            calendarLabelCounter++;
            i++;
        }
    }

    private void removeMouseListeners(GLabel l) {
        l.removeMouseListener(previousMonthMouseListener);
        l.removeMouseListener(currentMonthMouseListner);
        l.removeMouseListener(nextMonthMouseListener);
    }

    private void updateMonth() {
        monthLabel.setText(getMonthText());
        headerSection.layout();
    }

    private void previousMonth() {
        calendar.add(Calendar.MONTH, -1);
        updateAll();
    }

    private void nextMonth() {
        calendar.add(Calendar.MONTH, 1);
        updateAll();
    }

    private void previousYear() {
        calendar.add(Calendar.YEAR, -1);
        updateAll();
    }

    private void nextYear() {
        calendar.add(Calendar.YEAR, 1);
        updateAll();
    }

    private void previous10Year() {
        calendar.add(Calendar.YEAR, -10);
        updateAll();
    }

    private void next10Year() {
        calendar.add(Calendar.YEAR, 10);
        updateAll();
    }

    public Point computeSize(int wHint, int hHint, boolean changed) {
        checkWidget();
        int width = 0, height = 0;
        int borderWidth = getBorderWidth();
        int computedHeight = headerSection.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + bottomSection.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 2 * topSeparator.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 6 * calendarLabels[0].computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 10 * 4;
        height = Math.max(hHint, computedHeight + 2 * borderWidth);
        width = Math.max(wHint, calendarLabels[0].computeSize(SWT.DEFAULT, SWT.DEFAULT).x * 7 + 2 * borderWidth);
        return new Point(width, height);
    }

    public void setBackground(Color color) {
        super.setBackground(color);
        calendarSection.setBackground(color);
        bottomSection.setBackground(color);
        setCalendarSectionBackground(color);
        topSeparator.setBackground(color);
        buttomSeparator.setBackground(color);
    }

    public void setForeground(Color color) {
        super.setForeground(color);
        enabledForeground = color;
        calendarSection.setForeground(color);
        bottomSection.setForeground(color);
        setCalednarSectionForeground(color);
    }

    private void setCalednarSectionForeground(Color color) {
        checkWidget();
        foregroundColor = color;
        for (int i = 0; i < calendarLabels.length; i++) {
            calendarLabels[i].setForeground(foregroundColor);
        }
        for (int i = 0; i < headerLabels.length; i++) {
            headerLabels[i].setForeground(foregroundColor);
        }
    }

    public void setHeaderBackground(Color color) {
        titleBackground = color;
        headerSection.setBackground(color);
        monthLabel.setBackground(color);
    }

    private void setCalendarSectionBackground(Color color) {
        checkWidget();
        backgroundColor = color;
        for (int i = 0; i < calendarLabels.length; i++) {
            calendarLabels[i].setBackground(backgroundColor);
        }
        for (int i = 0; i < headerLabels.length; i++) {
            headerLabels[i].setBackground(backgroundColor);
        }
    }

    private void disposeColors(DisposeEvent e) {
    }

    public void addDateSelectionListener(DateSelectionListener l) {
        dateSelectionListeners.add(l);
    }

    private void dateSelected(GLabel dayLabel) {
        setSelectedDate(dayLabel);
        DateSelectedEvent dse = new DateSelectedEvent(selectedDate);
        for (Iterator itr = dateSelectionListeners.iterator(); itr.hasNext(); ) {
            DateSelectionListener l = (DateSelectionListener) itr.next();
            l.dateSelected(dse);
        }
    }

    private void setSelectedDate(GLabel dayLabel) {
        if (selectedLabel != null) {
            selectedLabel.setBackground(backgroundColor);
            selectedLabel.setForeground(foregroundColor);
        }
        if (dayLabel == null) {
            return;
        }
        selectedLabel = dayLabel;
        selectedLabel.setBackground(selectedDateBackground);
        selectedLabel.setForeground(selectedDateForeground);
        selectedDate = (Date) selectedLabel.getData();
    }

    private void dateFocused(GLabel dayLabel) {
        if (dayLabel == null) {
            return;
        }
        if (focusedLabel != null && focusedLabel != todayLabel && !datesEqual((Date) focusedLabel.getData(), selectedDate)) {
            focusedLabel.setBackground(backgroundColor);
            focusedLabel.setForeground(foregroundColor);
        }
        focusedLabel = dayLabel;
        if (focusedLabel != todayLabel && !datesEqual((Date) focusedLabel.getData(), selectedDate)) {
            focusedLabel.setBackground(focusedDateBackground);
            focusedLabel.setForeground(focusedDateForeground);
        }
    }

    public void removeDateSelectionListener(DateSelectionListener l) {
        dateSelectionListeners.remove(l);
    }

    class PreviousMonthMouseListener implements MouseListener {

        /**
		 * Logger for this class
		 */
        public void mouseDoubleClick(MouseEvent e) {
        }

        public void mouseDown(MouseEvent e) {
            previousMonth();
        }

        public void mouseUp(MouseEvent e) {
        }
    }

    class NextMonthMouseListener implements MouseListener {

        /**
		 * Logger for this class
		 */
        public void mouseDoubleClick(MouseEvent e) {
            nextMonth();
        }

        public void mouseDown(MouseEvent e) {
            nextMonth();
        }

        public void mouseUp(MouseEvent e) {
        }
    }

    class CurrentMonthMouseListener implements MouseListener {

        public void mouseDoubleClick(MouseEvent e) {
            Object o = e.widget;
            if (o instanceof Label) {
                o = ((Label) o).getParent();
            }
            GLabel l = (GLabel) o;
            dateSelected(l);
        }

        public void mouseDown(MouseEvent e) {
            Object o = e.widget;
            if (o instanceof Label) {
                o = ((Label) o).getParent();
            }
            GLabel l = (GLabel) o;
            dateFocused(l);
        }

        public void mouseUp(MouseEvent e) {
            if (singleClickSelection) {
                Object o = e.widget;
                if (o instanceof Label) {
                    o = ((Label) o).getParent();
                }
                GLabel l = (GLabel) o;
                dateSelected(l);
            }
        }
    }

    public Date getDate() {
        return selectedDate;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        dateFormatSymbols = new DateFormatSymbols(locale);
        updateAll();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setSelectedDateBackgroud(Color backgroundColor) {
        selectedDateBackground = backgroundColor;
    }

    public void setSelectedDateForeground(Color foregroundColor) {
        selectedDateForeground = foregroundColor;
    }

    private void createPopup() {
    }

    private void dropDown(boolean drop) {
    }

    /**
	 * private void fillMonthList() { Calendar iterator = (Calendar)
	 * calendar.clone(); iterator.add(Calendar.MONTH, -5);
	 * monthList.removeAll(); for (int i = 0; i < 10; i++) {
	 * monthList.add(dateFormatSymbols.getMonths()[iterator
	 * .get(Calendar.MONTH)] + " " + iterator.get(Calendar.YEAR));
	 * iterator.add(Calendar.MONTH, 1); } } private boolean isDropped() { return
	 * popup.getVisible(); }
	 */
    public void setHeaderForeground(Color color) {
        titleForeground = color;
        monthLabel.setForeground(titleForeground);
    }
}
