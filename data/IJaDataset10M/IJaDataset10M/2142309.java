package co.edu.unal.ungrid.services.client.applet.bimler.view;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import co.edu.unal.ungrid.image.RgbPlane;
import co.edu.unal.ungrid.image.util.JointHistogram;

public class JointHistogramView extends JPanel {

    public static final long serialVersionUID = 200609220000001L;

    public JointHistogramView(final JointHistogram<RgbPlane> jh) {
        assert jh != null;
        m_jh = jh;
    }

    private void render() {
        Rectangle r = getBounds();
        r.grow(-10, -10);
        m_img = new BufferedImage(r.width, r.height, BufferedImage.TYPE_INT_RGB);
        double[][] hdata = m_jh.getData();
        float dy = (float) hdata.length / r.height;
        float dx = (float) hdata[0].length / r.width;
        for (int y = 0; y < r.height; y++) {
            int hy = (int) (y * dy);
            for (int x = 0; x < r.width; x++) {
                int hx = (int) (x * dx);
                if (hdata[hy][hx] > 0) {
                    int v = (int) (Math.log(hdata[hy][hx]) * ilog103);
                    m_img.setRGB(x, r.height - y - 1, (v << 16) | (v << 8) | v);
                }
            }
        }
    }

    private void drawHistogram(Graphics g) {
        if (m_img == null) {
            render();
        }
        if (m_img != null) {
            g.drawImage(m_img, 10, 10, null);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        Rectangle r = getBounds();
        g.setClip(10, 10, r.width - 20, r.height - 20);
        drawHistogram(g);
    }

    private JointHistogram<RgbPlane> m_jh;

    private BufferedImage m_img;

    private static double log103 = Math.log(1.03);

    private static double ilog103 = 1.0 / log103;
}
