package org.jmove.tools;

import org.w3c.tidy.Tidy;
import java.io.*;
import java.util.*;

/**
 * Simple TestCase for running the jtidy html parser and checker against
 * a set of html files. Currently the test must be executed in the build/doc
 * directory.
 *
 * @author Christian Neumann
 */
public class JTidyTestCase extends junit.framework.TestCase {

    public JTidyTestCase(String name) {
        super(name);
    }

    public void testAllHtmlFiles() throws Exception {
        Vector files = new Vector();
        String dir = ".";
        collectAllHtmlFiles(dir, files);
        Iterator iterator = files.iterator();
        while (iterator.hasNext()) {
            assertHtmlValidation((String) iterator.next());
        }
    }

    private void collectAllHtmlFiles(String dir, Vector files) {
        String[] allFilesOfDir = (new File(dir)).list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".html");
            }
        });
        for (int i = 0; i < allFilesOfDir.length; i++) {
            files.add(dir + "/" + allFilesOfDir[i]);
        }
        String[] allDirsOfDir = (new File(dir)).list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return (new File(name).isDirectory());
            }
        });
        for (int i = 0; i < allDirsOfDir.length; i++) {
            collectAllHtmlFiles(dir + "/" + allDirsOfDir[i], files);
        }
    }

    private void assertHtmlValidation(String file) throws Exception {
        System.out.println("Validating file: " + file);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        Tidy tidy = new Tidy();
        tidy.setQuiet(true);
        tidy.setErrout(pw);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        tidy.parse(in, null);
        int errors = tidy.getParseErrors() + tidy.getParseWarnings();
        assertTrue(errors + " HTML validation error(s) in '" + file + "':\n" + sw.getBuffer().toString(), "".equals(sw.getBuffer().toString()));
        in.close();
    }
}
