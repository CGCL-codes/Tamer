package non_com.media.jai.codec;

import java.io.Serializable;

/**
 *  A class representing the fields of a PNG suggested palette entry. <p>
 *
 *  <b> This class is not a committed part of the JAI API. It may be removed or
 *  changed in future releases of JAI.</b>
 */
public class PNGSuggestedPaletteEntry implements Serializable {

    /**
   *  The name of the entry.
   */
    public String name;

    /**
   *  The depth of the color samples.
   */
    public int sampleDepth;

    /**
   *  The red color value of the entry.
   */
    public int red;

    /**
   *  The green color value of the entry.
   */
    public int green;

    /**
   *  The blue color value of the entry.
   */
    public int blue;

    /**
   *  The alpha opacity value of the entry.
   */
    public int alpha;

    /**
   *  The probable frequency of the color in the image.
   */
    public int frequency;
}
