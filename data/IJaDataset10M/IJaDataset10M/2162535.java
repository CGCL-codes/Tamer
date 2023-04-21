package org.tju.ebs.domain.manager;

import org.tju.ebs.domain.dao.AddressDAO;

public class AddressManager extends BaseManager {

    private AddressDAO addressDAO;

    public void setAddressDAO(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public AddressDAO getAddressDAO() {
        return addressDAO;
    }
}
