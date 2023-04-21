package org.wiztools.restclient.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import org.wiztools.restclient.TestExceptionResult;
import org.wiztools.restclient.TestResult;

/**
 *
 * @author NEWUSER
 */
class TestResultPanel extends JPanel {

    private TestResult lastTestResult;

    private JLabel jl_runCount = new JLabel("");

    private JLabel jl_failureCount = new JLabel("");

    private JLabel jl_errorCount = new JLabel("");

    private FailureTableModel tm_failures = new FailureTableModel();

    private FailureTableModel tm_errors = new FailureTableModel();

    private JScrollPane jsp_jt_failures;

    private JScrollPane jsp_jt_errors;

    private JTextArea jta_trace = new JTextArea();

    private JScrollPane jsp_jta_trace;

    private JLabel jl_status = new JLabel();

    private JLabel jl_icon = new JLabel();

    private Icon ICON_DEFAULT = UIUtil.getIconFromClasspath("org/wiztools/restclient/test/eye.png");

    private Icon ICON_SUCCESS = UIUtil.getIconFromClasspath("org/wiztools/restclient/test/accept.png");

    private Icon ICON_FAILURE = UIUtil.getIconFromClasspath("org/wiztools/restclient/test/cross.png");

    private static final Font BOLD_FONT = new Font(Font.DIALOG, Font.PLAIN, 18);

    TestResultPanel() {
        super();
        init();
    }

    private void init() {
        JPanel jp = this;
        jp.setLayout(new BorderLayout(5, 5));
        JPanel jp_north = new JPanel();
        jp_north.setLayout(new BorderLayout(5, 5));
        jl_icon.setIcon(ICON_DEFAULT);
        jp_north.add(jl_icon, BorderLayout.WEST);
        jp_north.add(jl_status, BorderLayout.CENTER);
        jp.add(jp_north, BorderLayout.NORTH);
        JTabbedPane jtp = new JTabbedPane();
        JPanel jp_summary = new JPanel();
        jp_summary.setLayout(new GridLayout(3, 1));
        {
            JPanel jp_t;
            JLabel jl_t;
            jl_runCount.setFont(BOLD_FONT);
            jl_failureCount.setFont(BOLD_FONT);
            jl_errorCount.setFont(BOLD_FONT);
            jp_t = new JPanel();
            jp_t.setLayout(new BorderLayout());
            jl_t = new JLabel("Tests Run: ");
            jl_t.setFont(BOLD_FONT);
            jp_t.add(jl_t, BorderLayout.CENTER);
            jp_t.add(jl_runCount, BorderLayout.EAST);
            jp_summary.add(jp_t);
            jp_t = new JPanel();
            jp_t.setLayout(new BorderLayout());
            jl_t = new JLabel("Tests Failures: ");
            jl_t.setFont(BOLD_FONT);
            jp_t.add(jl_t, BorderLayout.CENTER);
            jp_t.add(jl_failureCount, BorderLayout.EAST);
            jp_summary.add(jp_t);
            jp_t = new JPanel();
            jp_t.setLayout(new BorderLayout());
            jl_t = new JLabel("Tests Errors: ");
            jl_t.setFont(BOLD_FONT);
            jp_t.add(jl_t, BorderLayout.CENTER);
            jp_t.add(jl_errorCount, BorderLayout.EAST);
            jp_summary.add(jp_t);
        }
        {
            JPanel jp_t = new JPanel();
            jp_t.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp_t.add(jp_summary);
            jtp.add("Summary", jp_t);
        }
        {
            JTable jt = new JTable(tm_failures);
            Dimension d = jt.getPreferredSize();
            d.height = d.height / 2;
            jt.setPreferredScrollableViewportSize(d);
            jsp_jt_failures = new JScrollPane(jt);
            jtp.add("Failures", jsp_jt_failures);
        }
        {
            JTable jt = new JTable(tm_errors);
            Dimension d = jt.getPreferredSize();
            d.height = d.height / 2;
            jt.setPreferredScrollableViewportSize(d);
            jsp_jt_errors = new JScrollPane(jt);
            jtp.add("Errors", jsp_jt_errors);
        }
        {
            jta_trace.setEditable(false);
            jsp_jta_trace = new JScrollPane(jta_trace);
            jtp.add("Trace", jsp_jta_trace);
        }
        jp.add(jtp, BorderLayout.CENTER);
    }

    void clear() {
        jl_icon.setIcon(ICON_DEFAULT);
        jl_runCount.setText("");
        jl_failureCount.setText("");
        jl_errorCount.setText("");
        jl_status.setText("");
        tm_failures.setData(Collections.EMPTY_LIST);
        tm_errors.setData(Collections.EMPTY_LIST);
        jta_trace.setText("");
    }

    void setTestResult(TestResult result) {
        if (result == null) {
            return;
        }
        lastTestResult = result;
        int runCount = result.getRunCount();
        int failureCount = result.getFailureCount();
        int errorCount = result.getErrorCount();
        if (failureCount > 0 || errorCount > 0) {
            jl_icon.setIcon(ICON_FAILURE);
        } else {
            jl_icon.setIcon(ICON_SUCCESS);
        }
        jl_status.setText("Tests run: " + runCount + ", Failures: " + failureCount + ", Errors: " + errorCount);
        jl_runCount.setText(String.valueOf(runCount));
        jl_failureCount.setText(String.valueOf(failureCount));
        jl_errorCount.setText(String.valueOf(errorCount));
        tm_failures.setData(result.getFailures());
        tm_errors.setData(result.getErrors());
        Dimension d = jsp_jta_trace.getPreferredSize();
        jta_trace.setText(result.toString());
        jta_trace.setCaretPosition(0);
        jsp_jta_trace.setPreferredSize(d);
    }

    TestResult getTestResult() {
        return lastTestResult;
    }

    class FailureTableModel extends AbstractTableModel {

        private Object[] failures;

        public void setData(List<TestExceptionResult> failures) {
            if (failures != null) {
                this.failures = failures.toArray();
            }
            fireTableDataChanged();
        }

        @Override
        public String getColumnName(int col) {
            if (col == 0) {
                return "Message";
            } else {
                return "Line";
            }
        }

        public int getRowCount() {
            if (failures == null) {
                return 0;
            }
            return failures.length;
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            TestExceptionResult bean = (TestExceptionResult) failures[rowIndex];
            if (columnIndex == 0) {
                return bean.getExceptionMessage();
            } else {
                return bean.getLineNumber();
            }
        }
    }
}
