package org.escapek.core.internal.model.cmdb;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.escapek.core.internal.model.Node;
import org.escapek.core.internal.model.registry.RegistryNode;

/**
 * @author  nicolasjouanin
 */
@Entity
@Table(name = "PHONE_NUMBER")
public class PhoneNumber extends Node implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6121050657520171917L;

    private RegistryNode numberType;

    private String number;

    public PhoneNumber() {
    }

    /**
	 * @return  the number
	 * @uml.property  name="number"
	 */
    @Column(name = "PHONE_NUMBER")
    public String getNumber() {
        return number;
    }

    /**
	 * @param number  the number to set
	 * @uml.property  name="number"
	 */
    public void setNumber(String number) {
        this.number = number;
    }

    @ManyToOne
    @JoinColumn(name = "NUMBER_TYPE_FK")
    public RegistryNode getNumberType() {
        return numberType;
    }

    public void setNumberType(RegistryNode numberType) {
        this.numberType = numberType;
    }

    @Override
    public int hashCode() {
        int result = 23;
        final int multiplier = 47;
        result = multiplier * result + super.hashCode();
        result = multiplier * result + number.hashCode();
        result = multiplier * result + numberType.hashCode();
        return result;
    }
}
