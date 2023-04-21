package ch.oscg.jreleaseinfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Central class creates the java source file for
 * the desired properties.
 *
 * @author Thomas Cotting, Tangarena Engineering AG, Luzern
 * @version $Revision: 1.2 $ ($Date: 2005/08/06 14:12:36 $ / $Author: tcotting $)
 */
public class JReleaseInfoBean {

    /** Property name for Build Date. */
    public static final String PROPNAME_BUILDDATE = "buildDate";

    /** Exception message on invalid class name. */
    public static final String MESSAGE_EXC_CLASSNAME = "Invalid Java class name :";

    /** Exception message on invalid package name. */
    public static final String MESSAGE_EXC_PACKAGENAME = "Invalid package name :";

    /** Exception message on invalid filename. */
    public static final String MESSAGE_EXC_TARGETDIR = "Invalid target directory :";

    /** Target directory of version file to be created. */
    private String targetDir = null;

    /** ClassName of version file to be created .*/
    private String className = null;

    /** PackageName of version file to be created, 'null' means default package. */
    private String packageName = null;

    /**
    * Execute() method.
    * @param props Map containing the JReleaseInfoProperties
    *
    * @throws IllegalArgumentException on invalid arguments
    * @throws IOException on file writing problems
    */
    public void execute(Map props, SourceGeneratorIF sourceGenerator) throws IOException {
        if (!JReleaseInfoUtil.isValidClassNameString(className)) {
            throw new IllegalArgumentException(MESSAGE_EXC_CLASSNAME + className);
        }
        if (!JReleaseInfoUtil.isValidPackageNameString(packageName)) {
            throw new IllegalArgumentException(MESSAGE_EXC_PACKAGENAME + packageName);
        }
        if (!JReleaseInfoUtil.isValidNameString(targetDir)) {
            throw new IllegalArgumentException(MESSAGE_EXC_TARGETDIR + targetDir);
        }
        className = JReleaseInfoUtil.upperCaseFirstLetter(className);
        File versionFile = createJReleaseInfoFile();
        File parent = versionFile.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        if (versionFile.exists()) {
            versionFile.delete();
        }
        sourceGenerator.setProperties(props);
        sourceGenerator.setPackageName(packageName);
        sourceGenerator.setClassName(className);
        FileWriter fw = new FileWriter(versionFile);
        fw.write(sourceGenerator.createCode());
        fw.close();
    }

    /**
    * Utility method to write the class java code to a String.
    *
    * @return the created java class in a string
    */
    public File createJReleaseInfoFile() {
        String packageDir = null;
        if (packageName != null) {
            packageDir = packageName.replace('.', '/');
        }
        return new File(JReleaseInfoUtil.getPathElement(targetDir) + JReleaseInfoUtil.getPathElement(packageDir) + className + ".java");
    }

    /**
    * Utility method to delete the JReleaseInfo file (for test purposes).
    */
    public void deleteJReleaseInfoFile() {
        File file = createJReleaseInfoFile();
        if (file.exists()) {
            file.delete();
        }
    }

    /**
    * Set the class name of the JReleaseInfoAntTask class.
    *
    * @param className String
    */
    public void setClassName(String className) {
        if (className != null) {
            this.className = className.trim();
        }
    }

    /**
    * Set the package name of the JReleaseInfoAntTask class.
    *
    * @param packageName String
    */
    public void setPackageName(String packageName) {
        if ((packageName != null) && (packageName.trim() != "")) {
            this.packageName = packageName.trim().toLowerCase();
        }
    }

    /**
    * Set the target directory where the file should be created.
    *
    * @param targetDir Name of directory
    */
    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir.trim();
    }
}
