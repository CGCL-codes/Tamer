package jblip.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import javax.swing.JPanel;
import jblip.gui.data.images.ImageChangeListener;

public class ImageChangePanel extends JPanel implements ImageChangeListener {

    private static final long serialVersionUID = 1L;

    private Image current_image;

    public ImageChangePanel(final Image initial) {
        setOpaque(false);
        final MediaTracker mtr = new MediaTracker(this);
        mtr.addImage(initial, 0);
        try {
            mtr.waitForID(0);
        } catch (InterruptedException e) {
        }
        imageChange(initial);
    }

    @Override
    public Color getBackground() {
        return Color.WHITE;
    }

    @Override
    public Color getForeground() {
        return Color.WHITE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(current_image, 0, 0, this);
    }

    @Override
    public void imageChange(Image new_image) {
        this.current_image = new_image;
        final int FLAGS_WH = WIDTH | HEIGHT;
        if ((checkImage(new_image, this) & FLAGS_WH) == FLAGS_WH) {
            final Dimension size = new Dimension(new_image.getWidth(this), new_image.getHeight(this));
            changeSize(size);
        } else {
            prepareImage(new_image, this);
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        final int FLAGS_WH = WIDTH | HEIGHT;
        if ((infoflags & FLAGS_WH) == FLAGS_WH) {
            final Dimension size = new Dimension(img.getWidth(this), img.getHeight(this));
            changeSize(size);
        }
        return super.imageUpdate(img, infoflags, x, y, w, h);
    }

    private void changeSize(Dimension size) {
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
        setSize(size);
        invalidate();
        repaint();
    }
}
