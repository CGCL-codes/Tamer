package painter.app;

import genetic.BuildException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import painter.tools.canvas.Canvas;

/**
 * This is essentially an endless queue of potential programs.
 * @author Calvin Ashmore
 */
public class PainterProgramQueue {

    private Queue<PainterProgram> queue = new ConcurrentLinkedQueue<PainterProgram>();

    private Canvas canvas;

    private boolean fetching;

    private static final int CACHE_SIZE = 10;

    public PainterProgramQueue(Canvas canvas) {
        this.canvas = canvas;
        startFetch();
    }

    public synchronized PainterProgram pop() {
        if (queue.size() < CACHE_SIZE / 2) {
            startFetch();
        }
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(PainterProgramQueue.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        PainterProgram next = queue.remove();
        if (queue.isEmpty()) {
            startFetch();
        }
        return next;
    }

    private void startFetch() {
        if (fetching) {
            return;
        }
        fetching = true;
        new Thread(new Runnable() {

            public void run() {
                doFetch();
            }
        }).start();
    }

    private void doFetch() {
        for (int i = 0; i < CACHE_SIZE; i++) {
            PainterProgram newProgram;
            try {
                newProgram = build();
            } catch (OutOfMemoryError ex) {
                Logger.getLogger(PainterProgramQueue.class.getName()).log(Level.SEVERE, null, ex);
                Runtime.getRuntime().gc();
                continue;
            }
            boolean filledQueue = false;
            if (queue.isEmpty()) {
                filledQueue = true;
            }
            queue.add(newProgram);
            if (filledQueue) {
                synchronized (this) {
                    notify();
                }
            }
        }
        fetching = false;
    }

    private PainterProgram build() {
        while (true) {
            try {
                return PainterProgram.buildNew(canvas);
            } catch (BuildException ex) {
                Logger.getLogger(PainterProgramQueue.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
