package gnu.saw.terminal.graphical;

import java.io.IOException;
import java.io.Writer;

public class SAWGraphicalTerminalPrintStreamWriter extends Writer {

    public void close() throws IOException {
    }

    public void flush() throws IOException {
        SAWGraphicalTerminalWriter.flush();
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        SAWGraphicalTerminalWriter.write(cbuf, off, len);
    }

    public Writer append(char c) throws IOException {
        return super.append(c);
    }

    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return super.append(csq, start, end);
    }

    public Writer append(CharSequence csq) throws IOException {
        return super.append(csq);
    }

    public void write(char[] cbuf) throws IOException {
        SAWGraphicalTerminal.write(cbuf, 0, cbuf.length);
    }

    public void write(int c) throws IOException {
        SAWGraphicalTerminal.write((char) c);
    }

    public void write(String str, int off, int len) throws IOException {
        SAWGraphicalTerminalWriter.write(str.toCharArray(), off, len);
    }

    public void write(String str) throws IOException {
        SAWGraphicalTerminalWriter.write(str);
    }
}
