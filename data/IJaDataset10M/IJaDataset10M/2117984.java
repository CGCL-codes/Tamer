package com.qspin.qtaste.ui.tools;

import java.awt.*;
import java.util.*;

/**
 * The ScreenHelper class knows how many screens are available in the configuration. All the windows of the
 * front end can be redirected on a specific screen. Currently the windows are displayed on the larger
 * available screen in a multiple screens configuration.
 */
public class ScreenHelper {

    private java.util.List<Rectangle> mCoords = new ArrayList<Rectangle>();

    private int mLargerScreen;

    private int mCurrentScreen;

    private Dimension mForcedResolution;

    public ScreenHelper() {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        Rectangle rect = new Rectangle();
        for (int i = 0; i < devices.length; ++i) {
            GraphicsConfiguration gc = devices[i].getDefaultConfiguration();
            new Frame(gc);
            Rectangle currentrect = gc.getBounds();
            mCoords.add(currentrect);
            if (currentrect.width > rect.width) {
                rect = currentrect;
                mLargerScreen = i;
            }
        }
        mCurrentScreen = mLargerScreen;
    }

    public Rectangle getCurrentScreenCoords() {
        if (mForcedResolution == null) {
            return getScreenCoords(mCurrentScreen);
        }
        Rectangle rect = getScreenCoords(mCurrentScreen);
        rect.width = mForcedResolution.width;
        rect.height = mForcedResolution.height;
        return rect;
    }

    public int getSecondaryScreenIndex() {
        return 1 - mCurrentScreen;
    }

    public Rectangle getLargerScreenCoords() {
        return mCoords.get(mLargerScreen);
    }

    public int getNumberOfScreens() {
        return mCoords.size();
    }

    public Rectangle getScreenCoords(int pIndex) {
        return mCoords.get(pIndex);
    }

    public void setCurrentScreen(int pIndex) {
        mCurrentScreen = pIndex;
    }

    public void setForcedResolution(Dimension pDim) {
        mForcedResolution = pDim;
    }
}
