package fussball;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.File;
import java.io.*;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

/**
 *  Description of the Class
 *
 *@author     mic
 *@created    December 14, 2001
 */
public class StatistikTest extends HttpServlet {

    private String path = null;

    private String fileName = null;

    private String team = null;

    /**
	 *  Description of the Method
	 *
	 *@param  request               Description of Parameter
	 *@param  response              Description of Parameter
	 *@exception  ServletException  Description of Exception
	 *@exception  IOException       Description of Exception
	 *@since
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  request               Description of Parameter
	 *@param  response              Description of Parameter
	 *@exception  ServletException  Description of Exception
	 *@exception  IOException       Description of Exception
	 *@since
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ligaFileName = request.getParameter("ligaFileName");
        if (ligaFileName == null) {
            return;
        }
        String path = request.getParameter("path");
        if (path == null) {
            return;
        }
        String fileName = path + ligaFileName;
        this.team = request.getParameter("team");
        response.setContentType("image/jpeg");
        Statistik stat = new Statistik();
        stat.setOut(response.getOutputStream());
        stat.parse(fileName, team);
        response.getOutputStream().close();
    }
}
