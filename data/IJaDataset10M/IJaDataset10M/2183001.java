package edu.unibi.agbi.biodwh.entity.kegg.genes.orthology;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "kegg_ko_class")
@Table(name = "kegg_ko_class")
public class KeggKoClass implements java.io.Serializable {

    private static final long serialVersionUID = -4730316932927332787L;

    private Integer id;

    private KeggKo keggKo;

    private String koClass;

    public KeggKoClass() {
    }

    public KeggKoClass(KeggKo keggKo, String koClass) {
        this.keggKo = keggKo;
        this.koClass = koClass;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry", nullable = false)
    public KeggKo getKeggKo() {
        return this.keggKo;
    }

    public void setKeggKo(KeggKo keggKo) {
        this.keggKo = keggKo;
    }

    @Column(name = "ko_class", nullable = false)
    public String getKoClass() {
        return this.koClass;
    }

    public void setKoClass(String koClass) {
        this.koClass = koClass;
    }
}
