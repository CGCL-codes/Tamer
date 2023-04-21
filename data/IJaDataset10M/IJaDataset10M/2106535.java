package com.volantis.mcs.cache.impl;

import com.volantis.mcs.cache.CacheIdentity;
import com.volantis.synergetics.utilities.Base64;

/**
 * This class provide basic default behaviour for a {@link CacheIdentity}.
 * <p>All classes that wish to implement {@link CacheIdentity} should extend
 * this class.</p>
 */
public abstract class AbstractCacheIdentity implements CacheIdentity {

    /**
     * The creation time that makes up this identity
     */
    protected long createTime;

    /**
     * The expires time that makes up this identity
     */
    protected long expiresTime;

    /**
     * The sequence number that makes up this identity.
     */
    protected short sequenceNo;

    protected String base64Key = null;

    private static final char delimiter = '-';

    /**
     * Create a new {@link CacheIdentity}.
     * <p>This will normally be done via a call to
     *
     * @param createTime The creation time of the entity in milliseconds since
     *                   the EPOC.
     * @param expiresTime The time im milliseconds since the EPOC, 
     *                    after which the entry identified by this 
     *                    identity expires.
     * @param sequenceNo A sequence number which guarantees uniqueness when
     *                   combined with createTime.
     */
    protected AbstractCacheIdentity(long createTime, long expiresTime, short sequenceNo) {
        this.createTime = createTime;
        this.expiresTime = expiresTime;
        this.sequenceNo = sequenceNo;
    }

    /**
     * Create a new {@link CacheIdentity} from a base64Key.
     * <p>The base64Key is usually used as a way of externalising an identity
     * for use when passing between servlets or other external mechanisms.
     *
     * @param base64Key The String representation of a base 64 number.
     */
    protected AbstractCacheIdentity(String base64Key) {
        this.base64Key = base64Key;
        String decodedBase64Key = Base64.decodeToString(base64Key);
        int delim1Index = decodedBase64Key.indexOf(delimiter);
        int delim2Index = (delim1Index == -1) ? -1 : decodedBase64Key.indexOf(delimiter, delim1Index + 1);
        if ((delim1Index != -1) && (delim2Index != -1)) {
            createTime = Long.parseLong(decodedBase64Key.substring(0, delim1Index));
            expiresTime = Long.parseLong(decodedBase64Key.substring(delim1Index + 1, delim2Index));
            sequenceNo = Short.parseShort(decodedBase64Key.substring(delim2Index + 1));
        } else {
            throw new IllegalArgumentException("base64Key is not a valid" + " encoding of createTime, expiresTime and sequenceNo");
        }
    }

    public String getBase64KeyAsString() {
        if (base64Key == null) {
            base64Key = Base64.encodeString(Long.toString(createTime) + delimiter + Long.toString(expiresTime) + delimiter + Short.toString(sequenceNo));
        }
        return base64Key;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public short getSequenceNo() {
        return sequenceNo;
    }

    public int compareTo(Object o) {
        CacheIdentity identity = (CacheIdentity) o;
        if (createTime < identity.getCreateTime()) {
            return -1;
        } else if (createTime == identity.getCreateTime()) {
            if (sequenceNo < identity.getSequenceNo()) {
                return -1;
            } else if (sequenceNo == identity.getSequenceNo()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof CacheIdentity) {
                return (((CacheIdentity) o).getCreateTime() == createTime) && (((CacheIdentity) o).getSequenceNo() == sequenceNo);
            }
        }
        return false;
    }
}
