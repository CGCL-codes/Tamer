package org.mmtk.utility.options;

/**
 * Should we display detailed breakdown of where GC time is spent?
 */
public final class VerboseTiming extends org.vmutil.options.BooleanOption {

    /**
   * Create the option.
   */
    public VerboseTiming() {
        super(Options.set, "Verbose Timing", "Should we display detailed breakdown of where GC time is spent?", false);
    }
}
