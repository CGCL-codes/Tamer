package nuts.exts.struts2.views.freemarker.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.exts.struts2.components.ViewField;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.freemarker.tags.TagModel;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see ViewField
 */
public class ViewFieldModel extends TagModel {

    /**
     * Constructor
     * 
     * @param stack ValueStack 
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     */
    public ViewFieldModel(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        super(stack, req, res);
    }

    protected Component getBean() {
        return new ViewField(stack, req, res);
    }
}
