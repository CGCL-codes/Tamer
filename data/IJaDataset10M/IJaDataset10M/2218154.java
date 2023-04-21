package org.genxdm.bridgekit.xs.constraint;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.exceptions.FacetException;
import org.genxdm.xs.exceptions.FacetMaxLengthException;
import org.genxdm.xs.facets.FacetKind;
import org.genxdm.xs.facets.LengthFacetUOM;
import org.genxdm.xs.facets.MaxLength;

public final class FacetMaxLengthImpl extends FacetLengthCommonImpl implements MaxLength {

    private final int maxLength;

    public FacetMaxLengthImpl(final int maxLength, final boolean isFixed) {
        super(isFixed, FacetKind.MaxLength);
        PreCondition.assertTrue(maxLength >= 0, "maxLength >= 0");
        this.maxLength = maxLength;
    }

    protected void checkLength(final int length, final LengthFacetUOM uom) throws FacetException {
        if (length > this.maxLength) {
            throw new FacetMaxLengthException(this, length, uom);
        }
    }

    public int getMaxLength() {
        return maxLength;
    }
}
