package br.com.sinapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "documento")
@SequenceGenerator(name = "sq_documento", sequenceName = "sq_documento")
public class Documento implements Serializable {

    @Id
    @GeneratedValue(generator = "sq_documento", strategy = GenerationType.SEQUENCE)
    @Column(name = "cd_documento")
    private Long id;

    @Column(name = "ds_documento")
    private String descricao;

    @Transient
    @JoinColumn(name = "cd_documento", nullable = false)
    private List<EtapaProcesso> etapasProcesso = new ArrayList<EtapaProcesso>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<EtapaProcesso> getEtapasProcesso() {
        return etapasProcesso;
    }

    public void setEtapasProcesso(List<EtapaProcesso> etapasProcesso) {
        this.etapasProcesso = etapasProcesso;
    }
}
