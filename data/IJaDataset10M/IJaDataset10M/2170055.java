package com.triplea.rolap.api;

import com.triplea.dao.*;
import com.triplea.dao.sql.Utils;
import com.triplea.rolap.common.SessionManager;
import com.triplea.rolap.server.Reply;
import com.triplea.rolap.server.StringContent;
import com.triplea.rolap.plugins.IRequest;
import com.triplea.rolap.plugins.IReply;

public class ElementCreateHandler extends BaseHandler {

    public IReply handleRequest(IRequest request) {
        StringBuffer out = new StringBuffer(256);
        try {
            String database = request.getParameter("database");
            String dimension = request.getParameter("dimension");
            String new_name = request.getParameter("new_name");
            String type = request.getParameter("type");
            String inChildren = request.getParameter("children");
            String inWeights = request.getParameter("weights");
            String sid = request.getParameter("sid");
            Connection connection = server.getConnection();
            if (!SessionManager.getInstance().checkSession(sid)) {
                return REPLY_SESSION_BAD;
            }
            if (database == null) return createErrorReply(PaloErrors.DATABASE_NOT_FOUND, "");
            int db_id = Integer.parseInt(database);
            int db_index = connection.getDatabaseIndex(db_id);
            if (db_index == -1) return createErrorReply(PaloErrors.DATABASE_NOT_FOUND, "");
            Database db = connection.getDatabaseByID(db_id);
            if (dimension == null) {
                return createErrorReply(PaloErrors.ERROR_DIMENSION_NOT_FOUND, "");
            }
            int dim_id = Integer.parseInt(dimension);
            int dim_index = db.getDimensionIndex(dim_id);
            if (dim_index == -1) return createErrorReply(PaloErrors.ERROR_DIMENSION_NOT_FOUND, "");
            Dimension dim = db.getDimensionAt(dim_index);
            if (type == null) return createErrorReply(PaloErrors.INVALID_ELEMENT_TYPE, "");
            int type_id = Integer.parseInt(type);
            if (type_id != 1 && type_id != 2 && type_id != 4) return createErrorReply(PaloErrors.INVALID_ELEMENT_TYPE, "");
            if (new_name == null) return createErrorReply(PaloErrors.INVALID_ELEMENT_NAME, "");
            String newNameDecoded = APIUtils.decode(new_name);
            if (dim.getElementIndex(new_name) != -1) return createErrorReply(PaloErrors.ERROR_ELEMENT_ALREADY_EXISTS, "");
            String children[] = Utils.getList(inChildren, ',');
            float weights[] = new float[children.length];
            if (inWeights != null) {
                String[] strWeights = Utils.getList(inWeights, ',');
                if (children.length != strWeights.length) return createErrorReply(PaloErrors.ERROR_MISSING_PARAMETER, "");
                for (int i = 0; i < weights.length; i++) {
                    try {
                        weights[i] = Float.parseFloat(strWeights[i]);
                    } catch (Exception e) {
                        return createErrorReply(PaloErrors.ERROR_CONVERSION_FAILED, "");
                    }
                }
            } else {
                for (int i = 0; i < weights.length; i++) weights[i] = 1;
            }
            Element[] childs = new Element[children.length];
            for (int i = 0; i < children.length; i++) {
                int childIndex = dim.getElementIndex(Integer.parseInt(children[i]));
                if (childIndex == -1) return createErrorReply(PaloErrors.ERROR_ELEMENT_NOT_FOUND, "");
                childs[i] = dim.getElementAt(childIndex);
                for (int j = 0; j < i; j++) if (childs[j] == childs[i]) return createErrorReply(PaloErrors.ERROR_ELEMENT_ALREADY_CONSOLIDATED, "");
            }
            Element el = dim.createElement(newNameDecoded, type_id);
            for (int i = 0; i < children.length; i++) {
                Element child = dim.getElementById(Integer.parseInt(children[i]));
                el.addConsolidation(child, weights[i]);
                child.notifyParentAdded(el);
            }
            if (children.length > 0) el.handleAuxData();
            el.getResult(out);
            token = dim.getTokenName() + dim.getToken();
        } catch (Throwable e) {
            return createErrorReply(PaloErrors.INTERNAL_ERROR, e.getMessage());
        }
        return new Reply(Reply.OK, new StringContent(out), token);
    }
}
