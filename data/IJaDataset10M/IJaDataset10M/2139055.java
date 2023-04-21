package org.tripcom.security.tam;

import static org.tripcom.security.util.Checks.checkNotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tripcom.integration.entry.SpaceURI;
import org.tripcom.security.Assertion;
import org.tripcom.security.Constants;
import org.tripcom.security.Role;
import org.tripcom.security.SecurityContext;
import org.tripcom.security.policies.AttributeMappingRule;
import org.tripcom.security.policies.Policy;
import org.tripcom.security.policies.PolicyManager;
import org.tripcom.security.policies.TrustFilteringRule;

/**
 * Main implementation of the <tt>TAM</tt> sub-component interface.
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public class BaseTAM implements TAM {

    /** The log object. */
    private static Log log = LogFactory.getLog(BaseTAM.class);

    /** A reference to the policy manager module. */
    private PolicyManager policyManager;

    /**
     * Build a new instance of the TAM module, given the dependencies provided.
     * 
     * @param policyManager the policy manager module (not null).
     */
    public BaseTAM(PolicyManager policyManager) {
        checkNotNull(policyManager, "Null policy manager");
        this.policyManager = policyManager;
    }

    /**
     * {@inheritDoc} The method performs the following steps:
     * <ul>
     * <li>Retrieves the trust filtering rules, by aggregating them from the
     * target space up to the root space.</li>
     * <li>Computes trusted assertions using the retrieved trust filtering
     * rules.</li>
     * <li>Retrieves all the role mapping rules for the target space, by
     * aggregating all the rules along the path from the root space.</li>
     * <li>Applies the role mapping rules, computing and returning the set of
     * roles mapped to the client.</li>
     * </ul>
     */
    public Collection<Role> filterAssertionsAndMapRoles(SecurityContext context, SpaceURI targetSpace) {
        checkNotNull(context, "Null security context");
        Collection<Assertion> trustedAssertions = applyTrustFilteringRules(retrieveTrustFilteringRules(targetSpace), context);
        Collection<Role> result = applyAttributeMappingRules(retrieveAttributeMappingRules(targetSpace), trustedAssertions);
        return result;
    }

    /**
     * Performs trust filtering. This method applies the specified trust
     * filtering rules to the set of assertions provided by the client. All the
     * assertions that can be trusted by one or more rules are added to the
     * result. At the end, a check is performed that client identity is trusted.
     * If this is not the case, no assertions will be trusted.
     * 
     * @param rules the trust filtering rules to apply (not null).
     * @param context the security context associated to the request (not null).
     * @return the trusted assertions resulting from the trust filtering
     *         process.
     */
    @SuppressWarnings("unchecked")
    private Collection<Assertion> applyTrustFilteringRules(Collection<TrustFilteringRule> rules, SecurityContext context) {
        assert (rules != null) && (context != null);
        Set<Assertion> result = new HashSet<Assertion>();
        outer: for (Assertion assertion : context.getParsedAssertions()) {
            for (TrustFilteringRule rule : rules) {
                Collection<Assertion> trustedAssertions = rule.evaluate(assertion, context);
                if (!trustedAssertions.isEmpty()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Assertion " + assertion.toString() + " accepted by rule \"" + rule.toString() + "\" and translated to " + trustedAssertions);
                    }
                    result.addAll(trustedAssertions);
                    continue outer;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Assertion " + assertion.toString() + " discarded");
            }
        }
        for (Assertion assertion : result) {
            if (Constants.ATTRIBUTE_ID.equals(assertion.getAttributeName())) {
                log.debug("Identity assertions are trusted");
                return result;
            }
        }
        log.debug("Identity not trusted: discard all the assertions");
        return Collections.EMPTY_LIST;
    }

    /**
     * Performs attribute mapping. This method applies the specified attribute
     * mapping rules to the incoming set of trusted assertions. Roles derived by
     * rules are collected. At the end, the system role
     * {@link Constants#AUTHENTICATED} is added if client identity is available
     * and trusted.
     * 
     * @param rules the attribute mapping rules to apply (not null).
     * @param assertions the trusted assertions to map to roles (not null).
     * @return the roles resulting from the attribute mapping process.
     */
    private Collection<Role> applyAttributeMappingRules(Collection<AttributeMappingRule> rules, Collection<Assertion> assertions) {
        assert (rules != null) && (assertions != null);
        Set<Role> result = new HashSet<Role>();
        for (AttributeMappingRule rule : rules) {
            Collection<Role> roles = rule.evaluate(assertions);
            if (log.isDebugEnabled() && !roles.isEmpty()) {
                Collection<Role> temp = new ArrayList<Role>(roles);
                temp.removeAll(result);
                if (!temp.isEmpty()) {
                    log.debug("Mapped roles " + temp + " by rule \"" + rule.toString() + "\"");
                }
            }
            result.addAll(roles);
        }
        if (!assertions.isEmpty()) {
            result.add(Constants.AUTHENTICATED);
        }
        return result;
    }

    /**
     * Retrieves the trust filtering rules for a given target space. The method
     * retrieves the policies of all the spaces on the path from the root down
     * to the target space and aggregates their trust filtering rules (since
     * they are inherited by subspaces).
     * 
     * @param targetSpace the target space (not null).
     * @return the trust filtering rules applying to the target space.
     */
    private Collection<TrustFilteringRule> retrieveTrustFilteringRules(SpaceURI targetSpace) {
        if (targetSpace == null) {
            return policyManager.retrieveKernelPolicy().getTrustFilteringRules();
        }
        Collection<TrustFilteringRule> result;
        result = new ArrayList<TrustFilteringRule>();
        for (SpaceURI space = targetSpace; space != null; space = space.getParentSpace()) {
            Policy policy = policyManager.retrievePolicy(space);
            Collection<TrustFilteringRule> rules = policy.getTrustFilteringRules();
            result.addAll(rules);
            if (log.isDebugEnabled()) {
                log.debug("Retrieved " + rules.size() + " trust rules for space " + space);
            }
        }
        return result;
    }

    /**
     * Retrieves the attribute mapping rules for a given target space. The
     * method retrieves the policies of all the spaces on the path from the root
     * down to the target space and aggregates their attribute mapping rules
     * (since they are inherited by subspaces).
     * 
     * @param targetSpace the target space (not null).
     * @return the attribute mapping rules applying to the target space.
     */
    private Collection<AttributeMappingRule> retrieveAttributeMappingRules(SpaceURI targetSpace) {
        if (targetSpace == null) {
            return policyManager.retrieveKernelPolicy().getRoleMappingRules();
        }
        Collection<AttributeMappingRule> result;
        result = new ArrayList<AttributeMappingRule>();
        for (SpaceURI space = targetSpace; space != null; space = space.getParentSpace()) {
            Policy policy = policyManager.retrievePolicy(space);
            Collection<AttributeMappingRule> rules = policy.getRoleMappingRules();
            result.addAll(rules);
            if (log.isDebugEnabled()) {
                log.debug("Retrieved " + rules.size() + " mapping rules for space " + space);
            }
        }
        return result;
    }
}
