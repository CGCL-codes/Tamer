package corina.editor;

import corina.Sample;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

public class SlashedIfNullRenderer extends DefaultTableCellRenderer {

    public SlashedIfNullRenderer(Sample sample, TableModel model) {
        this.sample = sample;
        this.model = model;
    }

    private Sample sample;

    private TableModel model;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (!sample.isEditable() && value == null) return slasher;
        if (sample.isEditable() && !model.isCellEditable(row, column)) return slasher;
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    private static final Color DARK = new Color(0.7333f, 0.7765f, 0.8431f);

    private static final Color LIGHT = new Color(0.8196f, 0.8510f, 0.9216f);

    private static final int THIN = 2;

    private static final int THICK = 5;

    private static JComponent slasher = new JComponent() {

        public void paintComponent(Graphics g) {
            setOpaque(true);
            int w = getWidth(), h = getHeight();
            g.setColor(LIGHT);
            g.fillRect(0, 0, w, h);
            g.setColor(DARK);
            ((Graphics2D) g).setStroke(new BasicStroke(THIN - 1f));
            for (int x = 0; x < w + h; x += THIN + THICK) g.drawLine(x, 0, x - h, h);
        }
    };
}
