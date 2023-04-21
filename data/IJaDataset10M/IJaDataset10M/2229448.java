package net.deytan.tagger.ui.local;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import net.deytan.tagger.ui.Cell;
import net.deytan.tagger.ui.UIUtils;

public class TextCellRenderer extends AbstractTableCellRenderer {

    private static final Border focusedBorder = new Border(Color.blue, 1, 5, 0, 0, 0);

    private static final Border noBorder = new Border(5, 0, 0, 0);

    private final JLabel label;

    public static TextCellRenderer center() {
        return new TextCellRenderer(SwingConstants.CENTER);
    }

    public static TextCellRenderer left() {
        return new TextCellRenderer(SwingConstants.LEFT);
    }

    public static TextCellRenderer right() {
        return new TextCellRenderer(SwingConstants.RIGHT);
    }

    protected TextCellRenderer(final int horizontalAlignment) {
        super();
        this.label = new JLabel();
        this.label.setHorizontalAlignment(horizontalAlignment);
        this.setLayout(new GridBagLayout());
        this.add(this.label, UIUtils.newGridBagConstraints("gridx", 0, "gridy", 0, "fill", GridBagConstraints.BOTH, "weightx", 1.0, "weighty", 1.0));
    }

    @Override
    public java.awt.Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int rowIndex, final int columnIndex) {
        final Cell cell = ((TableModel) table.getModel()).getCellAt(rowIndex, columnIndex);
        this.label.setFont(table.getFont());
        if (isSelected) {
            if (hasFocus) {
                this.setForeground(UIManager.getColor("Table.focusCellForeground"));
                this.setBackground(UIManager.getColor("Table.focusCellBackground"));
                this.setBorder(TextCellRenderer.focusedBorder);
            } else {
                this.setForeground(table.getSelectionForeground());
                this.setBackground(table.getSelectionBackground());
                this.setBorder(TextCellRenderer.noBorder);
            }
        } else {
            this.setForeground(table.getForeground());
            this.setBackground(cell.getColor() != null ? cell.getColor() : table.getBackground());
            this.setBorder(TextCellRenderer.noBorder);
        }
        this.setText(cell.getText());
        return this;
    }

    protected void setText(final Object text) {
        this.label.setText(text != null ? text.toString() : "");
    }
}
