package de.helwich.mpo;

import static de.helwich.mpo.Util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;
import org.xml.sax.SAXException;
import de.helwich.mpo.MPOParser2.ImageStreamHandler;

/**
 * @author Hendrik Helwich
 */
public class MPOParserTest {

    private void testExifOrgSingleImage(String name) throws IOException, SAXException {
        final String fname = "/de/helwich/mpo/examples/" + name;
        final InputStream tin = getClass().getResourceAsStream(fname);
        MPOXMLHandler handler = new MPOXMLHandler() {

            private int picIdx = -1;

            @Override
            public void thumbnail(InputStream thumbStream) {
                super.thumbnail(thumbStream);
                InputStream tin = getClass().getResourceAsStream(fname + "_thumb" + picIdx + ".jpg");
                try {
                    assertEquals_(thumbStream, tin);
                    assertStreamFinished(tin);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void startPicture() {
                super.startPicture();
                picIdx++;
            }

            @Override
            public void endPicture() {
                super.endPicture();
            }
        };
        InputStream in = getClass().getResourceAsStream(fname);
        MPOParser2 parser = new MPOParser2();
        parser.addMPOHandler(handler);
        parser.parse(in, new ImageStreamHandler() {

            @Override
            public void streamFinished() {
                try {
                    assertStreamFinished(tin);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void handleImageStream(InputStream imageStream) throws IOException {
                assertEquals_(imageStream, tin);
            }
        });
        in.close();
        assertStreamFinished(tin);
        String xml = handler.toString();
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(fname + ".xml"));
        StringBuilder text = new StringBuilder();
        char[] buf = new char[1024];
        for (int numRead = 0; (numRead = reader.read(buf)) != -1; ) text.append(buf, 0, numRead);
        new XMLTestCase() {
        }.assertXMLEqual(text.toString(), xml);
    }

    @Test
    public void testExifOrgSingleImageSamples() throws IOException, SAXException {
        testExifOrgSingleImage("org_exif_samples/canon-ixus.jpg");
        testExifOrgSingleImage("org_exif_samples/fujifilm-dx10.jpg");
        testExifOrgSingleImage("org_exif_samples/fujifilm-finepix40i.jpg");
        testExifOrgSingleImage("org_exif_samples/fujifilm-mx1700.jpg");
        testExifOrgSingleImage("org_exif_samples/kodak-dc210.jpg");
        testExifOrgSingleImage("org_exif_samples/kodak-dc240.jpg");
        testExifOrgSingleImage("org_exif_samples/nikon-e950.jpg");
        testExifOrgSingleImage("org_exif_samples/olympus-c960.jpg");
        testExifOrgSingleImage("org_exif_samples/ricoh-rdc5300.jpg");
        testExifOrgSingleImage("org_exif_samples/sanyo-vpcg250.jpg");
        testExifOrgSingleImage("org_exif_samples/sanyo-vpcsx550.jpg");
        testExifOrgSingleImage("org_exif_samples/sony-cybershot.jpg");
        testExifOrgSingleImage("org_exif_samples/sony-d700.jpg");
    }

    @Test
    public void testMPOImageSamples() throws IOException, SAXException {
        testExifOrgSingleImage("Fujifilm_FinePix_REAL_3D_W1.mpo");
    }
}
