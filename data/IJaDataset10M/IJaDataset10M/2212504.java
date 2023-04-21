package com.volantis.map.ics.imageprocessor.convertor.impl;

import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorException;
import com.volantis.map.common.param.Parameters;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.BandSelectDescriptor;
import javax.imageio.metadata.IIOMetadata;

/**
 * Convert an image to indexed 256 level greyscale.
 */
public class IndexedGreyscaleConvertor extends IndexedConvertor implements ImageConvertor {

    public int getRequiredNumBits() {
        return 8;
    }

    public boolean isColor() {
        return false;
    }
}
