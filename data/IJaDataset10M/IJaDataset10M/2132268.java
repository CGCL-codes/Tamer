package com.dcivision.workflow.applet;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dcivision.framework.DataSourceFactory;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.TextUtility;
import com.dcivision.user.bean.UserRecord;

/**
 * <p>Class Name:       ListWorkflowGroupAction.java    </p>
 * <p>Description:      The list action class for ListWorkflowGroup.jsp</p>
 *
 * @author           Beyond Qu
 * @company          DCIVision Limited
 * @creation date    31/08/2004
 * @version          $Revision: 1.7 $
 */
public class AppletMiddleware extends HttpServlet {

    public static final String REVISION = "$Revision: 1.7 $";

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    private OutputDataToApplet servletToApplet = null;

    /**
     * init
     * This function will be called only once, when the servlet is called for
     * the first time to initialize servletToApplet object and to perform
     * one time activities
     *
     * @param config is a ServletConfig object
     * @throws ServletException exception
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Process the HTTP doGet request.
     *
     * @param request is a HttpServletRequest object
     * @param response is a HttpServletResponse object
     *
     * @throws ServletException exception
     * @throws IOException exception
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = DataSourceFactory.getConnection();
            String dataType = request.getParameter("dataType");
            Integer workflowRecordID = TextUtility.parseIntegerObj(request.getParameter("workflowRecordID"));
            servletToApplet = new OutputDataToApplet(request, this.getSessionContainer(request), conn, dataType, workflowRecordID);
            servletToApplet.sendResponseToApplet(response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception ignore) {
            } finally {
                conn = null;
            }
        }
    }

    /**
    * Process the HTTP doPost request. which will call doGet(...) method.
    *
    * @param request is a HttpServletRequest object
    * @param response is a HttpServletResponse object
    *
    * @throws ServletException exception
    * @throws IOException exception
   */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    /**
    * getConnection
    *
    * @param request  The HTTP request
    * @return     The connection which pre-opened and stored in request
    */
    public Connection getConnection(HttpServletRequest request) {
        return ((Connection) request.getAttribute(GlobalConstant.DB_KEY));
    }

    /**
    * getSessionContainer
    *
    * @param request  The HTTP request
    * @return     The Session Container
    */
    public SessionContainer getSessionContainer(HttpServletRequest request) {
        SessionContainer ctx = (SessionContainer) request.getSession().getAttribute(GlobalConstant.SESSION_CONTAINER_KEY);
        if (ctx == null) {
            ctx = new SessionContainer();
            ctx.setUserRecord(new UserRecord());
            request.getSession().setAttribute(GlobalConstant.SESSION_CONTAINER_KEY, ctx);
        }
        return (ctx);
    }
}
