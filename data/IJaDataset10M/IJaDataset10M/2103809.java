package com.thyante.thelibrarian.components;

import java.util.SortedSet;
import java.util.TreeSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import com.thyante.thelibrarian.util.I18n;

/**
 * @author Matthias-M. Christen
 *
 */
public class BarChart extends Canvas implements PaintListener {

    /**
	 * Height of a bar
	 */
    public static final int BAR_HEIGHT = 15;

    /**
	 * Gap between bars (in pixels)
	 */
    public static final int VERTICAL_GAP = 4;

    /**
	 * Vertical margin at the bottom and at the top
	 */
    public static final int VERTICAL_MARGIN = 10;

    protected class Bar implements Comparable<Bar> {

        private String m_strLabel;

        private double m_fValue;

        public Bar(String strLabel, double fValue) {
            m_strLabel = strLabel;
            m_fValue = fValue;
            if (m_strLabel == null || "".equals(m_strLabel)) m_strLabel = "[" + I18n.xl8("No value") + "]";
        }

        public String getLabel() {
            return m_strLabel;
        }

        public double getValue() {
            return m_fValue;
        }

        public void drawBar(GC gc, int x, int y, int nWidth, int nHeight) {
            int nHue = (int) (360 * m_fValue / m_fMaxBarValue);
            int nBarWidth = (int) (nWidth * m_fValue / m_fMaxBarValue);
            RGB[] rgRGBs = new RGB[] { new RGB(nHue, 0.7f, 0.7f), new RGB(nHue, 0.3f, 1.0f), new RGB(nHue, 1.0f, 0.3f) };
            Color[] rgColors = new Color[rgRGBs.length];
            for (int i = 0; i < rgRGBs.length; i++) rgColors[i] = new Color(gc.getDevice(), rgRGBs[i]);
            gc.setForeground(rgColors[0]);
            gc.setBackground(rgColors[1]);
            gc.fillGradientRectangle(x + nHeight / 3, y, nBarWidth, nHeight / 3, true);
            gc.setForeground(rgColors[1]);
            gc.setBackground(rgColors[2]);
            gc.fillGradientRectangle(x + nHeight / 3, y + nHeight / 3, nBarWidth, nHeight - nHeight / 3, true);
            gc.setBackground(rgColors[1]);
            gc.fillOval(x, y, (2 * nHeight) / 3, nHeight);
            int nStartX = x + nHeight / 3 + nBarWidth;
            PathData pathData = new PathData();
            pathData.points = new float[] { nStartX - nHeight / 2, y, nStartX, y, nStartX, y + nHeight, nStartX - nHeight / 2, y + nHeight, nStartX, y + (2 * nHeight) / 3, nStartX, y + nHeight / 3, nStartX - nHeight / 2, y };
            pathData.types = new byte[] { SWT.PATH_MOVE_TO, SWT.PATH_LINE_TO, SWT.PATH_LINE_TO, SWT.PATH_LINE_TO, SWT.PATH_CUBIC_TO, SWT.PATH_CLOSE };
            Path path = new Path(gc.getDevice(), pathData);
            gc.setBackground(BarChart.this.getBackground());
            gc.fillPath(path);
            StringBuffer sb = new StringBuffer(m_strLabel);
            sb.append(" (");
            if (m_fValue == (int) m_fValue) sb.append((int) m_fValue); else sb.append(m_fValue);
            sb.append(')');
            gc.setForeground(BarChart.this.getForeground());
            gc.drawString(sb.toString(), x + nHeight / 3, y + nHeight, true);
            path.dispose();
            for (int i = 0; i < rgRGBs.length; i++) rgColors[i].dispose();
        }

        public int compareTo(Bar bar) {
            if (m_fValue == bar.getValue()) return m_strLabel.compareToIgnoreCase(bar.getLabel());
            return m_fValue < bar.getValue() ? 1 : -1;
        }
    }

    /**
	 * The set of bars displayed in the chart
	 */
    protected SortedSet<Bar> m_setBars;

    /**
	 * The maximum bar value
	 */
    protected double m_fMaxBarValue;

    public BarChart(Composite cmpParent, int nStyle) {
        super(cmpParent, nStyle);
        m_setBars = new TreeSet<Bar>();
        m_fMaxBarValue = 0;
        addPaintListener(this);
    }

    public void clear() {
        m_setBars.clear();
        m_fMaxBarValue = 0;
        redraw();
    }

    public void addBar(String strLabel, double fValue) {
        m_setBars.add(new Bar(strLabel, fValue));
        m_fMaxBarValue = Math.max(m_fMaxBarValue, fValue);
    }

    protected Font getBarFont(GC gc) {
        FontData[] rgFontData = getFont().getFontData();
        for (FontData fd : rgFontData) fd.setHeight(Math.max(6, fd.getHeight() / 2));
        Font font = new Font(gc.getDevice(), rgFontData);
        gc.setFont(font);
        return font;
    }

    @Override
    public Point computeSize(int nHintWidth, int nHintHeight, boolean bChanged) {
        GC gc = new GC(getDisplay());
        Font font = getBarFont(gc);
        int nBarHeight = BAR_HEIGHT + gc.textExtent("Xyz").y + VERTICAL_GAP;
        font.dispose();
        gc.dispose();
        return new Point(nHintWidth == SWT.DEFAULT ? 0 : nHintWidth, VERTICAL_MARGIN + m_setBars.size() * nBarHeight + VERTICAL_MARGIN);
    }

    public void paintControl(PaintEvent e) {
        e.gc.setAdvanced(true);
        e.gc.setAntialias(SWT.ON);
        Rectangle r = getClientArea();
        int y = VERTICAL_MARGIN;
        Font font = getBarFont(e.gc);
        int nTextHeight = e.gc.textExtent("Xyz").y;
        for (Bar bar : m_setBars) {
            bar.drawBar(e.gc, 0, y, r.width - 20, BAR_HEIGHT);
            y += BAR_HEIGHT + nTextHeight + VERTICAL_GAP;
        }
        e.gc.setFont(getFont());
        font.dispose();
    }
}
