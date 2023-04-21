package com.volantis.map.ics.imageprocessor.utilities;

import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.ics.configuration.DitherMode;
import com.volantis.map.ics.imageprocessor.ImageInformation;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import javax.media.jai.DeferredProperty;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.CompositeDescriptor;
import java.awt.*;
import java.awt.image.BandedSampleModel;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.renderable.ParameterBlock;

/**
 * This class provides some useful utility methods to use for processing
 * images.
 */
public class ImageUtils {

    /**
     * The logging object to use in this class for localised logging services.
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(ImageUtils.class);

    /**
     * Removes the alpha channel from an image by merging the image with a opaque
     * white background using the alpha channel. The channel is then removed.
     *
     * @param src  the image to process.
     * @param size the required size of the color component arrays, if the
     *             image has an indexed colour model. Note that size must be a
     *             power of two, otherwise the GIFImageWriter fails. If the
     *             image has no indexed colour model then size is ignored.
     * @return a new image representing the input image with alpha removed, or
     *         the input image if no alpha is present
     */
    public static RenderedOp applyAlphaChannel(RenderedOp src, int size) {
        RenderedOp result = src;
        final ColorModel cm = src.getColorModel();
        if (cm.hasAlpha()) {
            if (!(cm instanceof IndexColorModel)) {
                result = applyAlphaFromNonIndexedImage(src);
            } else {
                result = applyAlphaFromIndexedImage(src, size);
            }
        }
        return result;
    }

    /**
     * Retrieves alpha band from the image. If the original image has no
     * alpha band then this method returns a constant single plane alpha
     * with a value of 127
     *
     * @param src source image to retrieve the alpha channel from. The image
     *            must be in ComponentColorModel.
     * @return RenderedOp representing alpha band of the source image.
     */
    public static RenderedOp extractAlphaChannel(RenderedOp src) {
        ColorModel cm = src.getColorModel();
        RenderedOp result = null;
        if (cm.getNumComponents() > cm.getNumColorComponents()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Performing band select and discarding alpha");
            }
            ParameterBlock selectPB = new ParameterBlock();
            selectPB.addSource(src);
            selectPB.add(new int[] { cm.getNumComponents() - 1 });
            result = JAI.create("bandselect", selectPB);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Generating alpha channel");
            }
            ParameterBlockJAI constPb = new ParameterBlockJAI("constant");
            constPb.setParameter("width", new Float(src.getWidth()));
            constPb.setParameter("height", new Float(src.getHeight()));
            constPb.setParameter("bandValues", new Byte[] { (byte) 127 });
            result = JAI.create("constant", constPb);
        }
        return result;
    }

    /**
     * Creates constant image with pointed bands.
     *
     * @param width      width of the image to create.
     * @param height     height of the image to create
     * @param bandValues band values. This parameter defines the number of
     *                   bands to be in image and band values to fill up the
     *                   bands. For example, if this is three element array of
     *                   {1, 2, 3} then image with three bands will be created;
     *                   all pixels of the first band will have value 1, of the
     *                   second band - 2 and for 3-th band it will be 3.
     * @return created image.
     */
    public static RenderedOp getConstantImage(int width, int height, byte[] bandValues) {
        Byte bv[] = new Byte[bandValues.length];
        System.arraycopy(bandValues, 0, bv, 0, bandValues.length);
        ParameterBlock pb = new ParameterBlock();
        pb.add(new Float(width));
        pb.add(new Float(height));
        pb.add(bv);
        return JAI.create("constant", pb);
    }

    /**
     * Helper method to remove the alpha channel from an image with an indexed
     * colour model.
     *
     * @param src  the image to transform
     * @param size the required size of the color component arrays. Note that
     *             size must be a power of two, otherwise the GIFImageWriter
     *             fails.
     * @return the transformed image
     */
    private static RenderedOp applyAlphaFromIndexedImage(RenderedOp src, int size) {
        final IndexColorModel oldModel = (IndexColorModel) src.getColorModel();
        if (logger.isDebugEnabled()) {
            logger.debug("Creating new colour model for image without " + "alpha channel.");
        }
        int[] rgbs = new int[size];
        oldModel.getRGBs(rgbs);
        IndexColorModel newModel = new IndexColorModel(oldModel.getPixelSize(), size, rgbs, 0, false, -1, oldModel.getTransferType());
        ImageLayout layout = new ImageLayout(src);
        layout.setColorModel(newModel);
        RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(src);
        RenderedOp result = JAI.create("format", pb, hints);
        return result;
    }

    /**
     * Remove the alpha channel from the src image. This method just rips
     * the alpha off. If you want to apply the alpha with a white
     * opaque background see {@link #applyAlphaChannel}. If the src
     * has not alpha band then the src is returned unchanged.
     *
     * @param src the source image
     * @return the image with the alpha channel removed
     */
    public static RenderedOp removeAlphaChannel(RenderedOp src) {
        final ColorModel cm = src.getColorModel();
        RenderedOp result = src;
        if (cm.getNumComponents() > cm.getNumColorComponents()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Performing band select and discarding alpha");
            }
            int[] bandSelects = new int[cm.getNumColorComponents()];
            for (int i = 0; i < cm.getNumColorComponents(); i++) {
                bandSelects[i] = i;
            }
            ParameterBlock selectPB = new ParameterBlock();
            selectPB.addSource(src);
            selectPB.add(bandSelects);
            result = JAI.create("bandselect", selectPB);
        }
        return result;
    }

    /**
     * Helper method to remove the alpha channel from an image with a
     * non-indexed colour model, using band selection.
     *
     * @param src the image to transform
     * @return the transformed image
     */
    private static RenderedOp applyAlphaFromNonIndexedImage(RenderedOp src) {
        RenderedOp result = src;
        final ColorModel cm = src.getColorModel();
        if (cm.getNumComponents() > cm.getNumColorComponents()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Performing band select and discarding alpha");
            }
            RenderedOp color = removeAlphaChannel(src);
            RenderedOp alpha = extractAlphaChannel(src);
            ParameterBlock blankPB = new ParameterBlock();
            blankPB.add((float) src.getWidth());
            blankPB.add((float) src.getHeight());
            Object[] data;
            final int dataType = color.getSampleModel().getDataType();
            switch(dataType) {
                case DataBuffer.TYPE_BYTE:
                    data = new Byte[] { (byte) 255, (byte) 255, (byte) 255 };
                    break;
                case DataBuffer.TYPE_USHORT:
                    data = new Short[] { (short) 65535, (short) 65535, (short) 65535 };
                    break;
                case DataBuffer.TYPE_INT:
                    data = new Integer[] { 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF };
                    break;
                case DataBuffer.TYPE_FLOAT:
                    data = new Float[] { 1.0f, 1.0f, 1.0f };
                    break;
                case DataBuffer.TYPE_DOUBLE:
                    data = new Double[] { 1.0, 1.0, 1.0 };
                    break;
                default:
                    throw new IllegalArgumentException("Unknown data buffer type: " + dataType);
            }
            blankPB.add(data);
            blankPB.addSource(color);
            RenderedOp blank = JAI.create("constant", blankPB);
            ParameterBlock applyAlphaPB = new ParameterBlock();
            applyAlphaPB.addSource(color);
            applyAlphaPB.addSource(blank);
            applyAlphaPB.add(alpha);
            applyAlphaPB.add(null);
            applyAlphaPB.add(Boolean.FALSE);
            applyAlphaPB.add(CompositeDescriptor.NO_DESTINATION_ALPHA);
            result = JAI.create("composite", applyAlphaPB);
        }
        return result;
    }

    /**
     * Returns the appropriate KernelJAI for the specified map size. Returns
     * null for "orderedDither"
     * @param params
     * @param numBits
     * @return
     * @throws MissingParameterException
     */
    private static KernelJAI getKernelFor(Parameters params, int numBits) throws MissingParameterException {
        String paramName;
        switch(numBits) {
            case 1:
                paramName = ParameterNames.DITHER_MODE_1_BIT;
                break;
            case 2:
                paramName = ParameterNames.DITHER_MODE_2_BIT;
                break;
            case 4:
                paramName = ParameterNames.DITHER_MODE_4_BIT;
                break;
            case 8:
                paramName = ParameterNames.DITHER_MODE_8_BIT;
                break;
            default:
                paramName = ParameterNames.DEFAULT_DITHER_MODE;
        }
        String kernelName = null;
        if (params.containsName(paramName)) {
            kernelName = params.getParameterValue(paramName);
        }
        KernelJAI kernel = KernelJAI.ERROR_FILTER_FLOYD_STEINBERG;
        if (DitherMode.JARVIS.toString().equals(kernelName)) {
            kernel = KernelJAI.ERROR_FILTER_JARVIS;
        } else if (DitherMode.STUCKI.toString().equals(kernelName)) {
            kernel = KernelJAI.ERROR_FILTER_STUCKI;
        } else if (DitherMode.ORDERED.toString().equals(kernelName)) {
            kernel = null;
        }
        kernel = null;
        return kernel;
    }

    /**
     * Perform dithering on the image to achive the specified number of
     * colours.
     *
     * @param src the source image to reduce the number of colours in
     * @param colorMapSize the target size of the color map
     * @return an IndexColorModel image that uses at most the specifeid number
     * of colour.
     */
    public static RenderedOp dither(RenderedOp src, Parameters params, int colorMapSize) throws MissingParameterException {
        int bitSize = numBitsToIndexColourmap(colorMapSize);
        KernelJAI kernel = getKernelFor(params, bitSize);
        IndexColorModel cm = getIndexColorModel(src, colorMapSize);
        ImageLayout layout = new ImageLayout();
        layout.setColorModel(cm);
        RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
        if (src.getColorModel().getTransparency() != Transparency.OPAQUE && (!(src.getColorModel() instanceof IndexColorModel))) {
            RenderedOp bands = ImageUtils.removeAlphaChannel(src);
            return ditherImage(bands, hints, kernel, colorMapSize);
        } else {
            return ditherImage(src, hints, kernel, colorMapSize);
        }
    }

    /**
     * Return an index colormodel that can be used as a hint to the error
     * diffusion operation.
     * @param src the source image
     * @param size the size of the desired colormap
     * @return
     */
    private static IndexColorModel getIndexColorModel(RenderedOp src, int size) {
        byte[] reds = new byte[size];
        byte[] greens = new byte[size];
        byte[] blues = new byte[size];
        int bitsPerPixel = numBitsToIndexColourmap(size);
        IndexColorModel dstColorModel = null;
        dstColorModel = new IndexColorModel(bitsPerPixel, size, reds, greens, blues);
        return dstColorModel;
    }

    /**
     * Returns the minimum number of bits required to index a colormap of the
     * specified size.
     *
     * @return the minimum number of bits required to index a colormap of the
     * specified size.
     */
    public static int numBitsToIndexColourmap(int colourMapSize) {
        return (int) Math.round(Math.ceil(Math.log(colourMapSize) / Math.log(2)));
    }

    /**
     * Dithers an image so that the size of the colour map is that specified.
     * The technique used (Floyd-Steinburg, Stuki, Jarvis, Ordered
     *
     *
     * This will build a colour map with up to 256 entries in it and then
     * use error diffusion to map colours to it.
     *
     * @param src the image to dither
     * @return the dithered image
     */
    public static RenderedOp ditherImage(RenderedOp src, RenderingHints hints, KernelJAI kernel, int colormapSize) {
        if (logger.isDebugEnabled()) {
            logger.debug("Dithering image using Floyd Steinberg");
        }
        ImageLayout layout = new ImageLayout();
        layout.setSampleModel(new BandedSampleModel(DataBuffer.TYPE_BYTE, src.getWidth(), src.getHeight(), src.getColorModel().getNumColorComponents()));
        RenderingHints rh = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
        ParameterBlock formatPB = new ParameterBlock();
        formatPB.addSource(src);
        formatPB.add(DataBuffer.TYPE_BYTE);
        src = JAI.create("format", formatPB, rh);
        if (logger.isDebugEnabled()) {
            logger.debug("format performed " + ImageInformation.asString(src));
        }
        ParameterBlockJAI pb2 = new ParameterBlockJAI("ColorQuantizer");
        pb2.addSource(src);
        pb2.setParameter("maxColorNum", colormapSize);
        RenderedOp dithered = JAI.create("ColorQuantizer", pb2, hints);
        if (kernel != null) {
            DeferredProperty lupTable = new DeferredProperty(dithered, "LUT", LookupTableJAI.class);
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(src);
            pb.add(lupTable.getData());
            pb.add(kernel);
            dithered = JAI.create("errordiffusion", pb, hints);
        }
        return dithered;
    }
}
