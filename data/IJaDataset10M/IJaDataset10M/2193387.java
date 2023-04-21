package br.pucpr.sas.java.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 
 * @author Wellington
 *
 */
@Entity(name = "PROPRIEDADE")
public class Propriedade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_PROPRIEDADE", nullable = false)
    private Long codigo;

    @Column(name = "NOME", length = 80, nullable = false)
    private String nome;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_CLIENTE", nullable = false, insertable = true, updatable = true)
    private Cliente cliente;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        if (codigo == 0) {
            codigo = null;
        }
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
