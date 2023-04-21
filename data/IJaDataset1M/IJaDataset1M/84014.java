package org.marre.wap.util;

import java.io.*;
import org.marre.wap.*;
import org.marre.mime.*;

/**
 *
 * @author Markus Eriksson
 * @version @version $Id: WspUtil.java 149 2002-09-15 19:45:35Z c95men $
 */
public class WspUtil {

    private WspUtil() {
    }

    /**
     * Writes a "uint8" in wsp format to the given output stream.
     *
     * @param theOs Stream to write to
     * @param theValue Value to write
     */
    public static void writeUint8(OutputStream theOs, int theValue) throws IOException {
        theOs.write(theValue);
    }

    /**
     * Writes a "Uintvar" in wsp format to the given output stream.
     *
     * @param theOs Stream to write to
     * @param theValue Value to write
     */
    public static void writeUintvar(OutputStream theOs, long theValue) throws IOException {
        int nOctets = 1;
        while ((theValue >> (7 * nOctets)) > 0) {
            nOctets++;
        }
        for (int i = nOctets; i > 0; i--) {
            byte octet = (byte) (theValue >> (7 * (i - 1)));
            byte byteValue = (byte) ((byte) octet & (byte) 0x7f);
            if (i > 1) {
                byteValue = (byte) (byteValue | (byte) 0x80);
            }
            theOs.write(byteValue);
        }
    }

    /**
     * Writes a "long integer" in wsp format to the given output stream.
     *
     * @param theOs Stream to write to
     * @param theValue Value to write
     */
    public static void writeLongInteger(OutputStream theOs, long theValue) throws IOException {
        int nOctets = 0;
        while ((theValue >> (8 * nOctets)) > 0) {
            nOctets++;
        }
        theOs.write((byte) nOctets);
        for (int i = nOctets; i > 0; i--) {
            byte octet = (byte) (theValue >> (8 * (i - 1)));
            byte byteValue = (byte) ((byte) octet & (byte) (0xff));
            theOs.write(byteValue);
        }
    }

    /**
     * Writes an "integer" in wsp format to the given output stream.
     *
     * @param theOs
     * @param theValue
     */
    public static void writeInteger(OutputStream theOs, long theValue) throws IOException {
        if (theValue < 128) {
            writeShortInteger(theOs, (int) theValue);
        } else {
            writeLongInteger(theOs, theValue);
        }
    }

    /**
     * Writes a "short integer" in wsp format to the given output stream.
     *
     * @param theOs Stream to write to
     * @param theValue Value to write
     */
    public static void writeShortInteger(OutputStream theOs, int theValue) throws IOException {
        theOs.write((byte) (theValue | (byte) 0x80));
    }

    public static void writeValueLength(OutputStream theOs, long theValue) throws IOException {
        if (theValue <= 30) {
            theOs.write((int) theValue);
        } else {
            theOs.write(31);
            writeUintvar(theOs, theValue);
        }
        theOs.write((byte) (theValue | (byte) 0x80));
    }

    /**
     * Writes an "extension media" in pdu format to the given output stream.
     * It currently only handles ASCII chars, but should be extended to
     * work with other charsets.
     *
     * @param theOs Stream to write to
     * @param theStr Text to write
     */
    public static void writeExtensionMedia(OutputStream theOs, String theStr) throws IOException {
        theOs.write(theStr.getBytes("ISO-8859-1"));
        theOs.write((byte) 0x00);
    }

    public static void writeTextString(OutputStream theOs, String theStr) throws IOException {
        byte strBytes[] = theStr.getBytes("ISO-8859-1");
        if ((strBytes[0] & 0x80) > 0x00) {
            theOs.write(0x7f);
        }
        theOs.write(strBytes);
        theOs.write(0x00);
    }

    public static void writeQuotedString(OutputStream theOs, String theStr) throws IOException {
        theOs.write(34);
        theOs.write(theStr.getBytes("ISO-8859-1"));
        theOs.write(0x00);
    }

    public static void writeTokenText(OutputStream theOs, String theStr) throws IOException {
        theOs.write(theStr.getBytes("ISO-8859-1"));
        theOs.write(0x00);
    }

    public static void writeTextValue(OutputStream theOs, String theStr) throws IOException {
        writeQuotedString(theOs, theStr);
    }

    /**
     * Writes a wsp encoded content-type as specified in
     * WAP-230-WSP-20010705-a.pdf.
     * <p>
     * Uses the "constrained media" format.<br>
     * Note! This method can only be used on simple content types (like
     * "text/plain" or "image/gif"). If a more complex content-type is needed
     * (like "image/gif; start=cid; parameter=value;") you must use the
     * MimeContentType class.
     *
     * @param theOs
     * @param theContentType
     * @throws IOException
     */
    public static void writeContentType(OutputStream theOs, String theContentType) throws IOException {
        int wellKnownContentType = findContentType(theContentType);
        if (wellKnownContentType == -1) {
            writeExtensionMedia(theOs, theContentType);
        } else {
            writeShortInteger(theOs, wellKnownContentType);
        }
    }

    /**
     * Writes a wsp encoded content-type as specified in
     * WAP-230-WSP-20010705-a.pdf.
     * <p>
     * This method automatically chooses the most compact way to represent
     * the given content type.
     *
     * @param theOs
     * @param theContentType
     * @throws IOException
     */
    public static void writeContentType(OutputStream theOs, MimeContentType theContentType) throws IOException {
        if (theContentType.getParamCount() == 0) {
            writeContentType(theOs, theContentType.getValue());
        } else {
            int wellKnownContentType = findContentType(theContentType.getValue());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (wellKnownContentType == -1) {
                writeExtensionMedia(baos, theContentType.getValue());
            } else {
                writeInteger(baos, wellKnownContentType);
            }
            for (int i = 0; i < theContentType.getParamCount(); i++) {
                MimeHeaderParam headerParam = theContentType.getParam(i);
                writeParameter(baos, headerParam.getName(), headerParam.getValue());
            }
            baos.close();
            writeValueLength(theOs, baos.size());
            theOs.write(baos.toByteArray());
        }
    }

    public static void writeTypedValue(OutputStream os, int paramType, String value) throws IOException {
        switch(paramType) {
            case WapConstants.PARAMETER_TYPE_NO_VALUE:
                os.write(0x00);
                break;
            case WapConstants.PARAMETER_TYPE_TEXT_VALUE:
                writeTextValue(os, value);
                break;
            case WapConstants.PARAMETER_TYPE_INTEGER_VALUE:
                writeInteger(os, Long.parseLong(value));
                break;
            case WapConstants.PARAMETER_TYPE_DATE_VALUE:
                writeTextString(os, value);
                break;
            case WapConstants.PARAMETER_TYPE_DELTA_SECONDS_VALUE:
                writeTextString(os, value);
                break;
            case WapConstants.PARAMETER_TYPE_Q_VALUE:
                writeTextString(os, value);
                break;
            case WapConstants.PARAMETER_TYPE_VERSION_VALUE:
                writeTextString(os, value);
                break;
            case WapConstants.PARAMETER_TYPE_URI_VALUE:
                writeTextString(os, value);
                break;
            case WapConstants.PARAMETER_TYPE_TEXT_STRING:
                writeTextString(os, value);
                break;
            case WapConstants.PARAMETER_TYPE_WELL_KNOWN_CHARSET:
                writeTextString(os, value);
                break;
            case WapConstants.PARAMETER_TYPE_FIELD_NAME:
                writeTextString(os, value);
                break;
            case WapConstants.PARAMETER_TYPE_SHORT_INTEGER:
                writeShortInteger(os, Integer.parseInt(value));
                break;
            case WapConstants.PARAMETER_TYPE_CONSTRAINED_ENCODING:
                writeContentType(os, value);
                break;
            default:
                writeTextString(os, value);
                break;
        }
    }

    public static void writeParameter(OutputStream os, String name, String value) throws IOException {
        int wellKnownParameter = findParameter(name);
        if (wellKnownParameter == -1) {
            writeTokenText(os, name);
            writeTextString(os, value);
        } else {
            writeInteger(os, wellKnownParameter);
            writeTypedValue(os, WapConstants.PARAMETER_TYPES[wellKnownParameter], value);
        }
    }

    public static void writeHeader(OutputStream theOs, MimeHeader theHeader) throws IOException {
        String name = theHeader.getName();
        if ("content-location".equalsIgnoreCase(name)) {
            writeHeaderContentLocation(theOs, theHeader.getValue());
        } else if ("content-id".equalsIgnoreCase(name)) {
            writeHeaderContentID(theOs, theHeader.getValue());
        }
    }

    /**
     * Writes a wsp encoded content-id header as specified in
     * WAP-230-WSP-20010705-a.pdf.
     *
     * @param theOs
     * @param theContentLocation
     * @throws IOException
     */
    public static void writeHeaderContentID(OutputStream theOs, String theContentId) throws IOException {
        WspUtil.writeShortInteger(theOs, WapConstants.HEADER_ID_CONTENT_ID);
        writeQuotedString(theOs, theContentId);
    }

    /**
     * Writes a wsp encoded content-location header as specified in
     * WAP-230-WSP-20010705-a.pdf.
     *
     * @param theOs
     * @param theContentLocation
     * @throws IOException
     */
    public static void writeHeaderContentLocation(OutputStream theOs, String theContentLocation) throws IOException {
        WspUtil.writeShortInteger(theOs, WapConstants.HEADER_ID_CONTENT_LOCATION);
        writeTextString(theOs, theContentLocation);
    }

    /**
     * Writes a wsp encoded X-Wap-Application-Id header as specified in
     * WAP-230-WSP-20010705-a.pdf.
     *
     * @param theOs
     * @param theAppId
     * @throws IOException
     */
    public static void writeHeaderXWapApplicationId(OutputStream theOs, String theAppId) throws IOException {
        int wellKnownAppId = findPushAppId(theAppId);
        WspUtil.writeShortInteger(theOs, WapConstants.HEADER_ID_X_WAP_APPLICATION_ID);
        if (wellKnownAppId == -1) {
            writeTextString(theOs, theAppId);
        } else {
            writeInteger(theOs, wellKnownAppId);
        }
    }

    /**
     *
     * @param stringTable
     * @param text
     * @return
     */
    public static int findString(String stringTable[], String text) {
        if (stringTable != null) {
            for (int i = 0; i < stringTable.length; i++) {
                if (stringTable[i].equals(text)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Finds an WINA assigned number for the given parameter.
     *
     * @param theParameter
     * @return WINA assigned number if found, otherwise -1
     */
    public static final int findParameter(String theParameter) {
        return findString(WapConstants.PARAMETER_NAMES, theParameter.toLowerCase());
    }

    /**
     * Finds an WINA assigned number for the given contenttype.
     *
     * @param theContentType
     * @return WINA assigned number if found, otherwise -1
     */
    public static final int findContentType(String theContentType) {
        return findString(WapConstants.CONTENT_TYPES, theContentType.toLowerCase());
    }

    /**
     * Finds an WINA "well known" number for the given push app id
     *
     * @param theContentType
     * @return WINA assigned number if found, otherwise -1
     */
    public static final int findPushAppId(String thePushAppId) {
        return findString(WapConstants.PUSH_APP_IDS, thePushAppId.toLowerCase());
    }
}
