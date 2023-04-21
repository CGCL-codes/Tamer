package net.sf.yaxdiff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Represents the output of <code>diff</code>.
 * @author Ramon Nogueira (ramon döt nogueira at g maíl döt cöm)
 *
 */
final class LineDifferences {

    private static final String OLD_GROUP_FORMAT = "r%df,%dl" + Util.LINE_TERMINATOR;

    private static final String NEW_GROUP_FORMAT = "a%dF,%dL" + Util.LINE_TERMINATOR;

    private static final String CHANGED_GROUP_FORMAT = OLD_GROUP_FORMAT + NEW_GROUP_FORMAT;

    private static final String UNCHANGED_GROUP_FORMAT = "";

    private static final String[] DIFF_CMD = new String[] { "diff", "-a", "--old-group-format=" + OLD_GROUP_FORMAT, "--new-group-format=" + NEW_GROUP_FORMAT, "--changed-group-format=" + CHANGED_GROUP_FORMAT, "--unchanged-group-format=" + UNCHANGED_GROUP_FORMAT };

    public static final int RESULT_SAME = 0, RESULT_DIFFERENCES = 1;

    private static final int RESULT_ERROR = 2;

    public final LinesFile oldFile, newFile;

    /**
     * Create a new instance of {@link LineDifferences} that contains the differences
     * between <code>oldFile</code> and <code>newFile</code>.
     * @param oldFile
     * @param newFile
     */
    public LineDifferences(final LinesFile oldFile, final LinesFile newFile) {
        this.oldFile = oldFile;
        this.newFile = newFile;
    }

    /**
     * Invoke the <code>diff</code> command on the given files, returning the parsed results, or
     * null if the files do not differ.
     * @param oldFile
     * @param newFile
     * @return Parsed results, or null if the files do not differ.
     * @throws IOException
     * @throws InterruptedException
     */
    public int runDiff(TreeifyTask removed, TreeifyTask added) throws ProcessingException {
        final String[] cmd = Arrays.copyOf(DIFF_CMD, DIFF_CMD.length + 2);
        cmd[cmd.length - 2] = oldFile.file.getAbsolutePath();
        cmd[cmd.length - 1] = newFile.file.getAbsolutePath();
        ProcessBuilder procBuilder = new ProcessBuilder(cmd);
        Process proc;
        try {
            proc = procBuilder.start();
        } catch (IOException e) {
            throw new ProcessingException("Problem running native `diff' command", e);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        try {
            String line;
            DiffGroup group;
            try {
                while ((line = reader.readLine()) != null) {
                    Util.log.fine("Processing diff output: " + line);
                    String[] startEnd = line.substring(1).split(",");
                    switch(line.charAt(0)) {
                        case 'a':
                            group = new DiffGroup(Integer.parseInt(startEnd[0]), Integer.parseInt(startEnd[1]), Direction.ADDED);
                            added.next(group);
                            break;
                        case 'r':
                            group = new DiffGroup(Integer.parseInt(startEnd[0]), Integer.parseInt(startEnd[1]), Direction.REMOVED);
                            removed.next(group);
                            break;
                        default:
                            throw new IllegalStateException("'" + line.charAt(0) + "' is not a valid difference indicator!");
                    }
                }
            } catch (IOException e) {
                throw new ProcessingException("Problem reading `diff' program output", e);
            }
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new ProcessingException(e);
            }
        }
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            throw new ProcessingException(e);
        }
        int result = proc.exitValue();
        switch(result) {
            case RESULT_ERROR:
                throw new RuntimeException("diff process returned an error");
            case RESULT_SAME:
                return RESULT_SAME;
            case RESULT_DIFFERENCES:
                break;
            default:
                throw new RuntimeException("diff output code not recognised, try using GNU diff");
        }
        return RESULT_DIFFERENCES;
    }
}
