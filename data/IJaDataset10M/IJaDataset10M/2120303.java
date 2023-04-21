package org.apache.batik.ext.awt.image;

/**
 * IdentityTransfer.java
 *
 * This class defines the Identity type transfer function for the
 * feComponentTransfer filter, as defined in chapter 15, section 11 of the SVG
 * specification.
 *
 * @author <a href="mailto:sheng.pei@sun.com">Sheng Pei</a>
 * @version $Id: IdentityTransfer.java,v 1.1 2005/11/21 09:51:39 dev Exp $ 
 */
public class IdentityTransfer implements TransferFunction {

    /**
     * This byte array stores the lookuptable data
     */
    public static byte[] lutData = new byte[256];

    static {
        for (int j = 0; j <= 255; j++) {
            lutData[j] = (byte) j;
        }
    }

    /**
     * This method will return the lut data in order
     * to construct a LookUpTable object
     */
    public byte[] getLookupTable() {
        return lutData;
    }
}
