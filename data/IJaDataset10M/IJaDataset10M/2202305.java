package de.schwarzrot.app.support;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import de.schwarzrot.app.Application;
import de.schwarzrot.app.MainEntry;
import de.schwarzrot.app.config.ApplicationConfig;
import de.schwarzrot.app.config.DSConfig;
import de.schwarzrot.app.config.DesktopConfig;
import de.schwarzrot.app.config.SystemDefault;
import de.schwarzrot.app.config.support.AbstractSysConfig;
import de.schwarzrot.app.domain.User;
import de.schwarzrot.app.errors.ApplicationException;
import de.schwarzrot.app.errors.support.RegisterableExceptionHandler;
import de.schwarzrot.data.access.DelegatingResourceBundleMessageSource;
import de.schwarzrot.data.transaction.TOCount;
import de.schwarzrot.data.transaction.TORead;
import de.schwarzrot.data.transaction.Transaction;
import de.schwarzrot.data.transaction.support.TransactionFactory;
import de.schwarzrot.jar.JarExtension;
import de.schwarzrot.jar.JarExtensionHandler;
import de.schwarzrot.logging.SRLogFactory;
import de.schwarzrot.security.SecurityManager;
import de.schwarzrot.system.SysInfo;
import de.schwarzrot.ui.Desktop;

/**
 * ApplicationLauncher is a Runnable called from main, responsable for all the
 * grunt work on application-startup (i.e. fetch beans from context, setup
 * services, load preferences ...).
 * 
 * <p>
 * typical usage:
 * 
 * <pre>
 * class AnyClass implements MainStarter {
 *    ...
 *    public static void main(String[] args) {
 *        String contextBase = &quot;de/schwarzrot/app/ctx/&quot;;
 *        String appContext = contextBase + &quot;application-context.xml&quot;;
 *        String businessCtx = contextBase + &quot;business-layer-context.xml&quot;;
 *        String securityCtx = contextBase + &quot;security-context.xml&quot;;
 *        MainStarter app = new AnyClass(...);
 *     
 *        new ApplicationLauncher(app, new String[] { securityContext, businessCtx, appCtx }).run();
 *    }
 * }
 * </pre>
 * 
 * <p>
 * The use-case of this class is very similar to the
 * <strong>ApplicationLauncher</strong> from springframework, with the main
 * difference, that this class supports application extensions with jarfiles.
 * <p>
 * Such a jarfile-extension needs a Manifest-Entry
 * <strong>Application-Context</strong> with the fully qualified path of the
 * application-context-file usable for an <strong>UrlClassLoader</strong>.
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * @param <C>
 *            - config type of application
 */
public abstract class AbstractApplicationLauncher<C extends AbstractSysConfig> {

    public static final String APPLICATION_TYPE = "Application";

    /**
     * constructor - loads the given context's and validates configuration.
     * 
     * @param starter
     *            - usually the class providing the main()
     * @param appContextFiles
     *            - the very basic requirements - at least one path to a context
     *            file
     * @param appArgs
     *            - the arguments from main, so commandline parsing can be
     *            deffered
     */
    public AbstractApplicationLauncher(MainEntry<C> starter, String[] appContextFiles, List<String> appArgs) {
        this.starter = starter;
        if (starter != null) starter.getConfig().setSetupNeeded(false);
        printAppInfo();
        parseAppArgs(appArgs);
        init(appContextFiles);
    }

    public final AbstractApplicationContext getAppContext() {
        return appContext;
    }

    public final MainEntry<C> getStarter() {
        return starter;
    }

    public final SysInfo getSysInfo() {
        return sysInfo;
    }

    public final void setAppContext(AbstractApplicationContext appContext) {
        this.appContext = appContext;
    }

    public final void setSysInfo(SysInfo sysInfo) {
        this.sysInfo = sysInfo;
    }

    /**
     * entry point for application startup
     */
    public void start() {
        assert starter != null : "could not start application without starter!";
        assert starter.getConfig() != null : "application is not really configured. Abort launch!";
        List<Application<?>> knownApplications = new ArrayList<Application<?>>();
        loadExtensions(knownApplications);
        new JarExtensionHandler().getClassLoader();
        if (starter.getConfig() instanceof DesktopConfig) ((DesktopConfig) starter.getConfig()).setKnownApplications(knownApplications);
    }

    /**
     * validate the configuration from application context. Database access is
     * vital for applications from SRFramework
     */
    protected void checkDBAccess() {
        getLogger().info("let's check database access ...");
        TransactionFactory taFactory = ApplicationServiceProvider.getService(TransactionFactory.class);
        TOCount<User> countOp = new TOCount<User>(User.class);
        try {
            Transaction t = taFactory.createTransaction();
            countOp.setResult(-1);
            t.add(countOp);
            t.execute();
        } catch (Exception e) {
            if (e instanceof SQLException) {
                System.out.println("caught a SQLException!");
                System.out.println("caught a SQLException!");
                System.out.println("caught a SQLException!");
            }
            e.printStackTrace();
        }
        if (countOp.getResult() < 0) {
            if (starter != null && starter.getConfig() != null) starter.getConfig().setSetupNeeded(true);
        }
    }

    protected void doUsage() {
        if (starter != null) {
            starter.usage();
            System.out.println("\ninternal options:");
            System.out.println("   --help                     the text you are reading");
            System.out.println("    -?                        same as --help");
            System.out.println("   --dump                     tell some informations about your system");
            System.out.println("   --charset                  tell supported charsets");
        } else {
            System.out.println("Class using ApplicationLauncher was not setup correctly!");
            System.out.println("don't know what to tell about it's usage ...");
        }
        System.exit(-1);
    }

    protected void dumpCharsets() {
        System.out.println("available charsets for java applications follows:");
        for (String cur : Charset.availableCharsets().keySet()) System.out.print(cur + "\t");
        System.out.println();
        System.exit(0);
    }

    protected final Log getLogger() {
        return LogFactory.getLog(getClass());
    }

    @SuppressWarnings("unchecked")
    protected AbstractApplicationContext init(String[] appContextFiles) {
        ClassPathXmlApplicationContext ctx = null;
        sysInfo = new SysInfo();
        ApplicationServiceProvider.registerService(SysInfo.class, sysInfo);
        getLogger().info("start initialization (0)...");
        for (String ctxFile : appContextFiles) {
            try {
                if (ctx != null) ctx = new ClassPathXmlApplicationContext(new String[] { ctxFile }, ctx); else ctx = new ClassPathXmlApplicationContext(ctxFile);
            } catch (Throwable t) {
                throw new ApplicationException("error on context processing", t);
            }
        }
        appContext = ctx;
        if (appContext != null) {
            ApplicationServiceProvider.registerService(ApplicationContext.class, appContext);
            if (appContext.containsBean("exceptionHandler")) {
                Object tmp = appContext.getBean("exceptionHandler");
                if (tmp instanceof RegisterableExceptionHandler) ((RegisterableExceptionHandler) tmp).registerExceptionHandler();
            }
            if (appContext.containsBean("messageSource")) {
                HierarchicalMessageSource hms = (HierarchicalMessageSource) appContext.getBean("messageSource");
                DelegatingResourceBundleMessageSource dms = new DelegatingResourceBundleMessageSource(hms);
                ApplicationServiceProvider.registerService(MessageSource.class, dms);
            }
            if (appContext.containsBean("appServices")) {
                ApplicationServiceConfigurer asc = (ApplicationServiceConfigurer) appContext.getBean("appServices");
                asc.publishServices();
            }
            if (appContext.containsBean("appConfig")) {
                C cfg = (C) appContext.getBean("appConfig");
                if (appContext.containsBean("dsConfig")) {
                    DSConfig dCfg = (DSConfig) appContext.getBean("dsConfig");
                    getLogger().warn("ds-config: " + dCfg);
                    cfg.getValuesFrom(dCfg);
                }
                getLogger().warn("appConfig found ...");
                if (starter != null) starter.setConfig(cfg);
            }
            checkDBAccess();
        } else if (starter != null) starter.getConfig().setSetupNeeded(true);
        SecurityManager securityManager = ApplicationServiceProvider.getService(SecurityManager.class);
        securityManager.login(System.getProperty("user.name"), sysInfo.getUserName());
        assert securityManager.getUser() != null : "failed to login with current user";
        return appContext;
    }

    /**
     * checks the plugin-directory for jarfiles, that contain valid application
     * extensions. Such a jarfile-extension needs a Manifest-Entry
     * {@code Application-Context} with the fully qualified path of the
     * application-context-file usable for an {@code UrlClassLoader}.
     */
    @SuppressWarnings("unchecked")
    protected void loadExtensions(List<Application<?>> knownApps) {
        JarExtensionHandler jh = new JarExtensionHandler();
        getLogger().info("working dir is: " + sysInfo.getStartupDirectory());
        if (starter == null) {
            jh.loadExtensions("ext");
        } else if (starter.getAppArgs() != null && starter.getAppArgs().containsKey("ext")) {
            jh.loadExtensions(starter.getAppArgs().get("ext"));
        } else if (starter.getConfig() != null && starter.getConfig().getPluginDir() != null) {
            jh.loadExtensions(starter.getConfig().getPluginDir());
        } else if (System.getProperty("extension.dir") != null) {
            jh.loadExtensions(System.getProperty("extension.dir"));
        } else jh.loadExtensions("ext");
        String cp = System.getProperty("java.class.path");
        File myArchive = new File(cp);
        if (myArchive.exists() && myArchive.isFile() && myArchive.canRead()) {
            JarExtension je = jh.verifyArchive(myArchive);
            Desktop desktop = ApplicationServiceProvider.getService(Desktop.class);
            if (desktop != null && je != null) desktop.setArchiveInfo(je);
        }
        if (knownApps != null) {
            TransactionFactory taFactory = ApplicationServiceProvider.getService(TransactionFactory.class);
            List<JarExtension> apps = jh.getExtensions(APPLICATION_TYPE);
            if (apps != null) {
                for (JarExtension ext : apps) {
                    Application<ApplicationConfig> cur = null;
                    if (ext.getMainObject() != null) cur = (Application<ApplicationConfig>) ext.getMainObject();
                    if (cur != null) {
                        ApplicationConfig tmp = null;
                        ApplicationConfig cfg = cur.getAppConfig();
                        Transaction ta = taFactory.createTransaction();
                        TORead<ApplicationConfig> tor = new TORead<ApplicationConfig>(cfg);
                        ta.add(tor);
                        ta.setRollbackOnly();
                        ta.execute();
                        if (tor.getResult() != null && tor.getResult().size() == 1) tmp = tor.getResult().get(0);
                        if (tmp != cfg) cur.setAppConfig(tmp);
                        knownApps.add(cur);
                        cur.getAppConfig().setVersion(ext.getVersion());
                        cur.getAppConfig().setArchive(ext);
                        if (ext.getHelpRoot() != null) cur.setHelpRoot(ext.getHelpRoot());
                    }
                }
            }
        }
    }

    protected void parseAppArgs(List<String> args) {
        Map<String, String> appArgs = new HashMap<String, String>();
        if (args == null) return;
        Pattern appParam = Pattern.compile("^--?(\\S+)");
        for (int i = 0; i < args.size(); i++) {
            Matcher m = appParam.matcher(args.get(i));
            if (m.matches()) {
                String param = m.group(1);
                String value = null;
                if (param.equals("?") || param.equalsIgnoreCase("help")) doUsage();
                if (param.equalsIgnoreCase("dump")) sysDump();
                if (param.equalsIgnoreCase("charset")) dumpCharsets();
                if (args.size() > (i + 1)) value = args.get(++i); else value = Boolean.TRUE.toString();
                appArgs.put(m.group(1), value);
            }
        }
        if (appArgs.containsKey("logger")) {
            SRLogFactory.setUserGivenLogger(appArgs.get("logger"));
        }
        if (starter != null) starter.setAppArgs(appArgs);
    }

    protected void printAppInfo() {
        if (starter != null) System.err.println(starter.getAppInfo()); else System.err.println(String.format(SystemDefault.APPINFO, "unknown?!?"));
    }

    protected void sysDump() {
        Properties props = System.getProperties();
        System.err.println("\n\n-----------------------------------------------------------------------------------");
        System.err.println("\tSystem properties look like ...");
        System.err.println("-----------------------------------------------------------------------------------");
        for (Object key : props.keySet()) {
            System.err.println(String.format("%30s ==> %s", key, props.getProperty((String) key)));
        }
        System.exit(0);
    }

    private AbstractApplicationContext appContext;

    private MainEntry<C> starter;

    private SysInfo sysInfo;
}
