package org.bpmsuite.validator.taglib;

import javax.servlet.jsp.tagext.BodyTagSupport;
import org.bpmsuite.struts.StrutsPortletForm;

/**
 * @author Dirk Weiser
 */
public class HasNotErrorsTag extends BodyTagSupport {

    private static final long serialVersionUID = 5064987782223469354L;

    public HasNotErrorsTag() {
    }

    public int doStartTag() {
        FormValidatorTag formValidatorTag = (FormValidatorTag) findAncestorWithClass(this, FormValidatorTag.class);
        StrutsPortletForm strutsPortletForm = formValidatorTag.getFrontControlsForm();
        if (formValidatorTag != null && strutsPortletForm == null) {
            return EVAL_BODY_INCLUDE;
        } else if (formValidatorTag != null && strutsPortletForm != null && (strutsPortletForm.getValidationErrors() == null || strutsPortletForm.getValidationErrors().size() == 0)) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }
}
