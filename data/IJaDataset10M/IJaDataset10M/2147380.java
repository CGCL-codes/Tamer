package org.ironrhino.common.action;

import javax.inject.Inject;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.spring.ApplicationContextConsole;
import org.ironrhino.core.struts.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@AutoConfig
public class ConsoleAction extends BaseAction {

    private static final long serialVersionUID = 8180265410790553918L;

    private static Logger log = LoggerFactory.getLogger(ConsoleAction.class);

    private String expression;

    private boolean global = true;

    @Inject
    private transient ApplicationContextConsole applicationContextConsole;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    @Override
    @InputConfig(resultName = "success")
    @Validations(requiredStrings = { @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "expression", trim = true, key = "validation.required") })
    public String execute() {
        try {
            Object o = applicationContextConsole.execute(expression, global);
            addActionMessage(getText("operate.success") + ":" + String.valueOf(o));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            addActionError(getText("error") + ":" + e.getMessage());
        }
        return SUCCESS;
    }
}
