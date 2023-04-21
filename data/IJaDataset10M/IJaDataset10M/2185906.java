package com.ecomponentes.formularios.empresatask.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.ecomponentes.formularios.empresatask.to.EmpresaTaskTO;
import com.ecomponentes.formularios.perfilempresa.bo.PerfilEmpresaBO;
import com.ecomponentes.formularios.perfilempresa.to.PerfilEmpresaTO;

public class EmpresaTaskEditForm extends ActionForm {

    private EmpresaTaskTO to = new EmpresaTaskTO();

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
        ActionErrors actionErrors = new ActionErrors();
        return actionErrors;
    }

    public EmpresaTaskTO getTo() {
        return to;
    }

    public void setTo(EmpresaTaskTO to) {
        this.to = to;
    }

    public PerfilEmpresaTO[] getPerfils() {
        PerfilEmpresaBO perfilEmpresaBO = new PerfilEmpresaBO();
        return perfilEmpresaBO.getPerfilsEmpresasByEmpresa(to.tbEmpresa.idEmpresa);
    }
}
