package rs.realestate.service;

import java.util.List;
import rs.realestate.model.Item;
import rs.realestate.model.address.AddressBean;

public interface AddressService {

    AddressBean getAddressBean(Integer countryId, Integer cityId, Integer locationId, String street);

    AddressBean getAddressBean(String country, String city, String location, String street);

    AddressBean getDefaultAddressBeanTest();

    List<Item> getCitiesByCountryId(Integer countryId);

    List<Item> getLocationsByCityId(Integer parseInt);
}
