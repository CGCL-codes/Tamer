package org.extwind.osgi.console.repository.components.repository;

import java.util.Arrays;
import java.util.List;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ClientBehaviorSupport;
import org.apache.tapestry5.services.Request;
import org.extwind.osgi.console.service.ConsoleConstants;
import org.extwind.osgi.console.service.LaunchService;
import org.extwind.osgi.console.service.Repository;
import org.extwind.osgi.tapestry.annotations.DynamicInject;
import org.extwind.osgi.tapestry.service.Reporter;
import org.extwind.osgi.tapestry.service.ZoneUpdater;

/**
 * @author Donf Yang
 * 
 */
@IncludeJavaScriptLibrary("classpath:org/extwind/osgi/console/repository/repository.js")
@IncludeStylesheet("classpath:org/extwind/osgi/console/repository/repository.css")
public class ShowAll {

    @Property(write = false)
    @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
    private boolean readonly;

    @Property(write = false)
    @Parameter
    private boolean extra;

    @Parameter
    private boolean flash;

    @Property(write = false)
    @Parameter(required = true, allowNull = false, autoconnect = true)
    private ZoneUpdater zoneUpdater;

    @Property(write = false)
    @Parameter(required = true, allowNull = false, autoconnect = true)
    private Reporter reporter;

    @Environmental
    private RenderSupport renderSupport;

    @Property
    private String currentRepository;

    @Property
    private String addedRepository;

    @DynamicInject
    private LaunchService launchService;

    @Inject
    private Request request;

    @Component
    private Form addRepositoryForm;

    @Property
    @Inject
    private Block repositoriesBlock, unsupportRepositoryBlock, repositoryNotLoadBlock;

    @Component
    private Form repositoryOptionsForm;

    @Inject
    private ComponentResources resources;

    @Environmental
    private ClientBehaviorSupport clientBehaviorSupport;

    @Persist
    private String location;

    private static final String EVENT_INTERNAL_SHOWREPOSITORY = "internalShowRepository";

    private static final String ZONE_DEFAULTBUNDLEFILES = "defaultBundleFilesZone";

    private static final String ZONE_SHOW_ALL = "zone_show_all";

    void setupRender() {
        if (flash) {
            location = null;
        }
    }

    public Object getRenderBlock() {
        try {
            if (launchService.getRepositoryFactory() == null) {
                return unsupportRepositoryBlock;
            }
        } catch (Exception e) {
            return e.toString();
        }
        return repositoriesBlock;
    }

    public List<String> getRepositories() throws Exception {
        List<String> locations = Arrays.asList(launchService.getRepositoryFactory().getRepositoryLocations());
        return locations;
    }

    Object onInternalShowRepository(String location, String showAllZone) throws Exception {
        this.location = location;
        zoneUpdater.add(showAllZone, repositoriesBlock);
        Repository repository = launchService.getRepositoryFactory().getRepository(location);
        if (repository == null) {
            throw new IllegalStateException("Repository is not exists - " + location);
        }
        if (repository.isLoaded() && repository.isOutOfDate()) {
            reporter.warn("Repository is out of date - " + location);
        }
        zoneUpdater.add(reporter.getZone(), reporter);
        resources.triggerEvent(ConsoleConstants.EVENT_SHOW_REPOSITORY, new Object[] { location, zoneUpdater }, null);
        return zoneUpdater.toMultiZoneUpdate();
    }

    public String getCurrentRepositoryLinkId() {
        String currentRepoHrefId = renderSupport.allocateClientId(currentRepository);
        Link link = resources.createEventLink(EVENT_INTERNAL_SHOWREPOSITORY, currentRepository, ZONE_SHOW_ALL);
        clientBehaviorSupport.linkZone(currentRepoHrefId, ZONE_SHOW_ALL, link);
        return currentRepoHrefId;
    }

    public String getShowAllZone() {
        return ZONE_SHOW_ALL;
    }

    public Repository getRepository() {
        try {
            return launchService.getRepositoryFactory().getRepository(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRowClass() {
        if (currentRepository.equals(location)) {
            return "selectedRepository";
        }
        return null;
    }

    public boolean isSelectedRepository() {
        if (currentRepository.equals(location)) {
            return true;
        }
        return false;
    }

    public String getRepositoryOptionsFormId() {
        return repositoryOptionsForm.getClientId();
    }

    public String getCheckedStr() {
        if (isSelectedRepository()) {
            return "checked=true";
        }
        return null;
    }

    Object onException(Throwable cause) {
        cause.printStackTrace();
        reporter.error(cause);
        return new MultiZoneUpdate(reporter.getZone(), reporter);
    }

    Object onSubmitFromAddRepositoryForm() {
        if (addRepositoryForm.getHasErrors()) {
            List<String> errors = addRepositoryForm.getDefaultTracker().getErrors();
            for (String msg : errors) {
                reporter.error(msg);
            }
            addRepositoryForm.clearErrors();
            return new MultiZoneUpdate(reporter.getZone(), reporter);
        }
        try {
            Repository description = launchService.getRepositoryFactory().addRepository(addedRepository);
            this.resources.triggerEvent(ConsoleConstants.EVENT_ADD_REPOSITORY, new Object[] { description, zoneUpdater }, null);
            reporter.success("Added Repository: " + description.getLocation());
        } catch (Exception e) {
            reporter.error(e.getMessage());
        }
        zoneUpdater.add(reporter.getZone(), reporter);
        zoneUpdater.add(ZONE_SHOW_ALL, repositoriesBlock);
        return zoneUpdater.toMultiZoneUpdate();
    }

    Object onSubmitFromRepositoryOptionsForm() {
        String[] repositories = request.getParameters("repository");
        if (repositories == null || repositories.length == 0) {
            return null;
        }
        String opt = request.getParameter("repositoryAction");
        byte optCode = -1;
        try {
            optCode = Byte.parseByte(opt);
        } catch (Exception e) {
            reporter.error("Unknown operation - " + opt);
            return new MultiZoneUpdate(reporter.getZone(), reporter);
        }
        switch(optCode) {
            case ConsoleConstants.OPERATION_REMOVE_REPOSITORY:
                internalRemoveRepositoryAction(repositories);
                break;
            case ConsoleConstants.OPERATION_LOAD_REPOSITORY:
                internalLoadRepositoryAction(repositories);
                break;
            case ConsoleConstants.OPERATION_UPDATE_REPOSITORY:
                internalUpdateRepositoryAction(repositories);
                break;
            default:
                reporter.error("Unknown operation - " + opt);
                return new MultiZoneUpdate(reporter.getZone(), reporter);
        }
        zoneUpdater.add(ZONE_SHOW_ALL, repositoriesBlock);
        zoneUpdater.add(reporter.getZone(), reporter);
        return zoneUpdater.toMultiZoneUpdate();
    }

    private void internalUpdateRepositoryAction(String[] repositoryLocations) {
        for (String repositoryLocation : repositoryLocations) {
            try {
                Repository repository = launchService.getRepositoryFactory().getRepository(repositoryLocation);
                if (repository == null) {
                    reporter.error("Repository is not exists - " + repositoryLocation);
                    continue;
                }
                repository.update();
                reporter.success("Update repository succefully - " + repositoryLocation);
                this.resources.triggerEvent(ConsoleConstants.EVENT_UPDATE_REPOSITORY, new Object[] { repository, zoneUpdater }, null);
            } catch (Exception e) {
                reporter.error("Unable to update repository - " + e);
                e.printStackTrace();
            }
        }
    }

    private void internalRemoveRepositoryAction(String[] repositoryLocations) {
        for (String repositoryLocation : repositoryLocations) {
            try {
                launchService.getRepositoryFactory().removeRepository(repositoryLocation);
                reporter.success("Remove repository succefully - " + repositoryLocation);
                this.resources.triggerEvent(ConsoleConstants.EVENT_REMOVE_REPOSITORY, new Object[] { repositoryLocation, zoneUpdater }, null);
                if (repositoryLocation.equalsIgnoreCase(location)) {
                    location = null;
                }
            } catch (Exception e) {
                reporter.error("Error on removing repository - " + e);
                e.printStackTrace();
            }
        }
    }

    private void internalLoadRepositoryAction(String[] repositoryLocations) {
        for (String repositoryLocation : repositoryLocations) {
            try {
                Repository repository = launchService.getRepositoryFactory().getRepository(repositoryLocation);
                if (repository.isLoaded()) {
                    reporter.info("Repository has already loaded - " + repositoryLocation);
                } else {
                    repository.load();
                    reporter.success("Loaded repository - " + repositoryLocation);
                    this.resources.triggerEvent(ConsoleConstants.EVENT_LOAD_REPOSITORY, new Object[] { repository, zoneUpdater }, null);
                }
            } catch (Exception e) {
                reporter.error("Error on loading repository - " + e);
                e.printStackTrace();
            }
        }
    }

    public byte getRemoveRepositoryActionCode() {
        return ConsoleConstants.OPERATION_REMOVE_REPOSITORY;
    }

    public byte getLoadRepositoryActionCode() {
        return ConsoleConstants.OPERATION_LOAD_REPOSITORY;
    }

    public byte getUpdateRepositoryActionCode() {
        return ConsoleConstants.OPERATION_UPDATE_REPOSITORY;
    }
}
