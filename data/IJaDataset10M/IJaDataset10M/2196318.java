package net.sf.igs.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.ggf.drmaa.JobTemplate;
import org.ggf.drmaa.Session;
import org.ggf.drmaa.SessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests whether support for the {@link JobTemplate#setInputPath(String)
 * setInputPath} method really works. The test operates by generating an input
 * file with some text in it (the name of the test class) and then invoking the
 * "cat" command with the file as the input. The output file should then have
 * the same text as the input file.
 * 
 * @see "cat command man page"
 */
public class InputPathTest {

    private static String name = InputPathTest.class.getSimpleName();

    private static File inputFile, outputFile = null;

    private static String inputPath, outputPath = null;

    /**
	 * Tests whether support for the {@link JobTemplate#setOutputPath(String) setOutputPath}
	 * works properly.
	 * @throws IOException 
	 */
    @BeforeClass
    public static void setup() throws IOException {
        inputPath = System.getProperty("user.home") + File.separator + name + ".txt";
        outputPath = System.getProperty("user.home") + File.separator + name + ".out";
        inputFile = new File(inputPath);
        outputFile = new File(outputPath);
        deleteInputFile();
        createInputFile();
    }

    private static void deleteInputFile() {
        if (inputFile != null && inputFile.exists()) {
            boolean deleted = false;
            do {
                deleted = inputFile.delete();
                if (!deleted) {
                    System.err.println("Unable to delete file " + inputFile.getAbsolutePath());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        System.err.println("Test interrupted.");
                        break;
                    }
                }
            } while (!deleted);
        }
    }

    private static void createInputFile() throws IOException {
        File input = new File(inputPath);
        input.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(input));
        writer.write(name);
        writer.newLine();
        writer.close();
    }

    /**
	 * Test the {@link JobTemplate#setInputPath(String) setInputPath} method.
	 */
    @Test
    public void testInputPath() {
        try {
            Session session = SessionFactory.getFactory().getSession();
            session.init(name);
            JobTemplate jt = session.createJobTemplate();
            jt.setRemoteCommand("/bin/cat");
            jt.setJobName(name);
            assertTrue(inputFile.exists());
            jt.setInputPath(":" + inputPath);
            jt.setOutputPath(":" + outputPath);
            String jobId = session.runJob(jt);
            assertNotNull(jobId);
            assertTrue(jobId.length() > 0);
            session.wait(jobId, Session.TIMEOUT_WAIT_FOREVER);
            session.deleteJobTemplate(jt);
            session.exit();
            Thread.sleep(3000);
            assertTrue(outputFile.exists());
            boolean correctOutput = false;
            BufferedReader reader = new BufferedReader(new FileReader(outputFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(name)) {
                    correctOutput = true;
                    break;
                }
            }
            reader.close();
            assertTrue(correctOutput);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
	 * Runs after the test is complete and removes the input and output files.
	 */
    @AfterClass
    public static void cleanup() {
        deleteInputFile();
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }
}
