package tinlizard.web;

import tinlizard.annotation.security.RolesAllowed;
import tinlizard.model.Codeline;
import tinlizard.model.Dependency;
import tinlizard.model.Policy;
import tinlizard.model.User;
import tinlizard.model.View;
import tinlizard.util.Messages;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.Exported;

/**
 * Decorate Codeline for Stapler.
 */
public final class CodelineWeb extends ObjectWeb<Codeline> {

    private Codeline subject;

    public CodelineWeb(final Codeline pb) {
        if (pb == null) {
            throw new IllegalStateException(Messages.error_1011());
        }
        this.subject = pb;
    }

    protected Codeline getSubject() {
        return this.subject;
    }

    public Integer getId() {
        return this.subject.getId();
    }

    public String getName() {
        return this.subject.getName();
    }

    @Exported
    public String getScmConnection() {
        return this.subject.getScmConnection();
    }

    @Exported
    public String getGroupId() {
        return this.subject.getGroupId();
    }

    @Exported
    public String getArtifactId() {
        return this.subject.getArtifactId();
    }

    @Exported
    public String getVersion() {
        return this.subject.getVersion();
    }

    @Exported
    public ProjectWeb getProject() {
        return new ProjectWeb(this.subject.getProject());
    }

    @Exported
    public UserWeb getOwner() {
        return new UserWeb(subject.getOwner());
    }

    @Exported
    public PolicyWeb getPolicy() {
        return new PolicyWeb(subject.getPolicy());
    }

    public boolean isMainLine() {
        return this.subject.isMainLine();
    }

    @Exported
    public Boolean getMainLine() {
        return this.subject.getMainLine();
    }

    @Exported
    public String getDescription() {
        return subject.getDescription();
    }

    @Exported
    public Date getCreated() {
        return subject.getCreated();
    }

    @Exported
    public String getCreatedBy() {
        return subject.getCreatedBy();
    }

    @Exported
    public Date getLastModified() {
        return subject.getLastModified();
    }

    @Exported
    public String getLastModifiedBy() {
        return subject.getLastModifiedBy();
    }

    @Exported(visibility = 1)
    public ViewWeb getView() {
        return new ViewWeb(this.subject.getView());
    }

    @Exported(visibility = 1)
    public CodelineCollectionWeb getConsumers() {
        Collection<Codeline> allCodelines = this.subject.findAllConsumers(true);
        if (allCodelines != null) {
            return new CodelineCollectionWeb(allCodelines, Messages._Consumers());
        } else {
            return null;
        }
    }

    @Exported(name = "dependencys")
    public DependencyCollectionWeb getDependencies() {
        Collection<Dependency> dependencies = subject.findAllDependencies();
        if (dependencies != null) {
            return new DependencyCollectionWeb(dependencies, Messages._Dependencies());
        } else {
            return null;
        }
    }

    public DependencyWeb getDependency(final String name) {
        if (StringUtils.isNotBlank(name)) {
            Collection<Dependency> dependencies = subject.findAllDependencies();
            if (dependencies != null) {
                for (Dependency dependency : dependencies) {
                    if (name.equals(dependency.getName())) {
                        return new DependencyWeb(dependency);
                    }
                }
            }
        }
        return null;
    }

    @RolesAllowed(RoleNames.USER)
    public void doDelete(final StaplerRequest request, final StaplerResponse response) throws Exception {
        String projectName = subject.getProject().getName();
        subject.delete();
        response.sendRedirect(request.getContextPath() + "/project/" + projectName);
    }

    @RolesAllowed(RoleNames.USER)
    public void doUpdate(final StaplerRequest request, final StaplerResponse response) throws Exception {
        request.bindParameters(subject, "codeline.");
        View view = new View();
        request.bindParameters(view, "view.");
        if (StringUtils.isNotBlank(view.getName())) {
            view = View.findByName(view.getName());
            if (view != null) {
                subject.setView(view);
            }
        }
        User user = new User();
        request.bindParameters(user, "owner.");
        if (StringUtils.isNotBlank(user.getName())) {
            user = User.getUserByName(user.getName(), true);
            if (user != null) {
                subject.setOwner(user);
            }
        }
        Policy policy = new Policy();
        request.bindParameters(policy, "policy.");
        if (StringUtils.isNotBlank(policy.getName())) {
            policy = Policy.findByName(policy.getName());
            if (policy != null) {
                subject.setPolicy(policy);
            }
        }
        subject.update();
        gotoMyIndex(request, response);
    }

    @RolesAllowed(RoleNames.USER)
    public void doRefresh(final StaplerRequest request, final StaplerResponse response) throws Exception {
        subject.refresh();
        gotoMyIndex(request, response);
    }
}
