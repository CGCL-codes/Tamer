package de.nava.informa.core;

/**
 * Meta-, or markerinterface, specifying objects, containing
 * an association to an <strong>image representation</strong>.
 *
 * @author Niko Schmuck
 * @version $Id: WithImageMIF.java 790 2006-01-24 18:02:17Z niko_schmuck $
 */
public interface WithImageMIF {

    /**
   * Retrieves the Image associated with this feed resp. news item.
   * @return An ImageIF representing the image associated with this object.
   */
    ImageIF getImage();

    /**
   * Sets the image for this feed resp. news item.
   * @param image The image
   */
    void setImage(ImageIF image);
}
