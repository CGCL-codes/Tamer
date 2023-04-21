package org.otfeed.protocol.message;

import static org.junit.Assert.*;
import java.nio.ByteBuffer;
import java.util.Date;
import org.junit.Test;
import org.otfeed.event.OTBookCancel;
import org.otfeed.event.OTBookDelete;
import org.otfeed.protocol.IMarshaler;

public class HistBooksRequestMarshalerTest extends MarshalerTestBase {

    @Test
    public void test() {
        String sessionId = "12345678901234567890-session-id";
        IMarshaler<HistBookRequest> marshaler = new HistBookRequestMarshaler(sessionId);
        HistBookRequest message = new HistBookRequest(123, "exchange", "symbol", new Date(1234000), new Date(3456000), OTBookCancel.class, OTBookDelete.class);
        ByteBuffer buffer = ALLOCATOR.allocate(1024);
        marshaler.marshal(message, buffer);
        buffer.flip();
        System.out.println(FORMAT.format(buffer));
        String modelBuffer = "01010000 14000000 7b000000 31323334" + "35363738 39303132 33343536 37383930" + "2d736573 73696f6e 2d696400 00000000" + "00000000 00000000 00000000 00000000" + "00000000 00000000 00000000 65786368" + "616e6765 00000000 00000073 796d626f" + "6c000000 00000000 0000d204 0000800d" + "00005000 0000";
        assertBuffersAreEqual(FORMAT.parse(modelBuffer), buffer);
        HistBookRequest echoMessage = marshaler.unmarshal(buffer);
        assertEquals(message, echoMessage);
    }

    @Test
    public void testAllTypes() {
        String sessionId = "12345678901234567890-session-id";
        IMarshaler<HistBookRequest> marshaler = new HistBookRequestMarshaler(sessionId);
        HistBookRequest message = new HistBookRequest(123, "exchange", "symbol", new Date(1234000), new Date(3456000));
        ByteBuffer buffer = ALLOCATOR.allocate(1024);
        marshaler.marshal(message, buffer);
        buffer.flip();
        System.out.println(FORMAT.format(buffer));
        String modelBuffer = "01010000 14000000 7b000000 31323334" + "35363738 39303132 33343536 37383930" + "2d736573 73696f6e 2d696400 00000000" + "00000000 00000000 00000000 00000000" + "00000000 00000000 00000000 65786368" + "616e6765 00000000 00000073 796d626f" + "6c000000 00000000 0000d204 0000800d" + "0000f00f 0000";
        assertBuffersAreEqual(FORMAT.parse(modelBuffer), buffer);
        HistBookRequest echoMessage = marshaler.unmarshal(buffer);
        assertEquals(message, echoMessage);
    }
}
