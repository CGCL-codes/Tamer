package pub.servlets.display;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import pub.utils.*;
import pub.db.*;
import pub.beans.*;
import pub.servlets.*;

public class DisplayURL implements SubServlet {

    public void doService(HttpServletRequest request, HttpServletResponse response, PubServlet parentServlet) throws IOException, ServletException {
        String presentation_jsp = chooseJSP(request, response);
        parentServlet.include(presentation_jsp, request, response);
    }

    private String chooseJSP(HttpServletRequest request, HttpServletResponse response) {
        String jsp_name = "/jsp/display/DisplayURL.jsp";
        return jsp_name;
    }
}
