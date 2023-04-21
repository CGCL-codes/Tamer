package org.mbari.aosn.colloport.servlet;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mbari.aosn.colloport.logic.CollOPort;
import org.mbari.aosn.colloport.model.Observatory;
import org.mbari.aosn.colloport.setter.ObservatorySetter;
import org.mbari.aosn.colloport.setter.PermissionSetter;
import org.mbari.aosn.colloport.setter.Setter;
import org.mbari.aosn.colloport.setter.TopicSetter;

/**
 * Servlet implementation class for Servlet: AdminSetData
 *
 */
public class AdminSetData extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2912397230005026626L;

    private static CollOPort colloport = null;

    private static Map<String, Setter> setterMap = null;

    public AdminSetData() {
        super();
        if (colloport == null) {
            colloport = CollOPort.getInstance();
        }
        if (setterMap == null) {
            setterMap = new HashMap<String, Setter>();
            setterMap.put("observatory", new ObservatorySetter());
            setterMap.put("permission", new PermissionSetter());
            setterMap.put("topic", new TopicSetter());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("In AdminSetData#doPost");
        if (!Login.isUserInRole(request, CollOPort.getAdminRole())) {
            response.getOutputStream().println("{error:\"no privileges to access AdminSetData\"}");
        }
        String observatory_id = request.getParameter("observatory_id");
        Observatory observatory = null;
        if (observatory_id != null) {
            observatory = colloport.getLatestObservatory(observatory_id);
        }
        Setter setter = (Setter) setterMap.get(request.getParameter("type"));
        if (setter == null) {
            return;
        }
        long current = System.currentTimeMillis();
        long expires = current;
        response.addDateHeader("Expires", expires);
        response.addDateHeader("Last-Modified", current);
        response.setContentType("text/plain");
        String fullname = CollOPort.getFullName(request);
        PrintStream out = new PrintStream(response.getOutputStream());
        setter.set(request, response, colloport, fullname, observatory, out);
    }
}
