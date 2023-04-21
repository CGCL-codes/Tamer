package com.panopset.java;

import java.io.File;
import com.panopset.RezIO;
import com.panopset.Strings;
import com.panopset.Util;
import com.panopset.UtilIO;

/**
 * Format source pre-compile time.
 *
 * <p>
 * For each java source line:
 * </p>
 *
 * <ul>
 * <li>
 * Replace tab characters with four spaces.</li>
 * <li>
 * Trim spaces from the end of the lines.</li>
 * <li>
 * Trim a space after a non-empty opening bracket. This makes up for the default
 * Eclipse formatting not matching default checkstyle requirements.</li>
 * </ul>
 *
 * <p>
 * <b>com.panopset.java.UniversalVersion</b> is also updated with the current
 * date.
 * </p>
 *
 * <h5>Ant Task</h5>
 *
 * I like to put an ant build.xml file in all my maven projects that does a
 * FormatSource on my src/main/java and src/test/java directories.
 *
 * If you have the flywheel source on your system, your path could be
 * <b>../flywheel/target/classes</b> instead of the flywheel jar. Or you could
 * specify ${home}/.m2/repository/com/panopset/flywheel/&lt;version&gt;/
 * flywheel-&lt;version&gt;.jar in a maven development environment.
 *
 * <pre>
 *
 * &lt;java classname="com.panopset.java.FormatSource"
 *    classpath="&lt;lib path&gt;panopset.jar" fork="true"&gt;
 *    &lt;arg value="&lt;java source path&gt;"/&gt;
 * &lt;/java&gt;
 *
 * </pre>
 *
 * @see com.panopset.java.Version
 *
 * @author Karl Dinwiddie
 *
 */
public final class FormatSource {

    /**
     * Format Java source files.
     *
     * @param sourceFileDirectory
     *            Source directory to format.
     */
    public static void formatSource(final String sourceFileDirectory) {
        final File file = new File(sourceFileDirectory);
        Util.dspmsg("Formatting " + RezIO.getCanonicalPath(file));
        new FormatSource().format(file);
    }

    /**
     * FormatSource constructor.
     */
    private FormatSource() {
    }

    /**
     * args[0] = Java source code directory.
     *
     * @param args
     *            args[0] is the source root directory that you wish to format.
     */
    public static void main(final String[] args) {
        if (args == null) {
            return;
        }
        if (args.length != 1) {
            return;
        }
        File dir = new File(args[0]);
        if (!dir.exists()) {
            return;
        }
        if (!dir.isDirectory()) {
            return;
        }
        new FormatSource().format(dir);
    }

    /**
     * Format a Java source file. Files without ".java" extension are ignored.
     *
     * @param src
     *            Source file.
     */
    private void format(final File src) {
        if (src.isFile()) {
            if (UtilIO.getExtension(src).equals(".java") || UtilIO.getExtension(src).equals(".xml")) {
                FormatFile ff = new FormatFile(src);
                String s = ff.getResult();
                if (!s.equals(Strings.getStringFromLines(ff.getSourceList()))) {
                    UtilIO.saveStringToFile(s, src);
                }
            }
        } else if (src.isDirectory()) {
            for (File f : src.listFiles()) {
                format(f);
            }
        }
    }
}
