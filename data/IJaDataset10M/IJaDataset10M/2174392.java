package org.gbif.ipt.action.manage;

import org.gbif.ipt.config.Constants;
import org.gbif.ipt.model.Extension;
import org.gbif.ipt.model.ExtensionMapping;
import org.gbif.ipt.model.Organisation;
import org.gbif.ipt.model.Resource;
import org.gbif.ipt.model.Resource.CoreRowType;
import org.gbif.ipt.model.User;
import org.gbif.ipt.model.User.Role;
import org.gbif.ipt.model.voc.PublicationStatus;
import org.gbif.ipt.service.DeletionNotAllowedException;
import org.gbif.ipt.service.InvalidConfigException;
import org.gbif.ipt.service.PublicationException;
import org.gbif.ipt.service.RegistryException;
import org.gbif.ipt.service.admin.ExtensionManager;
import org.gbif.ipt.service.admin.RegistrationManager;
import org.gbif.ipt.service.admin.UserAccountManager;
import org.gbif.ipt.service.manage.ResourceManager;
import org.gbif.ipt.task.StatusReport;
import org.gbif.ipt.utils.FileUtils;
import org.gbif.ipt.validation.EmlValidator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;

public class OverviewAction extends ManagerBaseAction {

    private static final String PUBLISHING = "publishing";

    @Inject
    private ResourceManager resourceManager;

    @Inject
    private UserAccountManager userManager;

    @Inject
    private RegistrationManager registrationManager;

    @Inject
    private ExtensionManager extensionManager;

    private List<User> potentialManagers;

    private List<Extension> potentialExtensions;

    private List<Organisation> organisations;

    private final EmlValidator emlValidator = new EmlValidator();

    private boolean missingMetadata;

    private boolean missingRegistrationMetadata;

    private StatusReport report;

    private Date now;

    private boolean unpublish = false;

    public String addmanager() throws Exception {
        if (resource == null) {
            return NOT_FOUND;
        }
        User u = userManager.get(id);
        if (u != null && !potentialManagers.contains(u)) {
            addActionError(getText("manage.overview.manager.not.available", new String[] { id }));
        } else if (u != null) {
            resource.addManager(u);
            addActionMessage(getText("manage.overview.user.added", new String[] { u.getName() }));
            saveResource();
            potentialManagers.remove(u);
        }
        return execute();
    }

    public String cancel() throws Exception {
        if (resource == null) {
            return NOT_FOUND;
        }
        try {
            resourceManager.cancelPublishing(resource.getShortname(), this);
            addActionMessage(getText("manage.overview.stopped.publishing", new String[] { resource.toString() }));
        } catch (PublicationException e) {
            String reason = "";
            if (e.getMessage() != null) {
                reason = e.getMessage();
            }
            addActionError(getText("manage.overview.failed.stop.publishing", new String[] { reason }));
            return ERROR;
        }
        return execute();
    }

    @Override
    public String delete() {
        if (resource == null) {
            return NOT_FOUND;
        }
        try {
            Resource res = resource;
            resourceManager.delete(res);
            addActionMessage(getText("manage.overview.resource.deleted", new String[] { res.toString() }));
            return HOME;
        } catch (IOException e) {
            String msg = getText("manage.resource.delete.failed");
            log.error(msg, e);
            addActionError(msg);
            addActionExceptionWarning(e);
        } catch (DeletionNotAllowedException e) {
            String msg = getText("manage.resource.delete.failed");
            log.error(msg, e);
            addActionError(msg);
            addActionExceptionWarning(e);
        }
        return SUCCESS;
    }

    public String delmanager() throws Exception {
        if (resource == null) {
            return NOT_FOUND;
        }
        User u = userManager.get(id);
        if (u == null || !resource.getManagers().contains(u)) {
            addActionError(getText("manage.overview.manager.not.available", new String[] { id }));
        } else {
            resource.getManagers().remove(u);
            addActionMessage(getText("manage.overview.user.removed", new String[] { u.getName() }));
            saveResource();
            potentialManagers.add(u);
        }
        return execute();
    }

    @Override
    public String execute() throws Exception {
        if (resource == null) {
            return NOT_FOUND;
        }
        return SUCCESS;
    }

    /**
   * Validate whether or not to show a confirmation message to overwrite the file(s) recently uploaded.
   *
   * @return true if a file exist in the user session. False otherwise.
   */
    public boolean getConfirmOverwrite() {
        return session.get(Constants.SESSION_FILE) != null;
    }

    /**
   * Calculate the size of the DwC-A file.
   *
   * @return the size (human readable) of the DwC-A file.
   */
    public String getDwcaFormattedSize() {
        return FileUtils.formatSize(resourceManager.getDwcaSize(resource), 2);
    }

    /**
   * Calculate the size of the EML file.
   *
   * @return the size (human readable) of the EML file.
   */
    public String getEmlFormattedSize() {
        return FileUtils.formatSize(resourceManager.getEmlSize(resource), 2);
    }

    public boolean getMissingBasicMetadata() {
        return !emlValidator.isValid(resource.getEml(), "basic");
    }

    /**
   * @return true if there are something missing metadata. False otherwise.
   */
    public boolean getMissingRegistrationMetadata() {
        return missingRegistrationMetadata;
    }

    public Date getNow() {
        return now;
    }

    public List<Organisation> getOrganisations() {
        return organisations;
    }

    public List<Extension> getPotentialExtensions() {
        return potentialExtensions;
    }

    public List<User> getPotentialManagers() {
        return potentialManagers;
    }

    public StatusReport getReport() {
        return report;
    }

    /**
   * Calculate the size of the RTF file.
   *
   * @return return the size (human readable) of the RTF file.
   */
    public String getRtfFormattedSize() {
        return FileUtils.formatSize(resourceManager.getRtfSize(resource), 2);
    }

    public boolean isMissingMetadata() {
        return missingMetadata;
    }

    public boolean isRtfFileExisting() {
        return resourceManager.isRtfExisting(resource.getShortname());
    }

    public String locked() throws Exception {
        now = new Date();
        if (report != null && report.isCompleted()) {
            addActionMessage(getText("manage.overview.resource.published"));
            return "cancel";
        }
        return SUCCESS;
    }

    public String makePrivate() throws Exception {
        if (resource == null) {
            return NOT_FOUND;
        }
        if (PublicationStatus.PUBLIC == resource.getStatus()) {
            if (unpublish) {
                try {
                    resourceManager.visibilityToPrivate(resource);
                    addActionMessage(getText("manage.overview.changed.publication.status", new String[] { resource.getStatus().toString() }));
                } catch (InvalidConfigException e) {
                    log.error("Cant unpublish resource " + resource, e);
                }
            } else {
                addActionWarning(getText("manage.overview.resource.invalid.operation", new String[] { resource.getShortname(), resource.getStatus().toString() }));
            }
        } else {
            addActionWarning(getText("manage.overview.resource.invalid.operation", new String[] { resource.getShortname(), resource.getStatus().toString() }));
        }
        return execute();
    }

    public String makePublic() throws Exception {
        if (resource == null) {
            return NOT_FOUND;
        }
        if (PublicationStatus.PRIVATE == resource.getStatus()) {
            try {
                resourceManager.visibilityToPublic(resource);
                addActionMessage(getText("manage.overview.changed.publication.status", new String[] { resource.getStatus().toString() }));
            } catch (InvalidConfigException e) {
                log.error("Cant publish resource " + resource, e);
            }
        } else {
            addActionWarning(getText("manage.overview.resource.invalid.operation", new String[] { resource.getShortname(), resource.getStatus().toString() }));
        }
        return execute();
    }

    private boolean minimumRegistryInfo(Resource resource) {
        if (resource == null) {
            return false;
        }
        if (resource.getEml() == null) {
            return false;
        }
        if (resource.getCreator() == null) {
            return false;
        }
        if (resource.getCreator().getEmail() == null) {
            return false;
        }
        if (!resource.isPublished()) {
            return false;
        }
        return true;
    }

    @Override
    public void prepare() throws Exception {
        super.prepare();
        if (resource != null) {
            report = resourceManager.status(resource.getShortname());
            potentialManagers = userManager.list(Role.Publisher);
            potentialManagers.addAll(userManager.list(Role.Manager));
            for (User u : resource.getManagers()) {
                potentialManagers.remove(u);
            }
            organisations = registrationManager.list();
            if (resource.hasCore()) {
                potentialExtensions = extensionManager.list(resource.getCoreRowType());
                potentialExtensions.add(0, extensionManager.get(resource.getCoreRowType()));
            } else if (resource.getSources().isEmpty()) {
                potentialExtensions = new ArrayList<Extension>();
            } else {
                if (resource.getCoreType() == null || "Other".equals(resource.getCoreType()) || resource.getCoreType().length() == 0) {
                    potentialExtensions = extensionManager.listCore();
                } else {
                    potentialExtensions = new ArrayList<Extension>();
                    if (resource.getCoreType().equalsIgnoreCase(CoreRowType.CHECKLIST.toString())) {
                        potentialExtensions.add(extensionManager.get(Constants.DWC_ROWTYPE_TAXON));
                    } else if (resource.getCoreType().equalsIgnoreCase(CoreRowType.OCCURRENCE.toString())) {
                        potentialExtensions.add(extensionManager.get(Constants.DWC_ROWTYPE_OCCURRENCE));
                    }
                }
            }
            missingMetadata = !emlValidator.isValid(resource.getEml(), null);
            missingRegistrationMetadata = !minimumRegistryInfo(resource);
            for (ExtensionMapping em : resource.getCoreMappings()) {
                if (em.getFields().isEmpty()) {
                    resource.deleteMapping(em);
                }
            }
        }
    }

    public String publish() throws Exception {
        if (resource == null) {
            return NOT_FOUND;
        }
        try {
            if (resourceManager.publish(resource, this)) {
                addActionMessage(getText("manage.overview.publishing.resource.version", new String[] { Integer.toString(resource.getEmlVersion()) }));
                return PUBLISHING;
            } else {
                if (resource.hasMappedData()) {
                    addActionWarning(getText("manage.overview.no.data.archive.generated"));
                } else {
                    addActionWarning(getText("manage.overview.data.missing"));
                }
                addActionMessage(getText("manage.overview.published.eml", new String[] { String.valueOf(resource.getEmlVersion()) }));
                missingRegistrationMetadata = !minimumRegistryInfo(resource);
                return SUCCESS;
            }
        } catch (PublicationException e) {
            if (PublicationException.TYPE.LOCKED == e.getType()) {
                addActionWarning(getText("manage.overview.resource.being.published"));
            } else {
                addActionWarning(getText("manage.overview.publishing.error"), e);
            }
        } catch (Exception e) {
            log.error("Error publishing resource", e);
            addActionWarning(getText("manage.overview.publishing.error"), e);
        }
        return ERROR;
    }

    public String registerResource() throws Exception {
        if (resource == null) {
            return NOT_FOUND;
        }
        if (PublicationStatus.PUBLIC == resource.getStatus()) {
            if (unpublish) {
                addActionWarning(getText("manage.overview.resource.invalid.operation", new String[] { resource.getShortname(), resource.getStatus().toString() }));
            } else {
                if (getCurrentUser().hasRegistrationRights()) {
                    Organisation org = null;
                    try {
                        org = registrationManager.get(id);
                        if (org == null) {
                            return execute();
                        }
                        resourceManager.register(resource, org, registrationManager.getIpt());
                        addActionMessage(getText("manage.overview.resource.registered", new String[] { org.getName() }));
                    } catch (RegistryException e) {
                        log.error("Cant register resource " + resource + " with organisation " + org, e);
                        addActionError(getText("manage.overview.failed.resource.registration"));
                    }
                } else {
                    addActionError(getText("manage.resource.status.registration.forbidden"));
                }
            }
        } else {
            addActionWarning(getText("manage.overview.resource.invalid.operation", new String[] { resource.getShortname(), resource.getStatus().toString() }));
        }
        return execute();
    }

    public void setUnpublish(String unpublish) {
        this.unpublish = StringUtils.trimToNull(unpublish) != null;
    }
}
