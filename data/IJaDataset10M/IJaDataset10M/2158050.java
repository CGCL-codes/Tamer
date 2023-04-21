package co.edu.unal.ungrid.expert.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JPanel;
import co.edu.unal.ungrid.image.Histogram;

public class HistogramView extends JPanel {

    public static final long serialVersionUID = 200609220000001L;

    public HistogramView(Histogram<Integer> hist) {
        m_hist = hist;
        m_clr = Color.GRAY;
    }

    public void setColor(Color clr) {
        m_clr = clr;
    }

    private void drawGrid(Graphics g) {
        g.setColor(new Color(216, 216, 216));
        Rectangle r = g.getClipBounds();
        float dx = (float) r.width / 10;
        float dy = (float) r.height / 10;
        for (int i = 1; i < 10; i++) {
            int x = (int) (i * dx);
            int y = (int) (i * dy);
            g.drawLine(r.x, r.y + y, r.x + r.width, r.y + y);
            g.drawLine(r.x + x, r.y, r.x + x, r.y + r.height);
        }
        g.drawRect(r.x, r.y, r.width - 1, r.height - 1);
    }

    private void drawHistogram(Graphics g) {
        g.setColor(m_clr);
        Rectangle r = g.getClipBounds();
        double[] data = m_hist.getData();
        double dx = (double) r.width / data.length;
        double dy = (double) r.height / m_hist.getMaximum();
        for (int i = 0; i < data.length; i++) {
            int xi = r.x + (int) (i * dx) + 1;
            int xe = r.x + (int) ((i + 1) * dx) + 1;
            int yh = (int) (dy * data[i]);
            int yi = r.y + (r.height - yh) - 1;
            g.fillRect(xi, yi, xe - xi, yh);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        Rectangle r = getBounds();
        g.setClip(10, 10, r.width - 20, r.height - 20);
        drawGrid(g);
        drawHistogram(g);
    }

    /**
	 * @uml.property name="m_hist"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    private Histogram<Integer> m_hist;

    /**
	 * @uml.property name="m_clr"
	 */
    private Color m_clr;
}
