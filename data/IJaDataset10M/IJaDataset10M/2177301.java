package com.dsrsc.model;

/**
 * @author Lothy
 */
public interface Appearance {

    byte getHairColour();

    byte getSkinColour();

    int getSprite(int i);

    int[] getSprites();

    byte getTopColour();

    byte getTrouserColour();

    boolean isValid();
}
