package hu.ihash.common.service.queue;

import java.awt.image.BufferedImage;

/**
 * An interface for image load listeners.
 * 
 * @author Gergely Kiss
 * 
 */
public interface IImageLoadListener {

    public void loaded(ImageJob job, BufferedImage image);
}
