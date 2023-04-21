package com.goodcodeisbeautiful.archtea.io.data.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class TXT2BINDataFilter extends AbstractDataFilter {

    private static final String PROP_DEFAULT_CHARSET = "default.charset";

    private static final String DEFAULT_CHARSET = "ISO-8859-1";

    @Override
    public boolean acceptBinaryStream() {
        return false;
    }

    @Override
    public boolean returnBinaryStream() {
        return true;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        Reader reader = getSource().getReader();
        String charset = getSource().getCharset();
        if (charset == null) charset = getProperties().getProperty(PROP_DEFAULT_CHARSET);
        if (charset == null) charset = DEFAULT_CHARSET;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bOut, charset);
        char[] buff = new char[1024];
        int len = reader.read(buff, 0, buff.length);
        while (len != -1) {
            writer.write(buff, 0, len);
            len = reader.read(buff, 0, buff.length);
        }
        writer.close();
        return new ByteArrayInputStream(bOut.toByteArray());
    }

    @Override
    public String getDescription() {
        return "Converter from Text to Binary";
    }
}
