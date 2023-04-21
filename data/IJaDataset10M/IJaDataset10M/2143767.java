package com.visitrend.ndvis.gui.spi;

import com.visitrend.ndvis.event.api.DataVisualizationListener;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author ao
 */
public interface DataVisualization extends Lookup.Provider {

    enum ZoomMode {

        IN, OUT, PAN
    }

    BufferedImage getOffScreenImage();

    AffineTransform getTransform();

    void setTransform(AffineTransform at);

    boolean isPounded();

    void setPounded(boolean pounded);

    void recolor();

    void resetImageManipulation();

    /**
     * The BufferedImage parameter will serve as the "offscreen image" for this
     * ImagePanel. It will be painted on the ImagePanel using any transforms in
     * the {@link #paintComponent(Graphics)} method.
     *
     * The ImagePanel's size will always be set to the dimensions of the
     * BufferedImage parameter.
     *
     * @param img
     */
    void setOffScreenImage(BufferedImage img);

    TopComponent getTopComponent();

    JComponent getImagePane();

    boolean isParamControllerListening();

    void addImagePanelListener(DataVisualizationListener listener);

    void removeImagePanelListener(DataVisualizationListener listener);

    void setParamControllerListening(boolean paramControllerListening);

    void fireSaveStateChange(boolean modified);
}
