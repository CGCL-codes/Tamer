package pl.nni.msz.struts.form.list;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ListCompaniesForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Collection companies;

    public Collection getCompanies() {
        return companies;
    }

    public void setCompanies(Collection companies) {
        this.companies = companies;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        companies = new ArrayList();
    }
}
