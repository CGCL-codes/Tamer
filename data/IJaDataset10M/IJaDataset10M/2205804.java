package com.vangent.hieos.logbrowser.servlets;

import com.vangent.hieos.logbrowser.log.db.Log;
import com.vangent.hieos.logbrowser.log.db.LoggerException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class GetTestNameServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(GetTestNameServlet.class);

    /**
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(ServletConfig config) throws ServletException {
    }

    /**
     *
     * @param req
     * @param res
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        PreparedStatement queryTestName = null;
        PreparedStatement queryTestNameWithIP = null;
        Log log = new Log();
        try {
            queryTestNameWithIP = log.getConnection().prepareStatement("select distinct test from main where ip=? order by test asc");
            queryTestName = log.getConnection().prepareStatement("select distinct test from main order by test asc");
            res.setContentType("text/xml");
            ResultSet result = null;
            if (req.getParameter("ip") != null) {
                queryTestNameWithIP.setString(1, req.getParameter("ip"));
                result = queryTestNameWithIP.executeQuery();
            } else {
                result = queryTestName.executeQuery();
            }
            res.getWriter().write("<result>");
            while (result.next()) {
                res.getWriter().write("<test >" + result.getString(1) + "</test>");
            }
            res.getWriter().write("</result>");
        } catch (SQLException e) {
            getError(e, res);
            e.printStackTrace();
        } catch (LoggerException e) {
            getError(e, res);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                log.closeConnection();
            } catch (LoggerException ex) {
                logger.error(ex);
            }
        }
    }

    /**
     *
     * @param e
     * @param response
     */
    private void getError(Exception e, HttpServletResponse response) {
        PrintWriter print;
        try {
            print = response.getWriter();
            response.setContentType("text/xml");
            StringBuffer toPrint = new StringBuffer();
            StringBuffer toPrint2 = new StringBuffer();
            toPrint.append("<result>");
            toPrint.append("<error>");
            toPrint.append(e.getClass().toString() + ":" + e.getMessage());
            toPrint2.append(e.getClass().toString() + ":" + e.getMessage() + "\n");
            StackTraceElement[] stack = e.getStackTrace();
            for (int i = 0; i < stack.length; i++) {
                toPrint2.append(stack[i].toString() + "\n");
            }
            toPrint.append("</error>");
            toPrint.append("</result>");
            print.write(toPrint.toString());
        } catch (IOException e1) {
        }
    }
}
