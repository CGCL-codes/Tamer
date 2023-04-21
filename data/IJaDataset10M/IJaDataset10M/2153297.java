package com.ivis.xprocess.web.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import com.ivis.xprocess.core.Portfolio;
import com.ivis.xprocess.framework.DataSource;
import com.ivis.xprocess.framework.DatasourceProvider;
import com.ivis.xprocess.framework.XchangeElement;
import com.ivis.xprocess.framework.XchangeElementContainer;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.impl.XchangeElementImpl;
import com.ivis.xprocess.framework.logging.LoggerConfigurator;
import com.ivis.xprocess.framework.schema.SchemaBuilder;
import com.ivis.xprocess.framework.schema.SchemaSource;
import com.ivis.xprocess.framework.vcs.Proxy;
import com.ivis.xprocess.framework.vcs.VcsProviderFactory;
import com.ivis.xprocess.framework.vcs.auth.VCSPasswordAuth;
import com.ivis.xprocess.framework.vcs.exceptions.VCSAuthenticationException;
import com.ivis.xprocess.framework.vcs.exceptions.VCSException;
import com.ivis.xprocess.framework.vcs.impl.svn.SubversionProvider;
import com.ivis.xprocess.framework.xml.IPersistenceHelper;
import com.ivis.xprocess.framework.xml.PersistenceHelper;
import com.ivis.xprocess.util.Day;
import com.ivis.xprocess.util.License;
import com.ivis.xprocess.util.LicenseException;
import com.ivis.xprocess.util.LicenseNotValidForThisVersion;
import com.ivis.xprocess.util.LicensingEnums.Edition;
import com.ivis.xprocess.web.commands.Commands;
import com.ivis.xprocess.web.commands.RescheduleDSCommand;
import com.ivis.xprocess.web.commands.UpdateMasterDSCommand;
import com.ivis.xprocess.web.framework.LicenseManager.Perspective;
import com.ivis.xprocess.web.queue.AbstractQueuedCommand;
import com.ivis.xprocess.web.spring.SpringContext;

public class MasterDataSource implements IMasterDatasource, DatasourceProvider {

    private static final Logger logger = Logger.getLogger(MasterDataSource.class.getName());

    private static final String ROOT_CLASS = "com.ivis.xprocess.core.Portfolio";

    private static final String LOG_DIR = "/logs/";

    private static final String REPOS_DIR = "/repos/";

    private static final String LOG_FILE = "xprocess.web.log";

    private static final String INIT_MSG = "MasterDataSource initialization information:";

    private boolean myStatusOK = true;

    private boolean myInitialized = false;

    private StringBuilder initMessages = new StringBuilder(INIT_MSG);

    private Throwable initializationException = null;

    private IPersistenceHelper ph;

    private String myRepositoryURL;

    private String myRepositoryUser;

    private String myRepositoryPassword;

    private String myLocalDirectory;

    private String myLicensePath;

    private String mySchemaSourceType;

    private ReadWriteLock myReadWriteLock;

    private Scheduler myScheduler;

    private String fileLoggingLevel;

    private String consoleLoggingLevel;

    private MasterScheduler masterScheduler;

    private LicenseManager licenseManager;

    private boolean schedulerServer = false;

    private DataSource myDataSource;

    private WebVCSManager vcsManager = null;

    private Class<?> schemaSource;

    private Level fileLogging;

    private Level consoleLogging;

    private String logDir;

    private String reposDir;

    private Date myLastUpdate = new Date();

    String message = "\n" + "*************************************************************" + "\n" + "*                                                           *" + "\n" + "*                                                           *" + "\n" + "*                                                           *" + "\n" + "*                                                           *" + "\n" + "*                 Powered by  xProcess                      *" + "\n" + "*                                                           *" + "\n" + "*                 www.openxprocess.com                      *" + "\n" + "*                                                           *" + "\n" + "*                                                           *" + "\n" + "*                                                           *" + "\n" + "*                                                           *" + "\n" + "*************************************************************";

    public MasterDataSource() {
        logger.log(Level.FINEST, "MasterDataSource constructor");
    }

    public synchronized ISessionDatasource getSessionDS(String user, String password) throws XprocessWebException {
        if (!(isStatusOK() && isInitialized())) {
            throw new XprocessWebException("Master DS not initialized", XprocessWebException.XprocessWebExceptionReason.MASTER_DS_NOT_INITIALIZED);
        }
        VCSPasswordAuth auth = new VCSPasswordAuth(user, password);
        try {
            myDataSource.getVcsProvider().testConnection(auth);
            licenseManager.login(user);
            ISessionDatasource session = new SessionDataSource(this, auth);
            Perspective[] perspectives = licenseManager.getPerspectives(user);
            if ((perspectives == null) || (perspectives.length == 0)) {
                String message = "User " + user + " does not have permission to access this web server";
                throw new XprocessWebException(message, XprocessWebException.XprocessWebExceptionReason.NOT_A_VALID_WEB_USER);
            }
            session.setPerspectives(perspectives);
            return session;
        } catch (VCSException e) {
            if (e instanceof VCSAuthenticationException) {
                throw new XprocessWebException("Authentication failed", XprocessWebException.XprocessWebExceptionReason.AUTHENTICATION_FAILURE, e);
            }
            throw new XprocessWebException("Failed to connect to server", XprocessWebException.XprocessWebExceptionReason.FAILED_TO_CONNECT_TO_SERVER, e);
        }
    }

    public void mergeAndSave(XchangeElement base, XchangeElement local) {
        try {
            beginWriteOperation();
            local.mergeAndSave(base);
        } finally {
            endWriteOperation();
            masterScheduler.notifyChange(base.getUuid());
        }
    }

    public XchangeElement createTransientExchangeElement(String uuid) throws Exception {
        beginReadOnlyOperation();
        try {
            XchangeElement source = (XchangeElement) getDataSource().getPersistenceHelper().getElement(uuid);
            if ((source == null) || source.isGhost()) {
                return null;
            }
            return source.createTransient();
        } finally {
            endReadOnlyOperation();
        }
    }

    /**
     * Update the datasource from the VCS.
     */
    public void update() {
        if (!(isStatusOK() && isInitialized())) {
            return;
        }
        myLastUpdate = new Date();
        try {
            beginWriteOperation();
            logger.log(Level.FINE, "Updating");
            vcsManager.update();
        } catch (VCSException e) {
            logger.log(Level.WARNING, "Failed to update", e);
        } finally {
            endWriteOperation();
        }
        masterScheduler.reschedule();
    }

    /**
     * Reschedule just the projects that have changed, i.e. an update
     * from VCS, all reschedule every project.
     *
     * @param all - true will reschedule all projects, false - reschedule
     * only the projects that have changed
     */
    public void reschedule(Boolean all) {
        if (!(isStatusOK() && isInitialized())) {
            return;
        }
        myLastUpdate = new Date();
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

    public Date getLastUpdate() {
        return myLastUpdate;
    }

    public void startup() throws Exception {
        init();
    }

    /**
     * Shutdown the MasterDataSource, wait for jobs to complete.
     */
    public void destroy() {
        setInitialized(false);
        logger.log(Level.INFO, "MasterDataSource shutting down");
        if (myScheduler != null) {
            logger.log(Level.FINE, "Stopping Quartz scheduler");
            try {
                boolean waitForJobsToCompleteOnShutdown = true;
                myScheduler.shutdown(waitForJobsToCompleteOnShutdown);
            } catch (SchedulerException e) {
                logger.log(Level.WARNING, "Can't shutdown Quartz scheduler", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void init() {
        try {
            if (myScheduler != null) {
                myScheduler.pauseAll();
                logger.info("===============Quartz Scheduler paused");
            }
            checkSpringProperties();
            checkLicense();
            checkClassPath();
            LoggerConfigurator.configure(logDir, LOG_FILE, getFileLogging(), getConsoleLogging());
            logger.info(message);
            if (!isStatusOK()) {
                return;
            }
            Class rootElement = null;
            SchemaSource source = null;
            try {
                rootElement = Class.forName(ROOT_CLASS);
                source = (SchemaSource) getSchemaSource().newInstance();
                SchemaBuilder.getInstance().buildSchema(source);
            } catch (Exception e) {
                myStatusOK = false;
                logger.log(Level.WARNING, "Problem setting schema source", e);
                return;
            }
            ph = new PersistenceHelper(reposDir, rootElement, null);
            ph.getDataSource().getDescriptor().setVcsType(VcsProviderFactory.SVN);
            ph.getDataSource().getDescriptor().setDatasourceURL(getRepositoryURL());
            ph.getDataSource().getDescriptor().setUserName(getRepositoryUser());
            ph.getDataSource().getDescriptor().setPassword(getRepositoryPassword());
            VCSPasswordAuth auth = new VCSPasswordAuth(getRepositoryUser(), getRepositoryPassword());
            try {
                ph.getDataSource().authenticateVCS(auth);
                ph.getDataSource().getDescriptor().saveLocal();
            } catch (VCSException e) {
                myStatusOK = false;
                logger.log(Level.WARNING, "Problem logging on to datasource", e);
                return;
            }
            myDataSource = ph.getDataSource();
            initMessages.append("\n  Data Source: " + getRepositoryURL());
            StringBuilder out = new StringBuilder();
            if (initMessages.length() > INIT_MSG.length()) {
                initMessages.append("\n\n Now proceeding to checkout/update data source...\n ");
                out.append("\n " + initMessages.toString());
                logger.log(Level.INFO, out.toString());
                initMessages = new StringBuilder(INIT_MSG);
            }
            try {
                Proxy proxy = null;
                vcsManager = new WebVCSManager(myDataSource, auth, "SVN", proxy, getRepositoryURL());
            } catch (VCSException e1) {
                myStatusOK = false;
                logger.log(Level.SEVERE, "Can't authenticate VCS ", e1);
                return;
            } catch (Throwable e) {
                myStatusOK = false;
                logger.log(Level.SEVERE, "Error initializing MasterDS ", e);
                return;
            }
            boolean new_dir = false;
            String localSvnDir = reposDir + File.separator + ".svn";
            File vcsdir = new File(localSvnDir);
            if (!vcsdir.exists()) {
                new_dir = true;
            }
            if (!new_dir) {
                String reposUrl = getRepositoryURL();
                SVNURL wcUrl = null;
                SVNStatusClient statusClient = ((SubversionProvider) myDataSource.getVcsProvider()).getClientManager().getStatusClient();
                try {
                    wcUrl = statusClient.doStatus(new File(reposDir), false).getURL();
                } catch (SVNException e1) {
                    logger.log(Level.WARNING, "Failed to update.", e1);
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
                    myStatusOK = false;
                    logger.log(Level.WARNING, "Repository Url " + reposUrlString + "is not same as Working Copy Url" + wcUrlString + " please delete working copy to continue.");
                    return;
                }
                try {
                    logger.log(Level.INFO, "\nUpdating " + reposDir + "...");
                    vcsManager.update();
                    logger.log(Level.INFO, " ...update finished");
                } catch (VCSException e) {
                    myStatusOK = false;
                    logger.log(Level.WARNING, "Failed to update.", e);
                    return;
                }
            } else {
                try {
                    logger.log(Level.INFO, "\nChecking out HEAD to " + reposDir + "...");
                    vcsManager.checkoutHead();
                    logger.log(Level.INFO, " ...finished checking out HEAD");
                } catch (VCSException e) {
                    myStatusOK = false;
                    logger.log(Level.WARNING, "Failed to check out HEAD.", e);
                    return;
                }
            }
            Set<String> rootContainerIds = ph.getFileIndex().index();
            ph.loadContainers(rootContainerIds);
            logger.log(Level.FINE, "Checking license and users/perspectives...");
            licenseManager.init();
            masterScheduler.rescheduleAllProjects();
            vcsManager.addVCSTransactionListner(masterScheduler);
            if (isSchedulerServer()) {
                boolean madeScheduler = myDataSource.makeSchedulerServer("webServer", true);
                myDataSource.getDescriptor().saveAll();
                myDataSource.getVcsProvider().commit();
                if (madeScheduler) {
                    logger.log(Level.INFO, "Web server is set as scheduler server: " + myDataSource.getDescriptor().getSchedulerServerName() + " : " + myDataSource.getDescriptor().getSchedulerServerUuid());
                } else {
                    logger.warning("Attempted to set web server as server scheduler but failed...");
                }
            }
            setInitialized(true);
        } catch (Throwable e) {
            myStatusOK = false;
            initializationException = e;
            return;
        } finally {
            if (!myStatusOK) {
                StringBuilder out = new StringBuilder("\n>>>>>>>>>>>>> MasterDataSource initialization failed. Destroying. ");
                if (initMessages.length() > INIT_MSG.length()) {
                    out.append("\n " + initMessages.toString());
                }
                if (initializationException != null) {
                    out.append("\n Exception(s): ");
                }
                out.append("\n\n");
                logger.log(Level.SEVERE, out.toString(), initializationException);
                destroy();
            } else {
                try {
                    myScheduler.resumeAll();
                    logger.info("===============Quartz Scheduler resumed");
                } catch (SchedulerException e) {
                    logger.log(Level.WARNING, "Failed to resume scheduler", e);
                }
            }
        }
    }

    /**
     * Set by Spring from xprocess.xml
     *
     * @param repositoryURL
     */
    public void setRepositoryURL(String repositoryURL) {
        myRepositoryURL = repositoryURL;
    }

    /**
     * @return the repository URL
     */
    public String getRepositoryURL() {
        return myRepositoryURL;
    }

    /**
     * Set by Spring from xprocess.xml
     *
     * @param localDirectory
     */
    public void setLocalDirectory(String localDirectory) {
        myLocalDirectory = localDirectory;
    }

    /**
     * @return the full path of the datasource on the file system
     */
    public String getLocalDirectory() {
        return myLocalDirectory;
    }

    /**
     * Set by Spring from xprocess.xml
     *
     * @param repositoryUser
     */
    public void setRepositoryUser(String repositoryUser) {
        myRepositoryUser = repositoryUser;
    }

    /**
     * @return the repository user
     */
    public String getRepositoryUser() {
        return myRepositoryUser;
    }

    /**
     * Set by Spring from xprocess.xml
     *
     * @param repositoryPassword
     */
    public void setRepositoryPassword(String repositoryPassword) {
        myRepositoryPassword = repositoryPassword;
    }

    /**
     * @return the users repository password
     */
    public String getRepositoryPassword() {
        return myRepositoryPassword;
    }

    /**
     * Used to determine if the MasterDataSource has been able
     * to establish a connection to the VCS.
     *
     * @param statusOK - true if the VCS connection has been made, otherwise false
     */
    public void setStatusOK(boolean statusOK) {
        myStatusOK = statusOK;
    }

    public boolean isStatusOK() {
        return myStatusOK;
    }

    /**
     * @return true if a connection can be made to the VCS, otherwise false
     */
    public boolean isReposOnline() {
        try {
            myDataSource.getVcsProvider().testConnection();
            setStatusOK(true);
        } catch (VCSException e) {
            setStatusOK(false);
            return false;
        }
        return true;
    }

    public String getMessage() {
        return initMessages.toString();
    }

    /**
     * Set by Spring from xprocess.xml, its the Quartz scheduler to
     * use for various events like VCS Update etc...
     *
     * @param scheduler
     */
    public void setScheduler(Scheduler scheduler) {
        myScheduler = scheduler;
    }

    /**
     * @return the Quartz scheduler
     */
    public Scheduler getScheduler() {
        return myScheduler;
    }

    /**
     * @param initialized true if the MasterDataSource was able to start up with no
     * problems
     */
    public void setInitialized(boolean initialized) {
        myInitialized = initialized;
    }

    public boolean isInitialized() {
        return myInitialized;
    }

    /**
     * Set by Spring from xprocess.xml
     *
     * @param readWriteLock
     */
    public void setReadWriteLock(ReadWriteLock readWriteLock) {
        myReadWriteLock = readWriteLock;
    }

    /**
     * @return theReadWriteLock
     */
    public ReadWriteLock getReadWriteLock() {
        return myReadWriteLock;
    }

    public Portfolio getRoot() throws Exception {
        Portfolio ret = null;
        try {
            beginReadOnlyOperation();
            XchangeElementContainer root = myDataSource.getRoot();
            if ((root != null) && (root instanceof Portfolio)) {
                ret = (Portfolio) root;
            } else {
                throw new Exception("Root element is not Portfolio " + root);
            }
        } finally {
            endReadOnlyOperation();
        }
        return ret;
    }

    /**
     * Set by Spring from xprocess.xml
     *
     * @param schemaSourceType
     */
    public void setSchemaSourceType(String schemaSourceType) {
        mySchemaSourceType = schemaSourceType;
    }

    public String getSchemaSourceType() {
        return mySchemaSourceType;
    }

    public Class<?> getSchemaSource() {
        return schemaSource;
    }

    public void setSchemaSource(Class<?> schemaSource) {
        this.schemaSource = schemaSource;
    }

    /**
     * Place an update request onto the command queue.
     *
     * @return the update command
     * @throws Exception if the bean registered as the Update command is not correct, i.e
     * the bean definition is incorrect and maybe pointing to the wrong class
     */
    public AbstractQueuedCommand requestUpdate() throws Exception {
        SpringContext ctx = SpringContext.getInstance();
        ApplicationContext app_ctx = ctx.getApplicationContext();
        if (!app_ctx.containsBean(Commands.UPDATE_COMMAND)) {
            throw new Exception("Bean " + Commands.UPDATE_COMMAND + " not found");
        }
        Object bean = app_ctx.getBean(Commands.UPDATE_COMMAND);
        if (!(bean instanceof UpdateMasterDSCommand)) {
            throw new Exception("Bean " + Commands.UPDATE_COMMAND + " is not of type " + UpdateMasterDSCommand.class.getName());
        }
        UpdateMasterDSCommand update_command = (UpdateMasterDSCommand) bean;
        update_command.request();
        return update_command;
    }

    /**
     * Place an reschedule request onto the command queue.
     *
     * @return the reschedule command
     * @throws Exception if the bean registered as the Reschedule command is not correct, i.e
     * the bean definition is incorrect and maybe pointing to the wrong class
     */
    public AbstractQueuedCommand requestReschedule(boolean all) throws Exception {
        SpringContext ctx = SpringContext.getInstance();
        ApplicationContext app_ctx = ctx.getApplicationContext();
        if (!app_ctx.containsBean(Commands.RESCHEDULE_COMMAND)) {
            throw new Exception("Bean " + Commands.RESCHEDULE_COMMAND + " not found");
        }
        Object bean = app_ctx.getBean(Commands.RESCHEDULE_COMMAND);
        if (!(bean instanceof RescheduleDSCommand)) {
            throw new Exception("Bean " + Commands.RESCHEDULE_COMMAND + " is not of type " + RescheduleDSCommand.class.getName());
        }
        RescheduleDSCommand command = (RescheduleDSCommand) bean;
        command.setScheduleAll(all);
        command.request();
        return command;
    }

    public Xelement findElement(String uuid) {
        Xelement ret = null;
        if (!(isStatusOK() && isInitialized())) {
            return ret;
        }
        try {
            beginReadOnlyOperation();
            ret = myDataSource.getPersistenceHelper().getElement(uuid);
            if (ret.isGhost()) {
                return null;
            }
        } finally {
            endReadOnlyOperation();
        }
        return ret;
    }

    /**
     * @return the full path to the license.lic
     */
    public String getLicensePath() {
        return myLicensePath;
    }

    /**
     * Set by Spring from xprocess.xml
     *
     * @param myLicensePath
     */
    public void setLicensePath(String myLicensePath) {
        this.myLicensePath = myLicensePath;
    }

    public DataSource getDataSource() {
        return myDataSource;
    }

    public void beginReadOnlyOperation() {
        myReadWriteLock.readLock().lock();
    }

    public void endReadOnlyOperation() {
        myReadWriteLock.readLock().unlock();
    }

    public void beginWriteOperation() {
        myReadWriteLock.writeLock().lock();
    }

    public void endWriteOperation() {
        myReadWriteLock.writeLock().unlock();
    }

    /**
     * @return the level of logging of the console
     */
    public Level getConsoleLogging() {
        return consoleLogging;
    }

    /**
     * Set the level of logging to the console.
     *
     * @param consoleLogging
     */
    public void setConsoleLogging(Level consoleLogging) {
        this.consoleLogging = consoleLogging;
    }

    public Level getFileLogging() {
        return fileLogging;
    }

    /**
     * Set the level of logging to the log file xprocess.web.log
     *
     * @param fileLogging
     */
    public void setFileLogging(Level fileLogging) {
        this.fileLogging = fileLogging;
    }

    /**
     * @return the consoleLoggingLevel set from xprocess.xml,
     * or null if it has not been set
     */
    public String getConsoleLoggingLevel() {
        return consoleLoggingLevel;
    }

    /**
     * Set by Spring from xprocess.xml, and may not be called if the
     * consoleLoggingLevel is not defined.
     *
     * @param consoleLoggingLevel
     */
    public void setConsoleLoggingLevel(String consoleLoggingLevel) {
        this.consoleLoggingLevel = consoleLoggingLevel;
    }

    /**
     * @return the fileLoggingLevel set from xprocess.xml,
     * or null if it has not been set
     */
    public String getFileLoggingLevel() {
        return fileLoggingLevel;
    }

    /**
     * Set by Spring from xprocess.xml, and may not be called if the
     * fileLoggingLevel is not defined.
     *
     * @param fileLoggingLevel
     */
    public void setFileLoggingLevel(String fileLoggingLevel) {
        this.fileLoggingLevel = fileLoggingLevel;
    }

    public MasterScheduler getMasterScheduler() {
        return masterScheduler;
    }

    /**
     * Set by Spring from xprocess.xml
     *
     * @param masterScheduler
     */
    public void setMasterScheduler(MasterScheduler masterScheduler) {
        this.masterScheduler = masterScheduler;
    }

    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    /**
     * @param licenseManager
     */
    public void setLicenseManager(LicenseManager licenseManager) {
        this.licenseManager = licenseManager;
    }

    public void saveAndAdd(XchangeElement element) {
        try {
            beginWriteOperation();
            ((XchangeElementImpl) element).save();
            ph.getDataSource().getVcsProvider().add(element);
        } catch (VCSException e) {
            logger.log(Level.WARNING, "Failure in save and add", e);
        } finally {
            endWriteOperation();
            masterScheduler.notifyChange(element.getUuid());
        }
    }

    public void saveAndAddDirtyElements() {
        Set<XchangeElement> dirty = new HashSet<XchangeElement>(ph.getDirtyElements());
        ph.saveAndAddDirtyElements();
        for (XchangeElement xe : dirty) {
            masterScheduler.notifyChange(xe.getUuid());
        }
    }

    /**
     * @return today
     */
    public Day getToday() {
        return new Day();
    }

    public WebVCSManager getVCSManager() {
        return vcsManager;
    }

    public synchronized void logout(ISessionDatasource session) {
        licenseManager.logout(session);
    }

    private void checkClassPath() {
        boolean ret = true;
        ClassLoader classloader = MasterDataSource.class.getClassLoader();
        String class_to_test;
        boolean isOK;
        class_to_test = "org.tmatesoft.svn.cli.SVN";
        isOK = checkIsClassInClasspath(ret, class_to_test, classloader);
        ret = ret && isOK;
        class_to_test = "org.jdom.input.SAXBuilder";
        isOK = checkIsClassInClasspath(ret, class_to_test, classloader);
        ret = ret && isOK;
        class_to_test = "org.jdom.Element";
        isOK = checkIsClassInClasspath(ret, class_to_test, classloader);
        ret = ret && isOK;
        class_to_test = "org.jdom.output.XMLOutputter";
        isOK = checkIsClassInClasspath(ret, class_to_test, classloader);
        ret = ret && isOK;
        class_to_test = "org.jdom.xpath.XPath";
        isOK = checkIsClassInClasspath(ret, class_to_test, classloader);
        ret = ret && isOK;
        myStatusOK = myStatusOK && ret;
    }

    private boolean checkIsClassInClasspath(boolean ret, String svn, ClassLoader classloader) {
        try {
            classloader.loadClass(svn);
        } catch (ClassNotFoundException e) {
            initMessages.append("\n  Class " + svn + " not found in classpath!");
            ret = false;
        } catch (Throwable e) {
            initMessages.append("\n  Class " + svn + " not found!" + e.getMessage());
            ret = false;
        }
        return ret;
    }

    private void checkSpringProperties() {
        boolean status = myStatusOK;
        if ((getRepositoryPassword() == null) || (getRepositoryPassword().length() <= 0)) {
            status = false;
            initMessages.append("\n  RepositoryPassword is not set");
        }
        if ((getRepositoryUser() == null) || (getRepositoryUser().length() <= 0)) {
            status = false;
            initMessages.append("\n  RepositoryUser is not set");
        }
        if ((getRepositoryURL() == null) || (getRepositoryURL().length() <= 0)) {
            status = false;
            initMessages.append("\n  Repository URL is not set;");
        }
        if ((getLocalDirectory() == null) || (getLocalDirectory().length() <= 0)) {
            status = false;
            initMessages.append("\n  Local directory is not set");
        }
        if (getReadWriteLock() == null) {
            status = false;
            initMessages.append("\n  ReadWriteLock is not set");
        }
        if (getMasterScheduler() == null) {
            status = false;
            initMessages.append("\n  MasterScheduler not set");
        }
        if ((this.getConsoleLoggingLevel() == null) || (this.getConsoleLoggingLevel().length() < 1)) {
            this.setConsoleLogging(Level.INFO);
        } else {
            this.setConsoleLogging(Level.parse(this.getConsoleLoggingLevel().toUpperCase()));
        }
        if ((this.getFileLoggingLevel() == null) || (this.getFileLoggingLevel().length() < 1)) {
            this.setFileLogging(Level.ALL);
        } else {
            this.setFileLogging(Level.parse(this.getFileLoggingLevel().toUpperCase()));
        }
        File root = new File(this.getLocalDirectory());
        if (root.exists() && !root.isDirectory()) {
            status = false;
            initMessages.append("\n  localDirectory dir specified is not a directory");
        }
        File repos = new File(root.getAbsoluteFile() + REPOS_DIR);
        File logs = new File(root.getAbsoluteFile() + LOG_DIR);
        if (repos.exists()) {
            initMessages.append("\n  Using existing repository directory: " + repos.getAbsolutePath());
        } else {
            repos.mkdirs();
        }
        reposDir = repos.getAbsolutePath();
        initMessages.append("\n  Repos dir= " + reposDir);
        if (!logs.exists()) {
            logs.mkdirs();
        }
        logDir = logs.getAbsolutePath() + File.separator;
        initMessages.append("\n  Log dir= " + logDir);
        if ((this.getSchemaSourceType() == null) || (this.getSchemaSourceType().length() < 1)) {
            status = false;
            initMessages.append("\n SchemaSourceType is not set");
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(this.getSchemaSourceType());
        } catch (ClassNotFoundException e) {
            initMessages.append("\n Invalid class name for schema source");
        }
        if (!SchemaSource.class.isAssignableFrom(clazz)) {
            initMessages.append("\n The schema class does not implement SchemaSource");
        }
        this.setSchemaSource(clazz);
        myStatusOK = myStatusOK && status;
    }

    private void checkLicense() {
        if ((myLicensePath == null) || (myLicensePath.length() < 1)) {
            myStatusOK = false;
            initMessages.append("\n  License path not set");
            return;
        }
        try {
            License.initialize(new FileReader(myLicensePath));
        } catch (FileNotFoundException e) {
            initMessages.append("\n  License file " + myLicensePath + " can not be found.");
            myStatusOK = false;
            return;
        } catch (LicenseException e) {
            initMessages.append("\n  Can not initialize license file. " + e.getMessage());
            myStatusOK = false;
            return;
        } catch (LicenseNotValidForThisVersion e) {
            initMessages.append("\n  Can not initialize license file. " + e.getMessage());
            myStatusOK = false;
            return;
        }
        if (License.getLicense().getEdition() != Edition.WEB) {
            initMessages.append("\n  Incorrect license edition for web server");
        }
    }

    /**
     * @return true if this MasterDataSource is the Scheduler server and
     * therefore can reschedule projects, otherwise false
     */
    public boolean isSchedulerServer() {
        return schedulerServer;
    }

    /**
     * Set by Spring from xprocess.xml
     *
     * @param schedulerServer
     */
    public void setSchedulerServer(boolean schedulerServer) {
        this.schedulerServer = schedulerServer;
    }

    public enum SchemaSourceType {

        JUinitSchemaSource, JarSchemaSource
    }
}
