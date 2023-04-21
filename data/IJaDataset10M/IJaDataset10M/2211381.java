package javax.faces.webapp;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Manfred Geiler (latest modification by $Author: slessard $)
 * @version $Revision: 701829 $ $Date: 2008-10-05 12:06:02 -0500 (Sun, 05 Oct 2008) $
 */
public abstract class ValidatorELTag extends TagSupport {

    private static final long serialVersionUID = 8794036166323016663L;

    @Override
    public int doStartTag() throws JspException {
        UIComponentClassicTagBase componentTag = UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
        if (componentTag == null) {
            throw new JspException("no parent UIComponentTag found");
        }
        if (!componentTag.getCreated()) {
            return Tag.SKIP_BODY;
        }
        Validator validator = createValidator();
        UIComponent component = componentTag.getComponentInstance();
        if (component == null) {
            throw new JspException("parent UIComponentTag has no UIComponent");
        }
        if (!(component instanceof EditableValueHolder)) {
            throw new JspException("UIComponent is no EditableValueHolder");
        }
        ((EditableValueHolder) component).addValidator(validator);
        return Tag.SKIP_BODY;
    }

    protected abstract Validator createValidator() throws JspException;
}
