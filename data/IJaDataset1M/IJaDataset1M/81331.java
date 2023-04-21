package org.mitre.rt.client.ui.cchecks;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;
import org.mitre.rt.client.util.GlobalUITools;
import org.mitre.rt.rtclient.ValueType;

/**
 *
 * @author BAKERJ
 */
public class ValueTypeTextRenderer extends JTextArea implements TableCellRenderer, TableCellEditor {

    private static final Logger logger = Logger.getLogger(ValueTypeTextRenderer.class.getPackage().getName());

    protected ChangeEvent changeEvent = new ChangeEvent(this);

    private String originalText = null;

    private ValueType valueRef = null;

    private int column;

    private JTable table;

    private static boolean inEditMode = false;

    public ValueTypeTextRenderer(JTable table) {
        this.setFont(GlobalUITools.FONT);
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.initListeners();
        this.table = table;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object item, boolean isSelected, boolean hasFocus, int row, int column) {
        ValueType value = (ValueType) item;
        column = table.convertColumnIndexToModel(column);
        if (column == ValueTypeTableModel.SELECTOR) {
            this.setText(value.getSelector());
        } else {
            List<String> valueList = value.getItemList();
            String text = "";
            if (valueList != null) {
                for (String val : valueList) {
                    text += val + "  ";
                }
            }
            this.setText(text);
        }
        setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
        GlobalUITools.setupTableRendererUI(this, table, row, column, isSelected, true);
        return this;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object item, boolean isSelected, int row, int column) {
        valueRef = (ValueType) item;
        this.column = table.convertColumnIndexToModel(column);
        if (this.column == ValueTypeTableModel.SELECTOR) {
            this.originalText = valueRef.getSelector();
            this.setText(valueRef.getSelector());
        } else {
            List<String> valueList = valueRef.getItemList();
            if (!valueList.isEmpty()) {
                this.setText(valueList.get(0));
            } else {
                this.setText("");
            }
        }
        this.setBackground(GlobalUITools.REQUIRED_ITEM_COLOR_BG);
        inEditMode = true;
        return this;
    }

    private void initListeners() {
        this.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                char typed = e.getKeyChar();
                if (typed == '\n') {
                    String str1 = getText().substring(0, getText().indexOf(typed));
                    String str2 = getText().substring(getText().indexOf(typed) + 1, getText().length());
                    setText(str1 + str2);
                    fireEditingStopped();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        this.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public void addCellEditorListener(CellEditorListener listener) {
        listenerList.add(CellEditorListener.class, listener);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener listener) {
        listenerList.remove(CellEditorListener.class, listener);
    }

    protected void fireEditingStopped() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingStopped(changeEvent);
            }
        }
        String currentText = (String) this.getCellEditorValue();
        if (column == ValueTypeTableModel.VALUE) {
            valueRef.setItemArray(new String[] { currentText });
            this.firePropertyChange("value_edited", this.getOriginalText(), currentText);
        } else {
            if (!currentText.equals("(Default)")) valueRef.setSelector(currentText);
            this.firePropertyChange("selector_edited", this.getOriginalText(), currentText);
        }
    }

    protected void fireEditingCanceled() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingCanceled(changeEvent);
            }
        }
        String currentText = (String) this.getCellEditorValue();
        if (column == ValueTypeTableModel.VALUE) this.firePropertyChange("value_edited", this.getOriginalText(), currentText); else this.firePropertyChange("selector_edited", this.getOriginalText(), currentText);
    }

    @Override
    public Object getCellEditorValue() {
        return this.getText();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) anEvent;
            if (mouseEvent.getButton() == MouseEvent.BUTTON1 && mouseEvent.getClickCount() == 2) {
                return true;
            }
        }
        if (anEvent == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        if (inEditMode) {
            inEditMode = false;
            fireEditingStopped();
        }
        return true;
    }

    @Override
    public void cancelCellEditing() {
        if (inEditMode) {
            inEditMode = false;
            fireEditingCanceled();
        }
    }

    public String getOriginalText() {
        return originalText;
    }
}
