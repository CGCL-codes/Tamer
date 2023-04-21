package org.jopendocument.link;

import org.openconcerto.utils.DesktopEnvironment;
import org.openconcerto.utils.DesktopEnvironment.Mac;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class finds out where OpenOffice.org is installed.
 * 
 * @author Sylvain CUAZ
 * @see #getInstance()
 */
public class OOInstallation {

    private static OOInstallation instance;

    /**
     * Return the installation for this machine.
     * 
     * @return the installation for this machine, <code>null</code> if not installed.
     * @throws IOException if an error occurs while searching.
     */
    public static OOInstallation getInstance() throws IOException {
        if (instance == null) {
            instance = detectInstall();
        }
        return instance;
    }

    /**
     * Forget the current installation to pick up a change (e.g. updated version).
     */
    public static void reset() {
        instance = null;
    }

    private static final Pattern stringValuePattern = Pattern.compile("^\\s*(.+?)\\s+REG_SZ\\s+(.+?)$", Pattern.MULTILINE);

    private static final String LOBundleID = "org.libreoffice.script";

    private static final String OOBundleID = "org.openoffice.script";

    private static String cmdSubstitution(String... args) throws IOException {
        final ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(false);
        return DesktopEnvironment.cmdSubstitution(pb.start());
    }

    static final URL toURL(final File f) {
        try {
            return f.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Couldn't transform to URL " + f, e);
        }
    }

    private static String findRootPath() {
        final String[] rootPaths = { "HKEY_LOCAL_MACHINE\\SOFTWARE\\LibreOffice", "HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\LibreOffice", "HKEY_LOCAL_MACHINE\\SOFTWARE\\OpenOffice.org", "HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\OpenOffice.org" };
        for (final String p : rootPaths) {
            if (DesktopEnvironment.test("reg", "query", p)) return p;
        }
        return null;
    }

    private static File findBundleDir() throws IOException {
        final Mac de = (Mac) DesktopEnvironment.getDE();
        for (final String bundleID : new String[] { LOBundleID, OOBundleID }) {
            final File url = de.getAppDir(bundleID);
            if (url != null) return url;
        }
        return null;
    }

    private static Map<String, String> getStringValues(final String path, final String option) throws IOException {
        final Map<String, String> values = new HashMap<String, String>();
        final String out = DesktopEnvironment.cmdSubstitution(Runtime.getRuntime().exec(new String[] { "reg", "query", path, option }));
        final Matcher matcher = stringValuePattern.matcher(out);
        while (matcher.find()) {
            values.put(matcher.group(1), matcher.group(2));
        }
        return values;
    }

    private static final void addPaths(final List<File> cp, final File progDir, final String basisDir, final String ureDir) throws IOException {
        add(cp, new File(progDir, "classes"));
        if (ureDir != null) {
            add(cp, ureDir + File.separator + "java");
            add(cp, ureDir + File.separator + "share" + File.separator + "java");
        }
        if (basisDir != null) add(cp, basisDir + File.separator + "program" + File.separator + "classes");
    }

    private static final void addUnixPaths(final List<File> cp, final File progDir) throws IOException {
        final File baseDir = progDir.getParentFile();
        final File basisDir = new File(baseDir, "basis-link");
        final String basisPath = (basisDir.exists() ? basisDir : baseDir).getPath();
        final String ureDir = basisPath + File.separator + "ure-link";
        addPaths(cp, progDir, basisPath, ureDir);
    }

    private static void add(final List<File> res, final File f) {
        if (f != null && f.isDirectory() && !res.contains(f)) {
            res.add(f);
        }
    }

    private static void add(final List<File> res, final String f) {
        if (f != null) add(res, new File(f));
    }

    private static OOInstallation detectInstall() throws IOException {
        final File exe;
        final List<File> cp = new ArrayList<File>(3);
        final String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            final String rootPath = findRootPath();
            if (rootPath == null) return null;
            final boolean libreOffice = rootPath.contains("LibreOffice");
            final Map<String, String> unoValues = getStringValues(rootPath + "\\UNO\\InstallPath", "/ve");
            if (unoValues.size() != 1) throw new IOException("No UNO install path: " + unoValues);
            final File unoPath = new File(unoValues.values().iterator().next());
            if (!unoPath.isDirectory()) throw new IOException(unoPath + " is not a directory");
            exe = new File(unoPath, "soffice.exe");
            final String layerPath;
            if (!libreOffice) {
                layerPath = "\\Layers\\OpenOffice.org";
            } else if (DesktopEnvironment.test("reg", "query", rootPath + "\\Layers")) {
                layerPath = "\\Layers\\LibreOffice";
            } else {
                layerPath = "\\Layers_\\LibreOffice";
            }
            final Map<String, String> layersValues = getStringValues(rootPath + layerPath, "/s");
            addPaths(cp, unoPath, layersValues.get("BASISINSTALLLOCATION"), layersValues.get("UREINSTALLLOCATION"));
        } else if (os.startsWith("Mac OS")) {
            final File appPkg = findBundleDir();
            if (appPkg == null) return null;
            exe = new File(appPkg, "Contents/MacOS/soffice");
            addUnixPaths(cp, new File(appPkg, "Contents/program"));
        } else if (os.startsWith("Linux")) {
            final String binPath = cmdSubstitution("which", "soffice").trim();
            if (binPath.length() != 0) {
                exe = new File(binPath).getCanonicalFile();
            } else {
                final File defaultInstall = new File("/usr/lib/openoffice/program/soffice");
                exe = defaultInstall.canExecute() ? defaultInstall : null;
            }
            if (exe != null) addUnixPaths(cp, exe.getParentFile());
        } else exe = null;
        return exe == null ? null : new OOInstallation(exe, cp);
    }

    private final File executable;

    private final List<File> classpath;

    private OOInstallation(File executable, List<File> classpath) throws IOException {
        super();
        if (!executable.isFile()) throw new IOException("executable not found at " + executable);
        this.executable = executable;
        this.classpath = Collections.unmodifiableList(new ArrayList<File>(classpath));
    }

    public final File getExecutable() {
        return this.executable;
    }

    public final List<File> getClasspath() {
        return this.classpath;
    }

    public final List<URL> getURLs(final Set<String> jars) {
        final int stop = this.getClasspath().size();
        final List<URL> res = new ArrayList<URL>();
        for (int i = 0; i < stop; i++) {
            final File[] foundJars = this.getClasspath().get(i).listFiles(new FileFilter() {

                @Override
                public boolean accept(File f) {
                    return jars.contains(f.getName());
                }
            });
            for (final File foundJar : foundJars) {
                res.add(toURL(foundJar));
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " exe: " + this.getExecutable() + " classpath: " + this.getClasspath();
    }

    public static void main(String[] args) {
        try {
            final OOInstallation i = getInstance();
            System.out.println(i == null ? "Not installed" : i);
        } catch (IOException e) {
            System.out.println("Couldn't detect OpenOffice.org: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
