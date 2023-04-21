package org.jbrix.gui.xform;

import org.w3c.dom.*;

/**
 *   The interface to be implemented by those objects that wish to
 *   be notified of a particular XComponent's events.
 */
public interface XComponentListener {

    /**
	 *   Called when <code>node</code> is edited by <code>xcomp</code>.
	 *
	 *   @param xcomp The XComponent generating the event.
	 *   @param node The node that was edited.
	 */
    void nodeEdited(XComponent xcomp, Node node);
}
