package org.josef.science.math;

import static org.josef.annotations.Status.Stage.PRODUCTION;
import static org.josef.annotations.Status.UnitTests.NOT_APPLICABLE;
import org.josef.annotations.Review;
import org.josef.annotations.Status;

/**
 * A collection of math related constants that could not be stored one of the
 * primitive wrapper classes or are better stored here since they are of
 * constant type.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 3261 $
 */
@Status(stage = PRODUCTION, unitTests = NOT_APPLICABLE)
@Review(by = "Kees Schotanus", at = "2010-11-25")
public final class MathConstants {

    /**
     * Private constructor prevents creation of an instance outside this class.
     */
    private MathConstants() {
    }

    /**
     * Radix 2 (binary).
     */
    public static final int RADIX_2 = 2;

    /**
     * Radix 10.
     */
    public static final int RADIX_10 = 10;

    /**
     * Radix 16 (hexadecimal).
     */
    public static final int RADIX_16 = 16;
}
