package org.openacs;

import java.io.*;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Logger;
import javax.ejb.FinderException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.openacs.utils.Ejb;

/**
 *
 * @author Administrator
 * @version
 */
public class ConfigServlet extends HttpServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        ConfigurationLocalHome cs = Ejb.lookupConfigurationBean();
        HostsLocalHome hs = Ejb.lookupHostsBean();
        Logger.getLogger(ConfigServlet.class.getName()).info("CfgServlet: from ip=" + request.getRemoteAddr());
        try {
            Collection _h = hs.findByIpM(request.getRemoteAddr());
            if (_h.isEmpty()) {
                response.setStatus(response.SC_NOT_FOUND);
                return;
            }
            HostsLocal host = (HostsLocal) _h.iterator().next();
            System.out.println("CfgServlet: Found host hwid=" + host.getHwid() + " config=" + host.getConfigname());
            if (host.getConfigname() != null) {
                ConfigurationLocal cfg = cs.findByPrimaryKey(new ConfigurationPK(host.getHwid(), host.getConfigname()));
                String cfgString = new String(cfg.getConfig());
                cfgString = processVars(cfgString, host);
                response.setContentLength(cfg.getConfig().length);
                ServletOutputStream out = response.getOutputStream();
                out.write(cfg.getConfig());
                out.flush();
                out.close();
            } else {
                response.setStatus(response.SC_NOT_FOUND);
            }
        } catch (FinderException ex) {
            System.out.println("CfgServlet: finderexception" + ex);
            response.setStatus(response.SC_NOT_FOUND);
        }
    }

    private String processVars(String cfg, HostsLocal host) throws IOException {
        StringBuilder c = new StringBuilder(cfg.length() * 2);
        int fromIndex = 0, curIndex;
        Properties props = new Properties();
        props.load(new ByteArrayInputStream(host.getProps()));
        while ((curIndex = cfg.indexOf("#{", fromIndex)) != -1) {
            int toIndex = cfg.indexOf('}', fromIndex);
            if (toIndex == -1) {
                Logger.getLogger(ConfigServlet.class.getName()).severe("Unclosed #{ at char " + curIndex + " of config " + host.getConfigname());
                throw new RuntimeException();
            } else {
                String name = cfg.substring(curIndex + 2, toIndex);
                c.append(cfg.substring(fromIndex, curIndex));
                String value = props.getProperty(name);
                if (value != null) {
                    c.append(value);
                } else {
                    Logger.getLogger(ConfigServlet.class.getName()).warning("Property '" + name + "' not found at char " + curIndex + " of config " + host.getConfigname());
                }
            }
            fromIndex = toIndex + 1;
        }
        c.append(cfg.substring(fromIndex));
        return c.toString();
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
