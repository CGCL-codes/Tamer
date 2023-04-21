package jaxlib.io.stream;

import java.io.IOException;
import java.io.OutputStream;
import org.jaxlib.io.XOutputStreamTestCase;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: BufferedXOutputStreamTest.java 1190 2004-06-13 20:27:58Z joerg_wassmer $
 */
public final class BufferedXOutputStreamTest extends XOutputStreamTestCase {

    public static void main(String[] args) {
        runSuite(BufferedXOutputStreamTest.class);
    }

    public BufferedXOutputStreamTest(String name) {
        super(name);
    }

    protected XOutputStream createStream(OutputStream out) throws IOException {
        return new BufferedXOutputStream(out, 123);
    }
}
