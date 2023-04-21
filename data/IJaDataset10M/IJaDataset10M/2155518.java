package org.apache.batik.svggen;

import com.google.code.appengine.awt.Graphics2D;

/**
 * This test validates that transforms are collapsed when they
 * should.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @version $Id: TransformCollapse.java 475477 2006-11-15 22:44:28Z cam $
 */
public class TransformCollapse implements Painter {

    public void paint(Graphics2D g) {
        g.translate(10, 10);
        g.translate(20, 30);
        g.drawString("translate collapse", 0, 0);
        g.scale(2, 2);
        g.scale(2, 4);
        g.drawString("scale collapse", 10, 10);
        g.scale(.25, .125);
        g.rotate(Math.toRadians(90));
        g.rotate(Math.toRadians(-60));
        g.drawString("rotate collapse", 0, 40);
        g.rotate(Math.toRadians(-30));
        g.drawString("identity", 0, 80);
    }
}
