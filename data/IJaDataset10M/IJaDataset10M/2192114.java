package org.commsuite.devices.fax;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createStrictMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import gnu.hylafax.HylaFAXClient;
import gnu.hylafax.Job;
import gnu.inet.ftp.ServerResponseException;
import java.io.IOException;
import java.io.InputStream;
import org.commsuite.devices.DeviceInitializationFailedException;
import org.commsuite.devices.OutboundMessageConversionFailedException;
import org.commsuite.devices.OutboundMessageInvalidContentException;
import org.commsuite.devices.OutboundMessageInvalidContentMimeTypeException;
import org.commsuite.devices.OutboundMessageInvalidDestinationAddressException;
import org.commsuite.devices.OutboundMessageSendException;
import org.commsuite.testing.AbstractCommunicationsSuiteTestCase;

/**
 * @author Rafał Malinowski
 */
public class FaxOutboundMessageTest extends AbstractCommunicationsSuiteTestCase {

    public void testConstructor() throws ServerResponseException, IOException {
        FaxDevice device = createStrictMock(FaxDevice.class);
        replay(device);
        FaxOutboundMessage message = new FaxOutboundMessage(device);
        assertNotNull("device is null", message.getDevice());
        assertSame("devices does not matchs", device, message.getDevice());
        assertNull("message id should be null, it is now created in Device::send", message.getMessageId());
        verify(device);
    }

    public void testMimeType() throws ServerResponseException, IOException {
        FaxDevice device = createStrictMock(FaxDevice.class);
        replay(device);
        FaxOutboundMessage message = new FaxOutboundMessage(device);
        assertTrue("fax should accept application/postscript", message.setContentMimeType("application/postscript"));
        assertTrue("fax should accept application/pdf", message.setContentMimeType("application/pdf"));
        assertTrue("fax should accept image/tiff", message.setContentMimeType("image/tiff"));
        assertTrue("fax should accept image/tiff-fx", message.setContentMimeType("image/tiff-fx"));
        assertTrue("fax should accept image/png", message.setContentMimeType("image/png"));
        assertTrue("fax should accept text/plain", message.setContentMimeType("text/plain"));
        assertFalse("fax should not accept text/css", message.setContentMimeType("text/css"));
        assertFalse("fax should not accept text", message.setContentMimeType("text"));
        assertFalse("fax should not accept text/", message.setContentMimeType("text/"));
        assertTrue("fax should accept image/jpeg", message.setContentMimeType("image/jpeg"));
        assertFalse("fax should not accept video/mpeg", message.setContentMimeType("video/mpeg"));
        assertFalse("fax should not accept [null]", message.setContentMimeType(null));
        verify(device);
    }

    public void testSendOk() throws ServerResponseException, IOException, OutboundMessageInvalidContentMimeTypeException, OutboundMessageInvalidContentException, OutboundMessageInvalidDestinationAddressException, OutboundMessageSendException, OutboundMessageConversionFailedException, DeviceInitializationFailedException {
        byte[] input = new byte[] { 1 };
        FaxDevice device = createStrictMock(FaxDevice.class);
        HylaFAXClient hylafax = createStrictMock(HylaFAXClient.class);
        Job hylafaxJob = createStrictMock(Job.class);
        hylafaxJob.getId();
        expectLastCall().andReturn(1);
        hylafaxJob.addDocument("fakeDocument1");
        hylafaxJob.setDialstring("+48 61 8417419");
        hylafaxJob.setMaximumTries(1);
        hylafaxJob.setKilltime("000159");
        hylafax.createJob();
        expectLastCall().andReturn(hylafaxJob);
        hylafax.putTemporary((InputStream) anyObject());
        expectLastCall().andReturn("fakeDocument1");
        hylafax.submit(hylafaxJob);
        device.getConnection();
        expectLastCall().andReturn(hylafax);
        device.closeConnection(hylafax);
        replay(device);
        replay(hylafax);
        replay(hylafaxJob);
        FaxOutboundMessage message = new FaxOutboundMessage(device);
        message.setContentMimeType("text/plain");
        message.setContent(input);
        message.setDestinationAddress("+48 61 8417419");
        message.send();
        assertEquals("messageId should be 1", "1", message.getMessageId());
        verify(device);
        verify(hylafax);
        verify(hylafaxJob);
    }

    public void testSendNoContent() throws ServerResponseException, IOException, OutboundMessageInvalidContentMimeTypeException, OutboundMessageInvalidDestinationAddressException, OutboundMessageSendException, OutboundMessageConversionFailedException {
        FaxDevice device = createStrictMock(FaxDevice.class);
        replay(device);
        FaxOutboundMessage message = new FaxOutboundMessage(device);
        message.setContentMimeType("text/plain");
        message.setDestinationAddress("+48 61 8417419");
        try {
            message.send();
            fail("OutboundMessageInvalidContentException expected");
        } catch (OutboundMessageInvalidContentException e) {
        }
        verify(device);
    }

    public void testSendNoContentMimeType() throws ServerResponseException, IOException, OutboundMessageInvalidContentException, OutboundMessageInvalidDestinationAddressException, OutboundMessageSendException, OutboundMessageConversionFailedException {
        byte[] input = new byte[] { 1 };
        FaxDevice device = createStrictMock(FaxDevice.class);
        replay(device);
        FaxOutboundMessage message = new FaxOutboundMessage(device);
        message.setContent(input);
        message.setDestinationAddress("+48 61 8417419");
        try {
            message.send();
            fail("OutboundMessageInvalidContentMimeTypeException expected");
        } catch (OutboundMessageInvalidContentMimeTypeException e) {
        }
        verify(device);
    }

    public void testSendNoDestination() throws ServerResponseException, IOException, OutboundMessageInvalidContentMimeTypeException, OutboundMessageInvalidContentException, OutboundMessageSendException, OutboundMessageConversionFailedException {
        byte[] input = new byte[] { 1 };
        FaxDevice device = createStrictMock(FaxDevice.class);
        replay(device);
        FaxOutboundMessage message = new FaxOutboundMessage(device);
        message.setContentMimeType("text/plain");
        message.setContent(input);
        try {
            message.send();
            fail("OutboundMessageInvalidDestinationAddressException expected");
        } catch (OutboundMessageInvalidDestinationAddressException e) {
        }
        verify(device);
    }

    public void testSendServerFailure() throws ServerResponseException, IOException, OutboundMessageInvalidContentMimeTypeException, OutboundMessageInvalidContentException, OutboundMessageInvalidDestinationAddressException, OutboundMessageSendException, OutboundMessageConversionFailedException, DeviceInitializationFailedException {
        byte[] input = new byte[] { 1 };
        FaxDevice device = createStrictMock(FaxDevice.class);
        HylaFAXClient hylafax = createStrictMock(HylaFAXClient.class);
        Job hylafaxJob = createStrictMock(Job.class);
        hylafaxJob.getId();
        expectLastCall().andReturn(1);
        hylafaxJob.addDocument("fakeDocument1");
        hylafaxJob.setDialstring("+48 61 8417419");
        hylafaxJob.setMaximumTries(1);
        hylafaxJob.setKilltime("000159");
        hylafax.createJob();
        expectLastCall().andReturn(hylafaxJob);
        hylafax.putTemporary((InputStream) anyObject());
        expectLastCall().andReturn("fakeDocument1");
        hylafax.submit(hylafaxJob);
        expectLastCall().andThrow(new ServerResponseException(""));
        device.getConnection();
        expectLastCall().andReturn(hylafax);
        replay(device);
        replay(hylafax);
        replay(hylafaxJob);
        FaxOutboundMessage message = new FaxOutboundMessage(device);
        message.setContentMimeType("text/plain");
        message.setContent(input);
        message.setDestinationAddress("+48 61 8417419");
        try {
            message.send();
            fail("OutboundMessageSendException expected");
        } catch (OutboundMessageSendException e) {
        }
        verify(device);
        verify(hylafax);
        verify(hylafaxJob);
    }

    public void testDoubleSend() throws ServerResponseException, IOException, OutboundMessageInvalidContentMimeTypeException, OutboundMessageInvalidContentException, OutboundMessageInvalidDestinationAddressException, OutboundMessageSendException, OutboundMessageConversionFailedException, DeviceInitializationFailedException {
        byte[] input = new byte[] { 1 };
        FaxDevice device = createStrictMock(FaxDevice.class);
        HylaFAXClient hylafax = createStrictMock(HylaFAXClient.class);
        Job hylafaxJob1 = createStrictMock(Job.class);
        Job hylafaxJob2 = createStrictMock(Job.class);
        hylafaxJob1.getId();
        expectLastCall().andReturn(1);
        hylafaxJob1.addDocument("fakeDocument1");
        hylafaxJob1.setDialstring("+48 61 8417419");
        hylafaxJob1.setMaximumTries(1);
        hylafaxJob1.setKilltime("000159");
        hylafaxJob2.getId();
        expectLastCall().andReturn(2);
        hylafaxJob2.addDocument("fakeDocument1");
        hylafaxJob2.setDialstring("+48 61 8417419");
        hylafaxJob2.setMaximumTries(1);
        hylafaxJob2.setKilltime("000159");
        hylafax.createJob();
        expectLastCall().andReturn(hylafaxJob1);
        hylafax.putTemporary((InputStream) anyObject());
        expectLastCall().andReturn("fakeDocument1");
        hylafax.submit(hylafaxJob1);
        hylafax.createJob();
        expectLastCall().andReturn(hylafaxJob2);
        hylafax.putTemporary((InputStream) anyObject());
        expectLastCall().andReturn("fakeDocument1");
        hylafax.submit(hylafaxJob2);
        device.getConnection();
        expectLastCall().andReturn(hylafax);
        device.closeConnection(hylafax);
        device.getConnection();
        expectLastCall().andReturn(hylafax);
        device.closeConnection(hylafax);
        replay(device);
        replay(hylafax);
        replay(hylafaxJob1);
        replay(hylafaxJob2);
        FaxOutboundMessage message = new FaxOutboundMessage(device);
        message.setContentMimeType("text/plain");
        message.setContent(input);
        message.setDestinationAddress("+48 61 8417419");
        message.send();
        assertEquals("messageId should be 1", "1", message.getMessageId());
        message.send();
        assertEquals("messageId should be 2", "2", message.getMessageId());
        verify(device);
        verify(hylafax);
        verify(hylafaxJob1);
        verify(hylafaxJob2);
    }
}
