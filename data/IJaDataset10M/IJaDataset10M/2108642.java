package de.fhg.igd.semoa.starter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;
import de.fhg.igd.util.ArgsParser;
import de.fhg.igd.util.ArgsParserException;
import de.fhg.igd.util.ClassResource;
import de.fhg.igd.util.UnknownOptionException;

/**
 * The main starter class. If started without any command line arguments,
 * a GUI will show up. Otherwise, all parameters necessary for launching
 * <i>SeMoA</i> will either be read from previously saved settings or
 * will be tried to be detected automatically.
 *
 * @author Matthias Pressfreund
 * @version "$Id$"
 */
public class Starter implements ActionListener {

    /**
     * The descriptor of possible command line options
     */
    protected static final String DESCR_ = "config:s,daemon:!,debug:!,f:s[,headless:!,help:!,nogui:!,rc:s[,tray:!";

    /**
     * The current user directory
     */
    public static final String USER_DIR = System.getProperty("user.dir");

    /**
     * The user home directory
     */
    public static final String USER_HOME = System.getProperty("user.home");

    /**
     * The property identifier
     */
    public static final String PROPERTY_ID = "-D";

    /**
     * The default properties file name
     */
    public static final String DEFAULT_PROPERTIES_FILE = new File(USER_HOME, ".semoa.properties").getAbsolutePath();

    /**
     * The name of the Java Xbootclasspath/p option
     */
    public static final String JAVA_OPTION_XBOOTCLASSPATHP = "-Xbootclasspath/p:";

    /**
     * The name of the Java server option
     */
    public static final String JAVA_OPTION_SERVER = "-server";

    /**
     * The name of the Java classpath option
     */
    public static final String JAVA_OPTION_CLASSPATH = "-classpath";

    /**
     * The Java headless option
     */
    public static final String JAVA_PROPERTY_HEADLESS = "java.awt.headless";

    /**
     * The name of the Java extdirs option
     */
    public static final String JAVA_PROPERTY_EXTDIRS = "java.ext.dirs";

    /**
     * The name of the Java semoa etc option
     */
    public static final String JAVA_PROPERTY_SEMOA_ETC = "semoa.etc";

    /**
     * The name of the <i>SeMoA Shell</i> class
     */
    public static final String SHELL_CLASS = "de.fhg.igd.semoa.shell.Shell";

    /**
     * The name of the <i>SeMoA</i> daemon option
     */
    public static final String SEMOA_OPTION_DAEMON = "-daemon";

    /**
     * The name of the <i>SeMoA</i> daemon-out option
     */
    public static final String SEMOA_OPTION_DAEMON_OUT = "-out";

    /**
     * The name of the <i>SeMoA</i> daemon-err option
     */
    public static final String SEMOA_OPTION_DAEMON_ERR = "-err";

    /**
     * The name of the <i>SeMoA</i> script file option
     */
    public static final String SEMOA_OPTION_F = "-f";

    /**
     * The prefix of the log file name when started as daemon
     */
    public static final String DAEMON_LOG_FILE_PREFIX = ".semoa-daemon-";

    /**
     * The date format for the daemon log file name
     */
    public static final String DAEMON_LOG_FILE_DATE_FORMAT = "yyMMdd_HHmmss";

    /**
     * The location of the tray icon
     */
    public static final String TRAY_ICON_LOCATION = "images/trayicon.gif";

    /**
     * The <i>headless</i> flag
     */
    protected boolean headless_;

    /**
     * The <i>daemon</i> flag
     */
    protected boolean daemon_;

    /**
     * The <i>debug</i> flag
     */
    protected boolean debug_;

    /**
     * The <i>Java binary</i> that will be used for launching <i>SeMoA</i>
     */
    protected JavaBinaryFile javaBinary_;

    /**
     * The <i>Java</i> classpath that will be used for launching <i>SeMoA</i>
     */
    protected JavaClassPath javaClasspath_;

    /**
     * The <i>SeMoA</i> base directory to be used for startup
     */
    protected SemoaBaseDirectory semoaBase_;

    /**
     * The Java security policy file
     */
    protected JavaSecurityPolicyFile javaSecurityPolicy_;

    /**
     * The <i>SeMoA</i> start script to be used for startup
     */
    protected SemoaScriptFile semoaScript_;

    /**
     * The parameters used by the startup script
     */
    protected SemoaScriptParameters scriptParameters_;

    /**
     * The <i>SeMoA</i> logging config file
     */
    protected SemoaLoggingConfigFile semoaLoggingConfig_;

    /**
     * The <i>SeMoA</i> log directory
     */
    protected SemoaLogDirectory semoaLogDir_;

    /**
     * The map of user defined system properties
     */
    protected Map userProperties_;

    /**
     * The main GUI frame
     */
    protected StarterFrame sframe_;

    /**
     * the process runner
     */
    protected ProcessRunner pr_;

    /**
     * Hidden construction. Only used once in {@link #main}.
     */
    protected Starter() {
    }

    /**
     * Initialize the <code>Starter</code>.
     *
     * @param args The command line arguments
     */
    protected void init(String[] args) {
        FileInputStream fis;
        Properties settings;
        JPopupMenu menu;
        JMenuItem stop;
        JMenuItem info;
        String prpfile;
        SystemTray st;
        ArgsParser ap;
        String[] argv;
        Border border;
        List argslist;
        TrayIcon t;
        Iterator i;
        String opt;
        File tmp;
        Set excl;
        Icon ic;
        int idx;
        debug_ = true;
        try {
            userProperties_ = new HashMap();
            argslist = new ArrayList(Arrays.asList(args));
            for (i = argslist.iterator(); i.hasNext(); ) {
                opt = (String) i.next();
                if (opt.startsWith(PROPERTY_ID)) {
                    if ((idx = opt.indexOf("=")) < 0) {
                        userProperties_.put(opt.substring(PROPERTY_ID.length()), "true");
                    } else {
                        userProperties_.put(opt.substring(PROPERTY_ID.length(), idx), opt.substring(idx + 1));
                    }
                    i.remove();
                }
            }
            if (userProperties_.containsKey(JAVA_PROPERTY_EXTDIRS)) {
                System.setProperty(JAVA_PROPERTY_EXTDIRS, (String) userProperties_.get(JAVA_PROPERTY_EXTDIRS));
            }
            excl = new HashSet();
            excl.add(JAVA_PROPERTY_HEADLESS);
            excl.add(JAVA_PROPERTY_EXTDIRS);
            excl.add(JAVA_PROPERTY_SEMOA_ETC);
            excl.add(JavaSecurityPolicyFile.PROPERTY);
            excl.add(SemoaBaseDirectory.PROPERTY);
            excl.add(SemoaLogDirectory.PROPERTY);
            excl.add(SemoaLoggingConfigFile.PROPERTY);
            userProperties_.keySet().removeAll(excl);
            argv = (String[]) argslist.toArray(new String[0]);
            ap = new ArgsParser(DESCR_);
            try {
                ap.parse(argv);
            } catch (UnknownOptionException uoe) {
                System.out.println("Option -" + uoe.getMessage() + " does not exist.");
                System.exit(1);
            }
            debug_ = ap.isDefined("debug");
            if (ap.isDefined("help")) {
                System.out.println(usage());
                System.exit(0);
            }
            if (ap.isDefined("config")) {
                prpfile = ap.stringValue("config");
            } else {
                prpfile = DEFAULT_PROPERTIES_FILE;
            }
            tmp = new File(prpfile);
            System.out.print("Reading settings... ");
            fis = null;
            settings = new Properties();
            try {
                fis = new FileInputStream(prpfile);
                settings.load(fis);
                System.out.println("Done" + (tmp.equals(new File(DEFAULT_PROPERTIES_FILE)) ? "." : ": " + prpfile));
            } catch (FileNotFoundException fnfe) {
                System.out.println("Failed. Not found" + (tmp.equals(new File(DEFAULT_PROPERTIES_FILE)) ? "." : ": " + prpfile));
            } catch (IOException ioe) {
                System.out.println("Failed. I/O error" + (tmp.equals(new File(DEFAULT_PROPERTIES_FILE)) ? "." : " while reading " + prpfile));
                System.exit(1);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
            }
            daemon_ = ap.isDefined("daemon");
            headless_ = ap.isDefined("headless");
            if (ap.isDefined("tray")) {
                ic = new ImageIcon(ClassResource.getRelativeImage(this, TRAY_ICON_LOCATION));
                menu = new JPopupMenu("SeMoA");
                stop = new JMenuItem("Stop Server");
                info = new JMenuItem("More Information");
                border = BorderFactory.createLineBorder(Color.black);
                border = BorderFactory.createTitledBorder(border, "SeMoA", TitledBorder.CENTER, TitledBorder.TOP);
                menu.setBorder(border);
                menu.addSeparator();
                info.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        new TrayiconFrame();
                    }
                });
                menu.add(info);
                menu.addSeparator();
                stop.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        if (pr_ != null) {
                            pr_.stop();
                        }
                    }
                });
                menu.add(stop);
                try {
                    t = new TrayIcon(ic, "SeMoA", menu);
                    st = SystemTray.getDefaultSystemTray();
                    st.addTrayIcon(t);
                } catch (Throwable e) {
                    System.out.print("Internal error. ");
                    if (debug_) {
                        System.out.println("Option '-tray' is not supported. " + "Please install the OS-dependent libraries.");
                        e.printStackTrace();
                    } else {
                        System.out.println("Option '-tray' is not supported. " + "Please install the OS-dependent libraries.");
                    }
                    System.exit(1);
                }
            }
            if (argv.length == 0 || (debug_ && argv.length == 1)) {
                startGUI(settings);
            } else {
                startDirectly(settings, ap);
            }
        } catch (Throwable e) {
            System.out.print("Internal error. ");
            if (debug_) {
                System.out.println("Please contact the SeMoA team and include " + "the following stacktrace into your report.");
                e.printStackTrace();
            } else {
                System.out.println("Please restart using the -debug option and provide " + "the returned information to the SeMoA team.");
            }
            System.exit(1);
        }
    }

    /**
     * Launch the StarterFrame if no command line arguments were provided,
     * otherwise use the info that comes with the arguments. This method will
     * be called only if no GUI is going to be used.
     *
     * @param settings Previously loaded settings
     * @param ap Previously parsed command line arguments
     * @throws Exception
     *   if the <code>ArgsParser</code> is not used correctly or
     *   the <i>SeMoA</i> process cannot be started due to various reasons
     */
    protected void startDirectly(Properties settings, ArgsParser ap) throws Exception {
        JceLibraryFile jce;
        findJavaBinary(settings.getProperty(JavaBinaryFile.PROPERTY));
        findSemoaBase(settings.getProperty(SemoaBaseDirectory.PROPERTY));
        if ((jce = semoaBase_.findJcePatch()) == null) {
            jce = javaBinary_.findJceLibrary();
        }
        if (jce == null || !jce.isRecommended()) {
            System.out.println("WARNING: " + (jce != null ? "Unrecommended" : "Missing") + " JCE library, starting SeMoA on low security...");
        }
        loadJavaClasspath(settings.getProperty(JavaClassPath.PROPERTY));
        if (!checkToolsLibrary()) {
            System.out.println("WARNING: Missing Tools Library. Dependent modules" + " (e.g. the Webservices Framework) cannot be used.");
        }
        findJavaSecurityPolicy(settings.getProperty(JavaSecurityPolicyFile.PROPERTY));
        findSemoaScript(ap, settings.getProperty(SemoaScriptFile.PROPERTY), settings.getProperty(SemoaScriptParameters.PROPERTY));
        findSemoaLoggingConfig(settings.getProperty(SemoaLoggingConfigFile.PROPERTY));
        if (ap.isDefined("rc") || ap.isDefined("f")) {
            guessSemoaLogDirectory(settings.getProperty(SemoaLogDirectory.PROPERTY));
        } else {
            findSemoaLogDirectory(settings.getProperty(SemoaLogDirectory.PROPERTY));
        }
        try {
            launchSemoa();
        } catch (Exception e) {
            System.out.println("Could not start SeMoA process.");
            throw e;
        }
    }

    /**
     * Find a valid {@link JavaBinaryFile} either by means of the properties
     * file entry or by searching the file system.
     *
     * @param pjbin The value stored in the properties file
     */
    protected void findJavaBinary(String pjbin) {
        Set javabins;
        System.out.print("Finding Java RE binary... ");
        try {
            javaBinary_ = JavaBinaryFile.create(pjbin, true);
            System.out.println("Done: " + javaBinary_);
        } catch (Exception e) {
            if ((javabins = JavaBinaryFile.find()).isEmpty()) {
                System.out.println("Failed. Could not find any Java RE binaries.");
                System.exit(1);
            } else {
                javaBinary_ = (JavaBinaryFile) javabins.iterator().next();
            }
            System.out.println("Failed. Trying: " + javaBinary_);
        }
    }

    /**
     * Load a valid {@link JavaClassPath} by means of the appropriate
     * properties file entry.
     *
     * @param pjcp The value stored in the properties file
     */
    protected void loadJavaClasspath(String pjcp) {
        System.out.print("Loading Java Class Path... ");
        try {
            javaClasspath_ = JavaClassPath.create(pjcp);
            System.out.print("Done: ");
        } catch (IllegalArgumentException e) {
            javaClasspath_ = JavaClassPath.createEmpty();
            System.out.print("Failed. Not found, using minimum: ");
        }
        System.out.println(javaClasspath_.toCompleteString(semoaBase_, javaBinary_, false));
    }

    /**
     * Find a valid {@link SemoaBaseDirectory} either by means of the
     * properties file entry or by searching the file system.
     *
     * @param psbase The value stored in the properties file
     */
    protected void findSemoaBase(String psbase) {
        Set basepaths;
        System.out.print("Finding SeMoA base directory... ");
        try {
            semoaBase_ = SemoaBaseDirectory.create(psbase, true);
            System.out.println("Done: " + semoaBase_);
        } catch (Exception e) {
            if ((basepaths = SemoaBaseDirectory.find()).isEmpty()) {
                System.out.println("Failed. Could not find any SeMoA base directories.");
                System.exit(1);
            } else {
                semoaBase_ = (SemoaBaseDirectory) basepaths.iterator().next();
            }
            System.out.println("Failed. Trying: " + semoaBase_);
        }
    }

    /**
     * Find a valid {@link JavaSecurityPolicyFile} either by means of the
     * properties file entry or by searching the <i>SeMoA</i> base directory.
     *
     * @param pjpolicy The value stored in the properties file
     */
    protected void findJavaSecurityPolicy(String pjpolicy) {
        System.out.print("Finding Java Security Policy... ");
        try {
            javaSecurityPolicy_ = JavaSecurityPolicyFile.create(pjpolicy);
            System.out.println("Done: " + javaSecurityPolicy_);
        } catch (Exception e) {
            javaSecurityPolicy_ = semoaBase_ != null ? semoaBase_.findDefaultSecurityPolicy() : null;
            if (javaSecurityPolicy_ == null) {
                System.out.println("Failed. Could not even find the default.");
                System.exit(1);
            }
            System.out.println("Failed. Using default: " + javaSecurityPolicy_);
        }
    }

    /**
     * Find a valid {@link SemoaScriptFile} either by means of the properties
     * file entry or by searching the <i>SeMoA</i> base directory.
     *
     * @param ap Previously parsed command line arguments
     * @param psscript The script file value stored in the properties file
     * @param psparams The script paramters value stored in the
     *   properties file
     * @throws ArgsParserException
     *   if an internal error occurred
     */
    protected void findSemoaScript(ArgsParser ap, String psscript, String psparams) throws ArgsParserException {
        String params;
        String[] tmp;
        int i;
        System.out.print("Finding SeMoA script... ");
        if (ap.isDefined("rc")) {
            semoaScript_ = semoaBase_ != null ? semoaBase_.findDefaultStartScript() : null;
            if (semoaScript_ == null) {
                System.out.println("Failed. Could not find the default in " + semoaBase_);
                System.exit(1);
            }
            tmp = (String[]) ap.values("rc");
            params = "";
            for (i = 0; i < tmp.length; i++) {
                params += tmp[i];
                if (i < tmp.length - 1) {
                    params += SemoaScriptParameters.DELIMITER;
                }
            }
        } else if (ap.isDefined("f")) {
            tmp = (String[]) ap.values("f");
            if (tmp.length == 0) {
                System.out.println("Failed. The f-option requires at least one parameter.");
                System.exit(1);
            }
            try {
                semoaScript_ = SemoaScriptFile.create(tmp[0]);
            } catch (IllegalArgumentException e) {
                System.out.println("Failed. " + tmp[0] + " is not a valid SeMoA script.");
                System.exit(1);
            }
            params = "";
            for (i = 1; i < tmp.length; i++) {
                params += tmp[i];
                if (i < tmp.length - 1) {
                    params += SemoaScriptParameters.DELIMITER;
                }
            }
        } else {
            try {
                semoaScript_ = SemoaScriptFile.create(psscript);
            } catch (IllegalArgumentException e) {
                System.out.println("Failed. " + psscript + " is not a valid SeMoA script.");
                System.exit(1);
            }
            params = psparams;
        }
        System.out.println("Done: " + semoaScript_);
        System.out.print("Checking script parameters... ");
        try {
            scriptParameters_ = SemoaScriptParameters.create(params);
            System.out.println("Done: " + scriptParameters_);
        } catch (Exception e) {
            System.out.println("Failed. The parameters are invalid.");
            System.exit(1);
        }
    }

    /**
     * Find a valid {@link SemoaLoggingConfigFile} either by means of the
     * properties file entry or by searching the <i>SeMoA</i> base directory.
     *
     * @param pslogging The value stored in the properties file
     */
    protected void findSemoaLoggingConfig(String pslogging) {
        System.out.print("Finding SeMoA Logging Config... ");
        try {
            semoaLoggingConfig_ = SemoaLoggingConfigFile.create(pslogging);
            System.out.println("Done: " + semoaLoggingConfig_);
        } catch (Exception e) {
            semoaLoggingConfig_ = semoaBase_ != null ? semoaBase_.findDefaultLoggingConfig() : null;
            if (semoaLoggingConfig_ == null) {
                System.out.println("Failed. Could not even find the default.");
                System.exit(1);
            }
            System.out.println("Failed. Using default: " + semoaLoggingConfig_);
        }
    }

    /**
     * Find a valid {@link SemoaLogDirectory} either by means of the
     * properties file entry or by making a guess.
     *
     * @param pslog The value stored in the properties file
     */
    protected void findSemoaLogDirectory(String pslog) {
        System.out.print("Finding SeMoA Log Directory... ");
        try {
            semoaLogDir_ = SemoaLogDirectory.create(pslog);
            System.out.print("Done: ");
        } catch (IllegalArgumentException e) {
            semoaLogDir_ = scriptParameters_ != null ? scriptParameters_.guessLogDirectory() : null;
            if (semoaLogDir_ == null) {
                System.out.println("Failed. Could not even make a proper guess.");
                System.exit(1);
            }
            System.out.print("Failed. Guessed: ");
        }
        System.out.println(semoaLogDir_);
    }

    /**
     * Try to guess the {@link SemoaLogDirectory}. Use the properties file
     * entry if guessing failed.
     *
     * @param pslog The value stored in the properties file
     */
    protected void guessSemoaLogDirectory(String pslog) {
        System.out.print("Guessing SeMoA Log Directory... ");
        semoaLogDir_ = scriptParameters_ != null ? scriptParameters_.guessLogDirectory() : null;
        if (semoaLogDir_ == null) {
            try {
                semoaLogDir_ = SemoaLogDirectory.create(pslog);
            } catch (IllegalArgumentException e) {
                System.out.println("Failed. Could not even find in properties file.");
                System.exit(1);
            }
        }
        System.out.println("Done: " + semoaLogDir_);
    }

    /**
     * Check whether or not a proper {@link ToolsLibraryFile} is included
     * in the classpath.
     * <p><b>Notice</b>: Since this method reads the variables
     * {@link #javaBinary_}, {@link #javaClasspath_} and {@link #semoaBase_},
     * appropriate values should be preloaded.
     *
     * @return <code>true</code> if a proper <code>ToolsLibraryFile</code>
     *   could be found
     */
    protected boolean checkToolsLibrary() {
        boolean tools;
        Iterator i;
        tools = false;
        i = (javaClasspath_ != null ? javaClasspath_ : JavaClassPath.createEmpty()).toCompleteList(semoaBase_, javaBinary_).iterator();
        while (i.hasNext()) {
            if (i.next() instanceof ToolsLibraryFile) {
                tools = true;
                break;
            }
        }
        return tools;
    }

    /**
     * GUI startup.
     *
     * @param settings The previously loaded settings
     */
    protected void startGUI(Properties settings) {
        headless_ = false;
        sframe_ = new StarterFrame(settings, userProperties_);
        sframe_.addStartListener(this);
        sframe_.setVisible(true);
    }

    /**
     * Extends the path found in the <i>&lt;java.ext.dirs&gt;</i> system
     * property by <i>&lt;semoa-base&gt;</i><tt>/ext</tt>, if it is
     * not yet contained.
     *
     * @param sbase The <i>SeMoA</i> base directory
     * @return A extdirs path that will contain the
     *   <i>SeMoA base</i><tt>/etc</tt> directory
     */
    public static String buildExtDirs(SemoaBaseDirectory sbase) {
        String extdirs;
        String ext;
        extdirs = System.getProperty(JAVA_PROPERTY_EXTDIRS, "");
        if (sbase != null) {
            ext = new File(sbase.getAbsolutePath(), "ext").getAbsolutePath();
            if (extdirs.indexOf(ext) < 0) {
                extdirs += File.pathSeparator + ext;
            }
        }
        return extdirs;
    }

    /**
     * Build the set of <i>Java</i> options used on startup.
     *
     * @return A <code>List</code> of <i>Java</i> options
     */
    protected List buildJavaOptions() {
        JceLibraryFile sjce;
        Map.Entry prp;
        Iterator i;
        List jopts;
        jopts = new ArrayList();
        if ((sjce = semoaBase_.findJcePatch()) != null) {
            jopts.add(JAVA_OPTION_XBOOTCLASSPATHP + sjce.getAbsolutePath());
        }
        if (javaBinary_.isServerSupported()) {
            jopts.add(JAVA_OPTION_SERVER);
        }
        if (headless_) {
            jopts.add(PROPERTY_ID + JAVA_PROPERTY_HEADLESS + "=true");
        }
        jopts.add(JAVA_OPTION_CLASSPATH);
        if (javaClasspath_ == null) {
            javaClasspath_ = JavaClassPath.createEmpty();
        }
        jopts.add(javaClasspath_.toCompleteString(semoaBase_, javaBinary_, true));
        jopts.add(PROPERTY_ID + JAVA_PROPERTY_EXTDIRS + "=" + buildExtDirs(semoaBase_));
        jopts.add(PROPERTY_ID + JavaSecurityPolicyFile.PROPERTY + "=" + javaSecurityPolicy_.getAbsolutePath());
        jopts.add(PROPERTY_ID + SemoaBaseDirectory.PROPERTY + "=" + semoaBase_.getAbsolutePath());
        jopts.add(PROPERTY_ID + JAVA_PROPERTY_SEMOA_ETC + "=" + new File(semoaBase_.getAbsolutePath(), "etc"));
        jopts.add(PROPERTY_ID + SemoaLoggingConfigFile.PROPERTY + "=" + semoaLoggingConfig_.getAbsolutePath());
        jopts.add(PROPERTY_ID + SemoaLogDirectory.PROPERTY + "=" + semoaLogDir_.getAbsolutePath());
        for (i = userProperties_.entrySet().iterator(); i.hasNext(); ) {
            prp = (Map.Entry) i.next();
            jopts.add(PROPERTY_ID + prp.getKey() + "=" + prp.getValue());
        }
        return jopts;
    }

    /**
     * Build the set of <i>SeMoA</i> options used on startup.
     *
     * @return A <code>List</code> of <i>SeMoA</i> options
     */
    protected List buildSemoaOptions() {
        String file;
        List sopts;
        sopts = new ArrayList();
        if (daemon_) {
            sopts.add(SEMOA_OPTION_DAEMON);
            file = semoaBase_ != null ? new File(semoaBase_, DAEMON_LOG_FILE_PREFIX + new SimpleDateFormat(DAEMON_LOG_FILE_DATE_FORMAT, Locale.ENGLISH).format(new Date()) + ".log").getAbsolutePath() : null;
            sopts.add(SEMOA_OPTION_DAEMON_OUT);
            sopts.add(file);
            sopts.add(SEMOA_OPTION_DAEMON_ERR);
            sopts.add(file);
        }
        sopts.add(SEMOA_OPTION_F);
        sopts.add(semoaScript_.getAbsolutePath());
        if (scriptParameters_ != null) {
            sopts.addAll(scriptParameters_.getList());
        }
        return sopts;
    }

    /**
     * Launch <i>SeMoA</i> in a seperate {@link Process} using the
     * previously selected <i>Java binary</i>.
     *
     * @throws Exception
     *   if an error occurres while starting the <code>Process</code>
     */
    protected void launchSemoa() throws Exception {
        String[] cmdarray;
        List cmdlist;
        int status;
        cmdlist = new ArrayList();
        cmdlist.add(javaBinary_.getAbsolutePath());
        cmdlist.addAll(buildJavaOptions());
        cmdlist.add(SHELL_CLASS);
        cmdlist.addAll(buildSemoaOptions());
        javaBinary_ = null;
        semoaBase_ = null;
        javaSecurityPolicy_ = null;
        semoaScript_ = null;
        scriptParameters_ = null;
        semoaLoggingConfig_ = null;
        semoaLogDir_ = null;
        userProperties_ = null;
        if (sframe_ != null) {
            sframe_.dispose();
            sframe_ = null;
        }
        System.out.println("Starting SeMoA...");
        if (debug_) {
            System.out.println(cmdlist);
        }
        cmdarray = (String[]) cmdlist.toArray(new String[0]);
        if (daemon_) {
            pr_ = new ProcessRunner(cmdarray, null, null, null);
        } else {
            pr_ = new ProcessRunner(cmdarray, System.out, System.err, System.in);
        }
        status = pr_.start();
        if (!daemon_) {
            System.out.println("\nExit status: " + status);
        }
        System.exit(0);
    }

    /**
     * Invoked when the the start button has been clicked
     */
    public void actionPerformed(ActionEvent e) {
        JceLibraryFile jce;
        String text;
        int rs;
        javaBinary_ = sframe_.getJavaBinaryFile();
        javaClasspath_ = sframe_.getJavaClassPath();
        semoaBase_ = sframe_.getSemoaBaseDirectory();
        javaSecurityPolicy_ = sframe_.getJavaSecurityPolicyFile();
        semoaScript_ = sframe_.getSemoaScriptFile();
        scriptParameters_ = sframe_.getSemoaScriptParameters();
        semoaLoggingConfig_ = sframe_.getSemoaLoggingConfigFile();
        semoaLogDir_ = sframe_.getSemoaLogDirectory();
        daemon_ = sframe_.isDaemonChecked();
        if ((jce = semoaBase_.findJcePatch()) == null) {
            jce = javaBinary_.findJceLibrary();
        }
        if (jce == null || !jce.isRecommended()) {
            text = jce != null ? "The currently installed JCE library is" + "\nnot recommended.\n" + "\nYou may want to use it anyway by the" + "\ncost of missing out on possibly" + "\nimportant security features. In this" + "\ncase, proceed by selecting <Yes>.\n" + "\nTo get your system protected by the" + "\ncomplete SeMoA security, select <No>" + "\nand copy a recommended JCE patch file" + "\n(igda8-jce.jar) into <semoa-base>/ext" : "Could not find any JCE library.\n" + "\nYou may want to proceed anyway by the" + "\nrisk of malfunctions and/or missing" + "\nsecurity features. In this case, proceed" + "\nby selecting <Yes>.\n" + "\nTo get your system protected by the" + "\ncomplete SeMoA security, select <No>" + "\nand copy a sufficient JCE patch file" + "\n(igda8-jce.jar) into <semoa-base>/ext";
            if (JOptionPane.showConfirmDialog(null, text + "\n\nProceed without proper JCE Library?\n", (jce != null ? "Unrecommended" : "Missing") + " JCE Library", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
                sframe_.setVisible(true);
                return;
            }
        }
        if (!checkToolsLibrary()) {
            text = "The currently available classpath does" + "\nnot contain a valid Tools Library.\n" + "\nIf usage of dependent modules (e.g. the" + "\nWebservices Framework) is not intended," + "\nsimply select <Yes> to continue.\n" + "\nOtherwise, select <No> and either choose" + "\na Java Binary package which provides the" + "\nrequired library file (typically tools.jar)," + "\nor manually include the library file into" + "\nyour classpath.";
            if (JOptionPane.showConfirmDialog(null, text + "\n\nContinue without Tools Library?\n", "Missing Tools Library", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
                sframe_.setVisible(true);
                return;
            }
        }
        sframe_.setVisible(false);
        System.out.print("Reconfirming parameters... ");
        if (!sframe_.reconfirm()) {
            System.out.println("Failed. File system changed.");
            sframe_.setVisible(true);
            return;
        }
        System.out.println("Done.");
        sframe_.removeStartListener(this);
        if ((rs = sframe_.shutdown()) != 0) {
            System.out.println("WARNING: GUI terminated with status " + rs);
        }
        try {
            launchSemoa();
        } catch (Exception ex) {
            System.out.println("Could not start SeMoA process.");
            if (debug_) {
                ex.printStackTrace();
            }
            System.exit(1);
        }
    }

    /**
     * Returns the command line help text.
     */
    protected static String usage() {
        return ("\nWelcome to the SeMoA Starter application." + "\n\nUSAGE: java " + Starter.class.getName() + " [<Options>]" + "\nor simply: java -jar <semoa-launcher-jar> [<Options>]" + "\n\nOptions are:" + "\n-nogui" + "\n\tStarts the SeMoA shell without displaying the SeMoA-Starter GUI." + "\n\t(SeMoA settings will be guessed and/or read from a previously" + "\n\tcreated property file.)" + "\n-f <semoa_start_script> [<script_parameters>]" + "\n\tStarts the SeMoA shell, and parses the given <semoa_start_script>" + "\n\twith the according <script_parameters>, if given." + "\n-rc <semoa_configuration>" + "\n\tStarts the SeMoA shell with the given <semoa_configuration>." + "\n\t(This is equivalent to" + "\n\t-f ${semoa_base_directory}/etc/rc <semoa_configuration>.)" + "\n-daemon" + "\n\tStarts the SeMoA shell as daemon in non-interactive mode. All output" + "\n\twill be written into a logfile in ${semoa_base_directory}." + "\n-headless" + "\n\tStarts the SeMoA shell on a host without display or running X-Server" + "\n\t(all GUIs will be omitted). This option may be useful for starting" + "\n\ta SeMoA server on a remote machine." + "\n-debug" + "\n\tProvides some debug information." + "\n-config <properties_file>" + "\n\tReads an optional properties file instead of the default" + "\n\t(.semoa.properties in the user home directory). Anyway," + "\n\tthe default will be written on system exit." + "\n-D<key>=<value>" + "\n\tPasses a user defined system property to the Java VM that will start" + "\n\tthe SeMoA server. Multiple usage of this option is permitted." + "\n-tray" + "\n\tAdds an icon to the tray when starting semoa." + "\n\nWhen using one of the given options (except 'debug'), the basic SeMoA settings" + "\nwill be guessed and/or read from a previously created property file and the" + "\nserver will be started right away. If no option was used, a GUI will show up.");
    }

    public static void main(String[] args) {
        new Starter().init(args);
    }
}
