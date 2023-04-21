package be.lassi.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Helper tool to write the copyright notice at the start
 * of each Java source file in this project.  The notice is
 * read from the top of this file (contents until the line
 * starting with "package").  This notice is replacing
 * anything above the line starting with "package" in all
 * Java source files in this project.
 */
public class LicenseNotice {

    /**
     * The number of files that have been processed.
     */
    private int fileCount;

    /**
     * The copyright notice that is written at the top of each file.
     */
    private String notice;

    /**
     * Writes the copyright notice.
     *
     * @param args command line arguments (ignored).
     * @throws IOException if problem
     */
    public static void main(final String[] args) throws IOException {
        new LicenseNotice().refresh();
    }

    /**
     * Refreshes the copyright notice.
     *
     * @throws IOException
     */
    public void refresh() throws IOException {
        readNotice();
        processDir(new File("."));
        processDir(new File("../lassi-testcases"));
        System.out.println("Files processed: " + fileCount);
    }

    /**
     * Refreshes the copyright notice in the files of given
     * directory and subdirectories.
     *
     * @param dir the directory to be processed
     * @throws IOException
     */
    private void processDir(final File dir) throws IOException {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                processDir(file);
            } else if (shouldProcess(file)) {
                System.out.println(file.getAbsolutePath());
                fileCount++;
                refreshFile(file);
            }
        }
    }

    /**
     * Indicates whether the copyright notice should
     * be refreshed in given file.
     *
     * @param file the file to be checked
     * @return true if file needs to be processed
     */
    private boolean shouldProcess(final File file) {
        String name = file.getName();
        return name.endsWith(".java");
    }

    /**
     * Reads the copyright notice from the top of this file.
     *
     * @throws IOException
     */
    private void readNotice() throws IOException {
        StringBuilder b = new StringBuilder();
        LineNumberReader reader = open(noticeFilename());
        try {
            String string = null;
            do {
                string = reader.readLine();
                if (string != null) {
                    if (string.startsWith("package")) {
                        string = null;
                    } else {
                        b.append(string);
                        b.append("\n");
                    }
                }
            } while (string != null);
            notice = b.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * Refreshes the copyright notice in given file.
     *
     * @param file the file to be refreshed
     * @throws IOException
     */
    private void refreshFile(final File file) throws IOException {
        String contents = readFile(file.getAbsolutePath());
        FileWriter out = new FileWriter(file);
        try {
            out.write(notice);
            out.write(contents);
        } finally {
            out.close();
        }
    }

    /**
     * Returns contents of file with given name; the contents
     * above the first line starting with "package" is ignored.
     *
     * @param filename the name of the file to read
     * @return the file contents starting with the package declaration
     * @throws IOException if problem reading the file
     */
    private String readFile(final String filename) throws IOException {
        StringBuilder b = new StringBuilder();
        boolean contentsStarted = false;
        LineNumberReader reader = open(filename);
        try {
            String string = null;
            do {
                string = reader.readLine();
                if (string != null) {
                    if (string.startsWith("package")) {
                        contentsStarted = true;
                    }
                    if (contentsStarted) {
                        b.append(string);
                        b.append("\n");
                    }
                }
            } while (string != null);
        } finally {
            reader.close();
        }
        String contents = b.toString();
        return contents;
    }

    /**
     * Opens file with given name.
     *
     * @param filename the name of the file to open
     * @return the file
     * @throws FileNotFoundException
     */
    private LineNumberReader open(final String filename) throws FileNotFoundException {
        return new LineNumberReader(new BufferedReader(new FileReader(filename)));
    }

    /**
     * Gets the name of this file.
     *
     * @return the name of this file
     */
    private String noticeFilename() {
        String filename = getClass().getName();
        filename = filename.replace('.', File.separatorChar);
        filename = "src" + File.separator + filename + ".java";
        return filename;
    }
}
