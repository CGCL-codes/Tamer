package edu.unibi.agbi.dawismd.entities.biodwh.kegg.medicus.drug;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "kegg_drug_target")
@Table(name = "kegg_drug_target")
public class KeggDrugTarget implements java.io.Serializable {

    private static final long serialVersionUID = 4126073657099768561L;

    private Integer id;

    private KeggDrug keggDrug;

    private String target;

    public KeggDrugTarget() {
    }

    public KeggDrugTarget(KeggDrug keggDrug, String target) {
        this.keggDrug = keggDrug;
        this.target = target;
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
    public KeggDrug getKeggDrug() {
        return this.keggDrug;
    }

    public void setKeggDrug(KeggDrug keggDrug) {
        this.keggDrug = keggDrug;
    }

    @Column(name = "target", nullable = false, length = 384)
    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
