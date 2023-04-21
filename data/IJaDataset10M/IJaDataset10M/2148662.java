package cartago.security;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import cartago.ArtifactId;
import cartago.Op;
import cartago.AgentId;

/**
 * Class representing a role and related policies.
 * 
 * @author aricci
 *
 */
public class Role {

    private ReentrantReadWriteLock rwl;

    private String name;

    private IArtifactUsePolicy useDefaultPolicy;

    private HashMap<String, IArtifactUsePolicy> usePolicyMap;

    public Role(String name) {
        this.name = name;
        rwl = new ReentrantReadWriteLock();
        usePolicyMap = new HashMap<String, IArtifactUsePolicy>();
        useDefaultPolicy = IWorkspaceSecurityManager.ALWAYS_ALLOW_USE;
    }

    public String getName() {
        return name;
    }

    public void addPolicyForUse(String artifactName, IArtifactUsePolicy policy) {
        usePolicyMap.put(artifactName, policy);
    }

    public void removePolicyForUse(String artifactName) {
        usePolicyMap.remove(artifactName);
    }

    public void setUseDefaultPolicy(IArtifactUsePolicy policy) {
        useDefaultPolicy = policy;
    }

    public boolean canUse(AgentId aid, ArtifactId id, Op opDetail) {
        try {
            rwl.readLock().lock();
            IArtifactUsePolicy p = usePolicyMap.get(id.getName());
            if (p != null) {
                return p.allow(aid, id, opDetail);
            } else {
                return useDefaultPolicy.allow(aid, id, opDetail);
            }
        } finally {
            rwl.readLock().unlock();
        }
    }
}
