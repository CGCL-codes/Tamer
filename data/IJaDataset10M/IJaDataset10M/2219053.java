package com.javaexpert.intellij.plugins.eclipseclasspath.synchronizer.domain;

import static com.intellij.openapi.roots.OrderRootType.*;
import com.intellij.openapi.roots.libraries.Library;
import com.javaexpert.intellij.plugins.eclipseclasspath.eclipse.EclipseClasspathEntry;
import static com.javaexpert.intellij.plugins.eclipseclasspath.eclipse.EclipseClasspathEntry.Kind.LIB;
import com.javaexpert.intellij.plugins.eclipseclasspath.eclipse.VarEclipseClasspathEntry;
import com.javaexpert.intellij.plugins.eclipseclasspath.synchronizer.ApplicationRunningTasksStub;
import net.sf.jdummy.JDummyTestCase;
import org.jmock.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: piotrga
 * Date: 2007-03-18
 * Time: 20:58:45
 */
public class IdeaLibraryTest extends JDummyTestCase {

    private Mock modifiableModel;

    private IdeaLibrary lib;

    private List<EclipseClasspathEntry> list;

    private static final String BASE_DIR = "base/dir";

    @Override
    @BeforeMethod
    protected void setUp() throws Exception {
        super.setUp();
        list = new ArrayList<EclipseClasspathEntry>();
        modifiableModel = mock(Library.ModifiableModel.class);
        Mock nativeLib = mock(Library.class);
        nativeLib.stubs().method("getModifiableModel").will(returnValue(modifiableModel.proxy()));
        lib = new IdeaLibraryImpl(((Library) nativeLib.proxy()), new ApplicationRunningTasksStub());
    }

    @AfterMethod
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void addVar() {
        initStubs();
        list.add(new VarEclipseClasspathEntry("VARIABLE/some/path.jar"));
        modifiableModel.expects(once()).method("addRoot").with(eq("jar://$VARIABLE$/some/path.jar!/"), eq(CLASSES));
        this.lib.repopulateEntries(list, BASE_DIR);
    }

    @Test(dataProvider = "sourcePaths")
    public void addLibWithSources(String testName, String sourcePath, String baseDir, String expected) {
        initStubs();
        EclipseClasspathEntry e = new EclipseClasspathEntry(LIB, "some/path.jar");
        e.setSourcePath(sourcePath);
        list.add(e);
        modifiableModel.stubs().method("addRoot").with(eq("jar://base/dir/some/path.jar!/"), eq(CLASSES));
        modifiableModel.expects(once()).method("addRoot").with(eq(expected), eq(SOURCES));
        lib.repopulateEntries(list, baseDir);
    }

    @Test(dataProvider = "javaDocPaths")
    public void addLibWithJavadoc(String testName, String sourcePath, String baseDir, String expected) {
        initStubs();
        EclipseClasspathEntry e = new EclipseClasspathEntry(LIB, "some/path.jar");
        e.setJavadocPath(sourcePath);
        list.add(e);
        modifiableModel.stubs().method("addRoot").with(eq("jar://base/dir/some/path.jar!/"), eq(CLASSES));
        modifiableModel.expects(once()).method("addRoot").with(eq(expected), eq(JAVADOC));
        lib.repopulateEntries(list, baseDir);
    }

    @Test
    public void clearsLibBeforeAddingNewStuff() {
        modifiableModel.expects(once()).method("getUrls").will(returnValue(new String[] { "1", "2" }));
        modifiableModel.expects(exactly(2)).method("removeRoot").with(ANYTHING, ANYTHING).will(returnValue(true));
        modifiableModel.expects(once()).method("commit");
        lib.repopulateEntries(list, "");
    }

    @Test(dataProvider = "classPaths")
    public void testOneEntry(String testName, String path, String baseDir, String expectedPath) {
        initStubs();
        list.add(new EclipseClasspathEntry(LIB, path));
        modifiableModel.expects(once()).method("addRoot").with(eq(expectedPath), eq(CLASSES));
        this.lib.repopulateEntries(list, baseDir);
    }

    private void initStubs() {
        modifiableModel.stubs().method("getUrls").will(returnValue(new String[0]));
        modifiableModel.stubs().method("commit");
    }

    @DataProvider()
    public Object[][] classPaths() {
        return sum(sourcePaths(), new Object[][] { { "Absolute Jar", "/src/lib.jar", BASE_DIR, "jar:///src/lib.jar!/" }, { "Relative Jar", "src/lib.jar", BASE_DIR, "jar://base/dir/src/lib.jar!/" } });
    }

    private Object[][] sum(Object[][] objects, Object[][] objects1) {
        List<Object[]> list = new ArrayList<Object[]>();
        list.addAll(Arrays.asList(objects));
        list.addAll(Arrays.asList(objects1));
        return (Object[][]) list.toArray(new Object[0][0]);
    }

    @DataProvider()
    public Object[][] javaDocPaths() {
        return sum(sourcePaths(), new Object[][] { { "Http Url", "http://src/dir", BASE_DIR, "http://src/dir" } });
    }

    @DataProvider()
    public Object[][] sourcePaths() {
        return new Object[][] { { "Relative", "src/dir", BASE_DIR, "file://base/dir/src/dir" }, { "Absolute", "/src/dir", BASE_DIR, "file:///src/dir" }, { "File URL", "file://src/dir", BASE_DIR, "file://src/dir" } };
    }
}
