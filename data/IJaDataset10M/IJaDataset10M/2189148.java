package net.assimilator.examples.raytrace;

import net.assimilator.examples.raytrace.render.RenderTask;
import net.assimilator.substrates.space.SpaceWorker;
import net.assimilator.watch.StopWatch;
import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Dispatcher provides an implementation of a SpaceWorker that waits for RayTraceJob
 * requests. The Dispatcher extracts pixels from the input image and sends RenderTask
 * entries into the JavaSpace it knows about.
 * <p/>
 * <p> Once the dispatch is complete, the Dispatcher waits for more RayTraceJob requests to
 * be posted to it's queue.
 *
 * @version $Id: Dispatcher.java 92 2007-03-18 21:10:07Z khartig $
 */
public class Dispatcher implements SpaceWorker {

    private int id;

    private JavaSpace space;

    private Thread thread;

    private Image image;

    private boolean keepAlive = true;

    private final java.util.List<RayTraceJob> queue = Collections.synchronizedList(new LinkedList<RayTraceJob>());

    private StopWatch watch;

    private static Logger logger = Logger.getLogger("net.assimilator.examples.raytrace");

    public Dispatcher(JavaSpace space) {
        this.space = space;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void shutdown() throws InterruptedException {
        keepAlive = false;
        thread.interrupt();
    }

    public void startJob(RayTraceJob job, Image image) {
        this.image = image;
        synchronized (queue) {
            queue.add(job);
            queue.notifyAll();
        }
    }

    public void run() {
        thread = Thread.currentThread();
        logger.info("Started Dispatcher [" + id + "]");
        while (!thread.isInterrupted() && keepAlive) {
            try {
                if (queue.isEmpty()) {
                    try {
                        synchronized (queue) {
                            queue.wait(1000);
                        }
                    } catch (InterruptedException e) {
                        logger.warning("Interrupted wait on queue");
                    }
                } else {
                    RayTraceJob job = queue.remove(0);
                    if (logger.isLoggable(Level.FINE)) logger.fine("Got new Job [" + job.getJobID() + "] to crunch on");
                    int xchunk = job.getXChunk();
                    int ychunk = job.getYChunk();
                    int height = job.getHeight();
                    int width = job.getWidth();
                    int x;
                    int y;
                    int row;
                    int column;
                    Vector<RenderTask> taskVector = new Vector<RenderTask>();
                    for (y = 0, column = 0; y < height; y += ychunk, column++) {
                        for (x = 0, row = 0; x < width; x += xchunk, row++) {
                            RenderTask task = new RenderTask(job.getJobID(), x, y, Math.min(x + (xchunk - 1), width - 1), Math.min(y + (ychunk - 1), height - 1), row, column, job.getImageURL(), false);
                            taskVector.add(task);
                        }
                    }
                    RenderTask[] tasks = new RenderTask[taskVector.size()];
                    taskVector.copyInto(tasks);
                    Random rand = new Random();
                    for (int i = tasks.length - 1; i > 0; i--) {
                        int s = (int) (rand.nextFloat() * (i + 1));
                        if (s == i) {
                            continue;
                        }
                        RenderTask t = tasks[s];
                        tasks[s] = tasks[i];
                        tasks[i] = t;
                    }
                    int numTasks = 0;
                    long t0 = System.currentTimeMillis();
                    for (RenderTask task1 : tasks) {
                        watch.startTiming();
                        try {
                            space.write(task1, null, Lease.FOREVER);
                            numTasks++;
                        } catch (Exception e) {
                            numTasks--;
                            e.printStackTrace();
                        } finally {
                            watch.stopTiming();
                        }
                    }
                    Integer i = 0;
                    RenderTask task = new RenderTask(job.getJobID(), i, i, i, i, i, i, null, true);
                    space.write(task, null, Lease.FOREVER);
                    numTasks++;
                    if (logger.isLoggable(Level.FINE)) {
                        long t1 = System.currentTimeMillis();
                        float total = (t1 - t0) / 1000.f;
                        float avg = total / numTasks;
                        logger.fine("sent [" + numTasks + "] tasks in [" + total + "] seconds, average [" + avg + "] secs");
                    }
                }
            } catch (RemoteException re) {
                logger.log(Level.SEVERE, "Writing to JavaSpace", re);
                break;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Dispatcher Processing Thread", e);
            }
        }
        logger.info("Dispatcher [" + id + "] leaving");
    }

    int[] getPixelsFromRectangle(Rectangle rect) {
        int[] pixels = new int[rect.width * rect.height];
        try {
            PixelGrabber pg = new PixelGrabber(image, rect.x, rect.y, rect.width, rect.height, pixels, 0, rect.width);
            pg.grabPixels();
            if ((pg.status() & ImageObserver.ABORT) != 0) {
                logger.warning("Error while fetching image!");
                return (new int[0]);
            }
        } catch (Throwable t) {
            if (t.getCause() != null) t = t.getCause();
            logger.log(Level.WARNING, "Getting Pixels From the Rectangle", t);
        }
        return (pixels);
    }

    /**
     * Getter for property watch.
     *
     * @return Value of property watch.
     */
    public StopWatch getWatch() {
        return (watch);
    }

    /**
     * Setter for property watch.
     *
     * @param watch New value of property watch.
     */
    public void setWatch(StopWatch watch) {
        this.watch = watch;
    }
}
