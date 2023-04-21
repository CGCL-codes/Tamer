package org.anddev.andengine.entity.scene.background;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:44:31 - 19.07.2010
 */
public class AutoParallaxBackground extends ParallaxBackground {

    private float mParallaxChangePerSecond;

    public AutoParallaxBackground(final float pRed, final float pGreen, final float pBlue, final float pParallaxChangePerSecond) {
        super(pRed, pGreen, pBlue);
        this.mParallaxChangePerSecond = pParallaxChangePerSecond;
    }

    public void setParallaxChangePerSecond(final float pParallaxChangePerSecond) {
        this.mParallaxChangePerSecond = pParallaxChangePerSecond;
    }

    @Override
    public void onUpdate(final float pSecondsElapsed) {
        super.onUpdate(pSecondsElapsed);
        this.mParallaxValue += this.mParallaxChangePerSecond * pSecondsElapsed;
    }
}
