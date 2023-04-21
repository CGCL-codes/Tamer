package com.volantis.impl.mcs.runtime.policies;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;
import com.volantis.mcs.runtime.policies.ActivatedPolicyRetriever;
import com.volantis.mcs.runtime.policies.cache.CacheablePolicyProvider;
import com.volantis.mcs.runtime.policies.cache.PolicyCache;

public class CachingActivatedPolicyRetriever implements ActivatedPolicyRetriever {

    private final PolicyCache cache;

    private final ActivatedPolicyRetriever delegate;

    public CachingActivatedPolicyRetriever(PolicyCache cache, ActivatedPolicyRetriever delegate) {
        this.cache = cache;
        this.delegate = delegate;
    }

    public ActivatedPolicy retrievePolicy(final RuntimeProject project, final String name) throws RepositoryException {
        Object key = cache.getKey(project, name);
        CacheablePolicyProvider provider = new CacheablePolicyProvider(delegate, project, name, cache);
        ActivatedPolicy policy = (ActivatedPolicy) cache.retrieve(key, provider);
        return policy;
    }
}
