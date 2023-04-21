package com.volantis.mps.assembler;

import com.volantis.mps.attachment.MessageAttachments;
import com.volantis.mps.message.MessageAsset;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.ProtocolIndependentMessage;
import com.volantis.testtools.server.HTTPServer;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.OutputStream;
import java.io.IOException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import junit.framework.AssertionFailedError;

/**
 * This class does provides unit test cases for testing the
 * <code>MimeMessageAssembler</code> class
 */
public class MimeMessageAssemblerTestCase extends TestCaseAbstract {

    /**
     * Initialise a new named instance of this test case.
     *
     * @param testName The name of the test case
     */
    public MimeMessageAssemblerTestCase(String testName) {
        super(testName);
    }

    /**
     * Test the assembly of messages
     */
    public void xtestAssembleMessage() throws Exception {
        HTTPServer assetServer = new HTTPServer(9999, 3);
        assetServer.start();
        MimeMessageAssembler mimeMessageAssembler = new MimeMessageAssembler() {

            protected void checkMessageConstraints(ArrayList imageAssets, Integer maxMessageSize, Integer maxImageSize, int actualMessageSize) throws MessageException {
            }
        };
        HashMap assetMap = new HashMap();
        assetMap.put("mimeref", new MessageAsset("Test Message"));
        MessageAsset asset1 = new MessageAsset(MessageAsset.ON_SERVER, "http://127.0.0.1:9999/test.gif");
        MessageAsset asset2 = new MessageAsset(MessageAsset.ON_DEVICE, "http://127.0.0.1:9999/test.gif");
        assetMap.put("asset1", asset1);
        assetMap.put("asset2", asset2);
        ProtocolIndependentMessage mesg = new ProtocolIndependentMessage("Hello", "text/plain", assetMap);
        MimeMultipart mimeMultipart = (MimeMultipart) mimeMessageAssembler.assembleMessage(mesg, new MessageAttachments());
        assertEquals("Number of parts should match", 3, mimeMultipart.getCount());
        MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(0);
        assertTrue("Message content is different. Was expecting " + "\"Test Message\", got \"" + part.getContent().toString(), "Hello".equals(part.getContent().toString()));
        checkAsset((MimeBodyPart) mimeMultipart.getBodyPart(1));
        checkAsset((MimeBodyPart) mimeMultipart.getBodyPart(2));
    }

    /**
     * A utility method that tests the validity of the given
     * <code>MimeBodyPart</code> as either a text asset or an inline asset.
     * </p>
     * <p>
     * Other tests can be added.  Just be sure to add them here to avoid order
     * dependecies of the {@link MimeMultipart#getBodyPart} method.  The order
     * items are returned differs in JDK 1.3 and 1.4; probably due to the
     * implementation of the collections used.
     * </p>
     *
     * @param part The mime body part to test.
     *
     * @throws Exception If there is a failure in the tests, or a problem
     *                   processing the body part during the tests.
     */
    private void checkAsset(MimeBodyPart part) throws Exception {
        if (!isTextAsset(part) && !isInlineAsset(part)) {
            fail("Attachment failed for part " + part.getContent().toString());
        }
    }

    /**
     * Test whether the body part provided is a text asset of a given specific
     * structure and content.
     *
     * @param part The mime body part to test.
     *
     * @return True if the assertions succeed for the given body part, false
     *         otherwise.
     *
     * @throws Exception If there is a problem testing the body part.
     */
    private boolean isTextAsset(MimeBodyPart part) throws Exception {
        boolean isText = true;
        try {
            assertTrue("First attachment is of wrong content type. " + "Was expecting text/plain got " + part.getContentType(), "text/plain".equals(part.getContentType().toString()));
            assertTrue("First attachment text is screwy. " + "Was expecting \"Test Message\" got " + part.getContent(), "Test Message".equals(part.getContent().toString()));
        } catch (AssertionFailedError e) {
            isText = false;
        }
        return isText;
    }

    /**
     * Test whether the body part provided is an inline asset of a given
     * specific structure and content.
     *
     * @param part The mime body part to test.
     *
     * @return True if the assertions succeed for the given body part, false
     *         otherwise.
     *
     * @throws Exception If there is a problem testing the body part.
     */
    private boolean isInlineAsset(MimeBodyPart part) throws Exception {
        boolean isAsset = true;
        try {
            if (!part.getDisposition().equals("inline")) {
                fail("Asset has wrong disposition got " + part.getDisposition());
            }
            if (!part.getFileName().equals("asset1")) {
                fail("Asset filename not appended to asset");
            }
        } catch (AssertionFailedError e) {
            isAsset = false;
        }
        return isAsset;
    }

    /**
     * This tests the determination of an asset's size.
     */
    public void testDetermineAssetSize() throws Exception {
        final String data = "Some test bytes";
        final int size = data.getBytes().length;
        MimeMessageAssembler mimeMessageAssembler = new MimeMessageAssembler();
        BodyPart asset = new MimeBodyPart() {

            public void writeTo(OutputStream outputStream) throws IOException, MessagingException {
                outputStream.write(data.getBytes());
            }
        };
        int actualSize = mimeMessageAssembler.determineAssetSize(asset);
        assertEquals("The sizes should match", size, actualSize);
        try {
            asset = new MimeBodyPart();
            actualSize = mimeMessageAssembler.determineAssetSize(asset);
            fail("Previous call should have caused an exception (1)");
        } catch (MessageException me) {
        }
        try {
            asset = new MimeBodyPart() {

                public void writeTo(OutputStream outputStream) throws IOException, MessagingException {
                    throw new MessagingException("Intentionally fail here");
                }
            };
            actualSize = mimeMessageAssembler.determineAssetSize(asset);
            fail("Previous call should have caused an exception (2)");
        } catch (MessageException me) {
        }
    }

    /**
     * This tests the validation of the message constraints.
     */
    public void testCheckMessageConstraints() throws Exception {
        MimeMessageAssembler mimeMessageAssembler = new MimeMessageAssembler();
        ArrayList assets = new ArrayList();
        Integer maxMessageSize = new Integer(10);
        Integer maxImageSize = new Integer(3);
        int actualSize = 5;
        mimeMessageAssembler.checkMessageConstraints(assets, maxMessageSize, maxImageSize, actualSize);
        actualSize = 15;
        try {
            mimeMessageAssembler.checkMessageConstraints(assets, maxMessageSize, maxImageSize, actualSize);
            fail("The message size should have been exceeded");
        } catch (MessageException me) {
        }
    }

    /**
     * This tests correct behaviour of the getFileName(...) method
     */
    public void testGetFileName() throws Exception {
        MMAGetFileName mma = new MMAGetFileName();
        String result = mma.getFileName("file.txt", "text/plain");
        assertEquals("File name should be file.txt", "file.txt", result);
        result = mma.getFileName("file", "text/plain");
        assertEquals("Filename should be file.plain", "file.plain", result);
        result = mma.getFileName("file:///c:\\path\\file.txt?someval=url", "text/plain");
        assertEquals("Filename should be file.txt", "file.txt", result);
        result = mma.getFileName("file:///c:\\path\\nofile\\", null);
        assertEquals("Should return path: file:///c:/path/nofile/", "file:///c:/path/nofile/", result);
        result = mma.getFileName("http://www.path-to-file.co.uk/file.txt", "text/plain");
        assertEquals("Should return path: file.txt", "file.txt", result);
        result = mma.getFileName("file.bmp", "image/bmp");
        assertEquals("return should be: file.bmp", "file.bmp", result);
        result = mma.getFileName("file.gif", "image/gif");
        assertEquals("return should be: file.gif", "file.gif", result);
        result = mma.getFileName("file.jpg", "image/jpeg");
        assertEquals("return should be: file.jpg", "file.jpg", result);
        result = mma.getFileName("file.png", "image/png");
        assertEquals("return should be: file.png", "file.png", result);
        result = mma.getFileName("file.tiff", "image/tiff");
        assertEquals("return should be: file.tiff", "file.tiff", result);
        result = mma.getFileName("file.pjpeg", "image/pjpeg");
        assertEquals("return should be: file.pjpeg", "file.pjpeg", result);
        result = mma.getFileName("file.wbmp", "image/vnd.wap.wbmp");
        assertEquals("return should be: file.wbmp", "file.wbmp", result);
        result = mma.getFileName("file.gsm", "audio/gsm");
        assertEquals("return should be: file.gsm", "file.gsm", result);
        result = mma.getFileName("file.midi", "audio/midi");
        assertEquals("return should be: file.midi", "file.midi", result);
        result = mma.getFileName("file.mp3", "audio/mpeg3");
        assertEquals("return should be: file.mp3", "file.mp3", result);
        result = mma.getFileName("file.wma", "audio/x-ms-wma");
        assertEquals("return should be: file.wma", "file.wma", result);
        result = mma.getFileName("file.mid", "audio/sp-midi");
        assertEquals("return should be: file.mid", "file.mid", result);
        result = mma.getFileName("file.wav", "audio/wav");
        assertEquals("return should be: file.wav", "file.wav", result);
        result = mma.getFileName("file.mg", "application/vnd.nokia.ringing-tone");
        assertEquals("return should be: file.mg", "file.mg", result);
        result = mma.getFileName("file.mmf", "application/vnd.smaf");
        assertEquals("return should be: file.mmf", "file.mmf", result);
        result = mma.getFileName("file.rmf", "audio/mmf");
        assertEquals("return should be: file.rmf", "file.rmf", result);
        result = mma.getFileName("file.au", "audio/basic");
        assertEquals("return should be: file.au", "file.au", result);
        result = mma.getFileName("file.imy", "audio/imelody");
        assertEquals("return should be: file.imy", "file.imy", result);
        result = mma.getFileName("file.amr", "audio/amr");
        assertEquals("return should be: file.amr", "file.amr", result);
        result = mma.getFileName("file.mpg", "video/mpeg");
        assertEquals("return should be: file.mpg", "file.mpg", result);
        result = mma.getFileName("file.wm", "video/x-ms-wm");
        assertEquals("return should be: file.wm", "file.wm", result);
        result = mma.getFileName("file.wmv", "video/x-ms-wmv");
        assertEquals("return should be: file.wmv", "file.wmv", result);
        result = mma.getFileName("file.swf", "application/x-shockwave-flash");
        assertEquals("return should be: file.swf", "file.swf", result);
        result = mma.getFileName("file.qt", "video/quicktime");
        assertEquals("return should be: file.qt", "file.qt", result);
        result = mma.getFileName("file.rv", "video/vnd.rn-realvideo");
        assertEquals("return should be: file.rv", "file.rv", result);
        result = mma.getFileName("file.rm", "application/vnd.rn-realmedia");
        assertEquals("return should be: file.rm", "file.rm", result);
        result = mma.getFileName("file.ram", "application/vnd.rn-realaudio");
        assertEquals("return should be: file.ram", "file.ram", result);
        result = mma.getFileName("file.avi", "video/avi");
        assertEquals("return should be: file.avi", "file.avi", result);
        result = mma.getFileName("file.end", "image/bmp");
        assertEquals("return should be: file.bmp", "file.bmp", result);
        result = mma.getFileName("file.end", "image/gif");
        assertEquals("return should be: file.gif", "file.gif", result);
        result = mma.getFileName("file.end", "image/jpeg");
        assertEquals("return should be: file.jpg", "file.jpg", result);
        result = mma.getFileName("file.end", "image/png");
        assertEquals("return should be: file.png", "file.png", result);
        result = mma.getFileName("file.end", "image/tiff");
        assertEquals("return should be: file.tiff", "file.tiff", result);
        result = mma.getFileName("file.end", "image/pjpeg");
        assertEquals("return should be: file.pjpeg", "file.pjpeg", result);
        result = mma.getFileName("file.end", "image/vnd.wap.wbmp");
        assertEquals("return should be: file.wbmp", "file.wbmp", result);
        result = mma.getFileName("file.end", "audio/gsm");
        assertEquals("return should be: file.gsm", "file.gsm", result);
        result = mma.getFileName("file.end", "audio/midi");
        assertEquals("return should be: file.midi", "file.midi", result);
        result = mma.getFileName("file.end", "audio/mpeg3");
        assertEquals("return should be: file.mp3", "file.mp3", result);
        result = mma.getFileName("file.end", "audio/x-ms-wma");
        assertEquals("return should be: file.wma", "file.wma", result);
        result = mma.getFileName("file.end", "audio/sp-midi");
        assertEquals("return should be: file.mid", "file.mid", result);
        result = mma.getFileName("file.end", "audio/wav");
        assertEquals("return should be: file.wav", "file.wav", result);
        result = mma.getFileName("file.end", "application/vnd.nokia.ringing-tone");
        assertEquals("return should be: file.mg", "file.mg", result);
        result = mma.getFileName("file.end", "application/vnd.smaf");
        assertEquals("return should be: file.mmf", "file.mmf", result);
        result = mma.getFileName("file.end", "audio/mmf");
        assertEquals("return should be: file.rmf", "file.rmf", result);
        result = mma.getFileName("file.end", "audio/basic");
        assertEquals("return should be: file.au", "file.au", result);
        result = mma.getFileName("file.end", "audio/imelody");
        assertEquals("return should be: file.imy", "file.imy", result);
        result = mma.getFileName("file.end", "audio/amr");
        assertEquals("return should be: file.amr", "file.amr", result);
        result = mma.getFileName("file.end", "video/mpeg");
        assertEquals("return should be: file.mpg", "file.mpg", result);
        result = mma.getFileName("file.end", "video/x-ms-wm");
        assertEquals("return should be: file.wm", "file.wm", result);
        result = mma.getFileName("file.end", "video/x-ms-wmv");
        assertEquals("return should be: file.wmv", "file.wmv", result);
        result = mma.getFileName("file.end", "application/x-shockwave-flash");
        assertEquals("return should be: file.swf", "file.swf", result);
        result = mma.getFileName("file.end", "video/quicktime");
        assertEquals("return should be: file.qt", "file.qt", result);
        result = mma.getFileName("file.end", "video/vnd.rn-realvideo");
        assertEquals("return should be: file.rv", "file.rv", result);
        result = mma.getFileName("file.end", "application/vnd.rn-realmedia");
        assertEquals("return should be: file.rm", "file.rm", result);
        result = mma.getFileName("file.end", "application/vnd.rn-realaudio");
        assertEquals("return should be: file.ram", "file.ram", result);
        result = mma.getFileName("file.end", "video/avi");
        assertEquals("return should be: file.avi", "file.avi", result);
        result = mma.getFileName("file.txt", "text/plain");
        assertEquals("return should be: file.txt", "file.txt", result);
        result = mma.getFileName("file.uext", "text/plain");
        assertEquals("return should be: file.uext", "file.uext", result);
        result = mma.getFileName("file.cns", "audio/vnd.cns.inf1");
        assertEquals("return should be: file.cns", "file.cns", result);
    }

    /**
     * Derived class for testing getFileName method of MimeMessageAssembler
     */
    private class MMAGetFileName extends MimeMessageAssembler {

        public String getFileName(String pathName, String mimeType) {
            return super.getFileName(pathName, mimeType);
        }
    }
}
