package org.itsnat.impl.core.js.dom.event.domext;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatDOMExtEvent;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.impl.core.js.dom.event.JSEventRenderImpl;

/**
 *
 * @author jmarranz
 */
public abstract class JSDOMExtEventRenderImpl extends JSEventRenderImpl {

    /** Creates a new instance of JSDOMExtEventRenderImpl */
    public JSDOMExtEventRenderImpl() {
    }

    public static JSDOMExtEventRenderImpl getJSDOMExtEventRender(ItsNatDOMExtEvent event) {
        if (event instanceof ItsNatUserEvent) return JSUserEventRenderImpl.SINGLETON; else throw new ItsNatException("This event type is not supported", event);
    }
}
