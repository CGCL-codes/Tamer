package org.cantaloop.jiomask.testing;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import junit.framework.TestCase;
import org.cantaloop.jiomask.CodeGenerator;
import org.cantaloop.jiomask.Main;

public class MainUTest extends TestCase {

    private CodeGeneratorMock m_mock;

    private Main m_main;

    public MainUTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        m_mock = new CodeGeneratorMock();
        m_main = new Main();
    }

    public void testAllButVerbose() {
        String commandline = "-p myPackage -x myXForms -j myJarFile -s myCode -c myClasses --keep-classes --keep-code --no-jar --no-compilation";
        StringTokenizer st = new StringTokenizer(commandline);
        String[] args = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            args[i++] = st.nextToken();
        }
        try {
            m_main.run(args, m_mock);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        m_mock.verify();
        assertEquals("myPackage", m_mock.getPackageName());
        assertEquals(new File("myXForms"), m_mock.getXformsFile());
        assertEquals(new File("myJarFile"), m_mock.getJarFile());
        assertEquals(new File("myCode"), m_mock.getCodeDir());
        assertEquals(new File("myClasses"), m_mock.getClassDir());
        assertTrue(m_mock.isKeepClasses());
        assertTrue(m_mock.isKeepCode());
        assertTrue(!m_mock.isBuildJarFile());
        assertTrue(!m_mock.isCompileCode());
    }

    public void testVerbose0() {
        String[] args = new String[] { "-x", "dummy", "-v", "0" };
        try {
            m_main.run(args, m_mock);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        assertEquals(CodeGenerator.SILENT, m_mock.getVerboseLevel());
    }

    public void testVerbose1() {
        String[] args = new String[] { "-x", "dummy", "-v", "1" };
        try {
            m_main.run(args, m_mock);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        assertEquals(CodeGenerator.PROGRESS_STEPS, m_mock.getVerboseLevel());
    }

    public void testVerbose2() {
        String[] args = new String[] { "-x", "dummy", "-v", "2" };
        try {
            m_main.run(args, m_mock);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        assertEquals(CodeGenerator.DETAILED_PROGRESS_STEPS, m_mock.getVerboseLevel());
    }

    public void testVerbose3() {
        String[] args = new String[] { "-x", "dummy", "-v", "3" };
        try {
            m_main.run(args, m_mock);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        assertEquals(CodeGenerator.PROGRESS_PLUS_INFO_LOGMSGS, m_mock.getVerboseLevel());
    }

    public void testMinimal() {
        String[] args = new String[] { "-x", "dummy" };
        try {
            m_main.run(args, m_mock);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        m_mock.verify();
    }

    public void testNoArguments() {
        try {
            m_main.run(new String[] {}, m_mock);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        if (m_mock.runWasCalled()) {
            fail("run() should not be called if xforms file is not given.");
        }
    }

    public void testKeepCodeAndKeepClasses() throws IOException {
        assertKeepCodeAndKeepClasses(new String[] { "-x", "dummy" }, false, false);
        m_mock.reset();
        assertKeepCodeAndKeepClasses(new String[] { "-x", "dummy", "--keep-code" }, true, false);
        m_mock.reset();
        assertKeepCodeAndKeepClasses(new String[] { "-x", "dummy", "--keep-classes" }, false, true);
        m_mock.reset();
        assertKeepCodeAndKeepClasses(new String[] { "-x", "dummy", "-s", "dummy" }, true, false);
        m_mock.reset();
        assertKeepCodeAndKeepClasses(new String[] { "-x", "dummy", "-c", "dummy" }, false, true);
        m_mock.reset();
        assertKeepCodeAndKeepClasses(new String[] { "-x", "dummy", "--keep-code", "-s", "dummy" }, true, false);
        m_mock.reset();
        assertKeepCodeAndKeepClasses(new String[] { "-x", "dummy", "--keep-classes", "-c", "dummy" }, false, true);
    }

    private void assertKeepCodeAndKeepClasses(String[] args, boolean keepCode, boolean keepClasses) throws IOException {
        m_main.run(args, m_mock);
        assertEquals(keepCode, m_mock.isKeepCode());
        assertEquals(keepClasses, m_mock.isKeepClasses());
    }
}
