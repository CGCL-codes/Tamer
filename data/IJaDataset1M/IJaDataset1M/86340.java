package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.policies.ConcretePolicy;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.RuntimeProject;
import java.util.List;

/**
 * Base for all activated {@link ConcretePolicy}s.
 */
public abstract class ActivatedConcretePolicyImpl extends ActivatedPolicyImpl implements ConcretePolicy {

    /**
     * Initialise.
     *
     * @param actualProject  The actual project containing the policy.
     * @param logicalProject The logical project containing the policy.
     */
    protected ActivatedConcretePolicyImpl(RuntimeProject actualProject, RuntimeProject logicalProject) {
        super(actualProject, logicalProject);
    }

    protected Policy getPolicy() {
        return getConcretePolicy();
    }

    /**
     * Get the underlying {@link ConcretePolicy}.
     *
     * @return The underlying {@link ConcretePolicy}.
     */
    protected abstract ConcretePolicy getConcretePolicy();

    public List getAlternatePolicies() {
        return getConcretePolicy().getAlternatePolicies();
    }

    public PolicyReference getAlternatePolicy(PolicyType policyType) {
        return getConcretePolicy().getAlternatePolicy(policyType);
    }
}
