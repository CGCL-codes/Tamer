package org.torweg.pulse.bundle;

import org.jdom.Element;
import org.torweg.pulse.service.request.ServiceRequest;

/**
 * is implemented by classes which can produce an extended JDOM deserialisation,
 * if they are given the current {@code ServiceRequest}.
 * 
 * @author Thomas Weber
 * @version $Revision: 1383 $
 */
public interface ExtendedJDOMable extends JDOMable {

    /**
	 * deserialises the {@code Object} adding additional information based
	 * on the given {@code ServiceRequest}.
	 * 
	 * @param r
	 *            the current request
	 * @return a JDOM representation of the {@code Object}
	 */
    Element deserializeToJDOM(final ServiceRequest r);
}
