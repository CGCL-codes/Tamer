package au.gov.naa.digipres.dpr.reports;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import au.gov.naa.digipres.dpr.task.ReportTask;
import au.gov.naa.digipres.dpr.util.TableSorter;

/**
 *
 */
public abstract class UserReport extends Report implements CalendarMonthReport, FinancialYearReport {

    ReportTableModel reportTableModel;

    protected int reportType;

    int calendarMonth;

    int calendarMonthYear;

    int financialYearStart;

    public UserReport(int reportType) {
        this.reportType = reportType;
    }

    @Override
    public JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        TableSorter sorter = new TableSorter(reportTableModel);
        JTable reportTable = new JTable(sorter) {

            private static final long serialVersionUID = 1L;

            @Override
            public String getToolTipText(MouseEvent arg0) {
                String tipText = null;
                Point point = arg0.getPoint();
                int row = rowAtPoint(point);
                int column = columnAtPoint(point);
                int realColumn = convertColumnIndexToModel(column);
                if (realColumn == 2) {
                    TableModel model = getModel();
                    Object valueAt = model.getValueAt(row, realColumn);
                    tipText = (String) valueAt;
                    if (tipText.length() == 0) {
                        tipText = "";
                    }
                } else {
                    tipText = super.getToolTipText(arg0);
                }
                return tipText;
            }
        };
        sorter.setTableHeader(reportTable.getTableHeader());
        panel.add(new JScrollPane(reportTable));
        return panel;
    }

    static class ReportTableModel extends AbstractTableModel {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        private static final String[] columnNames = { "User Name", "Transfer Job Number", "Tasks Performed" };

        private final List<ReportTask.UserReportEntry> modelRecords;

        public ReportTableModel(List<ReportTask.UserReportEntry> records) {
            modelRecords = records;
        }

        @Override
        public String getColumnName(int arg0) {
            return columnNames[arg0];
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return modelRecords.size();
        }

        public Object getValueAt(int row, int col) {
            ReportTask.UserReportEntry record = modelRecords.get(row);
            switch(col) {
                case 0:
                    return record.userName;
                case 1:
                    return record.transferJobNumber;
                case 2:
                    return record.tasks;
            }
            return null;
        }
    }

    @Override
    public String getTitle() {
        String title = "User Report";
        switch(reportType) {
            case ReportTask.CALENDAR_MONTH_TYPE:
                title += " (calendar month)";
                break;
            case ReportTask.FINANCIAL_YEAR_TYPE:
                title += " (financial year)";
                break;
        }
        return title;
    }

    @Override
    public String getExportTitle() {
        String title = "User Report";
        switch(reportType) {
            case ReportTask.CALENDAR_MONTH_TYPE:
                title = Report.getCalendarMonthExportTitle(title, calendarMonth, calendarMonthYear);
                break;
            case ReportTask.FINANCIAL_YEAR_TYPE:
                title = Report.getFinancialYearExportTitle(title, financialYearStart);
                break;
        }
        return title;
    }

    @Override
    public AbstractTableModel getTableModel() {
        return reportTableModel;
    }

    public void setReportTableModel(ReportTableModel reportTableModel) {
        this.reportTableModel = reportTableModel;
    }

    @Override
    public int getReportType() {
        return reportType;
    }

    public void setMonth(int month, int year) {
        calendarMonth = month;
        calendarMonthYear = year;
    }

    public void setStartYear(int startYear) {
        financialYearStart = startYear;
    }
}
