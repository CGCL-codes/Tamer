package cx.fbn.nevernote.utilities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import cx.fbn.nevernote.Global;

public class OutStream extends FilterOutputStream {

    List<String> buffer;

    File file;

    FileOutputStream fos;

    DataOutputStream dos;

    public OutStream(OutputStream out, String name) {
        super(out);
        buffer = new ArrayList<String>();
        file = Global.getFileManager().getLogsDirFile(name);
        try {
            fos = new FileOutputStream(file);
            dos = new DataOutputStream(fos);
        } catch (FileNotFoundException e) {
        }
    }

    @Override
    public synchronized void write(byte b[]) throws IOException {
        String aString = new String(b);
        buffer.add(aString);
        dos.writeBytes(aString + "\n");
    }

    @Override
    public synchronized void write(byte b[], int off, int len) throws IOException {
        String aString = new String(b, off, len);
        buffer.add(aString);
        dos.writeBytes(aString + "\n");
    }

    public List<String> getText() {
        return buffer;
    }
}
