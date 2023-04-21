package org.webdocwf.util.smime.der;

import org.webdocwf.util.smime.exception.SMIMEException;
import org.webdocwf.util.smime.exception.ErrorStorage;

/**
 * DERObjectIdentifier is primitive type of DER encoded object which represents
 * Object Identifier type in ASN.1 notation. A value (distinguishable from all
 * other such values) which is associated with an information object. For
 * example object identifier for RSA algorithm is 1.2.840.113549.1.1.1.
 * Implemented object identifiers are stored in class IdentifierStorage.
 */
public class DERObjectIdentifier extends DERObject {

    /**
     * This constructor has two different forms, depend on parameter typeConstruction0,
     * which can be: DOT_SEPARATED_ARRAY or NAME_STRING. If typeConstruction0 parameter
     * is DOT_SEPARATED_ARRAY then id0 definition is represented by numbers separated
     * with dots (example: "1.2.840.113549.1.1.1"). In the case of NAME_STRING, id0
     * definition is name of object identifier (example: "RSA").
     * @param id0 defines Object Identifier in representation determined by the
     * second parameter - typeConstruction0.
     * @param typeConstruction0 can take values DOT_SEPARATED_ARRAY and NAME_STRING
     * @exception SMIMEException if wrong type of parameters are passed to the
     * constructor. Also, exception could be thrown in super class constructor or
     * in super class addContent method.
     */
    public DERObjectIdentifier(String id0, String typeConstruction0) throws SMIMEException {
        super(6);
        byte[] contentID;
        if (typeConstruction0.equalsIgnoreCase("DOT_SEPARATED_ARRAY")) {
            int[] temp;
            int[] dotPosition;
            int j = -1, i = 0;
            do {
                j = id0.indexOf('.', j + 1);
                i++;
            } while (j != -1);
            if (i == 1) throw new SMIMEException(this, 1008);
            temp = new int[i];
            dotPosition = new int[i - 1];
            i = 0;
            j = -1;
            do {
                j = id0.indexOf('.', j + 1);
                if (j != -1) dotPosition[i] = j;
                i++;
            } while (j != -1);
            temp[0] = Integer.decode(id0.substring(0, dotPosition[0])).intValue();
            temp[temp.length - 1] = Integer.decode(id0.substring(dotPosition[dotPosition.length - 1] + 1)).intValue();
            for (i = 1; i != temp.length - 1; i++) temp[i] = Integer.decode(id0.substring(dotPosition[i - 1] + 1, dotPosition[i])).intValue();
            contentID = formatID(temp);
            super.addContent(contentID);
        } else if (typeConstruction0.equalsIgnoreCase("NAME_STRING")) {
            contentID = formatID(IdentifierStorage.getID(id0.toUpperCase()));
            super.addContent(contentID);
        } else throw new SMIMEException(this, 1009);
    }

    /**
     * Array of numbers is used for construction of DERObjectIdentifier. Every number in
     * array represents one number between dots in the object identifier string.
     * @param arrayID0 array of given numbers (example: for RSA algorithm those
     * numbers are 1, 2, 840, 113549, 1, 1, and 1).
     * @exception SMIMEException if wrong type of parameters are passed to the
     * constructor. Also, exception could be thrown in super class constructor or
     * in super class addContent method.
     */
    public DERObjectIdentifier(int[] arrayID0) throws SMIMEException {
        super(6);
        super.addContent(formatID(arrayID0));
    }

    /**
     * Creats Object Identifier octet string from discret number identifiers
     * @param id0 array of defined numbers for defined Object Identifier
     * @return Byte array representation of the DER encoded content of the Object
     * Identifier
     * @exception SMIMEException in case that unknown type of object identifier is
     * submited to constructors dealing with DOT_SEPARATED_ARRAY and NAME_STRING.
     * Also, it can be caused by non SMIMEException which is:
     * UnsupportedEncodingException.
     */
    private byte[] formatID(int[] id0) throws SMIMEException {
        int[] temp = new int[id0.length - 1];
        String s = new String();
        byte[] returnByteArray = null;
        if (id0[0] == -1) throw new SMIMEException(this, 1010);
        temp[0] = 40 * id0[0] + id0[1];
        for (int i = 2; i != id0.length; i++) temp[i - 1] = id0[i];
        try {
            for (int i = 0; i != temp.length; i++) {
                int j = 1;
                for (int a = 1; (a * 2) <= temp[i]; j++) a = a * 2;
                j = (int) Math.ceil((double) j / 7);
                byte[] tempElement = new byte[j];
                for (j = tempElement.length - 1; j >= 0; j--) {
                    tempElement[j] = (byte) ((temp[i] >> (7 * (tempElement.length - 1 - j))) & 0x7F);
                    if (j != (tempElement.length - 1)) tempElement[j] = (byte) (tempElement[j] | (0x80));
                }
                s = s.concat(new String(tempElement, "ISO-8859-1"));
            }
            returnByteArray = s.getBytes("ISO-8859-1");
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "formatID");
        }
        return returnByteArray;
    }
}
