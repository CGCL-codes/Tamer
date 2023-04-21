package com.bluebrim.paint.impl.shared;

import java.awt.*;

/**
 * Representerar processf�rgen svart
 * 
 */
public class CoProcessBlack extends com.bluebrim.paint.impl.shared.CoProcessColor implements com.bluebrim.paint.impl.shared.CoProcessBlackIF {

    public static Color PREVIEW_COLOR = Color.black;

    /**
 * This method was created by a SmartGuide.
 */
    public CoProcessBlack() {
    }

    public boolean equals(Object o) {
        return (o instanceof CoProcessBlack);
    }

    public float getBlackPercentage() {
        return 100;
    }

    public float getCyanPercentage() {
        return 0;
    }

    public String getFactoryKey() {
        return PROCESS_BLACK;
    }

    public float getMagentaPercentage() {
        return 0;
    }

    public String getName() {
        return com.bluebrim.paint.impl.shared.CoColorResources.getName(PROCESS_BLACK);
    }

    public Color getPreviewColor() {
        return PREVIEW_COLOR;
    }

    public Color getShadedPreviewColor(float shade) {
        float tFloat = (100 - shade) / 100;
        return new Color(tFloat, tFloat, tFloat);
    }

    public float getYellowPercentage() {
        return 0;
    }
}
