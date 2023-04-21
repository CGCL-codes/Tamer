package net.innig.io;

import java.io.InputStream;

public class NullInputStream extends InputStream {

    public int read() {
        return -1;
    }
}
