package pe.com.bn.sach.domain;

/**
 * Bnchf36ExpVendedorId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Bnchf36ExpVendedorId implements java.io.Serializable {

    private Bnchf35Inmueble bnchf35Inmueble;

    private Bnchf36Vendedor bnchf36Vendedor;

    /** default constructor */
    public Bnchf36ExpVendedorId() {
        bnchf35Inmueble = new Bnchf35Inmueble();
        bnchf36Vendedor = new Bnchf36Vendedor();
    }

    /** full constructor */
    public Bnchf36ExpVendedorId(Bnchf35Inmueble bnchf35Inmueble, Bnchf36Vendedor bnchf36Vendedor) {
        this.bnchf35Inmueble = bnchf35Inmueble;
        this.bnchf36Vendedor = bnchf36Vendedor;
    }

    public Bnchf35Inmueble getBnchf35Inmueble() {
        return this.bnchf35Inmueble;
    }

    public void setBnchf35Inmueble(Bnchf35Inmueble bnchf35Inmueble) {
        this.bnchf35Inmueble = bnchf35Inmueble;
    }

    public Bnchf36Vendedor getBnchf36Vendedor() {
        return this.bnchf36Vendedor;
    }

    public void setBnchf36Vendedor(Bnchf36Vendedor bnchf36Vendedor) {
        this.bnchf36Vendedor = bnchf36Vendedor;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof Bnchf36ExpVendedorId)) return false;
        Bnchf36ExpVendedorId castOther = (Bnchf36ExpVendedorId) other;
        return ((this.getBnchf35Inmueble() == castOther.getBnchf35Inmueble()) || (this.getBnchf35Inmueble() != null && castOther.getBnchf35Inmueble() != null && this.getBnchf35Inmueble().equals(castOther.getBnchf35Inmueble()))) && ((this.getBnchf36Vendedor() == castOther.getBnchf36Vendedor()) || (this.getBnchf36Vendedor() != null && castOther.getBnchf36Vendedor() != null && this.getBnchf36Vendedor().equals(castOther.getBnchf36Vendedor())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getBnchf35Inmueble() == null ? 0 : this.getBnchf35Inmueble().hashCode());
        result = 37 * result + (getBnchf36Vendedor() == null ? 0 : this.getBnchf36Vendedor().hashCode());
        return result;
    }
}
