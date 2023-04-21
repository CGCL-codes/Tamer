package com.bbn.vessel.author.templates;

import com.bbn.vessel.author.imspec.IMActionGroupNodeSpec;

/**
 * @author rtomlinson
 *
 */
public class GroupSpecWrapper {

    private final IMActionGroupNodeSpec groupSpec;

    /**
     * @param groupSpec the groupSpec to wrap
     */
    public GroupSpecWrapper(IMActionGroupNodeSpec groupSpec) {
        this.groupSpec = groupSpec;
    }

    @Override
    public String toString() {
        return groupSpec.getName();
    }

    /**
     * @return the groupSpec
     */
    public IMActionGroupNodeSpec getGroupSpec() {
        return groupSpec;
    }
}
