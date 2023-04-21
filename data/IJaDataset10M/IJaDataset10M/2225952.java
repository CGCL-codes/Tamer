package com.angel.mocks.architecture.services;

import com.angel.architecture.services.interfaces.GenericService;
import com.angel.mocks.providers.Address;

public interface AddressService extends GenericService {

    public Address findUniqueByStreetAndNumber(String streetName, Integer number);
}
