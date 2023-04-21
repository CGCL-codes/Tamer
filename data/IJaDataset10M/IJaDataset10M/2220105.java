package com.itextpdf.tool.xml.html;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.html.HTMLUtils;

/**
 * @author redlab_b
 *
 */
public class HtmlUtilsTest {

    private HTMLUtils util;

    @Before
    public void setup() {
        LoggerFactory.getInstance().setLogger(new SysoLogger(3));
        util = new HTMLUtils();
    }

    @Test
    public void testRTN() {
        Assert.assertEquals("", util.sanitize("\r\n\t"));
    }

    @Test
    public void testRTNinline() {
        Assert.assertEquals("", util.sanitizeInline("\r\n\t"));
    }
}
