package com.unitt.framework.websocket;

import junit.framework.Assert;
import org.junit.Test;

public class WebSocketUtilTest {

    @Test
    public void testConvertIntToBytes() {
        byte[] bytes = new byte[] { 0x37, new Integer(0xfa).byteValue(), 0x21, 0x3d };
        int bytesIn = WebSocketUtil.convertBytesToInt(bytes, 0);
        byte[] bytesOut = WebSocketUtil.convertIntToBytes(bytesIn);
        for (int i = 0; i < 4; i++) {
            Assert.assertEquals("Byte #" + i + " is different. Should be '" + Integer.toHexString(bytes[i]) + "'. It was '" + Integer.toHexString(bytesOut[i]), bytes[i], bytesOut[i]);
        }
    }

    @Test
    public void testConvertBytesToShort() {
        short valueIn = 1000;
        byte[] bytes = WebSocketUtil.convertShortToBytes(valueIn);
        int valueOut = WebSocketUtil.convertBytesToShort(bytes, 0);
        Assert.assertEquals("Did not convert correctly.", valueIn, valueOut);
        int largeValueIn = 48000;
        bytes = WebSocketUtil.convertShortToBytes(largeValueIn);
        valueOut = WebSocketUtil.convertBytesToShort(bytes, 0);
        Assert.assertEquals("Did not convert long value correctly.", largeValueIn, valueOut);
    }
}
