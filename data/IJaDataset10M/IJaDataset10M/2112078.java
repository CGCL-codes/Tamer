package resrc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 
 */
public class ResourceLoader {

    /**
	 * retrieves the given icon
	 * @param r the file location.
	 * @return Icon at specified file location
	 */
    public static Icon getIcon(String r) {
        ClassLoader cl = ResourceLoader.class.getClassLoader();
        return new ImageIcon(cl.getResource(r));
    }

    public static InputStream getFile(String r) {
        ClassLoader cl = ResourceLoader.class.getClassLoader();
        return cl.getResourceAsStream(r);
    }

    public static String getAppleScript(String r) {
        ClassLoader cl = ResourceLoader.class.getClassLoader();
        return cl.getResource(r).getPath();
    }

    /**
	 * Retrieves a file from a remote url
	 * @param src
	 * @param dst
	 * @return true/false depending on the successful download
	 */
    public static boolean retrieveRemoteFile(String src, String dst) {
        try {
            copyInputStream(new BufferedInputStream(new URL(src).openStream()), new FileOutputStream(dst));
            return true;
        } catch (MalformedURLException mue) {
            System.out.println("ResourceLoader.retrieveRemoteFile::MalformedURLException");
            return false;
        } catch (IOException ioe) {
            System.out.println("ResourceLoader.retrieveRemoteFile::IOException::");
            ioe.printStackTrace();
            return false;
        }
    }

    /**
	 * Unjars the given jar file to the provided directory.
	 * 
	 * @param jar - name of the jar file
	 * @param destinationDir - name of destination directory
	 * @return true or false depemding on success
	 */
    public static boolean unjar(String jar, String destinationDir) {
        try {
            JarFile jarFile = new JarFile(jar);
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = (JarEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    System.out.println("Extracting directory: " + entry.getName());
                    (new File(entry.getName())).mkdir();
                    continue;
                }
                System.err.println("Extracting file: " + entry.getName());
                copyInputStream(jarFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(entry.getName())));
            }
            jarFile.close();
        } catch (IOException ioe) {
            System.out.println("io exception:");
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    private static String locatePluginInJar(File jar) {
        String result = "";
        try {
            JarFile jarFile = new JarFile(jar);
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = (JarEntry) entries.nextElement();
                if (entry.getName().endsWith("Plugin.class")) {
                    result = entry.getName();
                    System.out.println("FOUND PLUGIN--->" + entry.getName());
                }
            }
            jarFile.close();
        } catch (IOException ioe) {
            System.out.println("io exception:");
            ioe.printStackTrace();
        }
        return result;
    }

    private static String extractPluginDir(String p) {
        String pD = p.substring(0, p.length() - 13);
        System.out.println("Directory-->" + pD);
        String[] result = pD.split(File.separator);
        StringBuffer pDN = new StringBuffer();
        pDN.append("plugins").append(File.separator);
        for (int i = 0; i < result.length; i++) {
            pDN.append(result[i]).append(".");
            System.out.println(result[i]);
        }
        System.out.println("Resultdir:-->" + pDN.toString());
        return pDN.toString().substring(0, pDN.length() - 1);
    }

    private static boolean createPluginDir(String p) {
        File pluginDir = new File(extractPluginDir(p));
        System.out.println("Resultdir:-->" + pluginDir.toString());
        return ((!pluginDir.isDirectory()) && pluginDir.mkdir());
    }

    public static boolean deployJar(String jar) {
        File jarFile = new File(jar);
        String p = locatePluginInJar(jarFile);
        return (!p.equals("")) && createPluginDir(p) && jarFile.renameTo(new File(extractPluginDir(p) + File.separator + jarFile.getName()));
    }

    /**
	 * copies streams from a to b
	 * @param in
	 * @param out
	 * @throws IOException
	 */
    private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
        in.close();
        out.close();
    }
}
