package org.xmi.repository.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.xmi.repository.RepositoryConnection;

/**
 * Persistence Version Element
 * @author e3a
 */
public class RepositoryVersion {

    private final Map<Properties, Object> map;

    public Map<Properties, ?> getMap() {
        return map;
    }

    @SuppressWarnings("unchecked")
    public RepositoryVersion() {
        this.map = new HashMap<Properties, Object>();
        this.map.put(Properties.versions, new HashMap());
    }

    public RepositoryVersion(Map<Properties, Object> map) {
        assert (map != null) : "version map is null.";
        assert (map.get(Properties.versions) != null) : "version item list is null.";
        this.map = map;
    }

    @SuppressWarnings("unchecked")
    public void addVersionItem(int version, int hash) {
        Map versions = getVersions();
        if (versions == null) {
            versions = new HashMap();
        }
        Map item = new HashMap();
        item.put(Properties.version_id, version);
        item.put(Properties.version_hash, hash);
        item.put(Properties.version_timestamp, System.currentTimeMillis());
        item.put(Properties.version_comment, "");
        versions.put(version, item);
        map.put(Properties.versions, versions);
    }

    @SuppressWarnings("unchecked")
    public void addVersionItem(int version, int hash, String comment) {
        Map versions = getVersions();
        if (versions == null) {
            versions = new HashMap();
        }
        Map item = new HashMap();
        item.put(Properties.version_id, version);
        item.put(Properties.version_hash, hash);
        item.put(Properties.version_timestamp, System.currentTimeMillis());
        item.put(Properties.version_comment, comment);
        versions.put(version, item);
        map.put(Properties.versions, versions);
    }

    public String getId() {
        assert (map.containsKey(Properties.id)) : "Id is null";
        return (String) map.get(Properties.id);
    }

    public void setId(String id) {
        assert (id != null) : "Id is null";
        map.put(Properties.id, id);
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, Map> getVersions() {
        assert (map.containsKey(Properties.versions)) : "versions is null";
        return (Map<Integer, Map>) map.get(Properties.versions);
    }

    @SuppressWarnings("unchecked")
    public void setVersions(Map<Integer, Map> versions) {
        assert (versions != null) : "versions is null";
        map.put(Properties.versions, versions);
    }

    /**
	 * Check if there is a specific version for the Element
	 * @param version
	 * @return
	 */
    public boolean containsVersion(int version) {
        return getVersions().containsKey(version);
    }

    /**
	 * Get the versions Element hash <p>
	 * returns the matching version or the next available older one.
	 * @param version can also be -1 for the latest version.
	 * @return
	 */
    public int getVersionHash(int version) {
        return (Integer) getVersions().get(getLatestAvailableVersion(version)).get(Properties.version_hash);
    }

    /**
	 * Get the latest available version for the Element
 	 * @return
 	 */
    public int getLatestVersion() {
        return getLatestAvailableVersion(RepositoryConnection.HEAD);
    }

    /**
	 * Get available version for this Element
 	 * @param latest
 	 * @return
 	 */
    public int getLatestAvailableVersion(int latest) {
        int version = 0;
        for (int v : getVersions().keySet()) {
            if (v > version && (v <= latest || latest == RepositoryConnection.HEAD)) {
                version = v;
            }
        }
        return version;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RepositoryVersion)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        RepositoryVersion rhs = (RepositoryVersion) obj;
        return new EqualsBuilder().append(getId(), rhs.getId()).append(getVersions(), rhs.getVersions()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(5541, 213).append(getId()).append(getVersions()).toHashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName()).append("[");
        buffer.append("\n\t : id:").append(getId()).append("\n\t : Versions[");
        for (Map.Entry<Integer, Map> i : getVersions().entrySet()) {
            buffer.append("\n\t\t:").append(i.getKey()).append(":").append(i.getValue());
        }
        buffer.append("\n\t]\n]");
        return buffer.toString();
    }
}
