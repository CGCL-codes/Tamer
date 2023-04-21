package org.apache.batik.svggen;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;

/**
 * This test validates drawImage conversions.
 *
 * @author <a href="mailto:vhardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id$
 */
public class Bug4389 implements Painter {

    public void paint(Graphics2D g) {
        ImageIcon image = new ImageIcon(ClassLoader.getSystemResource("org/apache/batik/svggen/resources/vangogh.png"));
        g.translate(40, 40);
        g.drawImage(image.getImage(), new AffineTransform(), null);
    }
}
