package edu.berkeley.guir.quill.util;

import javax.swing.Icon;
import java.awt.Component;
import java.awt.Graphics;

/** This is an icon that does not draw anything, but just returns a
    width and height. 
 * <P>
 * This software is distributed under the 
 * <A HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt">
 * Berkeley Software License</A>.
 */
public class EmptyIcon implements Icon {

    protected int width, height;

    public EmptyIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
    }
}
