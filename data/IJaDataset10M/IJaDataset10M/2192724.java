package br.usp.poli.mfc.struts.bean;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author William T.
 */
public class ListaPendencia implements Serializable {

    private Revista revista;

    private Boolean entrega;

    private Boolean recebimento;

    private Date dt_entrega;

    private Date dt_recebimento;

    public ListaPendencia() {
        this.setEntrega(false);
        this.setRecebimento(false);
    }

    public ListaPendencia(Revista _revista) {
        this();
        this.setRevista(_revista);
    }

    public Revista getRevista() {
        return revista;
    }

    public void setRevista(Revista revista) {
        this.revista = revista;
    }

    public Boolean getEntrega() {
        return entrega;
    }

    public void setEntrega(Boolean entrega) {
        this.entrega = entrega;
    }

    public Boolean getRecebimento() {
        return recebimento;
    }

    public void setRecebimento(Boolean recebimento) {
        this.recebimento = recebimento;
    }

    public Date getDt_entrega() {
        return dt_entrega;
    }

    public void setDt_entrega(Date dt_entrega) {
        this.dt_entrega = dt_entrega;
    }

    public Date getDt_recebimento() {
        return dt_recebimento;
    }

    public void setDt_recebimento(Date dt_recebimento) {
        this.dt_recebimento = dt_recebimento;
    }
}
