package org.fest.assertions;

import static org.fest.util.Closeables.close;
import static org.fest.util.Objects.areEqual;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Compares the contents of two files.
 *
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
class FileContentComparator {

    private static final String EOF = "EOF";

    /**
   * Verifies that the given files have the same content. Adapted from <a
   * href="http://junit-addons.sourceforge.net/junitx/framework/FileAssert.html" target="_blank">FileAssert</a> (from
   * <a href="http://sourceforge.net/projects/junit-addons">JUnit-addons</a>.)
   * @param actual the first <code>File</code> to compare.
   * @param expected the given <code>File</code> to compare <code>actual</code> to.
   * @return the differences between the given files, if any.
   * @throws IOException any I/O error.
   */
    LineDiff[] compareContents(File actual, File expected) throws IOException {
        InputStream actualInputStream = null;
        InputStream expectedInputStream = null;
        try {
            actualInputStream = new FileInputStream(actual);
            expectedInputStream = new FileInputStream(expected);
            List<LineDiff> diffs = checkSameContent(reader(actualInputStream), reader(expectedInputStream));
            return diffs.toArray(new LineDiff[diffs.size()]);
        } finally {
            close(expectedInputStream);
            close(actualInputStream);
        }
    }

    private LineNumberReader reader(InputStream inputStream) {
        return new LineNumberReader(new BufferedReader(new InputStreamReader(inputStream)));
    }

    private List<LineDiff> checkSameContent(LineNumberReader actual, LineNumberReader expected) throws IOException {
        List<LineDiff> diffs = new ArrayList<LineDiff>();
        while (true) {
            if (!expected.ready() && !actual.ready()) return diffs;
            int lineNumber = expected.getLineNumber();
            String actualLine = actual.readLine();
            String expectedLine = expected.readLine();
            if (areEqual(actualLine, expectedLine)) continue;
            diffs.add(new LineDiff(lineNumber, actualLine, expectedLine));
            if (!actual.ready() && expected.ready()) {
                diffs.add(new LineDiff(lineNumber, EOF, expectedLine));
                return diffs;
            }
            if (actual.ready() && !expected.ready()) {
                diffs.add(new LineDiff(lineNumber, actualLine, EOF));
                return diffs;
            }
        }
    }

    static class LineDiff {

        final int lineNumber;

        final String actual;

        final String expected;

        LineDiff(int lineNumber, String actual, String expected) {
            this.lineNumber = lineNumber;
            this.actual = actual;
            this.expected = expected;
        }
    }
}
