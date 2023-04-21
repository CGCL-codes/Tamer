package net.sf.brightside.petspace.tapestry.pages;

import net.sf.brightside.petspace.domain.Owner;

public class ShowUser {

    private Owner owner;

    private String ownerName;

    void onActivate(Owner owner) {
        this.setOwnerName(owner.getUsername());
    }

    Owner onPassivate() {
        System.out.println("Page passivated!");
        return owner;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
