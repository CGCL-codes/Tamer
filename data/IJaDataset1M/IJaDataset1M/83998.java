package visad.install;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.JOptionPane;
import visad.util.CmdlineGenericConsumer;
import visad.util.CmdlineParser;

class ClusterInstaller {

    private Runtime runtime = null;

    private String[] argList = null;

    private String cPush;

    public ClusterInstaller(String cPush) {
        this.cPush = cPush;
    }

    public Process push(String target) throws IOException {
        return push(target, target);
    }

    public Process push(String source, String target) throws IOException {
        if (runtime == null) {
            runtime = Runtime.getRuntime();
        }
        if (argList == null) {
            argList = new String[] { cPush, null, null };
        }
        argList[1] = source;
        argList[2] = target;
        return runtime.exec(argList);
    }
}

public class Main extends CmdlineGenericConsumer {

    private static final String CLASSPATH_PROPERTY = "java.class.path";

    private static final String ARCH_PROPERTY = "visad.install.arch";

    private static final String HOME_PROPERTY = "visad.install.home";

    private static final String PATH_PROPERTY = "visad.install.path";

    private static final String SPLASH_NAME = "visad-splash.jpg";

    private static final String JAR_NAME = "visad.jar";

    private static final String VISAD_JAR_URL = "ftp://ftp.ssec.wisc.edu/pub/visad-2.0/" + JAR_NAME;

    private boolean debug;

    private URL jarURL;

    private ChooserList chooser;

    private Path classpath, path;

    private ArrayList jarList, javaList;

    private File installerJar;

    private JavaFile installerJava;

    private File installerJavaDir, installerJavaJar;

    private String archStr;

    private String cPushStr;

    private boolean useSuppliedJava, downloadLatestJar;

    private File javaInstallDir, jarInstallDir;

    private ClusterInstaller clusterInstaller;

    public Main(String[] args) {
        CmdlineParser cmdline = new CmdlineParser(this);
        if (!cmdline.processArgs(args)) {
            System.exit(1);
            return;
        }
        File distDir;
        String ddStr = System.getProperty(HOME_PROPERTY);
        if (ddStr == null) {
            distDir = null;
        } else {
            distDir = new File(ddStr);
        }
        if (distDir == null || !distDir.exists()) {
            distDir = new File(".");
        }
        SplashScreen ss = null;
        File splashFile = new File(distDir, SPLASH_NAME);
        if (splashFile.exists()) {
            ss = new SplashScreen(getPath(splashFile));
            ss.setVisible(true);
        }
        boolean initResult = initialize(distDir);
        if (ss != null) {
            ss.setVisible(false);
        }
        if (!initResult) {
            System.exit(1);
            return;
        }
        if (debug) {
            dumpInitialState(distDir);
        }
        useSuppliedJava = downloadLatestJar = false;
        javaInstallDir = jarInstallDir = null;
        clusterInstaller = null;
        queryUser();
        if (debug) {
            dumpInstallState();
        }
        install();
    }

    /**
   * Check all java executables for minimum version.
   *
   * @param list list of File objects
   * @param major major number of java version
   * @param minor minor number of java version
   */
    private static final void checkJavaVersions(ArrayList list, int major, int minor) {
        int i = 0;
        while (i < list.size()) {
            JavaFile f = new JavaFile((File) list.get(i));
            if (f.matchMinimum(major, minor)) {
                list.set(i++, f);
            } else {
                list.remove(i);
            }
        }
    }

    public int checkOption(String mainName, char ch, String arg) {
        if (ch == 'x') {
            debug = true;
            return 1;
        }
        return 0;
    }

    /**
   * Push installed files out to cluster.
   *
   * @param mon progress monitor (ignored if <tt>null</tt>.)
   * @param source source file/directory
   * @param target directory on cluster machine where source is installed.
   */
    private final void clusterPush(ProgressMonitor mon, String source, String target) {
        Process p;
        try {
            p = clusterInstaller.push(source, target);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        try {
            p.getOutputStream().close();
        } catch (IOException ioe) {
        }
        BufferedReader in, err;
        in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        boolean looping = true;
        while (looping) {
            if (in != null) {
                try {
                    String line = in.readLine();
                    if (line == null) {
                        in = null;
                        looping = (err != null);
                    } else if (mon != null) {
                        mon.setDetail(line);
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    break;
                }
            }
            if (err != null) {
                try {
                    if (err.ready() || in == null) {
                        String line = err.readLine();
                        if (line == null) {
                            err = null;
                            looping = (in != null);
                        } else if (mon != null) {
                            mon.setDetail(line);
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    break;
                }
            }
        }
        try {
            p.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
   * Ugly hack to make JVM binaries executable.
   */
    private final void makeJVMBinariesExecutable() {
        if (path == null) {
            return;
        }
        ArrayList chmodList = path.find("chmod");
        if (chmodList == null || chmodList.size() == 0) {
            return;
        }
        File chmod = (File) chmodList.get(0);
        File jvmBin = new File(javaInstallDir, "bin");
        if (!jvmBin.exists()) {
            return;
        }
        String[] binFiles = jvmBin.list();
        if (binFiles == null || binFiles.length == 0) {
            return;
        }
        String[] cmd = new String[2 + binFiles.length];
        cmd[0] = chmod.toString();
        cmd[1] = "555";
        StringBuffer buf = new StringBuffer(jvmBin.toString());
        buf.append('/');
        final int len = buf.length();
        for (int i = 0; i < binFiles.length; i++) {
            buf.setLength(len);
            buf.append(binFiles[i]);
            cmd[i + 2] = buf.toString();
        }
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        try {
            p.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
   * Pop up directory chooser dialog and process user's response.
   *
   * @param chooser previously created directory chooser window
   * @param list list of directory choices
   * @param title directory chooser window title
   */
    private static final File chooseDirectory(ChooserList chooser, ArrayList list, String title) {
        final int listLen = (list == null ? 0 : list.size());
        boolean allDirs = true;
        for (int i = 0; i < listLen; i++) {
            File f = (File) list.get(i);
            if (!f.isDirectory()) {
                allDirs = false;
                break;
            }
        }
        File[] dList;
        if (!allDirs) {
            dList = new File[listLen];
            for (int i = 0; i < listLen; i++) {
                File f = (File) list.get(i);
                if (f.isDirectory()) {
                    dList[i] = f;
                } else {
                    dList[i] = new File(f.getParent());
                }
            }
        } else if (listLen > 0) {
            dList = (File[]) list.toArray(new File[list.size()]);
        } else {
            dList = null;
        }
        chooser.setList(dList);
        chooser.setFileSelectionMode(ChooserList.DIRECTORIES_ONLY);
        chooser.setDialogTitle(title);
        chooser.setApproveButtonToolTipText("Select directory");
        chooser.setApproveButtonText("Select...");
        int option = chooser.showDialog(null, "Select directory");
        if (option == ChooserList.CANCEL_OPTION) {
            return null;
        }
        File choice = chooser.getSelectedFile();
        if (!choice.exists()) {
            return choice;
        }
        if (choice.isDirectory()) {
            return choice;
        }
        return new File(choice.getParent());
    }

    /**
   * Pop up file chooser dialog and process user's response.
   *
   * @param chooser previously created file chooser window
   * @param list list of file choices
   * @param title file chooser window title
   */
    private static final File chooseFile(ChooserList chooser, ArrayList list, String title) {
        if (list == null) {
            chooser.setList(null);
        } else {
            chooser.setList((File[]) list.toArray(new File[list.size()]));
        }
        chooser.setFileSelectionMode(ChooserList.FILES_ONLY);
        chooser.setDialogTitle(title);
        chooser.setApproveButtonToolTipText("Choose file");
        int option = chooser.showDialog(null, "Choose file");
        if (option == ChooserList.CANCEL_OPTION) {
            return null;
        }
        return chooser.getSelectedFile();
    }

    /**
   * Dump post-initialization state for debugging
   */
    private final void dumpInitialState(File distDir) {
        if (distDir != null && distDir.exists()) {
            System.out.println("Distribution directory: " + distDir);
        } else {
            System.out.println("Distribution directory: UNKNOWN!");
        }
        if (installerJavaDir != null) {
            System.out.println("Supplied java directory: " + installerJavaDir);
        }
        if (installerJavaJar != null) {
            System.out.println("Supplied java jar file: " + installerJavaJar);
        }
        if (installerJava != null) {
            System.out.println("Supplied java: " + installerJava);
        }
        if (installerJar != null) {
            System.out.println("Supplied visad.jar: " + installerJar);
        }
        if (jarList == null || jarList.size() == 0) {
            System.err.println("No " + JAR_NAME + " found in " + classpath);
        } else {
            System.err.println("== jar file list ==");
            for (int i = 0; i < jarList.size(); i++) {
                System.out.println("#" + i + ": " + getPath((File) jarList.get(i)));
            }
        }
        if (javaList == null || javaList.size() == 0) {
            System.err.println("No java executable found in path " + path);
        } else {
            System.err.println("== java executable list ==");
            for (int i = 0; i < javaList.size(); i++) {
                System.out.println("#" + i + ": " + getPath((File) javaList.get(i)));
            }
        }
        if (cPushStr == null) {
            System.err.println("No cluster executable found in path " + path);
        } else {
            System.err.println("== cluster executable ==");
            System.out.println(cPushStr);
        }
    }

    /**
   * Dump pre-installation state for debugging.
   */
    private final void dumpInstallState() {
        if (useSuppliedJava) {
            System.err.println("Install java in " + javaInstallDir);
        }
        if (downloadLatestJar) {
            System.err.println("Download latest " + JAR_NAME);
        }
        System.err.println("Install " + JAR_NAME + " in " + jarInstallDir);
        if (clusterInstaller != null) {
            System.err.println("Push installed files out to cluster");
        }
    }

    /**
   * Remove and return the installer executable from the
   * list of java executable JavaFile objects.
   *
   * @param list list of JavaFile objects
   *
   * @return <tt>null</tt> if installer-supplied java executable
   *         was not found
   */
    private static final File extractInstallerFile(File distDir, ArrayList javaList) {
        String distPath = getPath(distDir);
        java.util.Iterator iter = javaList.iterator();
        while (iter.hasNext()) {
            File thisFile = (File) iter.next();
            if (getPath(thisFile).startsWith(distPath)) {
                iter.remove();
                return thisFile;
            }
        }
        return null;
    }

    /**
   * Return either the canonical path or, if that is not possible,
   * the specified path.
   *
   * @param f File object
   *
   * @return either the canonical path or the originally specified path.
   */
    private static final String getPath(File f) {
        try {
            return f.getCanonicalPath();
        } catch (IOException ioe) {
            return f.getPath();
        }
    }

    /**
   * Initialize internal state.
   */
    private final boolean initialize(File distDir) {
        chooser = new ChooserList();
        classpath = path = null;
        jarURL = null;
        jarList = javaList = null;
        try {
            classpath = new Path(System.getProperty(CLASSPATH_PROPERTY));
        } catch (IllegalArgumentException iae) {
            System.err.println(getClass().getName() + ": Couldn't get Java class path");
            return false;
        }
        String pathStr = System.getProperty(PATH_PROPERTY);
        if (pathStr == null) {
            path = null;
        } else {
            try {
                path = new Path(pathStr);
            } catch (IllegalArgumentException iae) {
                path = null;
            }
        }
        try {
            jarURL = new URL(VISAD_JAR_URL);
        } catch (java.net.MalformedURLException mue) {
            jarURL = null;
        }
        installerJar = null;
        jarList = classpath.findMatch(JAR_NAME);
        if (jarList != null) {
            loseDuplicates(jarList);
            installerJar = extractInstallerFile(distDir, jarList);
        }
        if (installerJar == null && jarURL == null) {
            System.err.println("Couldn't find either distributed jar file" + " or jar file URL!");
            System.exit(1);
        }
        installerJava = null;
        installerJavaDir = null;
        if (path == null) {
            javaList = null;
        } else {
            javaList = path.find("java");
            if (javaList != null) {
                loseDuplicates(javaList);
                checkJavaVersions(javaList, 1, 2);
                installerJava = (JavaFile) extractInstallerFile(distDir, javaList);
                if (installerJava != null && installerJava.getName().equals("java")) {
                    File canonJava = new File(getPath(installerJava));
                    installerJavaDir = new File(canonJava.getParent());
                    if (installerJavaDir.getName().equals("bin")) {
                        installerJavaDir = new File(installerJavaDir.getParent());
                    }
                }
            }
        }
        archStr = System.getProperty(ARCH_PROPERTY);
        if (archStr != null && archStr.length() == 0) {
            archStr = null;
        }
        installerJavaJar = null;
        if (installerJavaDir == null && archStr != null) {
            File tmpFile = new File(distDir, "jdk-" + archStr + ".jar");
            if (tmpFile.exists()) {
                installerJavaJar = tmpFile;
            }
        }
        cPushStr = null;
        if (path != null) {
            ArrayList c3List = path.find("cpush");
            if (c3List != null) {
                loseDuplicates(c3List);
                if (c3List != null && c3List.size() > 0) {
                    cPushStr = getPath((File) c3List.get(0));
                }
            }
        }
        return true;
    }

    public void initializeArgs() {
        debug = false;
    }

    /**
   * Install VisAD.
   */
    private final void install() {
        ProgressMonitor mon = new ProgressMonitor();
        mon.setPhase("Starting install");
        mon.setVisible(true);
        if (downloadLatestJar) {
            mon.setPhase("Downloading jar file");
            Download.getFile(jarURL, jarInstallDir, false);
        } else {
            mon.setPhase("Copying jar file");
            Util.copyFile(mon, installerJar, jarInstallDir, ".old");
        }
        if (clusterInstaller != null) {
            mon.setPhase("Pushing jar file to cluster");
            clusterPush(null, getPath(new File(jarInstallDir, JAR_NAME)), getPath(jarInstallDir));
        }
        if (useSuppliedJava) {
            mon.setPhase("Copying JVM");
            if (installerJavaDir != null) {
                if (javaInstallDir.exists()) {
                    javaInstallDir = new File(javaInstallDir, installerJavaDir.getName());
                }
                Util.copyDirectory(mon, installerJavaDir, javaInstallDir);
            } else {
                String jarTop = getJarTopDir(installerJavaJar);
                if (jarTop == null && archStr != null) {
                    jarTop = "jdk-" + archStr;
                }
                if (jarTop != null) {
                    javaInstallDir = new File(javaInstallDir, jarTop);
                }
                Util.copyJar(mon, installerJavaJar, javaInstallDir);
            }
            mon.setPhase("Setting JVM executable bits");
            makeJVMBinariesExecutable();
            if (clusterInstaller != null) {
                mon.setPhase("Pushing JVM to cluster");
                clusterPush(mon, javaInstallDir.toString(), javaInstallDir.getParent().toString());
            }
        }
        mon.setPhase("Install finished!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
        }
    }

    /**
   * Return the top directory contained in the specified jar file
   *
   * @param source jar file to examine
   *
   * @return either the sole top-level directory in the jar file
   *         or <tt>null</tt> if the jar file contains multiple
   *         top-level files/directories.
   */
    private String getJarTopDir(File source) {
        JarFile jar;
        try {
            jar = new JarFile(source);
        } catch (IOException ioe) {
            return null;
        }
        String topDir = null;
        Enumeration en = jar.entries();
        while (en.hasMoreElements()) {
            JarEntry entry = (JarEntry) en.nextElement();
            String entryName = entry.getName();
            if (JarFile.MANIFEST_NAME.startsWith(entryName)) {
                continue;
            }
            String dirName;
            int dirIdx = entryName.indexOf(File.separatorChar);
            if (dirIdx < 0) {
                dirName = entryName;
            } else {
                dirName = entryName.substring(0, dirIdx);
            }
            if (topDir == null) {
                topDir = dirName;
            } else if (!topDir.equals(dirName)) {
                topDir = null;
                break;
            }
        }
        return topDir;
    }

    /**
   * Remove duplicate objects from the list.
   *
   * @param list of Objects
   */
    private static final void loseDuplicates(ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            Object objI = list.get(i);
            int j = i + 1;
            while (j < list.size()) {
                Object objJ = list.get(j);
                if (!objI.equals(objJ)) {
                    j++;
                } else {
                    list.remove(j);
                }
            }
        }
    }

    public String optionUsage() {
        return super.optionUsage() + " [-x(debug)]";
    }

    /**
   * Query user about installation options.
   */
    private final void queryUser() {
        final int STEP_INSTALL_JAR = 0;
        final int STEP_DOWNLOAD_JAR = 1;
        final int STEP_USE_SUPPLIED = 2;
        final int STEP_INSTALL_JAVA = 3;
        final int STEP_CLUSTER = 4;
        final int STEP_FINISHED = 5;
        int step = 0;
        while (step < STEP_FINISHED) {
            switch(step) {
                case STEP_USE_SUPPLIED:
                    if (queryUserUseSuppliedJava()) {
                        step++;
                    } else {
                        step--;
                    }
                    break;
                case STEP_INSTALL_JAVA:
                    step += queryUserInstallJava();
                    break;
                case STEP_INSTALL_JAR:
                    step += queryUserInstallJar();
                    break;
                case STEP_DOWNLOAD_JAR:
                    if (queryUserDownloadJar()) {
                        step++;
                    } else {
                        step--;
                    }
                    break;
                case STEP_CLUSTER:
                    if (queryUserClusterPush()) {
                        step++;
                    } else {
                        step--;
                    }
                    break;
            }
            if (step < 0) {
                if (queryUserCancelInstall()) {
                    System.exit(0);
                    return;
                }
                step = 0;
            }
        }
    }

    /**
   * Ask user if they want to cancel the install.
   *
   * @return <tt>true</tt> if user wants to cancel.
   */
    private final boolean queryUserCancelInstall() {
        String canMsg = "Do you want to cancel this install?";
        int n = JOptionPane.showConfirmDialog(null, canMsg, "Cancel install?", JOptionPane.YES_NO_OPTION);
        return (n == JOptionPane.YES_OPTION);
    }

    /**
   * Ask user if they want to install everything on the cluster.
   *
   * @return false if [Cancel] button was pressed,
   *         true if another choice was made.
   */
    private final boolean queryUserClusterPush() {
        int result = JOptionPane.NO_OPTION;
        if (cPushStr != null) {
            String msg = "Would you like to push everything out to the cluster?";
            String title = "Push files to cluster?";
            result = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);
        }
        if (result != JOptionPane.YES_OPTION) {
            clusterInstaller = null;
        } else {
            clusterInstaller = new ClusterInstaller(cPushStr);
        }
        return (result != JOptionPane.CANCEL_OPTION);
    }

    /**
   * Ask user if they want to download the latest visad.jar
   *
   * @return false if [Cancel] button was pressed,
   *         true if another choice was made.
   */
    private final boolean queryUserDownloadJar() {
        int result = JOptionPane.YES_OPTION;
        if (installerJar != null) {
            String msg = "Would you like to download the latest " + JAR_NAME + "?";
            String title = "Download latest " + JAR_NAME + "?";
            result = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);
        }
        downloadLatestJar = (result == JOptionPane.YES_OPTION);
        return (result != JOptionPane.CANCEL_OPTION);
    }

    /**
   * Ask user to specify the directory in which the visad.jar file
   * should be installed.
   *
   * @return -1 if [Cancel] button was pressed,
   *          0 if a bad directory was selected,
   *          1 if a valid choice was made.
   */
    private final int queryUserInstallJar() {
        jarInstallDir = chooseDirectory(chooser, jarList, "Select the directory where the VisAD jar file should be installed");
        if (jarInstallDir == null) {
            return -1;
        }
        if (!jarInstallDir.canWrite()) {
            JOptionPane.showMessageDialog(null, "Cannot write to that directory!", "Bad directory?", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        return 1;
    }

    /**
   * Ask user to specify the directory in which the supplied
   * JDK should be installed.
   *
   * @return -1 if [Cancel] button was pressed,
   *          0 if a bad directory was selected,
   *          1 if a valid choice was made.
   */
    private final int queryUserInstallJava() {
        javaInstallDir = null;
        if (useSuppliedJava) {
            javaInstallDir = chooseDirectory(chooser, null, "Select the directory in which the JDK should be installed");
            if (javaInstallDir == null) {
                return -1;
            }
            if (!javaInstallDir.canWrite()) {
                JOptionPane.showMessageDialog(null, "Cannot write to that directory!", "Bad directory?", JOptionPane.ERROR_MESSAGE);
                return 0;
            }
        }
        return 1;
    }

    /**
   * Ask user if they'd like to have the supplied JDK installed.
   *
   * @return false if [Cancel] button was pressed,
   *         true if another choice was made.
   */
    private final boolean queryUserUseSuppliedJava() {
        int result = JOptionPane.NO_OPTION;
        if (installerJavaDir != null || installerJavaJar != null) {
            String msg;
            if (installerJavaDir != null) {
                msg = "Would you like to install the supplied " + " Java Development Kit " + installerJava.getMajor() + "." + installerJava.getMinor() + " (" + installerJava.getVersionString() + ")?";
            } else {
                msg = "Would you like to install the supplied " + " Java Development Kit?";
            }
            String title = "Install supplied JDK?";
            result = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);
        }
        useSuppliedJava = (result == JOptionPane.YES_OPTION);
        return (result != JOptionPane.CANCEL_OPTION);
    }

    public static final void main(String[] args) {
        new Main(args);
        System.exit(0);
    }
}
