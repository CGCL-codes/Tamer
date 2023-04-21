package org.tcpfile.net;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcpfile.fileio.FileHandling;
import org.tcpfile.main.Misc;
import org.tcpfile.net.messages.IMMessage;

public class ByteArrayTest {

    private static Logger log = LoggerFactory.getLogger(ByteArrayTest.class);

    byte[] b = FileHandling.readFileToByteArray("C:/msdia80.dll");

    static byte[] lzma;

    static byte[] b2zip;

    static byte[] gzip;

    static byte[] zip;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void testzip() {
        zip = ByteArray.zip(b);
        Misc.echo("zip: " + zip.length);
        assertNotNull(zip);
    }

    @Test
    public void testunzip() {
        byte[] b2 = ByteArray.unzip(zip);
        assertArrayEquals(b, b2);
    }

    @Test
    public void testgzip() {
        gzip = ByteArray.gzip(b);
        Misc.echo("gzip: " + gzip.length);
        assertNotNull(gzip);
    }

    @Test
    public void testgunzip() {
        byte[] b2 = ByteArray.gunzip(gzip);
        assertArrayEquals(b, b2);
    }

    @Test
    public void testConcat() {
        byte[] bb1 = ByteArray.getRandomBytes(3825);
        byte[] bb2 = ByteArray.getRandomBytes(328);
        byte[] bb3 = ByteArray.concat(bb1, bb2);
        assertArrayEquals(ByteArray.copyfromto(bb3, 0, bb1.length), bb1);
        assert (bb3.length == bb1.length + bb2.length);
        assertArrayEquals(ByteArray.copyfromto(bb3, bb1.length), bb2);
    }

    @Test
    public void testToByteArray() {
        Misc.echo("Start speed");
        IMMessage im = new IMMessage("Hi everybody");
        for (int i = 0; i < 10000; i++) ByteArray.toByteArray(im);
        Misc.echo("Stop speed");
        IMMessage im2 = new IMMessage("ja");
        IMMessage im3 = new IMMessage("nei");
        Misc.echo(new String(ByteArray.toByteArray(im2)));
        Misc.echo(new String((ByteArray.toByteArray(im3))));
        Misc.echo((ByteArray.toByteArray(im2)));
        Misc.echo((ByteArray.toByteArray(im3)));
        Misc.echo(ByteArray.gzip(ByteArray.toByteArray(im2)));
        Misc.echo(ByteArray.gzip(ByteArray.toByteArray(im3)));
    }

    @Test
    public void testToObject() {
        IMMessage im = new IMMessage("Hi everybody");
        Object o = 5645;
        byte[] obj = ByteArray.toByteArray(o);
        Misc.echo("Start speed");
        for (int i = 0; i < 10000; i++) {
            assert (ByteArray.toObject(obj).equals(o));
        }
        Misc.echo("Stop speed");
    }
}
