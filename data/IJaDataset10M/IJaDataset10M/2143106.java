package chapt06.multisample;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.Random;
import shared.*;

public class Multisample {

    static GLU glu = new GLU();

    static GLUT glut = new GLUT();

    static int NUM_SPHERES = 50;

    static GLFrame spheres[] = new GLFrame[NUM_SPHERES];

    static GLFrame frameCamera = new GLFrame();

    static float fLightPos[] = { -100.0f, 100.0f, 50.0f, 1.0f };

    static float fNoLight[] = { 0.0f, 0.0f, 0.0f, 0.0f };

    static float fLowLight[] = { 0.25f, 0.25f, 0.25f, 1.0f };

    static float fBrightLight[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    static float mShadowMatrix[] = new float[16];

    static float yRot = 0.0f;

    protected static void setup(GL2 gl2, int width, int height) {
        int iSphere;
        Random random = new Random();
        float vPoints[][] = { { 0.0f, -0.4f, 0.0f }, { 10.0f, -0.4f, 0.0f }, { 5.0f, -0.4f, -5.0f } };
        gl2.glClearColor(fLowLight[0], fLowLight[1], fLowLight[2], fLowLight[3]);
        gl2.glCullFace(GL2.GL_BACK);
        gl2.glFrontFace(GL2.GL_CCW);
        gl2.glEnable(GL2.GL_CULL_FACE);
        gl2.glEnable(GL2.GL_DEPTH_TEST);
        gl2.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, fNoLight, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, fLowLight, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, fBrightLight, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, fBrightLight, 0);
        gl2.glEnable(GL2.GL_LIGHTING);
        gl2.glEnable(GL2.GL_LIGHT0);
        float vPlaneEquation[] = new float[4];
        M3D.getPlaneEquation(vPlaneEquation, vPoints[0], vPoints[1], vPoints[2]);
        M3D.makePlanarShadowMatrix(mShadowMatrix, vPlaneEquation, fLightPos);
        gl2.glEnable(GL2.GL_COLOR_MATERIAL);
        gl2.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl2.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 128);
        for (iSphere = 0; iSphere < NUM_SPHERES; iSphere++) {
            float x = ((float) random.nextInt(400) - 200) * 0.1f;
            float z = ((float) random.nextInt(400) - 200) * 0.1f;
            spheres[iSphere] = new GLFrame();
            spheres[iSphere].setOrigin(x, 0.0f, z);
        }
        gl2.glEnable(GL2.GL_MULTISAMPLE);
    }

    static void drawGround(GL2 gl2) {
        float fExtent = 20.0f;
        float fStep = 1.0f;
        float y = -0.4f;
        float iStrip, iRun;
        for (iStrip = -fExtent; iStrip <= fExtent; iStrip += fStep) {
            gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
            gl2.glNormal3f(0.0f, 1.0f, 0.0f);
            for (iRun = fExtent; iRun >= -fExtent; iRun -= fStep) {
                gl2.glVertex3f(iStrip, y, iRun);
                gl2.glVertex3f(iStrip + fStep, y, iRun);
            }
            gl2.glEnd();
        }
    }

    static void drawInhabitants(GL2 gl2, boolean nShadow) {
        int i;
        if (!nShadow) yRot += 0.5f; else gl2.glColor3f(0.0f, 0.0f, 0.0f);
        if (!nShadow) gl2.glColor3f(0.0f, 1.0f, 0.0f);
        for (i = 0; i < NUM_SPHERES; i++) {
            gl2.glPushMatrix();
            spheres[i].applyActorTransform(gl2);
            glut.glutSolidSphere(0.3f, 17, 9);
            gl2.glPopMatrix();
        }
        gl2.glPushMatrix();
        gl2.glTranslatef(0.0f, 0.1f, -2.5f);
        if (!nShadow) gl2.glColor3f(0.0f, 0.0f, 1.0f);
        gl2.glPushMatrix();
        gl2.glRotatef(-yRot * 2.0f, 0.0f, 1.0f, 0.0f);
        gl2.glTranslatef(1.0f, 0.0f, 0.0f);
        glut.glutSolidSphere(0.1f, 17, 9);
        gl2.glPopMatrix();
        if (!nShadow) {
            gl2.glColor3f(1.0f, 0.0f, 0.0f);
            gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, fBrightLight, 0);
        }
        gl2.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
        GLT.drawTorus(gl2, 0.35f, 0.15f, 61, 37);
        gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, fNoLight, 0);
        gl2.glPopMatrix();
    }

    protected static void render(GL2 gl2, int w, int h) {
        float fAspect;
        if (h == 0) h = 1;
        gl2.glViewport(0, 0, w, h);
        fAspect = (float) w / (float) h;
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        glu.gluPerspective(35.0f, fAspect, 1.0f, 50.0f);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();
        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl2.glPushMatrix();
        frameCamera.applyCameraTransform(gl2);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, fLightPos, 0);
        gl2.glColor3f(0.6f, 0.4f, 0.1f);
        drawGround(gl2);
        gl2.glDisable(GL2.GL_DEPTH_TEST);
        gl2.glDisable(GL2.GL_LIGHTING);
        gl2.glPushMatrix();
        gl2.glMultMatrixf(mShadowMatrix, 0);
        drawInhabitants(gl2, true);
        gl2.glPopMatrix();
        gl2.glEnable(GL2.GL_LIGHTING);
        gl2.glEnable(GL2.GL_DEPTH_TEST);
        drawInhabitants(gl2, false);
        gl2.glPopMatrix();
        gl2.glFlush();
    }
}
