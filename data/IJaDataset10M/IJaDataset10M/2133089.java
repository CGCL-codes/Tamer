package br.com.sinapp.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Modelo da tabela Tipo de Usuario
 * 
 * @author Fabiomrodriguez
 */
@Entity
@Table(name = "tipo_usuario")
public class TipoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cd_usuario")
    private Character id;

    @Column(name = "ds_usuario")
    private String nome;

    /**
	 * @return the id
	 */
    public Character getId() {
        return this.id;
    }

    /**
	 * @param id
	 *            the id to set
	 */
    public void setId(Character id) {
        this.id = id;
    }

    /**
	 * @return the nome
	 */
    public String getNome() {
        return this.nome;
    }

    /**
	 * @param nome
	 *            the nome to set
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }
}
