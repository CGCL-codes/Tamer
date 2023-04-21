package org.chernovia.lib.sims.ca.hodge;

import java.util.Vector;
import org.chernovia.lib.sims.ca.CA_Listener;

/**
 * John Chernoff's Hodgepodge Matrix Engine, in 3D!.
 * 
 * This cellular automaton is based on an article in Scientific American
 * ("Computer Recreations", 1987) on the "Hodgepodge" machine, which generates
 * complex and visually interesting wave-like behavior in a set of cells based
 * on a few simple rules of behavior.
 * 
 * @author John Chernoff (jachern@yahoo.com)
 * @version (9/23/04)
 */
public class HodgeEngine3D implements Runnable {

    private Vector<CA_Listener> HodgeListeners;

    private Thread AnimMat;

    private int[][][] cells, tmp;

    private int SIZE_X = 50, SIZE_Y = 50, SIZE_Z = 50;

    private int N = 255, ticks = 0, G = 7, K1 = 7, K2 = 7;

    private boolean WRAP = false, NEUMANN = false;

    private boolean INITIALIZED = false, RUNNING = false, PAUSED = false, UPDATING = true;

    private boolean VERBOSE = false;

    /**
	 * The one and only constructor for a HodgeEngine3D.
	 * 
	 * @param size The number of horizonal and vertical cells in the Hodgepodge Matrix
	 * @param width The width of the HodgeEngine Canvas
	 * @param height The height of the HodgeEngine Canvas
	 */
    public HodgeEngine3D() {
        this(255, 50, 50, 50);
    }

    public HodgeEngine3D(int sizex, int sizey, int sizez) {
        this(255, sizex, sizey, sizez);
    }

    public HodgeEngine3D(int maxState, int sizex, int sizey, int sizez) {
        N = maxState;
        SIZE_X = sizex;
        SIZE_Y = sizey;
        SIZE_Z = sizez;
        HodgeListeners = new Vector<CA_Listener>();
    }

    public void addHodgeListener(CA_Listener listener) {
        HodgeListeners.add(listener);
    }

    /**
	 * Returns the current size of the Hodgepodge Matrix.
	 * 
	 * @return The current size of the Hodgepodge Matrix.
	 */
    public int getMatrixSizeX() {
        return SIZE_X;
    }

    public int getMatrixSizeY() {
        return SIZE_Y;
    }

    public int getMatrixSizeZ() {
        return SIZE_Z;
    }

    /**
	 * Returns the current Hodgepodge state.
	 * 
	 * @return A two dimensional array of "cells" at various states from 0 - N,
	 * where N is the state of greatest cell "sickness".
	 */
    public int[][][] getMatrix() {
        return cells;
    }

    /**
	 * Returns the largest possible value for any cell in the Hodgepodge Matrix.
	 * 
	 * @return The largest possible value for any cell in the Hodgepodge Matrix.
	 */
    public int getMaxCellState() {
        return N;
    }

    /**
	 * Returns how many 'ticks' the Hodgepodge Engine has advanced.
	 * 
	 * @return The number of 'ticks' since the Hodgepodge Engine last started.
	 */
    public int getCurrentTick() {
        return ticks;
    }

    /**
	 * Starts and returns a new Hodgepodge Thread.
	 * 
	 * @return The newly started Hodgepodge Thread, or null if already running.
	 */
    public Thread runHodge() {
        if (RUNNING) return null;
        AnimMat = new Thread(this);
        AnimMat.start();
        return AnimMat;
    }

    public void setVerbose(boolean v) {
        VERBOSE = v;
    }

    public void pause() {
        PAUSED = true;
    }

    public boolean isPaused() {
        return PAUSED;
    }

    public void setUpdate(boolean u) {
        UPDATING = u;
    }

    public boolean isUpdating() {
        return UPDATING;
    }

    public boolean isRunning() {
        return RUNNING;
    }

    public void stopHodge() {
        RUNNING = false;
    }

    /**
	 * Manually notifies all HodgeListeners that the HodgeEngine is starting.
	 */
    public void notifyStart() {
        for (int i = 0; i < HodgeListeners.size(); i++) {
            (HodgeListeners.elementAt(i)).startingRun();
        }
    }

    /**
	 * Manually notifies all HodgeListeners that the HodgeEngine has finished.
	 */
    public void notifyFinish() {
        for (int i = 0; i < HodgeListeners.size(); i++) {
            (HodgeListeners.elementAt(i)).finishedRun();
        }
    }

    /**
	 * The main method of a Hodgepodge thread.  Though this method is
	 * required to be public, do not call it!  Use runHodge() instead.
	 * 
	 * @see #runHodge()
	 */
    public void run() {
        if (RUNNING) return;
        initMatrix();
        notifyStart();
        RUNNING = true;
        PAUSED = false;
        while (RUNNING && !PAUSED) {
            if (UPDATING) notifyTick(nextTick()); else nextTick();
        }
        if (!RUNNING) notifyFinish();
        RUNNING = false;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        if (g > 0) G = g;
        if (VERBOSE) System.out.println("G: " + G);
    }

    public int getK1() {
        return K1;
    }

    public void setK1(int k1) {
        if (k1 > 0) K1 = k1;
        if (VERBOSE) System.out.println("K1: " + k1);
    }

    public int getK2() {
        return K2;
    }

    public void setK2(int k2) {
        if (k2 > 0) K2 = k2;
        if (VERBOSE) System.out.println("K2: " + k2);
    }

    public boolean getNeumann() {
        return NEUMANN;
    }

    public void setNeumann(boolean n) {
        NEUMANN = n;
    }

    public boolean getWrap() {
        return WRAP;
    }

    public void setWrap(boolean w) {
        WRAP = w;
    }

    /**
	 * Manually advances the HodgeEngine one 'tick'.
	 * 
	 * @return The 'tick' in time the HodgeEngine is currently at, or -1 if uninitialized.
	 */
    public int nextTick() {
        return nextTick(G, K1, K2, NEUMANN, WRAP);
    }

    public int nextTick(int t) {
        int stopTick = ticks + t;
        while (nextTick() < stopTick) {
        }
        return stopTick;
    }

    /**
	 * Manually notifies all HodgeListeners that the HodgeEngine has completed a 'tick'.
	 */
    public void notifyTick(int t) {
        for (int l = 0; l < HodgeListeners.size(); l++) {
            (HodgeListeners.elementAt(l)).nextTick(t);
        }
    }

    /**
	 * Manually initializes a HodgeEngine.
	 */
    public void initMatrix() {
        if (!INITIALIZED || !PAUSED) resetMatrix();
    }

    public void resetMatrix() {
        if (RUNNING) return;
        cells = new int[SIZE_X][SIZE_Y][SIZE_Z];
        tmp = new int[SIZE_X][SIZE_Y][SIZE_Z];
        for (int x = 0; x < SIZE_X; x++) for (int y = 0; y < SIZE_Y; y++) for (int z = 0; z < SIZE_Z; z++) {
            cells[x][y][z] = (int) (Math.random() * (N / 3));
            tmp[x][y][z] = cells[x][y][z];
        }
        ticks = 0;
        INITIALIZED = true;
    }

    public boolean isInitialized() {
        return INITIALIZED;
    }

    public void delayRunThread(long refreshRate) {
        if (RUNNING && refreshRate >= 10) {
            try {
                Thread.sleep(refreshRate);
            } catch (InterruptedException ignore) {
            }
        }
    }

    private int nextTick(int g, int K1, int K2, boolean neumann, boolean wrap) throws ArrayIndexOutOfBoundsException {
        if (!INITIALIZED) return -1;
        int minX = 1, minY = 1, minZ = 1, maxX = SIZE_X - 1, maxY = SIZE_Y - 1, maxZ = SIZE_Z - 1;
        if (wrap) {
            minX = 0;
            minY = 0;
            minZ = 1;
            maxX = SIZE_X;
            maxY = SIZE_Y;
            maxZ = SIZE_Z;
        }
        for (int x = minX; x < maxX; x++) for (int y = minY; y < maxY; y++) for (int z = minZ; z < maxZ; z++) {
            if (cells[x][y][z] == 0) {
                int A = 0;
                int B = 0;
                for (int i = x - 1; i <= x + 1; i++) for (int j = y - 1; j <= y + 1; j++) for (int k = z - 1; k <= z + 1; k++) {
                    if (i == x && j == y && k == z) A += 0; else if (cells[i][j][k] > 0) A++; else B++;
                    tmp[x][y][z] = (A / K1) + (B / K2);
                }
                if (tmp[x][y][z] > N) tmp[x][y][z] = N;
            } else if (cells[x][y][z] < N) {
                int A = 1;
                int S = cells[x][y][z];
                for (int i = x - 1; i <= x + 1; i++) for (int j = y - 1; j <= y + 1; j++) for (int k = z - 1; k <= z + 1; k++) {
                    if (i == x && j == y && k == z) A += 0; else if (cells[i][j][k] > 0) {
                        A++;
                        S += cells[i][j][k];
                    }
                }
                tmp[x][y][z] = (S / A) + G;
                if (tmp[x][y][z] > N) tmp[x][y][z] = N;
            } else tmp[x][y][z] = 0;
        }
        for (int x = 0; x < SIZE_X; x++) for (int y = 0; y < SIZE_Y; y++) for (int z = 0; z < SIZE_Z; z++) {
            cells[x][y][z] = tmp[x][y][z];
        }
        return ++ticks;
    }
}
