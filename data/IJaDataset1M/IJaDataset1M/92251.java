package FnattMisc;

import java.io.FileReader;

public class AppendingFileReader extends FileReader {

    public AppendingFileReader(String file) throws java.io.FileNotFoundException {
        super(file);
    }

    public AppendingFileReader(java.io.File file) throws java.io.FileNotFoundException {
        super(file);
    }

    public int read(char[] cbuf, int off, int len) throws java.io.IOException {
        int res = super.read(cbuf, off, len);
        if (res < len && res < cbuf.length - 1 && res != -1) {
            cbuf[res] = '\n';
            res++;
        }
        return res;
    }
}
