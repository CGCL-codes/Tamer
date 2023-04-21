package net.sf.syncopate.proxy.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FilterInputStream;

public class TeeInputStream extends FilterInputStream {

    private OutputStream out;

    public TeeInputStream(InputStream in, OutputStream out) {
        super(in);
        this.out = out;
    }

    @Override
    public int read() throws IOException {
        if (available() > 0) {
            int retVal = super.read();
            out.write(retVal);
            return retVal;
        } else {
            return 0;
        }
    }
}
