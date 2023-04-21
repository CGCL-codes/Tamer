package net.sf.wubiq.wrappers;

import java.awt.image.ImageObserver;
import java.io.Serializable;

/**
 * Wraps image observer (not serializable) into a suitable
 * serializable (but null) element.
 * @author Federico Alcantara
 *
 */
public class ImageObserverWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    public ImageObserverWrapper() {
    }

    public ImageObserverWrapper(ImageObserver obs) {
    }
}
