package uk.org.toot.audio.basic.stereoImage;

/**
 * Provides the contract that decouples StereoImageProcess from StereoImageControls
 * @author st
 *
 */
public interface StereoImageProcessVariables {

    float getWidthFactor();

    boolean isLRSwapped();

    boolean isBypassed();
}
