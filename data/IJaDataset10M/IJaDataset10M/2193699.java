package com.seitenbau.testing.shared.writer;

public class SimplePartWriter implements IPartWriter {

    private StringBuffer myBuffer = new StringBuffer();

    private static final String NL = System.getProperties().getProperty("line.separator");

    public void append(String... content) {
        for (String value : content) {
            myBuffer.append(value);
        }
    }

    public void addImport(String... importFQNs) {
        for (String value : importFQNs) {
            myBuffer.append(value);
        }
    }

    public void appendNewLine(String... content) {
        for (String value : content) {
            myBuffer.append(value);
        }
        appendNewLine();
    }

    public void appendNewLine() {
        myBuffer.append(NL);
    }

    public StringBuffer getBuffer() {
        return myBuffer;
    }

    public void append(StringBuffer buffer) {
        myBuffer.append(buffer);
    }
}
