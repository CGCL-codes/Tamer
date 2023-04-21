package org.isi.monet.applications.fms.control.actions;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.isi.monet.applications.fms.control.constants.Actions;
import org.isi.monet.applications.manager.core.constants.ErrorCode;
import org.isi.monet.core.exceptions.SystemException;

public class ActionsFactory {

    private static ActionsFactory oInstance;

    private HashMap<String, Class<? extends Action>> hmActions;

    private ActionsFactory() {
        this.hmActions = new HashMap<String, Class<? extends Action>>();
        this.registerActions();
    }

    private Boolean registerActions() {
        this.register(Actions.UPLOAD_DOCUMENT, ActionUploadDocument.class);
        this.register(Actions.DOWNLOAD_DOCUMENT, ActionDownloadDocument.class);
        this.register(Actions.UPLOAD_IMAGE, ActionUploadImage.class);
        this.register(Actions.DOWNLOAD_IMAGE, ActionDownloadImage.class);
        return true;
    }

    public static synchronized ActionsFactory getInstance() {
        if (oInstance == null) oInstance = new ActionsFactory();
        return oInstance;
    }

    public Action get(String sType, HttpServletRequest oRequest, HttpServletResponse oResponse) {
        Class<?> cAction;
        Action oAction = null;
        try {
            cAction = (Class<?>) this.hmActions.get(sType);
            oAction = (Action) cAction.newInstance();
            oAction.setRequest(oRequest);
            oAction.setResponse(oResponse);
            oAction.initialize();
        } catch (NullPointerException oException) {
            throw new SystemException(ErrorCode.ACTIONS_FACTORY, sType, oException);
        } catch (Exception oException) {
            throw new SystemException(ErrorCode.ACTIONS_FACTORY, sType, oException);
        }
        return oAction;
    }

    public Boolean register(String sType, Class<? extends Action> cAction) throws IllegalArgumentException {
        if ((cAction == null) || (sType == null)) {
            return false;
        }
        this.hmActions.put(sType, cAction);
        return true;
    }
}
