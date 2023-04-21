package it.ilz.hostingjava.actionform.paypal;

import it.ilz.hostingjava.backend.paypal.Controlli;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author andrea
 * @version
 */
public class AcquistaAutorizzato extends Ip {

    private String importo = "0.0";

    private String first_name = "";

    private String last_name = "";

    private String valuta = "";

    private String dominio = "";

    private String account = "";

    private int tipo = 0;

    public String getFirstname() {
        return first_name;
    }

    public void setFirstname(String s) {
        first_name = s;
    }

    public String getLastname() {
        return last_name;
    }

    public void setLastname(String s) {
        last_name = s;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String s) {
        dominio = s;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String s) {
        account = s;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String s) {
        valuta = s;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int i) {
        tipo = i;
    }

    public String getImporto() {
        return importo;
    }

    public void setImporto(String s) {
        importo = s;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (!Controlli.richiesta(this) || getAccount() == null || getAccount().length() < 1) {
            errors.add("account", new ActionMessage("error.account.required"));
        }
        if (getFirstname() == null || getFirstname().length() < 1) {
            errors.add("firstname", new ActionMessage("error.nome.required"));
        }
        if (getLastname() == null || getLastname().length() < 1) {
            errors.add("lastname", new ActionMessage("error.cognome.required"));
        }
        return errors;
    }

    private static Log logger = LogFactory.getLog(AcquistaAutorizzato.class);
}
