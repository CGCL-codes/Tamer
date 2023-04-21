package com.goodcodeisbeautiful.archtea.io.lfs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author hata
 *
 */
public class PackageTestCase extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite ts = new TestSuite(PackageTestCase.class.getPackage().getName() + " TestCase");
        ts.addTest(FileSystemEntryFactoryTestCase.suite());
        ts.addTest(FileSystemFolderEntryTestCase.suite());
        ts.addTest(FileSystemLeafEntryTestCase.suite());
        ts.addTest(ZipArchiveEntryTestCase.suite());
        ts.addTest(ZipLeafEntryTestCase.suite());
        ts.addTest(ZipPieceInputStreamTestCase.suite());
        ts.addTest(ZipFileEntryFactoryTestCase.suite());
        ts.addTest(ZipFolderEntryTestCase.suite());
        return ts;
    }

    /**
     * 
     */
    public PackageTestCase() {
        super();
    }

    /**
     * @param name
     */
    public PackageTestCase(String name) {
        super(name);
    }
}
