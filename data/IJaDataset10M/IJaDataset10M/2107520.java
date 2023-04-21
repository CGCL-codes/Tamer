package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.image.GenericImageSelection;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.synergetics.log.LogDispatcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Given a selection of variants with generic image selections and the device
 * that an image is intended for, select the best image from the selection for
 * that device.
 *
 * The image selection rules are as follows:
 *    The rules for selecting the best image are as follows:
 *    1. The image must use an encoding supported by the device.
 *    2. The image must be no wider in pixels than the width of the device in
 *    pixels.  If a value has been specified in the WIDTHHINT field, then
 *    the image must be no wider than WIDTHHINT percent of the width of the
 *    device in pixels.
 *    3. The RENDERING field should match. However, it is acceptable to send
 *    monochrome images to a colour device and colour images to a monochrome
 *    device under the following circumstances:
 *            ?  The image type must be "JPEG", "GIF", "BMP" or "PNG".
 *            ?  There is no appropriate image with a RENDERING that matches
 *             the device's colour capability.
 *    4. The PIXELDEPTH of the image should be less than or equal to that of
 *    the device. However, it is acceptable to send images with greater
 *    pixel depth under the following conditions:
 *            ?  The image type must be "JPEG", "GIF", "BMP" or "PNG".
 *            ?  There is no equally appropriate image with a PIXELDEPTH
 *            that is within the device's pixel depth capability.
 *            ?  The only equally appropriate image with a PIXELDEPTH that
 *            is within the device's pixel depth capability has a PIXELDEPTH
 *            of 1, and the device has a pixel depth greater than 1.
 *    This rule is intended to favour colour and greyscale images that have
 *    too large a pixel depth for the device, where the only alternative is
 *    a bi-level (black and white) image. Of course, if the device only
 *    supports bi-level images, then the bi-level image should be used.
 *    5. Where multiple images are equally applicable for a given device,
 *    JPEG is preferred over GIF and PNG where the pixel depth is greater
 *    than 8.  PNG is preferred over GIF, which is preferred over JPEG where
 *    the pixel depth is less than or equal to 8.  GIF images with fewer than
 *    8 bits per pixel tend to compress better than JPEG equivalents, but at
 *    larger pixel depths, JPEG images have the advantage. This rule is
 *    likely to pick the smaller file.
 */
public class ImageSpecificSelector implements VariablePolicyTypeSpecificSelector {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(ImageSpecificSelector.class);

    public Variant selectVariant(SelectionContext context, ActivatedVariablePolicy variablePolicy) {
        List genericVariants = variablePolicy.getGenericImages();
        if (genericVariants == null || genericVariants.isEmpty()) {
            return null;
        }
        InternalDevice device = context.getDevice();
        List images = new ArrayList(genericVariants);
        int maxWidth = -1;
        int deviceWidth = device.getPixelsX();
        EncodingCollection supportedEncodings = device.getSupportedImageEncodings();
        for (Iterator i = images.iterator(); i.hasNext(); ) {
            Variant variant = (Variant) i.next();
            ImageMetaData image = (ImageMetaData) variant.getMetaData();
            ImageEncoding encoding = image.getImageEncoding();
            if (!supportedEncodings.contains(encoding)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Discarding variant " + variant + " as device does not support encoding.");
                }
                i.remove();
                continue;
            }
            int currentWidth = image.getWidth();
            if (currentWidth > deviceWidth) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Discarding variant " + variant + " - width greater than device width.");
                }
                i.remove();
                continue;
            }
        }
        if (images.isEmpty()) {
            return null;
        }
        for (Iterator i = images.iterator(); i.hasNext(); ) {
            Variant variant = (Variant) i.next();
            ImageMetaData image = (ImageMetaData) variant.getMetaData();
            GenericImageSelection generic = (GenericImageSelection) variant.getSelection();
            int widthHint = generic.getWidthHint();
            int currentWidth = image.getWidth();
            if (widthHint != 0) {
                int allowedWidth = (widthHint * deviceWidth) / 100;
                if (currentWidth > allowedWidth) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Discarding variant " + variant + " - width (" + currentWidth + ") greater than " + widthHint + "% of device width (" + deviceWidth + ")");
                    }
                    i.remove();
                    continue;
                }
            }
            if (currentWidth > maxWidth) {
                maxWidth = currentWidth;
            }
        }
        if (images.isEmpty()) {
            return null;
        }
        ImageRendering deviceRenderingMode = device.getRenderMode();
        boolean renderingMatch = false;
        for (Iterator i = images.iterator(); i.hasNext(); ) {
            Variant variant = (Variant) i.next();
            ImageMetaData image = (ImageMetaData) variant.getMetaData();
            if (image.getWidth() < maxWidth) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Discarding variant " + variant + " - width less than best width.");
                }
                i.remove();
                continue;
            }
            if (image.getRendering() == deviceRenderingMode) {
                renderingMatch = true;
            }
        }
        if (images.isEmpty()) {
            return null;
        }
        int devicePixelDepth = device.getPixelDepth();
        boolean pixelDepthMatch = false;
        boolean poorPixelDepthMatch = false;
        boolean pixelDepthMismatch = false;
        for (Iterator i = images.iterator(); i.hasNext(); ) {
            Variant variant = (Variant) i.next();
            ImageMetaData image = (ImageMetaData) variant.getMetaData();
            if (renderingMatch) {
                if (image.getRendering() != deviceRenderingMode) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Discarding variant " + variant + " - rendering type does not match.");
                    }
                    i.remove();
                    continue;
                }
            } else {
                ImageEncoding currentImageEncoding = image.getImageEncoding();
                if (currentImageEncoding != ImageEncoding.JPEG && currentImageEncoding != ImageEncoding.GIF && currentImageEncoding != ImageEncoding.PNG && currentImageEncoding != ImageEncoding.TIFF && currentImageEncoding != ImageEncoding.BMP) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Discarding variant " + variant + " - encoding type does not allow " + "override of rendering.");
                    }
                    i.remove();
                    continue;
                }
            }
            if (image.getPixelDepth() <= devicePixelDepth) {
                if (image.getPixelDepth() == 1 && devicePixelDepth > 1) {
                    poorPixelDepthMatch = true;
                } else {
                    pixelDepthMatch = true;
                }
            } else {
                pixelDepthMismatch = true;
            }
        }
        if (images.isEmpty()) {
            return null;
        }
        if (!pixelDepthMatch && poorPixelDepthMatch && !pixelDepthMismatch) {
            pixelDepthMatch = true;
        }
        if (pixelDepthMatch) {
            for (Iterator i = images.iterator(); i.hasNext(); ) {
                Variant variant = (Variant) i.next();
                ImageMetaData image = (ImageMetaData) variant.getMetaData();
                if (image.getPixelDepth() > devicePixelDepth) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Discarding variant " + variant + " - pixelDepth less than best available.");
                    }
                    i.remove();
                    continue;
                }
            }
        } else {
            for (Iterator i = images.iterator(); i.hasNext(); ) {
                Variant variant = (Variant) i.next();
                ImageMetaData image = (ImageMetaData) variant.getMetaData();
                ImageEncoding currentImageEncoding = image.getImageEncoding();
                if (currentImageEncoding != ImageEncoding.JPEG && currentImageEncoding != ImageEncoding.GIF && currentImageEncoding != ImageEncoding.PNG && currentImageEncoding != ImageEncoding.TIFF && currentImageEncoding != ImageEncoding.BMP) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Discarding variant " + variant + " - pixelDepth too great and" + " outside of valid set of encodings capable" + " of reducing in depth.");
                    }
                    i.remove();
                    continue;
                }
            }
        }
        if (images.isEmpty()) {
            return null;
        } else if (images.size() == 1) {
            return (Variant) images.get(0);
        }
        int bestDepth = -1;
        if (pixelDepthMatch) {
            for (Iterator i = images.iterator(); i.hasNext(); ) {
                Variant variant = (Variant) i.next();
                ImageMetaData image = (ImageMetaData) variant.getMetaData();
                if (image.getPixelDepth() > bestDepth) {
                    bestDepth = image.getPixelDepth();
                }
            }
        } else {
            int bestDiff = 0;
            for (Iterator i = images.iterator(); i.hasNext(); ) {
                Variant variant = (Variant) i.next();
                ImageMetaData image = (ImageMetaData) variant.getMetaData();
                int pixelDepth = image.getPixelDepth();
                if (pixelDepth == 1) {
                    if (bestDepth == -1) {
                        bestDepth = pixelDepth;
                    }
                    continue;
                }
                int diff = pixelDepth - devicePixelDepth;
                if (diff < 0) {
                    diff = 0 - diff;
                }
                if (bestDiff == 0 || diff < bestDiff) {
                    bestDiff = diff;
                    bestDepth = image.getPixelDepth();
                }
            }
        }
        Variant imageJPEG = null;
        Variant imageGIF = null;
        Variant imagePNG = null;
        Variant imageTIFF = null;
        for (Iterator i = images.iterator(); i.hasNext(); ) {
            Variant variant = (Variant) i.next();
            ImageMetaData image = (ImageMetaData) variant.getMetaData();
            if (image.getPixelDepth() != bestDepth) {
                i.remove();
            }
            if (image.getImageEncoding() == ImageEncoding.JPEG) {
                imageJPEG = variant;
            } else if (image.getImageEncoding() == ImageEncoding.GIF) {
                imageGIF = variant;
            } else if (image.getImageEncoding() == ImageEncoding.PNG) {
                imagePNG = variant;
            } else if (image.getImageEncoding() == ImageEncoding.TIFF) {
                imageTIFF = variant;
            }
        }
        if (images.size() == 1) {
            return (Variant) images.get(0);
        }
        if (bestDepth <= 8 && imagePNG != null) {
            return imagePNG;
        } else if (bestDepth <= 8 && imageGIF != null) {
            return imageGIF;
        } else if (bestDepth > 8 && imageJPEG != null) {
            return imageJPEG;
        } else if (imagePNG != null) {
            return imagePNG;
        } else if (imageGIF != null) {
            return imageGIF;
        } else if (imageJPEG != null) {
            return imageJPEG;
        } else if (imageTIFF != null) {
            return imageTIFF;
        }
        if (images.size() > 0) {
            return (Variant) images.get(0);
        }
        return null;
    }
}
