package remote.scheduler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import remote.proxies.SchedulerServiceBean;
import remote.proxies.TaskSchedule;

/**
 * @author Kasza Miklós
 */
public class SchedulerApplet extends JApplet {

    private static final long serialVersionUID = -4398319700109519116L;

    private TaskPanel _taskPanel;

    private class ScheduleTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 6288429477583102886L;

        private List<TaskSchedule> _schedules;

        public ScheduleTableModel(Calendar date) {
            updateDate(date);
        }

        public int getColumnCount() {
            return 3;
        }

        public int getRowCount() {
            return _schedules.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return _schedules.get(rowIndex).getStartTime();
                case 1:
                    return _schedules.get(rowIndex).getActivityName();
                default:
                    return null;
            }
        }

        public void updateDate(Calendar date) {
            final SchedulerServiceBean scheduler = SchedulerInstance.createScheduler();
            _schedules = scheduler.getSchedulesOnDay(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
            fireTableDataChanged();
        }
    }

    @SuppressWarnings("unused")
    private void initPanels() {
        setLayout(new BorderLayout());
        JPanel westSidePanel = new JPanel();
        westSidePanel.setLayout(new BorderLayout());
        westSidePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        final ScheduleTableModel tableModel = new ScheduleTableModel(new GregorianCalendar());
        final DatePicker picker = new DatePicker(new GregorianCalendar());
        picker.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                tableModel.updateDate(picker.getSelectedDates().get(0));
            }
        });
        westSidePanel.add(picker, BorderLayout.NORTH);
        westSidePanel.add(new JScrollPane(), BorderLayout.CENTER);
        JTable scheduleTable = new JTable(tableModel);
        add(westSidePanel, BorderLayout.WEST);
        add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
    }

    private void initTaskScheduler() {
        Locale.setDefault(new Locale("en", "US"));
        _taskPanel = new TaskPanel(null);
        JButton startButton = _taskPanel.getScheduleButton();
        startButton.setEnabled(false);
        JButton cancelButton = _taskPanel.getCancelButton();
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                destroy();
            }
        });
        getRootPane().setDefaultButton(cancelButton);
        setContentPane(_taskPanel);
    }

    @Override
    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }
        resize(new Dimension(500, 400));
        initTaskScheduler();
    }
}
