package com.google.code.jahath.common;

import java.io.ByteArrayInputStream;
import org.junit.Assert;
import org.junit.Test;

public class CRLFInputStreamTest {

    @Test
    public void testReadLine() throws Exception {
        CRLFInputStream in = new CRLFInputStream(new ByteArrayInputStream("test1\r\ntest2\r\n".getBytes("ascii")));
        Assert.assertEquals("test1", in.readLine());
        Assert.assertEquals("test2", in.readLine());
    }

    @Test
    public void testRead() throws Exception {
        CRLFInputStream in = new CRLFInputStream(new ByteArrayInputStream("test\r\nABCDEFGHI".getBytes("ascii")));
        Assert.assertEquals("test", in.readLine());
        byte[] buffer = new byte[4];
        Assert.assertEquals(4, in.read(buffer));
        Assert.assertEquals('A', buffer[0]);
        Assert.assertEquals(4, in.read(buffer));
        Assert.assertEquals('E', buffer[0]);
        Assert.assertEquals(1, in.read(buffer));
        Assert.assertEquals('I', buffer[0]);
    }
}
