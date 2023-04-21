package net.frontlinesms.smsdevice.internet;

import net.frontlinesms.AbstractTestCase;
import net.frontlinesms.data.*;
import net.frontlinesms.data.domain.*;
import net.frontlinesms.smsdevice.internet.AbstractSmsInternetService;

/**
 * Tests send/receiving for internet services
 * 
 * @author Carlos Eduardo Endler Genz
 * @date 19/02/2009
 */
public abstract class AbstractInternetServiceTestCase extends AbstractTestCase {

    private AbstractSmsInternetService internetService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        internetService = init();
    }

    protected abstract AbstractSmsInternetService init() throws DuplicateKeyException;

    protected abstract void executeReceiving(AbstractSmsInternetService internetService);

    protected abstract Message getMessageDetails();

    /**
	 * Test method for {@link net.frontlinesms.smsdevice.internet.AbstractSmsInternetService#sendSmsDirect(net.frontlinesms.data.Message)}.
	 */
    public void testSendSmsDirect() {
        Message message = getMessageDetails();
        assertNotNull(message);
        internetService.sendSmsDirect(message);
        assertEquals(message.getStatus(), Message.STATUS_SENT);
    }

    /**
	 * Test method for {@link net.frontlinesms.smsdevice.internet.AbstractSmsInternetService#receiveSms()}.
	 */
    public void testReceiveSms() {
        executeReceiving(internetService);
    }
}
