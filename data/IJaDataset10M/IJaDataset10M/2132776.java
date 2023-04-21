package uk.ac.ebi.intact.struts.framework.util;

import java.io.*;
import java.util.Properties;
import uk.ac.ebi.intact.util.Assert;

/**
 * This utility class generates IntactTypes.properties file using the
 * classes in uk.ac.ebi.intact.model package. The algorithm as follows.
 * First, the program
 * searches for all the classes with uk.ac.ebi.intact.model package.
 * If a class has CvObject as its parent class then it is saved to a properties
 * file under an abbreviated class name. For example,
 * uk.ac.ebi.intact.model.CvTopic saved under CvTopic.
 * All decendents of CvObject apart from CvObject itself are stored.
 *
 * <b>Note</b> This program assumes that all the decendents of CvObject
 * are in the package uk.ac.ebi.intact.model. It also assumes that classes
 * already exists in this location (ie., compile sources before running this
 * tool).
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: IntactTypesGenerator.java 8 2002-10-03 17:52:00Z hhe $
 */
public class IntactTypesGenerator {

    /**
     * The root of the classpath. The package uk.ac.ebi.intact.model
     * should be able to access from this value.
     */
    private String myRoot;

    /**
     * The name of the resource file to write Intact types.
     */
    private String myResourceName;

    /**
     * Constructor that accepts jar filename.
     *
     * @param root the root of the classpath to access uk.ac.ebi.intact.model.
     * @param dest the name of the resource file to write Intact types. The
     * postfix '.properties' is appended to the name if it is not present. This
     * shouldn't be null.
     *
     * @pre root != null
     * @pre dest != null
     */
    public IntactTypesGenerator(String root, String dest) {
        myRoot = root;
        myResourceName = dest.endsWith(".properties") ? dest : dest + ".properties";
    }

    /**
     * This method does the actual writing of the resource file.
     */
    public void doIt() {
        String packageName = "uk.ac.ebi.intact.model";
        final String superCN = "CvObject";
        String path = myRoot + packageToFile(packageName);
        FilenameFilter filter = new FilenameFilter() {

            private String mySuperName = superCN + ".class";

            public boolean accept(File dir, String name) {
                if (name.startsWith("Cv") && name.endsWith(".class") && !name.equals(mySuperName)) {
                    return true;
                }
                return false;
            }
        };
        String[] files = (new File(path)).list(filter);
        String superCP = packageName + "." + superCN;
        Properties props = new Properties();
        for (int i = 0; i < files.length; i++) {
            String nameOnly = stripExtension(files[i]);
            String classname = packageName + "." + nameOnly;
            Class clazz = null;
            try {
                clazz = Class.forName(classname);
            } catch (ClassNotFoundException cnfe) {
                Assert.fail(cnfe);
            }
            Class superclass = clazz.getSuperclass();
            if (superclass.getName().equals(superCP)) {
                props.put(nameOnly, classname);
            }
        }
        try {
            writeToProperties(props);
        } catch (IOException ex) {
            Assert.fail(ex);
        }
    }

    /**
     * Converts a given package name to the platform dependent file path.
     * @param packName the name of the package.
     * @return the patform dependent file path.
     */
    private static String packageToFile(String packName) {
        char sep = System.getProperty("file.separator").charAt(0);
        return sep + packName.replace(".".charAt(0), sep) + sep;
    }

    /**
     * Strips extension bit from a filename.
     * @param filename the name of the file.
     * @return the filename after removing the extension; if there is no
     * extension then this equals to <tt>filename</tt>.
     */
    private static String stripExtension(String filename) {
        int pos = filename.indexOf('.');
        if (pos != -1) {
            return filename.substring(0, pos);
        }
        return filename;
    }

    /**
     * Writes the given properties contents to a properties file.
     * @param props the properties file to write contents.
     * @exception IOException throws for any I/O errors.
     */
    private void writeToProperties(Properties props) throws IOException {
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(myResourceName));
            props.store(out, "IntactTypes");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                }
            }
        }
    }
}
