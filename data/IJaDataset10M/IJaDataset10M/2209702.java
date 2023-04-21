package net.community.chest.io.encode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <P>Copyright GPLv2</P>
 *
 * @param <V> Type of element being encoded/decoded
 * @author Lyor G.
 * @since Jun 15, 2009 1:54:42 PM
 */
public interface ElementEncoder<V> {

    V read(InputStream in) throws IOException;

    void write(OutputStream out) throws IOException;
}
