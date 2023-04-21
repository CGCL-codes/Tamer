package de.offis.example_applications.transformation4u_client;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import org.apache.axis.AxisFault;
import de.offis.semanticmm4u.global.Constants;
import de.offis.semanticmm4u.global.Debug;

/**
 * 
 * This client code shows an easy way to access the
 * Transformation4U web service.
 * 
 *
 */
public class TestClient {

    public static final int SMIL2_0 = 101;

    public static final int SMIL2_0_BASIC_LANGUAGE_PROFILE = 102;

    public static final int SMIL2_1 = 11;

    public static final int SMIL2_1_EXTENDED_MOBILE_PROFILE = 12;

    public static final int SMIL2_1_MOBILE_PROFILE = 13;

    public static final int HTML = 2;

    public static final int HTML_AND_TIME = 3;

    public static final int XHTML = 4;

    public static final int XHTML_BASIC = 103;

    public static final int SVG1_2_TINY = 5;

    public static final int SVG1_2_BASIC = 6;

    public static final int SVG1_2 = 7;

    public static final int ZYX = 8;

    public static final int XMT_OMEGA = 9;

    public static final int FLASH = 10;

    public static final int REALPLAYER_SMIL2_0 = 0;

    public static final int REALPLAYER_SMIL2_0_BASIC_LANGUAGE_PROFILE = 1;

    public static final int TINYLINE_SVG = 100;

    public static final int POCKETSMILPLAYER_SMIL2_0_BASIC_LANGUAGE_PROFILE = 104;

    public static final int AMBULANTPLAYER_SMIL2_0 = 105;

    public static final int AMBULANTPLAYER_SMIL2_0_BASIC_LANGUAGE_PROFILE = 106;

    public static final String SERVICE_URL = "http://localhost:8080/axis/Transformation4U.jws";

    public static final String DOCUMENT_URL = Constants.getValue("mm4u_examples") + "slideshow.mm4u.xml";

    public static final String PROFILE_ID = "simple user";

    public static final int OUTPUT_FORMAT = HTML;

    public static final String OUTPUT_FILE = "bin/output.html";

    /**
	 * Main client logic.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        byte[] transformedDocumentFromURLAndFormat = null;
        byte[] transformedDocumentFromURLAndProfile = null;
        byte[] transformedDocumentFromContentAndFormat = null;
        byte[] transformedDocumentFromContentAndProfile = null;
        try {
            Transformation4UServiceLocator locator = new Transformation4UServiceLocator();
            URL serviceURL = new URL(SERVICE_URL);
            Transformation4USoapBindingStub service = new Transformation4USoapBindingStub(serviceURL, locator);
            System.out.println("Transformation of document 1 in progress...");
            transformedDocumentFromURLAndFormat = service.applyTransformationOnURL(DOCUMENT_URL, OUTPUT_FORMAT);
            System.out.println("Transformation of document 2 in progress...");
            transformedDocumentFromURLAndProfile = service.applyTransformationOnURL(DOCUMENT_URL, PROFILE_ID);
            String documentContent = getStringContentFromURL(new URL(DOCUMENT_URL));
            System.out.println("Transformation of document 3 in progress...");
            transformedDocumentFromContentAndFormat = service.applyTransformationOnDocument(documentContent, OUTPUT_FORMAT);
            System.out.println("Transformation of document 4 in progress...");
            transformedDocumentFromContentAndProfile = service.applyTransformationOnDocument(documentContent, PROFILE_ID);
        } catch (AxisFault e) {
            Debug.println("Transformation4U: AxisFault");
            e.printStackTrace();
        } catch (RemoteException e) {
            Debug.println("Transformation4U: RemoteException");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Debug.println("Transformation4U: MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            Debug.println("Transformation4U: IOException");
            e.printStackTrace();
        }
        if (transformedDocumentFromURLAndFormat != null) {
            try {
                System.out.print("Writing output file \"" + OUTPUT_FILE + "\"...");
                FileOutputStream os = new FileOutputStream(OUTPUT_FILE);
                os.write(transformedDocumentFromURLAndFormat);
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(" done.");
    }

    /**
	 * Helper method to obtain document content from an URL.
	 */
    private static String getStringContentFromURL(URL url) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        InputStream inputStream = url.openStream();
        int len;
        byte[] b = new byte[128];
        while ((len = inputStream.read(b)) != -1) result.write(b, 0, len);
        inputStream.close();
        return result.toString();
    }
}
