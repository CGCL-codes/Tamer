package nuts.ext.struts2.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * <!-- START SNIPPET: javadoc -->
 * Renders an HTML input element of type checkbox, populated by the specified property from the ValueStack.
 * <!-- END SNIPPET: javadoc -->
 *
 * <p/> <b>Examples</b>
 *
 * <pre>
 * <!-- START SNIPPET: example -->
 * JSP:
 * &lt;s:checkbox label="checkbox test" name="checkboxField1" value="aBoolean" fieldValue="true"/&gt;
 *
 * Velocity:
 * #tag( Checkbox "label=checkbox test" "name=checkboxField1" "value=aBoolean" )
 *
 * Resulting HTML (simple template, aBoolean == true):
 * &lt;input type="checkbox" name="checkboxField1" value="true" checked="checked" /&gt;
 *
 * <!-- END SNIPPET: example -->
 * </pre>
 *
 */
@StrutsTag(name = "checkbox", tldTagClass = "nuts.ext.struts2.views.jsp.ui.CheckboxTag", description = "Render a checkbox input field", allowDynamicAttributes = true)
public class Checkbox extends UIBean {

    /**
	 * TEMPLATE = "n-checkbox";
	 */
    public static final String TEMPLATE = "n-checkbox";

    protected String fieldValue;

    /**
     * Constructor
     * 
     * @param stack value stack
     * @param request request
     * @param response response
     */
    public Checkbox(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    protected void evaluateExtraParams() {
        if (fieldValue != null) {
            addParameter("fieldValue", findString(fieldValue));
        } else {
            addParameter("fieldValue", "true");
        }
    }

    protected Class getValueClassType() {
        return Boolean.class;
    }

    /**
     * @param fieldValue the fieldValue to set
     */
    @StrutsTagAttribute(description = "The actual HTML value attribute of the checkbox.", defaultValue = "true")
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
