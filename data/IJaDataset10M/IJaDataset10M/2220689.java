package net.sf.grotag.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Various tools to simplify testing.
 * 
 * @author Thomas Aglassinger
 */
public class TestTools {

    private static TestTools instance;

    public enum Folder {

        ACTUAL, EXPECTED, GUIDES, INPUT
    }

    public static final synchronized TestTools getInstance() {
        if (instance == null) {
            instance = new TestTools();
        }
        return instance;
    }

    private TestTools() {
        Tools.getInstance();
    }

    public File getTestActualFile(String fileName) {
        return getTestFile(Folder.ACTUAL, fileName);
    }

    public File getTestExpectedFile(String fileName) {
        return getTestFile(Folder.EXPECTED, fileName);
    }

    public File getTestInputFile(String fileName) {
        return getTestFile(Folder.INPUT, fileName);
    }

    public File getTestGuideFile(String fileName) {
        return getTestFile(Folder.GUIDES, fileName);
    }

    public File getTestFile(Folder baseFolder, String fileName) {
        File folder = new File("tests", baseFolder.toString().toLowerCase());
        File result = new File(folder, fileName);
        if ((baseFolder == Folder.ACTUAL) && !folder.exists()) {
            if (!folder.mkdirs() && !folder.exists()) {
                throw new IllegalStateException("cannot create folder: " + folder.getAbsolutePath());
            }
        }
        return result;
    }

    public File getTestFile(String fileName) {
        return getTestFile(Folder.INPUT, fileName);
    }

    public void assertFilesAreEqual(String fileName) throws IOException {
        File expectedFile = getTestFile(Folder.EXPECTED, fileName);
        File actualFile = getTestFile(Folder.ACTUAL, fileName);
        assertFilesAreEqual(expectedFile, actualFile);
    }

    @SuppressWarnings("unchecked")
    public String getTestName(Class classToTest, String methodToTest) {
        assert classToTest != null;
        assert methodToTest != null;
        String result = classToTest.getName();
        int dotIndex = result.lastIndexOf('.');
        assert dotIndex >= 0;
        result = result.substring(dotIndex + 1) + "." + methodToTest;
        return result;
    }

    public void assertFilesAreEqual(File expected, File actual) throws IOException {
        FileInputStream expectedFileInStream = new FileInputStream(expected);
        try {
            InputStreamReader expectedInStreamReader = new InputStreamReader(expectedFileInStream);
            try {
                BufferedReader expectedReader = new BufferedReader(expectedInStreamReader);
                try {
                    FileInputStream actualFileInStream = new FileInputStream(actual);
                    try {
                        InputStreamReader actualInStreamReader = new InputStreamReader(actualFileInStream);
                        try {
                            BufferedReader actualReader = new BufferedReader(actualInStreamReader);
                            try {
                                int lineNumber = 0;
                                String expectedLine;
                                String actualLine;
                                do {
                                    lineNumber += 1;
                                    expectedLine = expectedReader.readLine();
                                    actualLine = actualReader.readLine();
                                } while ((expectedLine != null) && (actualLine != null));
                            } finally {
                                actualReader.close();
                            }
                        } finally {
                            actualInStreamReader.close();
                        }
                    } finally {
                        actualFileInStream.close();
                    }
                } finally {
                    expectedReader.close();
                }
            } finally {
                expectedInStreamReader.close();
            }
        } finally {
            expectedFileInStream.close();
        }
    }
}
