package com.littleqworks.commons.web.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.littleqworks.commons.util.Filters;

public class Controller extends HttpServlet {

    private static final String ACTION_MAPPING = "action_mapping.properties";

    private static final String NO_ACTION = "no action to handler this request.";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Action action = getAction(getActionByUrl(request));
        ModelAndView view = action.process(request, response);
        if (view.getMsg() != "") {
            response.getWriter().write(view.getMsg());
        } else if (view.getView() != "") {
            request.getRequestDispatcher(view.getView()).forward(request, response);
        } else {
            response.getWriter().write(NO_ACTION);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public Action getAction(String s) {
        Action action = null;
        if (s == NO_ACTION) {
            action = new NoSuchAction();
            return action;
        }
        try {
            action = (Action) Class.forName(getActionClass(s)).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return action;
    }

    private String getActionClass(String s) {
        return null;
    }

    private String getActionByUrl(HttpServletRequest request) {
        String path = request.getServletPath();
        int pos1 = 0;
        int pos2 = 0;
        pos1 = path.lastIndexOf("/");
        pos2 = path.lastIndexOf(".");
        String actionUrl = path.substring(pos1 + 1, pos2 - pos1);
        Properties props = new Properties();
        final String actionConf = getServletContext().getRealPath("WEB-INF" + getSlider() + ACTION_MAPPING);
        try {
            props.load(new FileInputStream(actionConf));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String actionClass = props.getProperty(actionUrl);
        return actionClass == null ? NO_ACTION : actionClass;
    }

    private String getSlider() {
        String dirChar = "/";
        if (Filters.isChildIgnoreCase("windows", System.getProperty("os.name"))) {
            dirChar = "\\";
        }
        return dirChar;
    }
}
