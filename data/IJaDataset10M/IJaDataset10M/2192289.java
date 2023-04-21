package net.jalbum.filters;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import net.jalbum.filterManager.FilterProperties.FilterCategory;
import se.datadosen.jalbum.Msg;

public class ColorAdjustmentFilterPlugin extends BasicFilter {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7171960078871425785L;

    protected double brightness = 1;

    protected double contrast = 1;

    protected double saturation = 1;

    protected boolean absoluteBrightness = true;

    protected ColorAdjustmentControl colorAdjustmentControl;

    public ColorAdjustmentFilterPlugin() {
        this.name = Msg.getString(this, "filters.colorAdjustment.name");
        this.shortName = Msg.getString(this, "filters.colorAdjustment.shortName");
        this.icon = new ImageIcon(this.getClass().getResource("res/color.png"));
        this.author = "David Fichtmueller";
        this.version = "1.0";
        this.description = Msg.getString(this, "filters.colorAdjustment.description");
        this.category = FilterCategory.ADVANCED;
        this.prescale = true;
        this.postscale = false;
        this.colorAdjustmentControl = new ColorAdjustmentControl(this);
    }

    /**
	 * We never touch the original image so avoid cloning
	 * @param bi
	 * @return
	 */
    @Override
    protected BufferedImage cloneBufferedImage(final BufferedImage bi) {
        return bi;
    }

    @Override
    public BufferedImage renderImage(final BufferedImage bi) {
        final int w = bi.getWidth();
        final int h = bi.getHeight();
        final BufferedImage out = new BufferedImage(w, h, bi.getType());
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int rgb = bi.getRGB(x, y);
                int alpha = (int) ((rgb & 0xff000000l) >> 24);
                int r = ((rgb & 0x00ff0000) >> 16);
                int g = ((rgb & 0x0000ff00) >> 8);
                int b = ((rgb & 0x000000ff));
                if (this.absoluteBrightness) {
                    r = r + (int) (2.55 * this.brightness);
                    g = g + (int) (2.55 * this.brightness);
                    b = b + (int) (2.55 * this.brightness);
                } else {
                    r = (int) (r * this.brightness);
                    g = (int) (g * this.brightness);
                    b = (int) (b * this.brightness);
                }
                double Y = r * 0.299 + g * 0.587 + b * 0.114;
                double Cb = r * -0.168736 + g * -0.331264 + b * 0.5;
                double Cr = r * 0.5 + g * -0.418688 + b * -0.081312;
                Cb = Cb * this.saturation;
                Cr = Cr * this.saturation;
                Y = (Y - 127) * this.contrast + 127;
                Cb = Cb * this.contrast;
                Cr = Cr * this.contrast;
                r = (int) (Y + (Cr * 1.402));
                g = (int) (Y + (Cb * -0.344136) + (Cr * -0.714136));
                b = (int) (Y + (Cb * 1.772));
                if (alpha > 255) {
                    alpha = 255;
                } else if (alpha < 0) {
                    alpha = 0;
                }
                if (g > 255) {
                    g = 255;
                } else if (g < 0) {
                    g = 0;
                }
                if (r > 255) {
                    r = 255;
                } else if (r < 0) {
                    r = 0;
                }
                if (b > 255) {
                    b = 255;
                } else if (b < 0) {
                    b = 0;
                }
                rgb = ((alpha & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                out.setRGB(x, y, rgb);
            }
        }
        return out;
    }

    public void adjustContrast(int contrast) {
        final double gamma = 0.25;
        if (contrast > 0) {
            this.contrast = (100 * Math.pow(contrast - 1, 1 / gamma) / Math.pow(100, 1 / gamma)) + 1;
        } else if (contrast == 0) {
            this.contrast = 1;
        } else {
            this.contrast = 1 / ((100 * Math.pow(-contrast + 1, 1 / gamma) / Math.pow(100, 1 / gamma)) + 1);
        }
        this.renderImage();
    }

    public void adjustBrightness(int brightness) {
        final double gamma = 0.25;
        if (this.absoluteBrightness) {
            this.brightness = brightness;
        } else {
            if (brightness > 0) {
                this.brightness = (100 * Math.pow(brightness, 1 / gamma) / Math.pow(100, 1 / gamma)) + 1;
            } else if (brightness == 0) {
                this.brightness = 1;
            } else {
                this.brightness = 1 / ((100 * Math.pow(-brightness, 1 / gamma) / Math.pow(100, 1 / gamma)) + 1);
            }
        }
        this.renderImage();
    }

    public void adjustSaturation(int saturation) {
        final double gamma = 0.25;
        if (saturation > 0) {
            this.saturation = (100 * Math.pow(saturation - 1, 1 / gamma) / Math.pow(100, 1 / gamma)) + 1;
        } else if (saturation == 0) {
            this.saturation = 1;
        } else {
            this.saturation = 1 / ((100 * Math.pow(-saturation + 1, 1 / gamma) / Math.pow(100, 1 / gamma)) + 1);
        }
        this.renderImage();
    }

    @Override
    public JPanel getControls() {
        return this.colorAdjustmentControl;
    }

    public double getContrast() {
        return this.contrast;
    }

    public void setContrast(final double contrast) {
        this.contrast = contrast;
    }

    public double getBrightness() {
        return this.brightness;
    }

    public void setBrightness(final double brightness) {
        this.brightness = brightness;
    }

    public double getSaturation() {
        return this.saturation;
    }

    public void setSaturation(final double saturation) {
        this.saturation = saturation;
    }

    public boolean isAbsoluteBrightness() {
        return this.absoluteBrightness;
    }

    public void setAbsoluteBrightness(final boolean absoluteBrightness) {
        this.absoluteBrightness = absoluteBrightness;
    }
}
