package org.cake.game;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 * A basic low-level game loop callback.
 * @author Aaron Cake
 */
public interface iGameCallback {

    /**
     * Called whenever the game is requested an exit, through events such as clicking the close window button to see if the game is ready to exit.
     * @return true if the game is ready to exit, false if not. If false is returned it is up the implementation to exit when it is ready to do so.
     */
    public boolean exitRequested(Game g, GL gl, GLU glu);

    /**
     * Called when the game actually exits
     */
    public void exit(Game g, GL gl, GLU glu);

    /**
     * Initialize various game stuff, including loading resources.
     */
    public void init(Game g, GL gl, GLU glu);

    /**
     * Initialize the GL context with customized parameters here as it may need to be done multiple times.
     */
    public void initGL(Game g, GL gl, GLU glu);

    /**
     * Called once per frame to render/update the game.
     */
    public void render(Game g, GL gl, GLU glu);

    /**
     * Called after the game is completely finished.
     */
    public void finished();
}
