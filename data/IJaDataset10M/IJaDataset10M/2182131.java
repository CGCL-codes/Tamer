package net.sf.wubiq.wrappers;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.io.Serializable;

/**
 * @author Federico Alcantara
 *
 */
public class ShapeWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private GeneralPath generalPath;

    public ShapeWrapper() {
        generalPath = null;
    }

    public ShapeWrapper(Shape shape) {
        this();
        if (shape != null) {
            generalPath = new GeneralPath(shape);
        }
    }

    public Shape getShape() {
        return generalPath;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        if (generalPath != null) {
            return "ShapeWrapper [generalPath=" + ((Shape) generalPath).getBounds() + "]";
        } else {
            return "ShapeWrapper [generalPath=null]";
        }
    }
}
