package aportal.form.miscellaneous;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  Administrator
 */
public class MessagesForm extends ActionForm {

    java.util.Vector vecIn = null;

    String storeSession = "no";

    /** Creates a new instance of MessagesForm */
    public MessagesForm() {
    }

    /** Getter for property vecIn.
     * @return Value of property vecIn.
     *
     */
    public java.util.Vector getVecIn() {
        return vecIn;
    }

    /** Setter for property vecIn.
     * @param vecIn New value of property vecIn.
     *
     */
    public void setVecIn(java.util.Vector vecIn) {
        this.vecIn = vecIn;
    }

    /** Getter for property storeSession.
     * @return Value of property storeSession.
     *
     */
    public java.lang.String getStoreSession() {
        return storeSession;
    }

    /** Setter for property storeSession.
     * @param storeSession New value of property storeSession.
     *
     */
    public void setStoreSession(java.lang.String storeSession) {
        this.storeSession = storeSession;
    }
}
