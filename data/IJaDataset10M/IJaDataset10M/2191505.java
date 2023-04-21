package api.client.extended;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Helper Class, which is used to generate a classpath.
 * 
 * @author Christian Doerner
 *
 */
public class ClassPathGenerator {

    /**
	 * This class generates a classpath from the given path.
	 * 
	 * @param absolutJarFilesPath The absolut path to the jars
	 * @return The classpath
	 * @throws FileNotFoundException If the path was not valid or empty
	 */
    public static String ConstructClassPathFromJarDirectory(String absolutJarFilesPath) throws FileNotFoundException {
        File dir = new File(absolutJarFilesPath);
        String filename = ".";
        int jarFileCounter = 0;
        String[] children = dir.list();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                if (children[i].endsWith(".jar")) {
                    jarFileCounter++;
                    filename = filename + File.pathSeparator + absolutJarFilesPath + File.separator + children[i];
                }
            }
        } else {
            throw new FileNotFoundException("Directory " + absolutJarFilesPath + " contains no JAR files!");
        }
        if (jarFileCounter > 0) {
            return filename;
        } else {
            throw new FileNotFoundException("Directory " + absolutJarFilesPath + " contains no JAR files!");
        }
    }
}
