package octlight.test;

import net.java.games.jogl.GL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class LWJGLTEst {

    public static void main(String[] args) throws Exception {
        System.out.println(Display.getAdapter());
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        Display.create();
        long startTime = System.currentTimeMillis();
        double angle = 0;
        GL11.glMatrixMode(GL.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-2, 2, -2, 2, -2, 2);
        Display.setVSyncEnabled(true);
        for (int i = 0; i < 400; i++) {
            GL11.glClear(GL.GL_COLOR_BUFFER_BIT);
            GL11.glMatrixMode(GL.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glTranslatef((float) Math.sin(angle) / 2, 0, 0);
            GL11.glBegin(GL.GL_QUADS);
            GL11.glVertex2f(-0.2f, -0.7f);
            GL11.glVertex2f(-0.2f, 0.7f);
            GL11.glVertex2f(0.2f, 0.7f);
            GL11.glVertex2f(0.2f, -0.7f);
            GL11.glEnd();
            Display.update();
            angle += 0.1;
        }
        float time = (System.currentTimeMillis() - startTime) / 1000f;
        System.out.println("Rendering time for 400 frames is " + time + " seconds = " + (400 / time) + " fps.");
        Display.destroy();
    }
}
