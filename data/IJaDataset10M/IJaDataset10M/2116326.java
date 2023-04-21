package edu.ucdavis.genomics.metabolomics.binbase.gui.visualize.content;

import java.util.Collection;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import edu.ucdavis.genomics.metabolomics.binbase.gui.visualize.object.Node;

/**
 * renders nodes in form of a graph
 * 
 * @author Administrator
 * 
 */
public class GraphContentProvider implements IGraphEntityContentProvider {

    private Logger logger = Logger.getLogger(getClass());

    Vector<Node> elements = new Vector<Node>();

    /**
	 * contains our graph
	 */
    Collection<Node> graph = new Vector<Node>();

    public GraphContentProvider() {
    }

    /**
	 * returns a list of all nodes
	 */
    public Object[] getElements(Object arg0) {
        Collection<Node> content = (Collection<Node>) arg0;
        for (Node o : content) {
            if (o != null) {
                if (elements.contains(o) == false) {
                    elements.add(o);
                    for (Object ob : getElements(o.getChildren())) {
                        if (elements.contains(ob) == false) {
                            elements.add((Node) ob);
                        }
                    }
                }
            }
        }
        return elements.toArray();
    }

    public void dispose() {
    }

    @SuppressWarnings("unchecked")
    public void inputChanged(Viewer arg0, Object oldData, Object newData) {
        logger.info("changing input");
        if (newData != null) {
            if (newData instanceof Collection) {
                graph = (Collection<Node>) newData;
                elements.clear();
            } else {
                logger.warn(newData + " - is not a collection");
            }
        } else {
            logger.warn("set null input doing nothing");
        }
    }

    public Object[] getConnectedTo(Object arg0) {
        Node node = (Node) arg0;
        Collection<Node> collection = node.getChildren();
        if (node.getParent() != null) {
            for (Node p : node.getParent()) {
                if (collection.contains(p) == false) {
                    if (p != null) {
                        collection.add(p);
                    }
                }
            }
        }
        return collection.toArray();
    }
}
