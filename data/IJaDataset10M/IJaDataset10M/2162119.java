package org.parosproxy.paros.extension;

import org.parosproxy.paros.model.Session;

/**
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public interface SessionChangedListener {

    /**
	 * sessionChanged may be called by non-event thread. Should handle with care
	 * in all the listener. Use EventThread for each GUI event.
	 * 
	 * @param session
	 */
    public void sessionChanged(Session session);
}
