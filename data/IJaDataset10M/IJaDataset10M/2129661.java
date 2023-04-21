package ch.miranet.vof;

import ch.miranet.vof.VOFactory;

public class Person {

    private String name;

    private Address.Mutable address;

    public Person() {
        final VOFactory voFactory = new VOFactory();
        address = voFactory.createImplementation(Address.Mutable.class);
        address.setStreet("Itchylane");
        address.setZip("95060");
        address.setCity("Santa Cruz");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address.createImmutableClone();
    }

    public void updateAddress(Address address) {
        this.address.setStreet(address.getStreet());
        this.address.setZip(address.getZip());
        this.address.setCity(address.getCity());
    }

    @Override
    public String toString() {
        return getName();
    }
}
