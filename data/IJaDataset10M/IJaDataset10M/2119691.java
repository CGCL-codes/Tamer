package net.sf.breed.kout;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import net.sf.breed.kout.draw.IBreedDrawer;
import net.sf.breed.kout.io.KOutKeyListener;
import net.sf.breed.kout.model.IBreedWorld;
import net.sf.breed.kout.model.KOutMath;
import net.sf.breed.kout.model.actor.IBreedSprite;
import com.sun.opengl.util.Animator;

/**
 * The JOGL event listener.
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since Oct 11, 2006
 */
public class KOutEventLoop implements GLEventListener {

    /** The OpenGLU rendering unit. */
    private GLU glu;

    /** The world context. */
    private IBreedContext context;

    /** The animator. */
    private Animator animator;

    /** Whether stopping should be invoked. */
    private boolean stopCalled = false;

    /** Whether the stop has been finished. */
    private boolean stopFinalised = false;

    /**
   * A new event listener.
   * 
   * @param animator The ticking animator.
   */
    public KOutEventLoop(Animator animator) {
        this.animator = animator;
    }

    /**
   * Initializes the JOGL environment.
   * 
   * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
   */
    public void init(GLAutoDrawable drawable) {
        System.out.println("Init called.");
        final GL gl = drawable.getGL();
        drawable.setGL(new DebugGL(gl));
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        glu = new GLU();
        animator.start();
    }

    /**
   * Displays one tick.
   * 
   * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
   */
    public void display(GLAutoDrawable drawable) {
        if (stopCalled) {
            stopFinalised = true;
            return;
        }
        KOutKeyListener keyEar = (KOutKeyListener) context.getUserInput();
        KOutCamera camera = (KOutCamera) context.getCamera();
        IBreedWorld world = context.getWorld();
        if (keyEar.quitPressed) {
            quit();
            return;
        }
        List<IBreedDrawer> drawerList = world.getDrawers();
        for (IBreedDrawer drawer : drawerList) {
            drawer.tick(context);
        }
        List<IBreedSprite> actorList = world.getActors();
        for (IBreedSprite actor : actorList) {
            actor.tick(context);
        }
        camera.tick(context);
        final int actorCount = actorList.size();
        for (int pos1 = 0; pos1 < actorCount; pos1++) {
            IBreedSprite actor = actorList.get(pos1);
            for (int pos2 = pos1 + 1; pos2 < actorCount; pos2++) {
                IBreedSprite otherActor = actorList.get(pos2);
                if (KOutMath.isCollision(actor, otherActor)) {
                    actor.impact(otherActor);
                    otherActor.impact(actor);
                }
            }
        }
        for (IBreedSprite actor : actorList) {
            actor.strategy(context);
        }
        final GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        camera.setCamera(context, gl, glu);
        for (IBreedDrawer drawer : drawerList) {
            gl.glLoadIdentity();
            drawer.draw(gl, glu);
        }
        setLighting(gl);
    }

    /**
   * Sets the lighting.
   * 
   * @param gl The GL context.
   */
    private void setLighting(GL gl) {
        KOutKeyListener keyEar = (KOutKeyListener) context.getUserInput();
        if (!keyEar.isPressed(KeyEvent.VK_X)) {
            return;
        }
    }

    /**
   * Called by the drawable during the first repaint after the component
   * has been resized.
   * 
   * <p>Sets the view port here.
   * 
   * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable, int, int, int, int)
   */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        System.out.println("Reshape called.");
        final GL gl = drawable.getGL();
        if (height == 0) {
            height = 1;
        }
        gl.glViewport(0, 0, width, height);
        context.getCamera().reshapeWindow(gl, width, height);
    }

    /**
   * Invoked when the display has been changed. Currently unsupported.
   * 
   * @see javax.media.opengl.GLEventListener#displayChanged(javax.media.opengl.GLAutoDrawable, boolean, boolean)
   */
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        throw new UnsupportedOperationException("Changing display is not supported.");
    }

    /**
   * Tells the display to stop at the next opportunity.
   */
    public void stopDisplay() {
        stopCalled = true;
    }

    /**
   * Indicates whether a stop has been seen.
   * 
   * @return Whether a stop has been seen.
   */
    public boolean stopSeen() {
        return stopFinalised;
    }

    /**
   * Quits the application.
   */
    public void quit() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                stopDisplay();
                while (!stopSeen()) {
                    System.out.println("Stopping game...");
                    try {
                        Thread.sleep(200);
                    } catch (Exception ex) {
                    }
                }
                animator.stop();
                System.out.println("Game stopped.");
                System.exit(0);
            }
        });
        thread.setName("JOGL Application Quitter");
        thread.start();
    }

    /**
   * Returns the key listener.
   * 
   * @return The key listener.
   */
    public KeyListener getKeyListener() {
        if (null == context) {
            context = new KOutContext();
        }
        return (KeyListener) context.getUserInput();
    }
}
