package org.matsim.pt.counts;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import org.matsim.core.utils.io.IOUtils;

/**
 * a small and simple writer, in order to avoid copious "try" and "catch" for
 * "Exception"
 * 
 * @author yChen
 * 
 */
public class SimpleWriter implements Closeable, Flushable {

    private BufferedWriter writer = null;

    public SimpleWriter(final String outputFilename) {
        writer = IOUtils.getBufferedWriter(outputFilename);
    }

    public SimpleWriter(String outputFilename, String contents2write) {
        writer = IOUtils.getBufferedWriter(outputFilename);
        write(contents2write);
        close();
    }

    public void write(char[] c) {
        if (writer != null) try {
            writer.write(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(char c) {
        if (writer != null) try {
            writer.write(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String s) {
        if (writer != null) {
            try {
                writer.write(s);
            } catch (IOException e) {
                System.err.println("writer was not initialized yet!");
                e.printStackTrace();
            }
        }
    }

    public void write(Object o) {
        write(o.toString());
    }

    public void writeln(String s) {
        write(s + "\n");
    }

    public void writeln(Object o) {
        write(o + "\n");
    }

    public void writeln() {
        write('\n');
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeln(StringBuffer line) {
        writeln(line.toString());
    }
}
