package org.xteam.cs.grm.build;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TableOutputStream {

    private OutputStream stream;

    public TableOutputStream(OutputStream stream) {
        this.stream = stream;
    }

    public void writeValue(int value) throws IOException {
        stream.write((value >> 8) & 0xFF);
        stream.write(value & 0xFF);
    }

    public void writeString(String str) throws IOException {
        for (int i = 0; i < str.length(); ++i) {
            stream.write(str.charAt(i));
        }
        stream.write(0);
    }

    public void close() throws IOException {
        stream.close();
    }

    public void writeTable(short[] table) throws IOException {
        writeValue(table.length);
        for (int i = 0; i < table.length; ++i) {
            writeValue(table[i]);
        }
    }

    public void writeRLETable(short[] table) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int current = 0;
        int count = 0;
        for (int i = 0; i < table.length; ++i) {
            if (i == 0 || current == table[i]) {
                current = table[i];
                ++count;
            } else {
                out.write((count >> 8) & 0xff);
                out.write(count & 0xff);
                out.write((current >> 8) & 0xff);
                out.write(current & 0xff);
                count = 1;
                current = table[i];
            }
        }
        if (count > 0) {
            out.write((count >> 8) & 0xff);
            out.write(count & 0xff);
            out.write((current >> 8) & 0xff);
            out.write(current & 0xff);
        }
        byte[] b = out.toByteArray();
        writeValue(b.length / 4);
        writeValue(table.length - 1);
        stream.write(b);
    }
}
