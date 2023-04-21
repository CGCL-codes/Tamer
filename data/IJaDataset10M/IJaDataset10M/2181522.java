package ca.ucalgary.cpsc.agilePlanner.persister.impl.data;

import ca.ucalgary.cpsc.agilePlanner.persister.Owner;

public class OwnerDataObject extends ca.ucalgary.cpsc.agilePlanner.util.Object implements Owner {

    private String name;

    public OwnerDataObject() {
    }

    public OwnerDataObject(String name) {
        this.name = name;
    }

    public String getOwner() {
        return this.name;
    }

    public void setOwner(String name) {
        this.name = name;
    }

    public OwnerDataObject clone() {
        OwnerDataObject clone = new OwnerDataObject();
        clone.name = this.name;
        return clone;
    }
}
