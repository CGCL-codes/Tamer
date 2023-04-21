package de.fzi.injectj.testsuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Vector;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import recoder.util.Debug;
import de.fzi.injectj.backend.GlobalSettings;
import de.fzi.injectj.backend.Kernel;
import de.fzi.injectj.backend.ProgressEvent;
import de.fzi.injectj.backend.ProgressListener;
import de.fzi.injectj.backend.Project;
import de.fzi.injectj.model.GlobalFunctions;
import de.fzi.injectj.model.impl.recoder.NextTempVariableService;

/**
 * @author Tobias Gutzmann
 *
 * 
 */
public class TestProjects extends TestCase {

    private static final String projectDirName = "project/";

    private static final String outputDirName = "output/";

    private static final String expectedOutputDirName = "expected_output/";

    private static String basePath = "../injectj/NewTestSuite";

    private static String expectedOutputPath;

    private static String outputPath;

    private static String projectPath;

    public static void setupDirNames(String bp) {
        String basePath = bp;
        basePath = basePath.trim();
        if (!basePath.endsWith(File.separator)) {
            basePath = basePath + File.separator;
        }
        File f = new File(basePath);
        if (!f.isDirectory()) {
            System.err.println("Directory " + basePath + " could not be found.");
            System.err.println("Bailing out...");
            System.exit(-1);
        }
        projectPath = basePath + projectDirName;
        f = new File(projectPath);
        if (!f.isDirectory()) {
            System.err.println("Project file directory " + projectPath + " could not be found!");
            System.err.println("Bailing out...");
            System.exit(-1);
        }
        expectedOutputPath = basePath + expectedOutputDirName;
        f = new File(expectedOutputPath);
        if (!f.isDirectory()) {
            System.err.println("Directory " + expectedOutputPath + " for reference output files could not be found!");
            System.err.println("Bailing out...");
            System.exit(-1);
        }
        outputPath = basePath + outputDirName;
        f = new File(outputPath);
        if (!f.isDirectory()) {
            System.err.println("Output directory " + outputPath + " could not be found!");
            System.err.println("Bailing out...");
            System.exit(-1);
        }
    }

    public TestProjects(String arg0) {
        super(arg0);
    }

    private Vector findFilesRecursively(String path, String projectName) {
        Vector result = new Vector();
        File directory = new File(expectedOutputPath + projectName + "/" + path);
        if (directory == null) return result;
        String files[] = directory.list();
        if (files == null) return result;
        int fileNumReported = 0;
        for (int i = 0; i < files.length; i++) {
            File current = new File(expectedOutputPath + projectName + "/" + path + files[i]);
            if (!current.canRead()) {
                continue;
            } else if (current.isDirectory()) {
                if (!current.getName().equals("CVS")) result.addAll(findFilesRecursively(path + current.getName() + "/", projectName));
            } else if (current.isFile() && current.getName().endsWith(".java")) {
                result.add(path + current.getName());
                if (fileNumReported < 20) System.out.print(path + current.getName() + "; ");
                if (fileNumReported == 20) System.out.println("and more");
                fileNumReported++;
            }
        }
        return result;
    }

    private void testIJProject(String projectName) {
        testIJProject(projectName, true);
    }

    private void testIJProject(String projectName, boolean checkConsoleOutput) {
        new File(outputPath + projectName + "console_output.txt").delete();
        deleteJavaFilesRecursively(outputPath + projectName + "/");
        NextTempVariableService.resetNextTempVariableNr();
        Debug.setOutput(new PrintStream(new OutputStream() {

            public void write(int i) {
            }
        }));
        System.out.println("==========================");
        System.out.println("Testing " + projectName);
        System.out.print("Files for comparison: ");
        Vector expectedFiles = findFilesRecursively("", projectName);
        if (expectedFiles.size() == 0) System.err.println(">>>>>> No files for comparison!! Please fix that !!\n"); else System.out.println("\n");
        Kernel kernel = new Kernel();
        Project project = kernel.loadProject(projectPath + projectName + ".ijp");
        GlobalSettings.setVerbose(false);
        project.getProgressSupport().addProgressListener(new ProgressListener() {

            private int cnt = 0;

            public void progressPerformed(ProgressEvent e) {
                System.out.print(".");
                cnt++;
                if (cnt == 80) {
                    System.out.println();
                    cnt = 0;
                }
            }
        });
        assertTrue("Weaving failed", project.startWeaving());
        System.out.println("\n");
        String actualOutput = sw.toString();
        File latestOutputFile = new File(outputPath + projectName + "/console_output.txt");
        try {
            if (!latestOutputFile.exists()) {
                latestOutputFile.createNewFile();
            }
            FileWriter writer;
            writer = new FileWriter(latestOutputFile);
            writer.write(actualOutput);
            writer.close();
        } catch (IOException e) {
            System.err.println("Cannot write " + latestOutputFile.getAbsolutePath() + "; Reason: " + e.getMessage());
        }
        String filesMismatchingOutput = "";
        Iterator iter = expectedFiles.iterator();
        while (iter.hasNext()) {
            String current = (String) iter.next();
            if (!current.endsWith(".java")) continue;
            File expectedFile = new File(expectedOutputPath + projectName + "/" + current);
            File actualFile = new File(GlobalSettings.getAbsolutePath(project.getProjectPath(), project.getOutputPath()) + "/" + current);
            char exptbuf[] = new char[(int) expectedFile.length()];
            FileReader expectedReader;
            try {
                expectedReader = new FileReader(expectedFile);
                expectedReader.read(exptbuf);
            } catch (FileNotFoundException e) {
                fail("FileNotFoundException");
            } catch (IOException e) {
                fail("IOException");
            }
            char actbuf[] = new char[(int) actualFile.length()];
            FileReader actualReader;
            try {
                actualReader = new FileReader(actualFile);
                actualReader.read(actbuf);
            } catch (FileNotFoundException e) {
                fail("FileNotFoundException:" + actualFile);
            } catch (IOException e) {
                fail("IOException");
            }
            if (!compare(actbuf, exptbuf)) filesMismatchingOutput += current + ", ";
        }
        if (filesMismatchingOutput.length() != 0) {
            assertTrue("Files mismatching: " + filesMismatchingOutput, false);
        }
        if (checkConsoleOutput) {
            File expectedFile = new File(expectedOutputPath + projectName + "/" + "console_output.txt");
            File actualFile = new File(GlobalSettings.getAbsolutePath(project.getProjectPath(), project.getOutputPath()) + "/" + "console_output.txt");
            char exptbuf[] = new char[(int) expectedFile.length()];
            FileReader expectedReader;
            try {
                expectedReader = new FileReader(expectedFile);
                expectedReader.read(exptbuf);
            } catch (FileNotFoundException e) {
                fail("FileNotFoundException");
            } catch (IOException e) {
                fail("IOException");
            }
            char actbuf[] = new char[(int) actualFile.length()];
            FileReader actualReader;
            try {
                actualReader = new FileReader(actualFile);
                actualReader.read(actbuf);
            } catch (FileNotFoundException e) {
                fail("FileNotFoundException:" + actualFile);
            } catch (IOException e) {
                fail("IOException");
            }
            assertTrue("console output mismatch while testing project " + projectName, compare(actbuf, exptbuf));
        }
    }

    private PrintStream currentPrintStream;

    private StringWriter sw;

    protected void setUp() {
        setupDirNames(basePath);
        GlobalSettings.setVerbose(false);
        sw = new StringWriter();
        currentPrintStream = new PrintStream(new OutputStream() {

            public void write(int i) {
                sw.write(i);
            }
        });
        GlobalFunctions.setConsoleOutStream(currentPrintStream);
    }

    private void deleteJavaFilesRecursively(String path) {
        File myPath = new File(path);
        File list[] = myPath.listFiles();
        if (list == null) return;
        for (int i = 0; i < list.length; i++) {
            File current = list[i];
            if (current.isDirectory()) {
                deleteJavaFilesRecursively(current.getAbsolutePath());
                current.delete();
            } else if (current.isFile() && current.getName().endsWith(".java")) current.delete();
        }
    }

    protected void tearDown() {
        GlobalFunctions.setConsoleOutStream(System.out);
    }

    public void testBuildInAnalyses() {
        testIJProject("TestBuildInAnalyses");
    }

    public void testBuildInTransformations() {
        testIJProject("TestBuildInTransformations");
    }

    public void testOptimizations() {
        testIJProject("Optimizations");
    }

    public void testSeveralIssues() {
        testIJProject("TestSeveralIssues");
    }

    public void testTransformations() {
        testIJProject("TransformationTester");
    }

    public void testPrintParents() {
        testIJProject("PrintParents", false);
    }

    public void testBreakContinue() {
        testIJProject("BreakContinue");
    }

    public void testCompobench() {
        testIJProject("Compobench");
    }

    public void testReplaceAssert() {
        testIJProject("ReplaceAssert", false);
    }

    public void testConsumerProducer() {
        testIJProject("ConsumerProducer");
    }

    public void testCwmr() {
        testIJProject("Cwmr", false);
    }

    public void testComments() {
        testIJProject("Comments");
    }

    public void testNormalizeImports() {
        testIJProject("NormalizeImports");
    }

    public void testObservifier() {
        testIJProject("Observifier");
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            System.err.println("No Inject/J base directory for samples provided!");
            System.err.println("Example:");
            System.err.println("java -cp <...> TestProjects /home/user/InjectJ/samples");
            System.err.println("Bailing out...");
            System.exit(-1);
        }
        basePath = args[0];
        TestSuite testSuite = new TestSuite(TestProjects.class);
        TestResult result = new TestResult();
        testSuite.run(result);
        System.out.println("Number of errors: " + result.errorCount() + " / " + result.runCount());
        System.out.println("Number of failures: " + result.failureCount() + " / " + result.runCount());
    }

    /**
	 * This method normalizes the two input strings by removing redundant whitespaces and then compares 
	 * for equality
	 * @param actbuf the actual buffer
	 * @param exptbuf the expected text
	 * @return <code>true</code> if normalized strings are identical, <code>false</code> otherwise
	 */
    private boolean compare(char[] actbuf, char[] exptbuf) {
        assert (actbuf != null);
        assert (exptbuf != null);
        String[] strings = new String[] { new String(actbuf), new String(exptbuf) };
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].replaceAll("\r|\n|\t", " ");
            strings[i] = strings[i].replaceAll("  +", " ");
            strings[i] = strings[i].replaceAll(" ?\\{ ?", "\\{");
            strings[i] = strings[i].replaceAll(" ?\\} ?", "\\}");
            strings[i] = strings[i].replaceAll(" ?\\( ?", "\\(");
            strings[i] = strings[i].replaceAll(" ?\\) ?", "\\)");
            strings[i] = strings[i].replaceAll(" ?; ?", ";");
            strings[i] = strings[i].replaceAll(" ?, ?", ",");
        }
        boolean identical = strings[0].equals(strings[1]);
        return identical;
    }
}
