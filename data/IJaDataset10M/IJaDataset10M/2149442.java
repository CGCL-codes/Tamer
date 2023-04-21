package org.gridtrust.ppm.impl.policy.normalizer;

public interface PolicyNormalizer {

    /**
	 * Normalize XACML 1.1 policy and return it's normalized policy content.
	 * @param policyContent - Input policy content
	 * @return - Normalized policy content
	 * @throws Exception - Internal error in policy normalization
	 */
    public String normalizePolicy(String policyContent) throws Exception;
}
