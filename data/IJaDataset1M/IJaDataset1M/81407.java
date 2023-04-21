package org.netbeans.api.visual.router;

import org.netbeans.api.visual.widget.ConnectionWidget;
import java.awt.*;
import java.util.List;

/**
 * This class is responsible for routing path of connection widgets.
 * Built-in routers could be created by RouterFactory class. Routers are assigned to connection widgets using
 * ConnectionWidget.setRouter method.
 *
 * @author David Kaspar
 */
public interface Router {

    /**
     * Routes a path for a connection widget. The path is specified by a list of control points.
     * @param widget the connection widget
     * @return the list of control points
     */
    public List<Point> routeConnection(ConnectionWidget widget);
}
