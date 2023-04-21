package com.spring.hibernate.cashbox.model.dao.cashbox;

import java.io.Serializable;
import com.spring.hibernate.persistence.impl.GenericHibernateDaoImpl;
import com.spring.hibernate.cashbox.model.obj.cashbox.Addresses;

/**
 * DAO for table: Addresses
 * @author auto-generated
 */
public class AddressesDao extends GenericHibernateDaoImpl<Addresses, Serializable> {

    /** Constructor method */
    public AddressesDao() {
        super(Addresses.class);
    }
}
