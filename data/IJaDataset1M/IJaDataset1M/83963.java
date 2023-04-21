package de.javacus.grafmach.twoD;

import java.awt.Graphics2D;
import java.awt.Shape;

/** provides an easy way for swing, piccolo und Batik. 
 * @since 2010
 * @version 2010-02
 * @author Burkhard Loesel
 * 
 */
public interface Paintable {

    public void paint(Graphics2D g2D, int hWindow);

    public Shape getShape();
}
