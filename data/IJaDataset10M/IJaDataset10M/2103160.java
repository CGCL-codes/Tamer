package org.xith3d.ui.hud.utils;

import org.openmali.FastMath;
import org.openmali.vecmath2.Colorf;
import org.xith3d.scenegraph.Texture2DCanvas;
import org.xith3d.scenegraph.TextureImage2D;

/**
 * Default implementation of the {@link DropShadowFactory}.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class DefaultDropShadowFactory extends DropShadowFactory {

    private int offsetX = 0;

    private int offsetY = 0;

    private Colorf startColor = new Colorf(0f, 0f, 0f, 0f);

    private byte[] pixelLine = null;

    protected int getOffsetX() {
        return (offsetX);
    }

    protected int getOffsetY() {
        return (offsetY);
    }

    protected Colorf getStartColor() {
        return (startColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawDropShadow(int widgetRight, int widgetBottom, int widgetWidth, int widgetHeight, int zIndex, Texture2DCanvas texCanvas) {
        TextureImage2D image = texCanvas.getImage();
        int offsetX = getOffsetX();
        int offsetY = getOffsetY();
        int shadowWidth = getDropShadowWidth();
        int shadowHeight = getDropShadowHeight();
        int bufferSize = (widgetWidth + getDropShadowWidth()) * 4;
        if ((pixelLine == null) || (pixelLine.length < bufferSize)) {
            pixelLine = new byte[bufferSize];
        }
        Colorf startColor = getStartColor();
        byte r0 = startColor.getRedByte();
        byte g0 = startColor.getGreenByte();
        byte b0 = startColor.getBlueByte();
        int a0 = startColor.getAlphaInt();
        for (int j = 0; j < shadowWidth; j++) {
            for (int i = 0; i < shadowWidth; i++) {
                int dist = Math.min((int) FastMath.sqrt(i * i + (shadowWidth - j) * (shadowWidth - j)), shadowWidth);
                pixelLine[i * 4 + 0] = r0;
                pixelLine[i * 4 + 1] = g0;
                pixelLine[i * 4 + 2] = b0;
                pixelLine[i * 4 + 3] = (byte) ((shadowWidth - dist) * (255 - a0) / shadowWidth);
            }
            image.drawPixelLine(pixelLine, 4, widgetRight, widgetBottom - widgetHeight + offsetY + j, shadowWidth, true, null, true);
        }
        for (int j = offsetY + shadowWidth; j < widgetHeight; j++) {
            for (int i = 0; i < shadowWidth; i++) {
                pixelLine[i * 4 + 0] = r0;
                pixelLine[i * 4 + 1] = g0;
                pixelLine[i * 4 + 2] = b0;
                pixelLine[i * 4 + 3] = (byte) ((shadowWidth - i) * (255 - a0) / shadowWidth);
            }
            image.drawPixelLine(pixelLine, 4, widgetRight, widgetBottom - widgetHeight + j, shadowWidth, true, null, true);
        }
        for (int j = 0; j < shadowHeight; j++) {
            for (int i = 0; i < shadowHeight; i++) {
                int dist = Math.min((int) FastMath.sqrt((shadowHeight - i) * (shadowHeight - i) + j * j), shadowHeight);
                pixelLine[i * 4 + 0] = r0;
                pixelLine[i * 4 + 1] = g0;
                pixelLine[i * 4 + 2] = b0;
                pixelLine[i * 4 + 3] = (byte) ((shadowHeight - dist) * (255 - a0) / shadowHeight);
            }
            image.drawPixelLine(pixelLine, 4, widgetRight - widgetWidth + offsetX, widgetBottom + j, shadowHeight, true, null, true);
        }
        for (int j = 0; j < shadowHeight; j++) {
            for (int i = 0; i < widgetWidth - offsetX - shadowHeight; i++) {
                pixelLine[i * 4 + 0] = r0;
                pixelLine[i * 4 + 1] = g0;
                pixelLine[i * 4 + 2] = b0;
                pixelLine[i * 4 + 3] = (byte) ((shadowHeight - j) * (255 - a0) / shadowWidth);
            }
            image.drawPixelLine(pixelLine, 4, widgetRight - widgetWidth + offsetX + shadowHeight, widgetBottom + j, widgetWidth - offsetX - shadowHeight, true, null, true);
        }
        int m = (int) ((shadowWidth + shadowHeight) / 2f);
        for (int j = 0; j < shadowHeight; j++) {
            for (int i = 0; i < shadowWidth; i++) {
                int dist = Math.min((int) FastMath.sqrt(i * i + j * j), m);
                pixelLine[i * 4 + 0] = r0;
                pixelLine[i * 4 + 1] = g0;
                pixelLine[i * 4 + 2] = b0;
                pixelLine[i * 4 + 3] = (byte) ((m - dist) * (255 - a0) / m);
            }
            image.drawPixelLine(pixelLine, 4, widgetRight, widgetBottom + j, shadowWidth, true, null, true);
        }
    }

    public DefaultDropShadowFactory(int width, int height) {
        super(width, height);
    }

    public DefaultDropShadowFactory() {
        this(20, 20);
    }
}
