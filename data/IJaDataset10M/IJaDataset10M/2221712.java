package sears.gui.glassPane;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JViewport;

/**
 * This class, although it's a non Abstract class and could be use like it is,
 * is a <i>prototype</i> class and should be overwritted.
 */
public class DefaultViewportGlassPane extends ViewportGlassPane {

    private static final long serialVersionUID = -7782241093516309531L;

    /** the default color of the virtual glass pane */
    protected static final Color DEFAULT_COLOR = Color.BLACK;

    /** the default factor of the virtual glass pane alpha transparency*/
    protected static final float DEFAULT_ALPHA = .22f;

    private Color virtualBoundsColor;

    private float alpha;

    /**
	 * Creates a new class instance with default color and alpha factor transparency
	 * @param viewport	the viewport
	 * @throws 			NullPointerException if viewport is null
	 * @see ViewportGlassPane#ViewportGlassPane(JViewport)
	 */
    public DefaultViewportGlassPane(JViewport viewport) {
        super(viewport);
        init(DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    /**
	 * Creates a new class instance with non default color and alpha factor transparency
	 * 
	 * @param viewport				the viewport
	 * @param virtualBoundsColor	the color of the drawing virtual glass pane
	 * @param alpha					a float that define the opacity of the virtual glass pane
	 * @throws 			NullPointerException if viewport is null
	 * @see ViewportGlassPane#ViewportGlassPane(JViewport)
	 */
    public DefaultViewportGlassPane(JViewport viewport, Color virtualBoundsColor, float alpha) {
        super(viewport);
        init(virtualBoundsColor, alpha);
    }

    /**
	 * Initializes color and alpha factor with values given on parameters
	 * <br>If null values is given, default values is set.
	 * @param c	the color 
	 * @param a	the alpha factor
	 */
    private void init(Color c, float a) {
        if (c == null) {
            c = DEFAULT_COLOR;
        }
        if (a < 0) {
            a = DEFAULT_ALPHA;
        }
        virtualBoundsColor = c;
        alpha = a;
    }

    protected void paintComponentWithGraphics(Graphics2D gr) {
        Color color = gr.getColor();
        Composite comp = gr.getComposite();
        gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        gr.setColor(virtualBoundsColor);
        gr.fill(this.getVirtualBounds());
        gr.setColor(color);
        gr.setComposite(comp);
    }

    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        Graphics2D gr = (Graphics2D) g;
        paintChildrenWithGraphics(gr);
    }

    /**
	 * This method is called in <code>paintChildren(g)</code> method.
	 * Allow a way to use the graphics context of this component
	 * 
	 * @param gr the graphics context of this component
	 * @see ViewportGlassPane#paintComponent(Graphics)
	 */
    protected void paintChildrenWithGraphics(Graphics2D gr) {
    }
}
