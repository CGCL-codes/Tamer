package net.sourceforge.hachikaduki.model;

import java.io.Serializable;

public class Preference implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

    private Serializable value;

    public Preference() {
        super();
    }

    public Preference(String key, Serializable value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public Serializable getValue() {
        return this.value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Preference other = (Preference) obj;
        if (key == null) {
            if (other.key != null) return false;
        } else if (!key.equals(other.key)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }
}
