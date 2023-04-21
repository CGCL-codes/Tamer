package nuts.ext.struts2.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * <!-- START SNIPPET: javadoc --> Render HTML textarea tag.</p> <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;s:textarea label=&quot;Comments&quot; name=&quot;comments&quot; cols=&quot;30&quot; rows=&quot;8&quot;/&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
@StrutsTag(name = "textarea", tldTagClass = "nuts.ext.struts2.views.jsp.ui.TextareaTag", description = "Render HTML textarea tag.", allowDynamicAttributes = true)
public class TextArea extends UIBean {

    private static final String TEMPLATE = "textarea";

    protected String cols;

    protected String readonly;

    protected String rows;

    protected String wrap;

    protected String format;

    /**
	 * Constructor
	 * 
	 * @param stack value stack
	 * @param request request
	 * @param response response
	 */
    public TextArea(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    /**
	 * evaluate extra parameters
	 */
    public void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }
        if (cols != null) {
            addParameter("cols", findString(cols));
        }
        if (rows != null) {
            addParameter("rows", findString(rows));
        }
        if (wrap != null) {
            addParameter("wrap", findString(wrap));
        }
        if (format != null) {
            addParameter("format", findString(format));
        }
    }

    /**
	 * @param cols the cols to set
	 */
    @StrutsTagAttribute(description = "HTML cols attribute", type = "Integer")
    public void setCols(String cols) {
        this.cols = cols;
    }

    /**
	 * @param readonly the readonly to set
	 */
    @StrutsTagAttribute(description = "Whether the textarea is readonly", type = "Boolean", defaultValue = "false")
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    /**
	 * @param rows the rows to set
	 */
    @StrutsTagAttribute(description = "HTML rows attribute", type = "Integer")
    public void setRows(String rows) {
        this.rows = rows;
    }

    /**
	 * @param wrap the wrap the set
	 */
    @StrutsTagAttribute(description = "HTML wrap attribute")
    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

    /**
	 * @param format the format to set
	 */
    @StrutsTagAttribute(description = "format attribute", type = "String")
    public void setFormat(String format) {
        this.format = format;
    }
}
