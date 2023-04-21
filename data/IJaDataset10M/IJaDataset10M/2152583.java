package org.mmtk.utility.options;

/**
 * When printing statistics, should statistics for each
 * gc-mutator phase be printed?
 */
public final class PrintPhaseStats extends org.vmutil.options.BooleanOption {

    /**
   * Create the option.
   */
    public PrintPhaseStats() {
        super(Options.set, "Print Phase Stats", "When printing statistics, should statistics for each gc-mutator phase be printed?", false);
    }
}
