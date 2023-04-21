package net.sf.fb4j.canvas;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstracts HTTP response functionality for Facebook FBML and IFRAME applications.   
 *  
 * @author Gino Miceli
 */
public abstract class CanvasResponse {

    protected CanvasApplication application;

    protected HttpServletResponse response;

    public CanvasResponse(CanvasApplication application, HttpServletResponse response) {
        this.application = application;
        this.response = response;
    }

    public void sendRedirect(String url) throws IOException {
        response.sendRedirect(url);
    }

    public void redirectToLoginPage(boolean secure, boolean popup, boolean forceLogin, String afterLoginUri, boolean hideSaveLoginCheckbox, boolean canvasAfterLogin) throws IOException {
        String loginUrl = application.getLoginUrl(afterLoginUri, secure, popup, forceLogin, hideSaveLoginCheckbox, canvasAfterLogin);
        sendRedirect(loginUrl);
    }

    public void redirectToInstallPage(String nextUri) throws IOException {
        String installUrl = application.getInstallUrl(nextUri);
        sendRedirect(installUrl);
    }
}
