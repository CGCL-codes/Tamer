package com.ecmdeveloper.plugin.search.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import com.ecmdeveloper.plugin.search.model.AndContainer;
import com.ecmdeveloper.plugin.search.model.Comparison;
import com.ecmdeveloper.plugin.search.model.OrContainer;
import com.ecmdeveloper.plugin.search.model.QueryDiagram;
import com.ecmdeveloper.plugin.search.model.QuerySubpart;

/**
 * 
 * @author ricardo.belfor
 *
 */
public class CloneCommand extends Command {

    private List<QuerySubpart> parts;

    private List<QuerySubpart> newTopLevelParts;

    private List newConnections;

    private QueryDiagram parent;

    private Map<QuerySubpart, Rectangle> bounds;

    private Map<QuerySubpart, Integer> indices;

    private Map connectionPartMap;

    public CloneCommand() {
        super("Clone");
        parts = new LinkedList<QuerySubpart>();
    }

    public void addPart(QuerySubpart part, Rectangle newBounds) {
        parts.add(part);
        if (bounds == null) {
            bounds = new HashMap<QuerySubpart, Rectangle>();
        }
        bounds.put(part, newBounds);
    }

    public void addPart(QuerySubpart part, int index) {
        parts.add(part);
        if (indices == null) {
            indices = new HashMap<QuerySubpart, Integer>();
        }
        indices.put(part, new Integer(index));
    }

    protected void clonePart(QuerySubpart oldPart, QueryDiagram newParent, Rectangle newBounds, List newConnections, Map connectionPartMap, int index) {
        QuerySubpart newPart = null;
        if (oldPart instanceof Comparison) {
            newPart = new Comparison(oldPart.getQuery());
        } else if (oldPart instanceof OrContainer) {
            newPart = new OrContainer(oldPart.getQuery());
        } else if (oldPart instanceof AndContainer) {
            newPart = new AndContainer(oldPart.getQuery());
        }
        if (oldPart instanceof QueryDiagram) {
            Iterator i = ((QueryDiagram) oldPart).getChildren().iterator();
            while (i.hasNext()) {
                clonePart((QuerySubpart) i.next(), (QueryDiagram) newPart, null, newConnections, connectionPartMap, -1);
            }
        }
        if (index < 0) {
            newParent.addChild(newPart);
        } else {
            newParent.addChild(newPart, index);
        }
        newPart.setSize(oldPart.getSize());
        if (newBounds != null) {
            newPart.setLocation(newBounds.getTopLeft());
        } else {
            newPart.setLocation(oldPart.getLocation());
        }
        if (newParent == parent) newTopLevelParts.add(newPart);
        connectionPartMap.put(oldPart, newPart);
    }

    public void execute() {
        connectionPartMap = new HashMap();
        newConnections = new LinkedList();
        newTopLevelParts = new LinkedList();
        Iterator i = parts.iterator();
        QuerySubpart part = null;
        while (i.hasNext()) {
            part = (QuerySubpart) i.next();
            if (bounds != null && bounds.containsKey(part)) {
                clonePart(part, parent, (Rectangle) bounds.get(part), newConnections, connectionPartMap, -1);
            } else if (indices != null && indices.containsKey(part)) {
                clonePart(part, parent, null, newConnections, connectionPartMap, ((Integer) indices.get(part)).intValue());
            } else {
                clonePart(part, parent, null, newConnections, connectionPartMap, -1);
            }
        }
    }

    public void setParent(QueryDiagram parent) {
        this.parent = parent;
    }

    public void redo() {
        for (Iterator iter = newTopLevelParts.iterator(); iter.hasNext(); ) parent.addChild((QuerySubpart) iter.next());
    }

    public void undo() {
        for (Iterator iter = newTopLevelParts.iterator(); iter.hasNext(); ) parent.removeChild((QuerySubpart) iter.next());
    }
}
