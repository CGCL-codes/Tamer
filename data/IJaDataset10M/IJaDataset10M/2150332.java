package org.mmtk.utility.options;

/**
 * Performance counter options.
 */
public class PerfEvents extends org.vmutil.options.StringOption {

    /**
   * Create the option.
   */
    public PerfEvents() {
        super(Options.set, "Perf Events", "Use this to specify a comma seperated list of performance events to measure", "");
    }
}
