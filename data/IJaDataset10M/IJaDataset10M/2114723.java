package pe.com.bn.sach.domain;

/**
 * Bnchf54ParamTasaInteresId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Bnchf54ParamTasaInteresId implements java.io.Serializable {

    private Bnchf54ParametroProducto bnchf54ParametroProducto;

    private Long f54IdPlazInt;

    /** default constructor */
    public Bnchf54ParamTasaInteresId() {
        bnchf54ParametroProducto = new Bnchf54ParametroProducto();
    }

    /** full constructor */
    public Bnchf54ParamTasaInteresId(Bnchf54ParametroProducto bnchf54ParametroProducto, Long f54IdPlazInt) {
        this.bnchf54ParametroProducto = bnchf54ParametroProducto;
        this.f54IdPlazInt = f54IdPlazInt;
    }

    public Bnchf54ParametroProducto getBnchf54ParametroProducto() {
        return this.bnchf54ParametroProducto;
    }

    public void setBnchf54ParametroProducto(Bnchf54ParametroProducto bnchf54ParametroProducto) {
        this.bnchf54ParametroProducto = bnchf54ParametroProducto;
    }

    public Long getF54IdPlazInt() {
        return this.f54IdPlazInt;
    }

    public void setF54IdPlazInt(Long f54IdPlazInt) {
        this.f54IdPlazInt = f54IdPlazInt;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof Bnchf54ParamTasaInteresId)) return false;
        Bnchf54ParamTasaInteresId castOther = (Bnchf54ParamTasaInteresId) other;
        return ((this.getBnchf54ParametroProducto() == castOther.getBnchf54ParametroProducto()) || (this.getBnchf54ParametroProducto() != null && castOther.getBnchf54ParametroProducto() != null && this.getBnchf54ParametroProducto().equals(castOther.getBnchf54ParametroProducto()))) && ((this.getF54IdPlazInt() == castOther.getF54IdPlazInt()) || (this.getF54IdPlazInt() != null && castOther.getF54IdPlazInt() != null && this.getF54IdPlazInt().equals(castOther.getF54IdPlazInt())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getBnchf54ParametroProducto() == null ? 0 : this.getBnchf54ParametroProducto().hashCode());
        result = 37 * result + (getF54IdPlazInt() == null ? 0 : this.getF54IdPlazInt().hashCode());
        return result;
    }
}
