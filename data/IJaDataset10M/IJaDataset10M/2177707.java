package net.sf.jwan.jsf.html.tag;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HtmlCssTag extends UIComponentELTag {

    static Log logger = LogFactory.getLog(HtmlCssTag.class);

    private ValueExpression href = null;

    public String getRendererType() {
        return "net.sf.jwan.HtmlCss";
    }

    public String getComponentType() {
        return "net.sf.jwan.HtmlCss";
    }

    public void setProperties(UIComponent component) {
        super.setProperties(component);
        component.setValueExpression("href", href);
    }

    public void release() {
        super.release();
        href = null;
    }

    public void setHref(ValueExpression href) {
        this.href = href;
    }
}
