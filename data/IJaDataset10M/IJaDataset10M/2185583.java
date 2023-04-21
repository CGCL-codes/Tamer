package net.interdirected.autoupdate;

import java.util.prefs.*;
import java.io.*;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * @author Michael Quattlebaum
 */
public class AutomatedUpdate {

    public static Preferences prefs;

    private static SVNClientManager ourClientManager;

    private static UpdateEventHandler myEventHandler;

    private static GuiStatusScreen gss;

    private static boolean usegui;

    private static final AntLauncher ant = new AntLauncher();

    /**
	 * @param args
	 *            Not currently used. All parameters are stored in the
	 *            conf/autoupdate.xml file.
	 */
    public static void main(String[] args) {
        System.out.println("Starting AutomatedUpdate.java");
        run(args);
    }

    /**
	 * @param args
	 *            Not currently used. All parameters are stored in the
	 *            conf/autoupdate.xml file.
	 */
    public static void run(String[] args) {
        LoadPrefsFromFile();
        SoftwareUpdateable upd = null;
        String url = prefs.get("repositoryurl", null);
        String module_url = prefs.get("moduleurl", null);
        String name = prefs.get("repositoryusername", null);
        String password = prefs.get("repositorypassword", null);
        String applicationdirectory = prefs.get("applicationdirectory", null);
        String buildfile = prefs.get("antbuildfile", "build.xml");
        String antlocation = prefs.get("antlocation", ".");
        String updatecheckclass = prefs.get("updatecheckclass", ".");
        usegui = prefs.getBoolean("usegui", true);
        boolean autoclose = prefs.getBoolean("autoclose", false);
        boolean showbutton = (autoclose) ? false : true;
        try {
            upd = getSoftwareUpdateObject(updatecheckclass);
            System.out.println("Update Object:" + upd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (upd.isUpdatable()) {
            System.out.println("isUpdatable is true");
            if (usegui) {
                gss = new GuiStatusScreen(prefs.get("windowtitle", ""), prefs.get("instructions", ""), prefs.getInt("screenwidth", 500), prefs.getInt("screenheight", 400), prefs.get("buttontext", "Update is Complete"), showbutton, prefs.get("imageurl", null), prefs.getInt("imagewidth", 0), prefs.getInt("imageheight", 0));
                gss.appendText("Checking for file update.");
            } else System.out.println("Checking for file update.");
            DAVRepositoryFactory.setup();
            SVNRepository repository = null;
            ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
            try {
                repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url + "/" + module_url));
                ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
                repository.setAuthenticationManager(authManager);
                ourClientManager = SVNClientManager.newInstance(options, authManager);
                SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
                updateClient.setIgnoreExternals(false);
                myEventHandler = new net.interdirected.autoupdate.UpdateEventHandler();
                if (usegui) myEventHandler.setGuiStatusScreen(gss);
                updateClient.setEventHandler(myEventHandler);
                File outdirectory = new File(applicationdirectory);
                if (!outdirectory.exists()) {
                    File parentdir = outdirectory.getParentFile();
                    if (parentdir.exists()) outdirectory.mkdir(); else {
                        parentdir.mkdir();
                        outdirectory.mkdir();
                    }
                }
                updateClient.doCheckout(SVNURL.parseURIDecoded(url + "/" + module_url), outdirectory, SVNRevision.HEAD, SVNRevision.HEAD, true);
            } catch (org.tmatesoft.svn.core.SVNAuthenticationException ae) {
                System.out.println("Cannot authorize user login.");
                ShowException(ae);
            } catch (org.tmatesoft.svn.core.SVNException e) {
                System.out.println(e.getLocalizedMessage());
                ShowException(e);
            }
            if (usegui) gss.appendText("File check and download is complete."); else System.out.println("File check and download is complete.");
            if (prefs.getBoolean("launchant", false)) {
                if (usegui) gss.appendText("Running update scripts."); else System.out.println("Running update scripts.");
                try {
                    ExecuteAntLauncher(antlocation, applicationdirectory, buildfile);
                } catch (Exception e) {
                    ShowException(e);
                }
                if (usegui) gss.appendText("Update scripts complete."); else System.out.println("Update scripts complete.");
            }
            if (usegui) gss.appendText("Update is now complete."); else System.out.println("Update is now complete.");
            if (usegui && showbutton) gss.enableButton(); else System.exit(0);
        }
    }

    /**
	 * @return Returns a GuiStatusScreen if "usegui" is true. Otherwise returns
	 *         null.
	 */
    public static GuiStatusScreen getGuiStatusScreen() {
        if (usegui) return gss; else return null;
    }

    /**
	 * Static method to load the preferences for the updater from the
	 * conf/autoupdate.xml file.
	 */
    private static void LoadPrefsFromFile() {
        prefs = null;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream("conf" + File.separator + "autoupdate.xml"));
        } catch (FileNotFoundException e) {
            System.out.println("You must have a conf/autoupdate.xml configuration file.");
        }
        try {
            Preferences.importPreferences(is);
        } catch (InvalidPreferencesFormatException e) {
        } catch (IOException e) {
        }
        prefs = Preferences.systemNodeForPackage(AutomatedUpdate.class);
    }

    private static void ShowException(Exception e) {
        if (usegui) gss.appendText("ERROR: " + e.getMessage()); else System.out.println("ERROR: " + e.getMessage());
        StackTraceElement[] trace = e.getStackTrace();
        gss.appendText("TRACE:");
        for (int i = 0; i < trace.length; i++) {
            StackTraceElement stacke = trace[i];
            if (usegui) gss.appendText("   " + stacke.getClassName() + "(" + stacke.getMethodName() + ") line " + stacke.getLineNumber()); else System.out.println("   " + stacke.getClassName() + "(" + stacke.getMethodName() + ") line " + stacke.getLineNumber());
        }
    }

    private static void ExecuteAntLauncher(String antlocation, String run_dir, String buildfile) throws Exception {
        String separator = File.separator;
        String cpseparator = File.pathSeparator;
        ant.SetAntLocation(antlocation);
        ant.SetRunDirectory(run_dir);
        ant.SetBuildFile(run_dir + separator + buildfile);
        String cp = System.getenv("CLASSPATH");
        cp = cp + cpseparator + antlocation + separator + "lib" + separator + "ant.jar" + cpseparator + cpseparator + antlocation + separator + "lib" + separator + "ant-launcher.jar";
        ant.SetClassPath(cp);
        try {
            BufferedReader inr = ant.run();
            String line = "";
            while ((line = inr.readLine()) != null) {
                if (usegui) {
                    if (line.indexOf("tools.jar") < 0) gss.appendText(line);
                } else System.out.println(line);
            }
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
            ShowException(ioe);
        }
    }

    private static SoftwareUpdateable getSoftwareUpdateObject(String classname) {
        try {
            Class cl = Class.forName(classname);
            java.lang.reflect.Constructor co = cl.getConstructor(new Class[] {});
            return (SoftwareUpdateable) co.newInstance(new Object[] {});
        } catch (ClassNotFoundException badClass) {
            badClass.printStackTrace();
        } catch (NoSuchMethodException badConst) {
            badConst.printStackTrace();
        } catch (IllegalAccessException badPerm) {
            badPerm.printStackTrace();
        } catch (IllegalArgumentException badParms) {
            badParms.printStackTrace();
        } catch (InstantiationException abstractClass) {
            abstractClass.printStackTrace();
        } catch (java.lang.reflect.InvocationTargetException ite) {
            ite.printStackTrace();
        }
        return null;
    }
}
