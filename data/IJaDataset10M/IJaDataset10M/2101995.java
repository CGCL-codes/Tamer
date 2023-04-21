package org.extwind.osgi.launch.deploy.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.extwind.osgi.launch.deploy.Deployer;
import org.extwind.osgi.launch.remote.BundleDefinition;
import org.extwind.osgi.repository.Repository;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.framework.launch.Framework;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * @author donf.yang
 * 
 */
public class DeployerImpl implements Deployer {

    protected Framework framework;

    protected PackageAdmin packageAdmin;

    private boolean deploy = false;

    private static org.apache.juli.logging.Log logger = org.apache.juli.logging.LogFactory.getLog(DeployerImpl.class);

    private static final Object lock = new Object[0];

    public DeployerImpl(Framework framework, PackageAdmin packageAdmin) {
        this.framework = framework;
        this.packageAdmin = packageAdmin;
    }

    @Override
    public void deploy(Repository repository, boolean autoUpdate) throws Exception {
        synchronized (lock) {
            if (deploy) {
                throw new Exception("Processing deployment");
            }
            deploy = true;
        }
        logger.info("Deploying repository - " + repository.getLocation());
        try {
            internalDeploy(repository);
            logger.info("Deploy repository successfully - " + repository.getLocation());
        } finally {
            deploy = false;
        }
    }

    protected void internalDeploy(Repository repository) throws Exception {
        BundleContext context = framework.getBundleContext();
        Bundle[] installedBundles = context.getBundles();
        Map<String, Long> map = new HashMap<String, Long>(installedBundles.length);
        for (Bundle installedBundle : installedBundles) {
            Long id = map.get(installedBundle.getSymbolicName());
            if (id != null) {
                Bundle bnd = context.getBundle(id);
                if (bnd != null && bnd.getVersion().compareTo(installedBundle) > 0) {
                    continue;
                }
            }
            map.put(installedBundle.getSymbolicName(), installedBundle.getBundleId());
        }
        Set<Bundle> start = new HashSet<Bundle>();
        Set<Bundle> refresh = new HashSet<Bundle>();
        Map<Long, BundleDefinition> updateBundles = new HashMap<Long, BundleDefinition>();
        BundleDefinition[] bundles = repository.getBundles();
        for (BundleDefinition bundle : bundles) {
            Bundle installedBundle = null;
            Long id = map.get(bundle.getSymbolicName());
            if (id != null) {
                installedBundle = context.getBundle(id);
            }
            if (installedBundle == null) {
                logger.info("Installing bundle - " + bundle.getLocation());
                try {
                    Bundle newBundle = context.installBundle(bundle.getLocation(), repository.getBundleStream(bundle));
                    map.put(newBundle.getSymbolicName(), newBundle.getBundleId());
                    start.add(newBundle);
                } catch (Exception e) {
                    logger.warn("Unable to install bundle - " + bundle.getLocation(), e);
                }
            } else {
                if (new Version(bundle.getVersion()).compareTo(installedBundle.getVersion()) > 0) {
                    updateBundles.put(installedBundle.getBundleId(), bundle);
                    refresh.add(installedBundle);
                }
            }
        }
        if (start.size() > 0) {
            for (Bundle bundle : start) {
                try {
                    bundle.start();
                } catch (Exception e) {
                    logger.warn("Unable to start bundle - " + bundle.getLocation(), e);
                }
            }
        }
        if (updateBundles.size() > 0) {
            for (long bundleId : updateBundles.keySet()) {
                Bundle installedBundle = context.getBundle(bundleId);
                BundleDefinition bundle = updateBundles.get(bundleId);
                logger.info("Updating bundle - " + bundle.getLocation());
                try {
                    installedBundle.update(repository.getBundleStream(bundle));
                } catch (Exception e) {
                    logger.warn("Unable to update bundle - " + bundle.getLocation(), e);
                }
            }
        }
        if (refresh.size() > 0) {
            logger.info("Refreshing packages");
            try {
                packageAdmin.refreshPackages(refresh.toArray(new Bundle[0]));
            } catch (Exception e) {
                logger.warn("Error duing refresh packages", e);
            }
        }
    }
}
