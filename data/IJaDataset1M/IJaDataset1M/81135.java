package models.generic;

import models.Telefone;

/**
 * 
 * @author Flávio Gomes da Silva Lisboa
 * @version 0.1
 */
public abstract class AbstractTelefone implements IModel {

    protected Telefone telefone;

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }
}
