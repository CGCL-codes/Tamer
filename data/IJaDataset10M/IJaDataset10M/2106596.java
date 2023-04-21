package pe.com.bn.sach.domain;

/**
 * Bnchf56EvaluacionInmuebleId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Bnchf56EvaluacionInmuebleId implements java.io.Serializable {

    private Bnchf13Expediente bnchf13Expediente;

    private Long f56IdVersion;

    /** default constructor */
    public Bnchf56EvaluacionInmuebleId() {
    }

    /** full constructor */
    public Bnchf56EvaluacionInmuebleId(Bnchf13Expediente bnchf13Expediente, Long f56IdVersion) {
        this.bnchf13Expediente = bnchf13Expediente;
        this.f56IdVersion = f56IdVersion;
    }

    public Bnchf13Expediente getBnchf13Expediente() {
        return this.bnchf13Expediente;
    }

    public void setBnchf13Expediente(Bnchf13Expediente bnchf13Expediente) {
        this.bnchf13Expediente = bnchf13Expediente;
    }

    public Long getF56IdVersion() {
        return this.f56IdVersion;
    }

    public void setF56IdVersion(Long f56IdVersion) {
        this.f56IdVersion = f56IdVersion;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof Bnchf56EvaluacionInmuebleId)) return false;
        Bnchf56EvaluacionInmuebleId castOther = (Bnchf56EvaluacionInmuebleId) other;
        return ((this.getBnchf13Expediente() == castOther.getBnchf13Expediente()) || (this.getBnchf13Expediente() != null && castOther.getBnchf13Expediente() != null && this.getBnchf13Expediente().equals(castOther.getBnchf13Expediente()))) && ((this.getF56IdVersion() == castOther.getF56IdVersion()) || (this.getF56IdVersion() != null && castOther.getF56IdVersion() != null && this.getF56IdVersion().equals(castOther.getF56IdVersion())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getBnchf13Expediente() == null ? 0 : this.getBnchf13Expediente().hashCode());
        result = 37 * result + (getF56IdVersion() == null ? 0 : this.getF56IdVersion().hashCode());
        return result;
    }
}
