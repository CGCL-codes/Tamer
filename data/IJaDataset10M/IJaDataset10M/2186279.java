package jamm.webapp;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @struts.form name="updateAccountForm"
 */
public class UpdateAccountForm extends ActionForm {

    /**
     * The common name
     * 
     * @return a string
     */
    public String getCommonName() {
        return mCommonName;
    }

    /**
     * The e-mail address.
     * 
     * @return a String
     */
    public String getMail() {
        return mMail;
    }

    /**
     * Set common name.
     * 
     * @param string a string with common name or null
     */
    public void setCommonName(String string) {
        mCommonName = string;
    }

    /**
     * Set mail
     * 
     * @param string with the e-mail address
     */
    public void setMail(String string) {
        mMail = string;
    }

    /**
     * Resets the form to the default value.  It gets the value for
     * mail from the http request, and sets Mail to the attribute in the
     * request. 
     *
     * @param mapping an <code>ActionMapping</code> used to select
     *                this instance
     * @param request a <code>HttpServletRequest</code> that is being
     *                processed
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        mMail = (String) request.getAttribute("mail");
        mCommonName = "";
    }

    /** Common Name */
    private String mCommonName;

    /** e-Mail */
    private String mMail;
}
