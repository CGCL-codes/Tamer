package Business;

import org.apache.struts.action.ActionForm;

public class CursusForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public CursusForm() {
    }

    public CursusForm(String nom) {
        this.nom = nom;
    }
}
