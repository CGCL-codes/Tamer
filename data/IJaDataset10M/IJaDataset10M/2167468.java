package com.siberhus.easyexecutor.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import com.siberhus.easyexecutor.ApplicationInfo;
import com.siberhus.easyexecutor.EasyExecutor;

public class EasyExecutorFooConfigTest {

    @BeforeClass
    public static void init() throws Throwable {
        String args[] = new String[] { "classpath:foo-config.xml", "cat", "dog", "donkey" };
        EasyExecutor.main(args);
    }

    @Test
    public void testUserProperties() throws Exception {
        Properties fooProps = EasyExecutor.getInstance().getProperties("foo");
        Assert.assertEquals("Foo", fooProps.getProperty("name"));
    }

    @Test
    public void testApplicationInfo() {
        ApplicationInfo appInfo = EasyExecutor.getInstance().getApplicationInfo();
        Assert.assertEquals("foo-app", appInfo.getId());
        Assert.assertEquals("test", appInfo.getMode());
        Assert.assertEquals("Foo Application", appInfo.getName());
        Assert.assertEquals("0.1", appInfo.getVersion());
        Assert.assertEquals("Trivial Description", appInfo.getDescription());
    }

    @Test
    public void testLocale() throws ParseException {
        EasyExecutor ee = EasyExecutor.getInstance();
        Assert.assertEquals("en", ee.getLocale().getLanguage());
        Assert.assertEquals("US", ee.getLocale().getCountry());
        Locale lth = new Locale("en", "US");
        Assert.assertEquals(lth, ee.getLocale());
        Assert.assertEquals(lth, Locale.getDefault());
        SimpleDateFormat sdfEn = new SimpleDateFormat("dd/MM/yyyy");
        Date dateEn = sdfEn.parse("29/12/1982");
        SimpleDateFormat sdfTh = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH"));
        Date dateTh = sdfTh.parse("29/12/2525");
        Assert.assertEquals(dateEn, dateTh);
    }
}
