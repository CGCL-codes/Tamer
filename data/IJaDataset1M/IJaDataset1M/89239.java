package mensaje_objetos.banco;

import java.util.List;
import javax.persistence.*;

/**
 *
 * @author vladimir
 */
@Entity
@Table(name = "cuentab")
@javax.persistence.SequenceGenerator(sequenceName = "cuenta_seq", name = "SEQ")
public class Cuenta {

    private List<Transaccion> transaccions;

    private List<Cheque> cheques;

    private int codigo;

    private String descripcion;

    private String numeroCuenta;

    private Banco banco;

    @Id
    @GeneratedValue(generator = "SEQ", strategy = GenerationType.AUTO)
    @Column(name = "codigo")
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @ManyToOne()
    @JoinColumn(name = "codigo_banco")
    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    @Transient
    public List<Cheque> getCheques() {
        return cheques;
    }

    public void setCheques(List<Cheque> cheques) {
        this.cheques = cheques;
    }

    @Transient
    public List<Transaccion> getTransaccions() {
        return transaccions;
    }

    public void setTransaccions(List<Transaccion> transaccions) {
        this.transaccions = transaccions;
    }

    @Column(name = "numero_cuenta")
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
}
