package com.controltier.ctl.tasks;

import com.controltier.ctl.tools.BuildFileTest;
import com.controltier.ctl.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.Project;

/**
 * Created by IntelliJ IDEA.
 * User: alexh
 * Date: Aug 12, 2009
 * Time: 8:59:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestScriptExec extends BuildFileTest {

    protected void setUp() {
        configureProject("src/test/com/controltier/ctl/tasks/script-exec.xml");
    }

    protected void tearDown() throws Exception {
        FileUtils.deleteDir(new File(getFrameworkProjectsBase(), "TestScriptExec"));
    }

    public TestScriptExec(String name) {
        super(name);
    }

    public void testParseExtension() {
        ScriptExec task = new ScriptExec();
        assertEquals("should be a blank string", task.parseExtension("fooblah"), "");
        assertEquals("should be a xml extension", task.parseExtension("fooblah.xml"), "xml");
        assertEquals("should be a xml extension", task.parseExtension("foo.blah.xml"), "xml");
        assertEquals("should be a sh extension", task.parseExtension("foo_blah.sh"), "sh");
        assertEquals("should be a bat extension", task.parseExtension("foo\\blah.bat"), "bat");
    }

    public void testIsScript() throws IOException {
        ScriptExec task = new ScriptExec();
        assertTrue("should be a script", task.isScript("fooblah"));
        assertFalse("should not be a script file", task.isScriptfile("fooblah"));
    }

    public void testIsAntScriptfile() throws IOException {
        ScriptExec task = new ScriptExec();
        File buildxml = File.createTempFile("build", ".xml");
        File hellosh = File.createTempFile("hello", ".sh");
        assertTrue("should be an ant file", task.isAntScriptfile(buildxml.getAbsolutePath()));
        assertFalse("should not be an ant file", task.isAntScriptfile(hellosh.getAbsolutePath()));
        task.setExecutable("/bin/sh");
        assertFalse("should not be an antfile", task.isAntScriptfile(buildxml.getAbsolutePath()));
    }

    public void testIsMalformed() {
        ScriptExec task = new ScriptExec();
        task.setIgnoremalformed(false);
        task.setExecutable("${bogus}");
        assertNull("executable should be null but was: " + task.getExecutable(), task.getExecutable());
        task.setScript("${bogus}");
        assertNull("script should be null but was " + task.getScript(), task.getScript());
        task.setIgnoremalformed(true);
        task.setExecutable("${bogus}");
        assertEquals("executable should be set", task.getExecutable(), "${bogus}");
        task.setScript("${bogus}");
        assertEquals("script should be set", task.getScript(), "${bogus}");
    }

    public void test1() {
        setOutputResults("test1");
    }

    public void test2() {
        setOutputResults("test2");
        expectInfoOutput("test2", "hello");
    }

    public void test3() {
        setOutputResults("test3");
        expectInfoOutput("test3", "hello");
    }

    public void test4() {
        setOutputResults("test4");
        expectInfoOutput("test4", "1,2,3");
    }

    public void test5() {
        setOutputResults("test5");
        expectInfoOutput("test5", "hello");
    }

    public void testIsUnixScriptfileExtension() throws IOException {
        ScriptExec task = new ScriptExec();
        File scriptFile = File.createTempFile("foo", ".sh");
        assertTrue("unix shell script extension not detected", task.isUnixScriptfileExtension(scriptFile.getAbsolutePath()));
        scriptFile = File.createTempFile("foo", ".bat");
        assertFalse("windows shell script extension not detected", task.isUnixScriptfileExtension(scriptFile.getAbsolutePath()));
    }

    public void testCanUseWindowsExecutable() {
        ScriptExec task = new ScriptExec();
        task.setProject(new Project());
        task.getProject().setProperty("os.name", "UnixFlavor");
        assertFalse("shoud be unix", task.canUseWindowsExecutable());
        task.getProject().setProperty("os.name", "Windows");
        assertTrue("shoud be windows", task.canUseWindowsExecutable());
    }

    public void testResolveDefaultExecutable() {
        ScriptExec task = new ScriptExec();
        task.setProject(new Project());
        assertEquals("did not resolve executable to bash", task.resolveDefaultExecutable("bash"), "bash");
        task.getProject().setProperty("os.name", "UnixFlavor");
        assertEquals("did not resolve executable to /bin/sh", task.resolveDefaultExecutable(null), "/bin/sh");
        task.getProject().setProperty("os.name", "Windows");
        assertEquals("did not resolve executable to cmd.exe", task.resolveDefaultExecutable(null), "cmd.exe");
    }
}
