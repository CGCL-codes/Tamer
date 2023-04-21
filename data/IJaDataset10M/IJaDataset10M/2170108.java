package de.goto3d.freeimage4j.converters;

import java.awt.image.BufferedImage;
import de.goto3d.freeimage4j.FreeImage;
import de.goto3d.freeimage4j.FreeImageRuntimeException;

public final class AWTImageConverterFactory {

    /**
	 * This method will return a proper image converter for the given BufferedImage. It will try to find the 
	 * most suitable converter, e.g. for 15 bit BufferImages choose the converter that will generate a 15bit FreeImage.
	 * If no suitable converter can be found a 24bit image will be generated as fallback.
	 * 
	 * @param image The image to convert
	 * @return The image converter
	 */
    public static FreeImage convert(BufferedImage image) {
        switch(image.getType()) {
            case BufferedImage.TYPE_USHORT_565_RGB:
                return AWTImageConverter_16_RGB.getInstance().convert(image);
            case BufferedImage.TYPE_3BYTE_BGR:
                return AWTImageConverter_24_RGB.getInstance().convert(image);
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
                return AWTImageConverter_32_ARGB.getInstance().convert(image);
            case BufferedImage.TYPE_BYTE_BINARY:
                switch(image.getColorModel().getPixelSize()) {
                    case 1:
                        return AWTImageConverter_1Bit.getInstance().convert(image);
                    case 4:
                        return AWTImageConverter_4Bit.getInstance().convert(image);
                    case 8:
                        return AWTImageConverter_8Bit.getInstance().convert(image);
                    default:
                        return AWTImageConverter_Custom.getInstance().convert(image);
                }
            case BufferedImage.TYPE_BYTE_INDEXED:
                return AWTImageConverter_8Bit.getInstance().convert(image);
            case BufferedImage.TYPE_BYTE_GRAY:
                return AWTImageConverter_Gray.getInstance().convert(image);
            default:
                return AWTImageConverter_Custom.getInstance().convert(image);
        }
    }

    public static BufferedImage convert(FreeImage image) {
        switch(image.getBitsPerPixel()) {
            case 1:
                return AWTImageConverter_1Bit.getInstance().convert(image);
            case 4:
                return AWTImageConverter_4Bit.getInstance().convert(image);
            case 8:
                return AWTImageConverter_8Bit.getInstance().convert(image);
            case 16:
                return AWTImageConverter_16_RGB.getInstance().convert(image);
            case 24:
                return AWTImageConverter_24_RGB.getInstance().convert(image);
            case 32:
                return AWTImageConverter_32_ARGB.getInstance().convert(image);
        }
        throw new FreeImageRuntimeException("No converter available for image with bit depths: " + image.getBitsPerPixel());
    }
}
