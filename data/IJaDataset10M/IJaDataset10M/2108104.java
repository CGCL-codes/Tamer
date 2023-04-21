package org.fosterapet.ui.actionhandler;

import org.fosterapet.dao.*;
import org.fosterapet.ui.*;
import org.fosterapet.ui.ServletReqEnums.EServletReqParam;
import org.greatlogic.gae.*;
import org.greatlogic.gae.dao.DAOEnums.EDAOAction;
import org.greatlogic.gae.dao.*;

public class PetGetUsingIdActionHandler extends ActionHandlerBase {

    @Override
    public void handleAction(final FAPContext context) throws Exception {
        GLLog.fine("");
        DAOErrorList errorList = new DAOErrorList();
        long id = GAEUtil.getParamAsLong(context.getCurrentReq().getParameterMap(), EServletReqParam.BaseDAO_id);
        Pet pet = DAOHelper.getUsingId(EDAOAction.Get, Pet.class, context.getCurrentOrgId(), id, errorList);
        if (errorList.isEmpty()) {
            context.getCurrentRespJSONObject().put("pet", pet.toJSONObject());
        }
    }
}
