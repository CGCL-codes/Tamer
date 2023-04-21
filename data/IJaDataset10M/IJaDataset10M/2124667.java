package org.isi.monet.applications.console.control.actions;

import org.isi.monet.applications.console.core.constants.ErrorCode;
import org.isi.monet.applications.console.presentation.user.constants.Views;
import org.isi.monet.applications.console.presentation.user.views.ViewError;
import org.isi.monet.applications.console.presentation.user.views.ViewsFactory;

public class ActionShowBusinessUnitStopped extends Action {

    public ActionShowBusinessUnitStopped() {
        super();
    }

    public String execute() {
        ViewError oViewError;
        oViewError = (ViewError) ViewsFactory.getInstance().get(Views.ERROR, this.codeLanguage);
        oViewError.setErrorCode(ErrorCode.BUSINESS_UNIT_STOPPED);
        return oViewError.execute();
    }
}
