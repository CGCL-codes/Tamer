package org.fosterapet.ui.actionhandler;

import java.util.*;
import org.fosterapet.dao.*;
import org.fosterapet.ui.*;
import org.greatlogic.gae.*;
import org.greatlogic.gae.dao.*;
import org.json.*;

public class RoleGetListActionHandler extends ActionHandlerBase {

    @Override
    public void handleAction(final FAPContext context) throws Exception {
        GLLog.fine("");
        List<Role> roleList = DAOHelper.getList(context.getCurrentOrgId(), Role.class);
        JSONArray jSONArray = new JSONArray();
        for (Role role : roleList) {
            jSONArray.put(role.toJSONObject());
        }
        context.getCurrentRespJSONObject().put("roles", jSONArray);
    }
}
