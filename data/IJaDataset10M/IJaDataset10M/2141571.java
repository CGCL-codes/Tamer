package org.jcvi.command.grid;

import org.jcvi.common.command.Command;
import org.jcvi.common.command.grid.ArrayGridJobBuilder;
import org.jcvi.common.command.grid.GridJob;
import org.jcvi.common.command.grid.GridJobBuilders;
import org.jcvi.common.command.grid.GridJobFuture;
import org.jcvi.common.core.io.IOUtil;
import org.junit.*;
import java.io.*;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: aresnick
 * Date: Aug 11, 2010
 * Time: 10:15:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestArrayGridJob extends TestGridJob {

    private static final File GRID_SCRATCH_DIR = new File("/usr/local/scratch");

    private static File rootTestDir;

    private static File command;

    private File testDir;

    private File inputFile;

    private File outputFile;

    private File errorFile;

    @BeforeClass
    public static void globalSetup() throws Exception {
        rootTestDir = getScratchDir(GRID_SCRATCH_DIR);
        command = new File(rootTestDir, "test.sh");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(command);
            writer.println("#!/bin/bash");
            writer.println("read SLEEP_TIME");
            writer.println("/bin/sleep $SLEEP_TIME");
            writer.println("/bin/echo $SLEEP_TIME");
            writer.flush();
        } finally {
            writer.close();
        }
        command.setReadable(true);
        command.setWritable(true);
        command.setExecutable(true);
    }

    @Before
    public void testSetup() {
        testDir = getScratchDir(rootTestDir);
        inputFile = new File(testDir, "input");
        outputFile = new File(testDir, "output");
        errorFile = new File(testDir, "error");
    }

    @AfterClass
    public static void globalCleanup() throws Exception {
        File[] testDirs = rootTestDir.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        if (testDirs == null || testDirs.length == 0) {
            IOUtil.recursiveDelete(rootTestDir);
        }
    }

    private static File getScratchDir(File parentDir) {
        File dir = new File(parentDir, ("" + Math.random()).replace(".", ""));
        dir.mkdirs();
        return dir;
    }

    private void writeInputFiles(File inputFileRoot, Map<Integer, String> jobInputMap) throws Exception {
        for (Map.Entry<Integer, String> entry : jobInputMap.entrySet()) {
            File inputFile = new File(inputFileRoot.getAbsolutePath() + "." + entry.getKey());
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(inputFile);
                writer.println(entry.getValue());
                writer.flush();
            } finally {
                IOUtil.closeAndIgnoreErrors(writer);
            }
            inputFile.setReadable(true);
        }
    }

    private String getJobOutput(File filename) throws Exception {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(filename);
            String result = IOUtil.toString(stream);
            return result.substring(0, result.length() - 1);
        } finally {
            IOUtil.closeAndIgnoreErrors(stream);
        }
    }

    @Test
    public void testArrayGridJob() throws Exception {
        Map<Integer, String> jobInputMap = new HashMap<Integer, String>();
        jobInputMap.put(1, "5");
        jobInputMap.put(2, "5");
        jobInputMap.put(3, "5");
        jobInputMap.put(4, "5");
        jobInputMap.put(5, "5");
        writeInputFiles(inputFile, jobInputMap);
        ArrayGridJobBuilder builder = GridJobBuilders.createArrayGridJobBuilder(gridExecutor.getSession(), new Command(command), TestGridJob.TEST_PROJECT_CODE);
        builder.setBulkJobLoop(1, 5);
        builder.setInputFile(inputFile);
        builder.setOutputFile(outputFile);
        builder.setErrorFile(errorFile);
        GridJobFuture future = gridExecutor.submit(builder.build());
        int exitStatus = future.get();
        System.out.println("grid job future exit status was " + exitStatus);
        printJobResults(future.getGridJob());
        for (Map.Entry<Integer, String> entry : jobInputMap.entrySet()) {
            File result = new File(outputFile.getAbsolutePath() + "." + entry.getKey());
            assertEquals("array job " + entry.getValue() + " data output error", entry.getValue(), getJobOutput(result));
        }
        IOUtil.recursiveDelete(testDir);
    }

    @Test
    public void testArrayGridJobWithMaxRunningTasks() throws Exception {
        Map<Integer, String> jobInputMap = new HashMap<Integer, String>();
        jobInputMap.put(1, "5");
        jobInputMap.put(2, "5");
        jobInputMap.put(3, "5");
        jobInputMap.put(4, "5");
        jobInputMap.put(5, "5");
        writeInputFiles(inputFile, jobInputMap);
        ArrayGridJobBuilder builder = GridJobBuilders.createArrayGridJobBuilder(gridExecutor.getSession(), new Command(command), TestGridJob.TEST_PROJECT_CODE);
        builder.setBulkJobLoop(1, 5);
        builder.setMaxRunningTasks(2);
        builder.setInputFile(inputFile);
        builder.setOutputFile(outputFile);
        builder.setErrorFile(errorFile);
        GridJobFuture future = gridExecutor.submit(builder.build());
        int exitStatus = future.get();
        System.out.println("grid job future exit status was " + exitStatus);
        printJobResults(future.getGridJob());
        for (Map.Entry<Integer, String> entry : jobInputMap.entrySet()) {
            File result = new File(outputFile.getAbsolutePath() + "." + entry.getKey());
            assertEquals("array job " + entry.getValue() + " data output error", entry.getValue(), getJobOutput(result));
        }
        IOUtil.recursiveDelete(testDir);
    }

    @Test
    public void testArrayGridJobGridExecutorTimeout() throws Exception {
        Map<Integer, String> jobInputMap = new HashMap<Integer, String>();
        jobInputMap.put(1, "15");
        jobInputMap.put(2, "15");
        jobInputMap.put(3, "15");
        jobInputMap.put(4, "15");
        jobInputMap.put(5, "15");
        writeInputFiles(inputFile, jobInputMap);
        ArrayGridJobBuilder builder = GridJobBuilders.createArrayGridJobBuilder(gridExecutor.getSession(), new Command(command), TestGridJob.TEST_PROJECT_CODE);
        builder.setBulkJobLoop(1, 5);
        builder.setTimeout(Long.valueOf(5));
        builder.setInputFile(inputFile);
        builder.setOutputFile(outputFile);
        builder.setErrorFile(errorFile);
        GridJobFuture future = gridExecutor.submit(builder.build());
        int exitStatus = future.get();
        System.out.println("grid job future exit status was " + exitStatus);
        printJobResults(future.getGridJob());
        for (Map.Entry<Integer, String> entry : jobInputMap.entrySet()) {
            File result = new File(outputFile.getAbsolutePath() + "." + entry.getKey());
            assertTrue("array job " + entry.getValue() + " data output error", result.length() == 0);
        }
        IOUtil.recursiveDelete(testDir);
    }

    @Test
    public void testArrayGridJobGridExecutorTermination() throws Exception {
        Map<Integer, String> jobInputMap = new HashMap<Integer, String>();
        jobInputMap.put(1, "15");
        jobInputMap.put(2, "15");
        jobInputMap.put(3, "15");
        jobInputMap.put(4, "15");
        jobInputMap.put(5, "15");
        writeInputFiles(inputFile, jobInputMap);
        ArrayGridJobBuilder builder = GridJobBuilders.createArrayGridJobBuilder(gridExecutor.getSession(), new Command(command), TestGridJob.TEST_PROJECT_CODE);
        builder.setBulkJobLoop(1, 5);
        builder.setInputFile(inputFile);
        builder.setOutputFile(outputFile);
        builder.setErrorFile(errorFile);
        GridJobFuture future = gridExecutor.submit(builder.build());
        new Thread(new JobCancellation(future, 5)).start();
        int exitStatus = future.get();
        System.out.println("grid job future exit status was " + exitStatus);
        printJobResults(future.getGridJob());
        for (Map.Entry<Integer, String> entry : jobInputMap.entrySet()) {
            File result = new File(outputFile.getAbsolutePath() + "." + entry.getKey());
            assertTrue("array job " + entry.getValue() + " data output error", result.length() == 0);
        }
        IOUtil.recursiveDelete(testDir);
    }

    @Test
    public void testArrayGridJobDirectTermination() throws Exception {
        Map<Integer, String> jobInputMap = new HashMap<Integer, String>();
        jobInputMap.put(1, "15");
        jobInputMap.put(2, "15");
        jobInputMap.put(3, "15");
        jobInputMap.put(4, "15");
        jobInputMap.put(5, "15");
        writeInputFiles(inputFile, jobInputMap);
        ArrayGridJobBuilder builder = GridJobBuilders.createArrayGridJobBuilder(gridExecutor.getSession(), new Command(command), TestGridJob.TEST_PROJECT_CODE);
        builder.setBulkJobLoop(1, 5);
        builder.setTimeout(Long.valueOf(5));
        builder.setInputFile(inputFile);
        builder.setOutputFile(outputFile);
        builder.setErrorFile(errorFile);
        GridJob gridJob = builder.build();
        gridJob.runGridCommand();
        Thread.sleep(5000);
        gridJob.terminate();
        printJobResults(gridJob);
        for (Map.Entry<Integer, String> entry : jobInputMap.entrySet()) {
            File result = new File(outputFile.getAbsolutePath() + "." + entry.getKey());
            assertTrue("array job " + entry.getValue() + " data output error", result.length() == 0);
        }
        IOUtil.recursiveDelete(testDir);
    }
}
