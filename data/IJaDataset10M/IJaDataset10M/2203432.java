package krico.javalitest;

import java.io.*;
import java.util.Stack;
import junit.framework.*;
import java.lang.reflect.Method;

/**
 * Create a runtime TestSuite composed of all other classes that contain the name Test on their name and do not contain the name AllTests.
 * All this is done by the {@link #suite suite()} method.  <br>
 * <b>Note:</b> The class must extend TestCase and must have a non null suite method.
 */
public class AllTests extends TestCase {

    public AllTests(String t) {
        super(t);
    }

    public static Test suite() {
        File classesDir = new File(System.getProperty("javali.tests.classesdir"));
        if (!classesDir.exists()) {
            System.err.println("File: " + classesDir.getAbsolutePath() + " does not exist");
            return null;
        }
        TestSuite ret = new TestSuite();
        String classes[] = getClasses(classesDir);
        for (int i = 0; i < classes.length; i++) {
            try {
                Class cl = Class.forName(classes[i]);
                if (TestCase.class.isAssignableFrom(cl)) {
                    Method m = cl.getDeclaredMethod("suite", new Class[0]);
                    TestSuite ts = (TestSuite) m.invoke(cl, new Object[0]);
                    ret.addTest(ts);
                }
            } catch (Exception e) {
                System.err.println("Ex: " + e + " class: " + classes[i]);
            }
        }
        return ret;
    }

    public String toString() {
        return "Javali-AllTests-" + getName();
    }

    private static String[] getClasses(File dir) {
        FileFilter dirOnly = new FileFilter() {

            public boolean accept(File f) {
                return f.isDirectory();
            }
        };
        FileFilter testOnly = new FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) return false;
                if (f.getName().indexOf("Test") != -1) {
                    return f.getName().indexOf("AllTests") == -1 && f.getName().indexOf("$") == -1;
                } else return false;
            }
        };
        File[] dirs = dir.listFiles(dirOnly);
        Stack dirStack = new Stack();
        for (int i = 0; i < dirs.length; i++) dirStack.push(dirs[i]);
        Stack myFiles = new Stack();
        while (!dirStack.isEmpty()) {
            File actual = (File) dirStack.pop();
            File mf[] = actual.listFiles(testOnly);
            for (int i = 0; i < mf.length; i++) myFiles.push(mf[i]);
            dirs = actual.listFiles(dirOnly);
            for (int i = 0; i < dirs.length; i++) dirStack.push(dirs[i]);
        }
        String ret[] = new String[myFiles.size()];
        int j = 0;
        while (!myFiles.isEmpty()) {
            String fname = ((File) myFiles.pop()).getAbsolutePath();
            int idx = fname.indexOf(dir.getAbsolutePath());
            ret[j++] = fname.substring(idx + dir.getAbsolutePath().length() + 1, fname.length() - 6).replace('/', '.');
        }
        return ret;
    }
}
