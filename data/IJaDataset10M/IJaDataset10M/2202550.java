package net.sf.dict4j.log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class DictLogAppenderFile implements DictLogAppender {

    private PrintStream stream;

    public DictLogAppenderFile(String fileName) throws FileNotFoundException {
        stream = new PrintStream(new FileOutputStream(fileName, true));
    }

    public void appendIncoming(String string) {
        stream.println(string);
    }

    public void appendOutgoing(String string) {
        stream.println(string);
    }
}
