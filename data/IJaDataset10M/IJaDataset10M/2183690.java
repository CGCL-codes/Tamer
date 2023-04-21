package br.com.progepe.entity;

import java.io.Serializable;

public class DependenteRessarcimentoSaude implements Serializable {

    private static final long serialVersionUID = -6453895713062165271L;

    private Long codigo;

    private byte[] carteirinha;

    private RessarcimentoSaude ressarcimentoSaude;

    private Conjuge conjuge;

    private Dependente dependente;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public byte[] getCarteirinha() {
        return carteirinha;
    }

    public void setCarteirinha(byte[] carteirinha) {
        this.carteirinha = carteirinha;
    }

    public RessarcimentoSaude getRessarcimentoSaude() {
        return ressarcimentoSaude;
    }

    public void setRessarcimentoSaude(RessarcimentoSaude ressarcimentoSaude) {
        this.ressarcimentoSaude = ressarcimentoSaude;
    }

    public Conjuge getConjuge() {
        return conjuge;
    }

    public void setConjuge(Conjuge conjuge) {
        this.conjuge = conjuge;
    }

    public Dependente getDependente() {
        return dependente;
    }

    public void setDependente(Dependente dependente) {
        this.dependente = dependente;
    }
}
