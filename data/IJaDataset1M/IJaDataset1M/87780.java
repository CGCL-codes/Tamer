package nuts.ext.struts2.views.freemarker.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.ext.struts2.components.Text;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.freemarker.tags.TagModel;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see Text
 */
public class TextModel extends TagModel {

    /**
     * Constructor
     * 
     * @param stack ValueStack 
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     */
    public TextModel(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        super(stack, req, res);
    }

    protected Component getBean() {
        return new Text(stack);
    }
}
