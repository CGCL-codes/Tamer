package hu.sztaki.lpds.pgportal.portlets.admin;

import hu.sztaki.lpds.pgportal.service.message.PortalMessageService;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.portlet.*;
import java.sql.*;

/**
 * @author krisztian karoczkai
 */
public class HelpTextPortlet extends GenericPortlet {

    @Override
    public void doView(RenderRequest req, RenderResponse resp) throws PortletException, IOException {
        try {
            Connection connection = PortalMessageService.getI().getMessageDBConnection();
            PreparedStatement proc = connection.prepareStatement(" SELECT * FROM messages_texts WHERE tkey like 'help.%' ORDER BY tkey ");
            ResultSet rs = proc.executeQuery();
            Vector v = new Vector();
            while (rs.next()) {
                v.add(new MessageBean(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
            req.setAttribute("msgs", v);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/jsp/admin/helptexts.jsp");
        dispatcher.include(req, resp);
    }

    /**
     * Portlet esemeny kezeles
     */
    @Override
    public void processAction(ActionRequest req, ActionResponse resp) throws PortletException, IOException {
        try {
            Connection connection = PortalMessageService.getI().getMessageDBConnection();
            PreparedStatement proc = null;
            if (req.getParameter("ptyp").equals("0")) {
                proc = connection.prepareStatement("UPDATE messages_texts SET txt=? WHERE tkey=?");
                proc.setString(1, req.getParameter("content").trim());
                proc.setString(2, req.getParameter("ptkey").trim());
                PortalMessageService.getI().setMessage(req.getParameter("ptkey"), req.getParameter("content").trim());
            } else {
                proc = connection.prepareStatement("INSERT INTO messages_texts VALUES(?,?,?)");
                proc.setString(1, req.getParameter("pntkey").trim());
                proc.setString(2, req.getParameter("pndesc").trim());
                proc.setString(3, req.getParameter("content").trim());
                PortalMessageService.getI().setMessage(req.getParameter("pntkey"), req.getParameter("content").trim());
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
 * ajax esemeny
 */
    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        if (request.getParameter("d") != null) {
            String res = "" + request.getParameter("d") + "::" + PortalMessageService.getI().getMessage("" + request.getParameter("j"));
            response.getWriter().write(res);
        }
        if (request.getParameter("ptyp") != null) {
            PrintWriter out = response.getWriter();
            File f = new File(System.getProperty("catalina.home") + "/webapps/portal30/admin/" + request.getParameter("ptyp"));
            String[] s = f.list();
            out.println("var tinyMCE" + request.getParameter("ptyp") + "List = new Array(");
            for (int i = 0; i < s.length; i++) {
                out.print("[\"" + s[i] + "\",\"/portal30/admin/" + request.getParameter("ptyp") + "/" + s[i] + "\"],");
            }
            out.println("[\"--\",\"--\"]");
            out.println(");");
        }
    }
}
