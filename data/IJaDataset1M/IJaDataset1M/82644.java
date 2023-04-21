package ru.adv.test.util.siteconverter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.adv.test.AbstractTest;
import ru.adv.util.siteconverter.SiteConverter;

public class SiteConverterTest extends AbstractTest {

    String webhostDir;

    String outputDir;

    @Before
    public void init() {
        webhostDir = "/adv/vhosts/www.adv.ru";
        outputDir = "tmp/SiteConverter";
    }

    @Test
    @Ignore("Not for automatic testing")
    public void testCreateDistr() throws Exception {
        try {
            SiteConverter siteConverter = new SiteConverter();
            siteConverter.makeMozart3Distr(webhostDir, outputDir);
        } catch (Throwable t) {
            t.printStackTrace();
            fail();
        }
    }
}
