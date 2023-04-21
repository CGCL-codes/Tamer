package jimo.osgi.impl.framework.dependancy;

import jimo.osgi.impl.framework.BundleClassLoaderImpl;
import jimo.osgi.impl.framework.BundleImpl;
import jimo.osgi.impl.util.ManifestHeader;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;

public class PackageImport implements BundleDependency {

    BundleImpl bundle;

    PackageExport target;

    private String name;

    private Version version;

    private String bundleSymbolicName;

    private String resolution;

    public PackageImport(String importPackage, Bundle bundle) {
        this.bundle = (BundleImpl) bundle;
        setup(importPackage);
    }

    private void setup(String importPackage) {
        ManifestHeader hdr = new ManifestHeader(importPackage);
        name = hdr.getHeader();
        version = Version.parseVersion(hdr.getAttribute(Constants.VERSION_ATTRIBUTE));
        bundleSymbolicName = hdr.getAttribute(Constants.BUNDLE_SYMBOLICNAME_ATTRIBUTE);
        resolution = hdr.getDirective(Constants.RESOLUTION_DIRECTIVE);
    }

    public String getName() {
        return name;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public PackageExport getTarget() {
        return target;
    }

    public void setTarget(PackageExport packageExport) {
        if (target != null) target.removeImport(this);
        target = packageExport;
        if (target != null) target.addImport(this); else unsetTarget();
    }

    public void unsetTarget() {
        target = null;
        BundleClassLoaderImpl.addUnresolvedImport(this);
        bundle.addDependancy(this);
    }

    public void remove() {
        if (getTarget() != null) getTarget().removeImport(this);
        bundle.getBundleClassLoader().removeImport(this);
        bundle.removeDependancy(this);
    }

    public void resolve() {
        BundleClassLoaderImpl.resolve(this);
    }

    public boolean isResolved() {
        return target != null || Constants.RESOLUTION_OPTIONAL.equals(resolution);
    }

    public String getBundleSymbolicName() {
        return bundleSymbolicName;
    }

    public String getResolution() {
        return resolution;
    }

    public Version getVersion() {
        return version;
    }
}
