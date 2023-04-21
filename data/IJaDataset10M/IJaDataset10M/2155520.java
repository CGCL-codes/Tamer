package org.epo.jpxi.format;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.epo.jpxi.shared.JpxiException;
import org.epoline.jsf.utils.Log4jManager;

/**
 * This class help to build a G4 CCITT Tiff object
 * Creation date: (26/07/01 17:19:31)
 */
public class TiffFileG4BNS {

    public static final int INTEL = 1;

    public static final int MOTOROLA = 2;

    private int platForm = 0;

    private byte[][] tableIFD = null;

    private int addrIFDTable = 0;

    private int addrImage = 0;

    private short nbIFDEntry = 0;

    private int width = 0;

    private int height = 0;

    private short resolution = 0;

    private int imageLength = 0;

    /**
 * Convert a byte[] into a int
 * This method is used to convert a byte array into an integer.
 * The conversion is function of the kind of encoding used and given by theMode.
 * @return int
 * @param theData byte[]
*/
    private int byteToInt(byte[] theData) {
        int[] myTemp = new int[4];
        if (getMode() == INTEL) {
            myTemp[0] = theData[0] & 0xFF;
            myTemp[1] = theData[1] & 0xFF;
            myTemp[2] = theData[2] & 0xFF;
            myTemp[3] = theData[3] & 0xFF;
        } else {
            myTemp[0] = theData[3] & 0xFF;
            myTemp[1] = theData[2] & 0xFF;
            myTemp[2] = theData[1] & 0xFF;
            myTemp[3] = theData[0] & 0xFF;
        }
        int myReturnSize = (myTemp[3] << 24) + (myTemp[2] << 16) + (myTemp[1] << 8) + myTemp[0];
        return myReturnSize;
    }

    /**
 * Convert a byte[] into a short
 * This method is used to convert a byte array into a short.
 * The conversion is function of the kind of encoding used and given by theMode.
 * @return short
 * @param theData byte[]
*/
    private short byteToShort(byte[] theData) {
        int[] myIntTab = new int[2];
        if (getMode() == INTEL) {
            myIntTab[0] = theData[0] & 0xFF;
            myIntTab[1] = theData[1] & 0xFF;
        } else {
            myIntTab[0] = theData[1] & 0xFF;
            myIntTab[1] = theData[0] & 0xFF;
        }
        int myReturnSize = (myIntTab[1] << 8) + myIntTab[0];
        return (short) myReturnSize;
    }

    /**
 * This method Create an IFD Table for a Tiff File
 * @param  theWidth  	int
 * @param  theHeight 	int
 * @param  theImageSize int
 */
    public void createTableIFD(int theWidth, int theHeight, int theImageSize) {
        tableIFD = new byte[9][12];
        byte[][] IFDTagArray = { { (byte) 0xFE, 0x00, 0x04, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, { 0x00, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, { 0x01, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, { 0x03, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00 }, { 0x06, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, { 0x0A, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00 }, { 0x11, 0x01, 0x04, 0x00, 0x01, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00 }, { 0x16, 0x01, 0x04, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, { 0x17, 0x01, 0x04, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 } };
        byte[] intByte = new byte[4];
        intByte = intToByte(theWidth);
        System.arraycopy(intByte, 0, IFDTagArray[1], 8, 4);
        intByte = intToByte(theHeight);
        System.arraycopy(intByte, 0, IFDTagArray[2], 8, 4);
        intByte = intToByte(theHeight);
        System.arraycopy(intByte, 0, IFDTagArray[7], 8, 4);
        intByte = intToByte(theImageSize);
        System.arraycopy(intByte, 0, IFDTagArray[8], 8, 4);
        for (int cpt = 0; cpt < 9; cpt++) {
            System.arraycopy(IFDTagArray[cpt], 0, tableIFD[cpt], 0, 12);
        }
        setNbIFDEntry((short) 9);
        setAddrImage(8);
    }

    /**
 * Standard accessor, return the address of the IFD Table
 * @return int
 */
    public int getAddrIFDTable() {
        return addrIFDTable;
    }

    /**
 * Standard accessor, return the address of the image
 * @return int
 */
    public int getAddrImage() {
        return addrImage;
    }

    /**
 * Standard accessor, return the height
 * @return int
 */
    public int getHeight() {
        return height;
    }

    /**
 * Standard accessor, return the image length
 * @return int
 */
    public int getImageLength() {
        return imageLength;
    }

    /**
 * Standard accessor, return the number of TIFF IFD entry
 * @return short
 */
    public short getNbIFDEntry() {
        return nbIFDEntry;
    }

    /**
 * Standard accessor, return the plateform
 * @return int
 */
    public int getPlatForm() {
        return platForm;
    }

    /**
 * Standard accessor, return the image resolution
 * @return short
 */
    public short getResolution() {
        return resolution;
    }

    /**
 * Standard accessor, return the IFD table
 * @return byte[][]
 */
    public byte[][] getTableIFD() {
        return tableIFD;
    }

    /**
 * Standard accessor, return a value from the IFD
 * @return int
 * @param theCode short
 */
    public int getValueFromIFD(short theCode) {
        int myCpt = 0;
        short myShortValue = 0;
        byte[] shortByte = new byte[2];
        byte[] intByte = new byte[4];
        for (myCpt = 0; myCpt < getTableIFD().length; myCpt++) {
            System.arraycopy(getTableIFD()[myCpt], 0, shortByte, 0, 2);
            myShortValue = byteToShort(shortByte);
            if (myShortValue == theCode) {
                System.arraycopy(getTableIFD()[myCpt], 2, shortByte, 0, 2);
                myShortValue = byteToShort(shortByte);
                if (myShortValue == 3) {
                    System.arraycopy(getTableIFD()[myCpt], 8, shortByte, 0, 2);
                    return (int) byteToShort(shortByte);
                }
                if (myShortValue == 4) {
                    System.arraycopy(getTableIFD()[myCpt], 4, intByte, 0, 4);
                    int myIntValue = (int) byteToInt(intByte);
                    if (myIntValue == 1) {
                        System.arraycopy(getTableIFD()[myCpt], 8, intByte, 0, 4);
                        return (int) byteToInt(intByte);
                    } else return -2;
                }
            }
        }
        return -1;
    }

    /**
 * Standard accessor, the Tiff image width
 * @return int
 */
    public int getWidth() {
        return width;
    }

    /**
 * The intToByte(int intNumber) method convert an integer value into its byte array representation.
 * @return byte[]
 * @param intNumber int
 * Conversion of an integer into a 4 bytes table
 * The given byte array is formatted for the TIFF mode used.
 */
    private byte[] intToByte(int intNumber) {
        byte[] myByteTab = new byte[4];
        if (getPlatForm() == MOTOROLA) {
            myByteTab[3] = (byte) (intNumber & 0xFF);
            myByteTab[2] = (byte) ((intNumber >> 8) & 0xFF);
            myByteTab[1] = (byte) ((intNumber >> 16) & 0xFF);
            myByteTab[0] = (byte) ((intNumber >> 24) & 0xFF);
        } else {
            myByteTab[0] = (byte) (intNumber & 0xFF);
            myByteTab[1] = (byte) ((intNumber >> 8) & 0xFF);
            myByteTab[2] = (byte) ((intNumber >> 16) & 0xFF);
            myByteTab[3] = (byte) ((intNumber >> 24) & 0xFF);
        }
        if (getMode() != getPlatForm()) {
            myByteTab = reverseData(myByteTab);
        }
        return myByteTab;
    }

    /**
 * The reverseData(byte[] inArray) method reverse a byte array.
 * @return byte[] : the reversed array
 * @param inArray byte[] : the array to reverse
 */
    protected static byte[] reverseData(byte[] inArray) {
        byte[] outArray = new byte[inArray.length];
        for (int i = 0; i < inArray.length; i++) outArray[i] = inArray[inArray.length - i - 1];
        return outArray;
    }

    /**
 * Standard accessor, return IFD table address
 * @param newValue int
 */
    private void setAddrIFDTable(int newValue) {
        this.addrIFDTable = newValue;
    }

    /**
 * Standard accessor, return the image data adress
 * @param newValue int
 */
    private void setAddrImage(int newValue) {
        this.addrImage = newValue;
    }

    /**
 * Standard accessor, set the tif height.
 * @param newValue int
 */
    private void setHeight(int newValue) {
        this.height = newValue;
    }

    /**
 * Standard accessor, set the image length.
 * @param newValue int
 */
    private void setImageLength(int newValue) {
        this.imageLength = newValue;
    }

    /**
 * Standard accessor, set the number of entry in the
 * IFD table
 * @param newValue int
 */
    private void setNbIFDEntry(short newValue) {
        this.nbIFDEntry = newValue;
    }

    /**
 * Standard accessor, set the platform in use.
 * @param newValue int
 */
    private void setPlatForm(int newValue) {
        platForm = newValue;
    }

    /**
 * Standard accessor, set the table on IFD.
 * @param newValue byte[][]
 */
    private void setTableIFD(byte[][] newValue) {
        this.tableIFD = newValue;
    }

    /**
 * Standard accessor, set the width.
 * @param newValue int
 */
    private void setWidth(int newValue) {
        this.width = newValue;
    }

    /**
 * The shortToByte(short intNumber) method convert a short value into its byte array representation.
 * @return byte[]
 * @param shortNumber int
 * Conversion of a short into a 2 bytes table
 * The given byte array is formatted for the TIFF mode used.
 */
    private byte[] shortToByte(short shortNumber) {
        byte[] myByteTab = new byte[2];
        if (getPlatForm() == MOTOROLA) {
            myByteTab[1] = (byte) (shortNumber & 0xFF);
            myByteTab[0] = (byte) ((shortNumber >> 8) & 0xFF);
        } else {
            myByteTab[0] = (byte) (shortNumber & 0xFF);
            myByteTab[1] = (byte) ((shortNumber >> 8) & 0xFF);
        }
        if (getMode() != getPlatForm()) myByteTab = reverseData(myByteTab);
        return myByteTab;
    }

    /**
 * This method Load IFD Table of Tiff File
 * @param theTiffFile java.io.RandomAccessFile
 */
    public void loadTableIFD(byte[] theTiffData) throws JpxiException {
        byte[] myIFDEntryByte = new byte[2];
        byte[][] myTableIFD = null;
        System.arraycopy(theTiffData, getAddrIFDTable(), myIFDEntryByte, 0, 2);
        setNbIFDEntry(byteToShort(myIFDEntryByte));
        myTableIFD = new byte[getNbIFDEntry()][12];
        int myOffset = 0;
        for (int myCpt = 0; myCpt < getNbIFDEntry(); myCpt++) {
            System.arraycopy(theTiffData, getAddrIFDTable() + 2 + myOffset, myTableIFD[myCpt], 0, 12);
            myOffset += 12;
        }
        setTableIFD(myTableIFD);
        int imgAddr = getValueFromIFD((short) 273);
        if (imgAddr == -2) throw new JpxiException(JpxiException.ERR_CONVERT_INVALID_TIFF, "Indirect reference from IFD table (Image Address)");
        setAddrImage(imgAddr);
        setWidth(getValueFromIFD((short) 256));
        setHeight(getValueFromIFD((short) 257));
        int imgLgth = getValueFromIFD((short) 279);
        if (imgLgth == -2) throw new JpxiException(JpxiException.ERR_CONVERT_INVALID_TIFF, "Indirect reference from IFD table (Image Length)");
        setImageLength(imgLgth);
    }

    private byte[] header = null;

    /**
	 * the header size
	 */
    public static final int HEADER_SIZE = 8;

    /**
	 * the image height
	 */
    public static final int IMAGE_HEIGHT = 3508;

    /**
	 * the image width
	 */
    public static final int IMAGE_WIDTH = 2592;

    private static final byte INTEL_CODE = 0x49;

    private int mode = 0;

    private static final byte MOTOROLA_CODE = 0x4D;

    private String name = null;

    /**
 * TiffFileG4BNS standard constructor.
 */
    public TiffFileG4BNS() {
        setName("TiffFileG4BNS");
    }

    /**
 * TiffFile TiffFileG4BNS
 * TiffFileG4BNS(JpxiTrace theTrace, int theMode, int thePlatform, int theIFDAddress)
 * theMode the tiff mode INTEL/MOTOROLA
 * the Platform used to create the tiff
 * theIFDAddress :represent the adress of the Tiff IFD
 *
 */
    public TiffFileG4BNS(int theMode, int thePlatform, int theIFDAddress) {
        setMode(theMode);
        setPlatForm(thePlatform);
        byte[] myHeader = new byte[HEADER_SIZE];
        switch(getMode()) {
            case INTEL:
                myHeader[0] = INTEL_CODE;
                myHeader[1] = INTEL_CODE;
                myHeader[2] = 0x2a;
                myHeader[3] = 0x00;
                break;
            case MOTOROLA:
                myHeader[0] = MOTOROLA_CODE;
                myHeader[1] = MOTOROLA_CODE;
                myHeader[2] = 0x00;
                myHeader[3] = 0x2a;
                break;
        }
        setAddrIFDTable(theIFDAddress);
        byte[] myIFDOffset = new byte[4];
        myIFDOffset = intToByte(theIFDAddress);
        System.arraycopy(myIFDOffset, 0, myHeader, 4, 4);
        setHeader(myHeader);
    }

    /**
 * Standard accessor, return a tiff header
 * Creation date: (01/10/01 14:44:27)
 * @return byte[]
 */
    public byte[] getHeader() {
        return header;
    }

    /**
 * Standard accessor, return the tiff mode
 * Creation date: (01/10/01 14:44:27)
 * @return int
 */
    public int getMode() {
        return mode;
    }

    /**
 * This method load a TIFF header
 * @param theTiffFile byte[]
 */
    public void loadHeader(byte[] theData) throws JpxiException {
        try {
            byte[] myHeader = new byte[HEADER_SIZE];
            System.arraycopy(theData, 0, myHeader, 0, HEADER_SIZE);
            if (myHeader[0] == INTEL_CODE && myHeader[1] == INTEL_CODE) setMode(INTEL);
            if (myHeader[0] == MOTOROLA_CODE && myHeader[1] == MOTOROLA_CODE) setMode(MOTOROLA);
            if (getMode() == 0) throw new JpxiException(JpxiDocFormat.ERR_NOT_A_VALID_TIFF);
            setHeader(myHeader);
            byte[] myIntByte = new byte[4];
            System.arraycopy(myHeader, 4, myIntByte, 0, 4);
            int myIFDAddress = byteToInt(myIntByte);
            setAddrIFDTable(myIFDAddress);
        } catch (Exception e) {
            throw new JpxiException(JpxiDocFormat.ERR_NOT_A_VALID_TIFF);
        }
    }

    /**
 * Standard accessor, set the tif header
 * Creation date: (01/10/01 14:44:27)
 * @param newHeader byte[]
 */
    private void setHeader(byte[] newHeader) {
        header = newHeader;
    }

    /**
 * Standard accessor, set the tiff image mode.
 * Creation date: (01/10/01 14:44:27)
 * @param newMode int
 */
    private void setMode(int newMode) {
        mode = newMode;
    }

    /**
 * Standard accessor, set the class name.
 * Creation date: (21/09/01 16:37:06)
 * @param newName java.lang.String
 */
    private void setName(java.lang.String newName) {
        name = newName;
    }

    /**
 * This method return the IFD Table 
 * @return byte[]
 */
    public byte[] writeTableIFD() {
        byte[] shortByte = shortToByte(getNbIFDEntry());
        byte[] byteToReturn;
        int totalSizeOfTableIFD = 0;
        int offset = 0;
        java.util.Vector myVector = new java.util.Vector();
        myVector.addElement(shortByte);
        totalSizeOfTableIFD += shortByte.length;
        for (int myCpt = 0; myCpt < getTableIFD().length; myCpt++) {
            totalSizeOfTableIFD += ((getTableIFD())[myCpt]).length;
            myVector.addElement((getTableIFD())[myCpt]);
        }
        byteToReturn = new byte[totalSizeOfTableIFD + 4];
        Iterator myIt = myVector.iterator();
        while (myIt.hasNext()) {
            byte[] tmp = (byte[]) myIt.next();
            System.arraycopy(tmp, 0, byteToReturn, offset, tmp.length);
            offset += tmp.length;
        }
        byte[] tmp = new byte[4];
        for (int i = 0; i < 4; i++) tmp[i] = 0x00;
        System.arraycopy(tmp, 0, byteToReturn, offset, tmp.length);
        return byteToReturn;
    }

    /**
 * This method return the Tiff Header
 * @return byte[]
 */
    public byte[] writeTiffHeader() {
        if (getHeader() != null) return getHeader(); else return null;
    }
}
