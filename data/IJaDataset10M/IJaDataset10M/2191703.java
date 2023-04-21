package org.easyway.system;

import org.lwjgl.opengl.Display;

/**
 * @author Daniele Paggi
 * 
 */
public class ASincroD extends Thread {

    private Core core;

    /** create a new instance of Sincro */
    public ASincroD() {
        super();
        core = StaticRef.core;
        start();
    }

    /** main loop of game */
    public void run() {
        last = System.currentTimeMillis();
        ElaspedTime = 0;
        core.init();
        while (ASincroL.loop) {
            if (!Display.isCloseRequested()) {
                core.coreRender();
                Display.sync(ASincroL.fps);
                Display.update();
                fps();
            } else core.endGame();
        }
        core.cleanup();
    }

    /** used for the fps */
    private static int cont = 0, lastFps = 100;

    /** last time: used for calculate the fps */
    private long last;

    /** calculate the fps */
    private void fps() {
        cont++;
        if (System.currentTimeMillis() - last >= 1000) {
            lastFps = cont;
            System.out.println("FPS: " + cont);
            cont = 0;
            last = System.currentTimeMillis();
        }
    }

    /** returns how much FPS has got the Game in the last cycle */
    public static int getFps() {
        return lastFps;
    }

    private long ElaspedTime;

    /** returns the elasped time */
    public long getElaspedTime() {
        return ElaspedTime;
    }
}
