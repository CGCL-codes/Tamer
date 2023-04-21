package pe.com.bn.sach.domain;

/**
 * Bnchf54ParamReqsTitId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Bnchf54ParamReqsTitId implements java.io.Serializable {

    private Bnchf54ParametroProducto bnchf54ParametroProducto;

    /** default constructor */
    public Bnchf54ParamReqsTitId() {
    }

    /** full constructor */
    public Bnchf54ParamReqsTitId(Bnchf54ParametroProducto bnchf54ParametroProducto) {
        this.bnchf54ParametroProducto = bnchf54ParametroProducto;
    }

    public Bnchf54ParametroProducto getBnchf54ParametroProducto() {
        return this.bnchf54ParametroProducto;
    }

    public void setBnchf54ParametroProducto(Bnchf54ParametroProducto bnchf54ParametroProducto) {
        this.bnchf54ParametroProducto = bnchf54ParametroProducto;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof Bnchf54ParamReqsTitId)) return false;
        Bnchf54ParamReqsTitId castOther = (Bnchf54ParamReqsTitId) other;
        return ((this.getBnchf54ParametroProducto() == castOther.getBnchf54ParametroProducto()) || (this.getBnchf54ParametroProducto() != null && castOther.getBnchf54ParametroProducto() != null && this.getBnchf54ParametroProducto().equals(castOther.getBnchf54ParametroProducto())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getBnchf54ParametroProducto() == null ? 0 : this.getBnchf54ParametroProducto().hashCode());
        return result;
    }
}
