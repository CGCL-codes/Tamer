package com.rapidminer.gui.report;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import com.rapidminer.gui.tools.SwingTools;

/** 
 * This class provides a GUI-Component used to build the preview window in 
 * ReportOptionsDialog. 
 * 
 * @author Helge Homburg
 * @version $Id$
 */
public class PreviewPanel extends JPanel {

    private static final long serialVersionUID = -8755275262281699900L;

    private BufferedImage bufferedPreview;

    public void setPreviewImage(Image preview) {
        bufferedPreview = new BufferedImage(preview.getWidth(null), preview.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics graphics = bufferedPreview.getGraphics();
        graphics.drawImage(preview, 0, 0, null);
        graphics.dispose();
        this.setBackground(Color.WHITE);
    }

    public void setText(String[] text) {
        Graphics2D graphics2D = bufferedPreview.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setFont(new Font("SansSerif", Font.PLAIN, 90));
        TextLayout title = new TextLayout(text[0], graphics2D.getFont(), graphics2D.getFontRenderContext());
        graphics2D.setFont(new Font("SansSerif", Font.PLAIN, 60));
        TextLayout dimension = new TextLayout(text[1], graphics2D.getFont(), graphics2D.getFontRenderContext());
        graphics2D.setPaint(SwingTools.RAPID_I_BROWN);
        Rectangle2D bounds = title.getBounds();
        double x = bufferedPreview.getWidth() / 2 - bounds.getWidth() / 2;
        double y = bufferedPreview.getHeight() / 2 - bounds.getHeight() * 2 - bounds.getY();
        title.draw(graphics2D, (float) x, (float) y);
        graphics2D.setPaint(SwingTools.RAPID_I_ORANGE);
        bounds = title.getBounds();
        x = bufferedPreview.getWidth() / 2 - bounds.getWidth() / 2;
        y = bufferedPreview.getHeight() / 2 - bounds.getHeight() - bounds.getY();
        dimension.draw(graphics2D, (float) x, (float) y);
        graphics2D.dispose();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2D = (Graphics2D) graphics;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (this.bufferedPreview != null) {
            Insets borderInsets = this.getInsets();
            int paintWidth = this.getWidth() - (borderInsets.left + borderInsets.right);
            int paintHeight = this.getHeight() - (borderInsets.top + borderInsets.bottom);
            int imageWidth = this.bufferedPreview.getWidth();
            int imageHeight = this.bufferedPreview.getHeight();
            double scaleWidth = (double) paintWidth / imageWidth;
            double scaleHeight = (double) paintHeight / imageHeight;
            int width, height;
            if (scaleWidth <= scaleHeight) {
                width = (int) (imageWidth * scaleWidth);
                height = (int) (imageHeight * scaleWidth);
            } else {
                width = (int) (imageWidth * scaleHeight);
                height = (int) (imageHeight * scaleHeight);
            }
            borderInsets.set(borderInsets.top + (paintHeight - height) / 2, borderInsets.left + (paintWidth - width) / 2, borderInsets.bottom, borderInsets.right);
            g2D.drawImage(this.bufferedPreview, borderInsets.left, borderInsets.top, width, height, this);
        }
    }
}
