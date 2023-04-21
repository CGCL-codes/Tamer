package org.weras.portal.clientes.domain.pessoa;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import org.weras.portal.clientes.domain.ObjetoPersistente;

@Entity
public class Pessoa extends ObjetoPersistente {

    private static final long serialVersionUID = 1L;

    private String nomeOficial;

    private String nomeColoquial;

    private String observacao;

    private Date dataNasc;

    private String email;

    private Endereco endereco;

    public String getNomeColoquial() {
        return nomeColoquial;
    }

    public void setNomeColoquial(String nomeColoquial) {
        this.nomeColoquial = nomeColoquial;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic(optional = false)
    public String getNomeOficial() {
        return nomeOficial;
    }

    public void setNomeOficial(String nomeOficial) {
        this.nomeOficial = nomeOficial;
    }

    @Basic(optional = true)
    @Column(name = Pessoas.CAMPO_DATA_NASCIMENTO)
    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
