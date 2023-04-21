package ar.com.ironsoft.javaopenauth.web.servlets.foursquare;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ar.com.ironsoft.javaopenauth.dto.OAuthToken;
import ar.com.ironsoft.javaopenauth.url.URLService;
import ar.com.ironsoft.javaopenauth.utils.Constants;
import ar.com.ironsoft.javaopenauth.utils.ContentType;
import ar.com.ironsoft.javaopenauth.web.servlets.hotmail.ContactsServlet;

@SuppressWarnings("serial")
public class FoursquareContactsServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(ContactsServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> headers = new HashMap();
        OAuthToken token = (OAuthToken) request.getSession().getAttribute("oauth_token");
        String encodedToken = URLEncoder.encode(token.getAccessToken(), Constants.ENCODING_UTF_8);
        URLService urlService = new URLService();
        Map<String, String> json = urlService.fetchUrl("https://api.foursquare.com/v2/users/self/friends?oauth_token=" + encodedToken, ContentType.JSON);
        request.setAttribute("json", json.get("json"));
        try {
            request.getRequestDispatcher("/contacts.jsp").forward(request, response);
        } catch (ServletException e) {
            log.log(Level.WARNING, "server error when redirecting to contacts.jsp", e);
            response.getWriter().println("Some server error ocurred. " + e.getMessage());
        }
    }
}
