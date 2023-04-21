package org.jcvi.common.core.assembly.clc.cas.align;

public class DefaultCasAlignmentRegion implements CasAlignmentRegion {

    private final CasAlignmentRegionType type;

    private final long length;

    /**
     * @param type
     * @param length
     */
    public DefaultCasAlignmentRegion(CasAlignmentRegionType type, long length) {
        if (type == null) {
            throw new NullPointerException("type can not be null");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length can not < 0 : " + length);
        }
        this.type = type;
        this.length = length;
    }

    @Override
    public long getLength() {
        return length;
    }

    @Override
    public CasAlignmentRegionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "DefaultCasAlignmentRegion [type=" + type + ", length=" + length + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (length ^ (length >>> 32));
        result = prime * result + type.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DefaultCasAlignmentRegion)) {
            return false;
        }
        DefaultCasAlignmentRegion other = (DefaultCasAlignmentRegion) obj;
        if (length != other.length) {
            return false;
        }
        return type.equals(other.type);
    }
}
