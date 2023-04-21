package org.knopflerfish.util.servlet;

import java.util.Hashtable;
import javax.servlet.http.HttpServlet;

/** A <code>ServletDescriptor</code> holds data related to a Servlet.
 *
 * @author Lasse Helander (lars-erik.helander@home.se)
 */
public class ServletDescriptor {

    Hashtable initParameters;

    HttpServlet servlet;

    String subContext;

    public ServletDescriptor(String subContext, HttpServlet servlet) {
        this.subContext = subContext;
        this.servlet = servlet;
    }
}
