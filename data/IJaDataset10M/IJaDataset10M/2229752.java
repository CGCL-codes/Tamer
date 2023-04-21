package org.soapfabric.server.config;

import org.soapfabric.SOAPFaultException;
import org.soapfabric.server.Filter;
import org.soapfabric.server.FilterChain;
import org.soapfabric.server.SOAPRequest;
import org.soapfabric.server.SOAPResponse;

/**
 * @author <a href="mailto:mbsanchez at users.sf.net">Matt Sanchez</a>
 * @version $Id: NoOpFilter.java,v 1.2 2004/03/09 23:42:30 mbsanchez Exp $
 */
public class NoOpFilter implements Filter {

    public boolean doFilter(SOAPRequest request, SOAPResponse response, FilterChain chain) throws SOAPFaultException {
        return true;
    }
}
