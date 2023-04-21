package nuts.exts.struts2.views.velocity.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.exts.struts2.components.TextArea;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.velocity.components.AbstractDirective;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see TextArea
 */
public class TextAreaDirective extends AbstractDirective {

    protected Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new TextArea(stack, req, res);
    }

    /**
     * @return "textarea"
     */
    public String getBeanName() {
        return "textarea";
    }
}
