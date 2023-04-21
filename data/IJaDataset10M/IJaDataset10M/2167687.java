package org.jcvi.glk.elvira.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jcvi.glk.Coordinated;

public abstract class AbstractCoverageMap<V extends Coordinated, T extends CoverageRegion<V>> implements CoverageMap<T> {

    protected List<T> regions;

    protected List<V> startCoordinateSortedList;

    protected List<V> endCoordinateSortedList;

    protected V enteringAmplicon = null;

    protected V leavingAmplicon = null;

    protected List<V> coveringAmplicons = null;

    protected Iterator<V> enteringIterator = null;

    protected Iterator<V> leavingIterator = null;

    /**
     *
     * Creates a new <code>AbstractCoverageMap</code>.
     * @param amplicons A {@link Collection} of {@link Coordinated}s.
     */
    public AbstractCoverageMap(Collection<V> amplicons) {
        computeRegions(amplicons);
    }

    private void computeRegions(Collection<V> amplicons) {
        if (amplicons.size() > 0) {
            initalizeSortedLists(amplicons);
            computeRegions();
        } else {
            regions = Collections.<T>emptyList();
        }
    }

    private void computeRegions() {
        computeAllRegions();
        if (regions.size() > 0) {
            regions.remove(regions.size() - 1);
        }
    }

    private void computeAllRegions() {
        initializeValues();
        computeRegionsForAllEnteringAmplicons();
        computeRemainingRegions();
    }

    private void initializeValues() {
        regions = new ArrayList<T>();
        enteringIterator = startCoordinateSortedList.iterator();
        leavingIterator = endCoordinateSortedList.iterator();
        coveringAmplicons = new ArrayList<V>();
        enteringAmplicon = getNextAmplicon(enteringIterator);
        leavingAmplicon = getNextAmplicon(leavingIterator);
    }

    private void computeRegionsForAllEnteringAmplicons() {
        while (enteringAmplicon != null) {
            if (isEntering()) {
                handleEnteringAmplicon();
            } else if (isAbutment()) {
                removeLeavingAmplicon();
            } else {
                handleLeavingAmplicon();
            }
        }
    }

    private void computeRemainingRegions() {
        while (leavingAmplicon != null) {
            createNewEndRegionWithoutLeavingAmplicon();
            Long endCoord = leavingAmplicon.getEndCoordinate();
            leavingAmplicon = getNextAmplicon(leavingIterator);
            removeAnyAmpliconsWithSameEndCoordFromRegion(endCoord);
        }
    }

    private void removeAnyAmpliconsWithSameEndCoordFromRegion(Long endCoord) {
        while (leavingAmplicon != null && leavingAmplicon.getEndCoordinate().equals(endCoord)) {
            getPreviousRegion().getElements().remove(leavingAmplicon);
            removeLeavingAmplicon();
        }
    }

    private void handleEnteringAmplicon() {
        long startCoord = enteringAmplicon.getStartCoordinate();
        createNewRegionWithEnteringAmplicon();
        enteringAmplicon = getNextAmplicon(enteringIterator);
        handleAmpliconsWithSameStartCoord(startCoord);
    }

    private void handleLeavingAmplicon() {
        createNewRegionWithoutLeavingAmplicon();
        leavingAmplicon = getNextAmplicon(leavingIterator);
    }

    private void removeLeavingAmplicon() {
        coveringAmplicons.remove(leavingAmplicon);
        leavingAmplicon = getNextAmplicon(leavingIterator);
    }

    private boolean isAbutment() {
        return leavingAmplicon.getEndCoordinate() == enteringAmplicon.getStartCoordinate() - 1;
    }

    private void handleAmpliconsWithSameStartCoord(long regionStart) {
        while (enteringAmplicon != null && enteringAmplicon.getStartCoordinate().equals(regionStart)) {
            getPreviousRegion().getElements().add(enteringAmplicon);
            coveringAmplicons.add(enteringAmplicon);
            enteringAmplicon = getNextAmplicon(enteringIterator);
        }
    }

    private boolean isEntering() {
        return enteringAmplicon.getStartCoordinate() < leavingAmplicon.getEndCoordinate();
    }

    private void createNewEndRegionWithoutLeavingAmplicon() {
        coveringAmplicons.remove(leavingAmplicon);
        setEndCoordinateOfPreviousRegion(leavingAmplicon.getEndCoordinate());
        regions.add(createNewCoverageRegion(leavingAmplicon.getEndCoordinate() + 1, leavingAmplicon.getEndCoordinate() + 2, coveringAmplicons));
    }

    protected abstract T createNewCoverageRegion(Long start, Long end, List<V> elements);

    private void createNewRegionWithoutLeavingAmplicon() {
        coveringAmplicons.remove(leavingAmplicon);
        final long endCoordinate = leavingAmplicon.getEndCoordinate();
        setEndCoordinateOfPreviousRegion(endCoordinate);
        regions.add(createNewCoverageRegion(endCoordinate + 1, endCoordinate + 2, coveringAmplicons));
    }

    private void setEndCoordinateOfPreviousRegion(final long endCoordinate) {
        getPreviousRegion().setEndCoordinate(endCoordinate);
    }

    private void createNewRegionWithEnteringAmplicon() {
        if (regions.size() > 0) {
            final long endCoordinate = enteringAmplicon.getStartCoordinate() - 1;
            setEndCoordinateOfPreviousRegion(endCoordinate);
        }
        coveringAmplicons.add(enteringAmplicon);
        regions.add(createNewCoverageRegion(enteringAmplicon.getStartCoordinate(), enteringAmplicon.getStartCoordinate() + 1, coveringAmplicons));
    }

    private T getPreviousRegion() {
        return regions.get(regions.size() - 1);
    }

    private V getNextAmplicon(Iterator<V> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }

    private void initalizeSortedLists(Collection<V> amplicons) {
        startCoordinateSortedList = new ArrayList<V>();
        endCoordinateSortedList = new ArrayList<V>();
        startCoordinateSortedList.addAll(amplicons);
        endCoordinateSortedList.addAll(amplicons);
        filterAmpliconsWithoutCoordinates(startCoordinateSortedList);
        filterAmpliconsWithoutCoordinates(endCoordinateSortedList);
        Collections.sort(startCoordinateSortedList, new StartCoordinateComparator());
        Collections.sort(endCoordinateSortedList, new EndCoordinateComparator());
    }

    /**
     * If there are no coordinates (start or end are null) then
     * we remove them so they don't mess up our computations.
     * @param amp
     */
    private void filterAmpliconsWithoutCoordinates(Collection<V> amp) {
        for (Iterator<V> it = amp.iterator(); it.hasNext(); ) {
            V entry = it.next();
            if (entry.getEndCoordinate() == null || entry.getStartCoordinate() == null || (entry.getEndCoordinate() - entry.getStartCoordinate() == 0)) {
                it.remove();
            }
        }
    }

    @Override
    public int getSize() {
        return regions.size();
    }

    @Override
    public T getRegion(int i) {
        return regions.get(i);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AmpliconCoverageMap) {
            AmpliconCoverageMap other = (AmpliconCoverageMap) obj;
            if (getSize() != other.getSize()) {
                return false;
            }
            for (int i = 0; i < getSize(); i++) {
                if (!getRegion(i).equals(other.getRegion(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        final int PRIME = 37;
        int ret = 17;
        for (T region : regions) {
            ret = ret * PRIME + region.hashCode();
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (T region : regions) {
            buf.append(region);
            buf.append("\n");
        }
        return buf.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return regions.iterator();
    }

    @Override
    public List<T> getRegionsFrom(long start, long end) {
        List<T> selectedRegions = new ArrayList<T>();
        for (T region : regions) {
            long regionStart = region.getStartCoordinate().longValue();
            if (start <= regionStart && end >= regionStart) {
                selectedRegions.add(region);
            }
        }
        return selectedRegions;
    }
}
