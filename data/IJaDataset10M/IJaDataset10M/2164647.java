package org.osmorc;

import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osmorc.manifest.BundleManifest;
import java.util.Collection;
import java.util.List;

/**
 * The bundle manager allows for queries over the bundles which are known in the current project.
 *
 * @author Robert F. Beeger (robert@beeger.net)
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 */
public interface BundleManager {

    Object findBundle(String bundleSymbolicName);

    BundleManifest getBundleManifest(String bundleSymbolicName);

    BundleManifest getBundleManifest(@NotNull Object bundle);

    void addOrUpdateBundle(@NotNull Object bundle);

    @Nullable
    BundleDescription getBundleDescription(Object bundle);

    Collection<Object> determineBundleDependencies(@NotNull Object bundle);

    boolean isReexported(@NotNull Object reexportCandidate, @NotNull Object exporter);

    boolean reloadFrameworkInstanceLibraries(boolean onlyIfFrameworkInstanceSelectionChanged);

    Collection<Object> getHostBundles(@NotNull Object bundle);

    List<BundleDescription> getResolvedRequires(@NotNull Object bundle);

    List<ExportPackageDescription> getResolvedImports(@NotNull Object bundle);
}
