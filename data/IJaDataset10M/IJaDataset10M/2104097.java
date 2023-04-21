package swarm.performance.guitests;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collections;
import javax.imageio.ImageIO;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.Util;
import swarm.engine.Individual;
import swarm.engine.Parameters;
import swarm.engine.Population;
import swarm.engine.PopulationSimulator;
import swarm.engine.Recipe;

public class Test2 {

    private static final String GAME_TITLE = "Test1";

    private static final int FRAMERATE = 60;

    private static boolean finished = false;

    private static float angle = 0;

    private static int textureID = -1;

    /**
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        boolean fullscreen = (args.length == 1 && args[0].equals("-fullscreen"));
        try {
            init(fullscreen, args[0]);
            run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            Sys.alert(GAME_TITLE, "An error occured and the game will exit.");
        } finally {
            cleanup();
        }
        System.exit(0);
    }

    /**
	 * Initialise the game
	 * @param str 
	 * 
	 * @throws Exception
	 *             if init fails
	 */
    private static void init(boolean fullscreen, String str) throws Exception {
        initWindowAndGL(fullscreen);
        loadTexture();
        initProjection();
        Recipe recipe = new Recipe(str);
        population = new Population(recipe.createPopulation(200, 200), "Test");
        sim = new PopulationSimulator(population);
        sim.setWidth(Display.getDisplayMode().getWidth() / getScale());
        sim.setHeight(Display.getDisplayMode().getHeight() / getScale());
    }

    private static void initProjection() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, Display.getDisplayMode().getWidth(), 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
    }

    private static void initWindowAndGL(boolean fullscreen) throws LWJGLException {
        Display.setTitle(GAME_TITLE);
        Display.setFullscreen(fullscreen);
        Display.setVSyncEnabled(true);
        Display.create(new PixelFormat().withDepthBits(24).withSamples(16));
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        Util.checkGLError();
    }

    private static void loadTexture() throws IOException {
        BufferedImage img = ImageIO.read(Test2.class.getClassLoader().getResource("swarm/performance/point.png"));
        bindTexture();
        if (img.getHeight() > 1024 || img.getWidth() > 1024) {
            Image scaledImg;
            if (img.getHeight() > img.getWidth()) scaledImg = img.getScaledInstance(-1, 1024, Image.SCALE_SMOOTH); else scaledImg = img.getScaledInstance(1024, -1, Image.SCALE_SMOOTH);
            img = new BufferedImage(scaledImg.getWidth(null), scaledImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            img.getGraphics().drawImage(scaledImg, 0, 0, null);
        }
        ByteBuffer rgb = ByteBuffer.allocateDirect(img.getWidth() * img.getHeight() * 4);
        int[] rgb2 = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        rgb.asIntBuffer().put(rgb2);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, img.getWidth(), img.getHeight(), 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8, rgb);
        Util.checkGLError();
    }

    private static void bindTexture() {
        if (textureID == -1) {
            IntBuffer buf = ByteBuffer.allocateDirect(4).asIntBuffer();
            buf.put(-1);
            buf.clear();
            GL11.glGenTextures(buf);
            textureID = buf.get();
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        Util.checkGLError();
    }

    /**
	 * Runs the game (the "main loop")
	 */
    private static void run() {
        while (!finished) {
            Display.update();
            if (Display.isCloseRequested()) {
                finished = true;
            } else if (Display.isActive()) {
                logic();
                render();
                Display.sync(FRAMERATE);
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                logic();
                if (Display.isVisible() || Display.isDirty()) {
                    render();
                }
            }
        }
    }

    /**
	 * Do any game-specific cleanup
	 */
    private static void cleanup() {
        Display.destroy();
    }

    /**
	 * Do all calculations, handle input, etc.
	 */
    private static void logic() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            finished = true;
        }
        angle += 2.0f % 360;
        sim.stepSimulation(Collections.<Individual>emptyList(), 0);
    }

    @SuppressWarnings("unused")
    private static double rand() {
        return (Math.random() - 0.5) * 5;
    }

    private static Population population;

    private static PopulationSimulator sim;

    /**
	 * Render the current frame
	 */
    private static void render() {
        GL11.glClearColor(1, 1, 1, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glPushMatrix();
        GL11.glTranslatef(Display.getDisplayMode().getWidth() / 2, Display.getDisplayMode().getHeight() / 2, 0.0f);
        GL11.glScalef(getScale(), getScale(), 1);
        glEnable(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        for (Individual ind : population) {
            drawAgent(ind);
        }
        glDisable(GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private static float getScale() {
        return 0.6f;
    }

    public static void drawAgent(Individual ind) {
        double th = Math.atan2(ind.getDy(), ind.getDx());
        GL11.glPushMatrix();
        GL11.glTranslated(ind.getX(), ind.getY(), 0.0f);
        GL11.glRotatef((float) Math.toDegrees(th), 0, 0, 1.0f);
        Color color = ind.getGenome().getDisplayColor();
        glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        bindTexture();
        float length = (float) (ind.getGenome().getNeighborhoodRadius() * getMaxAgentRadius() / Parameters.getMaxNeighborhoodRadius()) + getMinAgentLength();
        float width = (float) (length / (1 + Math.hypot(ind.getDx(), ind.getDy())));
        GL11.glBegin(GL11.GL_QUADS);
        glTexCoord2d(0, 0);
        GL11.glVertex2f(-length, -width);
        glTexCoord2d(0, 1);
        GL11.glVertex2f(length, -width);
        glTexCoord2d(1, 1);
        GL11.glVertex2f(length, width);
        glTexCoord2d(1, 0);
        GL11.glVertex2f(-length, width);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private static int getMinAgentLength() {
        return 10;
    }

    private static double getMaxAgentRadius() {
        return 50;
    }
}
