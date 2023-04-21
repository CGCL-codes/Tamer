package com.wozgonon.console;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import com.wozgonon.docustrate.Description;
import com.wozgonon.docustrate.IHasDescription;
import com.wozgonon.math.Constants;
import com.wozgonon.math.CorrelationMatrix;
import com.wozgonon.math.IMatrix;
import com.wozgonon.math.IMatrixListener;
import com.wozgonon.swing.FrameWithBars;
import com.wozgonon.swing.Icons;
import com.wozgonon.swing.ScrollPaneWithTable;
import com.wozgonon.swing.TablePopupMenu;

public class CorrelationBox extends ScrollPaneWithTable implements IMatrixListener {

    private static final long serialVersionUID = 1L;

    public static final Description description = new Description("Correlations between use cases", 'r', Icons.newLetterIcon(Constants.RHO), "Please select table cells for which you would like to displays correlations.");

    private final CorrelationMatrix correlationMatrix = new CorrelationMatrix();

    private final IMatrix<?, ?> model;

    public CorrelationBox(final IMatrix<?, ?> model, FrameWithBars frame) {
        this.model = model;
        super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        final JTable table = super.getTable();
        final TablePopupMenu menu = new TablePopupMenu(frame);
        menu.setTable(table);
        table.addMouseListener(menu);
        table.setCellSelectionEnabled(true);
    }

    @Override
    public void matrixChanged() {
        correlationMatrix.recalculate(model);
        final JTable table = super.getTable();
        table.repaint();
        table.setDefaultRenderer(Object.class, new CorrelationCellRenderer());
        table.setModel(new AbstractTableModel() {

            private static final long serialVersionUID = 1L;

            public int getRowCount() {
                return correlationMatrix.getCorrelations().getRow().size() + 2;
            }

            public int getColumnCount() {
                return correlationMatrix.getCorrelations().getColumn().size() + 2;
            }

            public Object getValueAt(int row, int column) {
                if (row == 0) {
                    if (column == 0) {
                        return "";
                    } else if (column == 1) {
                        return "";
                    } else {
                        return "";
                    }
                } else if (row == 1) {
                    if (column == 0) {
                        return "";
                    } else if (column == 1) {
                        return Constants.SIGMA;
                    } else {
                        return String.format("%2.2f", correlationMatrix.getSd()[column - 2]);
                    }
                } else {
                    if (column == 0) {
                        return "";
                    } else if (column == 1) {
                        return String.format("%2.2f", correlationMatrix.getSd()[row - 2]);
                    } else if (row <= column) {
                        final float value = (float) (correlationMatrix.getCorrelations().getAt((short) (row - 2), (short) (column - 2)) / 100.0);
                        return value;
                    } else {
                        return "";
                    }
                }
            }
        });
    }
}

class CorrelationCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final Color[] shades = new Color[] { Color.red, Color.red.darker(), Color.orange, Color.orange.darker(), Color.gray, Color.lightGray, Color.yellow.darker(), Color.yellow, Color.green.darker(), Color.green };
        if (value instanceof Float) {
            final float number = ((Float) value) * shades.length;
            final int index = ((int) Math.rint(number) + shades.length) / 2 - 1;
            super.setBackground(shades[index]);
            super.setIcon(null);
            value = String.format("%2.2f", value);
        } else if (value instanceof IHasDescription) {
            final IHasDescription type = (IHasDescription) value;
            super.setIcon(type.getDescription().getIcon());
            super.setBackground(Color.white);
            super.setForeground(Color.black);
        } else {
            super.setIcon(null);
            super.setBackground(Color.white);
            super.setForeground(Color.black);
        }
        final Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        return renderer;
    }
}
