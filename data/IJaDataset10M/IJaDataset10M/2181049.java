package com.knwebapp.lctweb.products.ibmwebsphereappser;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.validation.Errors;
import com.knbase.struts.form.BaseForm;

public class Form extends BaseForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        Errors errors = this.getDataBinder().getErrors();
        while (errors.hasErrors()) {
            errors.getFieldError();
        }
        ActionErrors actionErrors = new ActionErrors();
        if (errors.hasErrors()) {
            ActionMessages message = new ActionMessages();
            message.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errors.toString()));
            actionErrors.add(message);
        }
        return actionErrors;
    }
}
