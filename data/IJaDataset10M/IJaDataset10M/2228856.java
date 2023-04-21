package com.miden2ena.mogeci.actionForm;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * La classe CertificaActionForm contiene i dati del CV da certificare.
 * Non contempla il metodo validate perche' viene completata automaticamente.
 * @author Miden2Ena s.r.l.
 * 
 */
public class CertificaActionForm extends org.apache.struts.action.ActionForm {

    private long id;

    private long flowExpression;

    public long getFlowExpression() {
        return flowExpression;
    }

    public void setFlowExpression(long flowExpression) {
        this.flowExpression = flowExpression;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CertificaActionForm() {
        super();
    }
}
