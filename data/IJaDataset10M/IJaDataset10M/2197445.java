package info.jonclark.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * Write lines of a file coming from a multi-threaded application in a specified
 * order. This class is thread-safe.
 */
public class AsyncPrintWriter {

    final Vector<String> vLines;

    final PrintWriter out;

    private static final long WAIT_FOR_LINES = 250;

    boolean isWriting = false;

    public AsyncPrintWriter(int nInitialLines, File outputFile) throws FileNotFoundException {
        vLines = new Vector<String>(nInitialLines);
        out = new PrintWriter(outputFile);
    }

    /**
         * @param line
         * @param nRow
         *                ZERO-BASED row number
         */
    public void println(String line, int nRow) {
        synchronized (vLines) {
            if (vLines.size() < nRow + 1) {
                vLines.setSize(nRow + 1);
            }
            vLines.setElementAt(line, nRow);
        }
    }

    public void beginAsyncWrite() {
        if (isWriting) {
            ;
        } else {
            isWriting = true;
            Thread thread = new Thread(writer);
            thread.start();
        }
    }

    private Runnable writer = new Runnable() {

        public void run() {
            int i = 0;
            while (isWriting) {
                String line;
                synchronized (vLines) {
                    if (i >= vLines.size()) {
                        line = null;
                    } else {
                        line = vLines.get(i);
                    }
                }
                if (line == null) {
                    try {
                        Thread.sleep(WAIT_FOR_LINES);
                    } catch (InterruptedException e) {
                        ;
                    }
                } else {
                    out.println(line);
                    vLines.set(i, null);
                    i++;
                }
            }
            synchronized (vLines) {
                while (i < vLines.size()) {
                    String line = vLines.get(i);
                    if (line == null) {
                        out.println();
                    } else {
                        out.println(line);
                    }
                    i++;
                }
            }
            out.flush();
            out.close();
        }
    };

    public void close() {
        isWriting = false;
    }

    public static void main(String[] args) throws Exception {
        AsyncPrintWriter writer = new AsyncPrintWriter(500, new File("c:/test.txt"));
        writer.beginAsyncWrite();
        Thread.sleep(500);
        writer.println("0", 0);
        Thread.sleep(500);
        writer.println("C", 3);
        Thread.sleep(500);
        writer.println("D", 4);
        Thread.sleep(500);
        writer.println("E", 5);
        Thread.sleep(500);
        writer.println("A", 1);
        Thread.sleep(500);
        writer.println("B", 2);
        Thread.sleep(5000);
        writer.println("F", 6);
        Thread.sleep(500);
        writer.println("G", 7);
        Thread.sleep(500);
        writer.println("H", 8);
        Thread.sleep(500);
        writer.close();
    }
}
