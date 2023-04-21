package org.tnt.ikaixin.util;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author TNT
 *
 */
public class ConfigurationManagerTest {

    private static Log log = LogFactory.getLog(ConfigurationManagerTest.class);

    private ConfigurationManager mgr;

    private String testConfFile = "Test" + File.separator + "ikaixin.conf";

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        ConfigurationManager.setConfFile(testConfFile);
        mgr = ConfigurationManager.getInstance();
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    public final void testGetValue() {
        Assert.assertEquals("", mgr.getValue("buyfriends.1blacklist"));
        Assert.assertEquals("", mgr.getValue("buyfriends.blacklist"));
        Assert.assertTrue(mgr.getValue("buyfriends.list").length() > 0);
    }

    @Test
    public final void testGetList() {
        Assert.assertTrue(mgr.getValue("buyfriends.list").length() > 0);
        log.info(mgr.getValue("buyfriends.list"));
        log.info(mgr.getBuyFriendsList().length);
    }

    public final void testGetValueRealTime() {
        Assert.assertEquals("3", mgr.getValue(ConfigurationManager.AJAX_TIMEOUT_STR));
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(testConfFile));
            props.setProperty(ConfigurationManager.AJAX_TIMEOUT_STR, "5");
            props.store(new FileOutputStream(testConfFile), "");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            Assert.assertTrue("5".equals(mgr.getValue(ConfigurationManager.AJAX_TIMEOUT_STR)));
            props.setProperty(ConfigurationManager.AJAX_TIMEOUT_STR, "3");
            props.store(new FileOutputStream(testConfFile), "");
        } catch (Exception e1) {
            fail("Unexpected Exception throwed!");
            e1.printStackTrace();
        }
    }

    public final void testGetBuyFriendsList() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(testConfFile));
            String listStr = "haha,hehe,xixi";
            props.setProperty(ConfigurationManager.BUYFRIEND_LIST_STR, listStr);
            props.store(new FileOutputStream(testConfFile), "");
            Assert.assertEquals(3, mgr.getBuyFriendsList().length);
            Assert.assertEquals(listStr.split(",")[0], mgr.getBuyFriendsList()[0]);
            Assert.assertEquals(listStr.split(",")[1], mgr.getBuyFriendsList()[1]);
            Assert.assertEquals(listStr.split(",")[2], mgr.getBuyFriendsList()[2]);
        } catch (Exception e) {
            fail("Unexcepted Exception throwed.");
        }
    }
}
