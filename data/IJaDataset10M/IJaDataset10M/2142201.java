package com.financialcontrol.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Leonardo
 */
@Embeddable
public class ParcelaLancamentoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "nu_lancamento")
    private int nuLancamento;

    @Basic(optional = false)
    @Column(name = "nu_parcela_lancamento")
    private int nuParcelaLancamento;

    public ParcelaLancamentoPK() {
    }

    public ParcelaLancamentoPK(int nuLancamento, int nuParcelaLancamento) {
        this.nuLancamento = nuLancamento;
        this.nuParcelaLancamento = nuParcelaLancamento;
    }

    public int getNuLancamento() {
        return nuLancamento;
    }

    public void setNuLancamento(int nuLancamento) {
        this.nuLancamento = nuLancamento;
    }

    public int getNuParcelaLancamento() {
        return nuParcelaLancamento;
    }

    public void setNuParcelaLancamento(int nuParcelaLancamento) {
        this.nuParcelaLancamento = nuParcelaLancamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) nuLancamento;
        hash += (int) nuParcelaLancamento;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ParcelaLancamentoPK)) {
            return false;
        }
        ParcelaLancamentoPK other = (ParcelaLancamentoPK) object;
        if (this.nuLancamento != other.nuLancamento) {
            return false;
        }
        if (this.nuParcelaLancamento != other.nuParcelaLancamento) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.financialcontrol.db.ParcelaLancamentoPK[nuLancamento=" + nuLancamento + ", nuParcelaLancamento=" + nuParcelaLancamento + "]";
    }
}
