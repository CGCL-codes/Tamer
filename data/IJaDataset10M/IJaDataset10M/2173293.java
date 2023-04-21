package org.jcvi.common.core.assembly.util.trimmer;

import org.jcvi.common.core.assembly.Contig;
import org.jcvi.common.core.assembly.PlacedRead;

public final class ContigTrimmerResult<P extends PlacedRead, C extends Contig<P>> {

    public static <P extends PlacedRead, C extends Contig<P>> ContigTrimmerResult<P, C> createTrimmedResult(C trimmedContig, int numberOfLeftTrimmedBases, int numberOfRightTrimmedBases) {
        return new ContigTrimmerResult<P, C>(trimmedContig, numberOfLeftTrimmedBases, numberOfRightTrimmedBases);
    }

    public static <P extends PlacedRead, C extends Contig<P>> ContigTrimmerResult<P, C> createUntrimmedResult(C untrimmedContig) {
        return new ContigTrimmerResult<P, C>(untrimmedContig, 0, 0);
    }

    private final C trimmedContig;

    private final int numberOfLeftTrimmedBases;

    private final int numberOfRightTrimmedBases;

    private ContigTrimmerResult(C trimmedContig, int numberOfLeftTrimmedBases, int numberOfRightTrimmedBases) {
        this.trimmedContig = trimmedContig;
        this.numberOfLeftTrimmedBases = numberOfLeftTrimmedBases;
        this.numberOfRightTrimmedBases = numberOfRightTrimmedBases;
    }

    public C getTrimmedContig() {
        return trimmedContig;
    }

    public int getNumberOfLeftTrimmedBases() {
        return numberOfLeftTrimmedBases;
    }

    public int getNumberOfRightTrimmedBases() {
        return numberOfRightTrimmedBases;
    }

    public boolean wasTrimmed() {
        return numberOfLeftTrimmedBases > 0 || numberOfRightTrimmedBases > 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + numberOfLeftTrimmedBases;
        result = prime * result + numberOfRightTrimmedBases;
        result = prime * result + ((trimmedContig == null) ? 0 : trimmedContig.hashCode());
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        ContigTrimmerResult other = (ContigTrimmerResult) obj;
        if (numberOfLeftTrimmedBases != other.numberOfLeftTrimmedBases) {
            return false;
        }
        if (numberOfRightTrimmedBases != other.numberOfRightTrimmedBases) {
            return false;
        }
        if (trimmedContig == null) {
            if (other.trimmedContig != null) {
                return false;
            }
        } else if (!trimmedContig.equals(other.trimmedContig)) {
            return false;
        }
        return true;
    }
}
