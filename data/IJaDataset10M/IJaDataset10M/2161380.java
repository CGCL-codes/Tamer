package com.greentea.relaxation.jnmf.gui.components.project.network.optimization;

import com.greentea.relaxation.jnmf.gui.components.paramstable.CurrentParametersHolder;
import com.greentea.relaxation.jnmf.gui.components.paramstable.IParametersChangedListener;
import com.greentea.relaxation.jnmf.gui.components.paramstable.ParametersTable;
import com.greentea.relaxation.jnmf.parameters.Parameter;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Created by IntelliJ IDEA. User: GreenTea Date: 14.02.2009 Time: 20:26:13 To change this template
 * use File | Settings | File Templates.
 */
class ParametersOptimizationCellEditor implements TableCellEditor, CellEditorListener, KeyListener {

    private DefaultTableModel model;

    private CurrentParametersHolder currentParametersHolder;

    private Color normalBackgroundColor = Color.WHITE;

    private Color wrongValueBackgroundColor = Color.WHITE;

    private DefaultCellEditor textFieldEditor = new DefaultCellEditor(new JTextField());

    private DefaultCellEditor defaultTextFieldEditor = new DefaultCellEditor(new JTextField());

    private Object lastValue;

    private DefaultCellEditor currentCellEditor;

    private Parameter currentParameter;

    private boolean editable = true;

    private boolean turnOfEditingListener;

    private int lastEditedColumn;

    private int lastEditedRow;

    private IParametersChangedListener parametersChangedListener;

    public ParametersOptimizationCellEditor(DefaultTableModel model, CurrentParametersHolder currentParametersHolder) {
        this.model = model;
        this.currentParametersHolder = currentParametersHolder;
        this.textFieldEditor.addCellEditorListener(this);
        this.defaultTextFieldEditor.addCellEditorListener(this);
        this.textFieldEditor.getComponent().addKeyListener(this);
        updateEditorsBackgroundColor();
    }

    public ParametersOptimizationCellEditor(DefaultTableModel model, CurrentParametersHolder currentParametersHolder, boolean editable) {
        this(model, currentParametersHolder);
        this.editable = editable;
    }

    public ParametersOptimizationCellEditor(DefaultTableModel model, CurrentParametersHolder currentParametersHolder, boolean editable, Color normalBackgroundColor, Color wrongValueBackgroundColor) {
        this(model, currentParametersHolder, editable);
        setNormalBackgroundColor(normalBackgroundColor);
        setWrongValueBackgroundColor(wrongValueBackgroundColor);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.lastEditedRow = row;
        this.lastEditedColumn = column;
        this.lastValue = value;
        currentParameter = currentParametersHolder.getCurrentParameters().get(row);
        selectCurrentCellEditor(table, column);
        return currentCellEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    private void selectCurrentCellEditor(JTable table, int column) {
        if (table.getColumnName(column).equals(ParametersOptimizationComponent.getValuesIntervalColumnName())) {
            currentCellEditor = textFieldEditor;
        } else if (table.getColumnName(column).equals(ParametersTable.getPossibleValuesColumnName())) {
            currentCellEditor = defaultTextFieldEditor;
        } else {
            throw new IllegalArgumentException("column is not valid");
        }
    }

    public Color getNormalBackgroundColor() {
        return normalBackgroundColor;
    }

    public void setNormalBackgroundColor(Color normalBackgroundColor) {
        this.normalBackgroundColor = normalBackgroundColor;
        updateEditorsBackgroundColor();
    }

    public Color getWrongValueBackgroundColor() {
        return wrongValueBackgroundColor;
    }

    public void setWrongValueBackgroundColor(Color wrongValueBackgroundColor) {
        this.wrongValueBackgroundColor = wrongValueBackgroundColor;
    }

    private void updateEditorsBackgroundColor() {
        this.textFieldEditor.getComponent().setBackground(normalBackgroundColor);
    }

    public Object getCellEditorValue() {
        return currentCellEditor.getCellEditorValue();
    }

    public boolean isCellEditable(EventObject anEvent) {
        return (anEvent instanceof MouseEvent) && editable;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return currentCellEditor.shouldSelectCell(anEvent);
    }

    public boolean stopCellEditing() {
        return lastEditedRow >= currentParametersHolder.getCurrentParameters().size() || currentCellEditor.stopCellEditing();
    }

    public void cancelCellEditing() {
        currentCellEditor.cancelCellEditing();
    }

    public void addCellEditorListener(CellEditorListener l) {
        currentCellEditor.addCellEditorListener(l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        currentCellEditor.removeCellEditorListener(l);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void editingStopped(ChangeEvent e) {
        if (turnOfEditingListener) {
            return;
        }
        if (currentCellEditor == defaultTextFieldEditor) {
            model.setValueAt(lastValue, lastEditedRow, lastEditedColumn);
            return;
        }
        Object newValue = currentCellEditor.getCellEditorValue();
        if (newValue instanceof String) {
            String values = (String) newValue;
            if (!currentParameter.isValidSubrange(values)) {
                model.setValueAt(lastValue, lastEditedRow, lastEditedColumn);
            }
        }
        currentCellEditor.getComponent().setBackground(normalBackgroundColor);
    }

    public void editingCanceled(ChangeEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    private void updateCurrentEditorColor(String newValueStr) {
        if (currentCellEditor == defaultTextFieldEditor) {
            return;
        }
        if (currentParameter.isValidSubrange(newValueStr)) {
            currentCellEditor.getComponent().setBackground(normalBackgroundColor);
        } else {
            currentCellEditor.getComponent().setBackground(wrongValueBackgroundColor);
        }
    }

    public void keyReleased(KeyEvent e) {
        updateCurrentEditorColor(((JTextField) e.getComponent()).getText());
    }

    public IParametersChangedListener getParametersChangedListener() {
        return parametersChangedListener;
    }

    public void setParametersChangedListener(IParametersChangedListener parametersChangedListener) {
        this.parametersChangedListener = parametersChangedListener;
    }
}
