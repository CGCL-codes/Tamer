package com.jpcomponents;

import com.jpcomponents.utils.RedimensionableDirections;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author Pablo
 */
public class JPRedimensionablePanel extends javax.swing.JPanel {

    private boolean maximum = true;

    private boolean animating = false;

    private int frameTime = 50;

    private int currentSize = 180;

    private boolean continueAnimation = false;

    private int currentAnimationTo;

    private static final int ANIMATE_TO_EXPAND = 0;

    private static final int ANIMATE_TO_COLLAPSE = 1;

    private int stepSize = 5;

    private RedimensionableDirections direction = RedimensionableDirections.EXPAND_TO_BOTTOM;

    private int minSizeToAnimation = 20;

    private int fps = 20;

    private boolean needRepaint = true;

    public JPRedimensionablePanel() {
        super();
        init();
    }

    public JPRedimensionablePanel(RedimensionableDirections direction, int minSizeToAnimation, int fps) {
        super();
        this.direction = direction;
        this.minSizeToAnimation = minSizeToAnimation;
        this.fps = fps;
        init();
    }

    public JPRedimensionablePanel(RedimensionableDirections direction, int minSizeToAnimation) {
        super();
        this.direction = direction;
        this.minSizeToAnimation = minSizeToAnimation;
        init();
    }

    public JPRedimensionablePanel(RedimensionableDirections direction) {
        super();
        this.direction = direction;
        init();
    }

    private void init() {
        this.frameTime = 60 / this.fps;
    }

    public RedimensionableDirections getDirection() {
        return direction;
    }

    public void setDirection(RedimensionableDirections direction) {
        this.direction = direction;
    }

    public void updateDimensions(Dimension d) {
        if (direction == RedimensionableDirections.EXPAND_TO_RIGHT || direction == RedimensionableDirections.EXPAND_TO_LEFT) {
            currentSize = d.width;
        } else {
            currentSize = d.height;
        }
    }

    public void updateDimensions(int w, int h) {
        Dimension d = new Dimension(w, h);
        updateDimensions(d);
    }

    public int getMinSizeToAnimation() {
        return minSizeToAnimation;
    }

    public void setMinSizeToAnimation(int minSizeToAnimation) {
        this.minSizeToAnimation = minSizeToAnimation;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public void animate() {
        if (!animating) {
            if (maximum) {
                currentAnimationTo = ANIMATE_TO_COLLAPSE;
            } else {
                currentAnimationTo = ANIMATE_TO_EXPAND;
            }
            new animationThread().start();
        }
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    private class animationThread extends Thread {

        public void run() {
            animating = true;
            needRepaint = true;
            while (animating) {
                performAnimation();
                repaint();
                try {
                    Thread.sleep(frameTime);
                } catch (InterruptedException ex) {
                }
            }
            needRepaint = false;
        }

        private void performAnimation() {
            if (direction == RedimensionableDirections.EXPAND_TO_RIGHT || direction == RedimensionableDirections.EXPAND_TO_LEFT) {
                horizontalAnimation();
            } else {
                verticalAnimation();
            }
        }

        private void horizontalAnimation() {
            if (currentAnimationTo == ANIMATE_TO_COLLAPSE) {
                if (getWidth() > minSizeToAnimation) {
                    setSize(getWidth() - stepSize, getHeight());
                    if (direction == RedimensionableDirections.EXPAND_TO_LEFT) {
                        setLocation(getLocation().x + stepSize, getLocation().y);
                    }
                } else {
                    animating = false;
                    maximum = false;
                }
            } else {
                if (getWidth() < currentSize) {
                    setSize(getWidth() + stepSize, getHeight());
                    if (direction == RedimensionableDirections.EXPAND_TO_LEFT) {
                        setLocation(getLocation().x - stepSize, getLocation().y);
                    }
                } else {
                    maximum = true;
                    animating = false;
                }
            }
        }

        private void verticalAnimation() {
            if (currentAnimationTo == ANIMATE_TO_COLLAPSE) {
                if (getHeight() > minSizeToAnimation) {
                    JPRedimensionablePanel.super.setSize(getWidth(), getHeight() - stepSize);
                    if (direction == RedimensionableDirections.EXPAND_TO_TOP) {
                        setLocation(getLocation().x, getLocation().y + stepSize);
                    }
                } else {
                    animating = false;
                    maximum = false;
                }
            } else {
                if (getHeight() < currentSize) {
                    JPRedimensionablePanel.super.setSize(getWidth(), getHeight() + stepSize);
                    if (direction == RedimensionableDirections.EXPAND_TO_TOP) {
                        setLocation(getLocation().x, getLocation().y - stepSize);
                    }
                } else {
                    maximum = true;
                    animating = false;
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 324, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 482, Short.MAX_VALUE));
    }
}
