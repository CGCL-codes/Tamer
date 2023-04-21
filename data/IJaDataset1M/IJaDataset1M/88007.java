package org.tripcom.security.ac;

import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.tripcom.integration.entry.SpaceURI;
import org.tripcom.security.Action;
import org.tripcom.security.Effect;
import org.tripcom.security.Operation;
import org.tripcom.security.RequestAuthorizedEvent;
import org.tripcom.security.Role;
import org.tripcom.security.metadata.MetadataRetriever;
import org.tripcom.security.policies.Policy;
import org.tripcom.security.policies.PolicyManager;
import org.tripcom.security.tam.TAM;
import org.tripcom.security.util.Cache;
import org.tripcom.security.util.SimpleCache;
import org.tripcom.security.util.SynchronizedCache;
import static org.tripcom.security.util.Checks.*;

/**
 * Caching extension of the base AC implementation.
 * 
 * @author Francesco Corcoglioniti &ltfrancesco.corcoglioniti@cefriel.it&gt;
 */
public class CachingAC extends BaseAC implements ApplicationListener {

    /** Shared log object. */
    private static Log log = LogFactory.getLog(BaseAC.class);

    /** A synchronized cache maintaining the computed policy decisions. */
    private Cache<CacheKey, Effect> policyDecisionCache;

    /**
     * Creates a new caching AC.
     * 
     * @param policyManager The {@link PolicyManager} implementation used for
     *            retrieving the {@link Policy}.
     * @param tam The {@link TAM} implementation used for retrieving the
     *            security roles.
     * @param metadataRetriever The {@link MetadataRetriever} implementation
     *            used for retrieving the subspaces.
     */
    public CachingAC(PolicyManager policyManager, TAM tam, MetadataRetriever metadataRetriever) {
        this(policyManager, tam, metadataRetriever, new SynchronizedCache<CacheKey, Effect>(new SimpleCache<CacheKey, Effect>()));
    }

    /**
     * Creates a new caching AC using the specified cache.
     * 
     * @param policyManager The {@link PolicyManager} implementation used for
     *            retrieving the {@link Policy}.
     * @param tam The {@link TAM} implementation used for retrieving the
     *            security roles.
     * @param metadataRetriever The {@link MetadataRetriever} implementation
     *            used for retrieving the subspaces.
     * @param policyDecisionCache the policy decision cache (not null).
     */
    public CachingAC(PolicyManager policyManager, TAM tam, MetadataRetriever metadataRetriever, Cache<CacheKey, Effect> policyDecisionCache) {
        super(policyManager, tam, metadataRetriever);
        checkNotNull(policyDecisionCache, "Null policy decision cache");
        this.policyDecisionCache = policyDecisionCache;
    }

    /**
     * {@inheritDoc} This method caches the results of the evaluation of a space
     * policy for a given Action and set of roles. Cached effects are reused
     * where possible in subsequent invocations.
     */
    protected Effect policyDecision(Action action, Policy policy, Set<Role> roles) {
        assert (action != null) && (policy != null) && (roles != null);
        CacheKey key = new CacheKey(policy.getTargetSpace(), action, roles);
        Effect result = policyDecisionCache.lookup(key);
        if (result == null) {
            result = super.policyDecision(action, policy, roles);
            policyDecisionCache.cache(key, result);
        } else if (log.isDebugEnabled()) {
            log.debug("Policy decision " + "for space " + policy.getTargetSpace() + ", action " + action + " retrieved from cache: " + result);
        }
        return result;
    }

    /**
     * Invalidates the cache entries after the authorization of operations
     * {@link Operation#SET_POLICY} and {@link Operation#DESTROY}.
     * 
     * @param applicationEvent the event to react to.
     */
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        assert (applicationEvent != null);
        if (!(applicationEvent instanceof RequestAuthorizedEvent)) {
            return;
        }
        RequestAuthorizedEvent event = (RequestAuthorizedEvent) applicationEvent;
        Operation operation = event.getRequest().getOperation();
        SpaceURI targetSpace = event.getRequest().getTargetSpace();
        if (operation.in(Operation.SET_POLICY, Operation.DESTROY)) {
            synchronized (policyDecisionCache) {
                int removedEntries = 0;
                for (CacheKey key : policyDecisionCache.exportKeys()) {
                    if (key.matchSpace(targetSpace)) {
                        policyDecisionCache.remove(key);
                        ++removedEntries;
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("Removed " + removedEntries + " cached entries");
                }
            }
        }
    }

    /**
     * Represents a key in the cache maintaining policy decisions.
     * <p>
     * A cache key is a structure consisting of a space URL, an action and a set
     * of roles.
     * </p>
     */
    public static class CacheKey {

        /** The space URL for this key. */
        private String space;

        /** The action for this key. */
        private Action action;

        /** The roles for this key. */
        private Set<Role> roles;

        /**
         * Creates a new cache key using the parameters specified.
         * 
         * @param space the space URL (not null).
         * @param action the action (not null).
         * @param roles the roles (not null).
         */
        public CacheKey(SpaceURI space, Action action, Set<Role> roles) {
            assert (action != null) && (roles != null);
            this.space = space == null ? "" : space.toString();
            this.action = action;
            this.roles = roles;
        }

        /**
         * Checks whether the specified space correspond to the one of this
         * cache key.
         * 
         * @param space the space URL to check (not null).
         * @return true if the specified space matches.
         */
        public boolean matchSpace(SpaceURI space) {
            return (space == null && "".equals(this.space)) || space.toString().equals(this.space);
        }

        /**
         * {@inheritDoc} This method checks the equivalence of the space URL,
         * the action and the set of roles.
         */
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if ((o == null) || !(o instanceof CacheKey)) {
                return false;
            }
            CacheKey key = (CacheKey) o;
            return space.equals(key.space) && (action == key.action) && roles.equals(key.roles);
        }

        /**
         * {@inheritDoc} The returned hash code depends on the space URL, the
         * action and the set of roles.
         */
        @Override
        public int hashCode() {
            return space.hashCode() ^ action.hashCode() ^ roles.hashCode();
        }
    }
}
