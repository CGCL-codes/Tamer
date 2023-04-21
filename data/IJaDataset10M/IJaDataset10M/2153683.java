package homura.hde.main.core.scene.shadow;

import homura.hde.util.export.JMEExporter;
import homura.hde.util.export.JMEImporter;
import homura.hde.util.export.Savable;
import java.io.IOException;

/**
 * <code>ShadowEdge</code>
 * Holds the indices of two points that form an edge in a ShadowTriangle
 * 
 * @author Mike Talbot (some code from a shadow implementation written Jan 2005)
 * @author Joshua Slack
 * @version $Id: ShadowEdge.java,v 1.4 2006/06/01 15:05:44 nca Exp $
 */
public class ShadowEdge implements Savable {

    /**
     * <code>triangle</code> (int) the triangle number (in an occluder) to
     * which the edge is connected or INVALID_TRIANGLE if not connected.
     */
    public int triangle = ShadowTriangle.INVALID_TRIANGLE;

    /** The indices of the two points comprising this edge. */
    public int p0, p1;

    /**
     * @param p0 the first point
     * @param p1 the second point
     */
    public ShadowEdge(int p0, int p1) {
        this.p0 = p0;
        this.p1 = p1;
    }

    public void write(JMEExporter e) throws IOException {
        e.getCapsule(this).write(p0, "p0", 0);
        e.getCapsule(this).write(p1, "p1", 0);
        e.getCapsule(this).write(triangle, "triangle", ShadowTriangle.INVALID_TRIANGLE);
    }

    public void read(JMEImporter e) throws IOException {
        p0 = e.getCapsule(this).readInt("p0", 0);
        p1 = e.getCapsule(this).readInt("p1", 0);
        triangle = e.getCapsule(this).readInt("triangle", ShadowTriangle.INVALID_TRIANGLE);
    }

    public Class getClassTag() {
        return this.getClass();
    }
}
