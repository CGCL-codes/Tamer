package org.mmtk.utility.options;

/**
 * Trigger cycle detection if the meta data volume grows to this limit.
 */
public final class CycleMetaDataLimit extends org.vmutil.options.PagesOption {

    /**
   * Create the option.
   */
    public CycleMetaDataLimit() {
        super(Options.set, "Cycle Meta Data Limit", "Trigger cycle detection if the meta data volume grows to this limit", 4096);
    }
}
