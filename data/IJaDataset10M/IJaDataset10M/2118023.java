package edu.unibi.agbi.biodwh.entity.iproclass;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IproclassIpiId implements java.io.Serializable {

    private static final long serialVersionUID = -5384452518737593637L;

    private String uniprotkbAc;

    private String ipi;

    public IproclassIpiId() {
    }

    public IproclassIpiId(String uniprotkbAc, String ipi) {
        this.uniprotkbAc = uniprotkbAc;
        this.ipi = ipi;
    }

    @Column(name = "uniprotkb_ac", nullable = false)
    public String getUniprotkbAc() {
        return this.uniprotkbAc;
    }

    public void setUniprotkbAc(String uniprotkbAc) {
        this.uniprotkbAc = uniprotkbAc;
    }

    @Column(name = "ipi", nullable = false)
    public String getIpi() {
        return this.ipi;
    }

    public void setIpi(String ipi) {
        this.ipi = ipi;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof IproclassIpiId)) return false;
        IproclassIpiId castOther = (IproclassIpiId) other;
        return ((this.getUniprotkbAc() == castOther.getUniprotkbAc()) || (this.getUniprotkbAc() != null && castOther.getUniprotkbAc() != null && this.getUniprotkbAc().equals(castOther.getUniprotkbAc()))) && ((this.getIpi() == castOther.getIpi()) || (this.getIpi() != null && castOther.getIpi() != null && this.getIpi().equals(castOther.getIpi())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getUniprotkbAc() == null ? 0 : this.getUniprotkbAc().hashCode());
        result = 37 * result + (getIpi() == null ? 0 : this.getIpi().hashCode());
        return result;
    }
}
