package org.jdeluxe.testing;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;

/**
 * The Class LinePaintListerner.
 */
public class LinePaintListerner implements PaintListener {

    /**
	 * Instantiates a new line paint listerner.
	 */
    public LinePaintListerner() {
    }

    public void paintControl(PaintEvent e) {
        System.out.println("PAINTME !!! " + e.toString());
        e.gc.setBackground(new Color(null, 255, 0, 0));
        e.gc.drawLine(0, 0, 1500, 100);
    }
}
