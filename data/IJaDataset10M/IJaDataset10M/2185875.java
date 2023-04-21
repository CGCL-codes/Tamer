package therandomhomepage.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: Siddique Hameed
 * Date: Nov 7, 2006
 * Time: 3:57:26 PM
 */
public class TheRandomHomepageProxyXMLServlet extends TheRandomHomepageProxyServlet {

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            httpServletResponse.setContentType("text/xml");
            super.doGet(httpServletRequest, httpServletResponse);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}
