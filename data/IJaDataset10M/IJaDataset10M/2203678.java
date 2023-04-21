package org.gecko.jee.community.mobidick.datarepository.persistence.impl;

import java.io.Serializable;

public class Property implements Serializable {

    private String ancestorKey;

    private String ancestorValue;

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Property)) {
            return false;
        }
        final Property property = (Property) object;
        boolean result = true;
        result = result && (getAncestorKey() == null ? property.getAncestorKey() == null : getAncestorKey().equals(property.getAncestorKey()));
        result = result && (getAncestorValue() == null ? property.getAncestorValue() == null : getAncestorValue().equals(property.getAncestorValue()));
        return result;
    }

    public String getAncestorKey() {
        return ancestorKey;
    }

    public String getAncestorValue() {
        return ancestorValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getAncestorKey() == null ? 0 : getAncestorKey().hashCode());
        result = prime * result + (getAncestorValue() == null ? 0 : getAncestorValue().hashCode());
        return result;
    }

    public void setAncestorKey(final String ancestorKey) {
        this.ancestorKey = ancestorKey;
    }

    public void setAncestorValue(final String ancestorValue) {
        this.ancestorValue = ancestorValue;
    }
}
