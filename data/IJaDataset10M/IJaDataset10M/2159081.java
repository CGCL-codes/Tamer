package org.apache.maven.bootstrap.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Describes a dependency.
 *
 * @version $Id: Dependency.java 420409 2006-07-10 03:31:52Z kenney $
 */
public class Dependency extends Model {

    private String id;

    private String url;

    private String jar;

    private String scope = SCOPE_COMPILE;

    private String resolvedVersion;

    private boolean optional;

    public static final String SCOPE_TEST = "test";

    public static final String SCOPE_COMPILE = "compile";

    public static final String SCOPE_RUNTIME = "runtime";

    private Set exclusions = new HashSet();

    public Dependency(List chain) {
        super(chain);
    }

    public Dependency(String groupId, String artifactId, String version, String type, List chain) {
        this(chain);
        setVersion(version);
        setArtifactId(artifactId);
        setGroupId(groupId);
        setType(type);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        if (isValid(getGroupId()) && isValid(getArtifactId())) {
            return getGroupId() + ":" + getArtifactId();
        }
        return id;
    }

    public String getArtifactDirectory() {
        if (isValid(getGroupId())) {
            return getGroupId();
        }
        return getId();
    }

    public String getArtifact() {
        if (jar != null) {
            return jar;
        }
        String artifact;
        if (isValid(getArtifactId())) {
            artifact = getArtifactId() + "-" + getResolvedVersion() + ".";
        } else {
            artifact = getId() + "-" + getResolvedVersion() + ".";
        }
        if ("jar".equals(getType()) || "maven-plugin".equals(getType())) {
            artifact += "jar";
        } else {
            artifact += getType();
        }
        return artifact;
    }

    public void setJar(String jar) {
        if (jar.trim().length() == 0) {
            return;
        }
        this.jar = jar;
    }

    public String getJar() {
        return jar;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return getPackaging();
    }

    public void setType(String type) {
        setPackaging(type);
    }

    private boolean isValid(String value) {
        return value != null && !value.trim().equals("");
    }

    public String toString() {
        return "Dependency[" + getId() + ":" + getVersion() + ":" + getType() + ":" + getClassifier() + "]";
    }

    public String getConflictId() {
        return getGroupId() + ":" + getArtifactId() + ":" + getType();
    }

    public String getDependencyConflictId() {
        return getGroupId() + ":" + getArtifactId() + ":" + getType() + ":" + getVersion();
    }

    public void setResolvedVersion(String resolvedVersion) {
        this.resolvedVersion = resolvedVersion;
    }

    public String getResolvedVersion() {
        if (resolvedVersion == null) {
            resolvedVersion = getVersion();
        }
        return resolvedVersion;
    }

    public void addExclusion(Exclusion currentExclusion) {
        exclusions.add(currentExclusion.getConflictId());
    }

    public Set getExclusions() {
        return exclusions;
    }

    public Dependency getPomDependency() {
        Dependency dep = new Dependency(getGroupId(), getArtifactId(), getVersion(), "pom", getChain());
        dep.getRepositories().addAll(getRepositories());
        return dep;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean equals(Object o) {
        if (o instanceof Dependency) {
            return super.equals(o);
        } else {
            return false;
        }
    }
}
