package br.net.woodstock.rockframework.web.test;

import java.io.Serializable;
import java.util.Collection;

public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nome;

    private Collection<Pessoa> pessoas;

    public Empresa() {
        super();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public Collection<Pessoa> getPessoas() {
        return this.pessoas;
    }

    public void setPessoas(final Collection<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }
}
