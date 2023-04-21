package org.crap4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.crap4j.CrapProject;
import org.crap4j.Main;
import org.crap4j.Options;
import junit.framework.TestCase;

public class EndToEndTest extends TestCase {

    public void testAll() throws Exception {
        String projectDir = "/Users/bobevans/Documents/projects/MDTWorkspace/crap4j";
        List<String> classpath = new ArrayList<String>();
        List<String> classDirs = new ArrayList<String>();
        classDirs.add("bin");
        List<String> testDirs = new ArrayList<String>();
        testDirs.add("test_bin");
        testDirs.add("agitar/test_bin");
        List<String> sourceDirs = new ArrayList<String>();
        sourceDirs.add("src");
        CrapProject p = new CrapProject(projectDir, classpath, testDirs, classDirs, sourceDirs, null);
        try {
            Main.createMain().run(p, false, false, false, "http://localhost:3000/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testMain() throws Exception {
        String projectDir = "/Users/bobevans/Documents/projects/MDTWorkspace/crap4j";
        String classDirs = "bin";
        String testDirs = "test_bin" + File.pathSeparator + "agitar/test_bin";
        String sourceDirs = "src";
        String[] args = { "-p", projectDir, "-c", classDirs, "-t", testDirs, "-s", sourceDirs };
        Options options = Main.parseArgs(args);
        assertEquals(projectDir, options.getProjectDir());
        assertEquals(0, options.getLibClasspaths().size());
        assertEquals(1, options.getClassDirs().size());
        assertEquals("bin", options.getClassDirs().get(0));
        assertEquals(2, options.getTestClassDirs().size());
        assertEquals("test_bin", options.getTestClassDirs().get(0));
        assertEquals("agitar/test_bin", options.getTestClassDirs().get(1));
        assertEquals(1, options.getSourceDirs().size());
        assertEquals("src", options.getSourceDirs().get(0));
    }

    public void testParseBadArgs() throws Exception {
        Options options = Main.parseArgs(new String[0]);
        assertFalse(options.valid());
    }
}
