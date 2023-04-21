package ajaxservlet;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import saadadb.database.Database;
import ajaxservlet.json.JsonUtils;

/**
 * @version $Id: SiteDesc.java 189 2012-02-07 10:47:36Z laurent.mistahl $
 * Used to init the web root in the JS interface
 * Servlet implementation class SiteDesc
 */
public class SiteDesc extends SaadaServlet implements Servlet {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor. 
	 */
    public SiteDesc() {
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ccccc");
        ServletOutputStream out = response.getOutputStream();
        JsonUtils.teePrint(out, "{");
        JsonUtils.teePrint(out, JsonUtils.getParam("dbname", Database.getName()) + ",");
        JsonUtils.teePrint(out, JsonUtils.getParam("rooturl", Database.getUrl_root()) + ",");
        try {
            if (Database.getWrapper().getBooleanAsString(true).equals("true")) {
                JsonUtils.teePrint(out, JsonUtils.getParam("booleansupported", "true"));
            } else {
                JsonUtils.teePrint(out, JsonUtils.getParam("booleansupported", "false"));
            }
        } catch (Exception e) {
            reportJsonError(request, response, e);
        }
        JsonUtils.teePrint(out, "}");
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
