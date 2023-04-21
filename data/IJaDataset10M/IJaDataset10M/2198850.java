package org.jcvi.assembly.ace;

import org.jcvi.common.util.Range;

public class DefaultAceBestSegment implements AceBestSegment {

    private final String name;

    private final Range gappedConsensusRange;

    /**
     * @param name
     * @param gappedConsensusRange
     * @throws NullPointerException if either parameter is null.
     */
    public DefaultAceBestSegment(String name, Range gappedConsensusRange) {
        if (name == null || gappedConsensusRange == null) {
            throw new NullPointerException("parameters can not be null");
        }
        this.name = name;
        this.gappedConsensusRange = gappedConsensusRange;
    }

    @Override
    public Range getGappedConsensusRange() {
        return gappedConsensusRange;
    }

    @Override
    public String getReadName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + gappedConsensusRange.hashCode();
        result = prime * result + name.hashCode();
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
        if (!(obj instanceof DefaultAceBestSegment)) {
            return false;
        }
        DefaultAceBestSegment other = (DefaultAceBestSegment) obj;
        return getReadName().equals(other.getReadName()) && getGappedConsensusRange().equals(other.getGappedConsensusRange());
    }

    @Override
    public String toString() {
        return "DefaultAceBestSegment [name=" + name + ", gappedConsensusRange=" + gappedConsensusRange + "]";
    }
}
