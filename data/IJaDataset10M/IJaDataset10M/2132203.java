package net.sourceforge.cruisecontrol.util;

/**
 * Tracks a "buffer" of lines from a build, which allows a caller to ask all lines after a certain starting line number.
 * Note that the buffer rotates and will only include up to the last maxLines lines written to the buffer.
 */
public class BuildOutputBuffer implements StreamConsumer {

    private String[] lineBuffer;

    private int totalLines;

    /**
     * @param maxLines Maximum number of lines that can be placed into the buffer.
     */
    public BuildOutputBuffer(int maxLines) {
        lineBuffer = new String[maxLines];
        totalLines = 0;
    }

    /**
     * Consumes the line provided by adding it next in the buffer. If the buffer is full, it starts back at the
     * beginning of the buffer!!!
     */
    public synchronized void consumeLine(String line) {
        lineBuffer[totalLines % lineBuffer.length] = line;
        totalLines += 1;
    }

    /**
     * @return All lines available from firstLine (inclusive) up to maxLines.
     */
    public String[] retrieveLines(int firstLine) {
        int count = totalLines - firstLine;
        if (count <= 0) {
            return new String[0];
        }
        String[] result;
        int i = 0;
        if (count > lineBuffer.length) {
            int linesSkipped = count - lineBuffer.length;
            firstLine += linesSkipped;
            count = lineBuffer.length;
            result = new String[count + 1];
            result[i++] = "(Skipped " + linesSkipped + " lines)";
        } else {
            result = new String[count];
        }
        for (; count-- > 0; i++) {
            result[i] = lineBuffer[firstLine++ % lineBuffer.length];
        }
        return result;
    }
}
