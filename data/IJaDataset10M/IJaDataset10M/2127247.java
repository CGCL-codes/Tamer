package nuts.exts.struts2.views.velocity.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.exts.struts2.components.Pager;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.velocity.components.AbstractDirective;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see Pager
 */
public class PagerDirective extends AbstractDirective {

    protected Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Pager(stack, req, res);
    }

    /**
     * @return "pager"
     */
    public String getBeanName() {
        return "pager";
    }
}
