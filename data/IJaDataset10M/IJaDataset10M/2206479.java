package org.vikamine.gui.discretization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.vikamine.gui.discretization.CutpointsTable.CutpointsTableModel;
import org.vikamine.gui.util.GuiUtils;

/**
 * The main panel with the bars and the grid. bars are added as components of
 * type ContinuousDistributionBarComponent
 * 
 * @author lemmerich
 * @date 04/2009
 */
class ContinuousDistributionPanel extends JPanel {

    /** generated */
    private static final long serialVersionUID = -8964070043172253274L;

    /** the maximum height of a bar */
    static final int BAR_HEIGHT = 350;

    /** how much space is over the bar */
    static final int TOP_SPACE = 50;

    private static final int NUMBER_OF_HORIZONTAL_LINES = 20;

    static final int LEFT_BORDER = 40;

    private final NumberFormat ABSOLUTE_FORMATTER = new DecimalFormat("#");

    public ContinuousDistributionPanel(CutpointsTable.CutpointsTableModel tableModel, boolean absolute) {
        super();
        this.tableModel = tableModel;
        this.absolute = absolute;
        this.setBackground(Color.white);
        updatePanel();
    }

    CutpointsTableModel tableModel;

    boolean absolute;

    private String getLineDescription(int i, boolean absolute) {
        i = NUMBER_OF_HORIZONTAL_LINES - i - 1;
        BarInformation firstBarInformation = tableModel.getBarInformations().get(0);
        if (absolute) {
            double max = firstBarInformation.getMaxSize();
            return ABSOLUTE_FORMATTER.format(((double) i / (double) NUMBER_OF_HORIZONTAL_LINES) * max);
        } else {
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Insets insets = getInsets();
        g.setColor(Color.LIGHT_GRAY);
        int fontHeight = g.getFontMetrics().getHeight();
        for (int i = 0; i < NUMBER_OF_HORIZONTAL_LINES; i++) {
            int heightOfLine = (int) Math.round(((i + 1) * BAR_HEIGHT) / (double) NUMBER_OF_HORIZONTAL_LINES);
            g.setColor(Color.black);
            String lineDescription = getLineDescription(i, absolute);
            g.drawString(lineDescription, 5, insets.top + TOP_SPACE + heightOfLine + (fontHeight / 4));
            if (i != NUMBER_OF_HORIZONTAL_LINES - 1) {
                g.setColor(Color.DARK_GRAY);
            }
            g.drawLine(insets.left + LEFT_BORDER, insets.top + TOP_SPACE + heightOfLine, getWidth() - insets.right, insets.top + TOP_SPACE + heightOfLine);
        }
        g.setColor(Color.black);
        String xAxisDescription = tableModel.getAttribute().getDescription();
        g.drawString(xAxisDescription, (getWidth() - SwingUtilities.computeStringWidth(g.getFontMetrics(), xAxisDescription)) / 2, TOP_SPACE + insets.top + BAR_HEIGHT + 20);
        g.drawLine(insets.left + LEFT_BORDER, insets.top + TOP_SPACE, insets.left + LEFT_BORDER, insets.top + BAR_HEIGHT + TOP_SPACE);
    }

    public void updatePanel() {
        removeAll();
        int hgap = 1;
        ((FlowLayout) getLayout()).setHgap(hgap);
        ((FlowLayout) getLayout()).setVgap(TOP_SPACE);
        int numberOfBars = tableModel.getBarInformations().size();
        GuiUtils.addGap(this, ContinuousDistributionPanel.LEFT_BORDER);
        for (BarInformation bi : tableModel.getBarInformations()) {
            BarComponent bar = new BarComponent(bi);
            bar.setParentBorders(ContinuousDistributionPanel.LEFT_BORDER + (numberOfBars * 2));
            bar.setPreferredBarHeight(BAR_HEIGHT);
            add(bar);
        }
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getParent().getWidth(), getParent().getHeight());
    }
}
