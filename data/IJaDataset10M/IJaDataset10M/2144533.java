package net.sourceforge.ondex.ovtk2.graphics;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

/**
 * An extendion of GraphicsDecorator that intercepts calls to make drawing more
 * efficient.
 * 
 * @author taubertj
 * 
 * 
 */
public class OVTK2GraphicsDecorator extends GraphicsDecorator {

    public OVTK2GraphicsDecorator() {
        this(null);
    }

    public OVTK2GraphicsDecorator(Graphics2D delegate) {
        super(delegate);
    }

    @Override
    public void draw(Component c, CellRendererPane rendererPane, int x, int y, int w, int h, boolean shouldValidate) {
        super.draw(c, rendererPane, x, y, w, h, shouldValidate);
    }

    @Override
    public void draw(Icon icon, Component c, Shape clip, int x, int y) {
        super.draw(icon, c, clip, x, y);
    }
}
