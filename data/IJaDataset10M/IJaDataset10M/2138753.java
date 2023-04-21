package org.dspace.app.didl;

import java.io.Serializable;

/**
 * This class implements UUID version 4. The values for the various fields are
 * crypto random values set by the factory class UUIDFactory
 * 
 * Development of this code was part of the aDORe repository project by the
 * Research Library of the Los Alamos National Laboratory.
 * 
 * This code is based on the implementation of UUID version 4 (the one that
 * uses random/pseudo-random numbers by Ashraf Amrou of the Old Dominion University
 * (Aug 14, 2003)
 **/
public final class UUID implements Serializable {

    private long hi;

    private long lo;

    /**
     * Construct a Version 4 UUID object from another UUID object
     * 
     * @param uuid
     *          the UUID to use as a base for the new UUID 
     **/
    public UUID(UUID uuid) {
        this.hi = uuid.hi;
        this.lo = uuid.lo;
    }

    /**
     * Construct a Version 4 UUID object form the two given long values.
     * These values are (pseudo)random numbers (best if crypto quality)
     * 
     * @param _hi
     *      first long value
     *      
     * @param _lo
     *      second long value
     *      
     **/
    public UUID(long _hi, long _lo) {
        this.hi = _hi;
        this.lo = _lo;
        lo &= 0x3FFFFFFFFFFFFFFFL;
        lo |= 0x8000000000000000L;
        lo |= 0x0000800000000000L;
        hi &= 0xFFFFFFFFFFFF0FFFL;
        hi |= 0x0000000000004000L;
    }

    /**
     * Compare UUID objects 
     * 
     * @param obj
     *      the object to compare this UUID against
     * 
     * @return true or false
     **/
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof UUID) return equals((UUID) obj);
        return false;
    }

    /**
     * Compare UUIDs 
     * 
     * @param uuid
     *      the UUID to compare this UUID against
     * 
     * @return true or false
     **/
    public boolean equals(UUID uuid) {
        return (hi == uuid.hi && lo == uuid.lo);
    }

    /**
     * Generate a hash for the UUID 
     * 
     * @return hash code for the UUID
     * 
     **/
    public int hashCode() {
        return new Long(hi ^ lo).hashCode();
    }

    /**
     * Obtain a string representation of the UUID object
     * 
     * @return the string representation of this UUID
     * 
     **/
    public String toString() {
        return (hexDigits(hi >> 32, 4) + "-" + hexDigits(hi >> 16, 2) + "-" + hexDigits(hi, 2) + "-" + hexDigits(lo >> 48, 2) + "-" + hexDigits(lo, 6));
    }

    /**
     * Obtain the Hex value of a given number of least significant octets
     * from a long value as a String
     * 
     * @param lVal
     *          the long value to retrieve octets from
     * 
     * @param nHexOctets
     *          number of hex octets to return
     * 
     * @return hex value of least significant octets as a string 
     * 
     **/
    private static String hexDigits(long lVal, int nHexOctets) {
        long tmp = 1L << (nHexOctets * 2 * 4);
        long result = lVal & (tmp - 1);
        result = tmp | result;
        return Long.toHexString(result).substring(1);
    }
}
