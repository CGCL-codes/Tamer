package fr.inria.zvtm.event;

import fr.inria.zvtm.engine.portals.Portal;

/**Interface to handle events happening in a Portal.
 * @author Emmanuel Pietriga
 */
public interface PortalListener {

    /** Cursor enters portal. */
    public void enterPortal(Portal p);

    /** Cursor exits portal. */
    public void exitPortal(Portal p);
}
