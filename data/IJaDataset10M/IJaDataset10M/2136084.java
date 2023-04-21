package com.superafroman.utils.scanner.criteria;

public class PackageNameCriteria implements IPackageCriteria {

    private String packageName;

    public PackageNameCriteria(String packageName) {
        if (packageName == null) {
            throw new NullPointerException("packageName must be specified");
        }
        this.packageName = packageName;
    }

    /**
     * {@inheritDoc}
     */
    public Match fitsCriteria(String packageName) {
        if (packageName == null) {
            return Match.NONE;
        }
        if (packageName.startsWith(this.packageName)) {
            return Match.COMPLETE;
        }
        if (this.packageName.startsWith(packageName)) {
            return Match.PARTIAL;
        }
        return Match.NONE;
    }
}
