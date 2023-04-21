package com.mebigfatguy.polycasso;

import java.awt.Image;
import java.util.EventObject;

/**
 * an event object that is fired when a new test image has been found that is 
 * the best so far.
 */
public class ImageGeneratedEvent extends EventObject {

    private static final long serialVersionUID = -7067803452935840915L;

    private transient Image bestImage;

    /**
	 * creates the event object with the source of the event as well as the image
	 * that is now the best image found.
	 * 
	 * @param source the object that generated this event (an image generator)
	 * @param image the best image found so far
	 */
    public ImageGeneratedEvent(Object source, Image image) {
        super(source);
        bestImage = image;
    }

    /**
	 * retrieve the best image as described by this event
	 * 
	 * @return the best image
	 */
    public Image getImage() {
        return bestImage;
    }
}
