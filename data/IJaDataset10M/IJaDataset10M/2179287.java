package jset.view.testsuitemanager;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import jset.view.cg.render.CoverageCellStyles;

public class CoverageTableCellRenderer extends JLabel implements TableCellRenderer {

    private double coverage = -1d;

    private static CoverageTableCellRenderer instance;

    public static synchronized CoverageTableCellRenderer getInstance() {
        if (instance == null) {
            instance = new CoverageTableCellRenderer();
        }
        return instance;
    }

    private CoverageTableCellRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object coverage, boolean isSelected, boolean hasFocus, int row, int column) {
        this.coverage = (Double) coverage;
        if (this.coverage >= 0d) {
            setToolTipText("Coverage value: " + ((int) (100 * this.coverage)) + "%");
        } else {
            setToolTipText("N/A");
            setText("N/A");
            setHorizontalAlignment(JLabel.CENTER);
        }
        return this;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (coverage >= 0d) {
            int width = getWidth();
            int height = getHeight();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(CoverageCellStyles.COVERAGE_NOT_COVERED_COLOR);
            g.fillRoundRect(1, 1, width - 2, height - 2, 4, 4);
            g.setColor(CoverageCellStyles.COVERAGE_INCLUSIVE_COVERED_COLOR);
            g.fillRoundRect(1, 1, (int) (coverage * width) - 2, height - 2, 4, 4);
        }
    }
}
