package org.tex4java.presenter;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import org.tex4java.Manager;
import org.tex4java.tex.boxworld.*;
import org.tex4java.tex.environment.*;

/**
 * A panel to display the boxworld.
 *
 * This program and the accompanying materials are made available under
 * the terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * @author <a href="mailto:paladin@cs.tu-berlin.de">Peter Annuss </a>
 * @author <a href="mailto:thomas@dohmke.de">Thomas Dohmke </a>
 * @version $Revision: 1.1.1.1 $
 */
public class BoxPanel extends JPanel implements Runnable {

    protected Manager manager;

    protected Environment env;

    double x0 = 0;

    double y0 = 0;

    double x = 0;

    double y = 0;

    int page = 0;

    int maxPages = 0;

    double zoom = 1;

    boolean zoomToWindow = false;

    boolean showBoxes = false;

    boolean hasFocus = true;

    boolean forward = true;

    boolean animateStarted = false;

    int animateStart = 0;

    int animateStop = 0;

    int animateCurrent = 0;

    LinkedList animated = null;

    Thread thread;

    public BufferedImage slide = null;

    Rectangle slideRec = null;

    volatile boolean synchronize;

    String temp = "temp";

    public BoxPanel(Manager manager) {
        this.manager = manager;
        this.env = manager.getEnvironment();
        synchronize = false;
        this.setBackground(Color.white);
        resetAnimate();
    }

    public BoxPanel(BoxPanel boxPanel) {
        synchronize = false;
        this.setBackground(Color.white);
        set(boxPanel);
    }

    public void set(BoxPanel boxPanel) {
        manager = boxPanel.manager;
        env = boxPanel.env;
        boxPanel.hasFocus = false;
        boxPanel.stopAnimation();
        resetAnimate();
        page = boxPanel.page;
        maxPages = boxPanel.maxPages;
        zoom = boxPanel.zoom;
        zoomToWindow = boxPanel.zoomToWindow;
        showBoxes = boxPanel.showBoxes;
        hasFocus = true;
        slideRec = null;
    }

    public void stopAnimation() {
        if ((thread != null) && (!thread.isInterrupted())) {
            Thread temp = thread;
            thread = new Thread(this);
            temp.interrupt();
            finish();
        }
    }

    public void resetAnimate() {
        animateStarted = false;
        animateStart = 65536;
        animateCurrent = 0;
        animateStop = 0;
        animated = new LinkedList();
        thread = new Thread(this);
        thread.start();
    }

    public void countPages() {
        int pageCount = 0;
        org.tex4java.tex.boxworld.Box topBox = env.box;
        for (org.tex4java.tex.boxworld.Box vbox = topBox.firstChild; vbox != null; vbox = vbox.nextSibling) {
            pageCount++;
        }
        this.maxPages = pageCount - 1;
    }

    public void nextPage() {
        stopAnimation();
        forward = true;
        if (animateCurrent < animateStop) {
            animateCurrent++;
            slideRec = null;
            repaint();
            return;
        }
        if (page < maxPages) {
            resetAnimate();
            page++;
            slideRec = null;
            slideRec = null;
            repaint();
        }
    }

    public void prevPage() {
        stopAnimation();
        forward = false;
        if (animateCurrent > animateStart) {
            animateCurrent--;
            slideRec = null;
            repaint();
            return;
        }
        if (page > 0) {
            resetAnimate();
            page--;
            slideRec = null;
            repaint();
        }
    }

    public void goToPage(int page) {
        stopAnimation();
        resetAnimate();
        forward = false;
        slideRec = null;
        this.page = page;
        this.repaint();
    }

    public void zoomIn() {
        zoomToWindow = false;
        zoom *= 2;
        slideRec = null;
        repaint();
    }

    public void zoomOut() {
        zoomToWindow = false;
        zoom /= 2;
        slideRec = null;
        repaint();
    }

    public void zoomToWindow() {
        zoomToWindow = !zoomToWindow;
        slideRec = null;
        repaint();
    }

    public void myRepaint() {
        synchronize = false;
        repaint();
    }

    public void paintSlide() {
        org.tex4java.tex.boxworld.Box topBox = env.box;
        int pageCount = 0;
        for (org.tex4java.tex.boxworld.Box vbox = topBox.firstChild; vbox != null; vbox = vbox.nextSibling) {
            if (pageCount == page) {
                if (vbox instanceof VBox) {
                    x = ((VBox) vbox).hoffset;
                    y = vbox.height + ((VBox) vbox).voffset;
                    Rectangle newRec = getBounds();
                    if ((slideRec == null) || (newRec.width != slideRec.width) || (newRec.height != slideRec.height)) {
                        slideRec = newRec;
                        slide = (BufferedImage) this.createImage(slideRec.width, slideRec.height);
                    }
                    Graphics2D g2 = slide.createGraphics();
                    g2.setBackground(Color.white);
                    g2.clearRect(0, 0, slideRec.width, slideRec.height);
                    if (zoomToWindow) {
                        if (slideRec.height / (vbox.height + 2 * ((VBox) vbox).voffset) < slideRec.width / (vbox.width + 2 * ((VBox) vbox).hoffset)) {
                            zoom = slideRec.height / (vbox.height + 2 * ((VBox) vbox).voffset);
                        } else {
                            zoom = slideRec.width / (vbox.width + 2 * ((VBox) vbox).hoffset);
                        }
                    }
                    g2.scale(zoom, zoom);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.black);
                    if (!animateStarted) {
                        animateStart = 65536;
                        if (forward) {
                            animateCurrent = 0;
                        } else {
                            animateCurrent = 65536;
                        }
                        animateStop = 0;
                    }
                    paintBox(g2, vbox);
                    if (!animateStarted) {
                        if (forward) {
                            animateCurrent = animateStart;
                        } else {
                            animateCurrent = animateStop;
                        }
                        animateStarted = true;
                    }
                    break;
                }
            }
            pageCount++;
        }
    }

    public void paintAnimated(Graphics2D g2) {
        if (animated == null) {
            return;
        }
        g2.scale(zoom, zoom);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Iterator it = animated.iterator();
        while (it.hasNext()) {
            Animation animation = (Animation) it.next();
            x = animation.x0 + animation.x;
            y = animation.y0 + animation.y;
            paintBox(g2, animation.box, false, true);
        }
    }

    public void paintComponent(Graphics g) {
        if (!hasFocus) {
            return;
        }
        Rectangle newRec = getBounds();
        if ((slideRec == null) || (newRec.width != slideRec.width) || (newRec.height != slideRec.height)) {
            paintSlide();
        }
        if (slide != null) {
            Graphics2D g2 = (Graphics2D) g;
            g.drawImage(slide, 0, 0, this);
            if ((thread != null) && thread.isAlive() && !thread.isInterrupted()) {
                paintAnimated(g2);
            }
            if (!thread.isAlive()) {
                thread.start();
            }
        }
        if (!synchronize) {
            synchronize = true;
            synchronized (temp) {
                temp.notify();
            }
        }
    }

    public void paintBox(Graphics2D g2, org.tex4java.tex.boxworld.Box box) {
        paintBox(g2, box, false, false);
    }

    public void paintBox(Graphics2D g2, org.tex4java.tex.boxworld.Box box, boolean dontDraw, boolean animate) {
        double XOld = x;
        double YOld = y;
        boolean dontDrawChildren = false;
        boolean show = false;
        if (!animate) {
            for (Iterator it = box.animation.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                int number = ((Integer) entry.getKey()).intValue();
                Animation animation = (Animation) entry.getValue();
                if ((number < animateStart) && (!animateStarted)) {
                    animateStart = number;
                }
                if ((number + 1 > animateStop) && (!animateStarted)) {
                    animateStop = number + 1;
                }
                if (animation instanceof AnimationShow) {
                    if (animateCurrent <= number) {
                        if (!show) {
                            dontDrawChildren = true;
                        }
                    } else {
                        dontDrawChildren = false;
                        show = true;
                    }
                }
                if (animation instanceof AnimationMove) {
                    if (animateCurrent <= number) {
                        if (!show) {
                            dontDrawChildren = true;
                        }
                    } else {
                        dontDrawChildren = false;
                        show = true;
                    }
                    if ((animateCurrent == number + 1) && (forward)) {
                        if (!animation.started) {
                            animation.start(x, y);
                            animated.add(animation);
                        }
                        dontDrawChildren = true;
                    }
                }
                if (animation instanceof AnimationHide) {
                    if (animateCurrent > number) {
                        dontDrawChildren = true;
                    }
                }
            }
        }
        if ((!dontDraw) && (!dontDrawChildren)) {
            if (showBoxes) {
                Rectangle2D boxRec = new Rectangle2D.Double(x + x0, y + y0 - box.height, box.width, box.depth + box.height);
                g2.draw(boxRec);
            }
            box.draw(g2, this, x + x0, y + y0);
        }
        if (box instanceof HBox) {
            for (org.tex4java.tex.boxworld.Box child = box.firstChild; child != null; child = child.nextSibling) {
                if (child instanceof Glue) {
                    x += ((Glue) child).size;
                } else {
                    if (child instanceof Kern) {
                        x += ((Kern) child).size;
                    } else {
                        y -= child.raise;
                        paintBox(g2, child, dontDrawChildren || dontDraw, false);
                        x += child.width;
                        y += child.raise;
                    }
                }
            }
            x = XOld;
            y = YOld;
            return;
        }
        if (box instanceof VBox) {
            y -= box.height;
            for (org.tex4java.tex.boxworld.Box child = box.firstChild; child != null; child = child.nextSibling) {
                if (child instanceof Glue) {
                    y += ((Glue) child).size;
                } else {
                    if (child instanceof Kern) {
                        y += ((Kern) child).size;
                    } else {
                        x -= child.move;
                        y += child.height - child.raise;
                        paintBox(g2, child, dontDrawChildren || dontDraw, false);
                        y += child.depth + child.raise;
                        x += child.move;
                    }
                }
            }
            x = XOld;
            y = YOld;
            return;
        }
    }

    public void run() {
        try {
            boolean repaint = false;
            while (true) {
                if (animated != null) {
                    Iterator it = animated.iterator();
                    while (it.hasNext()) {
                        repaint |= ((Animation) it.next()).run();
                    }
                    if (repaint) {
                        repaint = false;
                        myRepaint();
                        if (!synchronize) {
                            synchronized (temp) {
                                while (!synchronize) {
                                    temp.wait();
                                }
                            }
                        }
                    }
                    Thread.sleep(1);
                } else {
                    Thread.yield();
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    public void finish() {
        if (animated == null) {
            return;
        }
        Iterator it = animated.iterator();
        while (it.hasNext()) {
            ((Animation) it.next()).stop();
        }
        animated = new LinkedList();
    }
}
