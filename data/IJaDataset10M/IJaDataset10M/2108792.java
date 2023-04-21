package org.dmd.dmc;

import java.io.Serializable;

/**
 * The DmcNamedObjectNontransportableREF is used when creating object reference attributes
 * whose payload is NOT expected to be sent across whatever RPC mechanism is being used.
 * The object may be resolved locally, but will never be sent to the remote side
 * of a connection.
 */
@SuppressWarnings("serial")
public abstract class DmcNamedObjectNontransportableREF<DMO extends DmcNamedObjectIF> extends DmcNamedObjectREF<DMO> implements Serializable, DmcNamedObjectIF, Comparable<DmcNamedObjectNontransportableREF<?>> {

    protected transient DMO object;

    /**
	 * Constructs a new object reference attribute.
	 */
    public DmcNamedObjectNontransportableREF() {
        object = null;
    }

    public abstract void setObject(DMO o);

    public boolean equals(Object obj) {
        return (getObjectName().equals(((DmcNamedObjectIF) obj).getObjectName()));
    }

    @Override
    public boolean valuesAreEqual(DmcMappedAttributeIF obj) {
        boolean rc = false;
        if (obj instanceof DmcNamedObjectNontransportableREF<?>) {
            DmcNamedObjectNontransportableREF<?> other = (DmcNamedObjectNontransportableREF<?>) obj;
            rc = getObjectName().equals(other.getObjectName());
        }
        return (rc);
    }

    /**
	 * @return The object if this reference is resolved.
	 */
    public DMO getObject() {
        return (object);
    }

    /**
	 * @return True if the reference is resolved and false otherwise.
	 */
    public boolean isResolved() {
        if (object == null) return (false);
        return (true);
    }

    @Override
    public int compareTo(DmcNamedObjectNontransportableREF<?> o) {
        return (getObjectName().getNameString().compareTo(o.getObjectName().getNameString()));
    }
}
