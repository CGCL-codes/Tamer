package com.bitmovers.maui.components;

import java.awt.Graphics;

/** The <code>MouseDownPaintable</code> interface indicates that implementing
  * components can represent themselves graphically in a mouse-down state
  * (that is, the mouse has clicked on the component).
  *
  * @author Patrick Gibson (patrick@bitmovers.com)
  *
  */
public interface MouseDownPaintable extends Paintable {

    public static final String STATE_NAME = "down";

    public void paintMouseDown(Graphics graphics);
}
