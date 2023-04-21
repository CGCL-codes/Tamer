package org.codecover;

import java.io.File;

/**
 * Contains methods used by JUnit-Tests.
 * 
 * @author Christoph Müller
 * 
 * @version 1.0 ($Id: UtilsForTestingModel.java 1 2007-12-12 17:37:26Z t-scheller $)
 */
public class UtilsForTestingModel {

    /** point to the instrumentation folde in code */
    public static String BASEDIR;

    static {
        String baseDir = System.getProperty("basedir");
        if (baseDir == null) {
            BASEDIR = (new File("")).getAbsolutePath() + File.separatorChar;
        } else {
            BASEDIR = (new File(baseDir)).getAbsolutePath() + File.separatorChar;
        }
    }
}
