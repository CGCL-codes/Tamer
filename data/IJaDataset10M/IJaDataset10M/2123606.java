package edu.rice.cs.drjava.model;

import java.io.File;
import java.util.Set;
import java.util.HashSet;
import edu.rice.cs.drjava.DrJava;
import edu.rice.cs.drjava.config.OptionConstants;
import edu.rice.cs.plt.io.IOUtil;

/** Some common methods for determining Java source files, language level files, etc.
  * @version $Id: DrJavaFileUtils.java 5395 2010-09-21 15:26:15Z mgricken $
  */
public class DrJavaFileUtils {

    /** Return the set of source file extensions that this compiler supports.
    * @return the set of source file extensions that this compiler supports. */
    public static Set<String> getSourceFileExtensions() {
        HashSet<String> extensions = new HashSet<String>();
        extensions.add(OptionConstants.JAVA_FILE_EXTENSION);
        extensions.add(OptionConstants.DJ_FILE_EXTENSION);
        extensions.add(OptionConstants.OLD_DJ0_FILE_EXTENSION);
        extensions.add(OptionConstants.OLD_DJ1_FILE_EXTENSION);
        extensions.add(OptionConstants.OLD_DJ2_FILE_EXTENSION);
        return extensions;
    }

    /** Return the suggested file extension that will be appended to a file without extension.
    * @return the suggested file extension */
    public static String getSuggestedFileExtension() {
        return OptionConstants.LANGUAGE_LEVEL_EXTENSIONS[DrJava.getConfig().getSetting(OptionConstants.LANGUAGE_LEVEL)];
    }

    /** .java --> true
    * .dj   --> true
    * .dj0  --> true
    * .dj1  --> true
    * .dj2  --> true
    * otherwise false
    * @return true if the file is a Java or language level file. */
    public static boolean isSourceFile(String fileName) {
        return fileName.endsWith(OptionConstants.JAVA_FILE_EXTENSION) || fileName.endsWith(OptionConstants.DJ_FILE_EXTENSION) || fileName.endsWith(OptionConstants.OLD_DJ0_FILE_EXTENSION) || fileName.endsWith(OptionConstants.OLD_DJ1_FILE_EXTENSION) || fileName.endsWith(OptionConstants.OLD_DJ2_FILE_EXTENSION);
    }

    /** @return true if the file is a Java or language level file. */
    public static boolean isSourceFile(File f) {
        File canonicalFile = IOUtil.attemptCanonicalFile(f);
        String fileName = canonicalFile.getPath();
        return isSourceFile(fileName);
    }

    /** .dj   --> true
    * .dj0  --> true
    * .dj1  --> true
    * .dj2  --> true
    * otherwise false
    * @return true if the file is a language level file. */
    public static boolean isLLFile(String fileName) {
        return fileName.endsWith(OptionConstants.DJ_FILE_EXTENSION) || fileName.endsWith(OptionConstants.OLD_DJ0_FILE_EXTENSION) || fileName.endsWith(OptionConstants.OLD_DJ1_FILE_EXTENSION) || fileName.endsWith(OptionConstants.OLD_DJ2_FILE_EXTENSION);
    }

    /** @return true if the file is a language level file. */
    public static boolean isLLFile(File f) {
        File canonicalFile = IOUtil.attemptCanonicalFile(f);
        String fileName = canonicalFile.getPath();
        return isLLFile(fileName);
    }

    /** .dj0  --> true
    * .dj1  --> true
    * .dj2  --> true
    * otherwise false
    * @return true if the file is an old language level file. */
    public static boolean isOldLLFile(String fileName) {
        return fileName.endsWith(OptionConstants.OLD_DJ0_FILE_EXTENSION) || fileName.endsWith(OptionConstants.OLD_DJ1_FILE_EXTENSION) || fileName.endsWith(OptionConstants.OLD_DJ2_FILE_EXTENSION);
    }

    /** @return true if the file is an old language level file. */
    public static boolean isOldLLFile(File f) {
        File canonicalFile = IOUtil.attemptCanonicalFile(f);
        String fileName = canonicalFile.getPath();
        return isOldLLFile(fileName);
    }

    /** .pjt  --> true
    * otherwise false
    * @return true if the file is an old project file. */
    public static boolean isOldProjectFile(String fileName) {
        return fileName.endsWith(OptionConstants.OLD_PROJECT_FILE_EXTENSION);
    }

    /** @return true if the file is an old project file. */
    public static boolean isOldProjectFile(File f) {
        File canonicalFile = IOUtil.attemptCanonicalFile(f);
        String fileName = canonicalFile.getPath();
        return isOldProjectFile(fileName);
    }

    /** .pjt    --> true
    * .drjava --> true
    * .xml    --> true
    * otherwise false
    * @return true if the file is a project file. */
    public static boolean isProjectFile(String fileName) {
        return fileName.endsWith(OptionConstants.PROJECT_FILE_EXTENSION) || fileName.endsWith(OptionConstants.PROJECT_FILE_EXTENSION2) || fileName.endsWith(OptionConstants.OLD_PROJECT_FILE_EXTENSION);
    }

    /** @return true if the file is a project file. */
    public static boolean isProjectFile(File f) {
        File canonicalFile = IOUtil.attemptCanonicalFile(f);
        String fileName = canonicalFile.getPath();
        return isProjectFile(fileName);
    }

    /** A.dj   --> A.java
    * A.dj0  --> A.java
    * A.dj1  --> A.java
    * A.dj2  --> A.java
    * otherwise unchanged
    * @return matching Java file for a language level file. */
    public static String getJavaForLLFile(String fileName) {
        if (fileName.endsWith(OptionConstants.DJ_FILE_EXTENSION)) {
            return fileName.substring(0, fileName.lastIndexOf(OptionConstants.DJ_FILE_EXTENSION)) + OptionConstants.JAVA_FILE_EXTENSION;
        } else if (fileName.endsWith(OptionConstants.OLD_DJ0_FILE_EXTENSION)) {
            return fileName.substring(0, fileName.lastIndexOf(OptionConstants.OLD_DJ0_FILE_EXTENSION)) + OptionConstants.JAVA_FILE_EXTENSION;
        } else if (fileName.endsWith(OptionConstants.OLD_DJ1_FILE_EXTENSION)) {
            return fileName.substring(0, fileName.lastIndexOf(OptionConstants.OLD_DJ1_FILE_EXTENSION)) + OptionConstants.JAVA_FILE_EXTENSION;
        } else if (fileName.endsWith(OptionConstants.OLD_DJ2_FILE_EXTENSION)) {
            return fileName.substring(0, fileName.lastIndexOf(OptionConstants.OLD_DJ2_FILE_EXTENSION)) + OptionConstants.JAVA_FILE_EXTENSION;
        } else return fileName;
    }

    /** @return matching Java file for a language level file. */
    public static File getJavaForLLFile(File f) {
        File canonicalFile = IOUtil.attemptCanonicalFile(f);
        String fileName = canonicalFile.getPath();
        return new File(getJavaForLLFile(fileName));
    }

    /** A.java --> A.dj
    * otherwise unchanged
    * @return matching .dj file for a .java file. */
    public static File getDJForJavaFile(File f) {
        return getFileWithDifferentExt(f, OptionConstants.JAVA_FILE_EXTENSION, OptionConstants.DJ_FILE_EXTENSION);
    }

    /** A.java --> A.dj0
    * otherwise unchanged
    * @return matching .dj0 file for a .java file. */
    public static File getDJ0ForJavaFile(File f) {
        return getFileWithDifferentExt(f, OptionConstants.JAVA_FILE_EXTENSION, OptionConstants.OLD_DJ0_FILE_EXTENSION);
    }

    /** A.java --> A.dj1
    * otherwise unchanged
    * @return matching .dj1 file for a .java file. */
    public static File getDJ1ForJavaFile(File f) {
        return getFileWithDifferentExt(f, OptionConstants.JAVA_FILE_EXTENSION, OptionConstants.OLD_DJ1_FILE_EXTENSION);
    }

    /** A.java --> A.dj2
    * otherwise unchanged
    * @return matching .dj2 file for a .java file. */
    public static File getDJ2ForJavaFile(File f) {
        return getFileWithDifferentExt(f, OptionConstants.JAVA_FILE_EXTENSION, OptionConstants.OLD_DJ2_FILE_EXTENSION);
    }

    /** A.java --> A.dj
    * otherwise unchanged
    * @return matching .dj file for a .java file. */
    public static String getDJForJavaFile(String f) {
        return getFileWithDifferentExt(f, OptionConstants.JAVA_FILE_EXTENSION, OptionConstants.DJ_FILE_EXTENSION);
    }

    /** A.java --> A.dj0
    * otherwise unchanged
    * @return matching .dj0 file for a .java file. */
    public static String getDJ0ForJavaFile(String f) {
        return getFileWithDifferentExt(f, OptionConstants.JAVA_FILE_EXTENSION, OptionConstants.OLD_DJ0_FILE_EXTENSION);
    }

    /** A.java --> A.dj1
    * otherwise unchanged
    * @return matching .dj1 file for a .java file. */
    public static String getDJ1ForJavaFile(String f) {
        return getFileWithDifferentExt(f, OptionConstants.JAVA_FILE_EXTENSION, OptionConstants.OLD_DJ1_FILE_EXTENSION);
    }

    /** A.java --> A.dj2
    * otherwise unchanged
    * @return matching .dj2 file for a .java file. */
    public static String getDJ2ForJavaFile(String f) {
        return getFileWithDifferentExt(f, OptionConstants.JAVA_FILE_EXTENSION, OptionConstants.OLD_DJ2_FILE_EXTENSION);
    }

    /** A.dj0 -> A.dj
    * A.dj1 -> A.dj
    * A.dj2 -> A.java
    * otherwise unchanged
    * @return new language level file matching an old language level file. */
    public static String getNewLLForOldLLFile(String fileName) {
        if (fileName.endsWith(OptionConstants.OLD_DJ0_FILE_EXTENSION)) {
            return fileName.substring(0, fileName.lastIndexOf(OptionConstants.OLD_DJ0_FILE_EXTENSION)) + OptionConstants.DJ_FILE_EXTENSION;
        } else if (fileName.endsWith(OptionConstants.OLD_DJ1_FILE_EXTENSION)) {
            return fileName.substring(0, fileName.lastIndexOf(OptionConstants.OLD_DJ1_FILE_EXTENSION)) + OptionConstants.DJ_FILE_EXTENSION;
        } else if (fileName.endsWith(OptionConstants.OLD_DJ2_FILE_EXTENSION)) {
            return fileName.substring(0, fileName.lastIndexOf(OptionConstants.OLD_DJ2_FILE_EXTENSION)) + OptionConstants.JAVA_FILE_EXTENSION;
        } else return fileName;
    }

    /** @return new language level file matching an old language level file. */
    public static File getNewLLForOldLLFile(File f) {
        File canonicalFile = IOUtil.attemptCanonicalFile(f);
        String fileName = canonicalFile.getPath();
        return new File(getNewLLForOldLLFile(fileName));
    }

    /** getFileWithDifferentExt("A.java", ".java", ".dj") --> "A.dj"
    * @return matching file with extension dest for a file with extension source. */
    public static String getFileWithDifferentExt(String fileName, String source, String dest) {
        if (fileName.endsWith(source)) {
            return fileName.substring(0, fileName.lastIndexOf(source)) + dest;
        } else return fileName;
    }

    /** getFileWithDifferentExt(new File("A.java"), ".java", ".dj") ~= new File("A.dj")
    * @return matching file with extension dest for a file with extension source. */
    public static File getFileWithDifferentExt(File f, String source, String dest) {
        File canonicalFile = IOUtil.attemptCanonicalFile(f);
        String fileName = canonicalFile.getPath();
        return new File(getFileWithDifferentExt(fileName, source, dest));
    }

    /** Returns the relative directory (from the source root) that the source file with this qualifed name will be in, 
    * given its package. Returns the empty string for classes without packages.
    * @param className The fully qualified class name
    */
    public static String getPackageDir(String className) {
        int lastDotIndex = className.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        } else {
            String packageName = className.substring(0, lastDotIndex);
            packageName = packageName.replace('.', File.separatorChar);
            return packageName + File.separatorChar;
        }
    }

    /** @return the file without the extension; the dot is removed too. */
    public static String removeExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }

    /** @return the extension, including the dot. */
    public static String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex);
    }
}
