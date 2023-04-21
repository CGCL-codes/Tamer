package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.runtime.RuntimeProject;

/**
 * This interface allows various implementations of a post-load ("activation")
 * processor to be defined. These operate on repository policies to undertake
 * some configuration-specific initialization type processing.
 *
 * @mock.generate
 */
public interface PolicyActivator {

    /**
     * The given policy's post-load initialization is performed
     * by this method.
     *
     * @param actualProject  The <code>Project</code> from which the policy was
     *                       retrieved.
     * @param policyBuilder  the policy to be post-load initialized
     * @param logicalProject The <code>Project</code> to which the policy
     *                       appears to belong..
     */
    ActivatedPolicy activate(RuntimeProject actualProject, PolicyBuilder policyBuilder, RuntimeProject logicalProject);
}
