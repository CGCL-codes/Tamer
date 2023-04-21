package com.dokumentarchiv.test.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import junit.framework.TestCase;
import de.inovox.AdvancedMimeMessage;

/**
 * @author carsten
 *
 */
public class AdvancedMimeMessageTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link com.dokumentarchiv.plugins.AdvancedMimeMessage#getBytes()}.
     */
    public void testWriteTo() {
        File outFile = null;
        try {
            URL url = getClass().getResource("Test.msg");
            File inFile = new File(url.getFile());
            InputStream in = new FileInputStream(inFile);
            Session session = Session.getDefaultInstance(new Properties());
            AdvancedMimeMessage msg1 = new AdvancedMimeMessage(session, in);
            in.close();
            String id = msg1.getMessageID();
            int size = msg1.getSize();
            outFile = File.createTempFile("test", null);
            OutputStream out = new FileOutputStream(outFile);
            msg1.writeTo(out);
            out.close();
            in = new FileInputStream(outFile);
            AdvancedMimeMessage msg2 = new AdvancedMimeMessage(session, in);
            assertEquals(id, msg2.getMessageID());
            assertEquals(size, msg2.getSize());
            in.close();
            msg1.setCompressData(true);
            out = new FileOutputStream(outFile);
            msg1.writeTo(out);
            out.close();
            in = new FileInputStream(outFile);
            ZipInputStream zip = new ZipInputStream(in);
            ZipEntry entry = zip.getNextEntry();
            if (entry != null) {
                msg2 = new AdvancedMimeMessage(session, zip);
                assertEquals(id, msg2.getMessageID());
                assertEquals(size, msg2.getSize());
            } else {
                assertTrue("Zip file invalid", false);
            }
            zip.close();
            in.close();
            msg1.setCompressData(false);
            msg1.setEncryptData(true);
            out = new FileOutputStream(outFile);
            msg1.writeTo(out);
            out.close();
            in = new FileInputStream(outFile);
            msg2 = new AdvancedMimeMessage(session, in);
            in.close();
            MimeMessage decrypted = msg2.decryptMessage("78302a439aac40499d1c217235da3196ec57823e", "inovox");
            assertEquals(id, decrypted.getMessageID());
        } catch (Exception e) {
            fail(e.toString());
        } finally {
            if (outFile != null) {
                outFile.delete();
            }
        }
    }
}
