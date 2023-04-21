package org.monet.console.presentation.user.views;

import java.io.Writer;
import org.monet.console.control.constants.Actions;
import org.monet.console.core.constants.Tags;
import org.monet.console.presentation.user.util.Context;
import org.monet.kernel.model.BusinessUnit;

public class ViewLogin extends View {

    private BusinessUnit oBusinessUnit;

    public ViewLogin(Context oContext, String codeLanguage) {
        super(oContext, codeLanguage);
        this.oBusinessUnit = null;
    }

    public Boolean setBusinessUnit(BusinessUnit oBusinessUnit) {
        this.oBusinessUnit = oBusinessUnit;
        return true;
    }

    public void execute(Writer writer) {
        super.execute(writer);
        if (this.oBusinessUnit == null) return;
        this.oContext.put(Tags.ACTION_DOLOGIN, Actions.LOGIN);
        this.oContext.put(Tags.ACTION_DOLOGOUT, Actions.LOGOUT);
        this.oContext.put(Tags.ERROR_MESSAGE, (String) this.oTarget);
        this.oContext.put(Tags.BUSINESSUNIT, this.oBusinessUnit);
        this.oAgentTemplates.merge("/console/templates/login.tpl", this.oContext, writer);
    }
}
