package br.gov.demoiselle.rh.bean;

import br.gov.framework.demoiselle.core.bean.IPojo;

/**
 * 
 * @author Flávio Gomes da Silva Lisboa
 * @version 0.1
 */
@SuppressWarnings("serial")
public class Departamento implements IPojo {

    private long id;

    private String nome;

    public Departamento() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
