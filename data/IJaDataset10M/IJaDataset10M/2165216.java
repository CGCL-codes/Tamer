package demos.nehe.lesson08;

import demos.common.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.io.IOException;

class Renderer implements GLEventListener {

    private boolean lightingEnabled;

    private boolean lightingChanged = false;

    private boolean blendingEnabled;

    private boolean blendingChanged = false;

    private int filter;

    private int[] textures = new int[3];

    private float xrot;

    private float yrot;

    private float xspeed = 0.5f;

    private boolean increaseX;

    private boolean decreaseX;

    private float yspeed = 0.3f;

    private boolean increaseY;

    private boolean decreaseY;

    private float z = -5.0f;

    private boolean zoomIn;

    private boolean zoomOut;

    private float[] lightAmbient = { 0.5f, 0.5f, 0.5f, 1.0f };

    private float[] lightDiffuse = { 1.0f, 1.0f, 1.0f, 1.0f };

    private float[] lightPosition = { 0.0f, 0.0f, 2.0f, 1.0f };

    private GLU glu = new GLU();

    public void toggleBlending() {
        blendingEnabled = !blendingEnabled;
        blendingChanged = true;
    }

    public void toggleLighting() {
        lightingEnabled = !lightingEnabled;
        lightingChanged = true;
    }

    public void increaseXspeed(boolean increase) {
        increaseX = increase;
    }

    public void decreaseXspeed(boolean decrease) {
        decreaseX = decrease;
    }

    public void increaseYspeed(boolean increase) {
        increaseY = increase;
    }

    public void decreaseYspeed(boolean decrease) {
        decreaseY = decrease;
    }

    public void zoomIn(boolean zoom) {
        zoomIn = zoom;
    }

    public void zoomOut(boolean zoom) {
        zoomOut = zoom;
    }

    public void switchFilter() {
        filter = (filter + 1) % textures.length;
    }

    private void loadGLTextures(GLAutoDrawable gldrawable) throws IOException {
        TextureReader.Texture texture = TextureReader.readTexture("demos/data/images/glass.png");
        GL gl = gldrawable.getGL();
        gl.glGenTextures(3, textures, 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST);
        glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, 3, texture.getWidth(), texture.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
    }

    public void init(GLAutoDrawable glDrawable) {
        try {
            loadGLTextures(glDrawable);
        } catch (IOException e) {
            System.out.println("Unable to load textures,Bailing!");
            throw new RuntimeException(e);
        }
        GL gl = glDrawable.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPosition, 0);
        gl.glEnable(GL.GL_LIGHT1);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
    }

    private void update() {
        if (zoomOut) z -= 0.02f;
        if (zoomIn) z += 0.02f;
        if (decreaseX) xspeed -= 0.01f;
        if (increaseX) xspeed += 0.01f;
        if (increaseY) yspeed += 0.01f;
        if (decreaseY) yspeed -= 0.01f;
    }

    public void display(GLAutoDrawable glDrawable) {
        update();
        GL gl = glDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, z);
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[filter]);
        gl.glBegin(GL.GL_QUADS);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        xrot += xspeed;
        yrot += yspeed;
        if (lightingChanged) {
            if (lightingEnabled) gl.glEnable(GL.GL_LIGHTING); else gl.glDisable(GL.GL_LIGHTING);
            lightingChanged = false;
        }
        if (blendingChanged) {
            if (blendingEnabled) {
                gl.glEnable(GL.GL_BLEND);
                gl.glDisable(GL.GL_DEPTH_TEST);
            } else {
                gl.glDisable(GL.GL_BLEND);
                gl.glEnable(GL.GL_DEPTH_TEST);
            }
            blendingChanged = false;
        }
    }

    public void reshape(GLAutoDrawable glDrawable, int x, int y, int w, int h) {
        final GL gl = glDrawable.getGL();
        if (h <= 0) h = 1;
        final float a = (float) w / (float) h;
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, a, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable glDrawable, boolean b, boolean b1) {
    }
}
