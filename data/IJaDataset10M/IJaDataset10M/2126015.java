package net.sf.maple.resources.protocols;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import net.sf.maple.resources.Fragment;

public class InputStreamProtocol extends AbstractProtocol {

    private final InputStream in;

    public InputStreamProtocol(InputStream is) {
        in = is;
    }

    public String getProtocol() {
        return "inputstream";
    }

    public InputStream openInput(Fragment path) throws Exception {
        return new FilterInputStream(in) {

            @Override
            public void close() {
            }
        };
    }

    public OutputStream openOutput(Fragment path) throws Exception {
        throw new UnsupportedOperationException();
    }

    public URL toUrl(Fragment path) throws Exception {
        throw new UnsupportedOperationException();
    }

    public Fragment[] list(Fragment path) {
        throw new UnsupportedOperationException();
    }
}
