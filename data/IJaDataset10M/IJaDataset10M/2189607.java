package com.volantis.mcs.repository;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.synergetics.log.LogDispatcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class iterates through all the policies in a repository and loads
 * them in so as to populate the cache.
 */
public class PolicyCachePreloader {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(PolicyCachePreloader.class);

    /**
     * List of policy classes we will load.
     */
    private static List POLICY_TYPES;

    static {
        POLICY_TYPES = new ArrayList();
        POLICY_TYPES.add(PolicyType.BASE_URL);
        POLICY_TYPES.add(PolicyType.AUDIO);
        POLICY_TYPES.add(PolicyType.BUTTON_IMAGE);
        POLICY_TYPES.add(PolicyType.CHART);
        POLICY_TYPES.add(PolicyType.VIDEO);
        POLICY_TYPES.add(PolicyType.IMAGE);
        POLICY_TYPES.add(PolicyType.ROLLOVER_IMAGE);
        POLICY_TYPES.add(PolicyType.TEXT);
        POLICY_TYPES.add(PolicyType.SCRIPT);
        POLICY_TYPES.add(PolicyType.LINK);
        POLICY_TYPES.add(PolicyType.THEME);
        POLICY_TYPES.add(PolicyType.LAYOUT);
    }

    /**
     * The repository.
     */
    private final LocalRepository repository;

    private final Project project;

    private final DeviceRepositoryAccessor deviceRepositoryAccessor;

    /**
     * Initialise.
     * 
     * @param repository the repository to load the caches from.
     * @param project
     * @param deviceRepositoryAccessor
     */
    public PolicyCachePreloader(LocalRepository repository, Project project, DeviceRepositoryAccessor deviceRepositoryAccessor) {
        this.repository = repository;
        this.project = project;
        this.deviceRepositoryAccessor = deviceRepositoryAccessor;
    }

    /**
     * Preload the caches as per the class comment.
     */
    public void run() {
        LocalRepositoryConnection connection = null;
        try {
            try {
                connection = (LocalRepositoryConnection) repository.connect();
                if (logger.isDebugEnabled()) {
                    logger.debug("Commencing Policy Preload...");
                }
                ProjectPolicyReader reader = new ProjectPolicyReader(connection, project);
                Iterator i = POLICY_TYPES.iterator();
                while (i.hasNext()) {
                    PolicyType policyType = (PolicyType) i.next();
                    loadPolicies(reader, policyType);
                }
                loadDevices(connection);
                if (logger.isDebugEnabled()) {
                    logger.debug("Policy Preload Complete");
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (Exception e) {
            logger.error("policy-preload-error", e);
        }
    }

    private void loadDevices(LocalRepositoryConnection connection) throws RepositoryException {
        RepositoryEnumeration e = deviceRepositoryAccessor.enumerateDeviceNames(connection);
        try {
            while (e.hasNext()) {
                String deviceName = (String) e.next();
                loadDevice(connection, deviceRepositoryAccessor, deviceName);
            }
        } finally {
            e.close();
        }
    }

    private void loadDevice(RepositoryConnection connection, DeviceRepositoryAccessor accessor, String deviceName) throws RepositoryException {
        DefaultDevice device = accessor.retrieveDevice(connection, deviceName);
        if (device == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("The device named " + deviceName + " was not found in the repository. Skipping...");
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Preloading device " + deviceName);
        }
    }

    /**
     * Load policies of a given type.
     * 
     * @param reader
     * @param policyType the policy type.
     * @exception RepositoryException if an error occurs
     */
    private void loadPolicies(ProjectPolicyReader reader, PolicyType policyType) throws RepositoryException {
        if (logger.isDebugEnabled()) {
            logger.debug("Preloading all policies for " + policyType);
        }
        RepositoryEnumeration e = reader.enumeratePolicyNames(policyType);
        try {
            while (e.hasNext()) {
                String name = (String) e.next();
                loadPolicy(reader, name);
            }
        } finally {
            e.close();
        }
    }

    /**
     * Load a policy and any dependant children.
     * 
     * @param reader the reader for the policy
     * @param name the name of the policy
     * @exception RepositoryException if an error occurs
     */
    private void loadPolicy(ProjectPolicyReader reader, String name) throws RepositoryException {
        Policy policy = reader.retrievePolicy(name);
        if (policy == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("The policy named " + name + " was not found in the repository. Skipping...");
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Preloading policy " + name);
        }
    }
}
