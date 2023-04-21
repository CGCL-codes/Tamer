package de.bielefeld.uni.cebitec.repfish.gui;

import de.bielefeld.uni.cebitec.repfish.acefile.Contig;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author Patrick Schwientek (pschwien at cebitec.uni-bielefeld.de)
 */
public class ContigRepresentation extends JPanel {

    public static final int CONTIG_WIDTH = 100;

    public static final int CONTIG_HEIGHT = 60;

    public static final Font contigFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);

    private final int CONTIG_BAR_SEPARATOR_WIDTH = 5;

    private final int CONTIG_BOTTOM_PADDING = 15;

    private final int CONTIG_TOP_PADDING = 15;

    private float maxValue;

    private int maxBarHeight;

    private int barWidth;

    private float overrepresentation;

    private boolean selected;

    private Contig contig;

    private int index;

    public ContigRepresentation(int x, int y, int globalMedian, int index, Contig contig) {
        super(null);
        this.contig = contig;
        this.index = index;
        overrepresentation = Math.round((contig.getMedianCoverage() * 100) / (float) globalMedian) / 100f;
        setBounds(x, y, CONTIG_WIDTH, CONTIG_HEIGHT);
        maxValue = Math.max(contig.getLeftCoverage(), Math.max(contig.getMedianCoverage(), contig.getRightCoverage()));
        maxBarHeight = CONTIG_HEIGHT - (CONTIG_TOP_PADDING + CONTIG_BOTTOM_PADDING);
        barWidth = (CONTIG_WIDTH - (2 * CONTIG_BAR_SEPARATOR_WIDTH)) / 3;
        repaint();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (selected) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.LIGHT_GRAY);
        }
        g2d.fillRect(0, 0, getWidth(), getHeight());
        int value = (int) Math.max(1, (maxBarHeight / maxValue) * contig.getLeftCoverage());
        if (contig.getLeftCoverage() > 10) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.GREEN.darker());
        }
        g2d.fillRect(0, CONTIG_HEIGHT - CONTIG_BOTTOM_PADDING - value, barWidth, value);
        if (overrepresentation < 1.25) {
            g2d.setColor(Color.GREEN.darker());
        } else if (overrepresentation < 1.75) {
            g2d.setColor(Color.ORANGE.darker());
        } else {
            g2d.setColor(Color.RED.darker());
        }
        value = (int) Math.max(1, (maxBarHeight / maxValue) * contig.getMedianCoverage());
        g2d.fillRect(barWidth + CONTIG_BAR_SEPARATOR_WIDTH, CONTIG_HEIGHT - CONTIG_BOTTOM_PADDING - value, barWidth, value);
        value = (int) Math.max(1, (maxBarHeight / maxValue) * contig.getRightCoverage());
        if (contig.getRightCoverage() > 10) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.GREEN.darker());
        }
        g2d.fillRect(2 * (barWidth + CONTIG_BAR_SEPARATOR_WIDTH), CONTIG_HEIGHT - CONTIG_BOTTOM_PADDING - value, barWidth, value);
        g2d.setFont(contigFont);
        int len = (int) g2d.getFontMetrics().getStringBounds(overrepresentation + "x", g2d).getWidth();
        g2d.drawString(overrepresentation + "x", CONTIG_WIDTH - (len + 2), 11);
        g2d.setColor(Color.BLACK);
        g2d.drawString(contig.getName(), 1, 11);
        len = (int) g2d.getFontMetrics().getStringBounds(contig.getLeftCoverage() + "", g2d).getWidth();
        g2d.drawString(contig.getLeftCoverage() + "", barWidth / 2 - len / 2, CONTIG_HEIGHT - 3);
        len = (int) g2d.getFontMetrics().getStringBounds(contig.getMedianCoverage() + "", g2d).getWidth();
        g2d.drawString(contig.getMedianCoverage() + "", barWidth + CONTIG_BAR_SEPARATOR_WIDTH + barWidth / 2 - len / 2, CONTIG_HEIGHT - 3);
        len = (int) g2d.getFontMetrics().getStringBounds(contig.getRightCoverage() + "", g2d).getWidth();
        g2d.drawString(contig.getRightCoverage() + "", 2 * (barWidth + CONTIG_BAR_SEPARATOR_WIDTH) + barWidth / 2 - len / 2, CONTIG_HEIGHT - 3);
    }
}
