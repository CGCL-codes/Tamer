package com.reeltwo.jumble;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import com.reeltwo.jumble.util.JavaRunner;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the corresponding class.
 * 
 * @author Tin Pavlinic
 * @version $Revision: 738 $
 */
public class JumbleTest extends TestCase {

    public void testBroken() throws Exception {
        assertEquals(getExpectedOutput("experiments.Broken"), runCommandLineJumble("experiments.Broken", -1));
    }

    public void testInterface() throws Exception {
        assertEquals(getExpectedOutput("experiments.Interface"), runCommandLineJumble("experiments.Interface", -1));
    }

    public void testNonTestTestClass() throws Exception {
        assertEquals("ERROR: experiments.JumblerExperiment is not a test class.", runCommandLineJumble("experiments.JumblerExperiment", "experiments.JumblerExperiment", false).trim());
    }

    public void testNonExistentClass() throws Exception {
        assertEquals("ERROR: Class nonexistent.Class not found.", runCommandLineJumble("nonexistent.Class", "nonexistent.ClassTest", false).trim());
    }

    public void testJumblerExperiment() throws Exception {
        String expected = getExpectedOutput("experiments.JumblerExperiment");
        String got = runCommandLineJumble("experiments.JumblerExperiment", -1);
        StringTokenizer tokens1 = new StringTokenizer(expected, "\n");
        StringTokenizer tokens2 = new StringTokenizer(got, "\n");
        assertEquals(tokens1.countTokens(), tokens2.countTokens());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
    }

    public void testSillySuite() throws Exception {
        String expected = readAll(getClass().getClassLoader().getResourceAsStream("com/reeltwo/jumble/SillySuite.txt"));
        String got = runCommandLineJumble("experiments.JumblerExperiment", "experiments.JumblerExperimentSillySuiteTest", false);
        StringTokenizer tokens1 = new StringTokenizer(expected, "\n");
        StringTokenizer tokens2 = new StringTokenizer(got, "\n");
        assertEquals(tokens1.countTokens(), tokens2.countTokens());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
    }

    public void testNoDebug() throws Exception {
        String expected = getExpectedOutput("com.reeltwo.jumble.NoDebug");
        String got = runCommandLineJumble("DebugNone", "experiments.JumblerExperimentTest", false);
        StringTokenizer tokens1 = new StringTokenizer(expected, "\r\n");
        StringTokenizer tokens2 = new StringTokenizer(got, "\r\n");
        assertEquals(tokens1.countTokens(), tokens2.countTokens());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
    }

    public void testNoTestClass() throws Exception {
        assertEquals(getExpectedOutput("experiments.NoTestClass"), runCommandLineJumble("experiments.NoTestClass", -1));
    }

    public void testFloatReturn() throws Exception {
        final String out = runCommandLineJumble("experiments.FloatReturn", "experiments.FloatReturnTest", true);
        final StringTokenizer tokens = new StringTokenizer(out, "\n");
        assertEquals("Mutating experiments.FloatReturn", tokens.nextToken().trim());
        assertEquals("Tests: experiments.FloatReturnTest", tokens.nextToken().trim());
        assertNotNull(tokens.nextToken());
        assertEquals("..", tokens.nextToken().trim());
        assertNotNull(tokens.nextToken());
        assertEquals("Score: 100%", tokens.nextToken().trim());
    }

    public void testLength1() throws Exception {
        String expected = getExpectedOutput("experiments.JumblerExperiment");
        String got = runCommandLineJumble("experiments.JumblerExperiment", 1);
        StringTokenizer tokens1 = new StringTokenizer(expected, "\n");
        StringTokenizer tokens2 = new StringTokenizer(got, "\n");
        assertEquals(tokens1.countTokens(), tokens2.countTokens());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
    }

    public void testLength2() throws Exception {
        String expected = getExpectedOutput("experiments.JumblerExperiment");
        String got = runCommandLineJumble("experiments.JumblerExperiment", 2);
        StringTokenizer tokens1 = new StringTokenizer(expected, "\n");
        StringTokenizer tokens2 = new StringTokenizer(got, "\n");
        assertEquals(tokens1.countTokens(), tokens2.countTokens());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
    }

    public void testJUnit4() throws Exception {
        String expected = readAll(getClass().getClassLoader().getResourceAsStream("com/reeltwo/jumble/JumblerExperiment.txt")).replaceAll("experiments.JumblerExperimentTest", "experiments.JumblerExperimentJUnit4Test");
        String got = runCommandLineJumble("experiments.JumblerExperiment", "experiments.JumblerExperimentJUnit4Test", false);
        StringTokenizer tokens1 = new StringTokenizer(expected, "\n");
        StringTokenizer tokens2 = new StringTokenizer(got, "\n");
        assertEquals("failed with string: " + got, tokens1.countTokens(), tokens2.countTokens());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
        tokens1.nextToken();
        tokens2.nextToken();
        assertEquals(tokens1.nextToken(), tokens2.nextToken());
    }

    public static Test suite() {
        return new TestSuite(JumbleTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    private String getExpectedOutput(String className) throws Exception {
        String location = "com/reeltwo/jumble/" + className.substring(className.lastIndexOf('.') + 1) + ".txt";
        return readAll(getClass().getClassLoader().getResourceAsStream(location));
    }

    private String runCommandLineJumble(String className, int max) throws Exception {
        String[] args = max < 0 ? new String[] { className } : new String[] { className, "-m", "" + max };
        JavaRunner runner = new JavaRunner("com.reeltwo.jumble.Jumble", args);
        Process p = runner.start();
        return readAll(p.getInputStream());
    }

    private String runCommandLineJumble(String className, String testName, boolean returns) throws Exception {
        String[] args = new String[returns ? 3 : 2];
        args[0] = className;
        args[1] = returns ? "-r" : testName;
        if (returns) {
            args[2] = testName;
        }
        JavaRunner runner = new JavaRunner("com.reeltwo.jumble.Jumble", args);
        Process p = runner.start();
        return readAll(p.getInputStream());
    }

    public static String readAll(InputStream is) throws Exception {
        StringBuffer buf = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String temp;
        while ((temp = in.readLine()) != null) {
            buf.append(temp + "\n");
        }
        in.close();
        return buf.toString();
    }
}
