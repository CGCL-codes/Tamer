package org.eclipse.misc.pluginsexport;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.misc.pluginsexport.internal.Activator;
import org.eclipse.update.core.IFeature;
import org.eclipse.update.core.IFeatureReference;
import org.eclipse.update.core.IPluginEntry;

public class ExportJob extends Job {

    private final IFeatureReference[] selectedFeatures;

    public ExportJob(IFeatureReference[] selectedFeatures) {
        super("Exporting plug-ins");
        this.selectedFeatures = selectedFeatures;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        try {
            final Set<String> paths = new HashSet<String>();
            processFeatures(monitor, paths, selectedFeatures);
        } catch (CoreException e) {
            e.printStackTrace();
            Activator.getDefault().getLog().log(e.getStatus());
            return e.getStatus();
        }
        return Status.OK_STATUS;
    }

    private void processFeatures(final IProgressMonitor monitor, final Set<String> paths, final IFeatureReference[] features) throws CoreException {
        for (IFeatureReference featureReference : features) {
            final IFeature feature = featureReference.getFeature(monitor);
            processFeatures(monitor, paths, feature.getIncludedFeatureReferences());
            final IPluginEntry[] pluginEntries = feature.getPluginEntries();
            for (IPluginEntry pluginEntry : pluginEntries) {
                System.out.println(pluginEntry.getVersionedIdentifier().getIdentifier());
            }
            System.out.println(featureReference.getURL());
            System.out.println(feature.getURL());
        }
    }
}
