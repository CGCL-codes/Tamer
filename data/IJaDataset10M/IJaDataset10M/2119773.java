package org.openwar.victory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The central class of the Victory engine, the continuously running game loop.
 * @author Bart van Heukelom
 */
public class GameLoop implements Runnable {

    private static final int DEFAULT_SPS = 30;

    private static final int SPS_MEASURE_INTERVAL = 250;

    private static GameLoop instance;

    private int sps = DEFAULT_SPS;

    private double realSps = 0;

    private int step = 0;

    private long pause = 0;

    private volatile boolean run = false;

    private final Object stepLock = new Object();

    private final List<GameLoopHook> hooks = new ArrayList<GameLoopHook>();

    private GameLoop() {
    }

    /**
     * @return The single game instance. Only available after calling start(), before
     * that it will return <code>null</code>
     */
    public static GameLoop get() {
        if (instance == null) {
            instance = new GameLoop();
        }
        return instance;
    }

    /**
     * Start the game. This requires a Display to have been set previously.
     */
    public void start() {
        if (run) {
            throw new GameRuntimeException("Game loop already started");
        }
        run = true;
        final Thread gameThread = new Thread(this);
        gameThread.setName("Main game thread");
        gameThread.start();
        new SPSThread().start();
    }

    /**
     * Stop the game nicely.
     */
    public void stop() {
        run = false;
    }

    /**
     * The main loop of the game engine.
     */
    public void run() {
        long startTime;
        while (run) {
            startTime = System.nanoTime();
            for (GameLoopHook hook : hooks) {
                hook.beforeStep(step);
            }
            synchronized (stepLock) {
                for (GameLoopHook hook : hooks) {
                    hook.stepBegin(step);
                }
                EntityList.get().sort();
                Entity e;
                final Iterator<Entity> it = EntityList.get().iterator();
                while (it.hasNext()) {
                    it.next().step(step);
                }
                for (GameLoopHook hook : hooks) {
                    hook.stepEnd(step);
                }
                step++;
            }
            for (GameLoopHook hook : hooks) {
                hook.afterStep(step);
            }
            sleepFill(startTime);
        }
    }

    /**
     * @param startTime
     */
    private void sleepFill(final long startTime) {
        if (sps > 0) {
            try {
                final long timePassed = ((System.nanoTime() - startTime) / 1000000);
                Thread.sleep(Math.max(pause + (1000 / sps) - timePassed, 0));
                if (pause > 0) {
                    pause = 0;
                }
            } catch (final InterruptedException ex) {
                Thread.yield();
            }
        } else {
            Thread.yield();
        }
    }

    /**
     * Get the speed at which the game should be running.
     * 
     * @return The speed at which the game should be running in
     * <i>frames per second</i>
     */
    public int getSps() {
        return sps;
    }

    /**
     * @return The speed at which the game is actually running in <i>steps-per-second</i>.
     */
    public double getRealSps() {
        return realSps;
    }

    /**
     * Set the speed at which the game is running. Set to a value of 0 to not
     * limit the framerate.
     * @param someSps The speed at which the game is running in <i>steps-per-second</i>.
     */
    public void setSps(final int someSps) {
        sps = someSps;
    }

    /**
     * When this step ends, wait for this long before continuing to the next.
     * This pause will only be applied once. Note that this is a rather crude
     * way of pausing the game; there is no way to interrupt it and other activities
     * like input polling will be paused as well.
     * @param time The time (in ms) to pause.
     */
    public void pause(final long time) {
        pause += time;
    }

    /**
     * Get the current step in the game, measured from the start.
     * 
     * @return The current step in the game
     */
    public int getStep() {
        return step;
    }

    /**
     * @return The step lock object.
     */
    public Object getStepLock() {
        return stepLock;
    }

    public void insertHook(final GameLoopHook hook) {
        hooks.add(hook);
    }

    public void removeHook(final GameLoopHook hook) {
        hooks.remove(hook);
    }

    /**
     * A thread to measure the actual game speed.
     * 
     * @author Bart van Heukelom
     */
    private class SPSThread extends Thread {

        public void start() {
            this.setName("SPS Measure Thread");
            this.setDaemon(true);
            super.start();
        }

        @Override
        public void run() {
            int lastframe = 0;
            long lastTime = 0;
            double deltaTime = 0;
            int deltaFrame = 0;
            while (run) {
                deltaTime = (System.nanoTime() - lastTime) / 1000000000.0;
                lastTime = System.nanoTime();
                deltaFrame = step - lastframe;
                lastframe = step;
                realSps = deltaFrame / deltaTime;
                try {
                    Thread.sleep(SPS_MEASURE_INTERVAL);
                } catch (final InterruptedException ex) {
                    Thread.yield();
                }
            }
        }
    }
}
