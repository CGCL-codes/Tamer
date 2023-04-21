package ircam.jmax.editors.bpf;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;

/**
* The panel containing the JTable representation of an Explode.
 * The editing of a generic entry is handled by a DefaultCellEditor object.
 * See the setUpIntegerEditor method in this class for details. */
class BpfTablePanel extends JPanel implements ListSelectionListener {

    BpfTablePanel(BpfTableModel model, BpfGraphicContext gc) {
        this.tmodel = model;
        this.gc = gc;
        table = new JTable(tmodel);
        table.setPreferredScrollableViewportSize(new Dimension(BpfWindow.DEFAULT_WIDTH, BpfWindow.DEFAULT_HEIGHT - 55));
        table.setRowHeight(17);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(BpfWindow.DEFAULT_WIDTH, BpfWindow.DEFAULT_HEIGHT));
        setUpEditors();
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, scrollPane);
        table.setSelectionModel(gc.getSelection());
        gc.getSelection().addListSelectionListener(this);
        BpfDataModel bpfModel = tmodel.getDataModel();
        bpfModel.addBpfListener(new BpfDataListener() {

            public void pointChanged(int oldIndex, int newIndex, float newTime, float newValue) {
                repaint();
            }

            public void pointsChanged() {
                repaint();
            }

            public void pointAdded(int index) {
                table.revalidate();
            }

            public void pointsDeleted(int index, int size) {
                table.revalidate();
            }

            public void cleared() {
                table.revalidate();
            }

            public void nameChanged(String name) {
            }
        });
    }

    public void valueChanged(ListSelectionEvent e) {
        Rectangle rect;
        ListSelectionModel selection = table.getSelectionModel();
        if (selection.isSelectionEmpty() || selection.getValueIsAdjusting()) return;
        int minIndex = selection.getMinSelectionIndex();
        int maxIndex = selection.getMaxSelectionIndex();
        rect = table.getCellRect(minIndex, 0, true);
        if (minIndex != maxIndex) rect = rect.union(table.getCellRect(maxIndex, 0, true));
        table.scrollRectToVisible(rect);
    }

    private void setUpEditors() {
        final JTextField numberField = new JTextField();
        numberField.setHorizontalAlignment(JTextField.RIGHT);
        DefaultCellEditor floatEditor = new DefaultCellEditor(numberField) {

            public Object getCellEditorValue() {
                try {
                    return Float.valueOf(numberField.getText());
                } catch (NumberFormatException exc) {
                    Toolkit.getDefaultToolkit().beep();
                    System.err.println("Error:  invalid number format!");
                    return null;
                }
            }
        };
        table.setDefaultEditor(Float.class, floatEditor);
    }

    public class CellEditorArea extends DefaultCellEditor {

        JTextArea area = new JTextArea();

        JScrollPane scroll;

        public CellEditorArea(JTextField field) {
            super(field);
            scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            area.setText("" + table.getValueAt(row, column));
            return scroll;
        }

        public Object getCellEditorValue() {
            return area.getText();
        }
    }

    BpfTableModel tmodel;

    BpfGraphicContext gc;

    JScrollPane scrollPane;

    JTable table;
}
