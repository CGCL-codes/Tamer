package org.isi.monet.applications.backoffice.control.actions;

import org.isi.monet.applications.backoffice.control.constants.Actions;
import org.isi.monet.applications.backoffice.control.constants.MimeTypes;
import org.isi.monet.applications.backoffice.control.constants.Parameter;
import org.isi.monet.applications.backoffice.core.constants.ErrorCode;
import org.isi.monet.applications.backoffice.library.LibraryRequest;
import org.isi.monet.core.agents.AgentFilesystem;
import org.isi.monet.core.constants.Strings;
import org.isi.monet.core.exceptions.DataException;
import org.isi.monet.core.exceptions.SessionException;
import org.isi.monet.core.model.BusinessUnit;

public class ActionDoLoadBusinessUnitFile extends Action {

    public ActionDoLoadBusinessUnitFile() {
        super();
    }

    public String execute() {
        String sFilename = LibraryRequest.getParameter(Parameter.PATH, this.oRequest);
        MimeTypes oMimeTypes = MimeTypes.getInstance();
        String codeFormat, sAbsoluteFilename;
        if (!this.oComponentAccountsManager.isLogged()) {
            throw new SessionException(ErrorCode.USER_NOT_LOGGED, this.idSession);
        }
        if (sFilename == null) {
            throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_BUSINESS_UNIT_FILE);
        }
        sAbsoluteFilename = BusinessUnit.getInstance().getAbsoluteFilename(sFilename);
        codeFormat = oMimeTypes.getFromFilename(sFilename);
        if (codeFormat == null) {
            throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_BUSINESS_UNIT_FILE);
        }
        sFilename = sFilename.replace(Strings.BAR45, Strings.UNDERLINED);
        try {
            this.oResponse.setContentType(codeFormat);
            this.oResponse.setHeader("Content-Disposition", "inline;");
            this.oResponse.getOutputStream().write(AgentFilesystem.getBytesFromFile(sAbsoluteFilename));
            this.oResponse.getOutputStream().flush();
        } catch (Exception oException) {
            throw new DataException(ErrorCode.LOAD_BUSINESS_UNIT_FILE, sAbsoluteFilename, oException);
        }
        return null;
    }
}
