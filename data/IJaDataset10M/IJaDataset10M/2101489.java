package guestbook.authsub;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.WebContent;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.opensymphony.xwork2.ActionContext;
import com.server.model.LoginUser;
import com.server.tools.calendar.CalendarEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;

/**
 * Handles the processing of an AuthSub token.
 * <p>
 * The user will login to the Google account and lend permission for
 * this service to impersonate the user.  Upon completion of the login
 * and permission-grant, the user will be redirected to this servlet
 * with the token in the URL.
 *
 * 
 */
public class HandleTokenServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = AuthSubUtil.getTokenFromReply(req.getQueryString());
        if (token == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No token specified.");
            return;
        }
        String sessionToken;
        try {
            sessionToken = AuthSubUtil.exchangeForSessionToken(token, Utility.getPrivateKey());
        } catch (IOException e1) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Exception retrieving session token.");
            return;
        } catch (GeneralSecurityException e1) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Security error while retrieving session token.");
            return;
        } catch (AuthenticationException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server rejected one time use token.");
            return;
        }
        try {
            Map<String, String> info = AuthSubUtil.getTokenInfo(sessionToken, Utility.getPrivateKey());
            for (Iterator<String> iter = info.keySet().iterator(); iter.hasNext(); ) {
                String key = iter.next();
                System.out.println("\t(key, value): (" + key + ", " + info.get(key) + ")");
            }
        } catch (IOException e1) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Exception retrieving info for session token.");
            return;
        } catch (GeneralSecurityException e1) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Security error while retrieving session token info.");
            return;
        } catch (AuthenticationException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Auth error retrieving info for session token: " + e.getMessage());
            return;
        }
        String principal = Utility.getCookieValueWithName(req.getCookies(), Utility.LOGIN_COOKIE_NAME);
        if (principal == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unidentified principal.");
            return;
        }
        TokenManager.storeToken(principal, sessionToken);
        HttpSession httpsession = req.getSession();
        httpsession.setAttribute("authSubToken", sessionToken);
        System.out.println(httpsession.getAttribute("authSubToken").toString());
        StringBuffer continueUrl = req.getRequestURL();
        int index = continueUrl.lastIndexOf("/");
        continueUrl.delete(index, continueUrl.length());
        continueUrl.append(LoginServlet.NEXT_URL);
        resp.sendRedirect(continueUrl.toString());
    }
}
