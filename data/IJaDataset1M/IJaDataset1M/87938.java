package edu.unibi.agbi.biodwh.entity.iproclass;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IproclassRefseqId implements java.io.Serializable {

    private static final long serialVersionUID = -6692325433763953707L;

    private String uniprotkbAc;

    private String refseqId;

    public IproclassRefseqId() {
    }

    public IproclassRefseqId(String uniprotkbAc, String refseqId) {
        this.uniprotkbAc = uniprotkbAc;
        this.refseqId = refseqId;
    }

    @Column(name = "uniprotkb_ac", nullable = false)
    public String getUniprotkbAc() {
        return this.uniprotkbAc;
    }

    public void setUniprotkbAc(String uniprotkbAc) {
        this.uniprotkbAc = uniprotkbAc;
    }

    @Column(name = "refseq_id", nullable = false)
    public String getRefseqId() {
        return this.refseqId;
    }

    public void setRefseqId(String refseqId) {
        this.refseqId = refseqId;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof IproclassRefseqId)) return false;
        IproclassRefseqId castOther = (IproclassRefseqId) other;
        return ((this.getUniprotkbAc() == castOther.getUniprotkbAc()) || (this.getUniprotkbAc() != null && castOther.getUniprotkbAc() != null && this.getUniprotkbAc().equals(castOther.getUniprotkbAc()))) && ((this.getRefseqId() == castOther.getRefseqId()) || (this.getRefseqId() != null && castOther.getRefseqId() != null && this.getRefseqId().equals(castOther.getRefseqId())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getUniprotkbAc() == null ? 0 : this.getUniprotkbAc().hashCode());
        result = 37 * result + (getRefseqId() == null ? 0 : this.getRefseqId().hashCode());
        return result;
    }
}
