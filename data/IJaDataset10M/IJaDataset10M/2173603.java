package junitconverter;

import java.util.*;

public class SimpleClassWriter implements ClassWriter {

    private final List<String> lines;

    private final LineOffsets linesChanges = new LineOffsets();

    /**
	 * @param lines The source of the class we're writing.
	 */
    public SimpleClassWriter(List<String> lines) {
        this.lines = new ArrayList<String>(lines);
    }

    public void insertLine(int lineNumber, String line) {
        int realLine = linesChanges.getRealLinePosition(lineNumber);
        lines.add(realLine, line);
        linesChanges.addLineAt(lineNumber);
    }

    public void replaceLine(int lineNumber, String line) {
        int realLine = linesChanges.getRealLinePosition(lineNumber);
        lines.remove(realLine);
        lines.add(realLine, line);
    }

    public List<String> result() {
        return new ArrayList<String>(lines);
    }

    private static class LineOffsets {

        private final List<LineOffset> offsets = new ArrayList<LineOffset>();

        public int getRealLinePosition(int origLine) {
            int realLine = origLine;
            for (LineOffset offset : offsets) {
                if (offset.getLine() < origLine) {
                    realLine++;
                }
            }
            return realLine;
        }

        public void addLineAt(int origLine) {
            offsets.add(new LineOffset(origLine));
            Collections.sort(offsets);
        }
    }

    private static class LineOffset implements Comparable<LineOffset> {

        private int line;

        public LineOffset(int line) {
            this.line = line;
        }

        public int getLine() {
            return line;
        }

        public int compareTo(LineOffset o) {
            if (getLine() > o.getLine()) {
                return 1;
            } else if (getLine() < o.getLine()) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return String.format("[Line: %d]", line);
        }
    }
}
