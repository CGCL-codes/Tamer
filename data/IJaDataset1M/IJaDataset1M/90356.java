package jxl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 *
 * @author alex
 */
public class ListFile extends HashSet<String> {

    private File myFile = null;

    private boolean readonly = false;

    /** Creates a new instance of ListFile */
    public ListFile(File file) throws IOException {
        myFile = file;
        refresh();
    }

    public ListFile(InputStream is) throws IOException {
        readonly = true;
        load(is);
    }

    private void checkReadOnly() throws IOException {
        if (readonly) throw new IOException("This list is read-only");
    }

    public boolean isReadOnly() {
        return readonly;
    }

    public void flush() throws IOException {
        checkReadOnly();
        FileWriter writer = new FileWriter(myFile);
        for (String s : this) {
            writer.write(s + "\n");
        }
        writer.flush();
        writer.close();
    }

    public void merge() throws IOException {
        checkReadOnly();
        this.addAll(new ListFile(myFile));
        flush();
    }

    public void refresh() throws IOException {
        checkReadOnly();
        load(new FileInputStream(myFile));
    }

    private void load(InputStream is) throws IOException {
        StringTokenizer tok = new StringTokenizer(new String(FileUtils.load(is, true)));
        clear();
        while (tok.hasMoreTokens()) {
            add(tok.nextToken("\n").trim());
        }
    }
}
