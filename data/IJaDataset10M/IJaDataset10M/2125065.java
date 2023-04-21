package org.apache.batik.swing.svg;

import java.util.EventObject;
import org.apache.batik.gvt.GraphicsNode;

/**
 * This class represents an event which indicate an event originated
 * from a SVGLoadEventDispatcher instance.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGLoadEventDispatcherEvent.java,v 1.1 2005/11/21 09:51:34 dev Exp $
 */
public class SVGLoadEventDispatcherEvent extends EventObject {

    /**
     * The GVT root.
     */
    protected GraphicsNode gvtRoot;

    /**
     * Creates a new SVGLoadEventDispatcherEvent.
     * @param source the object that originated the event, ie. the
     *               SVGLoadEventDispatcher.
     * @param root   the GVT root.
     */
    public SVGLoadEventDispatcherEvent(Object source, GraphicsNode root) {
        super(source);
        gvtRoot = root;
    }

    /**
     * Returns the GVT tree root, or null if the gvt construction
     * was not completed or just started.
     */
    public GraphicsNode getGVTRoot() {
        return gvtRoot;
    }
}
