package com.testonica.kickelhahn.core.ui.svf;

import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import com.testonica.kickelhahn.core.formats.svf.SVFCommand;
import com.testonica.kickelhahn.core.formats.svf.SVFExecutionResult;

/**
 * @author Sergei Devadze
 */
class SVFTableModel extends DefaultTableModel {

    /** Column names */
    private static final String[] columnNames = { "Nr.", "Command", "Comment" };

    /** Column classes */
    private static final Class[] columnClasses = { Integer.class, SVFCommand.class, String.class };

    protected static final Color EXEC_BACK = new Color(204, 255, 153);

    protected static final Color ERROR_LABEL = Color.RED;

    /** Listeners */
    private java.util.Vector<PropertyButtonListener> buttonListeners = new java.util.Vector<PropertyButtonListener>();

    /** Controls editableness of table */
    private boolean isReadOnly = false;

    private int nextExecutedRow = -1;

    private int errorRow = -1;

    private int executionErrorRow = -1;

    private SVFExecutionResult[] results = new SVFExecutionResult[0];

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int i) {
        return columnNames[i];
    }

    public Class<?> getColumnClass(int i) {
        return columnClasses[i];
    }

    public void setRowCount(int count) {
        super.setRowCount(count);
        results = new SVFExecutionResult[count];
    }

    public boolean isCellEditable(int row, int col) {
        if (isReadOnly) return (col == 0); else return true;
    }

    public void setReadOnly(boolean b) {
        this.isReadOnly = b;
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public void addPropertyButtonListener(PropertyButtonListener listener) {
        buttonListeners.addElement(listener);
    }

    public void removePropertyButtonListener(PropertyButtonListener listener) {
        buttonListeners.removeElement(listener);
    }

    /** If -1 -- not execution mode */
    public void setNextExecutedRow(int row) {
        nextExecutedRow = row;
    }

    /** If -1 -- not execution mode */
    public int getNextExecutedRow() {
        return nextExecutedRow;
    }

    /** If -1 -- no errors */
    public void setErrorRow(int row) {
        errorRow = row;
    }

    /** If -1 -- no errors */
    public int getErrorRow() {
        return errorRow;
    }

    /** If -1 -- no exec errors */
    public void setExecutionErrorRow(int row) {
        executionErrorRow = row;
    }

    /** If -1 -- no exec errors */
    public int getExecutionErrorRow() {
        return executionErrorRow;
    }

    public SVFExecutionResult getExecutionResult(int i) {
        return results[i];
    }

    public void setExecutionResult(int i, SVFExecutionResult r) {
        results[i] = r;
    }

    public void clearExecutionResults() {
        for (int i = 0; i < results.length; i++) results[i] = null;
    }

    protected void removeAll() {
        setRowCount(0);
    }

    protected void firePropertyButtonEvent(int row, Object value) {
        for (int i = 0; i < buttonListeners.size(); i++) buttonListeners.elementAt(i).buttonClicked(row, value);
    }
}
