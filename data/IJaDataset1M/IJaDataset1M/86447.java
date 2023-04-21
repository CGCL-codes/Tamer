package jshomeorg.simplytrain.service.trackObjects.objects;

import java.awt.*;
import java.util.Iterator;
import jshomeorg.simplytrain.service.*;
import jshomeorg.simplytrain.*;
import jshomeorg.simplytrain.service.trackObjects.*;
import jshomeorg.simplytrain.service.trainCommands.*;
import jshomeorg.simplytrain.service.trainorders.setspeed;

/**
 *
 * @author js
 */
public class destinationPoint extends destinationObject {

    static Color col_hgr = new Color(0xff, 0xff, 0x00);

    static Color col_paint = new Color(0x00, 0x00, 0x00);

    static int fscale = track.RAILRADIUS * 2;

    /** Creates a new instance of speedSign1 */
    public destinationPoint() {
        super();
        setBounds(fscale * 2, fscale * 2);
        setPaintAlways(false);
    }

    public void paint(Graphics2D g2) {
        paintSig(g2, 0);
    }

    public void paintIcon(Graphics2D g2) {
        paintSig(g2, fscale);
    }

    private void paintSig(Graphics2D g2, int x) {
        g2.setColor(col_hgr);
        Polygon p = new Polygon();
        p.addPoint(x, 0);
        p.addPoint(x + fscale, fscale);
        p.addPoint(x - fscale, fscale);
        g2.fillPolygon(p);
        g2.setColor(col_paint);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawPolygon(p);
    }

    public void paintAnim(Graphics2D g2, String userdata, int count) {
    }

    public odsHashSet getData() {
        odsHashSet h = super.getData();
        return h;
    }

    public void setData(odsHashSet hm) {
        super.setData(hm);
    }

    public trackObject clone() {
        destinationPoint t = new destinationPoint();
        t.setData(getData());
        return t;
    }

    public String getGUIObjectName() {
        return "Zielpunkt";
    }
}
