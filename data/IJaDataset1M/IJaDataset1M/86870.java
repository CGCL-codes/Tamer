package org.apache.batik.svggen;

import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.RenderingHints;
import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Font;

/**
 * This test validates outputing font-size as a float
 *
 * @author <a href="mailto:vhardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id: Bug6535.java 475477 2006-11-15 22:44:28Z cam $
 */
public class Bug6535 implements Painter {

    public void paint(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setPaint(Color.black);
        g.scale(10, 10);
        Font font = new Font("Arial", Font.PLAIN, 1);
        Font font2 = font.deriveFont(1.5f);
        g.setFont(font);
        g.drawString("Hello, size 10", 4, 4);
        g.setFont(font2);
        g.drawString("Hello, size 15", 4, 8);
        g.scale(.1, .1);
        font = new Font("Arial", Font.PLAIN, 10);
        font2 = font.deriveFont(15f);
        g.setFont(font);
        g.drawString("Hello, size 10", 160, 40);
        g.setFont(font2);
        g.drawString("Hello, size 15", 160, 80);
    }
}
