package org.apache.mina.filter.codec.textline;

import static org.junit.Assert.assertEquals;
import java.nio.charset.Charset;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolCodecSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.junit.Test;

/**
 * Tests {@link TextLineEncoder}.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class TextLineEncoderTest {

    @Test
    public void testEncode() throws Exception {
        TextLineEncoder encoder = new TextLineEncoder(Charset.forName("UTF-8"), LineDelimiter.WINDOWS);
        ProtocolCodecSession session = new ProtocolCodecSession();
        ProtocolEncoderOutput out = session.getEncoderOutput();
        encoder.encode(session, "ABC", out);
        assertEquals(1, session.getEncoderOutputQueue().size());
        IoBuffer buf = (IoBuffer) session.getEncoderOutputQueue().poll();
        assertEquals(5, buf.remaining());
        assertEquals('A', buf.get());
        assertEquals('B', buf.get());
        assertEquals('C', buf.get());
        assertEquals('\r', buf.get());
        assertEquals('\n', buf.get());
    }
}
