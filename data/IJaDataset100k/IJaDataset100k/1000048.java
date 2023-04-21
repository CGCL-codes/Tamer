package hu.gbalage.owl.cms.disec.flickr.auth;

import hu.gbalage.owl.cms.disec.flickr.FlickrService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;

/**
 * @author balage
 *
 */
public class FlickrAuthCheck extends HttpServlet {

    private static final long serialVersionUID = 788293116188634227L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Flickr f;
        try {
            f = FlickrService.createFlickr();
        } catch (ParserConfigurationException e) {
            throw new ServletException(e);
        }
        AuthInterface authInterface = f.getAuthInterface();
        resp.setContentType("text/plain");
        String frob = "unknown";
        try {
            frob = req.getParameter("frob");
            Auth auth = authInterface.getToken(frob);
            resp.getWriter().println("Authentication success");
            resp.getWriter().println("Token: " + auth.getToken());
            resp.getWriter().println("nsid: " + auth.getUser().getId());
            resp.getWriter().println("Realname: " + auth.getUser().getRealName());
            resp.getWriter().println("Username: " + auth.getUser().getUsername());
            resp.getWriter().println("Permission: " + auth.getPermission().getType());
            new JDOAuthStore().store(auth);
        } catch (FlickrException e) {
            resp.getWriter().println("Authentication failure!");
            resp.getWriter().println("Frob: " + frob);
            resp.getWriter().println(e.getLocalizedMessage());
        } catch (SAXException e) {
            throw new ServletException(e);
        }
    }
}
