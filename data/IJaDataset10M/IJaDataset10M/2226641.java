package org.tigr.microarray.mev.cgh.CGHGuiObj.CGHPositionGraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import org.tigr.microarray.mev.cgh.CGHDataModel.CGHPositionGraphDataModel;
import org.tigr.microarray.mev.cluster.gui.IDisplayMenu;

public class CGHPositionGraphHeader extends JPanel {

    protected Insets insets;

    protected int contentWidth;

    protected int elementWidth;

    protected boolean isAntiAliasing = true;

    protected boolean isTracing = false;

    protected int tracespace;

    protected final int RECT_HEIGHT = 15;

    BufferedImage negColorImage;

    BufferedImage posColorImage;

    protected float maxValue;

    protected float minValue;

    CGHPositionGraphDataModel model;

    int rectSpacing = 5;

    /**
     * Constructs a <code>MultipleArrayHeader</code> with specified
     * insets and trace space.
     */
    public CGHPositionGraphHeader(Insets insets) {
        setBackground(Color.black);
        setSize(200, 200);
        setPreferredSize(new Dimension(200, 200));
        this.insets = insets;
        this.maxValue = 3.0f;
        this.minValue = 0.0f;
    }

    /**
     * Sets the anti-aliasing attribute.
     */
    public void setAntiAliasing(boolean isAntiAliasing) {
        this.isAntiAliasing = isAntiAliasing;
    }

    /**
     * Sets the element width attribute.
     */
    void setElementWidth(int width) {
        this.elementWidth = width;
        setFontSize(width);
        updateSize();
        this.repaint();
    }

    /**
     * Sets the content width attribute.
     */
    void setContentWidth(int width) {
        this.contentWidth = width;
        this.repaint();
    }

    /**
     * Sets min and max ratio values
     */
    public void setMinAndMaxRatios(float min, float max) {
        this.minValue = min;
        this.maxValue = max;
        this.repaint();
    }

    /**
     * Sets the isTracing attribute.
     */
    void setTracing(boolean isTracing) {
        this.isTracing = isTracing;
    }

    /**
     * Returns a trace space value.
     */
    private int getSpacing() {
        return rectSpacing;
    }

    /**
     * Sets the component font size.
     */
    private void setFontSize(int width) {
        if (width > 12) {
            width = 12;
        }
        setFont(new Font("monospaced", Font.PLAIN, width));
    }

    /**
     * Updates the header size.
     */
    void updateSize() {
        Graphics2D g = (Graphics2D) getGraphics();
        if (g == null) {
            return;
        }
        if (isAntiAliasing) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        FontMetrics hfm = g.getFontMetrics();
        int maxHeight = 0;
        String name;
        final int size = model.getNumExperiments();
        for (int feature = 0; feature < size; feature++) {
            name = model.getExperimentName(feature);
            if (name == null) {
                maxHeight = 1;
            } else {
                maxHeight = Math.max(maxHeight, hfm.stringWidth(name));
            }
        }
        setSize(contentWidth, maxHeight + 10 + this.RECT_HEIGHT + hfm.getHeight());
        setPreferredSize(new Dimension(contentWidth, maxHeight + 10 + this.RECT_HEIGHT + hfm.getHeight()));
    }

    /** Getter for property model.
     * @return Value of property model.
     */
    public CGHPositionGraphDataModel getModel() {
        return model;
    }

    /** Setter for property model.
     * @param model New value of property model.
     */
    public void setModel(CGHPositionGraphDataModel model) {
        this.model = model;
        this.negColorImage = model.getNegColorImage();
        this.posColorImage = model.getPosColorImage();
    }

    /** Getter for property insets.
     * @return Value of property insets.
     */
    public java.awt.Insets getInsets() {
        return insets;
    }

    /** Setter for property insets.
     * @param insets New value of property insets.
     */
    public void setInsets(java.awt.Insets insets) {
        this.insets = insets;
    }

    public void onMenuChanged(IDisplayMenu menu) {
        model.setNegColorImage(menu.getNegativeGradientImage());
        model.setPosColorImage(menu.getPositiveGradientImage());
        setMinAndMaxRatios(menu.getMinRatioScale(), menu.getMaxRatioScale());
    }
}
