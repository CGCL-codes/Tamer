package java.io;

/**
 * @author "Michael Maaser"
 * 
 */
public abstract class Writer {

    protected Object lock;

    protected Writer() {
    }

    protected Writer(Object lock) {
        this.lock = lock;
    }

    public abstract void close() throws IOException;

    public abstract void flush() throws IOException;

    public void write(char[] cbuf) throws IOException {
    }

    public abstract void write(char[] cbuf, int off, int len) throws IOException;

    public void write(int c) throws IOException {
    }

    public void write(String str) throws IOException {
    }

    public void write(String str, int off, int len) throws IOException {
    }
}
