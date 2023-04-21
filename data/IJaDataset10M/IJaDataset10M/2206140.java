package gj.shell.model;

import gj.awt.geom.Path;
import gj.model.Node;
import gj.util.ArcHelper;
import java.awt.geom.Point2D;

/**
 * Arc for Shell
 */
public class ShellArc implements gj.model.Arc {

    private Path path = new Path();

    private ShellNode start, end;

    private ShellGraph graph;

    /**
   * Constructor
   */
    protected ShellArc(ShellGraph grAph, ShellNode from, ShellNode to) {
        graph = grAph;
        start = from;
        end = to;
        ArcHelper.update(this);
    }

    /**
   * Translate
   */
    protected void translate(Point2D delta) {
        path.translate(delta);
    }

    /**
   * Remove it
   */
    public void delete() {
        graph.removeArc(this);
        start.removeArc(this);
        end.removeArc(this);
    }

    /**
   * @see gj.model.Arc#getStart()
   */
    public Node getStart() {
        return start;
    }

    /**
   * @see gj.model.Arc#getEnd()
   */
    public Node getEnd() {
        return end;
    }

    /**
   * @see gj.model.Arc#getPath()
   */
    public Path getPath() {
        return path;
    }

    /**
   * @see java.lang.Object#toString()
   */
    public String toString() {
        return start + ">" + end;
    }
}
