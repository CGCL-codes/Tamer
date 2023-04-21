package visual3d.texture;

import visual3d.datastruct.RGB;
import visual3d.datastruct.Texture;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;
import java.awt.image.Raster;
import javax.media.jai.PixelAccessor;
import javax.media.jai.UnpackedImageData;

public class ScriptTexture implements Texture {

    private int iRepeat = REPEAT_NONE;

    private int iWidth;

    private int iHeight;

    private byte[][] pixels;

    private int[][][] pixelArray;

    private int realWidth;

    private int realHeight;

    /**
     * Font size
     */
    private int fontsize = 0;

    /**
     * Text color
     */
    private int r = 0;

    private int g = 0;

    private int b = 0;

    /**
     * Background colour
     */
    private int br = 255;

    private int bg = 255;

    private int bb = 255;

    /**
     * Used to obtain fontmetrics for given fontname
     */
    private Graphics2D g2;

    /**
     * Cached Font object
     */
    private Font cachedFont;

    public ScriptTexture(String text) {
        this.iHeight = 0;
        this.iWidth = 0;
        init(null, text);
    }

    public ScriptTexture(String text, int fontsize) {
        this.fontsize = fontsize;
        this.iHeight = 0;
        this.iWidth = 0;
        init(null, text);
    }

    public ScriptTexture(String text, int iWidth, int iHeight) {
        this.iHeight = iHeight;
        this.iWidth = iWidth;
        init(null, text);
    }

    public ScriptTexture(Font font, String text, int iWidth, int iHeight) {
        this.iHeight = iHeight;
        this.iWidth = iWidth;
        init(font, text);
    }

    public ScriptTexture(Font font, String text) {
        this.iHeight = 0;
        this.iWidth = 0;
        init(font, text);
    }

    private void init(Font font, String text) {
        this.g2 = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR).createGraphics();
        setOptimalRenderQuality(g2);
        if (font == null) {
            fontsize = 17;
            Font createdFont = new Font("Dialog", Font.TRUETYPE_FONT, fontsize);
            cachedFont = createdFont.deriveFont((float) fontsize);
        } else {
            if (fontsize != 0) {
                cachedFont = cachedFont.deriveFont((float) fontsize);
            } else {
                cachedFont = font;
            }
        }
        g2.setFont(cachedFont);
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout layout = new TextLayout(text, cachedFont, frc);
        Rectangle2D bounds = layout.getBounds();
        int stringWidth = (int) (Math.ceil(bounds.getWidth()));
        FontMetrics fm = g2.getFontMetrics();
        int stringHeight = fm.getHeight();
        BufferedImage image = new BufferedImage(stringWidth, stringHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        setOptimalRenderQuality(graphics);
        graphics.setBackground(new Color(br, bg, bb));
        graphics.setColor(new Color(r, g, b));
        graphics.clearRect(0, 0, stringWidth, stringHeight);
        graphics.setFont(cachedFont);
        layout.draw(graphics, -(float) Math.floor(bounds.getX()), fm.getMaxAscent());
        Raster raster = image.getData();
        SampleModel samplemodel = raster.getSampleModel();
        PixelAccessor p = new PixelAccessor(samplemodel, null);
        UnpackedImageData pdata = p.getPixels(raster, raster.getBounds(), samplemodel.getDataType(), false);
        pixels = pdata.getByteData();
        realWidth = raster.getWidth();
        realHeight = raster.getHeight();
        pixelArray = new int[realWidth][realHeight][3];
        for (int k = 0; k < realHeight; k++) for (int j = 0; j < realWidth; j++) {
            if (pixels[0][(realWidth * k + j) * 3 + 2] >= 0) {
                pixelArray[j][k][0] = pixels[0][(realWidth * k + j) * 3 + 2];
            } else {
                pixelArray[j][k][0] = 256 + pixels[0][(realWidth * k + j) * 3 + 2];
            }
            if (pixels[0][(realWidth * k + j) * 3 + 1] >= 0) {
                pixelArray[j][k][1] = pixels[0][(realWidth * k + j) * 3 + 1];
            } else {
                pixelArray[j][k][1] = 256 + pixels[0][(realWidth * k + j) * 3 + 1];
            }
            if (pixels[0][(realWidth * k + j) * 3] >= 0) {
                pixelArray[j][k][2] = pixels[0][(realWidth * k + j) * 3];
            } else {
                pixelArray[j][k][2] = 256 + pixels[0][(realWidth * k + j) * 3];
            }
        }
    }

    public int getWidth() {
        return iWidth == 0 ? realWidth : iWidth;
    }

    public int getHeight() {
        return iHeight == 0 ? realHeight : iHeight;
    }

    public RGB getTexel(int i, int j) {
        iWidth = getWidth();
        iHeight = getHeight();
        int realI;
        int realJ;
        switch(iRepeat) {
            case REPEAT_NONE:
                if (i >= realWidth || j >= realHeight) return null;
                if (i >= iWidth || j >= iHeight) return null;
                try {
                    return new RGB((int) pixelArray[i][j][0], (int) pixelArray[i][j][1], (int) pixelArray[i][j][2]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    return null;
                }
            case REPEAT_VERTICAL:
                if (j >= iHeight) return null;
                realI = i % iWidth;
                if (realI >= realWidth || j >= realHeight) return null;
                try {
                    return new RGB((int) pixelArray[realI][j][0], (int) pixelArray[realI][j][1], (int) pixelArray[realI][j][2]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    return null;
                }
            case REPEAT_HORIZONTAL:
                if (i >= iWidth) return null;
                realJ = j % iHeight;
                if (i >= realWidth) return null;
                if (realJ >= realHeight) return null;
                try {
                    return new RGB((int) pixelArray[i][realJ][0], (int) pixelArray[i][realJ][1], (int) pixelArray[i][realJ][2]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    return null;
                }
            case REPEAT_BOTH:
                realI = i % iWidth;
                realJ = j % iHeight;
                if (realI >= realWidth || realJ >= realHeight) return null;
                try {
                    return new RGB((int) pixelArray[realI][realJ][0], (int) pixelArray[realI][realJ][1], (int) pixelArray[realI][realJ][2]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    /**
     * Sets rendering hints for optimal rendering quality
     *
     * @param graphics Graphics2D object to set rendering options on
     */
    private void setOptimalRenderQuality(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }
}
