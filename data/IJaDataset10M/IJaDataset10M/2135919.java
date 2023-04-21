package edu.uiuc.ncsa.myproxy.oa4mp.client.servlet;

import edu.uiuc.ncsa.myproxy.oa4mp.client.ClientEnvironment;
import edu.uiuc.ncsa.myproxy.oa4mp.client.OA4MPService;
import edu.uiuc.ncsa.security.servlet.AbstractServlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <p>Created by Jeff Gaynor<br>
 * on 2/10/12 at  12:51 PM
 */
public abstract class ClientServlet extends AbstractServlet {

    public static final String ACTION_KEY = "actionKey";

    public static final String ACTION_REDIRECT_VALUE = "redirect";

    public static final String REDIR = "redirect";

    public static final String TOKEN_KEY = "oauth_token";

    public static final String VERIFIER_KEY = "oauth_verifier";

    @Override
    public void loadEnvironment() throws IOException {
        environment = getBootstrapper().load();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            loadEnvironment();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static OA4MPService mpdsService;

    protected OA4MPService getMPDSService() throws IOException {
        if (mpdsService == null) {
            mpdsService = new OA4MPService(getClientEnvironment());
        }
        return mpdsService;
    }

    protected ClientEnvironment getClientEnvironment() throws IOException {
        return (ClientEnvironment) AbstractServlet.getEnvironment();
    }
}
