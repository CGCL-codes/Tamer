package tinlizard.web;

import tinlizard.annotation.security.RolesAllowed;
import tinlizard.model.Codeline;
import tinlizard.model.View;
import tinlizard.util.Messages;
import java.util.Collection;
import java.util.Date;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.Exported;

/**
 * Decorate View for Stapler.
 */
public final class ViewWeb extends ObjectWeb<View> {

    private final View subject;

    public ViewWeb(final View c) {
        if (c == null) {
            throw new IllegalStateException(Messages.error_1010());
        }
        this.subject = c;
    }

    protected View getSubject() {
        return this.subject;
    }

    public Integer getId() {
        return subject.getId();
    }

    public String getName() {
        return subject.getName();
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
    public CodelineCollectionWeb getCodelines() {
        Collection<Codeline> allCodelines = this.subject.getCodelines();
        if (((allCodelines != null) && !allCodelines.isEmpty())) {
            return new CodelineCollectionWeb(allCodelines, Messages._AllCodelines());
        } else {
            return null;
        }
    }

    public CodelineCollectionWeb getActiveCodelines() {
        Collection<Codeline> codelines = this.subject.getActiveCodelines();
        if (codelines != null) {
            return new CodelineCollectionWeb(codelines, Messages._ActiveCodelines());
        } else {
            return null;
        }
    }

    @RolesAllowed(RoleNames.USER)
    public void doDelete(final StaplerRequest request, final StaplerResponse response) throws Exception {
        subject.delete();
        response.sendRedirect(request.getContextPath());
    }

    @RolesAllowed(RoleNames.USER)
    public void doUpdate(final StaplerRequest request, final StaplerResponse response) throws Exception {
        request.bindParameters(subject, "view.");
        subject.update();
        gotoMyIndex(request, response);
    }
}
