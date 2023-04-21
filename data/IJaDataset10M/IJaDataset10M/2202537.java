package com.ivis.xprocess.web.framework;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.core.Portfolio;
import com.ivis.xprocess.framework.DataSource;
import com.ivis.xprocess.framework.XchangeElement;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.exceptions.DuplicateAccountNamesException;
import com.ivis.xprocess.framework.exceptions.MinimumVersionException;
import com.ivis.xprocess.framework.exceptions.NoRootLogicalContainerException;
import com.ivis.xprocess.framework.impl.XchangeElementImpl;
import com.ivis.xprocess.framework.logging.LoggerConfigurator;
import com.ivis.xprocess.framework.schema.JarSchemaSource;
import com.ivis.xprocess.framework.schema.SchemaBuilder;
import com.ivis.xprocess.framework.vcs.Proxy;
import com.ivis.xprocess.framework.vcs.VcsProviderFactory;
import com.ivis.xprocess.framework.vcs.auth.VCSPasswordAuth;
import com.ivis.xprocess.framework.vcs.exceptions.VCSAuthenticationException;
import com.ivis.xprocess.framework.vcs.exceptions.VCSException;
import com.ivis.xprocess.framework.vcs.impl.svn.SubversionProvider;
import com.ivis.xprocess.framework.xml.IPersistenceHelper;
import com.ivis.xprocess.framework.xml.PersistenceHelper;
import com.ivis.xprocess.web.commands.RescheduleDSCommand;
import com.ivis.xprocess.web.commands.UpdateMasterDSCommand;
import com.ivis.xprocess.web.elements.TransientPerson;
import com.ivis.xprocess.web.queue.AbstractQueuedCommand;
import com.ivis.xprocess.web.queue.WaitingExecutor;
import com.ivis.xprocess.web.queue.XReadWriteLock;

public class MasterDatasource implements IMasterDatasource {

    private static final Logger logger = Logger.getLogger(MasterDatasource.class.getName());

    private static HashMap<String, SessionDatasource> sessions = new HashMap<String, SessionDatasource>();

    private static IPersistenceHelper persistenceHelper;

    private WaitingExecutor waitingExecutor = new WaitingExecutor();

    private static MasterScheduler masterScheduler;

    private final ReadWriteLock myReadWriteLock = new XReadWriteLock();

    private UpdateMasterDSCommand update_command;

    private RescheduleDSCommand reschedule_command;

    private static String baseDir = "";

    private static String dsDir = "";

    private static String reposUrl = "";

    private static String dsName = "";

    private static String reposUsername = "";

    private static String reposPassword = "";

    private static String licenseLocation = "";

    private static boolean initialised = false;

    private static WebVCSManager vcsManager;

    public MasterDatasource() {
    }

    public void initialise(ServletContext servletContext) {
        logger.log(Level.INFO, "Initialise MasterDatasource: " + logger.getLevel());
        if (initialised) {
            return;
        }
        String webinfDir = servletContext.getRealPath("/WEB-INF");
        baseDir = webinfDir + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator;
        setupLogger();
        logger.log(Level.INFO, "Base dir = " + baseDir);
        try {
            waitingExecutor.setReadWriteLock((XReadWriteLock) myReadWriteLock);
            waitingExecutor.init();
            logger.log(Level.INFO, "Queue initialised");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to initialise queue");
        }
        masterScheduler = new MasterScheduler();
        masterScheduler.setSource(this);
        readConfig();
        loadDatasource();
        checkOutDatasource();
        initialised = true;
    }

    /**
	 * Only to be used for JUnit tests to initialise the MasterDatasource
	 */
    public void initialise(String junitBaseDir) {
        baseDir = junitBaseDir;
        try {
            System.out.println("Set up Queue " + waitingExecutor);
            waitingExecutor.setReadWriteLock((XReadWriteLock) myReadWriteLock);
            waitingExecutor.init();
        } catch (Exception e) {
            System.err.println("Unable to initialise queue");
        }
        masterScheduler = new MasterScheduler();
        masterScheduler.setSource(this);
        readConfig();
        loadDatasource();
        checkOutDatasource();
        initialised = true;
    }

    private void readConfig() {
        String configDirPath = baseDir + "config" + File.separator;
        File configFile = new File(configDirPath + "config.xml");
        if (!configFile.exists()) {
            logger.log(Level.SEVERE, "Unable to locate config file - " + configDirPath);
            throw new RuntimeException("Unable to locate config file");
        }
        System.out.println("Reading config file - " + configFile.getAbsolutePath());
        SAXBuilder builder = new SAXBuilder();
        try {
            Document configDocument = builder.build(configFile);
            Element rootElement = configDocument.getRootElement();
            Element element = rootElement.getChild("datasource_repository_url");
            if (element == null || element.getText().length() == 0) {
                throw new RuntimeException("No Database Repository URL found");
            } else {
                reposUrl = element.getText();
            }
            element = rootElement.getChild("datasource_name");
            if (element == null || element.getText().length() == 0) {
                throw new RuntimeException("No Database Repository URL found");
            } else {
                dsName = element.getText();
            }
            element = rootElement.getChild("repos_username");
            if (element == null || element.getText().length() == 0) {
                throw new RuntimeException("No Repository Username found");
            } else {
                reposUsername = element.getText();
            }
            element = rootElement.getChild("repos_password");
            if (element == null || element.getText().length() == 0) {
                throw new RuntimeException("No Repository password found");
            } else {
                reposPassword = element.getText();
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDatasource() {
        try {
            File reposDir = new File(baseDir + "repos");
            if (!reposDir.exists()) {
                reposDir.mkdir();
            }
            logger.log(Level.INFO, "Loading Datasource: " + reposDir.getAbsolutePath());
            SchemaBuilder.getInstance().buildSchema(new JarSchemaSource());
            dsDir = reposDir + File.separator + dsName;
            persistenceHelper = new PersistenceHelper(dsDir, Portfolio.class, null);
            persistenceHelper.getDataSource().getDescriptor().setVcsType(VcsProviderFactory.SVN);
            persistenceHelper.getDataSource().getDescriptor().setDatasourceURL(reposUrl);
            persistenceHelper.getDataSource().getDescriptor().setUserName(reposUsername);
            persistenceHelper.getDataSource().getDescriptor().setPassword(reposPassword);
        } catch (MinimumVersionException e) {
            e.printStackTrace();
        } catch (NoRootLogicalContainerException e) {
            e.printStackTrace();
        }
        VCSPasswordAuth auth = new VCSPasswordAuth("test", "password");
        try {
            Proxy proxy = null;
            vcsManager = new WebVCSManager(persistenceHelper.getDataSource(), auth, "SVN", proxy, reposUrl);
        } catch (VCSException e1) {
            logger.log(Level.SEVERE, "Can't authenticate VCS - " + reposUrl + " : " + e1.getMessage());
            return;
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Error initializing MasterDS");
            return;
        }
    }

    private void checkOutDatasource() {
        VCSPasswordAuth auth = new VCSPasswordAuth(reposUsername, reposPassword);
        try {
            persistenceHelper.getDataSource().authenticateVCS(auth);
            persistenceHelper.getDataSource().getDescriptor().saveLocal();
        } catch (VCSException e) {
            logger.log(Level.SEVERE, "Problem Loading Datasource: " + e.getMessage());
            return;
        }
        boolean new_dir = false;
        String localSvnDir = dsDir + File.separator + ".svn";
        File vcsdir = new File(localSvnDir);
        if (!vcsdir.exists()) {
            new_dir = true;
        }
        if (!new_dir) {
            SVNURL wcUrl = null;
            SVNStatusClient statusClient = ((SubversionProvider) persistenceHelper.getDataSource().getVcsProvider()).getClientManager().getStatusClient();
            try {
                wcUrl = statusClient.doStatus(new File(dsDir), false).getURL();
            } catch (SVNException e1) {
                logger.log(Level.SEVERE, "Failed to update - " + e1.getMessage());
            }
            String reposUrlString = reposUrl.toString().trim();
            String wcUrlString = wcUrl.toString().trim();
            if (reposUrlString.endsWith(File.separator)) {
                reposUrlString = reposUrlString.substring(0, reposUrlString.length() - 1);
            }
            if (wcUrlString.endsWith(File.separator)) {
                wcUrlString = wcUrlString.substring(0, wcUrlString.length() - 1);
            }
            if (!reposUrlString.equals(wcUrlString)) {
                logger.log(Level.SEVERE, "Repository Url " + reposUrlString + "is not same as Working Copy Url" + wcUrlString + " please delete working copy to continue.");
                return;
            }
            try {
                logger.log(Level.INFO, "Updating " + dsDir + "...");
                vcsManager.update();
                logger.log(Level.INFO, " ...update finished");
            } catch (VCSException e) {
                logger.log(Level.SEVERE, "Failed to update.");
                return;
            }
        } else {
            try {
                logger.log(Level.INFO, "Checking out HEAD to " + dsDir + "...");
                vcsManager.checkoutHead();
                logger.log(Level.INFO, " ...finished checking out HEAD");
            } catch (VCSException e) {
                logger.log(Level.SEVERE, "Failed to check out HEAD - " + e.getMessage());
                return;
            }
        }
        Set<String> rootContainerIds = persistenceHelper.getFileIndex().index();
        persistenceHelper.loadContainers(rootContainerIds);
        masterScheduler.rescheduleAllProjects();
        vcsManager.addVCSTransactionListener(masterScheduler);
    }

    public IPersistenceHelper getPersistenceHelper() {
        return persistenceHelper;
    }

    public SessionDatasource getSessionDatasource(String sessionToken) {
        SessionDatasource sessionDatasource = sessions.get(sessionToken);
        if (sessionDatasource != null) {
            if (!sessionDatasource.hasExpired()) {
                return sessionDatasource;
            } else {
                throw new RuntimeException("Session has expired");
            }
        }
        throw new RuntimeException("No active session found");
    }

    public SessionDatasource getSessionDatasource(String sessionToken, String username, String password) throws RemoteException {
        if (!initialised) {
            throw new RuntimeException("Master Datasource has not been initialised");
        }
        SessionDatasource sessionDatasource = sessions.get(sessionToken);
        if (sessionDatasource != null) {
            if (!sessionDatasource.hasExpired()) {
                return sessionDatasource;
            } else {
                throw new RuntimeException("Session has expired");
            }
        }
        if (sessionDatasource == null) {
            try {
                VCSPasswordAuth auth = new VCSPasswordAuth(username, password);
                DataSource dataSource = persistenceHelper.getDataSource();
                dataSource.getVcsProvider().testConnection(auth);
                sessionDatasource = new SessionDatasource(this, "123456789", auth);
                sessions.put(sessionToken, sessionDatasource);
            } catch (VCSException vcsException) {
                if (vcsException instanceof VCSAuthenticationException) {
                    throw new RemoteException("Authentication failed", vcsException);
                }
                throw new RemoteException("Failed to connect to server", vcsException);
            }
        }
        try {
            beginReadOnlyOperation();
            Person person = sessionDatasource.getDataSource().findPersonByAccountName(username);
            if (person == null) {
                throw new RemoteException("No Person in the Data Source can be found with the account name: " + username);
            }
            TransientElement transientElement = sessionDatasource.getTransientElement(person.getUuid());
            if (transientElement instanceof TransientPerson) {
                sessionDatasource.setLoggedInUser((TransientPerson) transientElement);
            } else {
                logger.log(Level.SEVERE, "Location of Person logged in returned unknown type: " + transientElement);
            }
        } catch (DuplicateAccountNamesException duplicateAccountNamesException) {
            throw new RemoteException("There is more than one person with this account name", duplicateAccountNamesException);
        } finally {
            endReadOnlyOperation();
        }
        return sessionDatasource;
    }

    public Xelement findElement(String uuid) {
        try {
            beginReadOnlyOperation();
            if (!initialised) {
                throw new RuntimeException("Master Datasource has not been initialised");
            }
            Xelement element = persistenceHelper.getElement(uuid);
            return element;
        } finally {
            endReadOnlyOperation();
        }
    }

    public static String getReposUrl() {
        return reposUrl;
    }

    public static String getReposUsername() {
        return reposUsername;
    }

    public static String getReposPassword() {
        return reposPassword;
    }

    public static String getLicenseLocation() {
        return licenseLocation;
    }

    @Override
    public DataSource getDataSource() {
        return persistenceHelper.getDataSource();
    }

    /**
	 * @return theReadWriteLock
	 */
    public ReadWriteLock getReadWriteLock() {
        return myReadWriteLock;
    }

    public void beginReadOnlyOperation() {
        getReadWriteLock().readLock().lock();
    }

    public void endReadOnlyOperation() {
        getReadWriteLock().readLock().unlock();
    }

    public void beginWriteOperation() {
        getReadWriteLock().writeLock().lock();
    }

    public void endWriteOperation() {
        getReadWriteLock().writeLock().unlock();
    }

    /**
	 * Update the datasource from the VCS.
	 */
    public void update() {
        try {
            beginWriteOperation();
            vcsManager.update();
        } catch (VCSException e) {
        } finally {
            endWriteOperation();
        }
        masterScheduler.reschedule();
    }

    /**
	 * Reschedule just the projects that have changed, i.e. an update from VCS,
	 * all reschedule every project.
	 * 
	 * @param all
	 *            - true will reschedule all projects, false - reschedule only
	 *            the projects that have changed
	 */
    public void reschedule(Boolean all) {
        try {
            beginWriteOperation();
            if (all) {
                masterScheduler.rescheduleAllProjects();
            } else {
                masterScheduler.reschedule();
            }
        } finally {
            endWriteOperation();
        }
    }

    public void saveAndAdd(XchangeElement element) {
        try {
            beginWriteOperation();
            ((XchangeElementImpl) element).save();
            persistenceHelper.getDataSource().getVcsProvider().add(element);
        } catch (VCSException e) {
            System.err.println("VCS Exception - " + e.getMessage());
        } finally {
            endWriteOperation();
            masterScheduler.notifyChange(element.getUuid());
        }
    }

    public WebVCSManager getVCSManager() {
        return vcsManager;
    }

    /**
	 * Place an update request onto the command queue.
	 * 
	 * @return the update command
	 * @throws ExceptiongetConsoleLogging
	 *             ()
	 */
    public AbstractQueuedCommand requestUpdate() throws Exception {
        if (update_command == null) {
            update_command = new UpdateMasterDSCommand();
            update_command.setExecutor(waitingExecutor);
            update_command.setPriority(20);
            update_command.setUnique(true);
            update_command.setName("Update");
        }
        update_command.request();
        return update_command;
    }

    /**
	 * Place an reschedule request onto the command queue.
	 * 
	 * @return the reschedule command
	 * @throws Exception
	 */
    public AbstractQueuedCommand requestReschedule(boolean all) throws Exception {
        if (reschedule_command == null) {
            reschedule_command = new RescheduleDSCommand();
            reschedule_command.setExecutor(waitingExecutor);
            reschedule_command.setPriority(20);
            reschedule_command.setUnique(true);
            reschedule_command.setName("Reschedule");
        }
        reschedule_command.setScheduleAll(all);
        reschedule_command.request();
        return reschedule_command;
    }

    @Override
    public WaitingExecutor getWaitingExecutor() {
        return waitingExecutor;
    }

    private void setupLogger() {
        String logDir = baseDir + File.separator + "logs" + File.separator;
        LoggerConfigurator.configure(logDir, "xserver.log", Level.FINE, Level.FINE);
        logger.log(Level.INFO, "Logger successfully setup @ location: " + logDir);
    }

    public void logout(String sessionToken) {
        logger.log(Level.INFO, "Logout from Session");
        SessionDatasource sessionDatasource = getSessionDatasource(sessionToken);
        if (sessionDatasource != null) {
            sessionDatasource.setExpired(true);
            sessions.remove(sessionToken);
        }
        logger.log(Level.INFO, "No. of Sessions still logged in: " + sessions.size());
    }

    @Override
    public MasterScheduler getMasterScheduler() {
        return masterScheduler;
    }

    public String getBaseDir() {
        return baseDir;
    }
}
