package com.thoughtworks.fireworks.core.table;

import com.thoughtworks.shadow.ComparableTestShadow;
import com.thoughtworks.shadow.Shadow;
import com.thoughtworks.shadow.ShadowCabinetListener;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class ShadowTableModel implements TableModel, TestListener, ShadowCabinetListener {

    public static final int TEST_METHOD = 0;

    public static final int MESSAGE = 1;

    public static final int TRACE_LOG = 2;

    public static final int TEST_CLASS = 3;

    public static final String[] COLUMN_NAME = new String[] { "Test", "Message", "Trace Log", "Test Class" };

    private final List<TestFailure> testFailures = new ArrayList<TestFailure>();

    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();

    private final TraceLogViewer traceLogViewer;

    public ShadowTableModel(TraceLogViewer traceLogViewer) {
        this.traceLogViewer = traceLogViewer;
    }

    public int getRowCount() {
        return testFailures.size();
    }

    public int getColumnCount() {
        return COLUMN_NAME.length;
    }

    public String getColumnName(int columnIndex) {
        return COLUMN_NAME[columnIndex];
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Shadow.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return testFailures.get(rowIndex).getShadow(columnIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Should not set table value!");
    }

    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }

    public void addError(Test test, Throwable t) {
        testFailures.add(new TestFailure((Shadow) test, t, traceLogViewer));
        fireTableChangeEvent(newTestFailureEvent());
    }

    public void addFailure(Test test, AssertionFailedError t) {
        addError(test, t);
    }

    public void endTest(Test test) {
    }

    public void startTest(Test test) {
    }

    public void afterAddTest(ComparableTestShadow test) {
    }

    public void afterRemoveTest(ComparableTestShadow test) {
    }

    public void endAction() {
    }

    public void startAction() {
        if (testFailures.isEmpty()) {
            return;
        }
        TableModelEvent event = clearTestFailuresEvent();
        testFailures.clear();
        fireTableChangeEvent(event);
    }

    private void fireTableChangeEvent(TableModelEvent event) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).tableChanged(event);
        }
    }

    private TableModelEvent clearTestFailuresEvent() {
        return new TableModelEvent(this, 0, lastRowIndex(), TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
    }

    private TableModelEvent newTestFailureEvent() {
        return new TableModelEvent(this, lastRowIndex(), lastRowIndex(), TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
    }

    private int lastRowIndex() {
        return testFailures.size() - 1;
    }
}
