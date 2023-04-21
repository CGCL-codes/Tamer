package org.uithin.toolkits.swing.scene;

import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

public interface ITool extends MouseListener, MouseMotionListener, MouseWheelListener {

    public void activate(ToolLayer aToolLayer);

    public void deactivate();

    /**
	 * Whether the tool claims mouse events for the given location.
	 * By default, returns true except over node editors.
	 */
    public boolean contains(int aX, int aY);

    public void paint(Graphics2D aGraphics);
}
