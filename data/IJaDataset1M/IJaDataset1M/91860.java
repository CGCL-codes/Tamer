package edu.gatech.cc.jcrasher.writer;

import static edu.gatech.cc.jcrasher.Assertions.notNull;
import static edu.gatech.cc.jcrasher.Constants.NL;
import static edu.gatech.cc.jcrasher.Constants.TAB;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import edu.gatech.cc.jcrasher.plans.stmt.Block;

/**
 * Generates a JUnit test case.
 * 
 * @author csallner@gatech.edu (Christoph Csallner)
 */
public class JUnitTestCaseWriter<T> extends AbstractJUnitTestWriter<T> implements TestCaseWriter {

    protected final boolean doFilter;

    protected final int fileNr;

    protected final Block<?>[] blocks;

    protected Class<? extends Throwable> expectThrown = null;

    protected int expectedThrowingLineNumber = 0;

    /** 
   * Constructor
   * 
   * Uses default values.
	 */
    public JUnitTestCaseWriter(final Class<T> testeeClass, final String comment, boolean doFilter, final Block<?>[] blocks, int fileNr) {
        this(testeeClass, comment, doFilter, blocks, fileNr, null, 0);
    }

    /**
	 * Constructor.
	 * <p>
   * Writes a series of test cases to a new java file together
   * with a text that will be included as a comment.
   * 
	 * @param doFilter wrap test case into a try-catch block that passes any
   * thrown exception to JCrasher's runtime filters.
   * @param expectThrown null indicates don't care.
   * @param expectedThrowingLineNumber smaller than one indicates don't care.
	 */
    public JUnitTestCaseWriter(final Class<T> testeeClass, final String comment, boolean doFilter, final Block<?>[] blocks, int fileNr, Class<? extends Throwable> expectThrown, int expectedThrowingLineNumber) {
        super(notNull(testeeClass), notNull(comment));
        this.doFilter = doFilter;
        this.blocks = notNull(blocks);
        this.fileNr = fileNr;
        this.expectThrown = expectThrown;
        this.expectedThrowingLineNumber = expectedThrowingLineNumber;
    }

    /**
	 * Constructor.
	 * 
	 * Creates a unnumbered test class.
	 */
    public JUnitTestCaseWriter(final Class<T> testeeClass, final String comment, boolean doFilter, final Block<?>[] blocks) {
        this(notNull(testeeClass), notNull(comment), doFilter, notNull(blocks), -1, null, 0);
    }

    /**
   * @return simple name of generated class = <simpleClassName>Test.
   * E.g., TesteeTest1 for (foo.bar.Testee, 1)
   */
    protected String getSimpleTestName() {
        notNull(testeeClass);
        String res = testeeClass.getSimpleName() + "Test";
        if (fileNr > -1) res += fileNr;
        return res;
    }

    /**
   * @return
   * <PRE>
   * public class TesteeTest1 extends ReInitializingTestCase {
   *    protected void setUp() { [..] }
   *    protected void tearDown() { [..] }    
   * </PRE> 
   */
    protected String getHeader() {
        notNull(testeeClass);
        final String simpleTestName = getSimpleTestName();
        String qualSuperClassName = "junit.framework.TestCase";
        if (doFilter) {
            qualSuperClassName = "edu.gatech.cc.junit.FilteringTestCase";
        }
        String reinitCode = "";
        if (doFilter) {
            reinitCode = TAB + TAB + "/* Re-initialize static fields of loaded classes. */" + NL + TAB + TAB + "edu.gatech.cc.junit.reinit.ClassRegistry.resetClasses();" + NL;
        }
        return "public class " + simpleTestName + " extends " + qualSuperClassName + " {" + NL + TAB + NL + getJavaDocComment("Executed before each testXXX().", TAB) + getOverride() + TAB + "protected void setUp() {" + NL + reinitCode + TAB + TAB + "//TODO: my setup code goes here." + NL + TAB + "}" + NL + TAB + NL + getJavaDocComment("Executed after each testXXX().", TAB) + getOverride() + TAB + "protected void tearDown() throws Exception {" + NL + TAB + TAB + "super.tearDown();" + NL + TAB + TAB + "//TODO: my tear down code goes here." + NL + TAB + "}" + NL;
    }

    /**
   * @return
   * <PRE>
   * protected String getNameOfTestedMeth() { [..] }
   * public MyTestCase(String name) { [..] }
   * public static void main(String[] args) { [..] }
   * public static Test suite() { [..] }
   * </PRE> 
   */
    protected String getFooter() {
        notNull(testeeClass);
        notNull(blocks);
        final String qualTesteeName = testeeClass.getName();
        final String simpleTestName = getSimpleTestName();
        final String testedMethName = getTestedMethName();
        final StringBuilder sb = new StringBuilder();
        if (doFilter && (testedMethName != null)) {
            sb.append(getOverride() + TAB + "protected String getNameOfTestedMeth() {" + NL + TAB + TAB + "return \"" + qualTesteeName + "." + testedMethName + "\";" + NL + TAB + "}" + NL + TAB + NL);
        }
        if (doFilter && (expectThrown != null)) {
            sb.append(getOverride() + TAB + "protected Class<? extends Throwable> getExpectedThrowable() {" + NL + TAB + TAB + "return " + expectThrown.getName() + ".class;" + NL + TAB + "}" + NL + TAB + NL);
        }
        if (doFilter && (expectedThrowingLineNumber > 0)) {
            sb.append(getOverride() + TAB + "protected int getExpectedThrowingLineNumber() {" + NL + TAB + TAB + "return " + expectedThrowingLineNumber + ";" + NL + TAB + "}" + NL + TAB + NL);
        }
        sb.append(getJavaDocComment("Constructor", TAB) + TAB + "public " + simpleTestName + "(String pName) {" + NL + TAB + TAB + "super(pName);" + NL + TAB + "}" + NL + TAB + NL + getJavaDocComment("Easy access for aggregating test suite.", TAB) + TAB + "public static junit.framework.Test suite() {" + NL + TAB + TAB + "return new " + "junit.framework.TestSuite(" + simpleTestName + ".class);" + NL + TAB + "}" + NL + TAB + NL + getJavaDocComment("Main", TAB) + TAB + "public static void main(String[] args) {" + NL + TAB + TAB + "junit.textui.TestRunner.run(" + simpleTestName + ".class);" + NL + TAB + "}" + NL);
        return sb.toString();
    }

    /**
   * @return <init> for constructor and simple name for methods.
   */
    protected String getTestedMethName(final Block<?> block) {
        notNull(block);
        if (block.getTestee() instanceof Constructor) {
            return "<init>";
        }
        return block.getTestee().getName();
    }

    /**
   * @return name of meth under test or <init> if common in blocks, null else.
   */
    protected String getTestedMethName() {
        notNull(blocks);
        switch(blocks.length) {
            case 0:
                return null;
            case 1:
                return getTestedMethName(blocks[0]);
            default:
                String testedMethName = getTestedMethName(blocks[0]);
                for (Block<?> block : blocks) {
                    if (!getTestedMethName(block).equals(testedMethName)) {
                        return null;
                    }
                }
                return testedMethName;
        }
    }

    /**
   * @return
   * <pre>
   * public void test123() throws Throwable {
   *   try {
   *     P p = new P();
   *     C.funcUnderTest(p);
   *   }
   *   catch (Exception e) {
   *     dispatchException(e);
   *   }
   * </pre>
   */
    protected String getTestCases() {
        notNull(testeeClass);
        notNull(blocks);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blocks.length; i++) {
            sb.append(NL + getJavaDocComment("JCrasher-generated test case.", TAB) + TAB + "public void test" + i + "() throws Throwable ");
            if (doFilter) {
                sb.append("{" + NL + TAB + TAB + "try");
            }
            sb.append(blocks[i].text() + NL);
            if (doFilter) {
                sb.append(TAB + TAB + "catch (Throwable throwable) {throwIf(throwable);}" + NL + TAB + "}" + NL);
            }
        }
        return sb.toString();
    }

    /**
   * Creates the test case.
   */
    public File write() {
        notNull(testeeClass);
        notNull(blocks);
        final File outFile = CreateFileUtil.createOutFile(testeeClass, getSimpleTestName());
        final FileWriter outWriter = createFileWriter(outFile);
        if (outWriter == null) {
            return null;
        }
        String content = getPackageHeader() + getJavaDocComment(comment, "") + getHeader() + getTestCases() + TAB + NL + TAB + NL + getFooter() + "}";
        boolean success = false;
        success = writeToFileWriter(outWriter, content);
        if (!success) {
            return null;
        }
        success = closeFileWriter(outWriter);
        if (!success) {
            return null;
        }
        return outFile;
    }
}
