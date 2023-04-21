package org.glossitope.container.backgrounds;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.joshy.util.u;
import org.glossitope.container.DesktopBackground;

/**
 *
 * @author joshy
 */
public class NasaBackground extends DesktopBackground {

    private BufferedImage background;

    /** Creates a new instance of NasaBackground */
    public NasaBackground() {
        setBackgroundName("NASA Pic of the Day");
        new Thread(new Runnable() {

            public void run() {
                try {
                    loadPicture();
                } catch (Exception ex) {
                    u.p(ex);
                }
            }
        }).start();
    }

    private void loadPicture() throws IOException {
        String base_url = "http://antwrp.gsfc.nasa.gov/apod/";
        URL url = new URL(base_url);
        u.p("loading the page: " + url);
        Reader input = new InputStreamReader(url.openStream());
        char buf[] = new char[1024];
        StringBuffer page_buffer = new StringBuffer();
        while (true) {
            int n = input.read(buf);
            if (n < 0) {
                break;
            }
            page_buffer.append(buf, 0, n);
        }
        Pattern pattern = Pattern.compile("<a href=\"(.*)\">\\s*<IMG SRC=\".*\"");
        Matcher matcher = pattern.matcher(page_buffer);
        matcher.find();
        String img_url = base_url + matcher.group(1);
        u.p("loading the image: " + img_url);
        background = ImageIO.read(new URL(img_url));
        u.p("image loaded: " + background);
        getDesktopPane().repaint();
    }

    public void paint(Graphics2D g) {
        if (background != null) {
            Dimension src = new Dimension(background.getWidth(), background.getHeight());
            Dimension view = new Dimension(getDesktopPane().getWidth(), getDesktopPane().getHeight());
            double scale = view.getWidth() / src.getWidth();
            double scale2 = view.getHeight() / src.getHeight();
            scale = Math.min(scale, scale2);
            Dimension dst = new Dimension((int) (src.getWidth() * scale), (int) (src.getHeight() * scale));
            g = (Graphics2D) g.create();
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(background, 0, 0, dst.width, dst.height, null);
        }
    }
}
