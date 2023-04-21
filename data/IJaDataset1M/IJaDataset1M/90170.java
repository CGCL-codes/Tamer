package au.edu.diasb.emmet;

import static au.edu.diasb.emmet.protocol.EmmetParameters.ACTION_PARAM;
import static au.edu.diasb.emmet.util.EmmetProperties.FORCE_HTTPS_PROP;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import au.edu.diasb.chico.mvc.BaseController;
import au.edu.diasb.chico.mvc.RequestFailureException;
import au.edu.diasb.emmet.protocol.EmmetAction;
import au.edu.diasb.emmet.util.EmmetProperties;

/**
 * Cutdown Emmet MVC Controller for use in webapps that only have the Emmet 
 * security stack.  It just implements the 'initiateLogin' request.
 * 
 * @author scrawley
 */
public class EmmetMiniMeController extends BaseController implements InitializingBean {

    private String loginUrl;

    private String logoutUrl;

    private EmmetProperties props;

    private boolean forceHttps;

    private LoginRedirector loginRedirector;

    public EmmetMiniMeController() {
        super(Logger.getLogger(EmmetMiniMeController.class));
        setSupportedMethods(new String[] { "GET", "POST" });
    }

    @Override
    public final void afterPropertiesSet() {
        Assert.notNull(props, "'props' not set");
        forceHttps = props.getBooleanProperty(FORCE_HTTPS_PROP);
        loginRedirector = new LoginRedirector(forceHttps, loginUrl, logoutUrl, props);
    }

    public final void setProps(Properties props) {
        if (loginRedirector != null) {
            throw new IllegalStateException("Too late to change the properties ...");
        }
        this.props = EmmetProperties.asInstance(props);
    }

    public final void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
        if (loginRedirector != null) {
            loginRedirector.setLoginUrl(loginUrl);
        }
    }

    public final void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
        if (loginRedirector != null) {
            loginRedirector.setLogoutUrl(logoutUrl);
        }
    }

    @Override
    protected EmmetResponse doHandleRequest(HttpMethod method, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            switch(method) {
                case GET:
                    String actionParam = getNonEmptyParameter(request, ACTION_PARAM);
                    EmmetAction action;
                    try {
                        action = EmmetAction.valueOf(actionParam);
                    } catch (IllegalArgumentException ex) {
                        throw new RequestFailureException(HttpServletResponse.SC_BAD_REQUEST, "Unknown action '" + actionParam + "'", ex);
                    }
                    switch(action) {
                        case initiateLogin:
                            return loginRedirector.initiateLogin(request, response);
                        case initiateLogout:
                            return loginRedirector.initiateLogout(request, response);
                        default:
                            throw new RequestFailureException(HttpServletResponse.SC_BAD_REQUEST, "Unknown action '" + actionParam + "'");
                    }
                default:
                    throw new RequestFailureException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unsupported method '" + method + "'");
            }
        } catch (Exception ex) {
            handleRequestException(ex, getLogger(), response);
            return null;
        }
    }
}
