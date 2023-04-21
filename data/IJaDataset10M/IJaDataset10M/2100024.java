package pluginTests;

import org.jmlspecs.eclipse.customparser.JavaParser;
import org.jmlspecs.eclipse.jmlchecker.JmlNameTypeResolver;
import org.jmlspecs.eclipse.jmlchecker.Options;
import org.jmlspecs.eclipse.jmlchecker.ProjectInfo;
import org.jmlspecs.eclipse.jmldom.*;
import utils.JUnitProblemRequestor;
import junit.framework.TestCase;

/**
 * This class implements a number of JUnit tests that exercise Java
 * parsing and type checking.
 */
public class JavaTypeTests extends TestCase {

    /**
   * The parser instance created in setUp and used by tests
   */
    JavaParser p;

    /** The ProjectInfo structure for these tests.  This is initialized
   * here so that the initialization is done once for all tests.  This
   * has performance advantages but might possibly introduce interactions
   * among the tests (FIXME - does it?) */
    protected static final ProjectInfo pi = new ProjectInfo(new Options(), JUnitProblemRequestor.req);

    public void setUp() throws Exception {
        super.setUp();
        pi.setJavaProject(null);
        p = new JavaParser(utils.Env.jlsLevel, JUnitProblemRequestor.req);
        JUnitProblemRequestor.req.reset();
        JUnitProblemRequestor.req.saveOutput(true);
    }

    public void tearDown() throws Exception {
        p = null;
        super.tearDown();
    }

    /** The method that does the actual work of parsing and type checking
   * and comparing the result to what is expected.  All of these tests
   * are expected to have no errors or warnings and no output.
   * @param code A snippet of source code (a whole compilation unit)
   */
    public void check(String code) {
        p.setSource(code);
        CompilationUnit cu = p.parseCompilationUnit();
        JmlNameTypeResolver.resolve(cu, false, pi);
        assertEquals(0, JUnitProblemRequestor.req.numberOfErrors());
        assertEquals(0, JUnitProblemRequestor.req.numberOfWarnings());
        String s = JUnitProblemRequestor.req.getOutput();
        if (s != null && s.length() > 0) System.out.println(s);
    }

    public void test1() {
        check("class T {}");
    }

    public void test2() {
        check("class T { int i; void m() { int k; i = 1; k = 2; }}");
    }

    public void testWhileStatement() {
        check("class T { boolean b; void m() { while (b) { b = 2 > 1; }; }}");
    }

    public void testForStatement() {
        check("class T { void m() { for (int i=0, j=i; i<5; ++i, ++j) { j=i; }}}");
        check("class T { void m() { for (int i=0, j=i;; ) { j=i; }}}");
        check("class T { void m() { int i,j; for (; ; ) { j=i; }}}");
        check("class T { void m() { int i,j; for (i=0, j=i; i<5; ++i, ++j) { j=i; }}}");
    }

    public void testReturnStatement() {
        check("class T { void m() { return; }}");
        check("class T { int m() { return 0; }}");
    }

    public void testSynchronizedStatement() {
        check("class T { int[] i; void m() { synchronized(i) {}}}");
        check("class T { T i; void m() { synchronized(i) {}}}");
        check("class T { T i; void m() { synchronized(this) {}}}");
    }

    public void testMutualReference() {
        check("class T { T i; U j; } class U { T ii; U jj; }");
    }
}
