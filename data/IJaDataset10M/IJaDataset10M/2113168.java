package org.escapek.core.internal.assemblers;

import org.escapek.core.dto.cmdb.AddressDTO;
import org.escapek.core.internal.model.cmdb.Address;

public class AddressAssembler extends AbstractAssembler<Address, AddressDTO> {

    private NodeAssembler nodeAssembler;

    private RegistryNodeAssembler registryAssembler;

    public AddressAssembler() {
        super();
        registryAssembler = new RegistryNodeAssembler();
        nodeAssembler = new NodeAssembler();
    }

    @Override
    public AddressDTO createDTO(Address source) {
        if (source == null) return null;
        AddressDTO dest = new AddressDTO();
        fillDTO(dest, source);
        return dest;
    }

    @Override
    public Address createModel(AddressDTO source) {
        if (source == null) return null;
        Address dest = new Address();
        fillModel(dest, source);
        return dest;
    }

    @Override
    public void fillDTO(AddressDTO dest, Address source) {
        nodeAssembler.fillDTO(dest, source);
        dest.setAddressType(registryAssembler.createDTO(source.getAddressType()));
        dest.setCity(source.getCity());
        dest.setCountryCode(registryAssembler.createDTO(source.getCountryCode()));
        dest.setPostcode(source.getPostcode());
        dest.setState(source.getState());
        dest.setStreet(source.getStreet());
    }

    @Override
    public void fillModel(Address dest, AddressDTO source) {
        nodeAssembler.fillModel(dest, source);
        dest.setAddressType(registryAssembler.createModel(source.getAddressType()));
        dest.setCity(source.getCity());
        dest.setCountryCode(registryAssembler.createModel(source.getCountryCode()));
        dest.setPostcode(source.getPostcode());
        dest.setState(source.getState());
        dest.setStreet(source.getStreet());
    }
}
