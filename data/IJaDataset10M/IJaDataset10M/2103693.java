package hu.jmemoryeditorw.jna;

import com.sun.jna.Structure;

/**
 * The RGBQUAD structure describes a color consisting of relative intensities of red, green, and blue.
 * @author karnokd, 2008.12.16.
 * @version $Revision 1.0$
 */
public class RGBQuad extends Structure {

    /** Specifies the intensity of blue in the color. */
    public byte rgbBlue;

    /** Specifies the intensity of green in the color. */
    public byte rgbGreen;

    /** Specifies the intensity of red in the color. */
    public byte rgbRed;

    /** Reserved; must be zero. */
    public byte rgbReserved;
}
