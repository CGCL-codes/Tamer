package ui.view.goal;

import interfaces.ITrainingDay;
import interfaces.Notifyable;
import interfaces.Subject;
import interfaces.observers.ChangeObserver;
import interfaces.observers.InsertionObserver;
import interfaces.observers.SelectionObserver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import resources.Messages;
import utils.DateFormats;
import utils.Units;
import application.Neki;

public class GoalPanel extends JPanel implements InsertionObserver, ChangeObserver, SelectionObserver {

    private static final long serialVersionUID = 1L;

    public static JTable goalsTable;

    public static SideTableModel goalsTableModel;

    public static GoalsCalendarPanel goalsCalendarPanel;

    public static int selItemIndex = 0;

    public GoalPanel() {
        super();
        this.setBackground(Color.WHITE);
        JPanel infoText;
        this.setLayout(new BorderLayout());
        infoText = new JPanel();
        infoText.setBackground(Neki.currentProfile.getColorScheme().headerBar);
        JLabel header = new JLabel(Messages.getString("GoalPanel.goalsProgress"));
        header.setFont(new java.awt.Font("Dialog", 1, 18));
        header.setForeground(Neki.currentProfile.getColorScheme().headerBarText);
        infoText.add(header, BorderLayout.NORTH);
        this.add(infoText, BorderLayout.NORTH);
        goalsCalendarPanel = new GoalsCalendarPanel();
        this.add(goalsCalendarPanel, BorderLayout.CENTER);
        goalsTable = new JTable(3, 2);
        JTableHeader tabHeader = new JTableHeader();
        goalsTable.setTableHeader(null);
        goalsTableModel = new SideTableModel();
        goalsTable.setModel(goalsTableModel);
        goalsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        goalsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent lse) {
                System.out.println(":: GoalPanel - ListSelectionListener " + lse);
                System.out.println("sel row : " + goalsTable.getSelectedRow());
                selItemIndex = goalsTable.getSelectedRow();
                ITrainingDay iday = goalsTableModel.getItem(selItemIndex);
                Neki.getGoalModel().setSelected(iday);
                int y = iday.getDateTime().getYear();
                int m = iday.getDateTime().getMonthOfYear();
                goalsCalendarPanel.newCalendar.setYearMonth(y, m);
            }
        });
        Neki.getGoalModel().add_INSObserver(this);
        Neki.getGoalModel().add_CHANGEObserver(this);
        Neki.getGoalModel().add_SELObserver(this);
        JScrollPane tableScroll = new JScrollPane(goalsTable);
        tableScroll.getViewport().setBackground(Color.white);
        tableScroll.setBorder(BorderFactory.createLineBorder(Color.black));
        tableScroll.setPreferredSize(new Dimension(180, 180));
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        Border eb = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border line = BorderFactory.createLineBorder(Color.BLACK, 1);
        Border cb = BorderFactory.createCompoundBorder(line, eb);
        tablePanel.setBorder(eb);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Neki.getProfile().getColorScheme().headerBar);
        tablePanel.add(buttonsPanel, BorderLayout.SOUTH);
        JButton deleteB = new JButton(Messages.getString("GoalPanel.delete"));
        deleteB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (selItemIndex < goalsTableModel.getSize()) {
                    ITrainingDay iday = goalsTableModel.getItem(selItemIndex);
                    goalsTableModel.del(iday);
                    Neki.getGoalModel().deleteTrainingDay(iday);
                    Neki.saveGoals();
                }
            }
        });
        deleteB.setBackground(Neki.currentProfile.getColorScheme().buttonBackground);
        deleteB.setForeground(Neki.currentProfile.getColorScheme().buttonText);
        buttonsPanel.add(deleteB);
        JButton changeB = new JButton(Messages.getString("ChangeProfileDialogue.change"));
        changeB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (selItemIndex < goalsTableModel.getSize()) {
                    ITrainingDay iday = goalsTableModel.getItem(selItemIndex);
                    TrainingDayDialog dialog = new TrainingDayDialog();
                    dialog.setTrainingDay(iday);
                    dialog.setVisible(true);
                }
            }
        });
        changeB.setBackground(Neki.currentProfile.getColorScheme().buttonBackground);
        changeB.setForeground(Neki.currentProfile.getColorScheme().buttonText);
        buttonsPanel.add(changeB);
        this.add(tablePanel, BorderLayout.EAST);
        JPanel barHolderPanel = new JPanel();
        barHolderPanel.setLayout(new BoxLayout(barHolderPanel, BoxLayout.PAGE_AXIS));
        barHolderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        barHolderPanel.setBackground(Color.WHITE);
        JLabel progressBar;
        progressBar = new JLabel("Bar for Progress:     [xxxxxxxxxxxx               ]");
        progressBar.setFont(new java.awt.Font("Dialog", 1, 20));
        progressBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel motivationBar;
        motivationBar = new JLabel("Bar for Motivation:  [xxxxxxxx                      ]");
        motivationBar.setFont(new java.awt.Font("Dialog", 1, 20));
        motivationBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.add(barHolderPanel, BorderLayout.SOUTH);
    }

    public GoalsCalendarPanel getGoalCalendarPanel() {
        return this.goalsCalendarPanel;
    }

    public void update(Subject o, Notifyable carrier) {
        if (carrier.getCommand() == Subject.SELECT) {
            System.out.println("%%%%%%%%%%%%% goalpanel select update");
            ITrainingDay selTd = Neki.getGoalModel().getSelected();
            int index = goalsTableModel.getIndex(selTd);
            goalsTable.getSelectionModel().setSelectionInterval(index, index);
        } else {
            System.out.println(":: GoalPanel -- Update of Table");
            ArrayList<ITrainingDay> tdList = Neki.getGoalModel().getCollection();
            this.goalsTableModel.reset();
            for (ITrainingDay td : tdList) {
                System.out.println(td.getGoal());
                this.goalsTableModel.add(td);
            }
        }
        this.goalsTable.updateUI();
    }

    public class SideTableModel extends AbstractTableModel {

        ArrayList<ITrainingDay> list = new ArrayList<ITrainingDay>();

        /** Constructor */
        public SideTableModel() {
            ArrayList<ITrainingDay> tdList = Neki.getGoalModel().getCollection();
            for (ITrainingDay td : tdList) {
                System.out.println(td.getGoal());
                this.add(td);
            }
        }

        public void reset() {
            this.list.clear();
        }

        public void add(ITrainingDay itd) {
            this.list.add(itd);
        }

        public void del(ITrainingDay itd) {
            for (ITrainingDay it : this.list) {
                if (it.equals(itd)) {
                    this.list.remove(itd);
                    System.out.println(":: GoalPanel - TableModel: TrainingDay deleted");
                    break;
                }
            }
            updateUI();
        }

        public int getSize() {
            return this.list.size();
        }

        public ITrainingDay getItem(int i) {
            return this.list.get(i);
        }

        public int getIndex(ITrainingDay idt) {
            for (int i = 0; i < getSize(); i++) {
                if (getItem(i).getDateTime().equals(idt.getDateTime())) {
                    return i;
                }
            }
            return 0;
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return list.size();
        }

        public Object getValueAt(int a, int b) {
            if (a < list.size()) {
                ITrainingDay id = list.get(a);
                if (b == 0) {
                    int y = id.getDateTime().getYear();
                    int m = id.getDateTime().getMonthOfYear();
                    int d = id.getDateTime().getDayOfMonth();
                    switch(Neki.currentProfile.getDateFormatFlag()) {
                        case DateFormats.DDMMYYYY:
                            return Integer.toString(d) + "/" + Integer.toString(m) + "/" + Integer.toString(y);
                        case DateFormats.MMDDYYYY:
                            return Integer.toString(m) + "/" + Integer.toString(d) + "/" + Integer.toString(y);
                    }
                    return Integer.toString(d) + "/" + Integer.toString(m) + "/" + Integer.toString(y);
                } else {
                    if (id.getGoalType() == ITrainingDay.GOAL_PLAIN) {
                        return "Goal";
                    } else if (id.getGoalType() == ITrainingDay.GOAL_DISTANCE) {
                        switch(id.getDistanceUnitType()) {
                            case Units.AMERICAN_MILES:
                                {
                                    NumberFormat pf = NumberFormat.getInstance(Neki.getAppLocale());
                                    if (pf instanceof DecimalFormat) {
                                        ((DecimalFormat) pf).setDecimalSeparatorAlwaysShown(true);
                                        ((DecimalFormat) pf).applyPattern(".00");
                                    }
                                    float milesinput = ((float) (id.getValue()) / 1000f) * Units.unit_to_miles_conversions[Units.KILOMETERS];
                                    return pf.format(milesinput) + " " + Units.unit_shortNames[Units.AMERICAN_MILES];
                                }
                            case Units.KILOMETERS:
                                {
                                    NumberFormat pf = NumberFormat.getInstance(Neki.getAppLocale());
                                    if (pf instanceof DecimalFormat) {
                                        ((DecimalFormat) pf).setDecimalSeparatorAlwaysShown(true);
                                        ((DecimalFormat) pf).applyPattern(".00");
                                    }
                                    float kminput = (float) (id.getValue()) / 1000f;
                                    return pf.format(kminput) + " " + Units.unit_shortNames[Units.KILOMETERS];
                                }
                            default:
                                return Integer.toString(id.getValue()) + " m";
                        }
                    } else {
                        return Integer.toString(id.getValue()) + " min";
                    }
                }
            }
            return null;
        }
    }
}
