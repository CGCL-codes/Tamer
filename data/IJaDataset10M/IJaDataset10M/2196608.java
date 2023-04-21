package com.explosion.utilities;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.swing.JFileChooser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.explosion.utilities.exception.ExceptionManagerFactory;
import com.explosion.utilities.process.threads.ProcessThread;
import com.explosion.utilities.regex.RegExpFileAndDirectoryFilter;
import com.explosion.utilities.regex.RegExpFileNameFilter;

public class FileSystemUtils {

    private static Logger log = LogManager.getLogger(FileSystemUtils.class);

    public static final int SAVETYPE = 0;

    public static final int OPENTYPE = 1;

    public static final int FILES_ONLY = 0;

    public static final int FILES_AND_DIRECTORIES = 1;

    public static final int DIRECTORIES_ONLY = 2;

    private static Vector jarFileOnClassPath = null;

    private static Set jarFileNamesOnClassPath = null;

    private static Vector allManifestEntries = null;

    /**
     * The mode refers to FILES_ONLY, DIRECTORIES_ONLY or FILES_AND_DIRECTORIES
     * The type refers to SAVE or OPEN The rest is pretty self explanatory
     * 
     * @param parent
     * @param type
     * @param multipleFiles
     * @param defaultFile
     * @param mode
     * @return @throws Exception
     */
    public static File[] chooseFiles(Component parent, int type, boolean multipleFiles, File defaultFile, int mode) throws Exception {
        return chooseFiles(parent, type, multipleFiles, defaultFile, mode, null);
    }

    /**
     * The mode refers to FILES_ONLY, DIRECTORIES_ONLY or FILES_AND_DIRECTORIES
     * The type refers to SAVE or OPEN The rest is pretty self explanatory
     * 
     * @param parent
     * @param type
     * @param multipleFiles
     * @param defaultFile
     * @param mode
     * @param title
     * @return @throws Exception
     */
    public static File[] chooseFiles(Component parent, int type, boolean multipleFiles, File defaultFile, int mode, String title) throws Exception {
        JFileChooser fc = new JFileChooser();
        fc.setDoubleBuffered(true);
        fc.setMultiSelectionEnabled(multipleFiles);
        File currentFile = null;
        if (defaultFile != null && defaultFile.exists()) {
            currentFile = defaultFile;
        } else {
            currentFile = new File(System.getProperty("user.dir"));
        }
        if (currentFile.exists()) {
            if (currentFile.getParent() != null) {
                fc.setCurrentDirectory(currentFile);
            }
        }
        switch(mode) {
            case (FileSystemUtils.FILES_ONLY):
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                break;
            case (FileSystemUtils.FILES_AND_DIRECTORIES):
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                break;
            case (FileSystemUtils.DIRECTORIES_ONLY):
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                break;
        }
        if (title != null) fc.setDialogTitle(title);
        int result = -999;
        if (type == SAVETYPE) result = fc.showSaveDialog(parent); else if (type == OPENTYPE) result = fc.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedfiles = new File[1];
            if (multipleFiles) selectedfiles = fc.getSelectedFiles(); else selectedfiles[0] = fc.getSelectedFile();
            System.setProperty("user.dir", selectedfiles[0].getAbsolutePath());
            return selectedfiles;
        }
        return null;
    }

    /**
     * This method returns a list of all the jar files which are on the
     * classpath. It is optimized and only actually parses the classpath once per VM.
     * 
     * @return Vector - a list of the JarFile instance.
     */
    public static Vector getJarFileListFromClasspath() throws Exception {
        if (jarFileOnClassPath != null) return jarFileOnClassPath;
        StringTokenizer tokenizer = new StringTokenizer(System.getProperty("java.class.path"), System.getProperty("path.separator"));
        jarFileOnClassPath = new Vector();
        while (tokenizer.hasMoreTokens()) {
            String jarFilePath = tokenizer.nextToken();
            try {
                JarFile jarFile = new JarFile(jarFilePath.trim());
                jarFileOnClassPath.addElement(jarFile);
            } catch (java.util.zip.ZipException e) {
            }
        }
        return jarFileOnClassPath;
    }

    /**
     * This method returns a list of File objects in a Vector. It only returns
     * those Files that are not in Jar files and are not jar files. ie all the
     * free files
     * 
     * @return Vector free files
     */
    public static Vector getFreeFileListFromClassPath() throws Exception {
        return getFreeFileListFromClassPath("");
    }

    /**
     * This method returns a list of File objects in a Vector. It only returns
     * those Files that are not in Jar files and are not jar files. ie all the
     * free files and also only returns those ofthefreefiles that match the
     * given pattern This method is not optimized and the classpath freefiles
     * will be searched every time this method is called.
     * 
     * @param pattern of files to include in the list
     */
    public static Vector getFreeFileListFromClassPath(String pattern) throws Exception {
        String classPath = System.getProperty("java.class.path");
        Vector list = new Vector();
        StringTokenizer tokenizer = new StringTokenizer(classPath, System.getProperty("path.separator"));
        while (tokenizer.hasMoreElements()) {
            String tokenPath = tokenizer.nextToken();
            File directory = new File(tokenPath);
            if (directory.exists() && directory.isDirectory()) {
                Vector v = getFileList(true, directory.getAbsolutePath(), pattern, false);
                for (int i = 0; i < v.size(); i++) list.addElement(v.elementAt(i));
            }
        }
        return list;
    }

    /**
     * This method hunts down a mainfest file with an entry with the given name
     * and returns the atributes for that entry
     * 
     * @param manifestEntryName
     * @return Attributes froentry
     * @throws Exception
     */
    public static Attributes getManifestAttributesForEntry(String manifestEntryName) throws Exception {
        boolean found = false;
        Attributes attributes = null;
        Vector jarFileList = getJarFileListFromClasspath();
        for (int i = 0; i < jarFileList.size(); i++) {
            JarFile jarFile = (JarFile) jarFileList.elementAt(i);
            if (jarFile != null) {
                Manifest manifest = jarFile.getManifest();
                if (manifest != null) {
                    attributes = (Attributes) manifest.getEntries().get(manifestEntryName);
                    if (attributes != null) {
                        found = true;
                        break;
                    }
                }
            }
        }
        if (!found) {
            Vector freeFiles = getFreeFileListFromClassPath("manifest.mf");
            for (int i = 0; i < freeFiles.size(); i++) {
                Manifest manifest = new Manifest(new FileInputStream((File) freeFiles.elementAt(i)));
                attributes = (Attributes) manifest.getEntries().get(manifestEntryName);
                if (attributes != null) break;
            }
        }
        return attributes;
    }

    /**
     * This method hunts down all Entries in all manifest files on the class
     * path. This is optimized to only perform the operation once.
     * 
     * @return Vector of Attributes objects one for each Entry in all manifest
     *         files
     * @throws Exception
     */
    public static Vector getAllManifestEntries(String manifestMustHave) throws Exception {
        if (allManifestEntries != null) return allManifestEntries;
        allManifestEntries = new Vector();
        Vector jarFileList = getJarFileListFromClasspath();
        for (int i = 0; i < jarFileList.size(); i++) {
            JarFile jarFile = (JarFile) jarFileList.elementAt(i);
            if (jarFile != null) {
                Manifest manifest = jarFile.getManifest();
                if (manifest != null) {
                    if (manifest.getMainAttributes().getValue(manifestMustHave) != null) {
                        if (manifest.getEntries() != null) {
                            Collection collection = manifest.getEntries().values();
                            Iterator it = collection.iterator();
                            while (it.hasNext()) allManifestEntries.addElement(it.next());
                        }
                    }
                }
            }
        }
        Vector freeFiles = getFreeFileListFromClassPath("manifest.mf");
        for (int i = 0; i < freeFiles.size(); i++) {
            Manifest manifest = new Manifest(new FileInputStream((File) freeFiles.elementAt(i)));
            Set set = manifest.getMainAttributes().keySet();
            Iterator tit = set.iterator();
            if (manifest.getMainAttributes().getValue(manifestMustHave) != null) {
                if (manifest.getEntries() != null) {
                    Collection collection = manifest.getEntries().values();
                    Iterator it = collection.iterator();
                    while (it.hasNext()) allManifestEntries.addElement(it.next());
                }
            }
        }
        return allManifestEntries;
    }

    /**
     * The following method loads an image from the classes on the classpath.
     * It uses the classloader which loaded the component to search for the image.
     * If that doesn;t work it tries the system classloader
     */
    public static synchronized Image loadImage(String imagePath, Component component) throws Exception {
        URL url = null;
        Image img = null;
        try {
            if (component.getClass().getClassLoader() == null) {
                if (imagePath.getClass().getClassLoader() != null) url = imagePath.getClass().getClassLoader().getResource(imagePath);
            } else {
                url = component.getClass().getClassLoader().getResource(imagePath);
            }
            if (url == null) url = ClassLoader.getSystemResource(imagePath);
            if (url == null) url = component.getClass().getClassLoader().getResource(GeneralConstants.UNAVAILABLE_IMAGE);
            if (url == null) url = ClassLoader.getSystemResource(GeneralConstants.UNAVAILABLE_IMAGE);
            if (url == null) throw new Exception("Can find neither image '" + imagePath + "' nor image '" + GeneralConstants.UNAVAILABLE_IMAGE + "'");
            img = Toolkit.getDefaultToolkit().getImage(url);
            MediaTracker tracker = new MediaTracker(component);
            tracker.addImage(img, 0);
            try {
                tracker.waitForID(0);
            } catch (InterruptedException exc) {
            } catch (Exception e) {
                ExceptionManagerFactory.getExceptionManager().manageException(e, "Unable to load image '" + imagePath + "'.");
            }
        } catch (Exception e) {
            log.debug("Exception while getting image " + imagePath + " for " + component + ".  You should check that the image you are looking for is on the classpath.");
            throw e;
        }
        return img;
    }

    /**
     * Loads a text file from the classpath
     * @param filePath
     * @param loader
     * @return
     */
    public static String loadTextFileFromClasspath(String filePath, ClassLoader loader) {
        log.debug("Getting resource at " + filePath);
        InputStream stream = null;
        StringBuffer string = new StringBuffer();
        try {
            if (loader != null) {
                stream = loader.getResourceAsStream(filePath);
            }
            if (stream == null && loader == null) {
                if (filePath.getClass().getClassLoader() != null) stream = filePath.getClass().getClassLoader().getResourceAsStream(filePath);
            }
            if (stream == null) {
                stream = ClassLoader.getSystemResourceAsStream(filePath);
            }
            if (stream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = reader.readLine();
                while (line != null) {
                    string.append(line + System.getProperty("line.separator"));
                    line = reader.readLine();
                }
                reader.close();
            }
        } catch (Exception e) {
            log.debug("Exception while getting file " + filePath + ".  You should check that the file you are looking for is on the classpath.");
        }
        return string.toString();
    }

    /**
     * The following method builds the file list of files to be processed
     */
    public static Vector getFileList(boolean recursive, String directory, String pattern, boolean caseSensitive) throws Exception {
        return getFileList(null, recursive, directory, pattern, caseSensitive);
    }

    /**
     * The following method builds the file list of files to be processed
     */
    public static Vector getFileList(ProcessThread controller, boolean recursive, String directory, String pattern, boolean caseSensitive) throws Exception {
        String fileNamePattern = (pattern == null ? "" : pattern);
        RegExpFileAndDirectoryFilter regExpAndDirectoryFilter;
        RegExpFileNameFilter regExpFilter = new RegExpFileNameFilter(fileNamePattern, caseSensitive);
        File sourceDirectory = new File(directory);
        Vector fileVector = new Vector();
        File[] fileList;
        if (!sourceDirectory.exists()) throw new Exception("Unable to find path specified for creating a list of files: " + directory);
        if (!sourceDirectory.isDirectory()) throw new Exception("Path specified for creating a list of files is not a directory : " + directory);
        if (!recursive) {
            regExpAndDirectoryFilter = new RegExpFileAndDirectoryFilter(regExpFilter);
            if (controller != null) controller.getProcess().setStatusText("Listing directory " + sourceDirectory.getAbsolutePath());
            fileList = sourceDirectory.listFiles(regExpAndDirectoryFilter);
            for (int i = 0; i < fileList.length; i++) fileVector.addElement(fileList[i]);
        } else {
            buildFileListRecursive(controller, sourceDirectory, regExpFilter, fileVector);
        }
        return fileVector;
    }

    /**
     * The following recursive method assists in the building of the list of
     * files to process. Adapted from code written by Michael Liu
     */
    private static void buildFileListRecursive(ProcessThread controller, File directory, RegExpFileNameFilter regExpFilter, Vector fileVector) throws Exception {
        if (controller != null) {
            if (controller.getStatus() == ProcessThread.THREAD_STOPPED) return;
        }
        if (directory.isFile()) {
            if (regExpFilter.accept(directory.getParentFile(), directory.getName())) fileVector.addElement(directory);
        } else {
            if (controller != null) controller.getProcess().setStatusText("Listing directory " + directory.getAbsolutePath());
            File[] fileArray = directory.listFiles();
            Vector directoryList = new Vector();
            if (fileArray != null) {
                for (int i = 0; i < fileArray.length; i++) {
                    if (controller != null) {
                        if (controller.getStatus() == ProcessThread.THREAD_STOPPED) return;
                    }
                    if (fileArray[i].isFile()) {
                        if (regExpFilter.accept(fileArray[i].getParentFile(), fileArray[i].getName())) fileVector.addElement(fileArray[i]);
                    } else directoryList.addElement(fileArray[i]);
                }
            }
            for (int i = 0; i < directoryList.size(); i++) buildFileListRecursive(controller, (File) directoryList.elementAt(i), regExpFilter, fileVector);
        }
    }

    /**
     * The following method obtains an input stream to a file in a Zip/Jar file
     * 
     * @param jarFile
     * @param pathOfFileToGet (format eg: com/explosion/utils/FileUtils.java
     * @return InputStream or null if the file was not found
     */
    public static InputStream getInputStreamFromJarFile(JarFile jarFile, String pathOfFileToGet) throws Exception {
        if (jarFile != null) {
            JarEntry entry = jarFile.getJarEntry(pathOfFileToGet);
            if (entry != null) return jarFile.getInputStream(entry);
        }
        return null;
    }

    /**
     * Method checkGivenPathValid. Checks to see that the given path points to a
     * valid filepath It may notbe null or point to a directory. An exception
     * will bethrown if it doesn't
     * 
     * @param filePath
     * @return File
     * @throws IllegalArgumentException if the filePath provided is not valid
     */
    public static File checkGivenPathValid(String filePath) throws IllegalArgumentException {
        if (filePath == null) throw new IllegalArgumentException("Path to file is null.");
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) throw new IllegalArgumentException("The path '" + filePath + "' supplied points to a directory and not a file.");
        }
        return file;
    }
}
