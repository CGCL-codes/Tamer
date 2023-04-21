package it.uniromadue.portaleuni.form;

import java.util.List;
import it.uniromadue.portaleuni.base.BaseForm;

public class DesignaPresidenteForm extends BaseForm {

    private String esito;

    private List utenti;

    private Integer utenteID;

    /**
	 * @return the esito
	 */
    public String getEsito() {
        return esito;
    }

    /**
	 * @param esito the esito to set
	 */
    public void setEsito(String esito) {
        this.esito = esito;
    }

    /**
	 * @return the utenti
	 */
    public List getUtenti() {
        return utenti;
    }

    /**
	 * @param utenti the utenti to set
	 */
    public void setUtenti(List utenti) {
        this.utenti = utenti;
    }

    /**
	 * @return the utenteID
	 */
    public Integer getUtenteID() {
        return utenteID;
    }

    /**
	 * @param utenteID the utenteID to set
	 */
    public void setUtenteID(Integer utenteID) {
        this.utenteID = utenteID;
    }
}
