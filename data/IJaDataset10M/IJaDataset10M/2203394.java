package au.edu.usq.utfx.framework;

import java.io.File;
import java.io.FilenameFilter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

/**
 * Suite of tests for XSLT stylesheets. This class represents a test suite of
 * UTF-X test definition files in a particular directory.
 * 
 * <p>
 * Copyright &copy; 2004 - <a href="http://www.usq.edu.au"> University of
 * Southern Queensland. </a>
 * </p>
 * 
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the <a href="http://www.gnu.org/licenses/gpl.txt">GNU General
 * Public License v2 </a> as published by the Free Software Foundation.
 * </p>
 * 
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * </p>
 * 
 * <code>
 * $Source: /cvsroot/utf-x/utf-x/src/java/au/edu/usq/utfx/framework/XsltTestDirectorySuite.java,v $
 * </code>
 * 
 * @author Jacek Radajewski
 * @author Oliver Lucido
 * @author Sally MacFarlane
 * @version $Revision: 1.9 $ $Date: 2005/02/28 02:54:34 $ $Name:  $
 */
public class XsltTestDirectorySuite extends TestSuite {

    /** logger */
    private static Logger log = Logger.getLogger("utfx.framework");

    /**
     * Filter used to filter UTF-X test definition files.
     * 
     * @see ConfigurationManager
     */
    private static FilenameFilter testFileFilter;

    /** directory which this test suite represents */
    private File dir;

    /** configuration manager */
    private ConfigurationManager cm;

    /**
     * Constructs an XSLT test directory suite with a directory name.
     * 
     * @param dirName name of the directory. doubles as the name of this test
     *        suite.
     */
    public XsltTestDirectorySuite(String dirName) {
        super(dirName);
        cm = ConfigurationManager.getInstance();
        testFileFilter = cm.getTestFileFilter();
        this.dir = new File(dirName);
        setName(dir.getName());
    }

    /**
     * Assemble and return test suite.
     * 
     * @param dirName name of the directory.
     */
    public static Test suite(String dirName) {
        XsltTestDirectorySuite suite = new XsltTestDirectorySuite(dirName);
        TestSuite childSuite;
        File[] testFiles = suite.dir.listFiles(testFileFilter);
        File[] testDirectories = suite.dir.listFiles();
        for (File testFile : testFiles) {
            log.debug("adding test file suite " + testFile.getName());
            suite.addTest(XsltTestFileSuite.suite(testFile.getAbsolutePath()));
        }
        for (File subDir : testDirectories) {
            if (subDir.isDirectory()) {
                childSuite = (TestSuite) XsltTestDirectorySuite.suite(subDir.getAbsolutePath());
                if (childSuite.testCount() > 0) {
                    log.debug("adding test directory suite " + subDir.getName());
                    suite.addTest(childSuite);
                }
            }
        }
        return suite;
    }
}
