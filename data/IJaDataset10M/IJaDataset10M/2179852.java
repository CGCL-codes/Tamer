package org.datanucleus.store.types.converters;

import java.util.UUID;

/**
 * Class to handle the conversion between java.util.UUID and a String form.
 */
public class UUIDStringConverter implements TypeConverter<UUID, String> {

    public UUID toMemberType(String str) {
        if (str == null) {
            return null;
        }
        return UUID.fromString(str);
    }

    public String toDatastoreType(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }
}
