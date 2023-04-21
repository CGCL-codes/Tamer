package modelo;

import java.io.Serializable;

/**
 * 
 * 
 * @hibernate.class
 *
 */
public class Ocorrencia_discriminacao implements Serializable {

    private String nome;

    private Long id;

    /**
	 * @hibernate.id 
	 * generator-class = "native"
	 * column = "id"
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @hibernate.property
	 */
    public String getNome() {
        return nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
