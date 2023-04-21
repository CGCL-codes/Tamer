package com.volantis.map.ics.imageprocessor.writer.impl;

import com.volantis.map.ics.configuration.OutputImageRules;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.imageprocessor.writer.ImageWriter;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterFactory;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterFactoryException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the ImageWriterFactory.
 */
public class ImageWriterFactoryImpl extends ImageWriterFactory {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER = LocalizationFactory.createLogger(ImageWriterFactoryImpl.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = LocalizationFactory.createExceptionLocalizer(ImageWriterFactoryImpl.class);

    /**
     * Rules to writer class map.
     */
    public static final Map RULES;

    static {
        Map rules = new HashMap();
        rules.put(OutputImageRules.COLOURBMP24, new BMPImageWriter());
        rules.put(OutputImageRules.WBMP, new WBMPImageWriter());
        rules.put(OutputImageRules.COLOURTIFF24, new TIFFImageWriter());
        rules.put(OutputImageRules.GRAYSCALEPNG1, new PNGMonochromeWriter());
        rules.put(OutputImageRules.GRAYSCALEPNG2, new PNGGreyscale4Writer());
        rules.put(OutputImageRules.GRAYSCALEPNG4, new PNGGreyscale16Writer());
        rules.put(OutputImageRules.GRAYSCALEPNG8, new PNGGreyscaleWriter());
        rules.put(OutputImageRules.GRAYSCALEPNG16, new PNGGreyscaleWriter());
        rules.put(OutputImageRules.COLOURPNG8, new PNGIndexedWriter());
        rules.put(OutputImageRules.COLOURPNG24, new PNGColourWriter());
        rules.put(OutputImageRules.GREYSCALEJPEG8, new JPEGGreyscaleWriter());
        rules.put(OutputImageRules.COLOURJPEG24, new JPEGColourWriter());
        rules.put(OutputImageRules.GREYSCALEGIF1, new GIFMonochromeWriter());
        rules.put(OutputImageRules.GREYSCALEGIF2, new GIFGreyscale4Writer());
        rules.put(OutputImageRules.GREYSCALEGIF4, new GIFGreyscale16Writer());
        rules.put(OutputImageRules.GREYSCALEGIF8, new GIFGreyscaleWriter());
        rules.put(OutputImageRules.COLOURGIF8, new GIFIndexedWriter());
        RULES = Collections.synchronizedMap(rules);
    }

    public ImageWriter getWriter(String imageRule, Parameters params) throws ImageWriterFactoryException {
        if (imageRule == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null", "imageRule");
            throw new IllegalArgumentException(msg);
        }
        if (params == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null", "params");
            throw new IllegalArgumentException(msg);
        }
        if ("".equals(imageRule)) {
            String msg = EXCEPTION_LOCALIZER.format("argument-value-is-empty-string", "imageRule");
            throw new IllegalArgumentException(msg);
        }
        try {
            boolean isGifEnabled = params.getBoolean(ParameterNames.GIF_ENABLED);
            if (imageRule.length() >= 2 && imageRule.charAt(1) == 'g' && !isGifEnabled) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Writer requested for GIF rule " + imageRule + ", but GIF support disabled, returning null");
                }
                return null;
            }
            if (!RULES.containsKey(imageRule)) {
                String msg = EXCEPTION_LOCALIZER.format("rule-unknown", "imageRule");
                throw new ImageWriterFactoryException(msg);
            }
            return (ImageWriter) RULES.get(imageRule);
        } catch (Exception e) {
            LOGGER.error("image-writing-failure", e.toString());
            throw new ImageWriterFactoryException(e);
        }
    }
}
