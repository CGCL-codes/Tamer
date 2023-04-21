package br.gov.demoiselle.escola.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import br.gov.framework.demoiselle.core.bean.IPojo;

@Entity
@Table(name = "professor")
public class Professor implements IPojo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "sq_professor")
    @Column(name = "id_professor")
    private Long id;

    @Column(name = "nome", length = 100)
    private String nome;

    public Professor(Long id) {
        this.id = id;
    }

    public Professor(String nome) {
        this.nome = nome;
    }

    public Professor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
