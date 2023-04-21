package com.mia.sct.data.browser;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.jdesktop.animation.timing.TimingTarget;
import com.mia.sct.controller.animation.AnimationDirective;

/**
 * BrowserAvatar.java
 * 
 * Interface for browser objects
 * 
 * @author Devon Bryant
 * @since Dec 30, 2007
 */
public interface BrowserAvatar extends Comparable<BrowserAvatar>, TimingTarget {

    /**
	 * Get the avatars image
	 * @return BufferedImage avatar
	 */
    public BufferedImage getImage();

    /**
	 * Set the avatar image
	 * @param inImage the avatar image
	 */
    public void setImage(BufferedImage inImage);

    /**
	 * Scale the image by the percentage given
	 * @param inScalePercentage scale percentage
	 */
    public void scaleImage(float inScalePercentage);

    /**
	 * Get the last scale value
	 * @return the last scale value
	 */
    public float getScale();

    /**
	 * Calculate the scaled dimensions of the avatar given a scale percentage
	 * @param inScalePercentage the scale percentage
	 * @return Dimensions the dimensions of the scaling
	 */
    public Dimension calculateScaleDimensions(float inScalePercentage);

    /**
	 * Get the avatars alpha value
	 * @return float alpha value
	 */
    public float getAlpha();

    /**
	 * Set the avatars alpha value
	 * @param inAlpha the avatar alpha value
	 */
    public void setAlpha(float inAlpha);

    /**
	 * Get the avatar x location
	 * @return avatar x location
	 */
    public int getX();

    /**
	 * Set the avatars x location
	 * @param inX the avatars x location
	 */
    public void setX(int inX);

    /**
	 * Get the avatar y location
	 * @return avatar y location
	 */
    public int getY();

    /**
	 * Set the avatars y location
	 * @param inY the avatars y location
	 */
    public void setY(int inY);

    /**
	 * Get the avatars width
	 * @return avatars width
	 */
    public int getWidth();

    /**
	 * Set the avatars height
	 * @param inHeight the avatars height
	 */
    public void setHeight(int inHeight);

    /**
	 * Get the avatars height
	 * @return avatars height
	 */
    public int getHeight();

    /**
	 * Set the avatars width
	 * @param inWidth the avatars width
	 */
    public void setWidth(int inWidth);

    /**
	 * Get if the avatar is selected
	 * @return true if the avatar is selected
	 */
    public boolean isSelected();

    /**
	 * Set the avatar selection value
	 * @param inSelected avatar selection value
	 */
    public void setSelected(boolean inSelected);

    /**
	 * Get the avatars name
	 * @return avatar name
	 */
    public String getName();

    /**
	 * Set the avatars name
	 * @param inName the avatars name
	 */
    public void setName(String inName);

    /**
	 * Get the image for the avatar name
	 * @return avatar name image
	 */
    public BufferedImage getNameImage();

    /**
	 * Set the image for the avatars name
	 * @param inImage the image for the avatars name
	 */
    public void setNameImage(BufferedImage inImage);

    /**
	 * Get whether the avatar has a name image
	 * @return true if avatar has a name image
	 */
    public boolean hasNameImage();

    /**
	 * Get the avatars unique identifier
	 * @return avatars unique ID
	 */
    public String getUniqueID();

    /**
	 * Set the avatars unique identifier
	 * @param inUniqueID the avatars unique ID
	 */
    public void setUniqueID(String inUniqueID);

    /**
	 * Set the avatars animation directive
	 * @param inAnimationDirective the animation directive for this avatar
	 */
    public void setAnimationDirective(AnimationDirective inAnimationDirective);

    /**
	 * Get the avatars animation directive
	 * @return the animation directive for this avatar
	 */
    public AnimationDirective getAnimationDirective();

    /**
	 * Pass the avatar a Graphics2D object to delegate painting to the avatar
	 * @param g2 Graphics2D object for the avatar to paint itself
	 */
    public void paintAvatar(Graphics2D g2);

    /**
	 * Get the Avatar type (INSTRUMENT, SCALE, CHORD)
	 * @return the Avatary type
	 */
    public String getType();

    /**
	 * Set the Avatar type (INSTRUMENT, SCALE, CHORD)
	 * @return the Avatary type
	 */
    public void setType(String inType);
}
