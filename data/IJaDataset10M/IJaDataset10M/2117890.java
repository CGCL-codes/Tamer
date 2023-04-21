package org.pluginbuilder.autotestsuite.internal;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.pluginbuilder.autotestsuite.Activator;

public class AutoTestSuite extends TestSuite {

    private static final String AUTOTEST_PLUGIN = "autotest.plugin";

    private static final String AUTOTEST_EXCLUSIONS = "autotest.exclusions";

    private String autotestPlugin;

    private String autotestExclusions;

    private ExclusionFilter exclusionFilter;

    private Class testCaseClass;

    public AutoTestSuite(String autotestPlugin, String autotestExclusions) {
        this.autotestPlugin = autotestPlugin;
        this.autotestExclusions = autotestExclusions;
        createTests();
    }

    public AutoTestSuite() {
        createTests();
    }

    public static Test suite() {
        return new AutoTestSuite();
    }

    private void createTests() {
        setName(getAutotestPlugin() + " auto test suite");
        String testCaseClassName = "junit.framework.TestCase";
        try {
            testCaseClass = getBundle().loadClass(testCaseClassName);
        } catch (ClassNotFoundException e) {
            Activator.traceMessage("Could not find class " + testCaseClassName + " in plug-in " + getAutotestPlugin());
        }
        findAllTestsInJarFiles();
        findAllTestsInBinDirectory();
    }

    private void findAllTestsInBinDirectory() {
        Activator.traceMessage("Searching bin directory for tests.");
        Enumeration enumeration;
        enumeration = getBundle().findEntries("/bin", "*.class", true);
        if (enumeration == null) {
            Activator.traceMessage("The plug-in " + getAutotestPlugin() + " does not contain any class files which could be tests.");
        } else {
            while (enumeration.hasMoreElements()) {
                URL bundleUrl = (URL) enumeration.nextElement();
                String classFileName = bundleUrl.getFile();
                if (classFileName.startsWith("/bin/")) {
                    classFileName = classFileName.substring(5);
                }
                addClass(classFileName);
            }
        }
    }

    private void findAllTestsInJarFiles() {
        Enumeration enumeration = getBundle().findEntries("/", "*.jar", true);
        if (enumeration == null) {
            Activator.traceMessage("The plug-in " + getAutotestPlugin() + " does not contain any jar files to search for tests.");
        } else {
            while (enumeration.hasMoreElements()) {
                URL bundleUrl = (URL) enumeration.nextElement();
                findTestCasesInJarFile(bundleUrl);
            }
        }
    }

    private void findTestCasesInJarFile(URL bundleUrl) {
        JarFile jarFile = getJarFile(bundleUrl);
        if (jarFile != null) {
            Enumeration jarFileEntries = jarFile.entries();
            while (jarFileEntries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) jarFileEntries.nextElement();
                String name = jarEntry.getName();
                Activator.traceMessage("Jar-Entry: " + name);
                if (!name.endsWith(".class")) {
                    continue;
                }
                addClass(name);
            }
        }
    }

    private void addClass(String classFileName) {
        String className = classFileName.replaceAll(".class", "");
        className = className.replaceAll("/", ".");
        try {
            Class clazz = getBundle().loadClass(className);
            if (Modifier.isAbstract(clazz.getModifiers())) {
                Activator.traceMessage("TestCase is abstract: " + clazz.getName());
            } else {
                if (testCaseClass.isAssignableFrom(clazz)) {
                    if (!getExclusionFilter().matches(className)) {
                        addTest(new TestSuite(clazz));
                        Activator.traceMessage("Added TestCase: " + clazz.getName());
                    } else {
                        Activator.traceMessage("Excluded TestCase: " + clazz.getName());
                    }
                }
            }
        } catch (ClassNotFoundException cnfe) {
            Activator.traceMessage("Bundle could not load class: " + className);
        } catch (NoClassDefFoundError ncdfe) {
            Activator.traceMessage("Bundle could not load class: " + className);
        }
    }

    private JarFile getJarFile(URL bundleUrl) {
        Activator.traceMessage("Searching " + bundleUrl + " for tests.");
        JarFile jarFile = null;
        try {
            URL url = FileLocator.resolve(bundleUrl);
            jarFile = new JarFile(url.getPath());
        } catch (IOException e) {
            try {
                Activator.traceMessage("Could not open " + bundleUrl + " directly. It seems to be in a jar.");
                File file = File.createTempFile("bundleJar", ".jar");
                file.deleteOnExit();
                InputStream inputStream = FileLocator.openStream(getBundle(), new Path(bundleUrl.getPath()), false);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while (inputStream.available() > 0) {
                    int read = inputStream.read(buffer);
                    outputStream.write(buffer, 0, read);
                }
                outputStream.close();
                inputStream.close();
                jarFile = new JarFile(file);
            } catch (IOException e1) {
                Activator.traceMessage("Could not get content of  " + bundleUrl + ". " + e1.getMessage());
            }
        }
        return jarFile;
    }

    private Bundle getBundle() {
        Bundle bundle;
        if (getAutotestPlugin() != null) {
            bundle = Platform.getBundle(getAutotestPlugin());
            if (bundle == null) {
                throw new RuntimeException("Could not find bundle: " + getAutotestPlugin());
            }
        } else {
            throw new RuntimeException("The system property " + AUTOTEST_PLUGIN + " must be set!");
        }
        return bundle;
    }

    private String getAutotestPlugin() {
        if (autotestPlugin == null) {
            autotestPlugin = System.getProperty(AUTOTEST_PLUGIN);
        }
        return autotestPlugin;
    }

    private ExclusionFilter getExclusionFilter() {
        if (exclusionFilter == null) {
            try {
                exclusionFilter = new ExclusionFilter(new ByteArrayInputStream(this.getAutotestExclusions().getBytes()));
            } catch (Exception e) {
                throw new RuntimeException("Could not create ExclusionFilter" + e.getMessage());
            }
        }
        return exclusionFilter;
    }

    private String getAutotestExclusions() {
        if (autotestExclusions == null) {
            autotestExclusions = System.getProperty(AUTOTEST_EXCLUSIONS);
            if (autotestExclusions == null) {
                autotestExclusions = "";
            }
        }
        return autotestExclusions;
    }
}
