package com.ecomponentes.formularios.regiao.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.ecomponentes.formularios.regiao.to.RegiaoTO;

public class RegiaoEditForm extends ActionForm {

    private RegiaoTO to = new RegiaoTO();

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
        ActionErrors actionErrors = new ActionErrors();
        return actionErrors;
    }

    public RegiaoTO getTo() {
        return to;
    }

    public void setTo(RegiaoTO to) {
        this.to = to;
    }
}
