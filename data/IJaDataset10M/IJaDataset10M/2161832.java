package com.scissor.junit;

import junit.framework.Test;
import junit.framework.TestSuite;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

/**
 * Rummages through the filesystem to build JUnit 3.x test suites. Can filter by
 * package name or annotation.
 */
public class TestSuiteBuilder {

    private static final FileFilter IS_VALID_DIRECTORY = new DirectoryFilter();

    private static final FileFilter IS_TEST = new JavaTestFileFilter();

    private final File directory;

    private CompositeNameFilter packageFilters = new CompositeNameFilter();

    private CompositeClassFilter classFilters = new CompositeClassFilter();

    private static final NewestFileComparator COMPARATOR = new NewestFileComparator();

    /**
     * Creates a TestSuiteBuilder focused on the named source directory.
     */
    public TestSuiteBuilder(String sourceDirectoryName) {
        this.directory = new File(sourceDirectoryName);
        if (!this.directory.exists()) {
            try {
                throw new IllegalArgumentException("Couldn't find path " + sourceDirectoryName + " starting from directory" + new File(".").getCanonicalPath());
            } catch (IOException e) {
                throw new IllegalStateException("Can't find home path!");
            }
        }
        addFilter(new IsAbstractFilter());
        addFilter(new ExcludeAnnotationFilter(Unfinished.class));
        addFilter(new ExcludeAnnotationFilter(ExcludeFromTests.class));
    }

    private void addFilter(ClassFilter filter) {
        classFilters.add(filter);
    }

    /**
     * Creates a test suite for all named packages and contained packages.
     */
    public TestSuite build(String... packageNames) throws ClassNotFoundException {
        TestSuite suite = new TestSuite(nameFor(packageNames));
        for (String packageName : packageNames) {
            File myDir = directoryForPackage(packageName);
            addChildDirs(suite, myDir, packageName);
            addTestsInDir(suite, myDir, packageName);
        }
        return suite;
    }

    private String nameFor(String... packageNames) {
        if (packageNames == null || packageNames.length == 0) return "unnamed";
        return packageNames[0];
    }

    private void addChildDirs(TestSuite suite, File myDir, String packageName) throws ClassNotFoundException {
        File[] directories = myDir.listFiles(IS_VALID_DIRECTORY);
        if (directories == null) return;
        Arrays.sort(directories, COMPARATOR);
        for (int i = 0; i < directories.length; i++) {
            File childDir = directories[i];
            String childPackage = packageName + "." + childDir.getName();
            if (packageFilters.examine(childPackage) != FilterResult.REJECT) {
                suite.addTest(build(childPackage));
            }
        }
    }

    private void addTestsInDir(TestSuite suite, File myDir, String packageName) throws ClassNotFoundException {
        File[] tests = myDir.listFiles(IS_TEST);
        Arrays.sort(tests, COMPARATOR);
        for (int i = 0; i < tests.length; i++) {
            File test = tests[i];
            Class aClass = loadTest(packageName, test);
            if (classFilters.examine(aClass) != FilterResult.REJECT) {
                suite.addTest(testForClass(aClass));
            }
        }
    }

    protected Test testForClass(Class aClass) {
        return new TestSuite(aClass);
    }

    private Class loadTest(String packageName, File test) throws ClassNotFoundException {
        String testName = test.getName().substring(0, test.getName().indexOf('.'));
        return (Class) ClassLoader.getSystemClassLoader().loadClass(packageName + "." + testName);
    }

    private File directoryForPackage(String packageName) {
        StringTokenizer st = new StringTokenizer(packageName, ".");
        File current = directory;
        while (st.hasMoreElements()) {
            String packageComponent = (String) st.nextElement();
            current = new File(current, packageComponent);
        }
        return current;
    }

    /**
     * Skip the given package and any subpackages.
     */
    public void skipPackage(String packageName) {
        packageFilters.add(new RejectPackagedNameFilter(packageName));
    }

    /**
     * Skip all classes marked with this Annotation. The annotation must be marked with RetentionPolicy.RUNTIME.
     */
    public void skipAnnotation(Class<? extends Annotation> aClass) {
        validateAnnotation(aClass);
        addFilter(new ExcludeAnnotationFilter(aClass));
    }

    /**
     * Include only classes marked with this Annotation. The annotation must be marked with RetentionPolicy.RUNTIME.
     */
    public void skipEverythingLackingAnnotation(Class<? extends Annotation> aClass) {
        validateAnnotation(aClass);
        addFilter(new RequireAnnotationFilter(aClass));
    }

    private void validateAnnotation(Class<? extends Annotation> aClass) {
        Retention retention = aClass.getAnnotation(Retention.class);
        if (retention == null || retention.value() != RetentionPolicy.RUNTIME) {
            throw new IllegalArgumentException("Please mark your annotation with @Retention(RetentionPolicy.RUNTIME)");
        }
    }

    private static class DirectoryFilter implements FileFilter {

        public boolean accept(File file) {
            if (!file.isDirectory()) return false;
            if (file.getName().equals("CVS")) return false;
            if (file.getName().equals(".svn")) return false;
            return true;
        }
    }

    private static class JavaTestFileFilter implements FileFilter {

        public boolean accept(File file) {
            if (file.getName().endsWith("Test.java")) return true;
            return false;
        }
    }

    private static class NewestFileComparator implements Comparator<File> {

        public int compare(File f1, File f2) {
            if (f1.lastModified() < f2.lastModified()) {
                return 1;
            } else if (f1.lastModified() > f2.lastModified()) {
                return -1;
            } else {
                return f1.compareTo(f2);
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Unfinished {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcludeFromTests {
    }
}
