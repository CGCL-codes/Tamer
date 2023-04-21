package org.mitre.rt.common;

import javax.xml.namespace.QName;
import org.mitre.rt.rtclient.IdedVersionedItemType;

/**
 *
 * @author bworrell
 */
public class ReferenceKey {

    private final String id;

    private final QName schemaType;

    public ReferenceKey(final IdedVersionedItemType item) {
        this.id = item.getId();
        this.schemaType = item.schemaType().getName();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if ((o instanceof ReferenceKey) == false) return false;
        final ReferenceKey key = (ReferenceKey) o;
        return schemaType.equals(key.schemaType) && id.equals(key.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() + 31 * this.schemaType.hashCode();
    }
}
