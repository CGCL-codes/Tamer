package org.openmoney.servlets;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Constructor;
import javax.servlet.*;
import javax.servlet.http.*;
import org.openmoney.db.*;
import org.openmoney.page.*;
import org.openmoney.struct.*;
import org.openmoney.util.*;

/**
 * The main system servlet
 * @author Reuben Firmin (openmoney@digitalsheep.com)
 */
public class Dispatch extends HttpServlet {

    boolean _first = true;

    public Dispatch() {
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (_first) {
            Config.init(getServletContext());
            Log.init();
            _first = false;
        }
        try {
            String page = request.getParameter("page");
            Origins user = null;
            if (page != null && page.equals("Register")) {
                Log.debug("Dispatch", ">> Registration");
            } else {
                if ((user = Login.isLoggedIn(request, response)) == null) {
                    Log.debug("Dispatch", "User not logged in");
                    page = "Login";
                    Log.debug("Dispatch", ">> Login");
                } else {
                    Log.debug("Dispatch", "User:" + user.getAcctId() + " ; Page: " + page);
                }
            }
            Page p = getPage(page, request, user);
            String html = p.getResponse();
            PrintWriter out = response.getWriter();
            out.println(html);
            out.close();
        } catch (Throwable t) {
            Log.log("Dispatch", t);
        }
    }

    /**
     * Return the specified page, or the index if it can't be found
	 */
    private final Page getPage(String pageName, HttpServletRequest hsr, Origins user) {
        Page p;
        if (pageName == null) pageName = "Index";
        Request request = new Request(hsr, user, pageName);
        try {
            Class c = Class.forName("org.openmoney.page.Page" + pageName);
            Constructor[] cs = c.getConstructors();
            Object pc = (cs[0].newInstance(new Object[] { request }));
            p = (Page) pc;
        } catch (Throwable t) {
            Log.log("Dispatch:getPage", "Can't load: " + t.getMessage());
            p = new PageIndex(request, pageName);
        }
        return p;
    }
}
