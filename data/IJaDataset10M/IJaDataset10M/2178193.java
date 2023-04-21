package codec.examples.tagging;

import codec.asn1.ASN1Exception;
import java.io.IOException;

/**
 * This class shows how a SEQUENCE value with tagged fields is created,
 * encoded and decoded.
 *
 * @author Alberto Sierra
 * @version "$Id: CodingDecodingExample.java,v 1.2 2004/08/05 13:21:04 pebinger Exp $"
 */
public class CodingDecodingExample {

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        Person asn1Object;
        byte[] encodedAsn1Object;
        StringBuffer buf;
        String octet;
        Person newAsn1Object;
        asn1Object = new Person("Sir", "Peter");
        System.out.println("ASN.1 object: ");
        System.out.println(asn1Object.toString());
        System.out.println();
        encodedAsn1Object = null;
        try {
            encodedAsn1Object = asn1Object.getEncoded();
        } catch (ASN1Exception e) {
            System.out.println("Error during encoding.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error during encoding.");
            e.printStackTrace();
        }
        buf = new StringBuffer();
        for (int i = 0; i < encodedAsn1Object.length; i++) {
            octet = Integer.toHexString(encodedAsn1Object[i] & 0xff);
            buf.append(" 0x");
            if (octet.length() == 1) {
                buf.append('0');
            }
            buf.append(octet);
        }
        System.out.println("Encoded ASN.1 object:");
        System.out.println(buf.toString());
        System.out.println();
        newAsn1Object = new Person();
        try {
            newAsn1Object.decode(encodedAsn1Object);
        } catch (ASN1Exception e) {
            System.out.println("Error during decoding.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error during decoding.");
            e.printStackTrace();
        }
        System.out.println("New ASN.1 object got by decoding the previous bytes:");
        System.out.println(newAsn1Object.toString());
        System.out.println();
    }
}
