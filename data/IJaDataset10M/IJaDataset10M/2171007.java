package ingenias.editor.events;

import java.util.*;
import java.io.*;
import ingenias.editor.*;
import org.jgraph.event.GraphLayoutCacheEvent;
import org.jgraph.graph.*;

public class GraphViewChange implements org.jgraph.event.GraphLayoutCacheListener {

    private GraphModel m;

    private Object working = null;

    public GraphViewChange(GraphModel m) {
        this.m = m;
    }

    public void update(Observable obs, Object arg1) {
    }

    public void graphLayoutCacheChanged(GraphLayoutCacheEvent e) {
    }
}
