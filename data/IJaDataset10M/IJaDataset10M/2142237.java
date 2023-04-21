package ui.controller.populator;

import model.address.Address;
import model.address.City;
import ui.view.component.AddressUI;

public class AddressPopulator implements DetailPopulator<Address, AddressUI> {

    public Address createFrom(AddressUI ui) {
        String addressString = ui.getAddress();
        City city = ui.getCity();
        return new Address(addressString, city);
    }

    public void modifyFrom(AddressUI ui, Address object) {
        object.setAddress(ui.getAddress());
        object.setCity(ui.getCity());
    }

    public void showIn(AddressUI ui, Address object) {
        ui.setAddress(object.getAddress());
        ui.setCity(object.getCity());
    }
}
