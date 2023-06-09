package org.limewire.mojito.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.limewire.mojito.KUID;
import org.limewire.mojito.db.DHTValueEntity;
import org.limewire.mojito.db.DHTValueType;
import org.limewire.mojito.db.Storable;
import org.limewire.mojito.routing.Contact;
import org.limewire.mojito.routing.RouteTable;
import org.limewire.mojito.routing.RouteTable.SelectMode;
import org.limewire.mojito.settings.DatabaseSettings;
import org.limewire.mojito.settings.KademliaSettings;

/**
 * Miscellaneous untilities for the Database
 */
public class DatabaseUtils {

    private DatabaseUtils() {
    }

    /**
     * Returns the expiration time of the given DHTValue
     */
    public static long getExpirationTime(RouteTable routeTable, DHTValueEntity entity) {
        KUID primaryKey = entity.getPrimaryKey();
        int k = KademliaSettings.REPLICATION_PARAMETER.getValue();
        Collection<Contact> nodes = routeTable.select(primaryKey, k, SelectMode.ALL);
        long creationTime = entity.getCreationTime();
        long expirationTime = DatabaseSettings.VALUE_EXPIRATION_TIME.getValue();
        if (nodes.size() < k || nodes.contains(routeTable.getLocalNode())) {
            return creationTime + expirationTime;
        } else {
            KUID valueBucketId = routeTable.getBucket(primaryKey).getBucketID();
            KUID localBucketId = routeTable.getBucket(routeTable.getLocalNode().getNodeID()).getBucketID();
            KUID xor = localBucketId.xor(valueBucketId);
            int lowestSetBit = xor.toBigInteger().getLowestSetBit();
            float ratio = 0.0f;
            if (lowestSetBit >= 0) {
                ratio = (float) (KUID.LENGTH_IN_BITS - lowestSetBit) / (float) KUID.LENGTH_IN_BITS;
            }
            return creationTime + (long) (expirationTime - (expirationTime * ratio));
        }
    }

    /**
     * Returns whether or not the given DHTValue has expired
     */
    public static boolean isExpired(RouteTable routeTable, DHTValueEntity entity) {
        return System.currentTimeMillis() >= getExpirationTime(routeTable, entity);
    }

    /**
     * 
     */
    public static boolean isPublishingRequired(Storable storable) {
        return isPublishingRequired(storable.getPublishTime(), storable.getLocationCount());
    }

    /**
     * 
     */
    public static boolean isPublishingRequired(long publishingTime, int locationCount) {
        if (publishingTime <= 0L || locationCount <= 0) {
            return true;
        }
        long t = ((locationCount * DatabaseSettings.VALUE_REPUBLISH_INTERVAL.getValue()) / KademliaSettings.REPLICATION_PARAMETER.getValue());
        long nextPublishTime = Math.max(t, DatabaseSettings.MIN_VALUE_REPUBLISH_INTERVAL.getValue());
        long time = publishingTime + nextPublishTime;
        return System.currentTimeMillis() >= time;
    }

    public static boolean isDHTValueType(DHTValueType valueType, DHTValueEntity entity) {
        return valueType.equals(DHTValueType.ANY) || valueType.equals(entity.getValue().getValueType());
    }

    public static Collection<? extends DHTValueEntity> filter(DHTValueType valueType, Collection<? extends DHTValueEntity> entities) {
        if (valueType.equals(DHTValueType.ANY)) {
            return entities;
        }
        List<DHTValueEntity> filtered = new ArrayList<DHTValueEntity>(entities.size());
        for (DHTValueEntity entity : entities) {
            if (isDHTValueType(valueType, entity)) {
                filtered.add(entity);
            }
        }
        return filtered;
    }

    public static DHTValueEntity getFirstEntityFor(DHTValueType valueType, Collection<? extends DHTValueEntity> entities) {
        for (DHTValueEntity entity : entities) {
            if (isDHTValueType(valueType, entity)) {
                return entity;
            }
        }
        return null;
    }
}
