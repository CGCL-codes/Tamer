package org.paninij.tests.panini_tests;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.paninij.tests.framework.IPaniniTestAdaptor;
import edu.iastate.cs.designlab.testutilities.AbstractCompilerTest;
import edu.iastate.cs.designlab.testutilities.ICompilerTest;

/**
 * Create compiler tests for the distribution examples.
 * @author Sean Mooney
 *
 */
public class ExamplesTestProvider {

    private static final String ROOT_TEST_FOLDER = "Examples";

    /**
     * Create a test that uses the files in the 
     * GeneticAlgorithm example.
     * @return
     */
    public static ICompilerTest createGeneticAlgTest() {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(ROOT_TEST_FOLDER);
        pathBuilder.append(File.separator);
        pathBuilder.append("GeneticAlgorithm");
        File root = new File(pathBuilder.toString());
        String[] files = filesToCompile(root, true);
        return makeTest(pathBuilder.toString(), files);
    }

    /**
     * Create a test that uses files in the Image example.
     * @return
     */
    public static ICompilerTest createImageStatsTest() {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(ROOT_TEST_FOLDER);
        pathBuilder.append(File.separator);
        pathBuilder.append("Image");
        File root = new File(pathBuilder.toString());
        String[] files = filesToCompile(root, true);
        return makeTest(pathBuilder.toString(), files);
    }

    /**
     * Create a test that uses files in the TravelService
     * example.
     * @return
     */
    public static ICompilerTest createTravelServiceTest() {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(ROOT_TEST_FOLDER);
        pathBuilder.append(File.separator);
        pathBuilder.append("TravelService");
        File root = new File(pathBuilder.toString());
        String[] files = filesToCompile(root, true);
        return makeTest(pathBuilder.toString(), files);
    }

    /**
     * 
     * @param testFolder
     * @param files
     * @return
     */
    private static ICompilerTest makeTest(String testFolder, String[] files) {
        IPaniniTestAdaptor testProv = new IPaniniTestAdaptor(testFolder, files);
        testProv.setCompileAppendTestFolder(false);
        return testProv;
    }

    /**
     * Return a list of .java files to compile from the current test folder.
     * @param testFolder
     * @param useFullPath true if full test path should be append to front of file file, false if only file name is needed
     * @return
     */
    protected static String[] filesToCompile(File testFolder, boolean useFullPath) {
        ArrayList<String> files = new ArrayList<String>();
        FileFilter javaFileFilter = new FileFilter() {

            public boolean accept(File paramFile) {
                String s = paramFile.getName();
                if (paramFile.isDirectory()) {
                    return !s.equals("bin");
                } else {
                    return s.endsWith(".java");
                }
            }
        };
        computeFilesToCompile(files, testFolder, javaFileFilter, useFullPath);
        String[] elems = new String[files.size()];
        return files.toArray(elems);
    }

    /**
     * Recursively find all the files that match the file filter.
     * Adds files directly to the fileList.
     * @param fileList
     * @param dir
     * @param filter file filter to accept or reject files.  The filter should accept directories.
     */
    private static void computeFilesToCompile(List<String> fileList, File dir, FileFilter filter, boolean useFullPath) {
        Stack<File> stack = new Stack<File>();
        pushAll(dir, stack, filter);
        while (!stack.isEmpty()) {
            File top = stack.pop();
            if (top.isDirectory()) {
                pushAll(top, stack, filter);
            } else {
                try {
                    if (useFullPath) {
                        fileList.add(top.getCanonicalPath());
                    } else {
                        fileList.add(top.getName());
                    }
                } catch (IOException ioe) {
                    Logger.getLogger(AbstractCompilerTest.class.getName()).log(Level.SEVERE, "Unable to get a file path", ioe);
                }
            }
        }
    }

    /**
     * Push all the files in the current file into the stack.
     * Returns a reference to the stack passed in.
     * @param files
     * @param stack
     * @return
     */
    private static Stack<File> pushAll(File file, Stack<File> stack, FileFilter filter) {
        File[] files = file.listFiles(filter);
        if (files != null) {
            for (File f : files) stack.push(f);
        }
        return stack;
    }
}
