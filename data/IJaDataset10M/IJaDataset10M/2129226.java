package repast.simphony.data2;

import java.io.PrintStream;
import java.util.List;

/**
 * 
 * @author Nick Collier
 */
public class ConsoleDataSink implements DataSink {

    public enum OutputStream {

        OUT, ERR
    }

    private PrintStream stream;

    private Formatter formatter;

    public ConsoleDataSink(OutputStream outputStream, Formatter formatter) {
        this.formatter = formatter;
        if (outputStream == OutputStream.OUT) stream = System.out; else stream = System.err;
    }

    @Override
    public void open(List<String> sourceIds) {
        String header = formatter.getHeader();
        if (header.length() > 0) stream.println(header);
    }

    @Override
    public void flush() {
        stream.flush();
    }

    @Override
    public void rowStarted() {
        formatter.clear();
    }

    @Override
    public void append(String key, Object value) {
        formatter.addData(key, value);
    }

    @Override
    public void rowEnded() {
        stream.println(formatter.formatData());
    }

    @Override
    public void recordEnded() {
    }

    @Override
    public void close() {
        stream.flush();
    }
}
