package com.barteo.emulator.device.swt;

import javax.microedition.lcdui.Image;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import com.barteo.emulator.app.ui.swt.ImageFilter;
import com.barteo.emulator.app.ui.swt.SwtGraphics;
import com.barteo.emulator.device.DeviceDisplay;
import com.barteo.emulator.device.DeviceFactory;
import com.barteo.emulator.device.DisplayGraphics;
import com.barteo.emulator.device.MutableImage;

public class SwtDisplayGraphics extends javax.microedition.lcdui.Graphics implements DisplayGraphics {

    public SwtGraphics g;

    private MutableImage image;

    private int color = 0;

    private javax.microedition.lcdui.Font currentFont = javax.microedition.lcdui.Font.getDefaultFont();

    public SwtDisplayGraphics(SwtGraphics a_g, MutableImage a_image) {
        g = a_g;
        image = a_image;
        g.setBackground(g.getColor(new RGB(((SwtDeviceDisplay) DeviceFactory.getDevice().getDeviceDisplay()).getBackgroundColor().getRed(), ((SwtDeviceDisplay) DeviceFactory.getDevice().getDeviceDisplay()).getBackgroundColor().getGreen(), ((SwtDeviceDisplay) DeviceFactory.getDevice().getDeviceDisplay()).getBackgroundColor().getBlue())));
        g.setFont(((SwtFontManager) DeviceFactory.getDevice().getFontManager()).getFont(currentFont));
    }

    public MutableImage getImage() {
        return image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int RGB) {
        ImageFilter filter = null;
        color = RGB;
        DeviceDisplay deviceDisplay = DeviceFactory.getDevice().getDeviceDisplay();
        if (deviceDisplay.isColor()) {
            filter = new RGBImageFilter();
        } else {
            if (deviceDisplay.numColors() == 2) {
                filter = new BWImageFilter();
            } else {
                filter = new GrayImageFilter();
            }
        }
        g.setForeground(g.getColor(filter.filterRGB(0, 0, new RGB((color >> 16) % 256, (color >> 8) % 256, color % 256))));
    }

    public javax.microedition.lcdui.Font getFont() {
        return currentFont;
    }

    public void setFont(javax.microedition.lcdui.Font font) {
        currentFont = font;
        g.setFont(((SwtFontManager) DeviceFactory.getDevice().getFontManager()).getFont(currentFont));
    }

    public void clipRect(int x, int y, int width, int height) {
        Rectangle rect = new Rectangle(x, y, width, height);
        if (rect.x < getClipX()) {
            rect.x = getClipX();
        }
        if (rect.y < getClipY()) {
            rect.y = getClipY();
        }
        if (x + width > getClipX() + getClipWidth()) {
            rect.width = getClipX() + getClipWidth() - rect.x;
        } else {
            rect.width = x + width - rect.x;
        }
        if (y + height > getClipY() + getClipHeight()) {
            rect.height = getClipY() + getClipHeight() - rect.y;
        } else {
            rect.height = y + height - rect.y;
        }
        setClip(rect.x, rect.y, rect.width, rect.height);
    }

    public void setClip(int x, int y, int width, int height) {
        g.setClipping(x, y, width, height);
    }

    public int getClipX() {
        Rectangle rect = g.getClipping();
        if (rect == null) {
            return 0;
        } else {
            return rect.x;
        }
    }

    public int getClipY() {
        Rectangle rect = g.getClipping();
        if (rect == null) {
            return 0;
        } else {
            return rect.y;
        }
    }

    public int getClipHeight() {
        Rectangle rect = g.getClipping();
        if (rect == null) {
            return DeviceFactory.getDevice().getDeviceDisplay().getHeight();
        } else {
            return rect.height;
        }
    }

    public int getClipWidth() {
        Rectangle rect = g.getClipping();
        if (rect == null) {
            return DeviceFactory.getDevice().getDeviceDisplay().getWidth();
        } else {
            return rect.width;
        }
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        g.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawImage(Image img, int x, int y, int anchor) {
        int newx = x;
        int newy = y;
        if (anchor == 0) {
            anchor = javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT;
        }
        if ((anchor & javax.microedition.lcdui.Graphics.RIGHT) != 0) {
            newx -= img.getWidth();
        } else if ((anchor & javax.microedition.lcdui.Graphics.HCENTER) != 0) {
            newx -= img.getWidth() / 2;
        }
        if ((anchor & javax.microedition.lcdui.Graphics.BOTTOM) != 0) {
            newy -= img.getHeight();
        } else if ((anchor & javax.microedition.lcdui.Graphics.VCENTER) != 0) {
            newy -= img.getHeight() / 2;
        }
        if (img.isMutable()) {
            g.drawImage(((SwtMutableImage) img).getImage(), newx, newy);
        } else {
            g.drawImage(((SwtImmutableImage) img).getImage(), newx, newy);
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

    public void drawRect(int x, int y, int width, int height) {
        drawLine(x, y, x + width, y);
        drawLine(x + width, y, x + width, y + height);
        drawLine(x + width, y + height, x, y + height);
        drawLine(x, y + height, x, y);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        g.drawRoundRectangle(x, y, width, height, arcWidth, arcHeight);
    }

    public void drawString(String str, int x, int y, int anchor) {
        int newx = x;
        int newy = y;
        if (anchor == 0) {
            anchor = javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT;
        }
        if ((anchor & javax.microedition.lcdui.Graphics.VCENTER) != 0) {
            newy -= g.getFontMetrics().getAscent();
        } else if ((anchor & javax.microedition.lcdui.Graphics.BOTTOM) != 0) {
            newy -= g.getFontMetrics().getHeight();
        }
        if ((anchor & javax.microedition.lcdui.Graphics.HCENTER) != 0) {
            newx -= g.stringWidth(str) / 2;
        } else if ((anchor & javax.microedition.lcdui.Graphics.RIGHT) != 0) {
            newx -= g.stringWidth(str);
        }
        g.drawString(str, newx, newy, true);
        if ((currentFont.getStyle() & javax.microedition.lcdui.Font.STYLE_UNDERLINED) != 0) {
            g.drawLine(newx, newy + 1, newx + g.stringWidth(str), newy + 1);
        }
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        Color tmp = g.getBackground();
        g.setBackground(g.getForeground());
        g.fillArc(x, y, width, height, startAngle, arcAngle);
        g.setBackground(tmp);
    }

    public void fillRect(int x, int y, int width, int height) {
        Color tmp = g.getBackground();
        g.setBackground(g.getForeground());
        g.fillRectangle(x, y, width, height);
        g.setBackground(tmp);
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        g.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight);
    }

    public void translate(int x, int y) {
        super.translate(x, y);
        g.translate(x, y);
    }
}
