package schemacrawler.crawl;

import java.io.Serializable;
import schemacrawler.schema.Property;
import sf.util.Utility;

abstract class MutableProperty implements Property {

    private static final long serialVersionUID = -7150431683440256142L;

    private final String name;

    private final Serializable value;

    MutableProperty(final String name, final Serializable value) {
        if (Utility.isBlank(name)) {
            throw new IllegalArgumentException("No property name provided");
        }
        this.name = name.trim();
        this.value = value;
    }

    /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MutableProperty)) {
            return false;
        }
        final MutableProperty other = (MutableProperty) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public final String getName() {
        return name;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Serializable getValue() {
        return value;
    }

    /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#hashCode()
   */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (value == null ? 0 : value.hashCode());
        return result;
    }
}
