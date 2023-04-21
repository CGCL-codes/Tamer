package org.apache.batik.bridge;

import java.awt.image.BufferedImage;
import java.util.EventObject;
import java.util.List;

/**
 * This class represents an event which indicate an event originated
 * from a UpdateManager instance.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: UpdateManagerEvent.java,v 1.1 2005/11/21 09:51:18 dev Exp $
 */
public class UpdateManagerEvent extends EventObject {

    /**
     * The buffered image.
     */
    protected BufferedImage image;

    /**
     * The dirty areas, as a List of Rectangles.
     */
    protected List dirtyAreas;

    /**
     * True if before painting this update the canvas's painting
     * transform needs to be cleared.
     */
    protected boolean clearPaintingTransform;

    /**
     * Creates a new UpdateManagerEvent.
     * @param source the object that originated the event, ie. the
     *               UpdateManager.
     * @param bi the image to paint.
     * @param das List of dirty areas.
     */
    public UpdateManagerEvent(Object source, BufferedImage bi, List das) {
        super(source);
        this.image = bi;
        this.dirtyAreas = das;
        this.clearPaintingTransform = false;
    }

    /**
     * Creates a new UpdateManagerEvent.
     * @param source the object that originated the event, ie. the
     *               UpdateManager.
     * @param bi the image to paint.
     * @param das List of dirty areas.
     * @param cpt Indicates if the painting transform should be
     *            cleared as a result of this event.
     */
    public UpdateManagerEvent(Object source, BufferedImage bi, List das, boolean cpt) {
        super(source);
        this.image = bi;
        this.dirtyAreas = das;
        this.clearPaintingTransform = cpt;
    }

    /**
     * Returns the image to display, or null if the rendering failed.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Returns the dirty areas (list of rectangles)
     */
    public List getDirtyAreas() {
        return dirtyAreas;
    }

    /**
     * returns true if the component should clear it's painting transform
     * before painting the associated BufferedImage.
     */
    public boolean getClearPaintingTransform() {
        return clearPaintingTransform;
    }
}
