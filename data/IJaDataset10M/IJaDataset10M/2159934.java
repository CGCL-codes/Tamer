package nuts.exts.struts2.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * <!-- START SNIPPET: javadoc --> Renders an HTML file input element. <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;s:file name=&quot;anUploadFile&quot; accept=&quot;text/*&quot; /&gt;
 * &lt;s:file name=&quot;anohterUploadFIle&quot; accept=&quot;text/html,text/plain&quot; /&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
@StrutsTag(name = "uploader", tldTagClass = "nuts.exts.struts2.views.jsp.ui.UploaderTag", description = "Render a uploader input field", allowDynamicAttributes = true)
public class Uploader extends UIBean {

    private static final String TEMPLATE = "n-uploader";

    protected String accept;

    protected String readonly;

    protected String size;

    /**
	 * Constructor
	 * 
	 * @param stack value stack
	 * @param request request
	 * @param response response
	 */
    public Uploader(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    /**
	 * evaluate parameters
	 */
    public void evaluateParams() {
        super.evaluateParams();
        if (accept != null) {
            addParameter("accept", findString(accept));
        }
        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }
        if (size != null) {
            addParameter("size", findString(size));
        }
    }

    /**
	 * @param accept the accept to set
	 */
    @StrutsTagAttribute(description = "HTML accept attribute to indicate accepted file mimetypes")
    public void setAccept(String accept) {
        this.accept = accept;
    }

    /**
     * @param readonly the readonly to set
     */
    @StrutsTagAttribute(description = "Whether the input is readonly", type = "Boolean", defaultValue = "false")
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    /**
	 * @param size the size to set
	 */
    @StrutsTagAttribute(description = "HTML size attribute", required = false, type = "Integer")
    public void setSize(String size) {
        this.size = size;
    }
}
