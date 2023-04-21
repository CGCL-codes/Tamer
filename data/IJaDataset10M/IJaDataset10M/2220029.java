package br.com.pleno.gp.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AgrupamentoDemandaPK implements Serializable {

    @Column(name = "DEMANDA_ID", nullable = false)
    private int demandaId;

    @Column(name = "AGRUPAMENTO_ID", nullable = false)
    private int agrupamentoId;

    /** Creates a new instance of ProjetoPessoaPapelPK */
    public AgrupamentoDemandaPK() {
    }

    public AgrupamentoDemandaPK(int demandaId, int agrupamentoId) {
        this.demandaId = demandaId;
        this.agrupamentoId = agrupamentoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) getDemandaId();
        hash += (int) getAgrupamentoId();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AgrupamentoDemandaPK)) {
            return false;
        }
        AgrupamentoDemandaPK other = (AgrupamentoDemandaPK) object;
        if (this.getDemandaId() != other.getDemandaId()) return false;
        if (this.getAgrupamentoId() != other.getAgrupamentoId()) return false;
        return true;
    }

    /**
     * @return the demandaId
     */
    public int getDemandaId() {
        return demandaId;
    }

    /**
     * @param demandaId the demandaId to set
     */
    public void setDemandaId(int demandaId) {
        this.demandaId = demandaId;
    }

    /**
     * @return the agrupamentoId
     */
    public int getAgrupamentoId() {
        return agrupamentoId;
    }

    /**
     * @param agrupamentoId the agrupamentoId to set
     */
    public void setAgrupamentoId(int agrupamentoId) {
        this.agrupamentoId = agrupamentoId;
    }
}
