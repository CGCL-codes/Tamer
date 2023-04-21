package gnujatella.utils;

import java.net.*;
import java.util.*;
import java.io.*;
import gnujatella.event.*;

/**
 * Provides basic funtcions for the update mechanism.
 */
public class UpdateUtil {

    private static AdvancedProperties repositoryInfo;

    private static URL repository;

    /**
         * Returns the URL of the currently used repository.
         */
    public static URL getRepository() {
        if (repository == null) try {
            repository = new URL("http://gnujatella.sourceforge.net/patches/");
        } catch (MalformedURLException e) {
        }
        return repository;
    }

    /**
         * Returns an AdvancedProperties object loaded with the
         * repository's info-file.
         */
    public static AdvancedProperties getRepositoryInfo() {
        if (repositoryInfo == null) try {
            repositoryInfo = new AdvancedProperties(new URL(getRepository(), "newest.txt"));
        } catch (Exception e) {
        }
        return repositoryInfo;
    }

    /**
         * Returns the sitename found in the repository's info-file.
         */
    public static String getRepositorySiteName() {
        return getRepositoryInfo().getProperty("site.name");
    }

    /**
         * Returns the site description found in the repository's info-file.
         */
    public static String getRepositorySiteDesc() {
        return getRepositoryInfo().getProperty("site.desc");
    }

    /**
         * Returns the site's logo found in the repository's info-file.
         */
    public static URL getRepositoryLogoURL() throws MalformedURLException {
        return new URL(repository, getRepositoryInfo().getProperty("site.logo"));
    }

    /**
         * Returns the version of the latest complete version found
         * in the repository's info-file.
         */
    public static String getLatestComplete() {
        return getRepositoryInfo().getProperty("complete.version");
    }

    /**
         * Returns the patch-level of the latest complete version found
         * in the repository's info-file.
         */
    public static int getLatestCompletePatchLevel() {
        return Integer.valueOf(getRepositoryInfo().getProperty("complete.patch")).intValue();
    }

    /**
         * Returns the filename of the latest complete version according
         * to the information found in the repository's info-file.
         */
    public static String getLatestCompleteName() {
        return "gnujatella-" + getLatestComplete() + ".jar";
    }

    /**
         * Returns the url of the latest complete version according
         * to the information found in the repository's info-file.
         */
    public static URL getLatestCompleteURL() {
        try {
            return new URL(getRepository(), getLatestCompleteName());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
         * Returns the latest patch found in the repository's info-file.
         */
    public static int getLatestPatch() {
        return Integer.valueOf(getRepositoryInfo().getProperty("patch")).intValue();
    }

    /**
         * Downloads the latest patch in the repository to the target directory.
         */
    public static void downloadPatch(File targetDir, int patch, ProgressListener listener) throws Exception {
        String patchName = "gnujatella-patch-" + patch + ".jar";
        URL patchURL = new URL(getRepository(), patchName);
        File patchFile = new File(targetDir.getPath(), patchName);
        IOUtil.downloadFile(patchURL, patchFile, listener);
    }

    /**
         * Reads a list from a configuration file.
         * The list needs to start with the key in rectangular braces and
         * ends either with another key or with an EOF.
         */
    public static Vector readList(File file, String key) {
        Vector vec = new Vector();
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(file));
            IOUtil.readUntil(reader, key);
            while (true) {
                String str = reader.readLine().trim();
                if (str.startsWith("[") && str.endsWith("]")) break;
                if (!str.equals("")) vec.add(str);
            }
        } catch (Exception e) {
        }
        return vec;
    }

    /**
         * Compares two directories and stores the files
         * differing in the returned Vector.
         */
    public static Vector compareDirs(File newDir, File oldDir) {
        Vector vec = new Vector();
        compareDirsRec(newDir, oldDir, vec);
        System.out.println();
        return vec;
    }

    /**
         * Recursive method used by compareDirs( File, File ).
         */
    protected static void compareDirsRec(File newDir, File oldDir, Vector vec) {
        System.out.print(".");
        if (newDir.isDirectory()) {
            File[] files = newDir.listFiles();
            for (int i = 0; i < files.length; i++) if (!files[i].getName().equals("CVS")) compareDirsRec(files[i], new File(oldDir.getPath(), files[i].getName()), vec);
        } else {
            if (!IOUtil.compareFiles(newDir, oldDir)) {
                vec.add(newDir);
                return;
            }
        }
    }

    /**
         * Applies a patch found in patchDir to the files found in
         * workingDir and returns the comment string.
         * Both the patch and the programm needs to be extracted
         * out of the archives before this method is called.
         * the file "resources/default.conf" is changed according
         * to the versions and descriptions found in the patche's
         * "changes.txt" file.
         */
    public static String applyPatch(File patchDir, File workingDir) throws IOException {
        String commentStr = "";
        AdvancedProperties appProp = new AdvancedProperties(workingDir, "gnujatella/resources/default.conf");
        File changeFile = new File(workingDir.getPath(), "patch/changes.txt");
        LineNumberReader reader = new LineNumberReader(new FileReader(changeFile));
        String patchLevel = reader.readLine();
        String patchType = reader.readLine();
        String version = reader.readLine();
        Vector comment = readList(changeFile, "[comment]");
        Vector delete = readList(changeFile, "[delete]");
        Vector files = readList(changeFile, "[files]");
        if (comment.size() > 0) for (int j = 0; j < comment.size(); j++) commentStr += " " + (String) comment.get(j);
        if (delete.size() > 0) for (int j = 0; j < delete.size(); j++) new File(workingDir.getPath(), (String) delete.get(j)).delete();
        if (files.size() > 0) for (int j = 0; j < files.size(); j++) {
            String str = (String) files.get(j);
            File dst = new File(workingDir.getPath(), str.substring(0, str.indexOf("=")));
            File src = new File(patchDir.getPath(), str.substring(str.indexOf("#")));
            IOUtil.downloadFile(src.toURL(), dst, null);
        }
        appProp.setProperty("application.version", version);
        appProp.setProperty("application.patchlevel", patchLevel);
        appProp.saveToFile(workingDir, "gnujatella/resources/default.conf", "GnuJaTella " + version + " - patchlevel #" + patchLevel);
        return commentStr;
    }
}
