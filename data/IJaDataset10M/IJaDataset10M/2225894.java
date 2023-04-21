package br.com.hsj.importador.entidades.tagcomercio;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the cad_clientes_vendedores database table.
 * 
 */
@Entity
@Table(name = "cad_clientes_vendedores")
public class CadClientesVendedore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "id_cliente", nullable = false)
    private int idCliente;

    @Column(name = "id_vendedor", nullable = false)
    private int idVendedor;

    public CadClientesVendedore() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdVendedor() {
        return this.idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }
}
