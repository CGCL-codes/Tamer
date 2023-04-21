package jOptical.Bitmap;

/**
 * Represents an image on which operations can be performed.
 * 
 * @author dennis
 */
public final class BitmapObject {

    public Pixel[][] bitMap;

    private int width;

    private int height;

    private int colourDepth;

    /**
	 * Constructs a new image.
	 * 
	 * @param bitMap
	 *            a map of pixels, may be null if a new empty map is to be
	 *            generated.
	 * @param width
	 *            the width of the new image.
	 * @param height
	 *            the height of the new image.
	 * @param colourDepth
	 *            the colour depth to be used.
	 */
    public BitmapObject(Pixel[][] bitMap, int width, int height, int colourDepth) {
        if (bitMap == null || width <= 0 || height <= 0 || colourDepth <= 0 || colourDepth > 32) throw new IllegalArgumentException("Invalid bitmap object parameters");
        this.bitMap = bitMap;
        this.width = width;
        this.height = height;
        this.colourDepth = colourDepth;
    }

    /**
	 * Copy constructor
	 * 
	 * @param object
	 *            the object to copy.
	 */
    public BitmapObject(BitmapObject object) {
        Pixel p;
        if (object == null) throw new IllegalArgumentException("Object to copy was null");
        this.bitMap = new Pixel[object.height][object.width];
        for (int i = 0; i < object.height; i++) {
            for (int j = 0; j < object.width; j++) {
                p = object.bitMap[i][j];
                if (p == null) p = null;
                this.bitMap[i][j] = new Pixel(p.red, p.green, p.blue);
            }
        }
        this.width = object.width;
        this.height = object.height;
        this.colourDepth = object.colourDepth;
    }

    /**
	 * @return the width
	 */
    public int getWidth() {
        return width;
    }

    /**
	 * @return the height
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * @return the colourDepth
	 */
    public int getColourDepth() {
        return colourDepth;
    }
}
