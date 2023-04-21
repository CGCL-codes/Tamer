package com.mgensystems.mdss.dependency;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MavenDependency {

    protected String groupId = "";

    protected String artifactId = "";

    protected String version = "";

    protected String scope = "";

    protected boolean common = false;

    protected String location = "";

    public MavenDependency() {
    }

    public MavenDependency(String dependency) {
        parseString(dependency);
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    protected void parseString(String dependency) {
        Matcher match = Pattern.compile("<dependency><groupId>(.*)<groupId><artifactId>(.*)</artifactId><version>(.*)</version></dependency>").matcher(dependency);
        if (match.find()) {
            this.setGroupId(match.group(1));
            this.setArtifactId(match.group(2));
            this.setVersion(match.group(3));
        }
    }

    public String toString() {
        return getGroupId() + ":" + getArtifactId() + ":" + getVersion();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MavenDependency)) {
            return false;
        } else {
            return this.toString().equalsIgnoreCase(((MavenDependency) o).toString());
        }
    }

    public String getNonVersion() {
        return getGroupId() + ":" + getArtifactId();
    }

    public String getMavenFormat(boolean includeVersion) {
        String scopeStr = "";
        StringBuffer ret = new StringBuffer();
        ret.append("<dependency><groupId>");
        ret.append(getGroupId());
        ret.append("<groupId><artifactId>");
        ret.append(getArtifactId());
        ret.append("</artifactId>");
        if (includeVersion) {
            ret.append("<version>");
            ret.append(getVersion());
            ret.append("</version>");
        }
        if (scope.length() > 0 && includeVersion) {
            ret.append("<scope>");
            ret.append(getScope());
            ret.append("</scope>");
        }
        ret.append("</dependency>");
        return ret.toString();
    }

    public String getMavenFormat() {
        return this.getMavenFormat(true);
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
