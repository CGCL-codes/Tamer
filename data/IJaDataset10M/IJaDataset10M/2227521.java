package edu.unibi.agbi.biodwh.entity.uniprot;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UniprotOrdLocNamesId implements java.io.Serializable {

    private static final long serialVersionUID = 5370256280976573958L;

    private String ordLocNames;

    private String uniprotId;

    public UniprotOrdLocNamesId() {
    }

    public UniprotOrdLocNamesId(String ordLocNames, String uniprotId) {
        this.ordLocNames = ordLocNames;
        this.uniprotId = uniprotId;
    }

    @Column(name = "ord_loc_names", nullable = false, length = 64)
    public String getOrdLocNames() {
        return this.ordLocNames;
    }

    public void setOrdLocNames(String ordLocNames) {
        this.ordLocNames = ordLocNames;
    }

    @Column(name = "uniprot_id", nullable = false, length = 16)
    public String getUniprotId() {
        return this.uniprotId;
    }

    public void setUniprotId(String uniprotId) {
        this.uniprotId = uniprotId;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof UniprotOrdLocNamesId)) return false;
        UniprotOrdLocNamesId castOther = (UniprotOrdLocNamesId) other;
        return ((this.getOrdLocNames() == castOther.getOrdLocNames()) || (this.getOrdLocNames() != null && castOther.getOrdLocNames() != null && this.getOrdLocNames().equals(castOther.getOrdLocNames()))) && ((this.getUniprotId() == castOther.getUniprotId()) || (this.getUniprotId() != null && castOther.getUniprotId() != null && this.getUniprotId().equals(castOther.getUniprotId())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getOrdLocNames() == null ? 0 : this.getOrdLocNames().hashCode());
        result = 37 * result + (getUniprotId() == null ? 0 : this.getUniprotId().hashCode());
        return result;
    }
}
