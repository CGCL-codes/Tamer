package org.gjt.jclasslib.io;

import java.io.DataInputStream;
import java.io.InputStream;

/**
    <tt>DataInputStream</tt> which extends <tt>ByteCodeInput</tt>.

    @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
    @version $Revision: 1.4 $ $Date: 2003/08/18 07:58:12 $
*/
public class ByteCodeInputStream extends DataInputStream implements ByteCodeInput {

    /**
        Constructor.
        @param in the input stream.
     */
    public ByteCodeInputStream(InputStream in) {
        super(new CountedInputStream(in));
    }

    public int getBytesRead() {
        return ((CountedInputStream) in).getBytesRead();
    }
}
