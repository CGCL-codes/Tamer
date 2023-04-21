package au.gov.nla.aons.logging.listeners;

import java.util.Calendar;
import au.gov.nla.aons.constants.LogComponentNames;
import au.gov.nla.aons.constants.LogLevels;
import au.gov.nla.aons.logging.LoggingManager;
import au.gov.nla.aons.logging.domain.LogMessage;
import au.gov.nla.aons.repository.RepositoryManager;
import au.gov.nla.aons.repository.domain.FormatIdentificationMetadata;
import au.gov.nla.aons.repository.domain.Repository;
import au.gov.nla.aons.repository.util.RepositoryListener;

public class LoggingRepositoryListener implements RepositoryListener {

    private LoggingManager loggingManager;

    private RepositoryManager repositoryManager;

    public void initialize() {
        repositoryManager.addRepositoryListener(this);
    }

    public void notifyCrawlCompleted(Repository repository) {
    }

    public void notifyFormatIdentificationLinked(FormatIdentificationMetadata formatIdent) {
    }

    public void notifyFormatIdentificationUnlinked(FormatIdentificationMetadata formatIdent) {
    }

    public void notifyManualCrawlStarted(Repository repository) {
    }

    public void notifyRepositoryCreated(Repository repository) {
        LogMessage logMessage = new LogMessage();
        logMessage.setComponentName(LogComponentNames.REPOSITORY);
        logMessage.setDateCreated(Calendar.getInstance());
        logMessage.setLogLevel(LogLevels.INFO);
        String resourceString = createResourceString(repository);
        logMessage.setSubject("Repository " + resourceString + " Created.");
        logMessage.setMessage("Repository " + resourceString + " Created.");
        loggingManager.createLogMessage(logMessage);
    }

    protected String createResourceString(Repository repository) {
        return "@Repository:" + repository.getId() + ":" + repository.getName() + "@";
    }

    public void notifyRepositoryDeleted(Repository repository) {
    }

    public void notifyRepositoryUpdated(Repository repository) {
    }

    public LoggingManager getLoggingManager() {
        return loggingManager;
    }

    public void setLoggingManager(LoggingManager loggingManager) {
        this.loggingManager = loggingManager;
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }
}
