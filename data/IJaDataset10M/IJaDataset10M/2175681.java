package org.isi.monet.applications.backoffice.control.actions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.isi.monet.applications.backoffice.control.constants.Actions;
import org.isi.monet.applications.backoffice.control.constants.Parameter;
import org.isi.monet.applications.backoffice.core.constants.ErrorCode;
import org.isi.monet.applications.backoffice.core.constants.MessageCode;
import org.isi.monet.applications.backoffice.core.model.Language;
import org.isi.monet.applications.backoffice.library.LibraryRequest;
import org.isi.monet.core.components.ComponentPersistence;
import org.isi.monet.core.constants.Strings;
import org.isi.monet.core.exceptions.DataException;
import org.isi.monet.core.exceptions.SessionException;
import org.isi.monet.core.model.Node;

public class ActionDoShareNode extends Action {

    private ComponentPersistence oComponentPersistence;

    public ActionDoShareNode() {
        super();
        this.oComponentPersistence = ComponentPersistence.getInstance();
    }

    public String execute() {
        String id = LibraryRequest.getParameter(Parameter.ID, this.oRequest);
        String sUsers = LibraryRequest.getParameter(Parameter.USERS, this.oRequest);
        String sDescription = LibraryRequest.getParameter(Parameter.DESCRIPTION, this.oRequest);
        DateFormat dfExpireDate = new SimpleDateFormat("dd/MM/yyyy");
        Date dtExpireDate = null;
        String sExpireDate;
        Node oNode;
        if (!this.oComponentAccountsManager.isLogged()) {
            throw new SessionException(ErrorCode.USER_NOT_LOGGED, this.idSession);
        }
        try {
            sExpireDate = LibraryRequest.getParameter(Parameter.EXPIRE_DATE, this.oRequest);
            if ((sExpireDate != null) && (!sExpireDate.equals(Strings.EMPTY))) dtExpireDate = dfExpireDate.parse(sExpireDate);
        } catch (ParseException oException) {
            throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SHARE_NODE, oException);
        }
        if ((id == null) || (sUsers == null)) {
            throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SHARE_NODE);
        }
        if (this.oPoolNode.exists(id)) oNode = this.oPoolNode.get(id); else {
            oNode = this.oComponentPersistence.loadNode(id);
            this.oPoolNode.add(oNode);
        }
        this.oComponentPersistence.shareNode(oNode, sUsers, sDescription, dtExpireDate);
        return Language.getInstance().getMessage(MessageCode.NODE_SHARED, this.codeLanguage);
    }
}
