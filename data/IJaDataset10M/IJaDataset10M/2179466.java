package com.jme3.system.lwjgl;

import com.jme3.math.FastMath;
import com.jme3.system.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.Sys;

/**
 * <code>Timer</code> handles the system's time related functionality. This
 * allows the calculation of the framerate. To keep the framerate calculation
 * accurate, a call to update each frame is required. <code>Timer</code> is a
 * singleton object and must be created via the <code>getTimer</code> method.
 *
 * @author Mark Powell
 * @version $Id: LWJGLTimer.java,v 1.21 2007/09/22 16:46:35 irrisor Exp $
 */
public class LwjglSmoothingTimer extends Timer {

    private static final Logger logger = Logger.getLogger(LwjglSmoothingTimer.class.getName());

    private long lastFrameDiff;

    private long oldTime;

    private float lastTPF, lastFPS;

    public static int TIMER_SMOOTHNESS = 32;

    private long[] tpf;

    private int smoothIndex;

    private static final long LWJGL_TIMER_RES = Sys.getTimerResolution();

    private static final float INV_LWJGL_TIMER_RES = (1f / LWJGL_TIMER_RES);

    private static float invTimerRezSmooth;

    public static final long LWJGL_TIME_TO_NANOS = (1000000000 / LWJGL_TIMER_RES);

    private long startTime;

    private boolean allSmooth = false;

    /**
     * Constructor builds a <code>Timer</code> object. All values will be
     * initialized to it's default values.
     */
    public LwjglSmoothingTimer() {
        reset();
        logger.log(Level.INFO, "Timer resolution: {0} ticks per second", LWJGL_TIMER_RES);
    }

    public void reset() {
        lastFrameDiff = 0;
        lastFPS = 0;
        lastTPF = 0;
        oldTime = -1;
        startTime = Sys.getTime();
        tpf = new long[TIMER_SMOOTHNESS];
        smoothIndex = TIMER_SMOOTHNESS - 1;
        invTimerRezSmooth = (1f / (LWJGL_TIMER_RES * TIMER_SMOOTHNESS));
        for (int i = tpf.length; --i >= 0; ) {
            tpf[i] = -1;
        }
    }

    /**
     * @see Timer#getTime() 
     */
    public long getTime() {
        return Sys.getTime() - startTime;
    }

    /**
     * @see Timer#getResolution() 
     */
    public long getResolution() {
        return LWJGL_TIMER_RES;
    }

    /**
     * <code>getFrameRate</code> returns the current frame rate since the last
     * call to <code>update</code>.
     *
     * @return the current frame rate.
     */
    public float getFrameRate() {
        return lastFPS;
    }

    public float getTimePerFrame() {
        return lastTPF;
    }

    /**
     * <code>update</code> recalulates the frame rate based on the previous
     * call to update. It is assumed that update is called each frame.
     */
    public void update() {
        long newTime = Sys.getTime();
        long oldTime = this.oldTime;
        this.oldTime = newTime;
        if (oldTime == -1) {
            lastTPF = 1 / 60f;
            lastFPS = 1f / lastTPF;
            return;
        }
        long frameDiff = newTime - oldTime;
        long lastFrameDiff = this.lastFrameDiff;
        if (lastFrameDiff > 0 && frameDiff > lastFrameDiff * 100) {
            frameDiff = lastFrameDiff * 100;
        }
        this.lastFrameDiff = frameDiff;
        tpf[smoothIndex] = frameDiff;
        smoothIndex--;
        if (smoothIndex < 0) {
            smoothIndex = tpf.length - 1;
        }
        lastTPF = 0.0f;
        if (!allSmooth) {
            int smoothCount = 0;
            for (int i = tpf.length; --i >= 0; ) {
                if (tpf[i] != -1) {
                    lastTPF += tpf[i];
                    smoothCount++;
                }
            }
            if (smoothCount == tpf.length) allSmooth = true;
            lastTPF *= (INV_LWJGL_TIMER_RES / smoothCount);
        } else {
            for (int i = tpf.length; --i >= 0; ) {
                if (tpf[i] != -1) {
                    lastTPF += tpf[i];
                }
            }
            lastTPF *= invTimerRezSmooth;
        }
        if (lastTPF < FastMath.FLT_EPSILON) {
            lastTPF = FastMath.FLT_EPSILON;
        }
        lastFPS = 1f / lastTPF;
    }

    /**
     * <code>toString</code> returns the string representation of this timer
     * in the format: <br>
     * <br>
     * jme.utility.Timer@1db699b <br>
     * Time: {LONG} <br>
     * FPS: {LONG} <br>
     *
     * @return the string representation of this object.
     */
    @Override
    public String toString() {
        String string = super.toString();
        string += "\nTime: " + oldTime;
        string += "\nFPS: " + getFrameRate();
        return string;
    }
}
