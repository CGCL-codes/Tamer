package edu.unibi.agbi.biodwh.entity.kegg.genes;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KeggGenesDiseaseId implements java.io.Serializable {

    private static final long serialVersionUID = 7401667035850078687L;

    private int id;

    private String disease;

    public KeggGenesDiseaseId() {
    }

    public KeggGenesDiseaseId(int id, String disease) {
        this.id = id;
        this.disease = disease;
    }

    @Column(name = "id", nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "disease", nullable = false)
    public String getDisease() {
        return this.disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof KeggGenesDiseaseId)) return false;
        KeggGenesDiseaseId castOther = (KeggGenesDiseaseId) other;
        return (this.getId() == castOther.getId()) && ((this.getDisease() == castOther.getDisease()) || (this.getDisease() != null && castOther.getDisease() != null && this.getDisease().equals(castOther.getDisease())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getId();
        result = 37 * result + (getDisease() == null ? 0 : this.getDisease().hashCode());
        return result;
    }
}
