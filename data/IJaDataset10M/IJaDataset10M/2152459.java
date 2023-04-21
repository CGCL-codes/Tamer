package org.limewire.mojito.messages;

import java.util.Collection;
import org.limewire.mojito.KUID;
import org.limewire.mojito.db.DHTValueType;

/**
 * An interface for FindValueRequest implementations
 */
public interface FindValueRequest extends LookupRequest {

    /**
     * Returns a Collection of KUIDs the remote Node
     * is looking for
     */
    public Collection<KUID> getSecondaryKeys();

    /**
     * Returns the type of the value the remote Node
     * is looking for
     */
    public DHTValueType getDHTValueType();
}
