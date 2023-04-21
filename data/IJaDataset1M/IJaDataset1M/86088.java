package gov.nist.javax.sip.address;

import java.io.UnsupportedEncodingException;

/**
 * Copied from Apache Excalibur project.
 * Source code available at http://www.google.com/codesearch?hl=en&q=+excalibur+decodePath+show:sK_gDY0W5Rw:OTjCHAiSuF0:th3BdHtpX20&sa=N&cd=1&ct=rc&cs_p=http://apache.edgescape.com/excalibur/excalibur-sourceresolve/source/excalibur-sourceresolve-1.1-src.zip&cs_f=excalibur-sourceresolve-1.1/src/java/org/apache/excalibur/source/SourceUtil.java
 * @author <A HREF="mailto:jean.deruelle@gmail.com">Jean Deruelle</A> 
 *
 */
public class RFC2396UrlDecoder {

    /**
     * Decode a path.
     *
     * <p>Interprets %XX (where XX is hexadecimal number) as UTF-8 encoded bytes.
     * <p>The validity of the input path is not checked (i.e. characters that
     * were not encoded will not be reported as errors).
     * <p>This method differs from URLDecoder.decode in that it always uses UTF-8
     * (while URLDecoder uses the platform default encoding, often ISO-8859-1),
     * and doesn't translate + characters to spaces.
     *
     * @param uri the path to decode
     * @return the decoded path
     */
    public static String decode(String uri) {
        StringBuffer translatedUri = new StringBuffer(uri.length());
        byte[] encodedchars = new byte[uri.length() / 3];
        int i = 0;
        int length = uri.length();
        int encodedcharsLength = 0;
        while (i < length) {
            if (uri.charAt(i) == '%') {
                while (i < length && uri.charAt(i) == '%') {
                    if (i + 2 < length) {
                        try {
                            byte x = (byte) Integer.parseInt(uri.substring(i + 1, i + 3), 16);
                            encodedchars[encodedcharsLength] = x;
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Illegal hex characters in pattern %" + uri.substring(i + 1, i + 3));
                        }
                        encodedcharsLength++;
                        i += 3;
                    } else {
                        throw new IllegalArgumentException("% character should be followed by 2 hexadecimal characters.");
                    }
                }
                try {
                    String translatedPart = new String(encodedchars, 0, encodedcharsLength, "UTF-8");
                    translatedUri.append(translatedPart);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("Problem in decodePath: UTF-8 encoding not supported.");
                }
                encodedcharsLength = 0;
            } else {
                translatedUri.append(uri.charAt(i));
                i++;
            }
        }
        return translatedUri.toString();
    }
}
