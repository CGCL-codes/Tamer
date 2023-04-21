package jbreeze4opixels.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import jbreeze4opixels.model.LayerCombiner;

public class LayerCombinerPanel extends JPanel implements LayerCombiner {

    private static final long serialVersionUID = -1432387508670663590L;

    private List<Image> images = new ArrayList<Image>();

    private float zoom = 1f;

    public LayerCombinerPanel() {
        setDoubleBuffered(true);
        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0 && zoom < 5f) zoomIn(); else if (e.getWheelRotation() < 0 && zoom > 0.9f) zoomOut();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Image img : images) {
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            int wz = (int) (w * zoom);
            int hz = (int) (h * zoom);
            g2.drawImage(img, 0, 0, wz, hz, 0, 0, w, h, null);
        }
    }

    public void clear() {
        images.removeAll(images);
        repaint();
    }

    public void add(Image... imgs) {
        images.addAll(Arrays.asList(imgs));
        repaint();
    }

    public void setZoom(float zoom) {
        if (zoom > 5f) zoom = 5f;
        if (zoom < 0.9f) zoom = 0.9f;
        this.zoom = zoom;
        repaint();
    }

    public void zoomIn() {
        if (zoom < 5f) {
            zoom += 0.1f;
            repaint();
        }
    }

    public void zoomOut() {
        if (zoom > 0.9f) {
            zoom -= 0.1f;
            repaint();
        }
    }

    public BufferedImage exportChar() {
        BufferedImage bi = new BufferedImage(105, 210, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        for (Image img : images) g2.drawImage(img, 0, 0, 105, 210, 0, 0, 105, 210, null);
        return bi;
    }
}
