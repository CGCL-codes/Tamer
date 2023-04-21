package org.eclipse.nebula.timetext;

import java.text.MessageFormat;
import org.apache.commons.lang.StringUtils;
import org.apache.regexp.RE;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.nebula.CommonColors;
import org.eclipse.nebula.NebulaImages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * 
 * @since 1.0.0
 */
public class TimeText extends Composite {

    /** ... TODO Javadoc ... */
    private static final String PATTERN_FIVE = "{0}h {1}m";

    /** ... TODO Javadoc ... */
    private static final String PATTERN_FOUR = "{0}h";

    /** ... TODO Javadoc ... */
    private static final String PATTERN_ONE = "{0}d";

    /** ... TODO Javadoc ... */
    private static final String PATTERN_SEVEN = "{0}d {1}m";

    /** ... TODO Javadoc ... */
    private static final String PATTERN_SIX = "{0}m";

    /** ... TODO Javadoc ... */
    private static final String PATTERN_THREE = "{0}d {1}h {2}m";

    /** ... TODO Javadoc ... */
    private static final String PATTERN_TWO = "{0}d {1}h";

    /** ... TODO Javadoc ... */
    private Button btnMinusDays;

    /** ... TODO Javadoc ... */
    private Button btnMinusHours;

    /** ... TODO Javadoc ... */
    private Button btnMinusMinutes;

    /** ... TODO Javadoc ... */
    private Button btnPlusDays;

    /** ... TODO Javadoc ... */
    private Button btnPlusHours;

    /** ... TODO Javadoc ... */
    private Button btnPlusMinutes;

    /** ... TODO Javadoc ... */
    private String days = StringUtils.EMPTY;

    /** ... TODO Javadoc ... */
    private String hours = StringUtils.EMPTY;

    /** ... TODO Javadoc ... */
    private String minutes = StringUtils.EMPTY;

    /** ... TODO Javadoc ... */
    private Listener mOobClickListener;

    /** ... TODO Javadoc ... */
    private Composite mTimeEntryComposite;

    /** ... TODO Javadoc ... */
    private Shell mTimeEntryShell;

    /** ... TODO Javadoc ... */
    private Text mTimeText;

    /** ... TODO Javadoc ... */
    private Text txtDays;

    /** ... TODO Javadoc ... */
    private Text txtHours;

    /** ... TODO Javadoc ... */
    private Text txtMinutes;

    /** ... TODO Javadoc ... */
    private Composite mParentComposite;

    /**
	 * 
	 * @param parent
	 * @param style
	 */
    public TimeText(Composite parent, int style) {
        super(parent, checkStyle(style));
        this.mParentComposite = parent;
        init();
    }

    /**
	 * 
	 * @param style
	 * @return
	 */
    private static int checkStyle(int style) {
        return style;
    }

    /**
	 * 
	 */
    public void addListener(int eventType, Listener listener) {
        checkWidget();
        mTimeText.addListener(eventType, listener);
    }

    /**
	 * Adds a modification listener to the combo box.
	 * 
	 * @param ml
	 *            ModifyListener
	 */
    public void addModifyListener(ModifyListener ml) {
        checkWidget();
        mTimeText.addModifyListener(ml);
    }

    /**
	 * 
	 * @return
	 */
    public String getText() {
        return mTimeText.getText();
    }

    /**
	 * 
	 */
    private void init() {
        GridLayout gl = new GridLayout(2, false);
        gl.horizontalSpacing = 0;
        gl.marginBottom = 1;
        gl.marginHeight = 0;
        gl.marginLeft = 0;
        gl.marginRight = 0;
        gl.marginTop = 1;
        gl.marginWidth = 0;
        setLayout(gl);
        this.setBackground(CommonColors.WHITE);
        this.setBackgroundMode(SWT.INHERIT_FORCE);
        mOobClickListener = new Listener() {

            public void handleEvent(Event event) {
                if (!isTimeTextVisible()) {
                    return;
                }
                Control cc = Display.getDefault().getCursorControl();
                if (cc != mTimeEntryComposite) {
                    if (cc != mTimeEntryShell) {
                        if (cc != txtDays && cc != btnPlusDays && cc != btnMinusDays && cc != txtHours && cc != btnPlusHours && cc != btnMinusHours && cc != txtMinutes && cc != btnPlusMinutes && cc != btnMinusMinutes) {
                            kill();
                        }
                    }
                }
            }
        };
        mTimeText = new Text(this, SWT.READ_ONLY);
        mTimeText.setBackground(new Color(Display.getDefault(), 255, 255, 255));
        mTimeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        Label timeEntryButton = new Label(this, SWT.NONE);
        timeEntryButton.setImage(NebulaImages.getImage(NebulaImages.IMG_TIME_ENTRY));
        timeEntryButton.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
            }

            public void mouseDown(MouseEvent e) {
                if (isTimeTextVisible()) {
                    kill();
                } else {
                    showTimeEntryFields();
                }
            }

            public void mouseUp(MouseEvent e) {
            }
        });
        Shell parentShell = mParentComposite.getShell();
        if (parentShell != null) {
            parentShell.addControlListener(new ControlListener() {

                public void controlMoved(ControlEvent e) {
                    kill();
                }

                public void controlResized(ControlEvent e) {
                    kill();
                }
            });
        }
        updateFields();
    }

    /**
	 * 
	 * @return
	 */
    protected boolean isTimeTextVisible() {
        return (mTimeEntryShell != null && !mTimeEntryShell.isDisposed());
    }

    /**
	 * 
	 */
    protected void kill() {
        if (mTimeEntryComposite == null) return;
        if (mTimeEntryComposite.isDisposed()) return;
        Display.getCurrent().removeFilter(SWT.FocusIn, mOobClickListener);
        if (mTimeEntryShell != null && !mTimeEntryShell.isDisposed()) {
            mTimeEntryShell.setVisible(false);
            mTimeEntryShell.dispose();
        }
    }

    /**
	 * 
	 */
    protected boolean minusDay() {
        boolean rValue = false;
        int currentDays = 0;
        if (StringUtils.isNotBlank(days)) {
            currentDays = Integer.parseInt(days);
        }
        if (currentDays == 1) {
            days = StringUtils.EMPTY;
            rValue = true;
        } else if (currentDays > 1) {
            days = String.valueOf(currentDays - 1);
            rValue = true;
        }
        updateFields();
        return rValue;
    }

    /**
	 * 
	 */
    protected boolean minusHour() {
        boolean rValue = false;
        int currentHours = 0;
        if (StringUtils.isNotBlank(hours)) {
            currentHours = Integer.parseInt(hours);
        }
        if (currentHours == 0) {
            if (minusDay()) {
                hours = "23";
                rValue = true;
            }
        } else if (currentHours == 1) {
            hours = StringUtils.EMPTY;
            rValue = true;
        } else if (currentHours > 1) {
            hours = String.valueOf(currentHours - 1);
            rValue = true;
        }
        updateFields();
        return rValue;
    }

    /**
	 * 
	 */
    protected void minusMinutes() {
        int currentMinutes = 0;
        if (StringUtils.isNotBlank(minutes)) {
            currentMinutes = Integer.parseInt(minutes);
        }
        if (currentMinutes == 5) {
            minutes = StringUtils.EMPTY;
        } else if (currentMinutes == 0) {
            if (minusHour()) {
                minutes = "55";
            }
        } else {
            minutes = String.valueOf(currentMinutes - 5);
        }
        updateFields();
    }

    /**
	 * 
	 */
    protected void plusDay() {
        int currentDays = 0;
        if (StringUtils.isNotBlank(days)) {
            currentDays = Integer.parseInt(days);
        }
        days = String.valueOf(currentDays + 1);
        updateFields();
    }

    /**
	 * 
	 */
    protected void plusHour() {
        int currentHours = 0;
        if (StringUtils.isNotBlank(hours)) {
            currentHours = Integer.parseInt(hours);
        }
        if (currentHours == 23) {
            plusDay();
            hours = StringUtils.EMPTY;
        } else {
            hours = String.valueOf(currentHours + 1);
        }
        updateFields();
    }

    /**
	 * 
	 */
    protected void plusMinutes() {
        int currentMinutes = 0;
        if (StringUtils.isNotBlank(minutes)) {
            currentMinutes = Integer.parseInt(minutes);
        }
        if (currentMinutes == 55) {
            plusHour();
            minutes = StringUtils.EMPTY;
        } else {
            minutes = String.valueOf(currentMinutes + 5);
        }
        updateFields();
    }

    /**
	 * 
	 */
    public void removeListener(int eventType, Listener listener) {
        checkWidget();
        mTimeText.removeListener(eventType, listener);
    }

    /**
	 * Removes a modification listener from the combo box.
	 * 
	 * @param ml
	 *            ModifyListener
	 */
    public void removeModifyListener(ModifyListener ml) {
        checkWidget();
        mTimeText.removeModifyListener(ml);
    }

    /**
	 * 
	 * @param timeValue
	 */
    public void setText(String timeValue) {
        RE parseExpression = new RE("^(\\d*d[ ]?)?(\\d*h[ ]?)?(\\d*m)?$");
        if (StringUtils.isNotBlank(timeValue)) {
            if (parseExpression.match(timeValue)) {
                for (int i = 1; i < parseExpression.getParenCount(); i++) {
                    String partial = StringUtils.trim(parseExpression.getParen(i));
                    if (partial != null) {
                        if (partial.endsWith("d")) {
                            days = StringUtils.substringBefore(partial, "d");
                        } else if (partial.endsWith("h")) {
                            hours = StringUtils.substringBefore(partial, "h");
                        } else if (partial.endsWith("m")) {
                            minutes = StringUtils.substringBefore(partial, "m");
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
        } else {
            days = StringUtils.EMPTY;
            hours = StringUtils.EMPTY;
            minutes = StringUtils.EMPTY;
        }
        updateFields();
    }

    /**
	 * 
	 */
    protected synchronized void showTimeEntryFields() {
        Display.getCurrent().addFilter(SWT.FocusIn, mOobClickListener);
        mTimeEntryShell = new Shell(Display.getDefault().getActiveShell(), SWT.NO_TRIM | SWT.NO_FOCUS | SWT.BORDER);
        mTimeEntryShell.setLayout(new FillLayout());
        mTimeEntryShell.setSize(200, 150);
        mTimeEntryShell.setBackground(CommonColors.WHITE);
        mTimeEntryShell.setBackgroundMode(SWT.INHERIT_FORCE);
        mTimeEntryShell.addShellListener(new ShellListener() {

            public void shellActivated(ShellEvent e) {
            }

            public void shellClosed(ShellEvent e) {
            }

            public void shellDeactivated(ShellEvent e) {
                kill();
            }

            public void shellDeiconified(ShellEvent e) {
            }

            public void shellIconified(ShellEvent e) {
                kill();
            }
        });
        mTimeEntryComposite = new Composite(mTimeEntryShell, SWT.BORDER);
        GridLayout gl = new GridLayout(3, false);
        gl.marginHeight = 2;
        gl.verticalSpacing = 2;
        mTimeEntryComposite.setLayout(gl);
        mTimeEntryComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Label lblDays = new Label(mTimeEntryComposite, SWT.NONE);
        lblDays.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        lblDays.setText("Days:");
        lblDays.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
        txtDays = new Text(mTimeEntryComposite, SWT.BORDER | SWT.READ_ONLY);
        txtDays.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        btnPlusDays = new Button(mTimeEntryComposite, SWT.NONE);
        btnPlusDays.setImage(NebulaImages.getImage(NebulaImages.IMG_ADD_INFO));
        btnPlusDays.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                plusDay();
            }
        });
        btnMinusDays = new Button(mTimeEntryComposite, SWT.NONE);
        btnMinusDays.setImage(NebulaImages.getImage(NebulaImages.IMG_REMOVE_INFO));
        btnMinusDays.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                minusDay();
            }
        });
        Label lblHours = new Label(mTimeEntryComposite, SWT.NONE);
        lblHours.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        lblHours.setText("Hours:");
        lblHours.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
        txtHours = new Text(mTimeEntryComposite, SWT.BORDER | SWT.READ_ONLY);
        txtHours.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        txtHours.setText(hours);
        btnPlusHours = new Button(mTimeEntryComposite, SWT.NONE);
        btnPlusHours.setImage(NebulaImages.getImage(NebulaImages.IMG_ADD_INFO));
        btnPlusHours.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                plusHour();
            }
        });
        btnMinusHours = new Button(mTimeEntryComposite, SWT.NONE);
        btnMinusHours.setImage(NebulaImages.getImage(NebulaImages.IMG_REMOVE_INFO));
        btnMinusHours.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                minusHour();
            }
        });
        Label lblMinutes = new Label(mTimeEntryComposite, SWT.NONE);
        lblMinutes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        lblMinutes.setText("Minutes:");
        lblMinutes.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
        txtMinutes = new Text(mTimeEntryComposite, SWT.BORDER | SWT.READ_ONLY);
        txtMinutes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        txtMinutes.setText(minutes);
        btnPlusMinutes = new Button(mTimeEntryComposite, SWT.NONE);
        btnPlusMinutes.setImage(NebulaImages.getImage(NebulaImages.IMG_ADD_INFO));
        btnPlusMinutes.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                plusMinutes();
            }
        });
        btnMinusMinutes = new Button(mTimeEntryComposite, SWT.NONE);
        btnMinusMinutes.setImage(NebulaImages.getImage(NebulaImages.IMG_REMOVE_INFO));
        btnMinusMinutes.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                minusMinutes();
            }
        });
        Rectangle bounds = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getBounds();
        Point loc = mParentComposite.toDisplay(this.getLocation());
        if (loc.y + getSize().y + mTimeEntryShell.getSize().y > bounds.height) {
            loc.y = loc.y - mTimeEntryShell.getSize().y + 1;
        } else {
            loc.y = loc.y + getSize().y;
        }
        updateFields();
        mTimeEntryShell.setLocation(loc);
        mTimeEntryShell.layout();
        mTimeEntryShell.setVisible(true);
    }

    /**
	 * 
	 */
    private void updateFields() {
        if (StringUtils.isNotBlank(days) && StringUtils.isBlank(hours) && StringUtils.isBlank(minutes)) {
            if (isTimeTextVisible()) {
                txtDays.setText(MessageFormat.format(PATTERN_ONE, days));
                txtHours.setText(StringUtils.EMPTY);
                txtMinutes.setText(StringUtils.EMPTY);
            }
            mTimeText.setText(MessageFormat.format(PATTERN_ONE, days));
        } else if (StringUtils.isNotBlank(days) && StringUtils.isNotBlank(hours) && StringUtils.isBlank(minutes)) {
            if (isTimeTextVisible()) {
                txtDays.setText(MessageFormat.format(PATTERN_ONE, days));
                txtHours.setText(MessageFormat.format(PATTERN_FOUR, hours));
                txtMinutes.setText(StringUtils.EMPTY);
            }
            mTimeText.setText(MessageFormat.format(PATTERN_TWO, days, hours));
        } else if (StringUtils.isNotBlank(days) && StringUtils.isNotBlank(hours) && StringUtils.isNotBlank(minutes)) {
            if (isTimeTextVisible()) {
                txtDays.setText(MessageFormat.format(PATTERN_ONE, days));
                txtHours.setText(MessageFormat.format(PATTERN_FOUR, hours));
                txtMinutes.setText(MessageFormat.format(PATTERN_SIX, minutes));
            }
            mTimeText.setText(MessageFormat.format(PATTERN_THREE, days, hours, minutes));
        } else if (StringUtils.isBlank(days) && StringUtils.isNotBlank(hours) && StringUtils.isBlank(minutes)) {
            if (isTimeTextVisible()) {
                txtDays.setText(StringUtils.EMPTY);
                txtHours.setText(MessageFormat.format(PATTERN_FOUR, hours));
                txtMinutes.setText(StringUtils.EMPTY);
            }
            mTimeText.setText(MessageFormat.format(PATTERN_FOUR, hours));
        } else if (StringUtils.isBlank(days) && StringUtils.isNotBlank(hours) && StringUtils.isNotBlank(minutes)) {
            if (isTimeTextVisible()) {
                txtDays.setText(StringUtils.EMPTY);
                txtHours.setText(MessageFormat.format(PATTERN_FOUR, hours));
                txtMinutes.setText(MessageFormat.format(PATTERN_SIX, minutes));
            }
            mTimeText.setText(MessageFormat.format(PATTERN_FIVE, hours, minutes));
        } else if (StringUtils.isBlank(days) && StringUtils.isBlank(hours) && StringUtils.isNotBlank(minutes)) {
            if (isTimeTextVisible()) {
                txtDays.setText(StringUtils.EMPTY);
                txtHours.setText(StringUtils.EMPTY);
                txtMinutes.setText(MessageFormat.format(PATTERN_SIX, minutes));
            }
            mTimeText.setText(MessageFormat.format(PATTERN_SIX, minutes));
        } else if (StringUtils.isNotBlank(days) && StringUtils.isBlank(hours) && StringUtils.isNotBlank(minutes)) {
            if (isTimeTextVisible()) {
                txtDays.setText(MessageFormat.format(PATTERN_ONE, days));
                txtHours.setText(StringUtils.EMPTY);
                txtMinutes.setText(MessageFormat.format(PATTERN_SIX, minutes));
            }
            mTimeText.setText(MessageFormat.format(PATTERN_SEVEN, days, minutes));
        } else {
            if (isTimeTextVisible()) {
                txtDays.setText(StringUtils.EMPTY);
                txtHours.setText(StringUtils.EMPTY);
                txtMinutes.setText(StringUtils.EMPTY);
            }
            mTimeText.setText(StringUtils.EMPTY);
        }
    }
}
