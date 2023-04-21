package org.eaasyst.eaa.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * <p>This abstract class is the base code for all PlainText
 * servlets.</p>
 *
 * @version 2.9.16
 */
public abstract class PlainTextServletBase extends ServletBase {

    private static final long serialVersionUID = 1;

    /**
	 * <p>The Servlet "init" method.</p>
	 *
	 * @param config the <code>ServletConfig</code> object
	 * @since Eaasy Street 2.9.16
	 */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        setContentType("text/plain;charset=UTF-8");
    }
}
