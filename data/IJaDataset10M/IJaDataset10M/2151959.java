package org.ihash.processing;

import java.awt.Rectangle;
import java.awt.image.Raster;
import java.util.Collection;

/**
 * A common interface for services that can extract image features.
 * 
 * @author Gergely Kiss
 */
public interface FeatureExtractor {

    /**
	 * Extracts the features from the image raster at the specified rectangle.
	 * 
	 * @param image
	 *            The image raster to perform the operation on
	 * @param rect
	 *            The clipping rectangle to use when performing the operation,
	 *            or null if the operation should apply to the whole image
	 * 
	 * @return The extracted collection of features, or null if none were found
	 */
    Collection<Feature> extract(Raster image, Rectangle rect);
}
