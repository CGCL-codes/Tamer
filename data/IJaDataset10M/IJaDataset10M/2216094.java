package org.hl7.types.impl;

import org.hl7.types.ANY;
import org.hl7.types.BL;
import org.hl7.types.CD;
import org.hl7.types.CR;
import org.hl7.types.ED;
import org.hl7.types.LIST;
import org.hl7.types.NullFlavor;
import org.hl7.types.SET;
import org.hl7.types.ST;
import org.hl7.types.UID;

/**
 * Minimal implementation of CD as a model we can use for all other type. This one is only used to convey NULL values.
 * Again, we have static values of it, so we don't create so many objects in a running program.
 */
public final class CDnull extends ANYimpl implements CD {

    public static CDnull NI = new CDnull(NullFlavorImpl.NI);

    public static CDnull NA = new CDnull(NullFlavorImpl.NA);

    public static CDnull UNK = new CDnull(NullFlavorImpl.UNK);

    public static CDnull NASK = new CDnull(NullFlavorImpl.NASK);

    public static CDnull ASKU = new CDnull(NullFlavorImpl.ASKU);

    public static CDnull NAV = new CDnull(NullFlavorImpl.NAV);

    public static CDnull OTH = new CDnull(NullFlavorImpl.OTH);

    public static CDnull PINF = new CDnull(NullFlavorImpl.PINF);

    public static CDnull NINF = new CDnull(NullFlavorImpl.NINF);

    private CDnull(NullFlavor nf) {
        super(nf);
    }

    /**
	 * Get the a null value according to the null flavor code.
	 */
    public static CDnull valueOf(String nullFlavorString) {
        String nf = nullFlavorString.intern();
        if (nf == NullFlavorImpl.sNI) return NI; else if (nf == NullFlavorImpl.sNA) return NA; else if (nf == NullFlavorImpl.sUNK) return UNK; else if (nf == NullFlavorImpl.sNASK) return NASK; else if (nf == NullFlavorImpl.sASKU) return ASKU; else if (nf == NullFlavorImpl.sNAV) return NAV; else if (nf == NullFlavorImpl.sOTH) return OTH; else throw new IllegalArgumentException("null flavor " + nf);
    }

    /** FIXME: is NA correct or should it be derived from this and that? */
    public BL equal(ANY that) {
        return BLimpl.NA;
    }

    public ST code() {
        return STnull.NA;
    }

    public ST displayName() {
        return STnull.NA;
    }

    public UID codeSystem() {
        return UIDnull.NA;
    }

    public ST codeSystemName() {
        return STnull.NA;
    }

    public ST codeSystemVersion() {
        return STnull.NA;
    }

    public ED originalText() {
        return EDnull.NA;
    }

    public BL implies(CD x) {
        return BLimpl.NA;
    }

    public CD mostSpecificGeneralization(CD x) {
        return CDnull.NA;
    }

    public LIST<CR> qualifier() {
        throw new UnsupportedOperationException();
    }

    public SET<CD> translation() {
        throw new UnsupportedOperationException();
    }
}

;
