package org.equanda.util.io;

import javolution.lang.TextBuilder;
import java.io.IOException;
import java.io.Writer;

/**
 * Writer implementation which appends into an existing (javolution) TextBuilder
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class TextBuilderWriter extends Writer {

    private TextBuilder tb;

    public TextBuilderWriter(TextBuilder builder) {
        tb = builder;
    }

    public void write(char[] chars, int start, int end) throws IOException {
        tb.append(chars, start, end);
    }

    public void flush() throws IOException {
    }

    public void close() throws IOException {
    }
}
