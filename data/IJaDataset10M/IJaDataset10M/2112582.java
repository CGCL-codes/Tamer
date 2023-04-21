package org.isi.monet.applications.backoffice.control.actions;

import org.isi.monet.applications.backoffice.control.constants.Parameter;
import org.isi.monet.applications.backoffice.core.constants.ErrorCode;
import org.isi.monet.applications.backoffice.library.LibraryRequest;
import org.isi.monet.applications.backoffice.presentation.user.constants.ViewType;
import org.isi.monet.applications.backoffice.presentation.user.constants.Views;
import org.isi.monet.applications.backoffice.presentation.user.views.ViewHelper;
import org.isi.monet.core.constants.Strings;
import org.isi.monet.core.exceptions.SessionException;
import org.isi.monet.core.model.BusinessUnit;
import org.isi.monet.core.model.Page;

public class ActionDoLoadHelperPage extends Action {

    private BusinessUnit oBusinessUnit;

    public ActionDoLoadHelperPage() {
        super();
        this.oBusinessUnit = BusinessUnit.getInstance();
    }

    public String execute() {
        String code = LibraryRequest.getParameter(Parameter.CODE, this.oRequest);
        String sMode = LibraryRequest.getParameter(Parameter.MODE, this.oRequest);
        ViewHelper oViewHelper;
        Page oPage;
        if (!this.oComponentAccountsManager.isLogged()) {
            throw new SessionException(ErrorCode.USER_NOT_LOGGED, this.idSession);
        }
        sMode = sMode.replace(Strings.AMPERSAND_HTML, Strings.AMPERSAND);
        oPage = this.oBusinessUnit.loadHelperPage(code);
        oViewHelper = (ViewHelper) this.oViewsFactory.get(Views.HELPER, this.oAgentRender, this.codeLanguage);
        oViewHelper.setMode(sMode);
        oViewHelper.setType(ViewType.HELP);
        oViewHelper.setTarget(oPage);
        oPage.setContent(oViewHelper.execute());
        return oPage.serializeToJSON();
    }
}
