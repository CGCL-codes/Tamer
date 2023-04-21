package com.gjl.app.view.web.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import com.gjl.app.view.web.BaseAction;
import com.gjl.oauth.OauthHandler;
import com.gjl.oauth.OauthHandlerFactory;

public class JouthAction extends BaseAction {

    private static final long serialVersionUID = -7828881511804409736L;

    private static final Logger log = Logger.getLogger(JouthAction.class.getName());

    @Override
    public String execute() throws Exception {
        try {
            OauthHandler oauthHandler = OauthHandlerFactory.getOauthHander();
            oauthHandler.fetchRequestToken();
            String url = oauthHandler.getAuthorizeUrl();
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute(JouthAction.class.getName(), oauthHandler);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.sendRedirect(url);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        return super.execute();
    }

    public String callback() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String oauthVerifier = request.getParameter("oauth_verifier");
        String oauthToken = request.getParameter("oauth_token");
        OauthHandler oauthHandler = (OauthHandler) session.getAttribute(JouthAction.class.getName());
        String url = oauthHandler.access(oauthVerifier, oauthToken);
        URL requestLocation = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestLocation.openConnection();
        connection.setRequestProperty("Authorization", "OAuth " + oauthHandler.getOauthParam().composeAccessParameter(",", ""));
        connection.connect();
        int code = connection.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            System.out.println("OK");
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        return SUCCESS;
    }
}
