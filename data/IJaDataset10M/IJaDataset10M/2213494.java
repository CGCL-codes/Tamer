package org.echarts.servlet.sip.examples.ReliableResponse.test;

import javax.servlet.ServletConfig;
import javax.servlet.sip.SipServletRequest;
import org.echarts.servlet.sip.EChartsSipServlet;

/** Sample comment
 */
public class ReliableResponseSipServlet extends EChartsSipServlet {

    public static final String rcsid = "$Id: $ $Name:  $";

    @Override
    public void servletInit(ServletConfig sc) throws Exception {
    }

    @Override
    public void servletDestroy() {
    }

    @Override
    protected String sessionKeyFromRequest(SipServletRequest req) {
        return super.sessionKeyFromRequest(req);
    }
}
