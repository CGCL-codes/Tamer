package br.ita.doacoes.domain.cadastrodoacoes;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author Guilherme Rodrigues Salerno
 * @since 15/11/2007 Classe que mapeia a Pessoa Jur�dica, herdando de Pessoa
 */
@Entity
@Table(name = "pessoa_juridica")
@DiscriminatorValue("JURIDICA")
public class PessoaJuridica extends Pessoa {

    private String cnpj;

    private String nome_contato;

    @Basic
    @Column(name = "CNPJ")
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Basic
    @Column(name = "nome_contato")
    public String getNome_contato() {
        return nome_contato;
    }

    public void setNome_contato(String nome_contato) {
        this.nome_contato = nome_contato;
    }

    @Override
    public String toString() {
        return getNome() + " - CNPJ : " + cnpj;
    }
}
