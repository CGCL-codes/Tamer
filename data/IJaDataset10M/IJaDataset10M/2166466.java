package javax.faces.webapp;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Manfred Geiler (latest modification by $Author: slessard $)
 * @version $Revision: 701829 $ $Date: 2008-10-05 12:06:02 -0500 (Sun, 05 Oct 2008) $
 * 
 * @since 1.2
 */
public abstract class ConverterELTag extends TagSupport {

    private static final long serialVersionUID = -616834506829108081L;

    @Override
    public int doStartTag() throws JspException {
        UIComponentClassicTagBase componentTag = UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
        if (componentTag == null) {
            throw new JspException("no parent UIComponentTag found");
        }
        if (!componentTag.getCreated()) {
            return Tag.SKIP_BODY;
        }
        UIComponent component = componentTag.getComponentInstance();
        if (component == null) {
            throw new JspException("parent UIComponentTag has no UIComponent");
        }
        if (!(component instanceof ValueHolder)) {
            throw new JspException("UIComponent is no ValueHolder");
        }
        Converter converter = createConverter();
        ((ValueHolder) component).setConverter(converter);
        return Tag.SKIP_BODY;
    }

    protected abstract Converter createConverter() throws JspException;
}
