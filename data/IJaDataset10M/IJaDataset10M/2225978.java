package org.xaware.server.engine.instruction.bizcomps.file;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.xaware.server.engine.instruction.bizcomps.file.append.TestFileAppend;
import org.xaware.server.engine.instruction.bizcomps.file.read.TestFileRead;
import org.xaware.server.engine.instruction.bizcomps.file.read.TestFileTrim;
import org.xaware.server.engine.instruction.bizcomps.file.write.TestFileWrite;

/**
 * Test the FileBizCompInst 
 * @author jtarnowski
 */
public class FileTestSuite {

    /**
     * Do it.
     * @return Test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.xaware.server.engine.instruction.bizcomps.file");
        suite.addTestSuite(TestFileTrim.class);
        suite.addTestSuite(TestFileRead.class);
        suite.addTestSuite(TestFileWrite.class);
        suite.addTestSuite(TestFileAppend.class);
        return suite;
    }
}
