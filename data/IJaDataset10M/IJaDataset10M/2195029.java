package com.kolszew.swing.actionlunchpanel.animation.sample;

import java.awt.Color;
import org.apache.log4j.Logger;
import com.kolszew.swing.actionlunchpanel.animation.AnimationComputor;
import com.kolszew.swing.actionlunchpanel.animation.ComputorInData;
import com.kolszew.swing.actionlunchpanel.animation.ComputorOutData;
import com.kolszew.swing.actionlunchpanel.animation.ComputorOutDataImpl;
import com.kolszew.swing.actionlunchpanel.animation.ComputorInData.TimeState;
import com.kolszew.swing.actionlunchpanel.animation.ComputorOutData.ImageType;

/**
 * Sample animation computor, generating animation which layout actions in
 * circle or ellipse and apply some basic behavior in start, focus and click
 * time.
 * 
 * @author Krzysztof Olszewski <kolszew73@o2.pl>
 * 
 */
public class CircleAnimationComputor implements AnimationComputor {

    private static final String ANIMATION_NAME = "Simple circle animation";

    private static final Logger logger = Logger.getLogger(CircleAnimationComputor.class);

    private static final int SHADOW_OFFSET = 6;

    private static final int BORDER_MARGIN = 2;

    private static final Color TITLE_FOREGROUND_STANDARD = Color.DARK_GRAY;

    private static final Color TITLE_FOREGROUND_FOCUSED = Color.BLACK;

    private final ComputorOutDataImpl out = new ComputorOutDataImpl();

    /**
	 * @see com.kolszew.swing.actionlunchpanel.animation.AnimationComputor#compute(ComputorInData)
	 */
    @Override
    public ComputorOutData compute(ComputorInData in) {
        if (logger.isTraceEnabled()) {
            logger.trace("Compute: " + "startTime=" + in.getStartTime());
        }
        if (in.isFocusTime(TimeState.PRESENT)) {
            out.setImageType(ImageType.BIG_IMAGE);
            out.setImageWidth(Math.round(in.getBigImageWidth() - (in.getBigImageWidth() - in.getSmallImageWidth()) * (1 - in.getFocusTime())));
            out.setImageHeight(Math.round(in.getBigImageHeight() - (in.getBigImageWidth() - in.getSmallImageWidth()) * (1 - in.getFocusTime())));
        } else {
            out.setImageType(ImageType.SMALL_IMAGE);
            out.setImageWidth(Math.round(in.getSmallImageWidth() * in.getStartTime()));
            out.setImageHeight(Math.round(in.getSmallImageHeight() * in.getStartTime()));
        }
        int startCircleFactor = 20;
        int targetCircleFactor = 2;
        int iconWidth = Math.min(in.getAreaWidth() / 2, in.getAreaHeight() / 2) / targetCircleFactor;
        out.setIconWidth(iconWidth);
        out.setIconHeight(out.getImageHeight() + SHADOW_OFFSET + 2 * BORDER_MARGIN);
        final float circleRadius = Math.min(in.getAreaWidth() / 2, in.getAreaHeight() / 2) / (startCircleFactor - ((startCircleFactor - targetCircleFactor) * in.getStartTime()));
        final float theta = (float) ((in.getStartTime() * Math.PI - Math.PI / 2) + (Math.PI * 2 * in.getElementNumber() / in.getElementsCount()));
        float elipseFactor = 2f;
        final float xPosDelta = (float) (Math.cos(theta) * circleRadius * elipseFactor);
        final float yPosDelta = (float) (Math.sin(theta) * circleRadius);
        out.setIconX((int) ((in.getAreaWidth() - out.getIconWidth()) / 2 - xPosDelta));
        out.setIconY((int) ((in.getAreaHeight() - out.getIconHeight()) / 2 - yPosDelta));
        int clickYDelta = Math.round(out.getIconHeight() / 3 * in.getClickTime());
        out.setIconY(out.getIconY() - clickYDelta);
        out.setIconHeight(out.getIconHeight() + clickYDelta);
        int imageToIconMarginX = (out.getIconWidth() - out.getImageWidth()) / 2;
        int imageToIconMarginY = (out.getIconHeight() - out.getImageHeight()) / 2;
        out.setImageX(imageToIconMarginX);
        out.setImageY(imageToIconMarginY);
        out.setImageShadowVisible(in.isFocusTime(TimeState.PRESENT));
        int shadow_offset = Math.round(SHADOW_OFFSET * in.getFocusTime());
        out.setImageShadowX(out.getImageX() + shadow_offset);
        out.setImageShadowY(out.getImageY() + shadow_offset);
        out.setImageShadowHeight(out.getImageHeight());
        out.setImageShadowWidth(out.getImageWidth());
        out.setTitleVisible(in.isStartTime(TimeState.BEFORE_COMPLETE));
        float titleLabelWidth = out.getIconWidth() * in.getStartTime();
        out.setTitleLabelWidth(Math.round(titleLabelWidth));
        out.setTitleLabelHeight(16);
        out.setTitleLabelX(out.getIconX());
        out.setTitleLabelY(out.getIconY() + out.getIconHeight());
        if (in.isFocusTime(TimeState.HALF)) {
            out.setTitleLabelForeground(TITLE_FOREGROUND_FOCUSED);
        } else {
            out.setTitleLabelForeground(TITLE_FOREGROUND_STANDARD);
        }
        out.setDescriptionVisible(in.isFocusTime(TimeState.HALF));
        if (out.isDescriptionVisible()) {
            out.setDescriptionLabelWidth((int) (circleRadius * (1 + elipseFactor / 2)));
            out.setDescriptionLabelHeight((int) (circleRadius));
            out.setDescriptionLabelX(((in.getAreaWidth() - out.getDescriptionLabelWidth()) / 2));
            out.setDescriptionLabelY(((in.getAreaHeight() - out.getDescriptionLabelHeight()) / 2) - (in.getAreaHeight() / 20));
            out.setDescriptionLabelAlpha(in.getFocusTime() * 0.99f);
        }
        out.setBackgroundImageVisible(in.isStartTime(TimeState.COMPLETE));
        return out;
    }

    /**
	 * Returns animation name
	 * 
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return ANIMATION_NAME;
    }
}
