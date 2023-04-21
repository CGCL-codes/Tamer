package net.sourceforge.javafpdf;

/**
 * DOCME
 * 
 * @author pluma
 * @since 4 Mar 2008
 * @version $Rev: 8 $
 */
public class ImageInfo {

    protected static enum ColorSpace {

        /** Grayscale. */
        DEVICEGRAY("DeviceGray"), /** RGB colors. */
        DEVICERGB("DeviceRGB"), /** Indexed colors. */
        INDEXED("Indexed");

        private final String string;

        private ColorSpace(final String string) {
            this.string = string;
        }

        /**
		 * @see java.lang.Enum#toString()
		 */
        @Override
        public String toString() {
            return this.string;
        }
    }

    /** Image index. */
    private final int i;

    /** Image number. */
    private int n;

    /** Image width. */
    private final float w;

    /** Image height. */
    private final float h;

    /** Color Space. */
    private final String cs;

    /** Palette. */
    private final String pal;

    /** Bits per channel? */
    private final int bpc;

    /** Filter? Known value: "FlateDecode" */
    private final String f;

    /** DecodeParms */
    private final String parms;

    /** Transparency mask. */
    private final int[] trns;

    /** Image data. */
    private final char[] data;

    /**
	 * DOCME Constructor.
	 * 
	 * @param i
	 * @param w
	 * @param h
	 * @param cs
	 * @param pal
	 * @param bpc
	 * @param f
	 * @param parms
	 * @param trns
	 * @param data
	 */
    public ImageInfo(final int i, final float w, final float h, final String cs, final String pal, final int bpc, final String f, final String parms, final int[] trns, final char[] data) {
        this.i = i;
        this.w = w;
        this.h = h;
        this.cs = cs;
        this.pal = pal;
        this.bpc = bpc;
        this.f = f;
        this.parms = parms;
        this.trns = trns;
        this.data = data;
    }

    /**
	 * DOCME
	 * 
	 * @return the n
	 */
    public int getN() {
        return this.n;
    }

    /**
	 * DOCME
	 * 
	 * @param n
	 *            the n to set
	 */
    public void setN(final int n) {
        this.n = n;
    }

    /**
	 * DOCME
	 * 
	 * @return the i
	 */
    public int getI() {
        return this.i;
    }

    /**
	 * DOCME
	 * 
	 * @return the w
	 */
    public float getW() {
        return this.w;
    }

    /**
	 * DOCME
	 * 
	 * @return the h
	 */
    public float getH() {
        return this.h;
    }

    /**
	 * DOCME
	 * 
	 * @return the cs
	 */
    public String getCs() {
        return this.cs;
    }

    /**
	 * DOCME
	 * 
	 * @return the pal
	 */
    public String getPal() {
        return this.pal;
    }

    /**
	 * DOCME
	 * 
	 * @return the bpc
	 */
    public int getBpc() {
        return this.bpc;
    }

    /**
	 * DOCME
	 * 
	 * @return the f
	 */
    public String getF() {
        return this.f;
    }

    /**
	 * DOCME
	 * 
	 * @return the parms
	 */
    public String getParms() {
        return this.parms;
    }

    /**
	 * DOCME
	 * 
	 * @return the trns
	 */
    public int[] getTrns() {
        return this.trns;
    }

    /**
	 * DOCME
	 * 
	 * @return the data
	 */
    public char[] getData() {
        return this.data;
    }
}
