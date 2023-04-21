package com.predator.soldatweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.predator.soldatweb.visitors.KillsVisitor;
import com.soldat.parser.SoldatVisitor;

/**
 * DOCUMENT ME!
 * 
 * @author Garc�a, Rom�n (latest modification by $Author: roman_garcia $).
 * @version $Revision: 1.1 $ $Date: 2005/05/09 14:25:35 $
 */
public class SoldatKillsServlet extends SoldatWebServlet {

    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = 3617296718312716084L;

    /**
     * @see com.predator.soldatweb.servlet.SoldatWebServlet#getSoldatVisitor(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected SoldatVisitor getSoldatVisitor(HttpServletRequest request, HttpServletResponse response) {
        return new KillsVisitor();
    }

    /**
     * @see com.predator.soldatweb.servlet.SoldatWebServlet#getForwardPage(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected String getForwardPage(HttpServletRequest request, HttpServletResponse response) {
        return "killStats.vm";
    }
}
