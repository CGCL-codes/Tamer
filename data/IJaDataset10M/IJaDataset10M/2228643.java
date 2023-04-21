package net.sf.buildbox.buildrobot.model;

import net.sf.buildbox.reactor.model.JobId;
import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement
public class CodeRoot extends Identifiable {

    public static final String SCHEDULING_ON_COMMIT_COMMAND_PROPERTY = "scheduling.on-commit-command";

    private String displayName;

    private VcsLocation vcsLocation;

    @Deprecated
    private String schedulingStrategy;

    private ProjectStatus status = ProjectStatus.ACTIVE;

    private List<InfoLink> links = new ArrayList<InfoLink>();

    private Map<String, String> buildProperties = new HashMap<String, String>();

    private Set<CodeRootDependency> dependencies = new HashSet<CodeRootDependency>();

    @XmlAttribute
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @XmlTransient
    public VcsLocation getVcsLocation() {
        return vcsLocation;
    }

    public void setVcsLocation(VcsLocation vcsLocation) {
        this.vcsLocation = vcsLocation;
    }

    public void setVcsLocationStr(String vcsLocationStr) {
        this.vcsLocation = VcsLocation.valueOf(vcsLocationStr);
    }

    @Deprecated
    public String getSchedulingStrategy() {
        return schedulingStrategy;
    }

    @Deprecated
    public void setSchedulingStrategy(String schedulingStrategy) {
        this.schedulingStrategy = schedulingStrategy;
    }

    @XmlElementWrapper
    @XmlElement(name = "link")
    public List<InfoLink> getLinks() {
        return links;
    }

    public void setLinks(List<InfoLink> links) {
        this.links = links;
    }

    public Map<String, String> getBuildProperties() {
        return buildProperties;
    }

    public void setBuildProperties(Map<String, String> buildProperties) {
        this.buildProperties = buildProperties;
    }

    public String getBuildProperty(String propertyName) {
        return buildProperties.get(propertyName);
    }

    public void setBuildProperty(String name, String value) {
        buildProperties.put(name, value);
    }

    /**
     * @return name of the target to schedule when commit happens
     */
    public String getOnCommitTargetName() {
        return buildProperties.get(SCHEDULING_ON_COMMIT_COMMAND_PROPERTY);
    }

    @XmlAttribute
    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    @XmlElementWrapper
    @XmlElement(name = "dependency")
    public Set<CodeRootDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<CodeRootDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public void addDependency(CodeRootDependency dependency) {
        dependencies.add(dependency);
    }

    @Deprecated
    public Set<JobId> getTargetDependenciesDirect(String targetName) {
        final Set<JobId> deps = new HashSet<JobId>();
        for (CodeRootDependency dependency : getActiveDirectDependencies(targetName)) {
            deps.add(dependency.getRequiredJobId());
        }
        return deps;
    }

    public Set<CodeRootDependency> getActiveDirectDependencies(String targetName) {
        final Set<CodeRootDependency> deps = new HashSet<CodeRootDependency>();
        for (CodeRootDependency dependency : dependencies) {
            if (!targetName.equals(dependency.getTarget())) continue;
            if (dependency.getUsedFileCount() < 1) continue;
            deps.add(dependency);
        }
        return deps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeRoot)) return false;
        final CodeRoot codeRoot = (CodeRoot) o;
        if (!buildProperties.equals(codeRoot.buildProperties)) return false;
        if (!displayName.equals(codeRoot.displayName)) return false;
        if (!links.equals(codeRoot.links)) return false;
        if (status != null) {
            if (!status.equals(codeRoot.status)) return false;
        }
        if (!vcsLocation.equals(codeRoot.vcsLocation)) return false;
        if (!dependencies.equals(codeRoot.dependencies)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = displayName.hashCode();
        result = 31 * result + vcsLocation.hashCode();
        if (status != null) {
            result = 31 * result + status.hashCode();
        }
        result = 31 * result + links.hashCode();
        result = 31 * result + buildProperties.hashCode();
        result = 31 * result + dependencies.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CodeRoot{");
        sb.append(vcsLocation);
        sb.append(", status='").append(status).append('\'');
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", buildProperties=").append(buildProperties);
        sb.append(", links=").append(links);
        sb.append(", dependencies=").append(dependencies);
        sb.append('}');
        return sb.toString();
    }
}
