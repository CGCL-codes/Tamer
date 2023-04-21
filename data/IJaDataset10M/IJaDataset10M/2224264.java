package it.uniromadue.portaleuni.form;

import it.uniromadue.portaleuni.base.BaseForm;
import it.uniromadue.portaleuni.dto.Utenti;
import java.util.ArrayList;

public class DateEsamiForm extends BaseForm {

    private ArrayList listaDateEsami;

    private Utenti utenteLoggato;

    private int loggato;

    public ArrayList getListaDateEsami() {
        return listaDateEsami;
    }

    public void setListaDateEsami(ArrayList listaDateEsami) {
        this.listaDateEsami = listaDateEsami;
    }

    public int getLoggato() {
        return loggato;
    }

    public void setLoggato(int loggato) {
        this.loggato = loggato;
    }

    public Utenti getUtenteLoggato() {
        return utenteLoggato;
    }

    public void setUtenteLoggato(Utenti utenteLoggato) {
        this.utenteLoggato = utenteLoggato;
    }
}
