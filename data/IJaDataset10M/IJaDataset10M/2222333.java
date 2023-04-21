package control;

import data.*;
import business.*;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ControlServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String argo = request.getParameter("argomento");
            String diff = request.getParameter("difficoltà");
            String ndom = request.getParameter("numerodomande");
            ServletContext thisContext = this.getServletContext();
            HttpSession thisSession = request.getSession();
            String propertiesPATH = (String) thisContext.getAttribute("PropertiesPath");
            String driverURL = (String) thisContext.getAttribute("DriverUrl");
            String databaseURL = (String) thisContext.getAttribute("DatabaseUrl");
            if (propertiesPATH == null) {
                propertiesPATH = thisContext.getRealPath("/Properties/qitzconfig.properties");
                thisContext.setAttribute("PropertiesPath", propertiesPATH);
            }
            if (driverURL == null) {
                driverURL = ManageProperties.getDriverURL(propertiesPATH);
                thisContext.setAttribute("DriverUrl", driverURL);
            }
            if (databaseURL == null) {
                databaseURL = ManageProperties.getDatabaseURL(propertiesPATH);
                thisContext.setAttribute("DatabaseUrl", databaseURL);
            }
            ObjResultset objreslt = (ObjResultset) thisSession.getAttribute("ObjResultset");
            if (objreslt == null) objreslt = new ObjResultset(); else objreslt.clearAll();
            ManageQuery.doQuery(objreslt, driverURL, databaseURL, argo, diff, ndom);
            thisSession.setAttribute("ObjResultset", objreslt);
            thisSession.setAttribute("ContaDom", 0);
            request.setAttribute("InTestServlet", "ControlServlet");
            String url = "/TestServlet";
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
            dispatcher.forward(request, response);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServletException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }
}
