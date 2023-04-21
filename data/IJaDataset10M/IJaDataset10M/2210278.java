package com.visitrend.ndvis.gui;

import com.visitrend.ndvis.Features;
import com.visitrend.ndvis.actions.PanUtility;
import com.visitrend.ndvis.actions.ZoomUtility;
import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import org.openide.awt.StatusDisplayer;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Geertjan Wielenga
 */
class ZoomHelper {

    private final ImagePanel panel;

    public ZoomHelper(ImagePanel panel) {
        this.panel = panel;
    }

    private RadioButtonMouseListener radioButtonMouseListener;

    private PanMouseListener panMouseListener;

    private PanMouseMotionListener panMouseMotionListener;

    private PanUtility pan;

    @FeatureEntryPoint(Features.ADJUST_IMAGE)
    public void setZoom(Zoomable.Mode mode) {
        panel.removeMouseListener(radioButtonMouseListener);
        panel.removeMouseListener(panMouseListener);
        panel.removeMouseMotionListener(panMouseMotionListener);
        switch(mode) {
            case IN:
                {
                    radioButtonMouseListener = new RadioButtonMouseListener(1.1);
                    panel.addMouseListener(radioButtonMouseListener);
                    ImageIcon ICON = ImageUtilities.loadImageIcon("toolbarButtonGraphics/general/ZoomIn16.gif", true);
                    Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(ICON.getImage(), new Point(0, 0), "ZOOMIN");
                    panel.setCursor(c);
                    StatusDisplayer.getDefault().setStatusText("Zoom in enabled...");
                }
                break;
            case OUT:
                {
                    radioButtonMouseListener = new RadioButtonMouseListener(0.9);
                    panel.addMouseListener(radioButtonMouseListener);
                    ImageIcon ICON = ImageUtilities.loadImageIcon("toolbarButtonGraphics/general/ZoomOut16.gif", true);
                    Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(ICON.getImage(), new Point(0, 0), "ZOOMOUT");
                    panel.setCursor(c);
                    StatusDisplayer.getDefault().setStatusText("Zoom out enabled...");
                }
                break;
            case PAN:
                {
                    pan = new PanUtility();
                    panMouseListener = new PanMouseListener();
                    panMouseMotionListener = new PanMouseMotionListener();
                    panel.addMouseListener(panMouseListener);
                    panel.addMouseMotionListener(panMouseMotionListener);
                    Cursor c = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
                    panel.setCursor(c);
                    StatusDisplayer.getDefault().setStatusText("Pan enabled...");
                }
                break;
            default:
                throw new AssertionError();
        }
    }

    private class RadioButtonMouseListener implements MouseListener {

        private final double zoomFactor;

        public RadioButtonMouseListener(double zoomFactor) {
            this.zoomFactor = zoomFactor;
        }

        @FeatureEntryPoint(Features.ADJUST_IMAGE)
        @Override
        public void mousePressed(MouseEvent evt) {
            if (SwingUtilities.isLeftMouseButton(evt)) {
                ZoomUtility.zoom(panel, evt.getPoint(), zoomFactor);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    private class PanMouseMotionListener implements MouseMotionListener {

        @Override
        @FeatureEntryPoint(Features.ADJUST_IMAGE)
        public void mouseDragged(MouseEvent e) {
            pan.pan(e.getPoint(), panel);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    private class PanMouseListener implements MouseListener {

        @FeatureEntryPoint(Features.ADJUST_IMAGE)
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                pan.startPan(e.getPoint(), panel);
            }
        }

        @FeatureEntryPoint(Features.ADJUST_IMAGE)
        @Override
        public void mouseReleased(MouseEvent e) {
            pan.stopPan();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
