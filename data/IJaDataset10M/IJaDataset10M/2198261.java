package org.algoristes.alkwarel.core;

/**
 * @author xgouchet
 * 
 */
public abstract class Filter extends Tool {

    /**
	 * @param name
	 */
    protected Filter(String name) {
        super(name);
    }

    /**
	 * Filters the given Image and returns a new image. The source image should
	 * not be modified, and only read to create the output Image.
	 * 
	 * @param input
	 *            the source image to filter
	 * @return the image resulting from applying the filter on the source image
	 * 
	 */
    public abstract Image filterImage(Image input);
}
