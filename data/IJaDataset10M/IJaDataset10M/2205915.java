package com.triplea.rolap.api;

import com.triplea.dao.Connection;
import com.triplea.dao.DataException;
import com.triplea.dao.Database;
import com.triplea.dao.Entity;
import com.triplea.rolap.common.MetaModelHolder;
import com.triplea.rolap.common.SessionManager;
import com.triplea.rolap.server.Reply;
import com.triplea.rolap.server.Request;
import com.triplea.rolap.server.StringContent;
import com.triplea.rolap.plugins.IRequest;
import com.triplea.rolap.plugins.IReply;

public class DatabaseUnloadHandler extends BaseHandler {

    public IReply handleRequest(IRequest request) {
        String database = request.getParameter("database");
        String sid = request.getParameter("sid");
        if (!SessionManager.getInstance().checkSession(sid)) {
            return REPLY_SESSION_BAD;
        }
        int dbId;
        try {
            dbId = Integer.parseInt(database);
        } catch (Throwable t) {
            return createErrorReply(PaloErrors.DATABASE_NOT_FOUND, "database not found");
        }
        StringBuffer out = new StringBuffer(256);
        try {
            Connection connection = server.getConnection();
            Database db = connection.getDatabaseByID(dbId);
            db.setStatus(Entity.STATUS_UNLOADED);
            out.append("1").append(DELIM);
            out.append(CRLF);
        } catch (Throwable e) {
            return createErrorReply(PaloErrors.INTERNAL_ERROR, e.getMessage());
        }
        return new Reply(Reply.OK, new StringContent(out));
    }
}
