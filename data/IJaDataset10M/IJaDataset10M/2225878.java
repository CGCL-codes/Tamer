package Business;

import org.apache.struts.action.ActionForm;

public class ResponsableForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String email;

    private String cursus;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCursus() {
        return cursus;
    }

    public void setCursus(String cursus) {
        this.cursus = cursus;
    }

    public ResponsableForm() {
    }

    public ResponsableForm(String email, String cursus) {
        this.cursus = cursus;
        this.email = email;
    }
}
