package demos.nehe.lesson23;

import demos.common.TextureReader;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.glu.GLU;
import java.io.IOException;

class Renderer implements GLEventListener {

    private int filter = 0;

    private int curObject = 0;

    private float xrot = 0f;

    private float yrot = 0f;

    private float yspeed;

    private boolean increaseY;

    private boolean decreaseY;

    private float xspeed;

    private boolean increaseX;

    private boolean decreaseX;

    private float z = -10;

    private boolean zoomIn;

    private boolean zoomOut;

    private int textures[] = new int[6];

    private float lightAmbient[] = { 0.2f, 0.2f, 0.2f };

    private float lightDiffuse[] = { 1.0f, 1.0f, 1.0f };

    private float lightPosition[] = { 0.0f, 0.0f, 2.0f };

    private GLUquadric quadratic;

    private GLU glu = new GLU();

    public Renderer() {
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

    public void zoomOut(boolean zoom) {
        zoomOut = zoom;
    }

    public void zoomIn(boolean zoom) {
        zoomIn = zoom;
    }

    public void switchFilter() {
        filter = (filter + 1) % 2;
    }

    public void switchObject() {
        curObject = (curObject + 1) % 4;
    }

    public void loadGLTextures(GL gl) throws IOException {
        String[] textureNames = new String[] { "demos/data/images/bg.png", "demos/data/images/reflect.png" };
        gl.glGenTextures(6, textures, 0);
        for (int textureIndex = 0; textureIndex < textureNames.length; textureIndex++) {
            String textureName = textureNames[textureIndex];
            TextureReader.Texture texture = TextureReader.readTexture(textureName);
            for (int loop = 0; loop <= 1; loop++) {
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[loop]);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
                gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[loop + 2]);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
                gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[loop + 4]);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST);
                gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
            }
        }
    }

    private void initLights(GL gl) {
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPosition, 0);
        gl.glEnable(GL.GL_LIGHT1);
    }

    public void init(GLAutoDrawable glDrawable) {
        GL gl = glDrawable.getGL();
        try {
            loadGLTextures(gl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        initLights(gl);
        quadratic = glu.gluNewQuadric();
        glu.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
        glu.gluQuadricTexture(quadratic, true);
        gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP);
        gl.glTexGeni(GL.GL_T, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP);
    }

    private void glDrawCube(GL gl) {
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
        gl.glEnable(GL.GL_TEXTURE_GEN_S);
        gl.glEnable(GL.GL_TEXTURE_GEN_T);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[filter + (filter + 1)]);
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, z);
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
        switch(curObject) {
            case 0:
                glDrawCube(gl);
                break;
            case 1:
                gl.glTranslatef(0.0f, 0.0f, -1.5f);
                glu.gluCylinder(quadratic, 1.0f, 1.0f, 3.0f, 32, 32);
                break;
            case 2:
                glu.gluSphere(quadratic, 1.3f, 32, 32);
                break;
            case 3:
                gl.glTranslatef(0.0f, 0.0f, -1.5f);
                glu.gluCylinder(quadratic, 1.0f, 0.0f, 3.0f, 32, 32);
                break;
        }
        gl.glPopMatrix();
        gl.glDisable(GL.GL_TEXTURE_GEN_S);
        gl.glDisable(GL.GL_TEXTURE_GEN_T);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[filter * 2]);
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -24.0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-13.3f, -10.0f, 10.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(13.3f, -10.0f, 10.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(13.3f, 10.0f, 10.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-13.3f, 10.0f, 10.0f);
        gl.glEnd();
        gl.glPopMatrix();
        xrot += xspeed;
        yrot += yspeed;
    }

    public void reshape(GLAutoDrawable glDrawable, int x, int y, int w, int h) {
        if (h == 0) h = 1;
        GL gl = glDrawable.getGL();
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, (float) w / (float) h, 0.1f, 100.0f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable glDrawable, boolean b, boolean b1) {
    }
}
