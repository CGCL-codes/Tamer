package net.sourceforge.transumanza.writer.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import net.sourceforge.transumanza.writer.AbstractWriter;

public class IdiotFileWriter extends AbstractWriter {

    BufferedWriter out;

    String sep = System.getProperty("line.separator");

    String fileName;

    public void open() throws Exception {
        out = new BufferedWriter(new FileWriter(fileName));
    }

    public void close() throws Exception {
        out.close();
    }

    public void write(Object o) throws Exception {
        out.write(o.toString());
        out.write(sep);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
