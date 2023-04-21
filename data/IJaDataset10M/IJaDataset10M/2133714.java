package shapetool;

import java.awt.*;
import java.awt.geom.*;

/**
 * This class represents a surfaced ellipse entity.
 * @author AntonioSRGomes
 * @version $Id: EllipseEntity.java,v 1.1 2003/10/22 03:06:43 asrgomes Exp $
 */
public class EllipseEntity extends ShapeEntity {

    /**
     * Constructs a ellipse entity.
     * @param name name of this entity.
     * @param w witdth of this ellipse.
     * @param h height of this ellipse.
     */
    public EllipseEntity(String name, double w, double h) {
        super(name, new Ellipse2D.Double(0.0, 0.0, w, h));
    }

    /**
     * Gets the current main paint color.
     * 
     * It is supposed that the entity has a main color, but this doesn't
     * restrict the entity to use several colors at the same time.
     *
     * @return current default color. Notice that this color depends on
     *         the current selection state.
     */
    public Color getCurrentPaintColor() {
        if (!isSelected()) return new Color(100, 100, 100); else return new Color(10, 10, 255);
    }
}
