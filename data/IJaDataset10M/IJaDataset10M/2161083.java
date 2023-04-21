package com.threerings.jpkg.debian.dependency;

/**
 * Holds a single Debian package dependency, such as a Depends, Conflicts, or Replaces dependency.
 * @see <a href="http://www.debian.org/doc/debian-policy/ch-relationships.html#s-binarydeps">Debian Policy Manual</a>
 */
public abstract class AbstractDependency implements ControlFileDependency {

    /**
     * Construct a new package dependency. The target package will depend on the supplied package's
     * name, but not a specific version of that package.
     * NOTE: No validation is performed on the package name.
     */
    public AbstractDependency(String name) {
        _dependency = name;
    }

    /**
     * Construct a new package dependency. The target package will depend on the supplied package's
     * name, as well as the supplied relationship to the supplied version,such as equals to
     * version 1.1.
     * NOTE: No validation is performed on the package name or package version.
     */
    public AbstractDependency(String name, String version, DependencyRelationships relationship) {
        _dependency = name + " (" + relationship.getOperator() + " " + version + ")";
    }

    public String asString() {
        return _dependency;
    }

    /** The dependency expressed as a string. */
    private final String _dependency;
}
