package com.sun.cdc.i18n.j2me;

import java.io.Reader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/** Reader for Little Endian UTF-16 encoded input streams. */
public class UTF_16LE_Reader extends UTF_16_Reader {

    /**
     * Open the reader
     * @param in the input stream to be read
     * @param enc identifies the encoding to be used
     * @return a reader for the given input stream and encoding
     * @throws UnsupportedEncodingException
     */
    public Reader open(InputStream in, String enc) throws UnsupportedEncodingException {
        super.open(in, enc);
        byteOrder = LITTLE_ENDIAN;
        return this;
    }
}
