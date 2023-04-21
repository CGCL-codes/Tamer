package com.mia.sct.view.instruments;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import org.apache.log4j.Logger;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import com.mia.sct.util.MIAImageUtils;
import com.mia.sct.view.MiaColors;

/**
 * ScaleOverlayUI
 * 
 * Lockable layer for displaying scales
 *
 * @author Devon Bryant
 * @since Apr 17, 2009
 */
public class ScaleOverlayUI extends AbstractLayerUI<JComponent> {

    private static Logger logger = Logger.getLogger(ScaleOverlayUI.class);

    private boolean locked = false;

    private BufferedImage scaleNameImg = null;

    private BufferedImage scaleNameBackImg = null;

    private int xImgOffset = 0;

    private int width = 0;

    private Rectangle scaleNameBounds = null;

    private String scaleName = null;

    public boolean shaded = false;

    private Color fillColor;

    private Painter<JComponent> scaleNamePainter = null;

    private Font scaleNameFont = null;

    private static final int HEIGHT_PADDING = 8;

    public ScaleOverlayUI() {
        super();
        initOverlay();
    }

    private void initOverlay() {
        GlossPainter<JComponent> glossPainter = new GlossPainter<JComponent>(MiaColors.Black.alpha(0.4f), GlossPainter.GlossPosition.BOTTOM);
        GradientPaint gradient = new GradientPaint(0f, 0f, Color.LIGHT_GRAY, 0f, 1f, Color.BLACK);
        MattePainter<JComponent> mattePainter = new MattePainter<JComponent>(gradient);
        mattePainter.setPaintStretched(true);
        scaleNamePainter = new CompoundPainter<JComponent>(mattePainter, glossPainter);
        scaleNameFont = new Font("Arial", Font.BOLD, 16);
        fillColor = MiaColors.Black.alpha(0.1f);
    }

    private void initScaleNameImage() {
        try {
            scaleNameImg = MIAImageUtils.getTextImage(scaleName, scaleNameFont, Color.white);
            xImgOffset = scaleNameImg.getWidth() / 2;
            scaleNameBounds = null;
        } catch (Exception exc) {
            logger.error("ScaleOverlayUI.initScaleNameImage():", exc);
        }
    }

    private void initScaleNameBackground(int inWidth) {
        try {
            scaleNameBackImg = MIAImageUtils.getPainterImage(inWidth, scaleNameImg.getHeight() + HEIGHT_PADDING, scaleNamePainter);
            width = inWidth;
        } catch (Exception exc) {
            logger.error("ScaleOverlayUI.initScaleNameBackground():", exc);
        }
    }

    /**
	 * @return the locked
	 */
    public boolean isLocked() {
        return locked;
    }

    /**
	 * @param inLocked the locked to set
	 */
    public void setLocked(boolean inLocked) {
        locked = inLocked;
        firePropertyChange("locked", !locked, locked);
    }

    /**
	 * Set the scale name
	 * @param inScaleName the scaleName to set
	 */
    public void setScaleName(String inScaleName) {
        scaleName = inScaleName;
        initScaleNameImage();
    }

    @Override
    protected void paintLayer(Graphics2D g2, JXLayer<JComponent> l) {
        super.paintLayer(g2, l);
        if (isLocked()) {
            if (scaleNameImg != null) {
                Color color = g2.getColor();
                g2.setColor(fillColor);
                g2.fillRect(0, 0, l.getWidth(), l.getHeight());
                g2.setColor(color);
                int xLoc = (l.getWidth() / 2) - xImgOffset;
                if (widthChanged(l.getWidth())) {
                    initScaleNameBackground(l.getWidth());
                }
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
                g2.drawImage(scaleNameBackImg, 0, 0, null);
                g2.drawImage(scaleNameImg, xLoc, HEIGHT_PADDING / 2, null);
                if (scaleNameBounds == null) {
                    scaleNameBounds = new Rectangle(xLoc, 0, scaleNameBackImg.getWidth(), scaleNameBackImg.getHeight());
                }
            }
        }
    }

    private boolean widthChanged(int inWidth) {
        boolean result = false;
        if (width != inWidth) {
            result = true;
        }
        return result;
    }

    @Override
    protected void processMouseEvent(MouseEvent inEvent, JXLayer<JComponent> inLayer) {
        super.processMouseEvent(inEvent, inLayer);
        if ((scaleNameImg != null) && (isLocked())) {
            if (inEvent.getID() == MouseEvent.MOUSE_CLICKED) {
                if (scaleNameBounds.contains(inEvent.getPoint())) {
                    setLocked(false);
                    setDirty(true);
                }
            }
        }
    }
}
