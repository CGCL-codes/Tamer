package org.mobicents.servlet.sip.core;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * Represents a Sip Servlet object
 * 
 * @author jean.deruelle@gmail.com
 *
 */
public interface MobicentsSipServlet {

    /**
	 * Retrieves the name of the sip servlet
	 * @return
	 */
    String getName();

    /**
	 * Allocate and return an instance of the Sip Servlet
	 * @return an instance of the Sip Servlet
	 * @throws ServletException if something went wrong during the allocation
	 */
    Servlet allocate() throws ServletException;

    /**
	 * DeAllocate the given Sip Servlet
	 * @throws ServletException if something went wrong during the deallocation
	 */
    void deallocate(Servlet servlet) throws ServletException;

    /**
	 * Check whehter or not the servlet is available 
	 * @return
	 */
    boolean isUnavailable();
}
