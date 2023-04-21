package de.axxeed.ajatt.gui.misc;

import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import de.axxeed.ajatt.AJaTT;
import de.axxeed.ajatt.Constants;
import de.axxeed.ajatt.gui.dialog.MainWindow;
import de.axxeed.ajatt.gui.dialog.OptionDialog;
import de.axxeed.ajatt.gui.dialog.TaskTree;
import de.axxeed.ajatt.gui.dialog.TimeViewDialog;
import de.axxeed.ajatt.model.Task;
import de.axxeed.ajatt.model.TaskList;
import de.axxeed.ajatt.persistance.LogRecovery;
import de.axxeed.ajatt.persistance.TaskFile;
import de.axxeed.ajatt.report.ExcelReport;
import de.axxeed.ajatt.report.MonthlyReport;

/**
 * Actions.java
 * Created: 23.02.2009 08:27:53
 * @author Markus J. Luzius
 * 
 */
@SuppressWarnings("serial")
public class Actions implements Constants {

    private static Logger log = Logger.getLogger(Actions.class);

    public static final Action INFO = new AbstractAction("Info") {

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "AJaTT - Another Java TimeTracker\n" + "Version " + AJaTT.VERSION + "\n" + "Author: Markus J. Luzius\n" + "(c) 2009 axxeed\n\n" + "http://sourceforge.net/projects/ajatt/\n\n" + "Current Task: " + TaskList.getActiveTask().getTaskPath(), "AJaTT", JOptionPane.INFORMATION_MESSAGE);
        }
    };

    public static final Action QUIT = new AbstractAction("Quit") {

        public void actionPerformed(ActionEvent e) {
            for (Iterator<Task> iter = TaskList.getInstance().getTasks().iterator(); iter.hasNext(); ) {
                Task t = iter.next();
                if (t.isActive()) {
                    t.stop();
                }
            }
            TaskFile.INSTANCE.save();
            MainWindow.INSTANCE.setVisible(false);
        }
    };

    public static final Action OPTIONS = new AbstractAction("Options") {

        public void actionPerformed(ActionEvent e) {
            new OptionDialog();
        }
    };

    public static final Action EXPORT = new AbstractAction("Export") {

        public void actionPerformed(ActionEvent e) {
            TaskFile.INSTANCE.export();
        }
    };

    public static final Action RECOVERY = new AbstractAction("Recovery") {

        public void actionPerformed(ActionEvent e) {
            new LogRecovery();
        }
    };

    public static final Action REPORT = new AbstractAction("Report (all)") {

        public void actionPerformed(ActionEvent e) {
            log.debug("Report");
            StringBuilder msg = new StringBuilder();
            long overallTime = 0;
            long dailyTotal = 0;
            for (Iterator<Task> iter = TaskList.getInstance().getTasks().iterator(); iter.hasNext(); ) {
                Task t = iter.next();
                if (t.getTotalDuration() > 0) {
                    if (!t.hasSubtasks()) {
                        overallTime += t.getTotalDuration();
                        dailyTotal += t.getDailyDuration();
                    }
                    msg.append(SDF_TIME.format(t.getTotalDuration() + LOC));
                    msg.append(" - ");
                    msg.append(SDF_TIME.format(t.getDailyDuration() + LOC));
                    if (t.hasSubtasks()) {
                        msg.append(" ++ ").append(t.getName());
                    } else {
                        msg.append(" -- ").append(t.getName());
                    }
                    if (t.isActive()) {
                        msg.insert(msg.length() - t.getName().length() - 3, "==>");
                        msg.append(" (active)");
                    }
                    msg.append("\n");
                }
            }
            msg.append("\n");
            msg.append(SDF_TIME.format(overallTime + LOC)).append(" - ").append(SDF_TIME.format(dailyTotal + LOC)).append(" - Total");
            JOptionPane.showMessageDialog(null, msg.toString(), "Report", JOptionPane.INFORMATION_MESSAGE);
        }
    };

    public static final Action REPORT_DAY = new AbstractAction("Quick report by date") {

        public void actionPerformed(ActionEvent e) {
            log.debug("daily report");
            String input = JOptionPane.showInputDialog(null, "Date:", "Report by date", JOptionPane.QUESTION_MESSAGE);
            Date reportDate = convertInput(input);
            if (reportDate == null) return;
            StringBuilder msg = new StringBuilder();
            msg.append("Times for " + SDF_DATE.format(reportDate) + ":").append("\n");
            msg.append("\n");
            long dailyTotal = 0;
            for (Iterator<Task> iter = TaskList.getInstance().getTasks().iterator(); iter.hasNext(); ) {
                Task t = iter.next();
                long dur = t.getDurationForDate(reportDate);
                if (dur > 0) {
                    if (!t.hasSubtasks()) {
                        dailyTotal += dur;
                    }
                    msg.append(SDF_TIME.format(dur + LOC));
                    msg.append(" - ").append(t.getName());
                    msg.append("\n");
                }
            }
            msg.append("\n");
            msg.append(SDF_TIME.format(dailyTotal + LOC)).append(" - Total");
            JOptionPane.showMessageDialog(null, msg.toString(), "Report", JOptionPane.INFORMATION_MESSAGE);
        }
    };

    public static final Action REPORT_MONTH = new AbstractAction("Report by month (CSV)") {

        public void actionPerformed(ActionEvent e) {
            log.debug("Report by month (CSV)");
            new MonthlyReport();
        }
    };

    public static final Action REPORT_MONTH_EXCEL = new AbstractAction("Report by month (Excel)") {

        public void actionPerformed(ActionEvent e) {
            log.debug("Report by month (Excel)");
            new ExcelReport();
            JOptionPane.showMessageDialog(null, "Excel file has been saved.", "Report by month", JOptionPane.INFORMATION_MESSAGE);
        }
    };

    public static final Action TIMES_TABLE = new AbstractAction("Report by date") {

        public void actionPerformed(ActionEvent e) {
            log.debug("Table of task times by date");
            String input = JOptionPane.showInputDialog(null, "Date (relative or dd.mm.yyyy):", "Report by date", JOptionPane.QUESTION_MESSAGE);
            Date reportDate = convertInput(input);
            if (reportDate == null) return;
            TimeViewDialog tvd = new TimeViewDialog(reportDate);
            tvd.setVisible(true);
        }
    };

    public static final Action TREE = new AbstractAction("Task view") {

        public void actionPerformed(ActionEvent e) {
            TaskTree tt = new TaskTree();
            tt.setVisible(true);
        }
    };

    private static Date convertInput(String input) {
        if (input == null || input.trim().length() < 1) return null;
        Date reportDate = null;
        int diffDays = 0;
        if (input.toLowerCase().charAt(0) == 'h' || input.toLowerCase().charAt(0) == 't') {
            return today;
        } else if (input.toLowerCase().charAt(0) == 'g' || input.toLowerCase().charAt(0) == 'y') {
            diffDays = -1;
        } else if (input.toLowerCase().charAt(0) == 'm') {
            diffDays = -cal.get(Calendar.DAY_OF_MONTH) + 1;
        }
        try {
            if (diffDays == 0) diffDays = Integer.parseInt(input);
            GregorianCalendar localCal = (GregorianCalendar) cal.clone();
            localCal.add(Calendar.DAY_OF_MONTH, diffDays);
            reportDate = new GregorianCalendar(localCal.get(Calendar.YEAR), localCal.get(Calendar.MONTH), localCal.get(Calendar.DAY_OF_MONTH)).getTime();
            return reportDate;
        } catch (NumberFormatException e) {
        }
        try {
            reportDate = SDF_DATE.parse(input);
        } catch (ParseException e) {
            log.warn("wrong date format: " + input);
            return null;
        }
        return reportDate;
    }
}
