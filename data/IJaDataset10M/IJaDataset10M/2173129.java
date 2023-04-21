package metso.paradigma.core.business.model;

import java.io.Serializable;
import java.util.Date;

public class LimitazioneTurno implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 219852344460938307L;

    private String nome;

    private String tipo;

    private Date dataInizio;

    private Date dataFine;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof LimitazioneTurno) {
            LimitazioneTurno turno = (LimitazioneTurno) obj;
            return (((getNome() == turno.getNome()) || (getNome() != null && getNome().equals(turno.getNome()))));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.getNome()).hashCode();
    }
}
