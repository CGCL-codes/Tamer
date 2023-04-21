package ro.gateway.aida.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author Mihai Postelnicu<p>
 *	Used by admin listers
 *  e-mail (mihai@ro-gateway.org)<br>
 * (c) 2003 by eRomania Gateway<p>
 */
public class CountryFilterForm extends ActionForm {

    private String iso3;

    /**
     * Method reset
     * @param ActionMapping mapping
     * @param HttpServletRequest request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        iso3 = "";
    }

    /**
	 * @return
	 */
    public String getIso3() {
        return iso3;
    }

    /**
	 * @param string
	 */
    public void setIso3(String string) {
        iso3 = string;
    }
}
