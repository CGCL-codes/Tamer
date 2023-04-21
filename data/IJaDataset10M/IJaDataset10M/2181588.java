package net.kolmodin.yalt;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.*;

/**
*
*   Wrapper to invoke the list action. This servlet takes  the 
*   request parameter "listType" or the
*   extension part of pathinfo and uses it as the argument listType to
*   the /list.do action. That is, if invoked as <code>/list/people.pdf </code>
*   the call is forwarded to <code>/list.do?listType=pdf </code>.
*   Or if invoked as <code>/list/list?listType=pdf </code>the result is 
*   the same.
*
*   @author Michael Kolmodin
*   @version $Version:$
*
*/
public class ListWrapper extends HttpServlet {

    protected Category log = Category.getInstance("ListWrapper");

    /**
    *
    *  Invoke /list.do, use request parameter 'ListType' or 
    *  extension part of pathinfo as the
    *  argument listType to list.do. 
    *
    *  @param  request     Servlet request for JSP page, forwarded.
    *  @param  response    Servlet reponse, forwarded.
    *  @throws ServletException 
    *                      Internal servlet errors.
    *  @throws IOException IO errors
    *
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String listType = null;
        String uri = request.getPathInfo();
        int dotpos = (uri == null ? -1 : uri.lastIndexOf('.'));
        if (dotpos >= 0) listType = uri.substring(dotpos + 1);
        if (listType == null) {
            log.warn("No listType found: " + HttpUtils.getRequestURL(request));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "A Listtype extension must be provided");
        } else {
            String forward = "/list.do?listType=" + listType;
            RequestDispatcher rd = getServletContext().getRequestDispatcher(forward);
            rd.forward(request, response);
        }
    }

    /**
    *
    *  Invoke /list.do, forwarding extension part of pathInfo as
    *  the listType parameter.
    *
    *  @param  request     Servlet request for JSP page, forwarded.
    *  @param  response    Servlet reponse, forwarded.
    *  @throws ServletException 
    *                      Internal servlet errors.
    *  @throws IOException IO errors
    *
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
