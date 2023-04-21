package com.triplea.rolap.api;

import com.triplea.dao.Connection;
import com.triplea.rolap.common.MetaModelHolder;
import com.triplea.rolap.common.Session;
import com.triplea.rolap.common.SessionManager;
import com.triplea.rolap.plugins.PluginProvider;
import com.triplea.rolap.plugins.IReply;
import com.triplea.rolap.plugins.IRequest;
import com.triplea.rolap.server.Reply;
import com.triplea.rolap.server.Request;
import com.triplea.rolap.server.StringContent;

/**
 * @author kiselev
 *         When: 14.09.2007 9:58:08
 */
public class ServerLoginHandler extends BaseHandler {

    public IReply handleRequest(IRequest request) {
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        String extern_password = request.getParameter("extern_password");
        if (password == null && extern_password == null) return createErrorReply(PaloErrors.ERROR_AUTHORIZATION_FAILED, "missing password");
        Session session = SessionManager.getInstance().generateSession(user);
        StringBuffer out = new StringBuffer(256);
        if (PluginProvider.getInstance().getAuthenticationInterface().connect(user, (null != extern_password ? extern_password : password))) {
            out.append(session.getSid()).append(DELIM);
            out.append(SessionManager.TTL).append(DELIM);
            out.append(CRLF);
            try {
                Connection connection = server.getConnection();
                return new Reply(Reply.OK, new StringContent(out), connection.getTokenName() + connection.getToken());
            } catch (Throwable t) {
                _logger.fatal("ServerLoginHandler", t);
            }
            return createErrorReply(PaloErrors.INTERNAL_ERROR);
        } else {
            return createErrorReply(PaloErrors.WORKER_AUTHORIZATION_FAILED, "login error");
        }
    }
}
