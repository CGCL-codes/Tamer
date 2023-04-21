package com.mia.sct.data.browser;

import static com.mia.sct.util.MIAVariables.DEFAULT_INSTRUMENT_IMG;
import static com.mia.sct.util.MIAVariables.INSTRUMENT_TYPE;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.graphics.ReflectionRenderer;
import com.mia.sct.util.MIAImageLoader;
import com.mia.sct.util.MIAImageUtils;
import com.mia.sct.view.MIALafManager;

/**
 * InstrumentBrowserObj.java
 * 
 * This class holds the data for an instrument browser object
 * 
 * @author Devon Bryant
 * @since Dec 29, 2007
 */
public class InstrumentBrowserObj extends AbstractBrowserObj implements ImageObserver {

    private static Logger logger = Logger.getLogger(InstrumentBrowserObj.class);

    private BufferedImage defaultInstrumentImg = null;

    /** Browser Image Animation/Selection Properties */
    public static final int PREFERRED_DIM_NORMAL = 200;

    public static final int WIDTH_ALLOWANCE = 10;

    protected ReflectionRenderer reflectionRenderer = null;

    protected Font imgNameFont = null;

    protected Color imgNameColor = null;

    private int startXLocation = 0;

    private int startYLocation = 0;

    private float startAlpha = 1.0f;

    private float startScale = 1.0f;

    protected String avatarType = INSTRUMENT_TYPE;

    /**
	 * Constructor
	 * 
	 * @param inInstrumentName the instrument name
	 * @param inUniqueID the instrument unique identifier
	 * @param inInstrumentThumbnail the instrument thumbnail image (preferrably 200 px height)
	 */
    public InstrumentBrowserObj(String inInstrumentName, String inUniqueID, BufferedImage inInstrumentThumbnail) {
        super();
        initializeBrowserObject();
        setName(inInstrumentName);
        setUniqueID(inUniqueID);
        if (inInstrumentThumbnail == null) {
            loadDefaultInstrumentImage();
            setImage(defaultInstrumentImg);
        } else {
            setImage(inInstrumentThumbnail);
        }
    }

    private void initializeBrowserObject() {
        setupReflectionRenderer();
        setImageNameFontAndColor();
    }

    private void setupReflectionRenderer() {
        reflectionRenderer = new ReflectionRenderer();
        reflectionRenderer.setOpacity(0.35f);
        reflectionRenderer.setLength(0.4f);
        reflectionRenderer.setBlurEnabled(true);
    }

    private void setImageNameFontAndColor() {
        imgNameFont = new Font("Arial", Font.BOLD, 12);
        imgNameColor = MIALafManager.getInstance().getColor(MIALafManager.DEFAULT_BROWSER_TEXT_COLOR);
    }

    private void loadDefaultInstrumentImage() {
        try {
            defaultInstrumentImg = MIAImageLoader.getInstance().getBufferedImage(DEFAULT_INSTRUMENT_IMG);
        } catch (Exception exc) {
            logger.error("InstrumentBrowserObj.loadDefaultInstrumentImage():", exc);
        }
    }

    @Override
    public void setName(String inName) {
        super.setName(inName);
        resetNameImage();
    }

    private void resetNameImage() {
        try {
            nameImage = MIAImageUtils.getTextImageWithShadowAndBackground(getName(), imgNameFont, imgNameColor);
            setNameImageWidth(nameImage.getWidth());
            setNameImageHeight(nameImage.getHeight());
        } catch (Exception exc) {
            logger.error("InstrumentBrowserObj.resetNameImage():", exc);
            setNameImage(null);
        }
    }

    @Override
    public void setImage(BufferedImage inImage) {
        resetMainImage(inImage);
        super.setImage(normalImage);
        resetDimensions();
    }

    private void resetMainImage(BufferedImage inImage) {
        Dimension scaleDim = null;
        try {
            int imgWidth = inImage.getWidth();
            int imgHeight = inImage.getHeight();
            scaleDim = new Dimension(PREFERRED_DIM_NORMAL, PREFERRED_DIM_NORMAL);
            if (needToScaleImg(imgWidth, imgHeight)) {
                scaleDim = calculateScaleDimensions(imgWidth, imgHeight);
                normalImage = MIAImageUtils.getBufferedImage(inImage.getScaledInstance((int) scaleDim.getWidth(), (int) scaleDim.getHeight(), Image.SCALE_AREA_AVERAGING));
            } else {
                normalImage = inImage;
            }
            normalImage = reflectionRenderer.appendReflection(normalImage);
            normalImageWidth = normalImage.getWidth();
            normalImageHeight = normalImage.getHeight();
        } catch (Exception exc) {
            logger.error("InstrumentBrowserObj.setDimensions():", exc);
        } finally {
            scaleDim = null;
        }
    }

    private boolean needToScaleImg(int inWidth, int inHeight) {
        boolean result = false;
        if (inHeight != PREFERRED_DIM_NORMAL) {
            result = true;
        } else if ((inWidth > (PREFERRED_DIM_NORMAL + WIDTH_ALLOWANCE)) || (inWidth < (PREFERRED_DIM_NORMAL - WIDTH_ALLOWANCE))) {
            result = true;
        }
        return result;
    }

    private Dimension calculateScaleDimensions(int inWidth, int inHeight) {
        Dimension result = null;
        double heightRatio = (double) PREFERRED_DIM_NORMAL / (double) inHeight;
        int scaledWidth = (int) (heightRatio * (double) inWidth);
        if (scaledWidth > (PREFERRED_DIM_NORMAL + WIDTH_ALLOWANCE)) {
            scaledWidth = PREFERRED_DIM_NORMAL + WIDTH_ALLOWANCE;
        } else if (scaledWidth < (PREFERRED_DIM_NORMAL - WIDTH_ALLOWANCE)) {
            scaledWidth = PREFERRED_DIM_NORMAL - WIDTH_ALLOWANCE;
        }
        result = new Dimension(scaledWidth, PREFERRED_DIM_NORMAL);
        return result;
    }

    private void resetDimensions() {
        try {
            setWidth(image.getWidth(this));
            setHeight(image.getHeight(this));
        } catch (Exception exc) {
            logger.error("InstrumentBrowserObj.setDimensions():", exc);
        }
    }

    public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
        boolean result = false;
        if (arg1 == ImageObserver.HEIGHT) {
            setHeight(arg5);
        } else if (arg1 == ImageObserver.WIDTH) {
            setWidth(arg4);
        }
        return result;
    }

    public int compareTo(BrowserAvatar inAvatar) {
        int result = 0;
        try {
            if (getName() != null) {
                result = getName().compareTo(inAvatar.getName());
            }
        } catch (Exception exc) {
            logger.error("InstrumentBrowserObj.compareTo():", exc);
        }
        return result;
    }

    public void begin() {
        scaleFast = true;
        startXLocation = getX();
        startYLocation = getY();
        startAlpha = getAlpha();
        startScale = getScale();
    }

    public void end() {
        if (animationDirective != null) {
            setX(animationDirective.getEndXLocation());
            setY(animationDirective.getEndYLocation());
            setAlpha(animationDirective.getEndAlpha());
            scaleImage(animationDirective.getEndScale());
        }
        scaleFast = false;
    }

    public void repeat() {
    }

    public void timingEvent(float inFraction) {
        if (animationDirective != null) {
            setX(startXLocation + (int) ((animationDirective.getEndXLocation() - startXLocation) * inFraction));
            setY(startYLocation + (int) ((animationDirective.getEndYLocation() - startYLocation) * inFraction));
            setAlpha(startAlpha + ((animationDirective.getEndAlpha() - startAlpha) * inFraction));
            scaleImage(startScale + ((animationDirective.getEndScale() - startScale) * inFraction));
        }
    }

    public String getType() {
        return avatarType;
    }

    public void setType(String inType) {
        avatarType = inType;
    }
}
