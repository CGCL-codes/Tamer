package com.mia.sct.transition.model;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * DefaultTransitionImage
 *
 * @author Devon Bryant
 * @since Jan 11, 2009
 */
public class DefaultTransitionImage extends AbstractTransitionImage {

    /**
	 * Default constructor
	 * @param inImage
	 */
    public DefaultTransitionImage(BufferedImage inImage, Dimension inParentBounds) {
        super(1.0f, inImage, 1, 0, 0, inParentBounds);
    }
}
