package edu.unibi.agbi.dawismd.entities.biodwh.hprd;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HprdGoCellularComponentTermRefId implements java.io.Serializable {

    private static final long serialVersionUID = 5980218443619406547L;

    private int id;

    private int refId;

    public HprdGoCellularComponentTermRefId() {
    }

    public HprdGoCellularComponentTermRefId(int id, int refId) {
        this.id = id;
        this.refId = refId;
    }

    @Column(name = "id", nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "ref_id", nullable = false)
    public int getRefId() {
        return this.refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof HprdGoCellularComponentTermRefId)) return false;
        HprdGoCellularComponentTermRefId castOther = (HprdGoCellularComponentTermRefId) other;
        return (this.getId() == castOther.getId()) && (this.getRefId() == castOther.getRefId());
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getId();
        result = 37 * result + this.getRefId();
        return result;
    }
}
