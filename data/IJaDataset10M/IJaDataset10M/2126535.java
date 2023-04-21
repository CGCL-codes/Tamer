package com.sksdpt.kioskjui.control.irda;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import com.sksdpt.kioskjui.bean.SendFileBean;
import com.sksdpt.kioskjui.control.config.DSess;

/**
 * IrdaSenderTest
 * 
 * @author "Luis Alfonso Vega Garcia" <vegacom@gmail.com>
 */
public class IrdaSenderTest extends TestCase {

    private static Logger logger = Logger.getLogger(IrdaSenderTest.class);

    /**
     * Test method for
     * {@link com.sksdpt.kioskjui.control.irda.IrdaSender#send(com.sksdpt.kioskjui.bean.SendFileBean)}.
     */
    public void testSend() {
        SendFileBean sendBean = new SendFileBean();
        sendBean.setFnamePath("/etc/fstab");
        sendBean.setMime("text/plain");
        IrdaSender sender = new IrdaSender();
        logger.info(sender);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DSess.sngltn();
    }
}
