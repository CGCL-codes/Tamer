package org.hugh.eclipse.tools.bundleclassfinder.model;

/**
 * 
 * @author hugh
 */
public class FoundClassModel {

    private long bundleId;

    private String bundleName;

    private String bundleVersion;

    private String bundleState;

    private String path;

    private String fullUrl;

    public long getBundleId() {
        return bundleId;
    }

    public void setBundleId(long bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getBundleState() {
        return bundleState;
    }

    public void setBundleState(String bundleState) {
        this.bundleState = bundleState;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String url) {
        this.fullUrl = url;
    }

    public String getBundleVersion() {
        return bundleVersion;
    }

    public void setBundleVersion(String bundleVersion) {
        this.bundleVersion = bundleVersion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (bundleId ^ (bundleId >>> 32));
        result = prime * result + ((bundleName == null) ? 0 : bundleName.hashCode());
        result = prime * result + ((bundleState == null) ? 0 : bundleState.hashCode());
        result = prime * result + ((bundleVersion == null) ? 0 : bundleVersion.hashCode());
        result = prime * result + ((fullUrl == null) ? 0 : fullUrl.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final FoundClassModel other = (FoundClassModel) obj;
        if (bundleId != other.bundleId) return false;
        if (bundleName == null) {
            if (other.bundleName != null) return false;
        } else if (!bundleName.equals(other.bundleName)) return false;
        if (bundleState == null) {
            if (other.bundleState != null) return false;
        } else if (!bundleState.equals(other.bundleState)) return false;
        if (bundleVersion == null) {
            if (other.bundleVersion != null) return false;
        } else if (!bundleVersion.equals(other.bundleVersion)) return false;
        if (fullUrl == null) {
            if (other.fullUrl != null) return false;
        } else if (!fullUrl.equals(other.fullUrl)) return false;
        if (path == null) {
            if (other.path != null) return false;
        } else if (!path.equals(other.path)) return false;
        return true;
    }
}
