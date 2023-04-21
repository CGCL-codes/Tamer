package org.jcvi.autoTasker.flu;

import org.jcvi.autoTasker.CodingRegionedScaffold;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.assembly.Scaffold;
import org.jcvi.glk.elvira.flu.FluSegment;

public class DefaultFluCdsScaffold implements FluCdsScaffold {

    private final CodingRegionedScaffold cdsScaffold;

    private final FluSegment segment;

    /**
     * @param cdsScaffold
     * @param segment
     */
    public DefaultFluCdsScaffold(CodingRegionedScaffold cdsScaffold, FluSegment segment) {
        if (cdsScaffold == null) {
            throw new NullPointerException("cdsScaffold can not be null");
        }
        if (segment == null) {
            throw new NullPointerException("segment can not be null");
        }
        this.cdsScaffold = cdsScaffold;
        this.segment = segment;
    }

    @Override
    public Scaffold getScaffold() {
        return cdsScaffold.getScaffold();
    }

    @Override
    public Range getCodingRange() {
        return cdsScaffold.getCodingRange();
    }

    @Override
    public String getName() {
        return cdsScaffold.getName();
    }

    @Override
    public FluSegment getSegment() {
        return segment;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cdsScaffold == null) ? 0 : cdsScaffold.hashCode());
        result = prime * result + ((segment == null) ? 0 : segment.hashCode());
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
        if (!(obj instanceof DefaultFluCdsScaffold)) {
            return false;
        }
        DefaultFluCdsScaffold other = (DefaultFluCdsScaffold) obj;
        if (cdsScaffold == null) {
            if (other.cdsScaffold != null) {
                return false;
            }
        } else if (!cdsScaffold.equals(other.cdsScaffold)) {
            return false;
        }
        if (segment != other.segment) {
            return false;
        }
        return true;
    }
}
