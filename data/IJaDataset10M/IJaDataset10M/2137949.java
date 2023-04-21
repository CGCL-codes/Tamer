package net.alinnistor.nk.visual.bricks;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class ImageArea extends JPanel {

    private BasicStroke bs;

    private GradientPaint gp;

    private Image image;

    private int srcx, srcy;

    private int destx, desty;

    private Rectangle rectSelection;

    public ImageArea() {
        rectSelection = new Rectangle();
        bs = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 12, 12 }, 0);
        gp = new GradientPaint(0.0f, 0.0f, Color.red, 1.0f, 1.0f, Color.white, true);
        MouseListener ml;
        ml = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (image == null) return;
                destx = srcx = e.getX();
                desty = srcy = e.getY();
                repaint();
            }
        };
        addMouseListener(ml);
        MouseMotionListener mml;
        mml = new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                if (image == null) return;
                destx = e.getX();
                desty = e.getY();
                repaint();
            }
        };
        addMouseMotionListener(mml);
    }

    public boolean crop() {
        if (srcx == destx && srcy == desty) return true;
        boolean succeeded = true;
        int x1 = (srcx < destx) ? srcx : destx;
        int y1 = (srcy < desty) ? srcy : desty;
        int x2 = (srcx > destx) ? srcx : destx;
        int y2 = (srcy > desty) ? srcy : desty;
        int width = (x2 - x1) + 1;
        int height = (y2 - y1) + 1;
        BufferedImage biCrop = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = biCrop.createGraphics();
        try {
            BufferedImage bi = (BufferedImage) image;
            BufferedImage bi2 = bi.getSubimage(x1, y1, width, height);
            g2d.drawImage(bi2, null, 0, 0);
        } catch (RasterFormatException e) {
            succeeded = false;
        }
        g2d.dispose();
        if (succeeded) setImage(biCrop); else {
            srcx = destx;
            srcy = desty;
            repaint();
        }
        return succeeded;
    }

    public Image getImage() {
        return image;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) g.drawImage(image, 0, 0, this);
        if (srcx != destx || srcy != desty) {
            int x1 = (srcx < destx) ? srcx : destx;
            int y1 = (srcy < desty) ? srcy : desty;
            int x2 = (srcx > destx) ? srcx : destx;
            int y2 = (srcy > desty) ? srcy : desty;
            rectSelection.x = x1;
            rectSelection.y = y1;
            rectSelection.width = (x2 - x1) + 1;
            rectSelection.height = (y2 - y1) + 1;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(bs);
            g2d.setPaint(gp);
            g2d.draw(rectSelection);
        }
    }

    public void setImage(Image image) {
        this.image = image;
        setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        revalidate();
        srcx = destx;
        srcy = desty;
        repaint();
    }
}
