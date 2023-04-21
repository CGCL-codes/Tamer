package org.springframework.faces.ui;

import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public abstract class DojoAdvisor extends UIComponentBase {

    protected static final String[] DOJO_ATTRS = new String[] { "disabled", "intermediateChanges", "tabIndex", "required", "promptMessage", "invalidMessage", "constraints", "regExp", "regExpGen" };

    private Boolean disabled;

    private Boolean intermediateChanges;

    private Integer tabIndex;

    private Boolean required;

    private String promptMessage;

    private String invalidMessage;

    private String constraints;

    private String regExp;

    private String regExpGen;

    public Boolean getDisabled() {
        if (disabled != null) {
            return disabled;
        }
        ValueExpression exp = getValueExpression("disabled");
        return exp != null ? (Boolean) exp.getValue(getFacesContext().getELContext()) : null;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getIntermediateChanges() {
        return intermediateChanges;
    }

    public void setIntermediateChanges(Boolean intermediateChanges) {
        this.intermediateChanges = intermediateChanges;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getPromptMessage() {
        if (promptMessage != null) {
            return promptMessage;
        }
        ValueExpression exp = getValueExpression("promptMessage");
        return exp != null ? (String) exp.getValue(getFacesContext().getELContext()) : null;
    }

    public void setPromptMessage(String promptMessage) {
        this.promptMessage = promptMessage;
    }

    public String getInvalidMessage() {
        if (invalidMessage != null) {
            return invalidMessage;
        }
        ValueExpression exp = getValueExpression("invalidMessage");
        return exp != null ? (String) exp.getValue(getFacesContext().getELContext()) : null;
    }

    public void setInvalidMessage(String invalidMessage) {
        this.invalidMessage = invalidMessage;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    public String getRegExpGen() {
        return regExpGen;
    }

    public void setRegExpGen(String regExpGen) {
        this.regExpGen = regExpGen;
    }

    protected abstract String[] getDojoAttributes();

    public abstract String getDojoComponentType();

    public Object saveState(FacesContext context) {
        Object[] values = new Object[10];
        values[0] = super.saveState(context);
        values[1] = this.constraints;
        values[2] = this.disabled;
        values[3] = this.intermediateChanges;
        values[4] = this.invalidMessage;
        values[5] = this.promptMessage;
        values[6] = this.regExp;
        values[7] = this.regExpGen;
        values[8] = this.required;
        values[9] = this.tabIndex;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        this.constraints = (String) values[1];
        this.disabled = (Boolean) values[2];
        this.intermediateChanges = (Boolean) values[3];
        this.invalidMessage = (String) values[4];
        this.promptMessage = (String) values[5];
        this.regExp = (String) values[6];
        this.regExpGen = (String) values[7];
        this.required = (Boolean) values[8];
        this.tabIndex = (Integer) values[9];
    }

    public String getFamily() {
        return "spring.faces.Advisor";
    }
}
