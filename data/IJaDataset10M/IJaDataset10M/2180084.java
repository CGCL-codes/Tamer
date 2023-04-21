package com.copsearch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Coment�rio
 *
 * @author Jo�o Paulo (joao_fernandes@netel.trana.com.br)
 * 
 * @since 20/05/2009
 */
@Entity
@Table(name = "veiculo")
public class Veiculo {

    private Long id;

    private String codigo;

    private Localizacao localizacao;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "codigo")
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @OneToOne(cascade = {  })
    @JoinColumn(name = "id_localizacao", unique = true, nullable = false, insertable = true, updatable = true)
    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }
}
