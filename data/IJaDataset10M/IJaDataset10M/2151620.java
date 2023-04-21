package net.sf.refactorit.test.loader;

import net.sf.refactorit.RitTestCase;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinClass;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.classmodel.CompilationUnit;
import net.sf.refactorit.classmodel.MissingBinClass;
import net.sf.refactorit.classmodel.MissingBinInterface;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.classmodel.expressions.BinConstructorInvocationExpression;
import net.sf.refactorit.classmodel.statements.BinExpressionStatement;
import net.sf.refactorit.common.util.StringUtil;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.options.GlobalOptions;
import net.sf.refactorit.parser.ASTTree;
import net.sf.refactorit.parser.FastJavaLexer;
import net.sf.refactorit.query.AbstractIndexer;
import net.sf.refactorit.query.BinItemVisitor;
import net.sf.refactorit.source.SourceParsingException;
import net.sf.refactorit.test.LocalTempFileCreator;
import net.sf.refactorit.test.RwRefactoringTestUtils;
import net.sf.refactorit.test.TestProject;
import net.sf.refactorit.test.Utils;
import net.sf.refactorit.utils.ClasspathUtil;
import net.sf.refactorit.utils.RefactorItConstants;
import net.sf.refactorit.vfs.Source;
import net.sf.refactorit.vfs.local.LocalClassPath;
import net.sf.refactorit.vfs.local.LocalJavadocPath;
import net.sf.refactorit.vfs.local.LocalSourcePath;
import org.apache.log4j.Category;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests driver for net.sf.refactorit.classmodel.Project.
 */
public class ProjectTest extends RitTestCase {

    /** Logger instance. */
    private static final Category cat = Category.getInstance(ProjectTest.class.getName());

    public ProjectTest(String name) {
        super(name);
    }

    public static Test suite() throws Exception {
        final TestSuite suite = new TestSuite("Source loading");
        final TestSuite subSuite = new TestSuite(ProjectTest.class);
        subSuite.setName("Special source reloading");
        suite.addTest(subSuite);
        return suite;
    }

    public void testAnnotationLoadingFromClassFiles() throws Exception {
        cat.info("Testing annotation loading from class files [REF-1478]");
        String content = "@SuppressWarnings(value={\"unchecked\"})\n" + "public class TestClass {\n" + "}";
        final Project project = Utils.createTestRbProjectFromString(content);
        IDEController.getInstance().setActiveProject(project);
        cat.debug("Loading project. Error is not expected.");
        project.getProjectLoader().build(null, true);
        project.accept(new AbstractIndexer());
        assertFalse((project.getProjectLoader().getErrorCollector()).hasErrors());
        cat.info("SUCCESS");
    }

    /**
   * Tests bug #71: Loading broken source, fixing it, then reloading results in
   * cyclic dependency exception
   */
    public void testBug71() throws Exception {
        cat.info("Testing bug #71");
        final File testDir = LocalTempFileCreator.createTempDirectory("testBug71", "");
        if (!testDir.exists()) {
            testDir.mkdir();
        }
        final File compilationUnit = new File(testDir, "Test.java");
        if (!compilationUnit.exists()) {
            compilationUnit.createNewFile();
        }
        Writer source = new FileWriter(compilationUnit);
        source.write("import abc.def.ghi; public class Test extends String {}");
        source.flush();
        source.close();
        compilationUnit.setLastModified(System.currentTimeMillis() - 2000);
        final Project project = Utils.createNewProjectFrom(new TestProject("", new LocalSourcePath(testDir.getAbsoluteFile().getAbsolutePath()), new LocalClassPath(ClasspathUtil.getDefaultClasspath()), null));
        IDEController.getInstance().setActiveProject(project);
        cat.debug("Loading project for the first time. Error expected.");
        project.getProjectLoader().build();
        assertTrue((project.getProjectLoader().getErrorCollector()).hasErrors());
        source = new FileWriter(compilationUnit);
        source.write("public class Test extends String {}");
        source.flush();
        source.close();
        compilationUnit.setLastModified(System.currentTimeMillis());
        cat.debug("Loading corrected project. No error expected.");
        project.getProjectLoader().build(null, false);
        cat.info("SUCCESS");
    }

    /**
   * Tests reload: Loading compileable source and then reloading same source, should not throw
   * duplicate type definitions
   */
    public void testReload() throws Exception {
        cat.info("Testing reloading");
        final File testDir = LocalTempFileCreator.createTempDirectory("reloadTest", "");
        if (!testDir.exists()) {
            testDir.mkdir();
        }
        final File packageDir = new File(testDir, "test");
        if (!packageDir.exists()) {
            packageDir.mkdir();
        }
        final File compilationUnit = new File(packageDir, "Test.java");
        if (!compilationUnit.exists()) {
            compilationUnit.createNewFile();
        }
        Writer source = new FileWriter(compilationUnit);
        source.write("package " + packageDir.getName() + "; public class Test {}");
        source.flush();
        source.close();
        compilationUnit.setLastModified(System.currentTimeMillis() - 2000);
        Project project = Utils.createNewProjectFrom(new TestProject("", new LocalSourcePath(testDir.getAbsoluteFile().getAbsolutePath()), new LocalClassPath(ClasspathUtil.getDefaultClasspath()), null));
        IDEController.getInstance().setActiveProject(project);
        cat.debug("Loading project for the first time.No error expected");
        try {
            project.getProjectLoader().build();
        } catch (Exception e) {
            fail("This should have been compilable source.");
        }
        cat.debug("Reloading same project. No error expected.");
        project.getProjectLoader().build(null, false);
        cat.info("SUCCESS");
    }

    public void testLastRebuildTime() throws Exception {
        Project p = Utils.createTestRbProjectFromString("public class X {}", "X.java", null);
        Source source = ((CompilationUnit) p.getCompilationUnits().get(0)).getSource();
        source.setLastModified(source.lastModified() + 1000);
        long previousRebuildTime = p.getLastRebuilded();
        Thread.sleep(50);
        p.getProjectLoader().build(null, false);
        assertTrue(previousRebuildTime < p.getLastRebuilded());
    }

    public void testRebuildBinaryClasses() throws Exception {
        cat.info("Testing rebuilding of binary classes");
        final Project project = Utils.createTestRbProject(Utils.getTestProjects().getProject("RebuildBinaryClasses"));
        project.getProjectLoader().build();
        project.discoverAllUsedTypes();
        BinTypeRef beforeRebuild = project.getTypeRefForName("E");
        assertNotNull("found class E before rebuild", beforeRebuild);
        assertTrue("class E is Ok before rebuild", beforeRebuild.getBinCIType() != null && !(beforeRebuild.getBinCIType() instanceof MissingBinClass) && !(beforeRebuild.getBinCIType() instanceof MissingBinInterface));
        project.getProjectLoader().build(null, true);
        project.discoverAllUsedTypes();
        BinTypeRef afterRebuild = project.getTypeRefForName("E");
        assertNotNull("found class E after rebuild", beforeRebuild);
        assertTrue("class E is Ok after rebuild", afterRebuild.getBinCIType() != null && !(afterRebuild.getBinCIType() instanceof MissingBinClass) && !(afterRebuild.getBinCIType() instanceof MissingBinInterface));
        if (RefactorItConstants.runNotImplementedTests) {
            assertTrue("class E wasn't rebuilt", !beforeRebuild.equals(afterRebuild));
        }
        cat.info("SUCCESS");
    }

    public void testRebuildLocalsOfLocals() throws Exception {
        cat.info("Testing rebuilding of locals of locals");
        final Project project = Utils.createTestRbProject(Utils.getTestProjects().getProject("RebuildLocalsOfLocals"));
        project.getProjectLoader().build();
        project.discoverAllUsedTypes();
        BinTypeRef beforeRebuild = project.getTypeRefForName("B");
        assertNotNull("found class B before rebuild", beforeRebuild);
        assertTrue("class B is Ok before rebuild", beforeRebuild.getBinCIType() != null && !(beforeRebuild.getBinCIType() instanceof MissingBinClass) && !(beforeRebuild.getBinCIType() instanceof MissingBinInterface));
        project.getProjectLoader().build(null, true);
        project.discoverAllUsedTypes();
        BinTypeRef afterRebuild = project.getTypeRefForName("B");
        assertNotNull("found class B after rebuild", afterRebuild);
        assertTrue("class B is Ok after rebuild", afterRebuild.getBinCIType() != null && !(afterRebuild.getBinCIType() instanceof MissingBinClass) && !(afterRebuild.getBinCIType() instanceof MissingBinInterface));
        assertTrue("project has errors after rebuild", !(project.getProjectLoader().getErrorCollector()).hasUserFriendlyErrors() && !(project.getProjectLoader().getErrorCollector()).hasUserFriendlyInfos());
        cat.info("SUCCESS");
    }

    public void testClearingOfLocalOfLocalOnIncrementalRebuild() throws Exception {
        cat.info("Testing incremental rebuilding of locals of locals");
        Project project = Utils.createTestRbProject(Utils.getTestProjects().getProject("IncrementalRebuildLocalsOfLocals"));
        project = RwRefactoringTestUtils.createMutableProject(project);
        project.getProjectLoader().build();
        project.discoverAllUsedTypes();
        BinTypeRef beforeRebuild = project.getTypeRefForName("YYY");
        assertNotNull("must find class YYY before rebuild", beforeRebuild);
        assertTrue("class YYY is Ok before rebuild", beforeRebuild.getBinCIType() != null && !(beforeRebuild.getBinCIType() instanceof MissingBinClass) && !(beforeRebuild.getBinCIType() instanceof MissingBinInterface));
        assertTrue("class YYY has subclasses before rebuild", beforeRebuild.getDirectSubclasses().size() > 0);
        CompilationUnit source = beforeRebuild.getBinCIType().getCompilationUnit();
        String content = source.getContent();
        content = StringUtil.replace(content, "class XXX", "//class XXX");
        Writer writer = new BufferedWriter(new OutputStreamWriter(source.getSource().getOutputStream(), GlobalOptions.getEncoding()), content.length() + 8);
        writer.write(content);
        writer.flush();
        writer.close();
        source.getProject().getProjectLoader().forceSourceModified(source.getSource());
        source.getSource().invalidateCaches();
        project.getProjectLoader().build(null, false);
        project.discoverAllUsedTypes();
        assertTrue("project must have no errors after rebuild", !(project.getProjectLoader().getErrorCollector()).hasUserFriendlyErrors() && !(project.getProjectLoader().getErrorCollector()).hasUserFriendlyInfos());
        BinTypeRef afterRebuild = project.getTypeRefForName("YYY");
        assertNotNull("must find class YYY after rebuild", afterRebuild);
        assertTrue("class YYY is Ok after rebuild", afterRebuild.getBinCIType() != null && !(afterRebuild.getBinCIType() instanceof MissingBinClass) && !(afterRebuild.getBinCIType() instanceof MissingBinInterface));
        assertTrue("class YYY has no subclasses after rebuild", afterRebuild.getDirectSubclasses().size() == 0);
        cat.info("SUCCESS");
    }

    public void testBug2021() throws Exception {
        if (!RefactorItConstants.runNotImplementedTests) {
            return;
        }
        Project p = Utils.createTestRbProjectFromString("import javax.swing.JSpinner;\n" + "import java.text.*;\n" + "public class X {\n" + "  JSpinner expiresSpinner = new JSpinner();\n" + "  public X() {\n" + "    expiresSpinner.setEditor(\n" + "       new JSpinner.DateEditor(\n" + "         expiresSpinner,\n" + "         ((SimpleDateFormat) DateFormat.getDateInstance(\n" + "            DateFormat.SHORT)\n" + "         ).toLocalizedPattern()\n" + "       )\n" + "    );\n" + "  }\n" + "  public static void main(String[] args) {new X();}\n" + "}", "X.java", null);
        {
            CompilationUnit f = p.getCompilationUnitForName("X.java");
            ((BinTypeRef) f.getDefinedTypes().get(0)).getBinCIType().accept(new BinItemVisitor());
        }
        assertFalse((p.getProjectLoader().getErrorCollector()).hasUserFriendlyErrors() ? (p.getProjectLoader().getErrorCollector()).getUserFriendlyErrors().next().toString() : null, (p.getProjectLoader().getErrorCollector()).hasUserFriendlyErrors());
    }

    public void testBug1733() throws Exception {
        try {
            Project p = Utils.createTestRbProjectFromString("interface Interface { }\n" + "class Class extends Interface {\n" + "  Class() {\n" + "    super();\n" + "  }\n" + "}");
            CompilationUnit compilationUnit = (CompilationUnit) p.getCompilationUnits().get(0);
            compilationUnit.visit(new BinItemVisitor());
        } catch (RuntimeException re) {
            assertTrue(re.getCause() instanceof SourceParsingException);
            SourceParsingException e = (SourceParsingException) re.getCause();
            assertTrue(e.justInformsThatUserFriendlyErrorsExist());
        }
    }

    public void testBug2057() throws Exception {
        cat.info("Testing bug 2057");
        final Project project = Utils.createTestRbProject(Utils.getTestProjects().getProject("bug #2057"));
        project.getProjectLoader().build();
        project.discoverAllUsedTypes();
        final BinCIType testBefore = project.getTypeRefForName("Test").getBinCIType();
        final BinCIType rebuildableBefore = project.getTypeRefForName("Rebuildable").getBinCIType();
        project.getProjectLoader().forceSourceModified(rebuildableBefore.getCompilationUnit().getSource());
        project.getProjectLoader().build(null, false);
        final BinCIType testAfter = project.getTypeRefForName("Test").getBinCIType();
        final BinCIType rebuildableAfter = project.getTypeRefForName("Rebuildable").getBinCIType();
        if (RefactorItConstants.runNotImplementedTests) {
            assertSame("Test type shouldn't be rebuilded!", testBefore, testAfter);
        }
        assertNotSame("Rebuildable type must have been rebuilded!", rebuildableBefore, rebuildableAfter);
        assertNotNull("Super constructor has an owner after rebuild", ((BinConstructorInvocationExpression) ((BinExpressionStatement) ((BinClass) testAfter).getDeclaredConstructors()[0].getBody().getStatements()[0]).getExpression()).getConstructor().getOwner());
        cat.info("SUCCESS");
    }

    public void testBugRIM708() throws Exception {
        int oldJvmMode = Project.getDefaultOptions().getJvmMode();
        Project.getDefaultOptions().setJvmMode(FastJavaLexer.JVM_50);
        try {
            Project p = Utils.createTestRbProjectFromString("package x;\n" + "public @interface Wuuu {\n" + "class A<T> extends B<p.H> implements C<T1>, p.D<T2>, p.E<p.F<T3>>, K, L, M, a.b.c.N, O {\n" + "  public enum AAA {\n" + "  };\n" + "}}");
            CompilationUnit compilationUnit = (CompilationUnit) p.getCompilationUnits().get(0);
            ASTTree tree = compilationUnit.getSource().getASTTree();
            assertEquals("Types ok: " + tree.getTypeNames(), new HashSet(Arrays.asList(new String[] { "A", "AAA", "Wuuu" })), tree.getTypeNames());
            assertEquals("Type full names ok: " + tree.getTypeFullNames(), new HashSet(Arrays.asList(new String[] { "Wuuu", "Wuuu$A", "Wuuu$A$AAA" })), tree.getTypeFullNames());
            assertEquals("Super types ok: " + tree.getSuperTypeNames(), new HashSet(Arrays.asList(new String[] { "B", "C", "D", "E", "K", "L", "M", "N", "O" })), tree.getSuperTypeNames());
        } finally {
            Project.getDefaultOptions().setJvmMode(oldJvmMode);
        }
    }

    public void testGetArrayInterfaces_normal() {
        Project p = new Project("", new LocalSourcePath("."), new LocalClassPath(ClasspathUtil.getDefaultClasspath()), new LocalJavadocPath("."));
        BinTypeRef[] result = p.getArrayInterfaces();
        assertEquals(2, result.length);
        assertFalse((p.getProjectLoader().getErrorCollector()).hasErrors());
    }

    public void testGetArrayInterfaces_noInterfaces() {
        Project p = new Project("", new LocalSourcePath("."), new LocalClassPath(ClasspathUtil.getDefaultClasspath()), new LocalJavadocPath("."));
        p.getProjectLoader().getClassLoader().putToCacheAsNotFound("java.io.Serializable");
        p.getProjectLoader().getClassLoader().putToCacheAsNotFound("java.lang.Cloneable");
        BinTypeRef[] result = p.getArrayInterfaces();
        assertEquals(0, result.length);
        assertFalse((p.getProjectLoader().getErrorCollector()).hasErrors());
    }

    public static void main(String args[]) throws Exception {
        ProjectTest test = new ProjectTest("");
        test.testLastRebuildTime();
    }
}
