package org.osmorc.misc;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VfsUtil;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Helper class which is capable of sorting a list of bundles according to their dependencies so they can be started in
 * the correct order.
 * <p/>
 * XXX: this class currently does not handle versions and doesn't work too well with cyclic dependencies
 *
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id:$
 */
public class BundleDependencySolver {

    /**
   * Sorts the list of bundles at the given URLs according their dependencies.
   *
   * @param bundleUrls the bundle urls
   * @return the sorted list.
   */
    public static String[] sortBundles(String[] bundleUrls) {
        List<BundleInfo> bundles = new ArrayList<BundleInfo>();
        for (String bundleUrl : bundleUrls) {
            try {
                JarFile file = new JarFile(VfsUtil.urlToPath(bundleUrl));
                Manifest manifest = file.getManifest();
                String exported = manifest.getMainAttributes().getValue(Constants.EXPORT_PACKAGE);
                String imported = manifest.getMainAttributes().getValue(Constants.IMPORT_PACKAGE);
                file.close();
                BundleInfo info = new BundleInfo(bundleUrl, getPackages(Constants.IMPORT_PACKAGE, imported), getPackages(Constants.EXPORT_PACKAGE, exported));
                bundles.add(info);
            } catch (Exception e) {
                bundles.add(new BundleInfo(bundleUrl, new String[0], new String[0]));
            }
        }
        ResolvingContext context = new ResolvingContext(bundles);
        return context.resolve();
    }

    private static String[] getPackages(String headerName, String headerValue) throws BundleException {
        if (headerValue == null) {
            return new String[0];
        }
        List<String> packages = new ArrayList<String>();
        ManifestElement[] elements = ManifestElement.parseHeader(headerName, headerValue);
        for (ManifestElement element : elements) {
            packages.add(element.getValue());
        }
        return packages.toArray(new String[packages.size()]);
    }

    public static class BundleInfo {

        public BundleInfo(String url, String[] requiredPackages, String[] providedPackages) {
            _url = url;
            _requiredPackages = requiredPackages;
            _providedPackages = providedPackages;
            _parents = new ArrayList<BundleInfo>();
        }

        public boolean addDependentBundle(BundleInfo child) {
            if (!child._parents.contains(this)) {
                if (increaseDepCounter()) {
                    child._parents.add(this);
                    return true;
                }
            }
            return false;
        }

        private boolean increaseDepCounter() {
            if (_cycleGuard) {
                _log.warn("Cyclic dependency encountered. This will not work... ");
                return false;
            }
            _cycleGuard = true;
            for (BundleInfo parent : _parents) {
                if (!parent.increaseDepCounter()) {
                    _cycleGuard = false;
                    return false;
                }
            }
            _dependencyCounter++;
            _cycleGuard = false;
            return true;
        }

        public boolean provides(String pkg) {
            for (String providedPackage : _providedPackages) {
                if (providedPackage.equals(pkg)) {
                    return true;
                }
            }
            return false;
        }

        public String getUrl() {
            return _url;
        }

        public void registerDependencies(ResolvingContext resolvingContext) {
            for (String requiredPackage : _requiredPackages) {
                BundleInfo providingBundle = resolvingContext.getBundleProviding(requiredPackage);
                if (providingBundle != null) {
                    if (!providingBundle.addDependentBundle(this)) {
                        List<BundleInfo> alternatives = resolvingContext.getAlternativesProviding(requiredPackage);
                        for (BundleInfo alternative : alternatives) {
                            if (alternative.addDependentBundle(this)) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        public int getDependencyCounter() {
            return _dependencyCounter;
        }

        private String _url;

        private String[] _requiredPackages;

        private String[] _providedPackages;

        private int _dependencyCounter;

        private boolean _cycleGuard = false;

        private List<BundleInfo> _parents;

        private static final Logger _log = Logger.getInstance(BundleInfo.class.getName());
    }

    public static class BundleInfoComparator implements Comparator<BundleInfo> {

        public int compare(BundleInfo bundleInfo, BundleInfo bundleInfo1) {
            return bundleInfo1.getDependencyCounter() - bundleInfo.getDependencyCounter();
        }
    }

    /**
   * Helper class which provides a resolving context. We use this class for caching queries on the base of installed
   * bundles, which should provide a decent speed up.
   */
    public static class ResolvingContext {

        public ResolvingContext(List<BundleInfo> bundleBase) {
            _bundleBase = bundleBase;
            _packageResolverCache = new WeakHashMap<String, BundleInfo>();
        }

        public BundleInfo getBundleProviding(String requiredPackage) {
            if (_packageResolverCache.containsKey(requiredPackage)) {
                return _packageResolverCache.get(requiredPackage);
            }
            for (BundleInfo knownBundle : _bundleBase) {
                if (knownBundle.provides(requiredPackage)) {
                    _packageResolverCache.put(requiredPackage, knownBundle);
                    return knownBundle;
                }
            }
            return null;
        }

        public List<BundleInfo> getAlternativesProviding(String requiredPackage) {
            BundleInfo defaultProvider = getBundleProviding(requiredPackage);
            List<BundleInfo> result = new ArrayList<BundleInfo>();
            for (BundleInfo bundleInfo : _bundleBase) {
                if (bundleInfo != defaultProvider && bundleInfo.provides(requiredPackage)) {
                    result.add(bundleInfo);
                }
            }
            return result;
        }

        public String[] resolve() {
            for (BundleInfo bundle : _bundleBase) {
                bundle.registerDependencies(this);
            }
            Collections.sort(_bundleBase, new BundleInfoComparator());
            String[] result = new String[_bundleBase.size()];
            int i = 0;
            for (BundleInfo bundle : _bundleBase) {
                result[i] = bundle.getUrl();
                i++;
            }
            return result;
        }

        private List<BundleInfo> _bundleBase;

        private WeakHashMap<String, BundleInfo> _packageResolverCache;
    }
}
