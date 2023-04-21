package graphlab.gui.plugins.main.core.actions.edge;

import graphlab.gui.core.graph.edge.Edge;
import graphlab.main.core.Event;

/**
 * @author: Ruzbeh
 * Date: Feb 12, 2005
 * Time: 10:55:06 PM
 */
public class EdgeSelectData {

    public static final String name = "Edge.Select";

    public static final Event event = new Event(name);

    public int x;

    public int y;

    public Edge e;
}
