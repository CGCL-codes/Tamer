package com.lamatek.swingextras;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * JDateChooser is a simple Date choosing component with similar functionality
 * to JFileChooser and JColorChooser. It can be used as a component, to 
 * be inserted into a client layout, or can display it's own Dialog
 * through use of the {@link #showDialog(Component, String) showDialog} method.
 * <p>
 * JDateChooser can be initialized to the current date using the no argument
 * constructor, or initialized to a predefined date by passing an instance
 * of Calendar to the constructor.<p>
 * Using the JDateChooser dialog works in a similar manner to JFileChooser
 * or JColorChooser. The {@link #showDialog(Component, String) showDialog} method
 * returns an int that equates to the public variables ACCEPT_OPTION, CANCEL_OPTION
 * or ERROR_OPTION.<p>
 * <tt>
 * JDateChooser chooser = new JDateChooser();<br>
 * if (chooser.showDialog(this, "Select a date...") == JDateChooser.ACCEPT_OPTION) {<br>
 * &nbsp;&nbsp;Calendar selectedDate = chooser.getSelectedDate();<br>
 * &nbsp;&nbsp;// process date here...<br>
 * }<p>
 * To use JDateChooser as a component within a GUI, users should subclass
 * JDateChooser and override the {@link #acceptSelection() acceptSelection} and
 * {@link #cancelSelection() cancelSelection} methods to process the 
 * corresponding user selection.<p>
 * The current date can be retrieved by calling {@link #getSelectedDate() getSelectedDate}
 * method.
 */
public class JDateChooser extends JPanel implements ActionListener, DaySelectionListener, IWindow {

    public static final int CANCEL_OPTION = 4;

    private int currentDay;

    private int currentMonth;

    private int currentYear;

    private JLabel dateText;

    private Calendar calendar;

    private Date date;

    private JButton previousYear;

    private JButton previousMonth;

    private JButton nextMonth;

    private JButton nextYear;

    private JButton okay;

    private JButton cancel;

    private JPanel days;

    private DayListener dayListener = new DayListener();

    /**
	 * This constructor creates a new instance of JDateChooser initialized to
	 * the current date.
	 */
    public JDateChooser() {
        this(Calendar.getInstance());
    }

    /**
	 * Creates a new instance of JDateChooser initialized to the given Calendar.
	 */
    public JDateChooser(Calendar c) {
        super();
        this.calendar = c;
        this.calendar.setLenient(true);
        setup();
    }

    private void setup() {
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel header = new JPanel(g);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(2, 0, 2, 0);
        previousYear = (JButton) header.add(new JButton("<<"));
        previousYear.addActionListener(this);
        previousYear.setToolTipText("A�o anterior");
        g.setConstraints(previousYear, c);
        previousMonth = (JButton) header.add(new JButton("<"));
        previousMonth.addActionListener(this);
        previousMonth.setToolTipText("Mes anterior");
        c.gridx++;
        g.setConstraints(previousMonth, c);
        dateText = (JLabel) header.add(new JLabel("", JLabel.CENTER));
        dateText.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        c.gridx++;
        c.weightx = 1.0;
        c.fill = c.BOTH;
        g.setConstraints(dateText, c);
        nextMonth = (JButton) header.add(new JButton(">"));
        nextMonth.addActionListener(this);
        nextMonth.setToolTipText("Mes siguiente");
        c.gridx++;
        c.weightx = 0.0;
        c.fill = c.NONE;
        g.setConstraints(nextMonth, c);
        nextYear = (JButton) header.add(new JButton(">>"));
        nextYear.addActionListener(this);
        nextYear.setToolTipText("A�o siguiente");
        c.gridx++;
        g.setConstraints(nextYear, c);
        updateCalendar(calendar);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okay = (JButton) buttons.add(new JButton("Ok"));
        okay.addActionListener(this);
        cancel = (JButton) buttons.add(new JButton(PluginServices.getText(this, "Cancelar")));
        cancel.addActionListener(this);
        setLayout(new BorderLayout());
        add("North", header);
        days.setBackground(Color.WHITE);
        days.setOpaque(true);
        days.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        add("Center", days);
        add("South", buttons);
    }

    private void updateCalendar(Calendar c) {
        if (days != null) remove(days);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        days = new JPanel(new GridLayout(7, 7));
        Calendar setup = (Calendar) calendar.clone();
        setup.set(Calendar.DAY_OF_WEEK, setup.getFirstDayOfWeek());
        int lastLayoutPosition = 0;
        for (int i = 0; i < 7; i++) {
            int dayInt = setup.get(Calendar.DAY_OF_WEEK);
            if (dayInt == Calendar.MONDAY) days.add(new JLabel("Mon", JLabel.CENTER));
            if (dayInt == Calendar.TUESDAY) days.add(new JLabel("Tue", JLabel.CENTER));
            if (dayInt == Calendar.WEDNESDAY) days.add(new JLabel("Wed", JLabel.CENTER));
            if (dayInt == Calendar.THURSDAY) days.add(new JLabel("Thu", JLabel.CENTER));
            if (dayInt == Calendar.FRIDAY) days.add(new JLabel("Fri", JLabel.CENTER));
            if (dayInt == Calendar.SATURDAY) days.add(new JLabel("Sat", JLabel.CENTER));
            if (dayInt == Calendar.SUNDAY) days.add(new JLabel("Sun", JLabel.CENTER));
            setup.roll(Calendar.DAY_OF_WEEK, true);
            lastLayoutPosition++;
        }
        setup = (Calendar) calendar.clone();
        setup.set(Calendar.DAY_OF_MONTH, 1);
        int first = setup.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < (first - 1); i++) {
            days.add(new JLabel(""));
            lastLayoutPosition++;
        }
        setup.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 0; i < setup.getActualMaximum(setup.DAY_OF_MONTH); i++) {
            DayButton button = new DayButton(setup.get(setup.DAY_OF_MONTH));
            if (setup.get(Calendar.DAY_OF_MONTH) == currentDay) {
                button.setBackground(Color.BLUE);
                button.setForeground(Color.WHITE);
                dayListener.setDia(button);
            }
            button.addDaySelectionListener(this);
            button.addMouseListener(dayListener);
            days.add(button);
            setup.roll(setup.DAY_OF_MONTH, true);
            lastLayoutPosition++;
        }
        for (int i = lastLayoutPosition; i < 49; i++) days.add(new JLabel(""));
        add("Center", days);
        validate();
        setup = null;
        updateLabel();
    }

    private void updateLabel() {
        Date date = calendar.getTime();
        dateText.setText(DateFormat.getInstance().format(date));
    }

    /**
	 * Returns the currently selected Date in the form of a java.util.Calendar
	 * object. Typically called adter receipt of an {@link #ACCEPT_OPTION ACCEPT_OPTION}
	 * (using the {@link #showDialog(Component, String) showDialog} method) or
	 * within the {@link #acceptSelection() acceptSelection} method (using the
	 * JDateChooser as a component.)<p>
	 * @return java.util.Calendar The selected date in the form of a Calendar object.
	 */
    public Calendar getSelectedDate() {
        return calendar;
    }

    /**
	 * Used to process events from the previous month, previous year, next month, next year,
	 * okay and cancel buttons. Users should call super.actionPerformed(ActionEvent) if overriding
	 * this method.
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okay) {
            PluginServices.getMDIManager().closeWindow(this);
        }
        if (e.getSource() == cancel) {
            calendar = null;
            PluginServices.getMDIManager().closeWindow(this);
        }
        if (e.getSource() == previousYear) {
            calendar.roll(Calendar.YEAR, -1);
            updateCalendar(calendar);
        }
        if (e.getSource() == previousMonth) {
            calendar.roll(Calendar.MONTH, -1);
            updateCalendar(calendar);
        }
        if (e.getSource() == nextMonth) {
            calendar.roll(Calendar.MONTH, 1);
            updateCalendar(calendar);
        }
        if (e.getSource() == nextYear) {
            calendar.roll(Calendar.YEAR, 1);
            updateCalendar(calendar);
        }
    }

    /**
	 * Used to process day selection events from the user. This method resets
	 * resets the Calendar object to the selected day. Subclasses should make a call
	 * to super.daySelected() if overriding this method.
	 */
    public void daySelected(int d) {
        calendar.set(Calendar.DAY_OF_MONTH, d);
        updateLabel();
        currentDay = d;
    }

    /**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#getWindowInfo()
	 */
    public WindowInfo getWindowInfo() {
        WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
        m_viewinfo.setTitle(PluginServices.getText(this, "seleccione_fecha"));
        m_viewinfo.setWidth(400);
        m_viewinfo.setHeight(200);
        return m_viewinfo;
    }

    /**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
    public void viewActivated() {
    }

    public Object getWindowProfile() {
        return WindowInfo.DIALOG_PROFILE;
    }
}

class DayListener extends MouseAdapter {

    private DayButton btnDia;

    public void setDia(DayButton btn) {
        btnDia = btn;
    }

    /**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
    public void mouseClicked(MouseEvent e) {
        if (btnDia != null) {
            btnDia.setBackground(Color.WHITE);
            btnDia.setForeground(Color.BLACK);
        }
        btnDia = (DayButton) e.getSource();
        btnDia.setBackground(Color.BLUE);
        btnDia.setForeground(Color.WHITE);
    }
}

class DayButton extends JLabel implements MouseListener {

    private int day;

    private Vector listeners;

    public DayButton(int d) {
        super((new Integer(d)).toString());
        this.day = d;
        addMouseListener(this);
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
        this.setHorizontalAlignment(JLabel.CENTER);
    }

    public void addDaySelectionListener(DaySelectionListener l) {
        if (listeners == null) listeners = new Vector(1, 1);
        listeners.addElement(l);
    }

    public void removeDaySelectionListener(DaySelectionListener l) {
        if (listeners != null) listeners.removeElement(l);
    }

    public void removeAllListeners() {
        listeners = new Vector(1, 1);
    }

    /**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
    public void mouseClicked(MouseEvent e) {
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                ((DaySelectionListener) listeners.elementAt(i)).daySelected(day);
            }
        }
    }

    /**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
    public void mouseEntered(MouseEvent arg0) {
    }

    /**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
    public void mouseExited(MouseEvent arg0) {
    }

    /**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    public void mousePressed(MouseEvent arg0) {
    }

    /**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
    public void mouseReleased(MouseEvent arg0) {
    }
}
