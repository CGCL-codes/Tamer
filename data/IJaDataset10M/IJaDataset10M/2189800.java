package ca.ubc.icapture.genapha.forms;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author BTripp
 */
public class RedirectForm extends ActionForm {

    private String URL;

    public RedirectForm() {
        super();
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
