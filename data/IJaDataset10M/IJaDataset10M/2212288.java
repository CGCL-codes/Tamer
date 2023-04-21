package com.angel.mocks.daos.impl;

import java.util.HashMap;
import java.util.Map;
import com.angel.architecture.persistence.ids.ObjectId;
import com.angel.dao.generic.impl.GenericSpringHibernateDAO;
import com.angel.mocks.daos.AddressDAO;
import com.angel.mocks.providers.Address;

/**
 *
 * @author William
 */
public class AddressSpringHibernateDAO extends GenericSpringHibernateDAO<Address, ObjectId> implements AddressDAO {

    public AddressSpringHibernateDAO() {
        super(Address.class, ObjectId.class);
    }

    public Address findUniqueByStreetAndNumber(String streetName, Integer number) {
        Map<String, Object> propertiesValues = new HashMap<String, Object>();
        propertiesValues.put("street", streetName);
        propertiesValues.put("number", number);
        return super.findUnique(propertiesValues);
    }
}
