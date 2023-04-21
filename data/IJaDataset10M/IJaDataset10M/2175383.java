package com.volantis.map.ics.imageprocessor.convertor;

import com.volantis.map.common.param.Parameters;
import java.awt.Dimension;
import javax.media.jai.RenderedOp;

public interface ImageConvertor extends ImageRule {

    /**
     * Calculate the image size required to ensure that an image fits into a
     * given amount of memory.
     *
     * @param imageSize      the size of the original image.
     * @param compressedSize the size of the orignal image in bytes after
     *                       compression.
     * @param requiredSize   the size in bytes that the image needs to be
     *                       compressed to.
     * @return the image size that will fit into the given memory assuming the
     *         same compression ratio.
     */
    Dimension calcScale(Dimension imageSize, long compressedSize, long requiredSize);

    /**
     * Convert an image to another format using the dithering algorithm
     * specified in the parameters.
     *
     * @param src    the image to convert.
     * @param params the current parameters.
     * @return the converted image.
     */
    RenderedOp convert(RenderedOp src, Parameters params) throws ImageConvertorException;
}
