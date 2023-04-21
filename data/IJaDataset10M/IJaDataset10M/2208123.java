package br.ind.nikon.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("rolo")
public class Rolo extends AbstractItemPedidoAmbiente {

    private Double montagem;

    private String puxadores;

    public Double getMontagem() {
        return montagem;
    }

    public void setMontagem(Double montagem) {
        this.montagem = montagem;
    }

    public String getPuxadores() {
        return puxadores;
    }

    public void setPuxadores(String puxadores) {
        this.puxadores = puxadores;
    }

    @Override
    public boolean hasBlackOut() {
        return false;
    }
}
