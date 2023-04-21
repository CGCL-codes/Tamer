package nuts.ext.struts2.views.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.ext.struts2.components.Property;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see Property
 */
public class PropertyTag extends ComponentTagSupport {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = -4233662078530349600L;

    protected String value;

    protected Object rawvalue;

    protected String defaultValue;

    protected String format;

    protected String escape;

    /**
	 * @see org.apache.struts2.views.jsp.ComponentTagSupport#getBean(com.opensymphony.xwork2.util.ValueStack,
	 *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Property(stack);
    }

    protected void populateParams() {
        super.populateParams();
        Property p = (Property) component;
        p.setDefault(defaultValue);
        p.setRawvalue(rawvalue);
        p.setValue(value);
        p.setFormat(format);
        p.setEscape(escape);
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * @param rawvalue the rawvalue to set
	 */
    public void setRawvalue(Object rawvalue) {
        this.rawvalue = rawvalue;
    }

    /**
	 * @param defaultValue the defaultValue to set
	 */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
	 * @param format the format to set
	 */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
	 * @param escape the escape to set
	 */
    public void setEscape(String escape) {
        this.escape = escape;
    }
}
