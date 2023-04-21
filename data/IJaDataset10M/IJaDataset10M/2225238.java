package org.escapek.core.cmdb.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author  i67i-24
 */
@Entity
public class PhoneNumber extends ContactData implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6121050657520171917L;

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
}
