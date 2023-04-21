package wekinator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.*;

/**
 *
 * @author rebecca
 */
public class PlayalongScore {

    protected List<double[]> paramLists = null;

    protected List<Double> secondLists = null;

    protected List<Boolean> smoothLists = null;

    protected int smoothMs = 25;

    protected static boolean isPlaying = false;

    protected static transient Thread myPlayerThread = null;

    protected ScorePlayer myScorePlayer = new ScorePlayer();

    protected static final Object lock1 = new Object();

    int numParams = 0;

    protected int playingRow = 0;

    public static final String PROP_PLAYINGROW = "playingRow";

    public static final String PROP_ISPLAYING = "isPlaying";

    protected EventListenerList listenerList = new EventListenerList();

    protected transient PlayalongScoreViewer myViewer = null;

    public void view() {
        if (myViewer == null) {
            myViewer = new PlayalongScoreViewer(this);
            myViewer.setVisible(true);
        }
        myViewer.setVisible(true);
        myViewer.toFront();
    }

    /**
     * Get the value of playingRow
     *
     * @return the value of playingRow
     */
    public boolean getIsPlaying() {
        return isPlaying;
    }

    /**
     * Get the value of playingRow
     *
     * @return the value of playingRow
     */
    public int getPlayingRow() {
        return playingRow;
    }

    /**
     * Set the value of playingRow
     *
     * @param playingRow new value of playingRow
     */
    protected void setPlayingRow(int playingRow) {
        int oldPlayingRow = this.playingRow;
        this.playingRow = playingRow;
        propertyChangeSupport.firePropertyChange(PROP_PLAYINGROW, oldPlayingRow, playingRow);
    }

    protected synchronized void setIsPlaying(boolean isPlaying) {
        boolean oldIsPlaying = PlayalongScore.isPlaying;
        PlayalongScore.isPlaying = isPlaying;
        propertyChangeSupport.firePropertyChange(PROP_ISPLAYING, oldIsPlaying, isPlaying);
    }

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private ChangeEvent changeEvent = null;

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    PlayalongScore(int numParams) {
        paramLists = new LinkedList<double[]>();
        secondLists = new LinkedList<Double>();
        smoothLists = new LinkedList<Boolean>();
        this.numParams = numParams;
    }

    public synchronized void play() {
        if (!isPlaying) {
            myScorePlayer.play();
        }
        setIsPlaying(true);
    }

    public synchronized void stop() {
        if (isPlaying) {
            myScorePlayer.stop();
            setIsPlaying(false);
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void addParams(double[] p, double time) {
        synchronized (lock1) {
            if (p != null && p.length == numParams && time > 0) {
                double[] newp = new double[p.length];
                for (int i = 0; i < p.length; i++) {
                    newp[i] = p[i];
                }
                paramLists.add(newp);
                secondLists.add(time);
                smoothLists.add(false);
                fireStateChanged();
            }
        }
    }

    public double[] getParamsAt(int index) {
        return paramLists.get(index);
    }

    public boolean getSmoothAt(int index) {
        return smoothLists.get(index);
    }

    public int getNumParams() {
        return numParams;
    }

    public double getSecondsAt(int index) {
        return secondLists.get(index);
    }

    public int getScoreLength() {
        return paramLists.size();
    }

    public void setParamsAt(int index, double[] params) {
        if (params != null && params.length == numParams && index >= 0 && index < paramLists.size()) {
            paramLists.set(index, params);
            fireStateChanged();
        }
    }

    public void setSecondsAt(int index, double seconds) {
        if (index >= 0 && index < paramLists.size()) {
            secondLists.set(index, seconds);
            fireStateChanged();
        }
    }

    public void setSmoothAt(int index, boolean smooth) {
        if (index >= 0 && index < paramLists.size()) {
            smoothLists.set(index, smooth);
            fireStateChanged();
        }
    }

    public void removeAt(int index) {
        synchronized (lock1) {
            paramLists.remove(index);
            secondLists.remove(index);
            smoothLists.remove(index);
            fireStateChanged();
        }
    }

    public void setParamsAt(int index, int paramNum, double val) {
        double[] ps = paramLists.get(index);
        if (ps != null && ps.length > paramNum && paramNum >= 0) {
            ps[paramNum] = val;
            fireStateChanged();
        }
    }

    private class ScorePlayer {

        protected int nextRow = 0;

        protected double[] currentParams = new double[0];

        protected boolean smooth = false;

        protected double waitSec = 0;

        protected double[] increments = new double[0];

        protected double[] currentSmoothingValues = new double[0];

        protected int numIncrements = 0;

        protected int thisIncrement = 0;

        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        ScheduledFuture<?> advancerHandler = null;

        ScheduledFuture<?> smootherHandler = null;

        final Runnable parameterAdvancer = new Runnable() {

            public void run() {
                advanceParameters();
            }
        };

        final Runnable parameterSmoother = new Runnable() {

            public void run() {
                smoothParameters();
            }
        };

        public void play() {
            advanceParameters();
        }

        public void stop() {
            if (advancerHandler != null) {
                advancerHandler.cancel(true);
                System.out.println("Advancer cancelled");
            }
            if (smootherHandler != null) {
                smootherHandler.cancel(true);
                System.out.println("Smoother cancelled");
            }
        }

        protected void advanceParameters() {
            synchronized (lock1) {
                if (paramLists.size() == 0) {
                    return;
                }
                if (nextRow >= paramLists.size()) {
                    nextRow = 0;
                }
                System.out.println("Advancing parameters: " + nextRow);
                if (currentSmoothingValues.length != currentParams.length) {
                    currentSmoothingValues = new double[currentParams.length];
                }
                System.arraycopy(currentParams, 0, currentSmoothingValues, 0, currentParams.length);
                currentParams = paramLists.get(nextRow);
                smooth = smoothLists.get(nextRow);
                waitSec = secondLists.get(nextRow);
                setPlayingRow(nextRow);
                nextRow++;
                advancerHandler = scheduler.schedule(parameterAdvancer, (long) waitSec, TimeUnit.SECONDS);
                if (smooth && currentSmoothingValues == null || currentSmoothingValues.length != currentParams.length) {
                    smooth = false;
                    increments = new double[currentParams.length];
                }
                if (!smooth) {
                    sendUpdateMessage(currentParams);
                    if (smootherHandler != null) {
                        smootherHandler.cancel(true);
                    }
                } else {
                    if (smootherHandler != null) {
                        smootherHandler.cancel(true);
                    }
                    for (int i = 0; i < currentSmoothingValues.length; i++) {
                        numIncrements = (int) waitSec * 1000 / smoothMs;
                        thisIncrement = 1;
                        increments[i] = (currentParams[i] - currentSmoothingValues[i]) / numIncrements;
                    }
                    sendUpdateMessage(currentSmoothingValues);
                    smootherHandler = scheduler.scheduleAtFixedRate(parameterSmoother, smoothMs, smoothMs, TimeUnit.MILLISECONDS);
                }
            }
        }

        protected void sendUpdateMessage(double[] vals) {
            double[] tmp = new double[vals.length];
            System.arraycopy(vals, 0, tmp, 0, vals.length);
            WekinatorLearningManager.getInstance().setParams(tmp);
            OscHandler.getOscHandler().packageDistAndSendParamsToSynth(vals);
        }

        protected void smoothParameters() {
            if (thisIncrement < numIncrements) {
                for (int i = 0; i < currentSmoothingValues.length; i++) {
                    currentSmoothingValues[i] += increments[i];
                }
                sendUpdateMessage(currentSmoothingValues);
                thisIncrement++;
            }
        }
    }

    public static void main(String[] args) {
        try {
            PlayalongScore ps = new PlayalongScore(3);
            double[] d = { 1.0, 2.0, 3.0 };
            ps.addParams(d, 1.0);
            double[] d2 = { 4.0, 5.0, 6.0 };
            ps.addParams(d2, 1.0);
            System.out.println("Playing");
            ps.play();
            Thread.sleep(2999);
            System.out.println("removing");
            ps.removeAt(1);
            Thread.sleep(1001);
            ps.removeAt(0);
            Thread.sleep(10000);
            System.out.println("Stopping");
            ps.stop();
            System.out.println("Stopped");
        } catch (InterruptedException ex) {
            Logger.getLogger(PlayalongScore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
