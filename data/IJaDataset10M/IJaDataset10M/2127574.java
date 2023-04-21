package org.proteomecommons.MSExpedite.Graph;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class Scroller extends AbstractGraphController {

    public static final int NONE = -1;

    public static final int SCROLL_LEFT = 0;

    public static final int SCROLL_RIGHT = 1;

    public static final int PLAY = 2;

    Graph renderer = null;

    long refreshRate = 100L;

    double stepSize = 10;

    double deltaMass = 10.0;

    TimerThread timer;

    boolean bPauseTimer = false;

    int scrollDirection = SCROLL_RIGHT;

    boolean respondToSetXRangeEvent = true;

    boolean isScrollerOn = false;

    public Scroller() {
    }

    public Scroller(final Graph rc) {
        setRenderer(rc);
    }

    public Scroller(final Graph rc, final long rate) {
        this(rc);
        setRefreshRate(rate);
    }

    public Scroller(final Graph rc, final long rate, final double masssRes) {
        this(rc, rate);
        setDeltaMass(masssRes);
    }

    public Scroller(final Graph rc, final double massRes) {
        setRenderer(rc);
        setDeltaMass(massRes);
    }

    public void setDeltaMass(final double dm) {
        deltaMass = dm;
    }

    public void setScrollDirection(int direction) {
        scrollDirection = direction;
    }

    public double getDeltaMass() {
        return deltaMass;
    }

    public final void setRenderer(final Graph rc) {
        renderer = rc;
        listenForEvents();
    }

    void listenForEvents() {
        renderer.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                String propName = (String) e.getPropertyName();
                if (propName.equals(Graph.EVENT_COMPONENT_RESIZED)) {
                    if (!isPaused()) pause(true);
                } else if (propName.equals(Graph.EVENT_X_RANGE_SET)) {
                    if (!respondToSetXRangeEvent) {
                        respondToSetXRangeEvent = true;
                        return;
                    }
                    Range r = (Range) e.getNewValue();
                    long di = r.end - r.start;
                    double dm = renderer.getDataPoints().x[r.end] - renderer.getDataPoints().x[r.start];
                    setDeltaMass(dm);
                }
            }
        });
    }

    public void pause() {
        pause(true);
    }

    public final void setRefreshRate(final long rate) {
        refreshRate = rate;
    }

    public final synchronized void setStepSize(final double mr) {
        stepSize = mr;
    }

    public final synchronized boolean isPaused() {
        return isTimerPaused();
    }

    public final synchronized boolean isTimerPaused() {
        return bPauseTimer;
    }

    public final synchronized void pause(boolean bPause) {
        bPauseTimer = bPause;
    }

    public final void start() {
        if (timer == null) {
            timer = new TimerThread();
            timer.start();
        }
        pause(false);
    }

    public final void scrollLeft() {
        Array2D dp = renderer.getDataPoints();
        GraphicsState gs = renderer.getGraphicsState();
        int start = gs.getStartIndex();
        int end = gs.getEndIndex();
        if (dp == null || dp.length() == 0 || end >= dp.length() - 1) {
            pause(true);
            return;
        }
        final double m1 = renderer.getDataPoints().x[start];
        double m2 = m1;
        for (int i = start - 1; i > 0; i--) {
            m2 = renderer.getDataPoints().x[i];
            if ((m1 - m2) > stepSize) {
                start = i;
                break;
            }
        }
        if (start < 0) return;
        int w = renderer.getGraphDimension().width;
        double fLastX = dp.x[start];
        int lastIndex = -1;
        double tmpD;
        for (int i = start; i < dp.length(); i++) {
            tmpD = renderer.getScreenXPosition(dp.x[i]);
            if (Math.abs(fLastX - dp.x[i]) >= deltaMass) {
                fLastX = dp.x[i];
                lastIndex = i;
                break;
            }
        }
        if (lastIndex >= dp.length()) return;
        if (lastIndex <= 0) {
            pause(true);
            return;
        }
        setXRange(start, lastIndex);
        renderer.reconstructImage();
    }

    private void setXRange(int start, int end) {
        Array2D dp = renderer.getDataPoints();
        float xMin = dp.x[start];
        float xMax = dp.x[end];
        renderer.setXRange(xMin, xMax);
        renderer.setXRange(new Range(start, end));
        respondToSetXRangeEvent = false;
    }

    public synchronized void play() {
        this.setScrollDirection(SCROLL_RIGHT);
        start();
    }

    public synchronized void scroll() {
        switch(scrollDirection) {
            case SCROLL_RIGHT:
                scrollRight();
                break;
            case SCROLL_LEFT:
                scrollLeft();
                break;
            default:
                break;
        }
    }

    public synchronized void stepRight() {
        pause(true);
        scrollRight();
    }

    public synchronized void stepLeft() {
        pause(true);
        scrollLeft();
    }

    public synchronized void scrollRight() {
        Array2D dp = renderer.getDataPoints();
        GraphicsState gs = renderer.getGraphicsState();
        int start = gs.startIndex;
        int end = gs.endIndex;
        if (dp == null || dp.length() == 0 || end >= dp.length() - 1) {
            pause(true);
            return;
        }
        int startIndex = start;
        final double m1 = dp.x[startIndex];
        double m2 = m1;
        for (int i = startIndex + 1; i < dp.length(); i++) {
            m2 = dp.x[i];
            if ((m2 - m1) > stepSize) {
                startIndex = i;
                break;
            }
        }
        if (startIndex >= dp.length() - 1) return;
        int w = renderer.getGraphDimension().width;
        double fLastX = dp.x[startIndex];
        int lastIndex = -1;
        double tmpD = 0;
        for (int i = startIndex; i < dp.length(); i++) {
            if (Math.abs(fLastX - dp.x[i]) >= deltaMass) {
                fLastX = dp.x[i];
                lastIndex = i;
                break;
            }
        }
        if (lastIndex >= dp.length()) return;
        if (lastIndex <= 0) {
            pause(true);
            return;
        }
        setXRange(startIndex, lastIndex);
        renderer.reconstructImage();
    }

    public int getScrollDirection() {
        return this.scrollDirection;
    }

    public void enable(boolean b) {
        if (b) scroll(); else pause(true);
    }

    class TimerThread extends Thread {

        public TimerThread() {
        }

        public void run() {
            while (true) {
                try {
                    if (!isTimerPaused()) {
                        scroll();
                        sleep(refreshRate);
                    } else {
                        sleep(500);
                    }
                } catch (InterruptedException ex) {
                    pause(true);
                }
            }
        }
    }
}
