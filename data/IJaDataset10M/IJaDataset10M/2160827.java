package org.apache.axis.holders;

import org.apache.axis.types.UnsignedInt;
import javax.xml.rpc.holders.Holder;

/**
 * Class UnsignedIntHolder
 *
 */
public final class UnsignedIntHolder implements Holder {

    /** Field _value */
    public UnsignedInt value;

    /**
     * Constructor UnsignedIntHolder
     */
    public UnsignedIntHolder() {
    }

    /**
     * Constructor UnsignedIntHolder
     *
     * @param value
     */
    public UnsignedIntHolder(UnsignedInt value) {
        this.value = value;
    }
}
