package org.jcvi.glk.elvira.report;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jcvi.glk.Coordinated;

public abstract class AbstractCoverageRegion<T extends Coordinated> implements CoverageRegion<T> {

    private Set<T> elements;

    private Long startCoordinate, endCoordinate;

    public AbstractCoverageRegion(long startCoordinate, long endCoordinate, List<T> elements) {
        if (startCoordinate == endCoordinate) {
            throw new IllegalArgumentException("start and end coordinates can not be equal");
        }
        if (startCoordinate > endCoordinate) {
            throw new IllegalArgumentException("start can not be greaterThan equal");
        }
        this.elements = new LinkedHashSet<T>();
        this.elements.addAll(elements);
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
    }

    @Override
    public int getCoverage() {
        return elements.size();
    }

    @Override
    public Collection<T> getElements() {
        return elements;
    }

    @Override
    public Long getStartCoordinate() {
        return startCoordinate;
    }

    @Override
    public Long getEndCoordinate() {
        return endCoordinate;
    }

    @Override
    public void setEndCoordinate(Long endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof AbstractCoverageRegion) {
            AbstractCoverageRegion<?> other = (AbstractCoverageRegion<?>) obj;
            if (!getStartCoordinate().equals(other.getStartCoordinate())) {
                return false;
            }
            if (!getEndCoordinate().equals(other.getEndCoordinate())) {
                return false;
            }
            if (getCoverage() != other.getCoverage()) {
                return false;
            }
            if (!getElements().containsAll(other.getElements())) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int ret = 17;
        ret = ret * PRIME + (int) (startCoordinate ^ startCoordinate >>> 32);
        ret = ret * PRIME + (int) (endCoordinate ^ endCoordinate >>> 32);
        for (T element : getElements()) {
            ret = ret * PRIME + element.hashCode();
        }
        return ret;
    }

    /**
     * NEEDSDOC: Update this method.
     * @param startCoordinate A long
     */
    public void setStartCoordinate(long startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getCoverage());
        result.append("x @ [");
        result.append(startCoordinate);
        result.append(", ");
        result.append(endCoordinate);
        result.append(" ]");
        return result.toString();
    }
}
