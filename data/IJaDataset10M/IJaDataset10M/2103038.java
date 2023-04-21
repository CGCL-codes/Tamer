package uk.ac.ebi.mg.xchg;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.pri.messenger.Messenger;
import com.pri.messenger.server.ServerMessenger;
import com.pri.messenger.server.ServletHttpConnection;
import com.pri.session.ClientSession;
import com.pri.session.HTTPRequestData;
import com.pri.session.RequestData;

public class MessengerServlet extends HttpServlet {

    protected static volatile boolean ready = false;

    public static final String sessionCookieName = "SESSID";

    ServerMessenger msgr;

    public MessengerServlet() {
        Messenger m = Messenger.getDefaultMessenger();
        if (m instanceof ServerMessenger) {
            msgr = (ServerMessenger) m;
            return;
        }
        try {
            if (m != null) msgr = new ServerMessenger(m, UserCore.getUserCore()); else msgr = new ServerMessenger(UserCore.getUserCore());
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        if (!ready) throw new UnavailableException("Service temporarily unavailable", 600);
        String sessID = null;
        sessID = req.getParameter(sessionCookieName);
        if (sessID == null) {
            Cookie[] cuks = ((HttpServletRequest) req).getCookies();
            if (cuks != null && cuks.length != 0) {
                for (int i = cuks.length - 1; i >= 0; i--) {
                    if (cuks[i].getName().equals(sessionCookieName)) {
                        sessID = cuks[i].getValue();
                        break;
                    }
                }
            }
            if (sessID == null) {
                serviceAnon((HttpServletRequest) req, (HttpServletResponse) res);
                return;
            }
        }
        RequestData reqData = new HTTPRequestData(req);
        UserCore uc = UserCore.getUserCore();
        if (uc == null) throw new UnavailableException("Server not ready");
        ClientSession cldata = uc.getSession(sessID, reqData);
        if (cldata != null) {
            req.setAttribute("ClientSession", cldata);
            service((HttpServletRequest) req, (HttpServletResponse) res, cldata);
            return;
        }
        serviceAnon((HttpServletRequest) req, (HttpServletResponse) res);
    }

    @SuppressWarnings("unused")
    protected void service(HttpServletRequest req, HttpServletResponse res, @SuppressWarnings("unused") ClientSession cs) throws ServletException, IOException {
        if (!req.getMethod().equalsIgnoreCase("POST")) {
            res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        try {
            msgr.processRequest(new ServletHttpConnection(req, res), cs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    protected void serviceAnon(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        sendNotLogedIn(res);
    }

    protected void sendNotLogedIn(HttpServletResponse res) throws IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    public static void setWebappReady(boolean b) {
        ready = b;
    }
}
