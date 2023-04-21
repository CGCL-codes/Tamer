package com.customwars.client.ui.slick;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import java.util.Arrays;

/**
 * Image Rotor rotates over a set of images. Each image is shown for a fixed time frame
 * called a duration. Single frames can be shown/hidden
 *
 * Usage:
 * ImageRotator imgRotator = new ImageRotator(images, 1000);
 * imgRotator.update(delta);
 * imgRotator.draw(5,5);
 *
 * imgRotator.hideFrame(3)
 * imgRotator.hideFrame(4)
 */
public class ImageRotator {

    private final boolean[] showFrames;

    private final Image[] images;

    private final int duration;

    private Animation anim;

    /**
   * Create an Image Rotator
   * All frames are visible
   *
   * @param images   The images to rotate over
   * @param duration The time in ms to show each image
   */
    public ImageRotator(Image[] images, int duration) {
        this.images = images;
        this.duration = duration;
        this.showFrames = new boolean[images.length];
        showAllFrames();
    }

    public void showAllFrames() {
        Arrays.fill(showFrames, true);
        createAnim();
    }

    public void hideAllFrames() {
        Arrays.fill(showFrames, false);
        createAnim();
    }

    public void setShowFrame(int frameIndex, boolean show) {
        if (show) {
            showFrame(frameIndex);
        } else {
            hideFrame(frameIndex);
        }
    }

    public void showFrame(int frameIndex) {
        showFrames[frameIndex] = true;
        createAnim();
    }

    public void hideFrame(int frameIndex) {
        showFrames[frameIndex] = false;
        createAnim();
    }

    /**
   * Create a new animation
   * The animation only contains the images that should be visible
   */
    private void createAnim() {
        anim = new Animation();
        for (int frameIndex = 0; frameIndex < images.length; frameIndex++) {
            if (showFrames[frameIndex]) {
                anim.addFrame(images[frameIndex], duration);
            }
        }
    }

    public boolean isFrameVisible(int frameIndex) {
        return showFrames[frameIndex];
    }

    public void update(long delta) {
        anim.update(delta);
    }

    public void draw(float x, float y) {
        if (anim.getFrameCount() > 0) {
            anim.draw(x, y);
        }
    }

    public int getWidth() {
        return anim.getFrameCount() > 0 ? anim.getWidth() : 0;
    }

    public int getHeight() {
        return anim.getFrameCount() > 0 ? anim.getHeight() : 0;
    }
}
