package org.aspectme.instrument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Representation of a class file. A class file has a name and contains bytecode.
 * 
 * @author Magnus Robertsson
 */
public class URLClassFile extends ClassFile {

    private URL url;

    public URLClassFile(String name, String url) throws MalformedURLException {
        super(name);
        this.url = new URL(url);
    }

    public byte[] getByteCode() throws IOException {
        InputStream in = null;
        ByteArrayOutputStream buf = new ByteArrayOutputStream(2048);
        try {
            in = url.openStream();
            int b = in.read();
            while (b != -1) {
                buf.write(b);
                b = in.read();
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return buf.toByteArray();
    }
}
