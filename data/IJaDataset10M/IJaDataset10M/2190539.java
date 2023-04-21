package avoware.intchat.server.servlet;

import avoware.intchat.server.misc.Tools;
import avoware.intchat.server.session.IntChatSessionManager;
import avoware.intchat.server.session.IntChatSession;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ResourceBundle;
import java.util.Locale;
import avoware.intchat.server.IntChatServerDefaults;
import avoware.intchat.server.RuntimeParameters;
import avoware.intchat.server.RuntimeParameters.ParameterNames;
import java.util.HashMap;
import avoware.intchat.shared.util.HTMLMisc;
import avoware.intchat.server.io.IntChatServletWriter;
import java.sql.Connection;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.RetryRequest;

/**
 *
 * @author Andrew Orlov
 */
public class SystemSettingsEdit2 extends _Auth {

    private Connector connector;

    /** Creates a new instance of SystemSettingsEdit2
     * @param connector
     * @param icsm
     * @throws IllegalArgumentException
     */
    public SystemSettingsEdit2(Connector connector, IntChatSessionManager icsm) throws IllegalArgumentException {
        super(icsm);
        this.connector = connector;
    }

    @SuppressWarnings(value = { "unchecked" })
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (doAuth(request, response)) {
            Connection conn = null;
            try {
                IntChatSession _session = icsm.getIntChatSession(request);
                int UID = _session.getUID();
                Locale LOCALE = _session.getLocale();
                String _prefixedSessionId = icsm.getSessionURLPrefix() + request.getSession(false).getId();
                conn = getJDBCConnection(icsm.getDatabaseConnectionPool(), request, response, HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                if (conn == null) return;
                boolean[] moduleAccess = Tools.getModuleAccess(conn, UID);
                if (!moduleAccess[0]) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                ResourceBundle bundle = ResourceBundle.getBundle("avoware/intchat/server/i18n/IntChatServerIntPack", LOCALE);
                ParameterNames[] params = ParameterNames.values();
                HashMap<ParameterNames, String> newParams = new HashMap<ParameterNames, String>();
                for (int i = 0; i < params.length; i++) {
                    String paramName = params[i].toString();
                    String paramValue = request.getParameter(paramName);
                    if (paramValue != null) {
                        newParams.put(ParameterNames.valueOf(paramName), paramValue);
                    }
                }
                String message = null;
                if (RuntimeParameters.applyNewParams(newParams)) {
                    RuntimeParameters.save(conn);
                } else {
                    message = RuntimeParameters.getLastError();
                }
                if (message != null) {
                    HashMap<String, String> replacements = new HashMap<String, String>();
                    replacements.put("_HEAD_", bundle.getString("systemsettingsedit2_erroroccured"));
                    replacements.put("_MESSAGE_", message);
                    replacements.put("_URL_", SystemSettingsEdit1.class.getSimpleName() + _prefixedSessionId);
                    replacements.put("_URLTEXT_", bundle.getString("systemsettingsedit2_back"));
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("text/html");
                    response.setCharacterEncoding(IntChatServerDefaults.ENCODING);
                    new IntChatServletWriter(request, response, IntChatServerDefaults.ENCODING).writeFinalizing(Tools._header + HTMLMisc.loadReplacements(Tools._simple_message, replacements) + Tools._footer);
                    return;
                }
                icsm.onSystemSettingsUpdated();
                response.sendRedirect(AdminTasks.class.getSimpleName() + _prefixedSessionId);
            } catch (RetryRequest rr) {
                throw rr;
            } catch (Exception e) {
                Tools.makeErrorResponse(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
            } finally {
                try {
                    if (conn != null) icsm.getDatabaseConnectionPool().releaseConnection(conn);
                } catch (Exception e) {
                }
            }
        }
    }
}
