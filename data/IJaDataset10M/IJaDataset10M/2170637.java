package jcox.jplc.ibios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import jcox.jplc.device.DeviceAddress;
import jcox.jplc.ibios.response.DownloadResponse;
import jcox.jplc.ibios.response.EventReportResponse;
import jcox.jplc.ibios.response.GetVersionResponse;
import jcox.jplc.ibios.response.IBIOSResponse;
import jcox.jplc.ibios.response.InsteonMessageReceivedResponse;
import jcox.jplc.ibios.response.MaskResponse;
import jcox.jplc.ibios.response.UnhandledResponse;
import jcox.jplc.message.Hops;
import jcox.jplc.message.InsteonMessageFlags;
import jcox.jplc.message.InsteonMessageType;
import jcox.jplc.message.command.CommonCommandType;
import jcox.jplc.util.HexUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class IBIOSParserTest {

    private IBIOSParser parser;

    @Mock
    BlockingQueue<IBIOSResponse> ibiosResponses;

    private final byte[] startOfNextMessage = new byte[] { 0x02 };

    private final byte[] rawGetVersionResponse1 = new byte[] {};

    private final byte[] rawGetVersionResponse2 = new byte[] { 0x02 };

    private final byte[] rawGetVersionResponse3 = new byte[] { 0x48, (byte) 0xff, (byte) 0xff };

    private final byte[] rawGetVersionResponse4 = new byte[] { (byte) 0xff, 0x04, 0x00 };

    private final byte[] rawGetVersionResponse5 = new byte[] { 0x23, 0x06 };

    private final byte[] rawGetVersionResponse5WithStartOfNewMessage = new byte[] { 0x23, 0x20, 0x20, 0x06, 0x02, 0x48, (byte) 0xff, (byte) 0xff };

    private final byte[] rawNoAcknowledgementVersionResponse5 = new byte[] { 0x23, 0x50 };

    private final byte[] rawDownloadResponse1 = new byte[] { 0x02 };

    private final byte[] rawDownloadResponse2 = new byte[] { 0x40, 0x01, (byte) 0xA4, 0x00, 0x06, 0x06 };

    private final byte[] rawDownloadResponse2withStartOfNewMessage = new byte[] { 0x40, 0x01, (byte) 0xA4, 0x00, 0x06, 0x06, 0x02, 0x48, (byte) 0xff };

    private final byte[] rawNoAcknowledgementDownloadResponse2 = new byte[] { 0x40, 0x01, (byte) 0xA4, 0x00, 0x06, (byte) 0x90 };

    private final byte[] rawMaskResponse1 = new byte[] { 0x02 };

    private final byte[] rawMaskResponse2 = new byte[] { 0x46, 0x01, 0x42, 0x10, (byte) 0xFF, 0x06 };

    private final byte[] rawMaskResponse2WithStartOfNewMessage = new byte[] { 0x46, 0x01, 0x42, 0x10, (byte) 0xFF, 0x06, 0x02, 0x48, (byte) 0xff };

    private final byte[] rawNoAcknowledgementMaskResponse2 = new byte[] { 0x46, 0x01, 0x42, 0x10, (byte) 0xFF, 0x50 };

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        parser = new IBIOSParser(ibiosResponses);
    }

    @Test
    public void testParseDownloadResponse() throws Exception {
        parser.parse(rawDownloadResponse1);
        parser.parse(rawDownloadResponse2);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of DownloadResponse", ibiosQueueArgument.getValue() instanceof DownloadResponse);
        DownloadResponse downloadResponse = (DownloadResponse) ibiosQueueArgument.getValue();
        assertEquals("Unexpected data array length", 6, downloadResponse.getDataLength());
        assertEquals("Expecting an ACK", Acknowledgement.ACK, downloadResponse.getAcknowledgement());
        assertEquals("Unexpected FlatMemoryMapAddress", FlatMemoryMapAddress.INSTEON_MESSAGE_CONSTRUCION_BUFFER, downloadResponse.getToFlatMemoryModelAddress());
    }

    @Test
    public void testParseDownloadResponseWithStartOfAnotherMessageAtTheEnd() throws Exception {
        parser.parse(rawDownloadResponse1);
        parser.parse(rawDownloadResponse2withStartOfNewMessage);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of DownloadResponse", ibiosQueueArgument.getValue() instanceof DownloadResponse);
        DownloadResponse downloadResponse = (DownloadResponse) ibiosQueueArgument.getValue();
        assertEquals("Unexpected data array length", 6, downloadResponse.getDataLength());
        assertEquals("Expecting an ACK", Acknowledgement.ACK, downloadResponse.getAcknowledgement());
        assertEquals("Unexpected FlatMemoryMapAddress", FlatMemoryMapAddress.INSTEON_MESSAGE_CONSTRUCION_BUFFER, downloadResponse.getToFlatMemoryModelAddress());
        assertTrue("Expected parser to hold start of next message.", parser.abandonCurrentParse().length > 0);
    }

    @Test
    public void testParseGarbledFollowedByGarbageThenDownloadResponse() throws Exception {
        parser.parse(rawGetVersionResponse1);
        parser.parse(rawGetVersionResponse2);
        parser.parse(rawGetVersionResponse3);
        parser.parse(new byte[] { (byte) 0x90, 0x20, 0x20, 0x50, (byte) 0x90, 0x20, 0x20 });
        parser.parse(rawDownloadResponse1);
        parser.parse(rawDownloadResponse2);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses, times(2)).put(ibiosQueueArgument.capture());
        List<IBIOSResponse> ibiosQueueArguments = ibiosQueueArgument.getAllValues();
        assertEquals("Unexpected number of responses queued", 2, ibiosQueueArguments.size());
        assertTrue("Expecting ibios queue(0) to hold an instance of UnhandledResponse", ibiosQueueArguments.get(0) instanceof UnhandledResponse);
        assertTrue("Expecting ibios queue(1) to hold an instance of DownloadResponse", ibiosQueueArguments.get(1) instanceof DownloadResponse);
        DownloadResponse downloadResponse = (DownloadResponse) ibiosQueueArguments.get(1);
        assertEquals("Unexpected data array length", 6, downloadResponse.getDataLength());
        assertEquals("Expecting an ACK", Acknowledgement.ACK, downloadResponse.getAcknowledgement());
        assertEquals("Unexpected FlatMemoryMapAddress", FlatMemoryMapAddress.INSTEON_MESSAGE_CONSTRUCION_BUFFER, downloadResponse.getToFlatMemoryModelAddress());
    }

    @Test
    public void testParseMaskResponse() throws Exception {
        parser.parse(rawMaskResponse1);
        parser.parse(rawMaskResponse2);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of DownloadResponse", ibiosQueueArgument.getValue() instanceof MaskResponse);
        MaskResponse maskResponse = (MaskResponse) ibiosQueueArgument.getValue();
        assertEquals("Unexpected OR mask", (byte) 0x10, maskResponse.getOrMask());
        assertEquals("Unexpected AND mask", (byte) 0xFF, maskResponse.getAndMask());
        assertEquals("Expecting an ACK", Acknowledgement.ACK, maskResponse.getAcknowledgement());
        assertEquals("Unexpected FlatMemoryMapAddress", FlatMemoryMapAddress.SEND_INSTEON_MESSAGE_CONTROL, maskResponse.getMaskedFlatMemoryModelAddress());
    }

    @Test
    public void testParseMaskResponseThatHasMessageAtEnd() throws Exception {
        parser.parse(rawMaskResponse1);
        parser.parse(rawMaskResponse2WithStartOfNewMessage);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of DownloadResponse", ibiosQueueArgument.getValue() instanceof MaskResponse);
        MaskResponse maskResponse = (MaskResponse) ibiosQueueArgument.getValue();
        assertEquals("Unexpected OR mask", (byte) 0x10, maskResponse.getOrMask());
        assertEquals("Unexpected AND mask", (byte) 0xFF, maskResponse.getAndMask());
        assertEquals("Expecting an ACK", Acknowledgement.ACK, maskResponse.getAcknowledgement());
        assertEquals("Unexpected FlatMemoryMapAddress", FlatMemoryMapAddress.SEND_INSTEON_MESSAGE_CONTROL, maskResponse.getMaskedFlatMemoryModelAddress());
        assertTrue("Expected parser to hold start of next message.", parser.abandonCurrentParse().length > 0);
    }

    @Test
    public void testParseInvalidDownloadResponseNoAcknowledgement() throws Exception {
        parser.parse(rawDownloadResponse1);
        parser.parse(rawNoAcknowledgementDownloadResponse2);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to hold an instance of UnhandledResponse", ibiosQueueArgument.getValue() instanceof UnhandledResponse);
    }

    @Test
    public void testParseInvalidMaskResponseNoAcknowledgement() throws Exception {
        parser.parse(rawMaskResponse1);
        parser.parse(rawNoAcknowledgementMaskResponse2);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to hold an instance of UnhandledResponse", ibiosQueueArgument.getValue() instanceof UnhandledResponse);
    }

    @Test
    public void testParseGetVersionMessage() throws Exception {
        parser.parse(rawGetVersionResponse1);
        parser.parse(rawGetVersionResponse2);
        parser.parse(rawGetVersionResponse3);
        parser.parse(rawGetVersionResponse4);
        parser.parse(rawGetVersionResponse5);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of GetVersionResponse", ibiosQueueArgument.getValue() instanceof GetVersionResponse);
        GetVersionResponse getVersionResponse = (GetVersionResponse) ibiosQueueArgument.getValue();
        assertEquals("Unexpected device address", new DeviceAddress((byte) 0xff, (byte) 0xff, (byte) 0xff), getVersionResponse.getDeviceAddress());
        assertTrue("Unexpected device type", Arrays.equals(new byte[] { 0x04, 0x00 }, getVersionResponse.getDeviceType()));
        assertEquals("Unexpected firmware version", 0x23, getVersionResponse.getFirmwareRevision());
    }

    @Test
    public void testParseInvalidGetVersionResponseNoAcknowledgement() throws Exception {
        parser.parse(rawGetVersionResponse1);
        parser.parse(rawGetVersionResponse2);
        parser.parse(rawGetVersionResponse3);
        parser.parse(rawGetVersionResponse4);
        parser.parse(rawNoAcknowledgementVersionResponse5);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to hold an instance of UnhandledResponse", ibiosQueueArgument.getValue() instanceof UnhandledResponse);
    }

    @Test
    public void testParseInvalidGetVersionResponseTooLong() throws Exception {
        parser.parse(rawGetVersionResponse1);
        parser.parse(rawGetVersionResponse2);
        parser.parse(rawGetVersionResponse3);
        parser.parse(rawGetVersionResponse4);
        parser.parse(rawGetVersionResponse5WithStartOfNewMessage);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to hold an instance of UnhandledResponse", ibiosQueueArgument.getValue() instanceof UnhandledResponse);
    }

    @Test
    public void testParseGetVersionMessageThenDownloadThenMask() throws Exception {
        parser.parse(rawGetVersionResponse1);
        parser.parse(rawGetVersionResponse2);
        parser.parse(rawGetVersionResponse3);
        parser.parse(rawGetVersionResponse4);
        parser.parse(rawGetVersionResponse5);
        parser.parse(rawDownloadResponse1);
        parser.parse(rawDownloadResponse2);
        parser.parse(rawMaskResponse1);
        parser.parse(rawMaskResponse2);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses, times(3)).put(ibiosQueueArgument.capture());
        List<IBIOSResponse> ibiosQueueArguments = ibiosQueueArgument.getAllValues();
        assertEquals("Unexpected number of responses queued", 3, ibiosQueueArguments.size());
        assertTrue("Expecting ibios queue(0) to hold an instance of GetVersionResponse", ibiosQueueArguments.get(0) instanceof GetVersionResponse);
        assertTrue("Expecting ibios queue(1) to hold an instance of DownloadResponse", ibiosQueueArguments.get(1) instanceof DownloadResponse);
        assertTrue("Expecting ibios queue(2) to hold an instance of MaskResponse", ibiosQueueArguments.get(2) instanceof MaskResponse);
    }

    @Test
    public void testAbandonCurrentParseNothingStartedYet() {
        byte actual[] = parser.abandonCurrentParse();
        assertEquals("Expecting an empty byte array", HexUtils.unsignedBytesToHex(new byte[0]), HexUtils.unsignedBytesToHex(actual));
    }

    @Test
    public void testAbandonCurrentParse() throws Exception {
        parser.parse(rawGetVersionResponse1);
        parser.parse(rawGetVersionResponse2);
        parser.parse(rawGetVersionResponse3);
        byte actual[] = parser.abandonCurrentParse();
        assertEquals("Expecting an empty byte array", HexUtils.unsignedBytesToHex(new byte[] { (byte) 0xff, (byte) 0xff }), HexUtils.unsignedBytesToHex(actual));
    }

    @Test
    public void testAbandonCurrentParseThenParse() throws Exception {
        parser.parse(rawGetVersionResponse1);
        parser.parse(rawGetVersionResponse2);
        parser.parse(rawGetVersionResponse3);
        byte actual[] = parser.abandonCurrentParse();
        assertEquals("Expecting an empty byte array", HexUtils.unsignedBytesToHex(new byte[] { (byte) 0xff, (byte) 0xff }), HexUtils.unsignedBytesToHex(actual));
        parser.parse(rawDownloadResponse1);
        parser.parse(rawDownloadResponse2);
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of DownloadResponse", ibiosQueueArgument.getValue() instanceof DownloadResponse);
    }

    @Test
    public void testAckAtEndOfDownloadResponse() throws Exception {
        HIDPacketAdapter adapter = new HIDPacketAdapter();
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { (byte) 0x84, 0x01, (byte) 0xA4, 0x00, 0x06, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { (byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of DownloadResponse", ibiosQueueArgument.getValue() instanceof DownloadResponse);
        assertEquals("Expecting current parse contents to be empty", HexUtils.unsignedBytesToHex(new byte[] {}), HexUtils.unsignedBytesToHex(parser.abandonCurrentParse()));
    }

    @Test
    public void testGetActualVersionResponse() throws Exception {
        HIDPacketAdapter adapter = new HIDPacketAdapter();
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { (byte) 0x84, 0x01, (byte) 0xA4, 0x00, 0x06, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { (byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of DownloadResponse", ibiosQueueArgument.getValue() instanceof DownloadResponse);
        assertEquals("Expecting current parse contents to be empty", HexUtils.unsignedBytesToHex(new byte[] {}), HexUtils.unsignedBytesToHex(parser.abandonCurrentParse()));
    }

    @Test
    public void testGetActualVersionResponse2() throws Exception {
        HIDPacketAdapter adapter = new HIDPacketAdapter();
        parser.parse(adapter.disassembleToByteArray(new byte[] { (byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x02, 0x02, 0x48, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x04, 0x08, (byte) 0xA3, 0x31, 0x03, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x03, 0x02, 0x2D, 0x06, 0x00, 0x00, 0x00, 0x00 }));
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of GetVersionResponse", ibiosQueueArgument.getValue() instanceof GetVersionResponse);
        assertEquals("Expecting current parse contents to be empty", HexUtils.unsignedBytesToHex(new byte[] {}), HexUtils.unsignedBytesToHex(parser.abandonCurrentParse()));
    }

    @Test
    public void testGetActualVersionResponse3() throws Exception {
        HIDPacketAdapter adapter = new HIDPacketAdapter();
        parser.parse(adapter.disassembleToByteArray(new byte[] { (byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x07, 0x48, 0x08, (byte) 0xA3, 0x31, 0x03, 0x02, 0x2D }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { (byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x07, 0x48, 0x08, (byte) 0xA3, 0x31, 0x03, 0x02, 0x2D }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses, atLeast(2)).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of GetVersionResponse at index 0", ibiosQueueArgument.getAllValues().get(0) instanceof GetVersionResponse);
        assertTrue("Expecting ibios queue to have an instance of GetVersionResponse at index 1", ibiosQueueArgument.getAllValues().get(1) instanceof GetVersionResponse);
        assertEquals("Expecting current parse contents to be empty", HexUtils.unsignedBytesToHex(new byte[] {}), HexUtils.unsignedBytesToHex(parser.abandonCurrentParse()));
    }

    @Test
    public void testParseInsteonMessageReceived() throws Exception {
        InsteonMessageFlags insteonMessageFlags = new InsteonMessageFlags(InsteonMessageType.DIRECT_MESSAGE, false, Hops.THREE, Hops.THREE);
        parser.parse(new byte[] { 0x02 });
        parser.parse(new byte[] { 0x4F, IBIOSEvent.EVNT_IRX_MYMSG.getHandle() });
        parser.parse(new byte[] { 0x44, 0x72, (byte) 0xD2 });
        parser.parse(new byte[] { 0x00, 0x02, (byte) 0xAC });
        parser.parse(new byte[] { insteonMessageFlags.toByte(), CommonCommandType.ON.getCommandByte(), (byte) 0xFF });
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of InsteonMessageReceivedResponse", ibiosQueueArgument.getValue() instanceof InsteonMessageReceivedResponse);
        InsteonMessageReceivedResponse insteonMessageReceivedResponse = (InsteonMessageReceivedResponse) ibiosQueueArgument.getValue();
        assertNotNull("Expected InsteonMessageReceivedResponse to have a date received", insteonMessageReceivedResponse.getDateReceived());
        assertNotNull("Expected InsteonMessageReceivedResponse to hold an Insteon Message", insteonMessageReceivedResponse.getInsteonMessage());
        assertEquals("Unexpected IBIOS event", IBIOSEvent.EVNT_IRX_MYMSG, insteonMessageReceivedResponse.getIBIOSEvent());
        assertEquals("Unexpected from address", HexUtils.unsignedBytesToHex(new byte[] { 0x44, 0x72, (byte) 0xD2 }), HexUtils.unsignedBytesToHex(insteonMessageReceivedResponse.getInsteonMessage().getFromAddress().toByteArray()));
    }

    @Test
    public void testParseEventReportInMultiplePackets() throws Exception {
        parser.parse(new byte[] { 0x02 });
        parser.parse(new byte[] { 0x45, IBIOSEvent.EVNT_LOAD_ON.getHandle() });
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of EventReportResponse", ibiosQueueArgument.getValue() instanceof EventReportResponse);
        EventReportResponse eventReportResponse = (EventReportResponse) ibiosQueueArgument.getValue();
        assertNotNull("Expected eventReportResponse to have a date received", eventReportResponse.getDateReceived());
        assertEquals("Unexpected IBIOS event", IBIOSEvent.EVNT_LOAD_ON, eventReportResponse.getIBIOSEvent());
    }

    @Test
    public void testParseEventReportInOnePacket() throws Exception {
        parser.parse(new byte[] { 0x02, 0x45, IBIOSEvent.EVNT_LOAD_ON.getHandle() });
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of EventReportResponse", ibiosQueueArgument.getValue() instanceof EventReportResponse);
        EventReportResponse eventReportResponse = (EventReportResponse) ibiosQueueArgument.getValue();
        assertNotNull("Expected eventReportResponse to have a date received", eventReportResponse.getDateReceived());
        assertEquals("Unexpected IBIOS event", IBIOSEvent.EVNT_LOAD_ON, eventReportResponse.getIBIOSEvent());
    }

    @Test
    public void testParseEventReportNotAnIBIOSEvent() throws Exception {
        parser.parse(new byte[] { 0x02, 0x45, (byte) 0xFF });
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to hold an instance of UnhandledResponse", ibiosQueueArgument.getValue() instanceof UnhandledResponse);
    }

    @Test
    public void testParseEventReportPacketHasStartOfNewMessageAtEndOfPacket() throws Exception {
        parser.parse(new byte[] { 0x02, 0x45, IBIOSEvent.EVNT_LOAD_ON.getHandle(), 0x02, 0x48, (byte) 0xff });
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of EventReportResponse", ibiosQueueArgument.getValue() instanceof EventReportResponse);
        assertTrue("Expected parser to hold start of next message.", parser.abandonCurrentParse().length > 0);
    }

    @Test
    public void testGetCheckSum() throws Exception {
        parser.parse(new byte[] { 0x02, 0x44, 0x02, 0x30, 0x00, (byte) 0xa0 });
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of UnhandledResponse", ibiosQueueArgument.getValue() instanceof UnhandledResponse);
        assertEquals("Expecting current parse contents to be empty", HexUtils.unsignedBytesToHex(new byte[] {}), HexUtils.unsignedBytesToHex(parser.abandonCurrentParse()));
    }

    @Test
    public void testLeadingZerosOnParse() throws Exception {
        HIDPacketAdapter adapter = new HIDPacketAdapter();
        parser.parse(adapter.disassembleToByteArray(new byte[] { (byte) 0x03, 0x00, 0x02, 0x45, 0x00, 0x00, 0x00, 0x00 }));
        parser.parse(adapter.disassembleToByteArray(new byte[] { 0x01, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }));
        ArgumentCaptor<IBIOSResponse> ibiosQueueArgument = ArgumentCaptor.forClass(IBIOSResponse.class);
        verify(ibiosResponses).put(ibiosQueueArgument.capture());
        assertTrue("Expecting ibios queue to have an instance of EventReport", ibiosQueueArgument.getValue() instanceof EventReportResponse);
        assertEquals("Expecting current parse contents to be empty", HexUtils.unsignedBytesToHex(new byte[] {}), HexUtils.unsignedBytesToHex(parser.abandonCurrentParse()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArg() throws Exception {
        parser.parse(null);
    }
}
