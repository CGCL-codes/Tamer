package com.abich.eve.evecalc.alloys;

public class Region {

    private String name;

    private String typeID;

    public Region(String name, String typeID) {
        this.name = name;
        this.typeID = typeID;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getTypeID() {
        return typeID;
    }

    public final void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    @Override
    public String toString() {
        return getName();
    }
}
