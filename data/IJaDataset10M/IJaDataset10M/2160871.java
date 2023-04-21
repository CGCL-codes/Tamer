package nu.esox.gui.aspect;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;

public class TableAdapter extends AbstractAdapter implements ListSelectionListener {

    private final JTable m_table;

    private transient boolean m_isUpdating = false;

    public TableAdapter(JTable table, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, Class aspectClass, String aspectName) {
        super(modelOwner, modelClass, getAspectMethodName, setAspectMethodName, aspectClass, aspectName, null, null);
        m_table = table;
        m_table.getSelectionModel().addListSelectionListener(this);
        update();
    }

    protected Object getItemOfRow(JTable t, int row) {
        return null;
    }

    protected int getRowOfItem(JTable t, Object item) {
        return -1;
    }

    public void valueChanged(ListSelectionEvent ev) {
        if (m_isUpdating) return;
        if (m_table.getSelectedRow() != -1) {
            setAspectValue(getItemOfRow(m_table, m_table.convertRowIndexToModel(m_table.getSelectedRow())));
        } else {
            setAspectValue(null);
        }
    }

    protected void update(Object projectedValue) {
        m_isUpdating = true;
        int n = -1;
        if (projectedValue != null) n = getRowOfItem(m_table, projectedValue);
        if (n == -1) {
            m_table.clearSelection();
        } else {
            n = m_table.convertRowIndexToView(n);
            m_table.getSelectionModel().setSelectionInterval(n, n);
        }
        m_isUpdating = false;
    }
}
