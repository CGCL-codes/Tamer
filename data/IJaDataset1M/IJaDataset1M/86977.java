package shapetool;

import java.awt.geom.*;

/**
 * This class represents a surfaced box entity.
 * @author AntonioSRGomes
 * @version $Id: BoxEntity.java,v 1.1 2003/10/22 03:06:43 asrgomes Exp $
 */
public class BoxEntity extends ShapeEntity {

    /**
     * Constructs a box entity.
     * @param name name of this entity.
     * @param w witdth of this box.
     * @param h height of this box.
     */
    public BoxEntity(String name, double w, double h) {
        super(name, new Rectangle2D.Double(0.0, 0.0, w, h));
    }
}
