package it.unipg.bipod.web;

import javax.faces.bean.*;
import java.util.regex.*;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;

/**
 * MainBean e' il managed bean "radice" per BiPoD.<br>
 * Tiene traccia dell'utente che ha effettuato il login al sistema e
 * attraverso la sua proprietà loginBean permette di effettuare operazioni
 * di autenticazione al sistema. Una volta che l'utente si è autenticato
 * tutte le operazioni specifiche a docente e segretario vengono svolte rispettivamente
 * dai due Bean pubblici: docente (di tipo {@link DocenteBean}) e segretario (di tipo
 * {@link SegretarioBean}).<br>
 * Eventuali messaggi o errori vengono segnalati mediante i metodi forniti da DefaultBean.
 * 
 * @author Lorenzo Porzi
 * @see DocenteBean
 * @see SegretarioBean
 * @see LoginBean
 * @see DefaultBean
 */
@ManagedBean
@SessionScoped
public class MainBean extends DefaultBean {

    private LoginBean login;

    private DocenteBean docente;

    private SegretarioBean segretario;

    private Pattern emailPattern;

    /**
	 * Imposta il LoginBean standard e iniziallizza a {@code null} docente e segretario.
	 */
    public MainBean() {
        super();
        this.login = new LoginBean(this);
        this.docente = null;
        this.segretario = null;
        this.emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
    }

    public void setDocente(DocenteBean docente) {
        this.docente = docente;
    }

    public DocenteBean getDocente() {
        return docente;
    }

    public void setSegretario(SegretarioBean segretario) {
        this.segretario = segretario;
    }

    public SegretarioBean getSegretario() {
        return segretario;
    }

    public LoginBean getLogin() {
        return login;
    }

    /**
	 * Metodo che reindirizza all'indice nel caso che l'utente non sia autenticato.
	 * 
	 * @return {@code true} se l'utente e' autenticato, {@code false} altrimenti.
	 * @throws IOException
	 */
    public boolean getLoginEseguito() throws IOException {
        if (docente != null || segretario != null) return true;
        return false;
    }

    /**
	 * Controlla che il valore del componente da validare sia un indirizzo email valido.
	 * 
	 * @param context il contesto di faces corrente.
	 * @param componentToValidate il componente da validare.
	 * @param value il valore da controllare.
	 * @throws ValidatorException nel caso in cui la validazione dia risultato negativo.
	 */
    public void validaEmail(FacesContext context, UIComponent componentToValidate, Object value) {
        String email = ((String) value).trim();
        Matcher match = emailPattern.matcher(email);
        if (!match.matches()) throw new ValidatorException(new FacesMessage(getSettings().getProperty("msgEmailMalformata")));
    }

    /**
	 * Resetta a null i campi docente e segretario, effettivamente seseguendo il logout
	 * per l'utente corrente.
	 */
    public void resetFields() {
        this.docente = null;
        this.segretario = null;
    }
}
