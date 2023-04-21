package com.ecomponentes.formularios.subsetor.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.ecomponentes.formularios.setor.bo.SetorBO;
import com.ecomponentes.formularios.setor.to.SetorTO;
import com.ecomponentes.formularios.subsetor.to.SubSetorTO;

public class SubSetorEditForm extends ActionForm {

    private SubSetorTO to = new SubSetorTO();

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
        ActionErrors actionErrors = new ActionErrors();
        return actionErrors;
    }

    public SubSetorTO getTo() {
        return to;
    }

    public void setTo(SubSetorTO to) {
        this.to = to;
    }

    public SetorTO[] getSetors() {
        SetorBO setorBO = new SetorBO();
        return setorBO.selecionarTodos();
    }
}
