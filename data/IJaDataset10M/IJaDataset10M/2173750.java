package org.timepedia.chronoscope.java2d.swing;

import org.timepedia.chronoscope.client.canvas.ViewReadyCallback;
import org.timepedia.chronoscope.client.gss.GssContext;
import org.timepedia.chronoscope.client.util.PortableTimer;
import org.timepedia.chronoscope.client.util.PortableTimerTask;
import org.timepedia.chronoscope.java2d.canvas.ViewJava2D;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

/**
 * View that enables animation
 */
public class InteractiveViewJava2D extends ViewJava2D {

    abstract static class NormalTimerTask extends TimerTask implements PortableTimer {

        private static Timer timer = new Timer();

        boolean canceled;

        public NormalTimerTask() {
        }

        public void cancelTimer() {
            if (canceled) {
                return;
            }
            this.cancel();
            canceled = true;
        }

        public void schedule(int delayMillis) {
            canceled = false;
            timer.schedule(new TimerTask() {

                public void run() {
                    NormalTimerTask.this.run();
                }
            }, delayMillis);
        }

        public void scheduleRepeating(int periodMillis) {
            timer.scheduleAtFixedRate(this, (long) periodMillis, (long) periodMillis);
        }
    }

    private RedrawListener redrawListener;

    public PortableTimer createTimer(final PortableTimerTask run) {
        return new NormalTimerTask() {

            public double getTime() {
                return System.currentTimeMillis();
            }

            public void run() {
                try {
                    final PortableTimer timer = this;
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            if (!canceled) {
                                run.run(timer);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void flipCanvas() {
        super.flipCanvas();
        redrawListener.onRedraw();
    }

    public void initialize(final int width, final int height, boolean doubleBuffered, final GssContext gssContext, final ViewReadyCallback callback, RedrawListener redrawListener) {
        super.initialize(width, height, doubleBuffered, gssContext, callback);
        this.redrawListener = redrawListener;
    }
}
