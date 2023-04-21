package cz.fi.muni.xkremser.editor.server.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import org.apache.log4j.Logger;
import cz.fi.muni.xkremser.editor.server.HttpCookies;
import cz.fi.muni.xkremser.editor.server.ServerUtils;
import cz.fi.muni.xkremser.editor.server.URLS;
import cz.fi.muni.xkremser.editor.server.config.EditorConfiguration.ServerConstants;
import cz.fi.muni.xkremser.editor.shared.rpc.action.LogoutAction;
import cz.fi.muni.xkremser.editor.shared.rpc.action.LogoutResult;

/**
 * The Class PutRecentlyModifiedHandler.
 */
public class LogoutHandler implements ActionHandler<LogoutAction, LogoutResult> {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(LogoutHandler.class.getPackage().toString());

    private static final Logger ACCESS_LOGGER = Logger.getLogger(ServerConstants.ACCESS_LOG_ID);

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    /** The http session provider. */
    private final Provider<HttpSession> httpSessionProvider;

    private final Provider<HttpServletRequest> reqProvider;

    /**
     * Instantiates a new put recently modified handler.
     * 
     * @param httpSessionProvider
     *        the http session provider
     */
    @Inject
    public LogoutHandler(Provider<HttpSession> httpSessionProvider, Provider<HttpServletRequest> reqProvider) {
        this.httpSessionProvider = httpSessionProvider;
        this.reqProvider = reqProvider;
    }

    @Override
    public LogoutResult execute(final LogoutAction action, final ExecutionContext context) throws ActionException {
        HttpSession session = httpSessionProvider.get();
        LOGGER.debug("Processing action: LogoutAction");
        ServerUtils.checkExpiredSession(httpSessionProvider);
        ACCESS_LOGGER.info("LOG OUT: [" + FORMATTER.format(new Date()) + "] User " + session.getAttribute(HttpCookies.NAME_KEY) + " with openID " + session.getAttribute(HttpCookies.SESSION_ID_KEY) + " and IP " + reqProvider.get().getRemoteAddr());
        session.setAttribute(HttpCookies.SESSION_ID_KEY, null);
        session.invalidate();
        return new LogoutResult(URLS.ROOT() + (URLS.LOCALHOST() ? URLS.LOGIN_LOCAL_PAGE : URLS.LOGIN_PAGE));
    }

    @Override
    public Class<LogoutAction> getActionType() {
        return LogoutAction.class;
    }

    @Override
    public void undo(LogoutAction action, LogoutResult result, ExecutionContext context) throws ActionException {
    }
}
