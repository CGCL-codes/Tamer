package org.plazmaforge.studio.reportdesigner.util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import net.sf.jasperreports.engine.design.JRDesignFont;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.plazmaforge.framework.util.StringUtils;
import org.plazmaforge.studio.reportdesigner.model.DesignFont;

public class Utils {

    public static java.awt.Color getAWTColor(Color color) {
        if (color == null) {
            return null;
        }
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        java.awt.Color awtColor = new java.awt.Color(red, green, blue);
        return awtColor;
    }

    public static JRDesignFont getDesignFont(DesignFont font) {
        if (font == null) {
            return null;
        }
        JRDesignFont jrFont = new JRDesignFont();
        if (!font.isEmptyFontName()) {
            jrFont.setFontName(font.getFontName());
        }
        if (!font.isEmptyFontSize()) {
            jrFont.setFontSize(font.getFontSize());
        }
        if (font.isBold()) {
            jrFont.setBold(true);
        }
        if (font.isItalic()) {
            jrFont.setItalic(true);
        }
        if (font.isStrikeout()) {
            jrFont.setStrikeThrough(true);
        }
        if (font.isUnderline()) {
            jrFont.setUnderline(true);
        }
        if (font.isPdfEmbedded()) {
            jrFont.setPdfEmbedded(true);
        }
        String pdfFontName = font.getPdfFontName();
        String pdfEncoding = font.getPdfEncoding();
        if (!isEmpty(pdfFontName)) {
            jrFont.setPdfFontName(pdfFontName);
        }
        if (!isEmpty(pdfEncoding)) {
            jrFont.setPdfEncoding(pdfEncoding);
        }
        return jrFont;
    }

    public static JRDesignFont getDesignFont(Font font) {
        if (font == null) {
            return null;
        }
        JRDesignFont jrFont = new JRDesignFont();
        FontData fd = font.getFontData()[0];
        String fontName = fd.getName();
        if (!isEmpty(fontName)) {
            jrFont.setFontName(fontName);
        }
        jrFont.setFontSize(fd.getHeight());
        int style = fd.getStyle();
        if ((style & SWT.BOLD) != 0) {
            jrFont.setBold(true);
        }
        if ((style & SWT.ITALIC) != 0) {
            jrFont.setItalic(true);
        }
        boolean strikeout = SWTResourceManager.isStrikeoutFont(font);
        boolean underline = SWTResourceManager.isUnderlineFont(font);
        if (strikeout) {
            jrFont.setStrikeThrough(true);
        }
        if (underline) {
            jrFont.setUnderline(true);
        }
        return jrFont;
    }

    public static java.awt.Font getAWTFont(Font font) {
        if (font == null) {
            return null;
        }
        FontData fd = font.getFontData()[0];
        String name = fd.getName();
        int size = (int) fd.height;
        int style = fd.getStyle();
        int awtStyle = 0;
        if ((style & SWT.BOLD) != 0) {
            awtStyle |= java.awt.Font.BOLD;
        }
        if ((style & SWT.ITALIC) != 0) {
            awtStyle |= java.awt.Font.ITALIC;
        }
        return new java.awt.Font(name, awtStyle, size);
    }

    public static BufferedImage convertToAWT(ImageData data) {
        ColorModel colorModel = null;
        PaletteData palette = data.palette;
        if (palette.isDirect) {
            colorModel = new DirectColorModel(data.depth, palette.redMask, palette.greenMask, palette.blueMask);
            BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    int pixel = data.getPixel(x, y);
                    RGB rgb = palette.getRGB(pixel);
                    bufferedImage.setRGB(x, y, rgb.red << 16 | rgb.green << 8 | rgb.blue);
                }
            }
            return bufferedImage;
        } else {
            RGB[] rgbs = palette.getRGBs();
            byte[] red = new byte[rgbs.length];
            byte[] green = new byte[rgbs.length];
            byte[] blue = new byte[rgbs.length];
            for (int i = 0; i < rgbs.length; i++) {
                RGB rgb = rgbs[i];
                red[i] = (byte) rgb.red;
                green[i] = (byte) rgb.green;
                blue[i] = (byte) rgb.blue;
            }
            if (data.transparentPixel != -1) {
                colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue, data.transparentPixel);
            } else {
                colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
            }
            BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
            WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[1];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    int pixel = data.getPixel(x, y);
                    pixelArray[0] = pixel;
                    raster.setPixel(x, y, pixelArray);
                }
            }
            return bufferedImage;
        }
    }

    private static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }
}
