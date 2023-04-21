package org.gzigzag.util;

import org.gzigzag.*;
import java.awt.*;
import java.awt.image.*;

/** Try out creating Gabor patterns.
 */
public class TryGabor {

    public static final String rcsid = "$Id: TryGabor.java,v 1.2 2001/10/10 11:47:01 tjl Exp $";

    public static class ImageComponent extends Component {

        Image img;

        int w, h;

        int mult = 2;

        public ImageComponent(Image img, int w, int h) {
            this.img = img;
            this.w = w;
            this.h = h;
        }

        public Dimension getPreferredSize() {
            return new Dimension(mult * w, mult * h);
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public void paint(Graphics g) {
            g.setColor(Color.red);
            g.fillRect(0, 0, w, h);
            for (int x = 0; x < mult; x++) for (int y = 0; y < mult; y++) g.drawImage(img, x * w, y * h, null);
        }
    }

    Frame frame;

    Image[] images = new Image[2];

    TryGabor(String[] argv) {
        frame = new Frame();
        frame.setLayout(new FlowLayout());
        images[0] = GaborPatterns.createPattern(1, 1, 5, 1, 0, 0xff000000, 0xffffffff, 200, 200);
        images[1] = GaborPatterns.createPattern(2, 2, 5, 1, 0, 0xff000000, 0xffffffff, 200, 200);
        for (int i = 0; i < images.length; i++) {
            frame.add(new ImageComponent(images[i], images[i].getWidth(null), images[i].getHeight(null)));
        }
        frame.setSize(800, 800);
        frame.validate();
        frame.show();
    }

    public static void main(String[] argv) {
        new TryGabor(argv);
    }
}
