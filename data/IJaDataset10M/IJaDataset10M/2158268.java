package net.sf.cplab.gazer;

import java.io.InputStream;
import net.sf.cplab.core.util.RawBytes;

/**
 * @author jtse
 *
 */
public class IdfAsciiSample implements RawBytes {

    private InputStream in;

    public InputStream getInputStream() {
        return in;
    }

    public void setInputStream(InputStream in) {
        this.in = in;
    }
}
