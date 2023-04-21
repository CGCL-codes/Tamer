package org.argouml.application.api;

/**  An interface which must be implemented as the UI for 
 *   each primary panel.
 *
 *   @author Thierry Lach
 *   @since 0.9.5
 */
public interface QuadrantPanel {

    public static final int Q_TOP = 1;

    public static final int Q_BOTTOM = 2;

    public static final int Q_LEFT = 4;

    public static final int Q_RIGHT = 8;

    public static final int Q_TOP_LEFT = Q_TOP + Q_LEFT;

    public static final int Q_TOP_RIGHT = Q_TOP + Q_RIGHT;

    public static final int Q_BOTTOM_LEFT = Q_BOTTOM + Q_LEFT;

    public static final int Q_BOTTOM_RIGHT = Q_BOTTOM + Q_RIGHT;

    public int getQuadrant();
}
