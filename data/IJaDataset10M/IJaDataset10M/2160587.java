package com.triplea.rolap.api;

import com.triplea.dao.Connection;
import com.triplea.dao.Database;
import com.triplea.dao.Dimension;
import com.triplea.rolap.common.SessionManager;
import com.triplea.rolap.server.Reply;
import com.triplea.rolap.server.StringContent;
import com.triplea.rolap.plugins.IRequest;
import com.triplea.rolap.plugins.IReply;

public class DimensionRenameHandler extends BaseHandler {

    public IReply handleRequest(IRequest request) {
        String database = request.getParameter("database");
        String dimension = request.getParameter("dimension");
        String new_name = request.getParameter("new_name");
        String sid = request.getParameter("sid");
        if (!SessionManager.getInstance().checkSession(sid)) {
            return REPLY_SESSION_BAD;
        }
        int db_id;
        try {
            db_id = Integer.parseInt(database);
        } catch (Throwable t) {
            return createErrorReply(PaloErrors.DATABASE_NOT_FOUND, "database not found");
        }
        int dim_id;
        try {
            dim_id = Integer.parseInt(dimension);
        } catch (Throwable t) {
            return createErrorReply(PaloErrors.ERROR_DIMENSION_NOT_FOUND, "dimension not found");
        }
        StringBuffer out = new StringBuffer(256);
        try {
            Connection connection = server.getConnection();
            Database db = connection.getDatabaseByID(db_id);
            Dimension dim = db.getDimensionById(dim_id);
            dim.setName(APIUtils.decode(new_name));
            out.append(dim.getId()).append(DELIM);
            out.append(dim.getName()).append(DELIM);
            out.append(dim.getElementCount()).append(DELIM);
            out.append("0").append(DELIM);
            out.append("0").append(DELIM);
            out.append("0").append(DELIM);
            token = dim.getTokenName() + dim.getToken();
        } catch (Throwable e) {
            return createErrorReply(PaloErrors.INTERNAL_ERROR, e.getMessage());
        }
        return new Reply(Reply.OK, new StringContent(out), token);
    }
}
