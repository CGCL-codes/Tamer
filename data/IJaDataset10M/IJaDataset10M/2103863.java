package nuts.exts.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.exts.struts2.components.Password;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.TextFieldTag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see Password
 */
@SuppressWarnings("serial")
public class PasswordTag extends TextFieldTag {

    protected String showPassword;

    /**
	 * @see org.apache.struts2.views.jsp.ui.TextFieldTag#getBean(com.opensymphony.xwork2.util.ValueStack,
	 *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Password(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();
        ((Password) component).setShowPassword(showPassword);
    }

    /**
	 * @param showPassword the showPassword to set
	 */
    public void setShow(String showPassword) {
        this.showPassword = showPassword;
    }

    /**
	 * @param showPassword the showPassword to set
	 */
    public void setShowPassword(String showPassword) {
        this.showPassword = showPassword;
    }
}
