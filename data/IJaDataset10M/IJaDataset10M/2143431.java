package edu.xtec.jclic.report.servlet;

import edu.xtec.servlet.*;

/**
 *
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class Img extends AbstractServlet {

    protected RequestProcessor createRP() throws Exception {
        return new edu.xtec.jclic.report.rp.Img();
    }
}
