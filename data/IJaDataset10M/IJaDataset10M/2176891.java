package com.bizsensors.gourangi.struts.actions;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.bizsensors.gourangi.db.Dashboard;
import com.bizsensors.gourangi.db.DashboardKpi;
import com.bizsensors.gourangi.db.HibernateUtil;

public class KPITreeAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Enumeration list = request.getParameterNames();
        String kpiType = request.getParameter("kpiType");
        String selectedNodeID = request.getParameter("selectedNodeID");
        String selectedNodeName = request.getParameter("kpiName");
        request.getSession().setAttribute("selectedNodeType", kpiType);
        request.getSession().setAttribute("selectedNodeID", selectedNodeID);
        request.getSession().setAttribute("selectedNodeName", selectedNodeName);
        if (kpiType != null && kpiType.equals("FOLDER")) {
            request.getSession().setAttribute("KPI_FOLDER_ID", selectedNodeID);
        }
        if (kpiType != null && kpiType.equals("KPI")) {
            request.getSession().setAttribute("KPI_ID", selectedNodeID);
        }
        String target = new String("default");
        String subMenuID = null;
        if (request.getSession().getAttribute("SELECTED_URL") != null) {
            subMenuID = request.getSession().getAttribute("SELECTED_URL").toString();
        }
        if (subMenuID != null) {
            target = subMenuID;
        }
        return (mapping.findForward(target));
    }
}
