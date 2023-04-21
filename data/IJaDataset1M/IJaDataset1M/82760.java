package base.gui.clock.hand;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import base.gui.clock.ClockPane;
import base.util.ModelTime;

public class ModernClockHand implements ClockHand {

    public static final ModernClockHand INSTANCE = new ModernClockHand();

    private ModernClockHand() {
    }

    public String getRessourceKey() {
        return "clock_hand.modern";
    }

    public void paint(final Graphics2D gr, final ClockPane cp) {
        Dimension size = cp.getSize();
        int x = size.width;
        gr.setColor(cp.getColor());
        ModelTime t = cp.getTime();
        if (t != null) {
            double h = t.getStunde() % 12;
            double m = t.getMinute();
            h += m / 60;
            int md = x / 60;
            int hd = x / 40;
            Shape zh1 = new Rectangle(0, 0, hd, x / 3);
            Shape zh2 = new Rectangle(-hd, 0, hd, x / 3);
            Shape zm1 = new Rectangle(0, 0, md, x / 2);
            Shape zm2 = new Rectangle(-md, 0, md, x / 2);
            AffineTransform ah = new AffineTransform();
            ah.translate((x / 2), x / 2);
            ah.rotate((h / 6 + 1) * Math.PI);
            zh1 = ah.createTransformedShape(zh1);
            zh2 = ah.createTransformedShape(zh2);
            gr.fill(zh1);
            gr.fill(zh2);
            AffineTransform am = new AffineTransform();
            am.translate((x / 2), x / 2);
            am.rotate((m / 30 + 1) * Math.PI);
            zm1 = am.createTransformedShape(zm1);
            zm2 = am.createTransformedShape(zm2);
            gr.fill(zm1);
            gr.fill(zm2);
            AffineTransform ak = new AffineTransform();
            Shape k = new Ellipse2D.Double(-2 * hd, -2 * hd, 4 * hd, 4 * hd);
            ak.translate((x / 2), x / 2);
            k = ak.createTransformedShape(k);
            gr.fill(k);
        }
    }
}
