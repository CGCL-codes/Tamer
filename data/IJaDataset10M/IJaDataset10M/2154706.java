package org.snipsnap.net;

import snipsnap.api.app.Application;
import snipsnap.api.config.Configuration;
import snipsnap.api.snip.Snip;
import snipsnap.api.snip.SnipLink;
import snipsnap.api.snip.SnipSpaceFactory;
import snipsnap.api.label.Label;
import org.snipsnap.snip.label.LabelManager;
import snipsnap.api.container.Components;
import org.snipsnap.net.filter.MultipartWrapper;
import org.radeox.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Adding a label to a snip
 * @author Stephan J. Schmidt
 * @version $Id: AddLabelServlet.java 1821 2005-04-07 11:33:25Z stephan $
 */
public class AddLabelServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Configuration config = snipsnap.api.app.Application.get().getConfiguration();
        String type = request.getHeader("Content-Type");
        if (type != null && type.startsWith("multipart/form-data")) {
            try {
                request = new MultipartWrapper(request, config.getEncoding() != null ? config.getEncoding() : "UTF-8");
            } catch (IllegalArgumentException e) {
                Logger.warn("AddLabelServlet: multipart/form-data wrapper:" + e.getMessage());
            }
        }
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        snipsnap.api.config.Configuration config = Application.get().getConfiguration();
        String snipName = request.getParameter("snipname");
        if (null == snipName) {
            response.sendRedirect(config.getUrl(config.getStartSnip()));
            return;
        }
        if (null != request.getParameter("cancel")) {
            response.sendRedirect(config.getUrl("/exec/labels?snipname=" + snipsnap.api.snip.SnipLink.encode(snipName)));
            return;
        }
        Snip snip = snipsnap.api.snip.SnipSpaceFactory.getInstance().load(snipName);
        request.setAttribute("snip", snip);
        String labelType = request.getParameter("labeltype");
        LabelManager manager = (LabelManager) snipsnap.api.container.Components.getComponent(LabelManager.class);
        snipsnap.api.label.Label label = manager.getLabel(labelType);
        request.setAttribute("label", label);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/exec/addlabel.jsp");
        dispatcher.forward(request, response);
    }
}
