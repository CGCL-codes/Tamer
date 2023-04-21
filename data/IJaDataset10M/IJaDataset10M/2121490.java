package de.ulrich_fuchs.jtypeset;

import java.awt.image.BufferedImage;

/**
 * A plugin is an additional module that is able to render 
 * a specific string. The renderend string is returned as a buffered image
 * @author ulrich
 *
 */
public interface Plugin {

    /** Renders the image*/
    BufferedImage render(String s, int dpi);

    /**
	 * Renders the image for a given maximal width
	 * @param s	The string to render
	 * @param maxWidth the maximal width (given 
	 * 		  in typographical points, pt) that is available to render the image
	 * @param dpi the dots per inch resolution to use
	 * @return
	 */
    BufferedImage renderMaxWidth(String s, int maxWidth, int dpi);
}
