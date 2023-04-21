package edu.xtec.jclic.boxes;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import javax.swing.JComponent;

/**
 * This is the mst simple implementation of {@link edu.xtec.jclic.boxes.AbstractBox}. It
 * does nor draws nothing.
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class SimpleBox extends AbstractBox {

    /** Creates new SimpleBox */
    public SimpleBox(AbstractBox parent, JComponent container, BoxBase boxBase) {
        super(parent, container, boxBase);
    }

    public boolean update(Graphics2D g2, Rectangle dirtyRegion, ImageObserver io) {
        return true;
    }

    public boolean updateContent(Graphics2D g2, Rectangle dirtyRegion, ImageObserver io) {
        return true;
    }
}
