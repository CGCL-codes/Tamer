package sdljavax.gui;

import java.io.IOException;
import sdljava.video.SDLRect;
import org.gljava.opengl.GL;
import org.gljava.opengl.TextureFactory;

public class Window extends Container {

    public Window(GL gl, String name, SDLRect rect, SDLRect screenDimensions, String backgroundTexturePath) throws IOException {
        super(gl, name, rect, screenDimensions);
        if (backgroundTexturePath != null) {
            backgroundTexture = TextureFactory.getFactory().loadTexture(gl, backgroundTexturePath);
        }
    }

    public void draw(GL gl) {
        super.draw(gl);
        gl.glPushMatrix();
        gl.glTranslatef(xPos, yPos, 0);
        gl.glDisable(gl.GL_TEXTURE_2D);
        if (defaultFont != null) {
            defaultFont.faceSize(14, 72);
            gl.glColor3f(1.0f, 1.0f, 1.0f);
            gl.glRasterPos2f(0f + 2.0f, height - defaultFont.ascender() + 2.0f);
            defaultFont.render(name);
        }
        gl.glPopMatrix();
    }
}
