package nuts.exts.struts2.views.velocity.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.exts.struts2.components.Submit;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.velocity.components.AbstractDirective;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see Submit
 */
public class SubmitDirective extends AbstractDirective {

    /**
	 * @return "submit"
	 */
    public String getBeanName() {
        return "submit";
    }

    protected Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Submit(stack, req, res);
    }
}
