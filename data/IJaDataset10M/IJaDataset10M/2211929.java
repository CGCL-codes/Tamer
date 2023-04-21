package org.soapfabric.server;

import org.soapfabric.SOAPFaultException;

/**
 * @author <a href="mailto:mbsanchez at users.sf.net">Matt Sanchez</a>
 * @version $Id: SOAPService.java,v 1.2 2004/03/09 23:42:29 mbsanchez Exp $
 */
public interface SOAPService {

    void service(SOAPRequest request, SOAPResponse response) throws SOAPFaultException;
}
