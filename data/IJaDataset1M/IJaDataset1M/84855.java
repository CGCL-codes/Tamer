package com.google.code.http4j;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.google.code.http4j.StatusLine;
import com.google.code.http4j.impl.StatusLineParser;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 * 
 */
public final class StatusLineTestCase {

    private StatusLine s100;

    private StatusLine s200;

    private StatusLine s204;

    private StatusLine s205;

    private StatusLine s304;

    private StatusLine s404;

    @BeforeClass
    public void beforeClass() {
        s100 = createStatusLine("HTTP/1.1", 100, "Continue");
        s200 = createStatusLine("HTTP/1.0", 200, "OK");
        s204 = createStatusLine("HTTP/1.0", 204, "No Content");
        s205 = createStatusLine("HTTP/1.1", 205, "Reset Content");
        s304 = createStatusLine("HTTP/1.1", 304, "Not Modified");
        s404 = createStatusLine("HTTP/1.1", 404, "Not Found");
    }

    private StatusLine createStatusLine(String version, int statusCode, String reason) {
        return new StatusLineParser.BasicStatusLine(version, statusCode, reason);
    }

    @Test
    public void getVersion() {
        Assert.assertEquals(s100.getVersion(), "HTTP/1.1");
        Assert.assertEquals(s200.getVersion(), "HTTP/1.0");
        Assert.assertEquals(s204.getVersion(), "HTTP/1.0");
        Assert.assertEquals(s205.getVersion(), "HTTP/1.1");
        Assert.assertEquals(s304.getVersion(), "HTTP/1.1");
        Assert.assertEquals(s404.getVersion(), "HTTP/1.1");
    }

    @Test
    public void getStatusCode() {
        Assert.assertEquals(s100.getStatusCode(), 100);
        Assert.assertEquals(s200.getStatusCode(), 200);
        Assert.assertEquals(s204.getStatusCode(), 204);
        Assert.assertEquals(s205.getStatusCode(), 205);
        Assert.assertEquals(s304.getStatusCode(), 304);
        Assert.assertEquals(s404.getStatusCode(), 404);
    }

    @Test
    public void getReason() {
        Assert.assertEquals(s100.getReason(), "Continue");
        Assert.assertEquals(s200.getReason(), "OK");
        Assert.assertEquals(s204.getReason(), "No Content");
        Assert.assertEquals(s205.getReason(), "Reset Content");
        Assert.assertEquals(s304.getReason(), "Not Modified");
        Assert.assertEquals(s404.getReason(), "Not Found");
    }

    @Test
    public void hasEntity() {
        Assert.assertFalse(s100.hasEntity());
        Assert.assertTrue(s200.hasEntity());
        Assert.assertFalse(s204.hasEntity());
        Assert.assertFalse(s205.hasEntity());
        Assert.assertFalse(s304.hasEntity());
        Assert.assertTrue(s404.hasEntity());
    }
}
