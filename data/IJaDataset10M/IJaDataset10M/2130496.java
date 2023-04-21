package edu.unibi.agbi.dawismd.entities.biodwh.hprd;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "hprd_protein_nonprotein_interactions_experiment_type")
@Table(name = "hprd_protein_nonprotein_interactions_experiment_type")
public class HprdProteinNonproteinInteractionsExperimentType implements java.io.Serializable {

    private static final long serialVersionUID = -4745606813221143602L;

    private HprdProteinNonproteinInteractionsExperimentTypeId id;

    private HprdProteinNonproteinInteractions hprdProteinNonproteinInteractions;

    public HprdProteinNonproteinInteractionsExperimentType() {
    }

    public HprdProteinNonproteinInteractionsExperimentType(HprdProteinNonproteinInteractionsExperimentTypeId id, HprdProteinNonproteinInteractions hprdProteinNonproteinInteractions) {
        this.id = id;
        this.hprdProteinNonproteinInteractions = hprdProteinNonproteinInteractions;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false)), @AttributeOverride(name = "experimentType", column = @Column(name = "experiment_type", nullable = false)) })
    public HprdProteinNonproteinInteractionsExperimentTypeId getId() {
        return this.id;
    }

    public void setId(HprdProteinNonproteinInteractionsExperimentTypeId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false, insertable = false, updatable = false)
    public HprdProteinNonproteinInteractions getHprdProteinNonproteinInteractions() {
        return this.hprdProteinNonproteinInteractions;
    }

    public void setHprdProteinNonproteinInteractions(HprdProteinNonproteinInteractions hprdProteinNonproteinInteractions) {
        this.hprdProteinNonproteinInteractions = hprdProteinNonproteinInteractions;
    }
}
