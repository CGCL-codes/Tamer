package org.armedbear.j;

public final class OutputBuffer extends Buffer {

    private OutputBuffer() {
        supportsUndo = false;
        type = TYPE_OUTPUT;
        mode = PlainTextMode.getMode();
        formatter = new PlainTextFormatter(this);
        lineSeparator = System.getProperty("line.separator");
        readOnly = true;
        setTransient(true);
        setProperty(Property.VERTICAL_RULE, 0);
        setProperty(Property.SHOW_LINE_NUMBERS, false);
        setProperty(Property.HIGHLIGHT_MATCHING_BRACKET, false);
        setProperty(Property.HIGHLIGHT_BRACKETS, false);
        setInitialized(true);
    }

    public static OutputBuffer getOutputBuffer(String text) {
        OutputBuffer outputBuffer = new OutputBuffer();
        outputBuffer.setText(text);
        return outputBuffer;
    }

    public int load() {
        if (!isLoaded()) {
            if (getFirstLine() == null) {
                appendLine("");
                renumber();
            }
            setLoaded(true);
        }
        return LOAD_COMPLETED;
    }

    public String getFileNameForDisplay() {
        return title != null ? title : "";
    }
}
