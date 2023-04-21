package org.mmtk.utility.options;

import org.mmtk.policy.MarkSweepSpace;

/**
 * Number of bits to use for the header cycle of mark sweep spaces.
 */
public final class MarkSweepMarkBits extends org.vmutil.options.IntOption {

    /**
   * Create the option.
   */
    public MarkSweepMarkBits() {
        super(Options.set, "Mark Sweep Mark Bits", "Number of bits to use for the header cycle of mark sweep spaces", MarkSweepSpace.DEFAULT_MARKCOUNT_BITS);
    }

    /**
   * Ensure the value is valid.
   */
    protected void validate() {
        failIf(this.value <= 0, "Must provide at least one bit");
        failIf(this.value > MarkSweepSpace.MAX_MARKCOUNT_BITS, "Only " + MarkSweepSpace.MAX_MARKCOUNT_BITS + " bits are reserved in MarkSweepSpace");
    }
}
