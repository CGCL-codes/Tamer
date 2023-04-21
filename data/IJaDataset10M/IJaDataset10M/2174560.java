package gui;

import gui.widgets.AdvancedRecurrenceWidget;
import gui.widgets.FormatBox;
import gui.widgets.NthDayOfMonthRecurrenceWidget;
import gui.widgets.NthDayRecurrenceWidget;
import gui.widgets.RecurrenceWidget;
import gui.widgets.WeeklyRecurrenceWidget;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import observer.Observer;
import subsystem.Event;
import subsystem.Period;
import subsystem.SimpleDate;

/**Add event dialog.  Superclass for all of the event dialogs.
 * 
 * @author Vladimir Hadzhiyski
 * @author Robert Middleton
 *
 */
public abstract class EventDialog implements ActionListener, Observer {

    static final boolean shouldFill = true;

    static final boolean shouldWeightX = true;

    static final boolean RIGHT_TO_LEFT = false;

    protected FormatBox startDate;

    protected FormatBox endDate;

    protected JTextField startTime;

    protected JTextField endTime;

    protected JTextField title;

    protected JTextField location;

    protected JTextArea description;

    protected JCheckBox recurring;

    protected JComboBox recurringType;

    protected RecurrenceWidget currentWidget;

    protected WeeklyRecurrenceWidget weeklyWidget;

    protected NthDayOfMonthRecurrenceWidget nthDayOfMonthWidget;

    protected NthDayRecurrenceWidget nthDayWidget;

    protected AdvancedRecurrenceWidget advancedWidget;

    protected CakeGUI parent;

    protected JDialog dialog;

    protected JFrame theFrame = new JFrame();

    protected JPanel thePane;

    protected boolean shown;

    protected Event globalE;

    /**Constructor
	 * 
	 * @param parent The CakeCal to add the event to.
	 */
    public EventDialog(CakeGUI parent) {
        this.parent = parent;
        shown = false;
        startDate = new FormatBox("date");
        startTime = new JTextField();
        endDate = new FormatBox("date");
        endTime = new JTextField();
        thePane = new JPanel(new GridLayout(1, 1));
        thePane.add(addComponentsToPane());
        dialog = new JDialog(theFrame, "Add Event");
    }

    /**
	 * Creates a main panel for the dialog box

	 * 
	 * @return		pane				A new JPanel filled with all the elements of the dialog box.
	 */
    private JPanel addComponentsToPane() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        if (RIGHT_TO_LEFT) {
            gridPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        gridPanel.add(middlePanel(), c);
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        gridPanel.add(bottomPanel(), c);
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0;
        gridPanel.add(buttonsPanel(), c);
        return gridPanel;
    }

    /** Returns a JPanel with the middle part of the dialog box, the start/end date/time
	 * 
	 * 
	 * @author vrh88879
	 * 
	 * @return It returns start/end date/time panel
	 */
    private JPanel middlePanel() {
        JPanel pane = new JPanel();
        pane.removeAll();
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 10, 10);
        if (shouldFill) {
            c.fill = GridBagConstraints.HORIZONTAL;
        }
        JLabel label = new JLabel("Subject:     ");
        c.gridx = 1;
        c.gridy = 0;
        pane.add(label, c);
        title = new JTextField(10);
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(title, c);
        label = new JLabel("Location:    ");
        c.gridx = 1;
        c.gridy = 1;
        pane.add(label, c);
        location = new JTextField(10);
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(location, c);
        label = new JLabel(" ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        pane.add(label, c);
        label = new JLabel("Start");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        pane.add(label, c);
        label = new JLabel("End");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        pane.add(label, c);
        label = new JLabel(" ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        pane.add(label, c);
        label = new JLabel(" ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 2;
        pane.add(label, c);
        startDate.setColumns(10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        pane.add(startDate, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        pane.add(startTime, c);
        SimpleDate d = new SimpleDate(parent.getCurrentMonth(), parent.getCurrentDay(), parent.getCurrentYear());
        startDate.setData(d.format());
        endDate.setData(d.format());
        endDate.setColumns(10);
        endDate.setEnabled(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 3;
        pane.add(endDate, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 4;
        pane.add(endTime, c);
        label = new JLabel(" ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 6;
        pane.add(label, c);
        label = new JLabel("Recurring? ");
        c.gridx = 1;
        c.gridy = 6;
        pane.add(label, c);
        recurring = new JCheckBox();
        recurring.addActionListener(this);
        recurring.setActionCommand("recurring");
        c.gridx = 2;
        c.gridy = 6;
        pane.add(recurring, c);
        recurringType = new JComboBox();
        recurringType.addActionListener(this);
        recurringType.setActionCommand("recurringTypeChanged");
        recurringType.setVisible(false);
        recurringType.addItem("Week Days");
        recurringType.addItem("N Days");
        recurringType.addItem("Nth Day of Month");
        recurringType.addItem("Custom");
        c.gridx = 1;
        c.gridy = 7;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(recurringType, c);
        weeklyWidget = new WeeklyRecurrenceWidget();
        weeklyWidget.setVisible(false);
        c.gridx = 1;
        c.gridy = 8;
        c.gridwidth = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        pane.add(weeklyWidget, c);
        nthDayOfMonthWidget = new NthDayOfMonthRecurrenceWidget();
        nthDayOfMonthWidget.setVisible(false);
        c.gridx = 1;
        c.gridy = 9;
        c.gridwidth = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        pane.add(nthDayOfMonthWidget, c);
        nthDayWidget = new NthDayRecurrenceWidget();
        nthDayWidget.setVisible(false);
        c.gridx = 1;
        c.gridy = 9;
        c.gridwidth = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        pane.add(nthDayWidget, c);
        advancedWidget = new AdvancedRecurrenceWidget();
        advancedWidget.setVisible(false);
        c.gridx = 1;
        c.gridy = 9;
        c.gridwidth = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        pane.add(advancedWidget, c);
        return pane;
    }

    /** Returns the 'Description' field and the label.
	 * 
	 * 
	 * @author vrh88879
	 * 
	 * @return It returns 'Description' and the JTextBox panel -> bottomPanel
	 */
    private JPanel bottomPanel() {
        JPanel pane = new JPanel();
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill) {
            c.fill = GridBagConstraints.HORIZONTAL;
        }
        c.insets = new Insets(0, 0, 10, 10);
        JLabel label = new JLabel("Description:");
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 2;
        pane.add(label, c);
        description = new JTextArea(5, 20);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 2;
        c.weighty = 3;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(description);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.add(scrollPane, c);
        return pane;
    }

    /** Makes a JPanel with two buttons, 'Add Event' and 'Cancel'
	 * 
	 * 
	 * 
	 * @return JPanel with two buttons - 'Add Event' and 'Cancel'
	 */
    protected abstract JPanel buttonsPanel();

    /**Displays the dialog box.  
	 * 
	 */
    public void showDialogBox() {
        if (shown) {
            SimpleDate d = new SimpleDate(parent.getCurrentMonth(), parent.getCurrentDay(), parent.getCurrentYear());
            startDate.setData(d.format());
            endDate.setData(d.format());
            dialog.setVisible(true);
            return;
        }
        JPanel pane = new JPanel(new GridLayout(1, 1));
        pane.add(addComponentsToPane());
        dialog.add(pane);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setResizable(true);
        dialog.setSize(320, 420);
        dialog.setLocationRelativeTo(null);
        dialog.requestFocus();
        dialog.setFocusableWindowState(true);
        dialog.setVisible(true);
        shown = true;
    }

    /**Makes it invisible!  You must call this, or else the dialog box will not dissapear.
	 * Note: this simply makes the dialog invisible, it does not actually end the process as that
	 * causes the CakeGUI to close as well.  This still has some legacy code from when this was
	 * a pop-up dialog; the functionality to make it a pop-up dialog should still exist elsewhere
	 * in the code.
	 */
    public void close() {
        title.setText("");
        description.setText("");
        location.setText("");
        startTime.setText("");
        endTime.setText("");
        recurring.setSelected(false);
        recurringType.setVisible(false);
        recurringType.setSelectedIndex(0);
        if (currentWidget != null) {
            currentWidget.setVisible(false);
            currentWidget.reset();
            currentWidget = null;
        }
        if (dialog.isVisible()) {
            dialog.setVisible(false);
        }
        parent.getDayView().clear();
        parent.resetEventPanel();
    }

    /**Update the current month and current day from the CakeGUI.
	 * 
	 */
    public void updateData() {
        SimpleDate d = new SimpleDate(parent.getCurrentMonth(), parent.getCurrentDay(), parent.getCurrentYear());
        startDate.setData(d.format());
        endDate.setData(d.format());
    }

    /**Fill in the dialog with the information passed in.
	 * 
	 * @param e The event to fill in the information for.
	 */
    public void fillIn(Event e) {
        globalE = e;
        title.setText(e.getTitle());
        description.setText(e.getDescription());
        startDate.setData(e.getPeriod().start.date.format());
        endDate.setData(e.getPeriod().end.date.format());
        location.setText(e.getLocation());
        int hour = e.getPeriod().start.time.hour;
        int min = e.getPeriod().start.time.minutes;
        startTime.setText((hour > 12 ? hour - 12 : hour) + ":" + (min == 0 ? "00" : min) + (hour >= 12 ? "PM" : "AM"));
        hour = e.getPeriod().end.time.hour;
        min = e.getPeriod().end.time.minutes;
        endTime.setText((hour > 12 ? hour - 12 : hour) + ":" + (min == 0 ? "00" : min) + (hour >= 12 ? "PM" : "AM"));
        if ((e.getRecurringType()).equals("0")) {
            recurring.setSelected(false);
            recurringType.setVisible(false);
        } else {
            recurring.setSelected(true);
            recurringType.setVisible(true);
            recurringType.setSelectedItem("Custom");
            this.advancedWidget.setREx(e.getRecurringType());
            endDate.setEnabled(true);
        }
    }

    /**Do something based on an action.  Provides basic implementation.
	 * 
	 * @param e
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "recurring") {
            recurringType.setVisible(!recurringType.isVisible());
            if (recurringType.isVisible()) {
                if (recurringType.getSelectedItem().equals("Week Days")) {
                    weeklyWidget.setVisible(true);
                    currentWidget = weeklyWidget;
                } else if (recurringType.getSelectedItem().equals("Nth Day of Month")) {
                    nthDayOfMonthWidget.setVisible(true);
                    currentWidget = nthDayOfMonthWidget;
                } else if (recurringType.getSelectedItem().equals("N Days")) {
                    nthDayWidget.setVisible(true);
                    currentWidget = nthDayWidget;
                } else if (recurringType.getSelectedItem().equals("Custom")) {
                    advancedWidget.setVisible(true);
                    currentWidget = advancedWidget;
                }
            } else {
                weeklyWidget.setVisible(false);
                nthDayOfMonthWidget.setVisible(false);
                nthDayWidget.setVisible(false);
                advancedWidget.setVisible(false);
                currentWidget = null;
            }
        } else if (e.getActionCommand() == "recurringTypeChanged") {
            try {
                weeklyWidget.setVisible(false);
                nthDayOfMonthWidget.setVisible(false);
                nthDayWidget.setVisible(false);
                advancedWidget.setVisible(false);
                currentWidget = null;
                if (recurringType.getSelectedItem().equals("Week Days")) {
                    weeklyWidget.setVisible(true);
                    currentWidget = weeklyWidget;
                } else if (recurringType.getSelectedItem().equals("Nth Day of Month")) {
                    nthDayOfMonthWidget.setVisible(true);
                    currentWidget = nthDayOfMonthWidget;
                } else if (recurringType.getSelectedItem().equals("N Days")) {
                    nthDayWidget.setVisible(true);
                    currentWidget = nthDayWidget;
                } else if (recurringType.getSelectedItem().equals("Custom")) {
                    advancedWidget.setVisible(true);
                    currentWidget = advancedWidget;
                }
            } catch (Exception n) {
            }
        }
        if (!recurring.isSelected()) endDate.setEnabled(false); else endDate.setEnabled(true);
    }

    protected Period validateTime() throws Exception {
        if (startDate.isValid() && endDate.isValid()) {
            String startT = startTime.getText();
            int startHour = 0;
            int startMin = 0;
            String[] startTT = startTime.getText().split(":|[a-z]|[A-z]");
            switch(startTT.length) {
                case 0:
                    throw new BadTimeException();
                case 1:
                    if (startTT[0].length() < 3) {
                        throw new BadTimeException("Your time must have at least 3 digits.\n" + "For example, 8:00 and 800 are both valid\n" + "representations of time.");
                    }
                    startMin = Integer.parseInt(startTT[0].substring(startTT[0].length() - 2));
                    startHour = Integer.parseInt(startTT[0].substring(0, startTT[0].length() - 2));
                    break;
                case 2:
                    startHour = Integer.parseInt(startTT[0]);
                    startMin = Integer.parseInt(startTT[1]);
                    break;
                default:
                    throw new BadTimeException("Your time is formatted incorrectly");
            }
            String endT = endTime.getText();
            int endHour = 0;
            int endMin = 0;
            String[] endTT = endTime.getText().split(":|[a-z]|[A-z]");
            switch(endTT.length) {
                case 0:
                    throw new BadTimeException();
                case 1:
                    if (endTT[0].length() < 3) {
                        throw new BadTimeException("Your time must have at least 3 digits.\n" + "For example, 8:00 and 800 are both valid\n" + "representations of time.");
                    }
                    endMin = Integer.parseInt(endTT[0].substring(endTT[0].length() - 2));
                    endHour = Integer.parseInt(endTT[0].substring(0, endTT[0].length() - 2));
                    break;
                case 2:
                    endHour = Integer.parseInt(endTT[0]);
                    endMin = Integer.parseInt(endTT[1]);
                    break;
                default:
                    throw new BadTimeException("Your time is formatted incorrectly");
            }
            if (startT.contains("p") || startT.contains("P")) {
                startHour += 12;
            }
            if (endT.contains("p") || endT.contains("P")) {
                endHour += 12;
            }
            if (startHour > 23 || startMin > 59 || endHour > 23 || endMin > 59) {
                throw new BadTimeException("Your hours or minutes fall outside of the valid range");
            }
            if ((startHour * 100 + startMin) >= (endHour * 100 + endMin)) {
                throw new BadTimeException("Your event starts before it ends!\n" + "start time: " + startHour + ":" + (startMin == 0 ? "00" : startMin) + "\n" + "end time: " + endHour + ":" + (endMin == 0 ? "00" : endMin));
            }
            return Period.parse(startDate.format() + ":" + startHour + "." + startMin + "-" + endDate.format() + ":" + endHour + "." + endMin);
        } else {
            throw new BadTimeException("Check your time!");
        }
    }
}

@SuppressWarnings("serial")
class BadTimeException extends Exception {

    public BadTimeException() {
        super();
    }

    public BadTimeException(String str) {
        super(str);
    }
}
