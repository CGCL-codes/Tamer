package edu.unibi.agbi.biodwh.entity.hprd;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HprdProteinComplexesExperimentTypeId implements java.io.Serializable {

    private static final long serialVersionUID = -3785320128403314420L;

    private int id;

    private String experimentType;

    public HprdProteinComplexesExperimentTypeId() {
    }

    public HprdProteinComplexesExperimentTypeId(int id, String experimentType) {
        this.id = id;
        this.experimentType = experimentType;
    }

    @Column(name = "id", nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "experiment_type", nullable = false)
    public String getExperimentType() {
        return this.experimentType;
    }

    public void setExperimentType(String experimentType) {
        this.experimentType = experimentType;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof HprdProteinComplexesExperimentTypeId)) return false;
        HprdProteinComplexesExperimentTypeId castOther = (HprdProteinComplexesExperimentTypeId) other;
        return (this.getId() == castOther.getId()) && ((this.getExperimentType() == castOther.getExperimentType()) || (this.getExperimentType() != null && castOther.getExperimentType() != null && this.getExperimentType().equals(castOther.getExperimentType())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getId();
        result = 37 * result + (getExperimentType() == null ? 0 : this.getExperimentType().hashCode());
        return result;
    }
}
