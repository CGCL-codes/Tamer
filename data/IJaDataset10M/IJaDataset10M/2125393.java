package com.googlecode.compress_j2me.gzip;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class GunzipTest extends UnitTest {

    private AssertiveOutputStream baos;

    @Test
    public void testGunzipEmpty() throws IOException {
        Gzip gzip = Gzip.gunzip(file2in("samples/empty.gz"), baos = file2out("samples/empty"));
        Assert.assertEquals(baos.expectedSize(), baos.size());
        Assert.assertEquals(null, gzip.getFilename());
        Assert.assertEquals(null, gzip.getComment());
    }

    @Test
    public void testGunzipA() throws IOException {
        Gzip gzip = Gzip.gunzip(file2in("samples/a.gz"), baos = file2out("samples/a"));
        Assert.assertEquals(baos.expectedSize(), baos.size());
        Assert.assertEquals(null, gzip.getFilename());
        Assert.assertEquals(null, gzip.getComment());
    }

    @Test
    public void testGunzipABCDEx10() throws IOException {
        Gzip gzip = Gzip.gunzip(file2in("samples/ABCDEx10.gz"), baos = file2out("samples/ABCDEx10"));
        Assert.assertEquals(baos.expectedSize(), baos.size());
        Assert.assertEquals(null, gzip.getFilename());
        Assert.assertEquals(null, gzip.getComment());
    }

    @Test
    public void testGunzip0xF0FF() throws IOException {
        Gzip gzip = Gzip.gunzip(file2in("samples/0xF0FF.gz"), baos = file2out("samples/0xF0FF"));
        Assert.assertEquals(baos.expectedSize(), baos.size());
        Assert.assertEquals(null, gzip.getFilename());
        Assert.assertEquals(null, gzip.getComment());
    }

    @Test
    public void testGunzipAx10() throws IOException {
        Gzip gzip = Gzip.gunzip(file2in("samples/Ax10.txt.gz"), baos = file2out("samples/Ax10.txt"));
        Assert.assertEquals(baos.expectedSize(), baos.size());
        Assert.assertEquals(null, gzip.getFilename());
        Assert.assertEquals(null, gzip.getComment());
    }

    @Test
    public void testGunzipHelloWorldFile() throws IOException {
        Gzip gzip = Gzip.gunzip(file2in("samples/helloworld.txt.gz"), baos = file2out("samples/helloworld.txt"));
        Assert.assertEquals(baos.expectedSize(), baos.size());
        Assert.assertEquals("helloworld.txt", gzip.getFilename());
        Assert.assertEquals(null, gzip.getComment());
    }

    @Test
    public void testGunzipGoogleLogo() throws IOException {
        Gzip gzip = Gzip.gunzip(file2in("samples/google_logo.png.gz"), baos = file2out("samples/google_logo.png"));
        Assert.assertEquals(baos.expectedSize(), baos.size());
        Assert.assertEquals(null, gzip.getFilename());
        Assert.assertEquals(null, gzip.getComment());
    }

    @Test
    public void testGunzipBash() throws IOException {
        Gzip gzip = Gzip.gunzip(file2in("samples/bash.gz"), baos = file2out("samples/bash"));
        Assert.assertEquals(baos.expectedSize(), baos.size());
        Assert.assertEquals(null, gzip.getFilename());
        Assert.assertEquals(null, gzip.getComment());
    }
}
