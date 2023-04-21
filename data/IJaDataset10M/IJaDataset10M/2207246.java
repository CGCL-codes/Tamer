package eu.pisolutions.ocelot.util;

import eu.pisolutions.lang.Validations;
import eu.pisolutions.ocelot.document.color.ColorSpace;
import eu.pisolutions.ocelot.document.color.device.DeviceCmykColorSpace;
import eu.pisolutions.ocelot.document.color.device.DeviceGrayColorSpace;
import eu.pisolutions.ocelot.document.color.device.DeviceRgbColorSpace;

public final class ColorSpaces extends Object {

    public static ColorSpace toPdfColorSpace(java.awt.color.ColorSpace awtColorSpace) {
        ColorSpaces.validateColorSpace(awtColorSpace);
        switch(awtColorSpace.getType()) {
            case java.awt.color.ColorSpace.TYPE_GRAY:
                return new DeviceGrayColorSpace();
            case java.awt.color.ColorSpace.TYPE_RGB:
                return new DeviceRgbColorSpace();
            case java.awt.color.ColorSpace.TYPE_CMYK:
                return new DeviceCmykColorSpace();
            default:
                throw new IllegalArgumentException("Cannot convert AWT color space " + awtColorSpace + " to PDF color space");
        }
    }

    public static java.awt.color.ColorSpace toAwtColorSpace(ColorSpace pdfColorSpace) {
        ColorSpaces.validateColorSpace(pdfColorSpace);
        if (pdfColorSpace instanceof DeviceGrayColorSpace) {
            return java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_GRAY);
        }
        if (pdfColorSpace instanceof DeviceRgbColorSpace) {
            return java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_sRGB);
        }
        throw new IllegalArgumentException("Cannot convert PDF color space " + pdfColorSpace + " to AWT color space");
    }

    private static void validateColorSpace(Object colorSpace) {
        Validations.notNull(colorSpace, "color space");
    }

    private ColorSpaces() {
        super();
    }
}
