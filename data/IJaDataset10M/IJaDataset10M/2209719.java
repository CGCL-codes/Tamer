package org.codescale.edependency.finder.internal;

import java.util.Collection;
import java.util.HashMap;
import org.codescale.eDependency.Bundle;
import org.codescale.eDependency.EDependencyFactory;
import org.codescale.eDependency.Package;
import org.codescale.eDependency.RequireBundle;
import org.codescale.eDependency.Visibility;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.ModelEntry;
import org.eclipse.pde.core.plugin.PluginRegistry;

/**
 * @author Matthias Kappeller
 * 
 */
public class BundleLocator {

    private static Bundle convertToEMF(IPluginModelBase pluginModel, HashMap<String, Bundle> bundleCache) {
        BundleDescription bundleDescription = pluginModel.getBundleDescription();
        if (bundleDescription == null) {
            return null;
        }
        Bundle eBundle = null;
        if ((pluginModel.getExtensions() != null) && (pluginModel.getExtensions().getExtensions().length > 0)) {
            eBundle = EDependencyFactory.eINSTANCE.createPlugin();
        } else {
            eBundle = EDependencyFactory.eINSTANCE.createBundle();
        }
        eBundle.setSymbolicName(bundleDescription.getSymbolicName());
        bundleCache.put(eBundle.getSymbolicName(), eBundle);
        for (ExportPackageDescription exportPackageDescription : bundleDescription.getExportPackages()) {
            Package ePackage = EDependencyFactory.eINSTANCE.createPackage();
            ePackage.setName(exportPackageDescription.getName());
        }
        IPluginBase pluginBase = pluginModel.getPluginBase();
        if (pluginBase != null) {
            eBundle.setName(pluginBase.getTranslatedName());
        }
        setRequiredBundles(bundleCache, bundleDescription, eBundle);
        eBundle.setVersion(bundleDescription.getVersion());
        eBundle.setDependenciesOut(pluginModel.getBundleDescription().getRequiredBundles().length);
        eBundle.setDependenciesOut(pluginModel.getBundleDescription().getDependents().length);
        return eBundle;
    }

    /**
     * @param monitor
     * @return all installed bundles from pluginregistry
     */
    public static Collection<Bundle> searchInstalledBundles(IProgressMonitor monitor) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        HashMap<String, Bundle> bundleCache = new HashMap<String, Bundle>();
        IPluginModelBase[] models = PluginRegistry.getAllModels();
        monitor.beginTask("Search bundles...", models.length);
        if (bundleCache.size() == 0) {
            for (IPluginModelBase model : models) {
                monitor.worked(1);
                if (bundleCache.get(model.getBundleDescription().getSymbolicName()) == null) {
                    Bundle bundle = convertToEMF(model, bundleCache);
                    bundleCache.put(bundle.getSymbolicName(), bundle);
                }
            }
        } else {
            monitor.worked(models.length);
        }
        monitor.done();
        return bundleCache.values();
    }

    /**
     * @param pluginModel
     * @param bundleCache
     * @param bundleDescription
     * @param source
     */
    private static void setRequiredBundles(HashMap<String, Bundle> bundleCache, BundleDescription bundleDescription, Bundle source) {
        BundleSpecification[] requiredBundles = bundleDescription.getRequiredBundles();
        for (BundleSpecification bundleSpecification : requiredBundles) {
            String requiredBundleSymbolicName = bundleSpecification.getName();
            Bundle requiredBundle = bundleCache.get(requiredBundleSymbolicName);
            if ((requiredBundle == null) && !requiredBundleSymbolicName.equals(bundleDescription.getSymbolicName())) {
                ModelEntry entry = PluginRegistry.findEntry(requiredBundleSymbolicName);
                if (entry != null) {
                    IPluginModelBase model = entry.getModel();
                    if (model != null) {
                        requiredBundle = convertToEMF(model, bundleCache);
                        bundleCache.put(requiredBundleSymbolicName, requiredBundle);
                    }
                }
            }
            RequireBundle requireBundle = EDependencyFactory.eINSTANCE.createRequireBundle();
            requireBundle.setSource(source);
            requireBundle.setTarget(requiredBundle);
            requireBundle.setBundleVersion(bundleSpecification.getVersionRange());
            Visibility visibility = null;
            if (bundleSpecification.isExported()) {
                visibility = Visibility.REEXPORT;
            }
            requireBundle.setVisibility(visibility);
            requireBundle.setOptional(bundleSpecification.isOptional());
            source.getRequireBundleList().add(requireBundle);
        }
    }
}
