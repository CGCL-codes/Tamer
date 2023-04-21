package shu.jai.jaistuff.display;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * This class represents a JPanel which contains two scrollable instances of
 * DisplayJAIWithPixelInfo.
 * The scrolling bars of both images are synchronized so scrolling one image
 * will automatically scroll the other.
 */
public class DisplayTwoSynchronizedImages extends JPanel implements AdjustmentListener {

    /** The DisplayJAIWithPixelInfo for the first image. */
    protected DisplayJAIWithPixelInfo dj1;

    /** The DisplayJAIWithPixelInfo for the second image. */
    protected DisplayJAIWithPixelInfo dj2;

    /** The JScrollPane which will contain the first of the images */
    protected JScrollPane jsp1;

    /** The JScrollPane which will contain the second of the images */
    protected JScrollPane jsp2;

    /**
   * Creates an instance of this class, setting the components' layout,
   * creating two instances of DisplayJAIWithPixelInfo for the two images and
   * creating/registering event handlers for the scroll bars.
   * @param im1 the first image (left side)
   * @param im2 the second image (right side)
   */
    public DisplayTwoSynchronizedImages(RenderedImage im1, RenderedImage im2) {
        super();
        setLayout(new GridLayout(1, 2));
        dj1 = new DisplayJAIWithPixelInfo(im1);
        dj2 = new DisplayJAIWithPixelInfo(im2);
        jsp1 = new JScrollPane(dj1);
        jsp2 = new JScrollPane(dj2);
        add(jsp1);
        add(jsp2);
        jsp1.getHorizontalScrollBar().addAdjustmentListener(this);
        jsp1.getVerticalScrollBar().addAdjustmentListener(this);
        jsp2.getHorizontalScrollBar().addAdjustmentListener(this);
        jsp2.getVerticalScrollBar().addAdjustmentListener(this);
    }

    /**
   * This method changes the first image to be displayed.
   * @param newImage the new first image.
   */
    public void setImage1(RenderedImage newimage) {
        dj1.set(newimage);
        repaint();
    }

    /**
   * This method changes the second image to be displayed.
   * @param newImage the new second image.
   */
    public void setImage2(RenderedImage newimage) {
        dj2.set(newimage);
        repaint();
    }

    /**
   * This method returns the first image.
   * @return the first image.
   */
    public RenderedImage getImage1() {
        return dj1.getSource();
    }

    /**
   * This method returns the second image.
   * @return the second image.
   */
    public RenderedImage getImage2() {
        return dj2.getSource();
    }

    /**
   * This method returns the first DisplayJAIWithPixelInfo component.
   * @return the first DisplayJAIWithPixelInfo component.
   */
    public DisplayJAIWithPixelInfo getDisplayJAIComponent1() {
        return dj1;
    }

    /**
   * This method returns the second DisplayJAIWithPixelInfo component.
   * @return the second DisplayJAIWithPixelInfo component.
   */
    public DisplayJAIWithPixelInfo getDisplayJAIComponent2() {
        return dj2;
    }

    /**
   * This method will be called when any of the scroll bars of the instances of
   * DisplayJAIWithPixelInfo are changed. The method will adjust the scroll bar of the
   * other DisplayJAIWithPixelInfo as needed.
   * @param e the AdjustmentEvent that ocurred (meaning that one of the scroll
   *        bars position has changed.
   */
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getSource() == jsp1.getHorizontalScrollBar()) {
            jsp2.getHorizontalScrollBar().setValue(e.getValue());
        }
        if (e.getSource() == jsp1.getVerticalScrollBar()) {
            jsp2.getVerticalScrollBar().setValue(e.getValue());
        }
        if (e.getSource() == jsp2.getHorizontalScrollBar()) {
            jsp1.getHorizontalScrollBar().setValue(e.getValue());
        }
        if (e.getSource() == jsp2.getVerticalScrollBar()) {
            jsp1.getVerticalScrollBar().setValue(e.getValue());
        }
    }
}
