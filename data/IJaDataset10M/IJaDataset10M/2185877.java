package org.webcockpit.beans;

import java.io.Serializable;

/**
 * A JSP bean to configure the Texturepaint of Cewolf charts.
 */
public class CewolfTexturepaint implements Serializable {

    public CewolfTexturepaint() {
    }

    ;

    private String image = "";

    private int width;

    private int height;

    /**
	 * @return Returns the height.
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * @param height The height to set.
	 */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
	 * @return Returns the image.
	 */
    public String getImage() {
        return image;
    }

    /**
	 * @param image The image to set.
	 */
    public void setImage(String image) {
        this.image = image;
    }

    /**
	 * @return Returns the width.
	 */
    public int getWidth() {
        return width;
    }

    /**
	 * @param width The width to set.
	 */
    public void setWidth(int width) {
        this.width = width;
    }
}
