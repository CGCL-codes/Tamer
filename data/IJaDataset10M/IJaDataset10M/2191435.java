package br.com.wepa.webapps.orca.controle.actions.struts;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ActionEvent {

    private ActionForward forward;

    private ActionForm form;

    private String name;

    private String scope;

    public ActionEvent() {
    }

    public ActionEvent(ActionForm form, ActionMapping mapping, ActionForward forward) {
        super();
        setForward(forward);
        setForm(form);
        setScope(mapping.getScope());
        setName(mapping.getName());
    }

    public ActionForward getForward() {
        return forward;
    }

    public void setForward(ActionForward forward) {
        this.forward = forward;
    }

    public boolean hasForm() {
        return form != null;
    }

    @Override
    public String toString() {
        return "Event: FORM=" + (form != null ? form.toString() : "") + " FORWARD=" + (forward != null ? forward.toString() : "");
    }

    public ActionForm getForm() {
        return form;
    }

    public void setForm(ActionForm form) {
        this.form = form;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
