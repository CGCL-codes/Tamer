package org.motiv.core;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * A Cache Element, consisting of a key, value and attributes.
 * @author Pavlov Dm
 */
public class Element implements Serializable {

    private static final AtomicLongFieldUpdater<Element> HIT_COUNT_UPDATER = AtomicLongFieldUpdater.newUpdater(Element.class, "hitCount");

    /**
     * the cache key.
     */
    private final Object key;

    /**
     * the value.
     */
    private final Object value;

    /**
     * version of the element. System.currentTimeMillis() is used to compute version for updated elements. That
     * way, the actual version of the updated element does not need to be checked.
     */
    private volatile long version;

    /**
     * The number of times the element was hit.
     */
    private volatile long hitCount;

    /**
     * The amount of time for the element to live, in seconds. 0 indicates unlimited.
     */
    private volatile int timeToLive = Integer.MIN_VALUE;

    /**
     * The amount of time for the element to idle, in seconds. 0 indicates unlimited.
     */
    private volatile int timeToIdle = Integer.MIN_VALUE;

    /**
     * The creation time.
     */
    private long creationTime;

    /**
     * The last access time.
     */
    private long lastAccessTime;

    /**
     * If there is an Element in the Cache and it is replaced with a new Element for the same key,
     * then both the version number and lastUpdateTime should be updated to reflect that. The creation time
     * will be the creation time of the new Element, not the original one, so that TTL concepts still work.
     */
    private volatile long lastUpdateTime;

    /**
     * A full constructor.
     */
    public Element(final Object key, final Object value, final long version) {
        this.key = key;
        this.value = value;
        this.version = version;
        HIT_COUNT_UPDATER.set(this, 0);
        creationTime = System.currentTimeMillis();
    }

    /**
     * A full constructor.
     */
    public Element(final Serializable key, final Serializable value, final long version) {
        this((Object) key, (Object) value, version);
    }

    /**
     * Constructor.
     * @param key
     * @param value
     */
    public Element(final Serializable key, final Serializable value) {
        this((Object) key, (Object) value, 1L);
    }

    /**
     * Constructor.
     * @param key
     * @param value
     */
    public Element(final Object key, final Object value) {
        this(key, value, 1L);
    }

    /**
    * Gets the key attribute of the Element object.
    * @return The key as an Object.
    */
    public final Object getObjectKey() {
        return key;
    }

    /**
     * Gets the key attribute of the Element object.
     * @return The key value. If the key is not Serializable, null is returned and an info log message emitted
     */
    public final Serializable getKey() {
        Serializable keyAsSerializable;
        try {
            keyAsSerializable = (Serializable) key;
        } catch (Exception e) {
            throw new CacheException("The key " + key + " is not Serializable. Consider using Element#getObjectKey()");
        }
        return keyAsSerializable;
    }

    /**
     * Gets the value attribute of the Element object as an Object.
     * @return The value as an Object.  i.e no restriction is placed on it
     */
    public final Object getObjectValue() {
        return value;
    }

    /**
    * Gets the value attribute of the Element object.
    * @return The value which must be Serializable. If not use {@link #getObjectValue}. If the value is not Serializable, null is returned and an info log message emitted
    */
    public final Serializable getValue() {
        Serializable valueAsSerializable;
        try {
            valueAsSerializable = (Serializable) value;
        } catch (Exception e) {
            throw new CacheException("The value " + value + " for key " + key + " is not Serializable. Consider using Element#getObjectValue()");
        }
        return valueAsSerializable;
    }

    /**
     * Equals comparison with another element, based on the key.
     */
    @Override
    public final boolean equals(final Object object) {
        if (object == null || !(object instanceof Element)) {
            return false;
        }
        Element element = (Element) object;
        if (key == null || element.getObjectKey() == null) {
            return false;
        }
        return key.equals(element.getObjectKey());
    }

    /**
     * Sets time to Live
     * @param timeToLiveSeconds the number of seconds to live
     */
    public void setTimeToLive(final int timeToLiveSeconds) {
        if (timeToLiveSeconds < 0) {
            throw new IllegalArgumentException("timeToLive can't be negative");
        }
        this.timeToLive = timeToLiveSeconds;
    }

    /**
     * Sets time to idle
     * @param timeToIdleSeconds the number of seconds to idle
     */
    public void setTimeToIdle(final int timeToIdleSeconds) {
        if (timeToIdleSeconds < 0) {
            throw new IllegalArgumentException("timeToIdle can't be negative");
        }
        this.timeToIdle = timeToIdleSeconds;
    }

    /**
     * @return the time to live, in seconds
     */
    public int getTimeToLive() {
        if (Integer.MIN_VALUE == timeToLive) {
            return 0;
        } else {
            return timeToLive;
        }
    }

    /**
     * @return the time to idle, in seconds
     */
    public int getTimeToIdle() {
        if (Integer.MIN_VALUE == timeToIdle) {
            return 0;
        } else {
            return timeToIdle;
        }
    }

    /**
    * Gets the hashcode, based on the key.
    */
    @Override
    public final int hashCode() {
        return key.hashCode();
    }

    /**
    * Sets the version attribute of the ElementAttributes object.
    * @param version The new version value
    */
    public final void setVersion(final long version) {
        this.version = version;
    }

    /**
    * Gets the version attribute of the ElementAttributes object.
    * @return The version value
    */
    public final long getVersion() {
        return version;
    }

    /**
    * Gets the hit count on this element.
    */
    public final long getHitCount() {
        return hitCount;
    }

    /**
     * Resets the hit count to 0 and the last access time to now. Used when an Element is put into a cache.
     */
    public final void resetAccessStatistics() {
        resetLastAccessTime();
        HIT_COUNT_UPDATER.set(this, 0);
    }

    /**
     * Sets the last access time to now and increase the hit count.
     */
    public final void updateAccessStatistics() {
        HIT_COUNT_UPDATER.incrementAndGet(this);
        updateLastAccessTime(System.currentTimeMillis());
    }

    /**
     * Sets the last access time to now without updating the hit count.
     */
    public final void updateUpdateStatistics() {
        lastUpdateTime = System.currentTimeMillis();
        version = lastUpdateTime;
    }

    /**
    * Calculates the latest of creation and update time
    * @return if never updated, creation time is returned, otherwise updated time
    */
    public final long getLatestOfCreationAndUpdateTime() {
        if (lastUpdateTime == 0) {
            return getCreationTime();
        } else {
            return lastUpdateTime;
        }
    }

    /**
     * Returns a {@link String} representation of the {@link Element}.
     */
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ key = ").append(key).append(", value=").append(value).append(", version=").append(version).append(", hitCount=").append(hitCount).append(", creationTime=").append(creationTime).append(", lastAccessTime=").append(lastAccessTime).append(" ]");
        return sb.toString();
    }

    /**
     * An element is expired if the expiration time as given by {@link #getExpirationTime()} is in the past.
     * @return true if the Element is expired, otherwise false. If no lifespan has been set for the Element it is
     *         considered not able to expire.
     */
    public boolean isExpired() {
        if (!isLifespanSet() || isEternal()) {
            return false;
        }
        long now = System.currentTimeMillis();
        long expirationTime = getExpirationTime();
        return now > expirationTime;
    }

    /**
     * Set creation time method
     */
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * Get creation time method
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * Get last access time method
     */
    public long getLastAccessTime() {
        return lastAccessTime;
    }

    /**
     * Update last access time method
     */
    public void updateLastAccessTime(long time) {
        lastAccessTime = time;
    }

    /**
     * Reset last access time method
     */
    public void resetLastAccessTime() {
        lastAccessTime = System.currentTimeMillis();
    }

    /**
     * @return true if the element is eternal
     */
    public boolean isEternal() {
        return (timeToIdle == 0) && (timeToLive == 0);
    }

    /**
     * Whether any combination of eternal, TTL or TTI has been set.
     *
     * @return true if set.
     */
    public boolean isLifespanSet() {
        return this.timeToIdle != Integer.MIN_VALUE || this.timeToLive != Integer.MIN_VALUE;
    }

    /**
     * Returns the expiration time based on time to live. If this element also has a time to idle setting, the expiry
     * time will vary depending on whether the element is accessed.
     *
     * @return the time to expiration
     */
    public long getExpirationTime() {
        if (!isLifespanSet() || isEternal()) {
            return Long.MAX_VALUE;
        }
        long expirationTime = 0;
        long ttlExpiry = getCreationTime() + toMillis(getTimeToLive());
        long mostRecentTime = Math.max(getCreationTime(), getLastAccessTime());
        long ttiExpiry = mostRecentTime + toMillis(getTimeToIdle());
        if (getTimeToLive() != 0 && (getTimeToIdle() == 0 || getLastAccessTime() == 0)) {
            expirationTime = ttlExpiry;
        } else if (getTimeToLive() == 0) {
            expirationTime = ttiExpiry;
        } else {
            expirationTime = Math.min(ttlExpiry, ttiExpiry);
        }
        return expirationTime;
    }

    /**
     * Converts seconds to milliseconds, with a precision of 1 second
     * @param timeInSecs the time in seconds
     * @return The equivalent time in milliseconds
     */
    public static long toMillis(int timeInSecs) {
        return timeInSecs * 1000L;
    }
}
