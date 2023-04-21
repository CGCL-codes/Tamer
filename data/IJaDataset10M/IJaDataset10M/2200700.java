package jaxlib.jaxlib_private.jni;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import jaxlib.JaXLib;
import jaxlib.io.IO;
import jaxlib.io.file.Files;
import jaxlib.io.stream.BufferedXOutputStream;
import jaxlib.system.SystemProperties;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: LoadInternalLibraryAction.java 1376 2005-04-18 01:18:38Z joerg_wassmer $
 */
public final class LoadInternalLibraryAction extends Object implements PrivilegedAction<Void> {

    private static final Hashtable<String, Boolean> loadedLibs = new Hashtable<String, Boolean>(1);

    private static File deploymentDir;

    private final ClassLoader classLoader;

    private final String libName;

    public LoadInternalLibraryAction(ClassLoader classLoader, String libName) {
        super();
        assert (classLoader != null);
        assert (libName.trim() == libName) : libName;
        this.classLoader = classLoader;
        this.libName = libName;
    }

    private static String getArch() {
        String arch = System.getProperty(SystemProperties.OS_ARCH);
        if (arch != null) {
            if (arch.startsWith("i") && arch.endsWith("86")) arch = "i586";
        }
        return arch;
    }

    private static File getDeploymentDir() {
        synchronized (loadedLibs) {
            if (deploymentDir != null) return deploymentDir;
            File d = null;
            String userHomePath = System.getProperty(SystemProperties.USER_HOME);
            if (userHomePath != null) {
                File userHome = new File(userHomePath);
                if (userHome.isDirectory()) {
                    d = new File(userHome, ".java");
                    if (d.isDirectory()) {
                        d = new File(userHome, ".java/lib/jaxlib/native");
                        d.mkdirs();
                    } else {
                        d = new File(userHome, "java");
                        if (d.isDirectory()) {
                            d = new File(d, "lib/jaxlib/native");
                            d.mkdirs();
                        } else {
                            d = new File(userHome, ".java/lib/jaxlib/native");
                            d.mkdirs();
                        }
                    }
                }
            }
            if ((d != null) && d.isDirectory() && d.canWrite()) {
                deploymentDir = d;
                return d;
            } else {
                try {
                    d = Files.createTempDirectory();
                    d.deleteOnExit();
                } catch (Throwable ex) {
                    throw (UnsatisfiedLinkError) new UnsatisfiedLinkError("error creating temp dir").initCause(ex);
                }
            }
            deploymentDir = d;
            return d;
        }
    }

    private static File getFile(URL url, String mappedLibName) {
        String path = url.toString();
        if (path.startsWith("file:")) {
            return new File(path.substring("file:".length(), path.length()));
        } else {
            File dir = getDeploymentDir();
            File f = new File(dir, mappedLibName);
            if (f.isFile()) return f;
            InputStream in = null;
            BufferedXOutputStream out = null;
            try {
                Logger.global.config("transferring library from url to file:\n  url =" + url + "\n  file = " + f + "\n");
                in = url.openStream();
                out = new BufferedXOutputStream(Files.createLockedOutputStream(f, 3, TimeUnit.SECONDS));
                out.transferFrom(in, -1);
                f.deleteOnExit();
                dir.deleteOnExit();
                return f;
            } catch (Throwable ex) {
                Logger.global.log(Level.WARNING, "error transferring library from url to file:\n  url =" + url + "\n  file = " + f + "\n", ex);
                if (f != null) f.delete();
                throw (UnsatisfiedLinkError) new UnsatisfiedLinkError("error installing library to temp dir").initCause(ex);
            } finally {
                IO.tryClose(in);
                IO.tryClose(out);
            }
        }
    }

    private String getMappedLibraryName() {
        String s = System.mapLibraryName(this.libName);
        if (s.endsWith(".so")) {
            return s + "." + getVersionString();
        } else {
            int a = s.lastIndexOf('.');
            if (a < 0) throw new UnsatisfiedLinkError("don't now how to map library name on this system");
            return s.substring(0, a) + "-" + getVersionString() + s.substring(a, s.length());
        }
    }

    private static String getOS() {
        String os = System.getProperty(SystemProperties.OS_NAME);
        if (os != null) {
            String s = os.toLowerCase();
            if (s.indexOf("linux") >= 0) os = "linux"; else if (s.indexOf("windows") >= 0) os = "windows"; else if (s.indexOf("solaris") >= 0) os = "solaris"; else if (s.indexOf("os/2") >= 0) os = "os2";
        }
        return os;
    }

    private static String getVersionString() {
        return JaXLib.getMajorVersion() + "." + JaXLib.getMinorVersion() + "." + JaXLib.getSubVersion();
    }

    public Void run() {
        Boolean loaded = loadedLibs.get(this.libName);
        if (loaded == Boolean.TRUE) return null; else if (loaded == Boolean.FALSE) throw new UnsatisfiedLinkError("loading of library already failed previously: " + this.libName);
        if (!this.libName.equals("jaxlib-jni")) {
            LoadInternalLibraryAction a = new LoadInternalLibraryAction(this.classLoader, "jaxlib-jni");
            a.run();
        }
        try {
            run0();
            loadedLibs.put(this.libName, Boolean.TRUE);
            return null;
        } catch (UnsatisfiedLinkError err) {
            loadedLibs.put(this.libName, Boolean.FALSE);
            Logger.global.log(Level.CONFIG, "native library unavailable: " + this.libName, err);
            throw err;
        }
    }

    private void run0() {
        UnsatisfiedLinkError err;
        try {
            System.loadLibrary(this.libName);
            Logger.global.config("native system library loaded: " + this.libName);
            return;
        } catch (UnsatisfiedLinkError ex) {
            err = ex;
        }
        String libName = getMappedLibraryName();
        String os = getOS();
        if (os == null) throw err;
        String arch = getArch();
        if (arch == null) throw err;
        String resName = "jaxlib/native/" + os + "/" + arch + "/" + libName;
        URL url = classLoader.getResource(resName);
        if (url == null) throw err;
        File file = getFile(url, libName);
        if (file == null) throw err;
        Logger.global.config("loading library from file: " + file);
        System.load(file.getAbsolutePath());
        return;
    }
}
