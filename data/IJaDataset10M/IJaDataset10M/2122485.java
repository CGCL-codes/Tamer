package com.volantis.mcs.runtime.configuration.project;

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;
import java.util.List;

/**
 * Configuration for a policy type specific
 */
public class PolicyTypePartitionConfiguration {

    /**
     * The policy type for which this applies.
     */
    List policyTypes;

    /**
     * The size of the partition.
     */
    int size;

    /**
     * The constraints for policies within this partition.
     */
    RemotePolicyCacheConfiguration constraints;

    /**
     * Get the policy types.
     *
     * @return The policy types as a list of {@link PolicyType}.
     */
    public List getPolicyTypes() {
        return policyTypes;
    }

    /**
     * Get the constraints.
     *
     * @return The constraints.
     */
    public RemotePolicyCacheConfiguration getConstraints() {
        return constraints;
    }

    /**
     * Get the size.
     *
     * @return The size.
     */
    public int getSize() {
        return size;
    }
}
