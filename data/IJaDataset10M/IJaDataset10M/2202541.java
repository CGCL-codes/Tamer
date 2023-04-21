package org.mmtk.utility.options;

import static org.mmtk.policy.immix.ImmixConstants.DEFAULT_DEFRAG_FREE_HEADROOM;

public class DefragFreeHeadroom extends org.vmutil.options.PagesOption {

    /**
   * Create the option.
   */
    public DefragFreeHeadroom() {
        super(Options.set, "Defrag Free Headroom", "Allow the defragmenter this amount of free headroom during defrag. For analysis purposes only!", DEFAULT_DEFRAG_FREE_HEADROOM);
    }
}
