package org.cheetah.core.charset.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import org.cheetah.core.charset.CharsetProvider;

public class Utf8Provider implements CharsetProvider {

    public Writer createEncoder(OutputStream out) throws IOException {
        return new Utf8Encoder(out);
    }

    public Reader createDecoder(InputStream in) throws IOException {
        return new Utf8Decoder(in);
    }
}
