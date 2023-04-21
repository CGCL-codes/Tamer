package model.bean;

import java.io.Serializable;

/**
 *
 * @author Alessandro
 */
public class ClienteJB extends PessoaJB implements Serializable {

    private long idCliente;

    public ClienteJB() {
        super();
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }
}
