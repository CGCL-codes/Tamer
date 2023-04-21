package com.coladoro.plugin.ed2k.tests.protocol;

import static org.testng.AssertJUnit.assertEquals;
import java.net.InetAddress;
import java.nio.ByteOrder;
import java.util.zip.Deflater;
import org.apache.mina.common.IoBuffer;
import org.apache.mina.common.IoSession;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import com.coladoro.plugin.ed2k.protocol.Ed2kHelloMessage;
import com.coladoro.plugin.ed2k.protocol.Ed2kHelloResponseMessage;
import com.coladoro.plugin.ed2k.protocol.Ed2kIntegerTag;
import com.coladoro.plugin.ed2k.protocol.Ed2kMessage;
import com.coladoro.plugin.ed2k.protocol.Ed2kStringTag;
import com.coladoro.plugin.ed2k.protocol.decoders.Ed2kHelloResponseDecoder;
import com.coladoro.plugin.ed2k.utils.Utils;

/**
 * @author tanis
 */
public class Ed2kHelloMessageResponseTest {

    /**
     * Logger of the test.
     */
    final Logger logger = LoggerFactory.getLogger(Ed2kHelloMessageResponseTest.class);

    @Test
    public void testDecoding() throws Exception {
        Mockery context = new JUnit4Mockery();
        final IoSession session = context.mock(IoSession.class);
        Ed2kProtocolDecoderOutputForTesting output = new Ed2kProtocolDecoderOutputForTesting();
        String hexMessage = "e3540000004caa3a92c6330e67b4cbc1eb1bc6836f0f575a0c4236120600000002010001050067617a6f75030100113c000000030100f940120000030100fa1e421334030100feb5040000030100fb00c00000c18addd69210";
        byte[] message = Utils.toByteArray(hexMessage);
        Ed2kHelloResponseDecoder decoder = new Ed2kHelloResponseDecoder(Deflater.BEST_COMPRESSION);
        IoBuffer buffer = IoBuffer.allocate(message.length);
        buffer.setAutoExpand(true);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(message);
        buffer.rewind();
        decoder.decode(session, buffer, output);
        Ed2kHelloMessage decodedMessage = (Ed2kHelloMessage) output.getMessageQueue().element();
        logger.info("Hello message: " + decodedMessage);
        assertEquals("AA3A92C6330E67B4CBC1EB1BC6836F0F", decodedMessage.getClientHash().toUpperCase());
        assertEquals(Ed2kMessage.HELLO_RESPONSE_MESSAGE, decodedMessage.getType());
        assertEquals(0xE3, decodedMessage.getProtocol());
        assertEquals(84, decodedMessage.getSize());
        assertEquals(InetAddress.getByName("87.90.12.66").getAddress(), decodedMessage.getClientId());
        assertEquals(4662, decodedMessage.getTcpPort());
        assertEquals(6, decodedMessage.getTagList().size());
        assertEquals(new Ed2kStringTag((short) 0x01, "gazou"), decodedMessage.getTagList().get(0));
        assertEquals(new Ed2kIntegerTag((short) 0x11, 60), decodedMessage.getTagList().get(1));
        assertEquals(new Ed2kIntegerTag((short) 0xF9, 4672), decodedMessage.getTagList().get(2));
        assertEquals(new Ed2kIntegerTag((short) 0xFA, 873677342), decodedMessage.getTagList().get(3));
        assertEquals(new Ed2kIntegerTag((short) 0xFE, 1205), decodedMessage.getTagList().get(4));
        assertEquals(new Ed2kIntegerTag((short) 0xFB, 49152), decodedMessage.getTagList().get(5));
        assertEquals(InetAddress.getByName("193.138.221.214").getAddress(), decodedMessage.getServerIp());
        assertEquals(4242, decodedMessage.getServerPort());
    }

    @Test
    public void testEncoding() throws Exception {
        Ed2kHelloResponseMessage hello = new Ed2kHelloResponseMessage();
        hello.setClientHash(Utils.toByteArray("A92989DAE00EE54731B67D38408C6FCA"));
        hello.setClientId("213.96.214.211");
        hello.setTcpPort(18887);
        hello.setClientName("http://emule-project.net");
        hello.setServerIp(InetAddress.getByName("193.138.221.214").getAddress());
        hello.setServerPort(4242);
        byte[] messageEncoded = hello.encode();
        logger.info("Message: " + Utils.toHexString(messageEncoded));
        assertEquals("e3670000004ca92989dae00ee54731b67d38408c6fcad560d6d3c74906000000020100011800687474703a2f2f656d756c652d70726f6a6563742e6e6574030100113c000000030100f9cb490000030100fa10424304030100fe80030000030100fb00ab0000c18addd69210", Utils.toHexString(messageEncoded).toLowerCase());
    }
}
