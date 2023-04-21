package jaxlib.io.stream;

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;

/**
 * TODO: comment
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: FilterXInputStream.java,v 1.3 2004/09/14 19:59:39 joerg_wassmer Exp $
 */
public abstract class FilterXInputStream extends XInputStream {

    protected InputStream in;

    protected FilterXInputStream(InputStream in) {
        super();
        this.in = in;
    }

    @Override
    public int available() throws IOException {
        if (this.in == null) throw new ClosedChannelException();
        return this.in.available();
    }

    @Override
    public void close() throws IOException {
        InputStream in = this.in;
        if (in != null) {
            closeInstance();
            in.close();
        }
    }

    @Override
    public void closeInstance() throws IOException {
        this.in = null;
    }

    @Override
    public boolean isOpen() {
        return this.in != null;
    }
}
