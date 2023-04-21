package com.csaba.connector.model;

import java.io.Serializable;
import java.util.Locale;

public class Country implements Serializable {

    private static final long serialVersionUID = -6499406119822965132L;

    public static final Country HUNGARY = new Country("HU", "Magyarország");

    public static final Country OMAN = new Country("OM", "Oman");

    private String id;

    private String name;

    public Country() {
        final Locale def = Locale.getDefault();
        id = def.getCountry();
        name = def.getDisplayCountry();
    }

    private Country(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != null && obj instanceof Country) {
            final Country other = (Country) obj;
            return id.equals(other.id);
        }
        return false;
    }
}
