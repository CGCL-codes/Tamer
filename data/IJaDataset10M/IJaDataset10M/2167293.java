package org.jsmpp.bean;

/**
 * Raw data coding is intended for reserved coding groups.
 * 
 * @author uudashr
 * 
 */
public class RawDataCoding implements DataCoding {

    private final byte value;

    /**
     * Construct with specified value from PDU.
     * 
     * @param value is the byte value from PDU.
     */
    public RawDataCoding(byte value) {
        this.value = value;
    }

    public byte toByte() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RawDataCoding other = (RawDataCoding) obj;
        if (value != other.value) return false;
        return true;
    }

    @Override
    public String toString() {
        return "DataCoding:" + (0xff & toByte());
    }
}
