package net.sf.janalogtv.util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 * RenderCanvas is a {@link Canvas} subclass that simplifies synchronous rendering.
 */
@SuppressWarnings("serial")
public class RenderCanvas extends Canvas {

    /**
	 * Create a new RenderCanvas with the given size.
	 * @param size the preferred width and height of the canvas
	 */
    public RenderCanvas(Dimension size) {
        setPreferredSize(size);
        setIgnoreRepaint(true);
        setBackground(Color.black);
    }

    /**
	 * Start rendering a frame to the canvas.
	 * The returned {@link Graphics2D} object must be passed to {@link #stopRender(Graphics2D)}
	 * when one frame has been rendered. 
	 * @return the object that should be used for rendering.
	 */
    public Graphics2D startRender() {
        return (Graphics2D) getGraphics();
    }

    /**
	 * Stop rendering a frame.
	 * @param g the object returned by {@link #startRender()} when the frame was started.
	 */
    public void stopRender(Graphics2D g) {
        g.dispose();
    }
}
