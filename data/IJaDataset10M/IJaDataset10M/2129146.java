package net.jalbum.filters;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.ImageIcon;
import net.jalbum.filterManager.FilterProperties.FilterCategory;
import se.datadosen.jalbum.ModifiesSize;
import se.datadosen.jalbum.Msg;

public class CroppingFilterPlugin extends BoxPanel implements ModifiesSize {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6690447553047905588L;

    CroppingControl croppingControl;

    public CroppingFilterPlugin() {
        this.name = Msg.getString(this, "filters.cropping.name");
        this.shortName = Msg.getString(this, "filters.cropping.shortName");
        this.icon = new ImageIcon(this.getClass().getResource("res/crop.png"));
        this.author = "David Fichtmueller";
        this.version = "1.0";
        this.description = Msg.getString(this, "filters.cropping.description");
        this.category = FilterCategory.BASIC;
        this.prescale = true;
        this.postscale = false;
        this.X1 = 0.1;
        this.X2 = 0.9;
        this.Y1 = 0.1;
        this.Y2 = 0.9;
        this.resizableBox.setInitial(false);
        this.resizableBox.update(this.X1, this.Y1, this.X2, this.Y2);
        this.croppingControl = new CroppingControl(this);
        this.boxControl = this.croppingControl;
        this.resizableObjectControl = this.croppingControl;
        this.croppingControl.update(this.X1, this.Y1, this.X2 - this.X1, this.Y2 - this.Y1);
    }

    @Override
    public void setPreviewImage(final BufferedImage bi) {
        super.setPreviewImage(bi);
        this.croppingControl.initiatePredefinedRatios();
        this.X1 = 0.1;
        this.X2 = 0.9;
        this.Y1 = 0.1;
        this.Y2 = 0.9;
        this.resizableBox.setInitial(false);
        this.resizableBox.update(this.X1, this.Y1, this.X2, this.Y2);
        this.resizableBox.adjustKeepRatio(false);
        this.renderPreview();
    }

    @Override
    public BufferedImage renderImage(final BufferedImage bi) {
        return bi.getSubimage((int) (this.X1 * bi.getWidth()), (int) (this.Y1 * bi.getHeight()), (int) ((this.X2 - this.X1) * bi.getWidth()), (int) ((this.Y2 - this.Y1) * bi.getHeight()));
    }

    @Override
    public void renderPreview() {
        this.outputImage = this.filterManager.cloneBufferedImage(this.inputImage);
        this.paint(this.outputImage.getGraphics());
        this.filterManager.setGUIImage(this.outputImage);
    }

    @Override
    public void paint(final Graphics g) {
        g.setColor(new Color(255, 255, 255, 127));
        final int intX1 = (int) (this.resizableBox.getOutX1() * (this.width - 1));
        final int intX2 = (int) (this.resizableBox.getOutX2() * (this.width - 1));
        final int intY1 = (int) (this.resizableBox.getOutY1() * (this.height - 1));
        final int intY2 = (int) (this.resizableBox.getOutY2() * (this.height - 1));
        if (intX2 + 1 > 0 && intY1 > 0) {
            g.fillRect(0, 0, intX2 + 1, intY1);
        }
        if (this.resizableBox.getWidth() - 1 - intX2 > 0 && intY2 + 1 > 0) {
            g.fillRect(intX2 + 1, 0, this.resizableBox.getWidth() - 1 - intX2, intY2 + 1);
        }
        if (this.resizableBox.getWidth() - intX1 > 0 && this.resizableBox.getHeight() - 1 - intY2 > 0) {
            g.fillRect(intX1, intY2 + 1, this.resizableBox.getWidth() - intX1, this.resizableBox.getHeight() - 1 - intY2);
        }
        if (intX1 > 0 && this.resizableBox.getHeight() - intY1 > 0) {
            g.fillRect(0, intY1, intX1, this.resizableBox.getHeight() - intY1);
        }
        this.resizableBox.paintHandles(g);
    }

    public Dimension getModifiedSize(final Dimension dimension, final Map arg1) {
        return new Dimension((int) ((this.X2 - this.X1) * dimension.getWidth()), (int) ((this.Y2 - this.Y1) * dimension.getHeight()));
    }
}
