package org.openkonnect.api.service;

import org.openkonnect.api.OpenKonnectException;
import org.openkonnect.api.model.Address;

public interface IAddressService extends IOpenKonnectService {

    public void createAddress(Address address) throws OpenKonnectException;

    public void updateAddress(Address address) throws OpenKonnectException;

    public void deleteAddress(Address address) throws OpenKonnectException;
}
