package co.edu.unal.ungrid.test.worker.tiling;

import org.jdom.Element;
import co.edu.unal.ungrid.image.AbstractImage;
import co.edu.unal.ungrid.image.AbstractPlane;
import co.edu.unal.ungrid.image.processing.ImageProcessor;
import co.edu.unal.ungrid.xml.XmlUtil;

public class BlurFilter<Plane extends AbstractPlane> implements ImageProcessor<Plane> {

    public static final long serialVersionUID = 20060918000010L;

    public BlurFilter(int radius) {
        setRadius(radius);
    }

    public void config(Element root) {
        assert root != null;
        setRadius(XmlUtil.getInt(root, "radius"));
    }

    public boolean isValid() {
        return (m_radius > 0.0);
    }

    private int process(int[] ia, int length) {
        int b = 0;
        int g = 0;
        int r = 0;
        int a = 0;
        for (int i = 0; i < length; i++) {
            b += (ia[i] & 0x000000FF);
            g += (ia[i] & 0x0000FF00) >> 8;
            r += (ia[i] & 0x00FF0000) >> 16;
            a += (ia[i] & 0xFF000000) >> 24;
        }
        b /= length;
        g /= length;
        r /= length;
        a /= length;
        return a | (r << 16) | (g << 8) | b;
    }

    public AbstractImage<Plane> filter(final AbstractImage<Plane> img) {
        assert img != null;
        int iw = img.getWidth();
        int ih = img.getHeight();
        int[] in = img.intArray();
        int[] out = new int[in.length];
        int[] buf = new int[(2 * m_radius + 1) * (2 * m_radius + 1)];
        for (int y = 0; y < ih; y++) {
            int row = y * iw;
            for (int x = 0; x < iw; x++) {
                int miny = Math.max(0, y - m_radius);
                int maxy = Math.min(y + m_radius, ih);
                int minx = Math.max(0, x - m_radius);
                int maxx = Math.min(x + m_radius, iw);
                int n = 0;
                for (int yy = miny, yyy = 0; yy < maxy; yy++, yyy++) {
                    int brow = yyy * (maxy - miny);
                    for (int xx = minx, xxx = 0; xx < maxx; xx++, xxx++) {
                        buf[brow + xxx] = in[yy * iw + xx];
                        n++;
                    }
                }
                out[row + x] = process(buf, n);
            }
        }
        AbstractImage<Plane> ai = img.newImage();
        ai.fromArray(out);
        return ai;
    }

    public int getRadius() {
        return m_radius;
    }

    public void setRadius(int radius) {
        assert radius > 0;
        m_radius = radius;
    }

    private int m_radius;
}
