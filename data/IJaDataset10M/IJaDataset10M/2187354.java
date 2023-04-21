package gj.model.impl;

import gj.awt.geom.Path;
import gj.model.Arc;
import gj.model.Node;

/**
 * @see gj.model.MutableArc
 */
public class ArcImpl implements Arc {

    /** starting Node */
    private NodeImpl start;

    /** ending Node */
    private NodeImpl end;

    /** path */
    private Path path;

    ArcImpl(NodeImpl start, NodeImpl end, Path path) {
        this.start = start;
        this.end = end;
        this.path = path;
    }

    void disconnect() {
        start.removeArc(this);
        end.removeArc(this);
    }

    /**
   * String represenation
   */
    public String toString() {
        return start.toString() + ">" + end.toString();
    }

    /**
   * @see Arc#getPath()
   */
    public Path getPath() {
        return path;
    }

    /**
   * @see Arc#getStart()
   */
    public Node getStart() {
        return start;
    }

    /**
   * @see Arc#getEnd()
   */
    public Node getEnd() {
        return end;
    }
}
