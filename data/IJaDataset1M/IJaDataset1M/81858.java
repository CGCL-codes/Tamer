package edu.unibi.agbi.biodwh.entity.hprd;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "hprd_genetic_diseases_ref")
@Table(name = "hprd_genetic_diseases_ref")
public class HprdGeneticDiseasesRef implements java.io.Serializable {

    private static final long serialVersionUID = -4330506905884120132L;

    private HprdGeneticDiseasesRefId id;

    private HprdGeneticDiseases hprdGeneticDiseases;

    public HprdGeneticDiseasesRef() {
    }

    public HprdGeneticDiseasesRef(HprdGeneticDiseasesRefId id, HprdGeneticDiseases hprdGeneticDiseases) {
        this.id = id;
        this.hprdGeneticDiseases = hprdGeneticDiseases;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false)), @AttributeOverride(name = "refId", column = @Column(name = "ref_id", nullable = false)) })
    public HprdGeneticDiseasesRefId getId() {
        return this.id;
    }

    public void setId(HprdGeneticDiseasesRefId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false, insertable = false, updatable = false)
    public HprdGeneticDiseases getHprdGeneticDiseases() {
        return this.hprdGeneticDiseases;
    }

    public void setHprdGeneticDiseases(HprdGeneticDiseases hprdGeneticDiseases) {
        this.hprdGeneticDiseases = hprdGeneticDiseases;
    }
}
