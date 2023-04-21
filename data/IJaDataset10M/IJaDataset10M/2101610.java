package com.volantis.mcs.eclipse.builder.common.policies;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Configuration information for PolicyFileAccessor.createPolicy()
 */
public interface CreatePolicyConfiguration {

    /**
     * Returns the progress monitor to be used for creating this policy.
     *
     * @return The progress monitor
     */
    IProgressMonitor getProgressMonitor();
}
