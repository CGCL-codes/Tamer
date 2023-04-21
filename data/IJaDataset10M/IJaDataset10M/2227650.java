package lokahi.core.common.interfaces;

import lokahi.core.common.interfaces.Collectable;

/**
 * @author Stephen Toback
 * @version $Id: Restable.java,v 1.1 2006/03/07 20:18:51 drtobes Exp $
 */
public interface Restable extends Collectable {

    StringBuilder buildXMLRepresention();

    StringBuilder buildShortXMLRepresentation();
}
