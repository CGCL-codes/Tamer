package drk.game;

import drk.DeltaTimer;
import drk.Updatable;
import drk.graphics.*;
import drk.maze.*;
import java.awt.event.*;
import javax.media.opengl.*;

public class MazeGame extends GLRenderedGraphicsListener implements KeyListener, MouseListener, Updatable, GLInitializable, GLRenderable, MouseMotionListener {

    public RenderableMaze m;

    public MazeCamera ec;

    public boolean isInitialized() {
        return true;
    }

    public MazeGame() {
        super(new DebugMazeCamera());
        ec = (MazeCamera) this.camera;
        ec.setMazeGame(this);
    }

    public void init(GLAutoDrawable a) {
        GL gl = a.getGL();
        frameTimer.update();
        initialize(gl);
        m.setDeltaTimer(frameTimer);
    }

    int numFrames = 0;

    double timePassed = 0.0;

    public void display(GLAutoDrawable a) {
        frameTimer.update();
        timePassed += frameTimer.getDeltaTimeSeconds();
        numFrames++;
        if (timePassed > 1.0) {
            System.err.println("AverageFramerate: " + DeltaTimer.getMicrosecondsPerFrame(timePassed, (double) numFrames) + " uspf, " + DeltaTimer.getFramesPerSecond(timePassed, (double) numFrames) + "fps");
            timePassed = 0.0;
            numFrames = 0;
        }
        render(a.getGL());
        update();
    }

    public void initialize(GL gl) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        camera.fovy = 50.0;
        ec.initialize(gl);
    }

    public void render(GL gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        camera.render(gl);
        m.setCamera(ec);
        m.render(gl);
    }

    public void update() {
        ec.update();
    }

    public void frameClose() {
        jf.dispose();
    }

    public void frameVisible(int x) {
        if (x == 0) super.jf.setVisible(false); else super.jf.setVisible(true);
    }

    public static void main(String[] argv) {
        MazeGame tgl = new MazeGame();
        tgl.camera.fovy = 30.0;
        tgl.doMain(640, 480, null, true);
    }
}
