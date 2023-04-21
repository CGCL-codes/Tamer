package com.gampire.pc.model;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import com.gampire.pc.util.image.ImageUtil;

public class FireInfo implements Serializable {

    public static final int DEFAULT_IMAGE_WIDTH = (int) (640 * 0.8);

    public static final int DEFAULT_IMAGE_HEIGHT = (int) (480 * 0.8);

    public FireInfo(String message, int distance, boolean sideShot, boolean rearShot, boolean isCertain, int[] fireImageRgb, int fireImageWidth, int fireImageHeight) {
        this.message = message;
        this.distance = distance;
        this.sideShot = sideShot;
        this.rearShot = rearShot;
        this.isCertain = isCertain;
        this.fireImageRgb = fireImageRgb;
        this.fireImageWidth = fireImageWidth;
        this.fireImageHeight = fireImageHeight;
    }

    /**
	 * Message telling what is happening.
	 */
    private String message;

    /**
	 * Distance at which the target resides (-1 if unknown, MAX_INT if out of
	 * line of sight).
	 */
    private int distance;

    /**
	 * Boolean indicating if it is a side shot.
	 */
    private boolean sideShot;

    /**
	 * Boolean indicating if it is a rear shot.
	 */
    private boolean rearShot;

    /**
	 * Boolean indicating if the distance is certain.
	 */
    private boolean isCertain;

    /**
	 * The rgb pixels of the fire image.
	 */
    private int[] fireImageRgb;

    /**
	 * Width of fireImage.
	 */
    private int fireImageWidth;

    /**
	 * Height of fireImage.
	 */
    private int fireImageHeight;

    public String getMessage() {
        return message;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isSideShot() {
        return sideShot;
    }

    public boolean isRearShot() {
        return rearShot;
    }

    public boolean distanceCertain() {
        return isCertain;
    }

    public int[] getFireImageRgb() {
        return fireImageRgb;
    }

    public boolean distanceKnown() {
        return distance != -1;
    }

    public boolean outOfLOS() {
        return distance == Integer.MAX_VALUE;
    }

    public BufferedImage getFireImage(boolean targetIsDestroyed) {
        BufferedImage fireImage;
        fireImage = new BufferedImage(fireImageWidth, fireImageHeight, BufferedImage.TYPE_INT_RGB);
        fireImage.setRGB(0, 0, fireImageWidth, fireImageHeight, fireImageRgb, 0, fireImageWidth);
        if (targetIsDestroyed && distanceCertain()) {
            fireImage = ImageUtil.computeScaledImage(fireImage, fireImage.getHeight());
            fireImage = ImageUtil.addExplosion(fireImage, 0.75f);
        }
        return fireImage;
    }
}
