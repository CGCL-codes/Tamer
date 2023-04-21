package org.apache.harmony.awt.state;

import com.google.code.appengine.awt.Point;

/**
 * State of menu bar
 */
public interface MenuBarState extends MenuState {

    Point getLocationOnScreen();
}
