package de.grogra.imp3d.glsl.renderpass;

import java.util.Iterator;
import java.util.Vector;
import javax.media.opengl.GL;
import javax.vecmath.Matrix4d;
import de.grogra.imp3d.Camera;
import de.grogra.imp3d.ParallelProjection;
import de.grogra.imp3d.PerspectiveProjection;
import de.grogra.imp3d.Projection;
import de.grogra.imp3d.glsl.GLSLDisplay;
import de.grogra.imp3d.glsl.GLSLUpdateCache;
import de.grogra.imp3d.glsl.Measures;
import de.grogra.imp3d.glsl.OpenGLState;
import de.grogra.imp3d.glsl.GLDisplay.GLVisitor;
import de.grogra.imp3d.glsl.utility.Drawable;

public abstract class RenderPass {

    protected int getID() {
        return -1;
    }

    protected GLSLUpdateCache newVisit = null;

    public void postDrawCallback(Drawable dr, OpenGLState glState, GLSLDisplay disp) {
    }

    public void renderVector(GLSLDisplay disp, Matrix4d worldToView, boolean normal, Vector<Drawable> cache) {
        GLVisitor old = disp.getVisitor();
        OpenGLState glState = disp.getCurrentGLState();
        if (newVisit == null) newVisit = new GLSLUpdateCache(disp);
        newVisit.init(disp.getRenderGraphState(), worldToView, 0);
        disp.setVisitor(newVisit);
        Iterator<Drawable> it = cache.iterator();
        while (it.hasNext()) {
            Drawable dr = it.next();
            postDrawCallback(dr, glState, disp);
            dr.draw(disp, newVisit, worldToView, normal);
        }
        disp.setVisitor(old);
    }

    public void renderAndUpdateVector(GLSLDisplay disp, Matrix4d worldToView, Vector<Drawable> cache) {
        GLVisitor old = disp.getVisitor();
        OpenGLState glState = disp.getCurrentGLState();
        if (newVisit == null) newVisit = new GLSLUpdateCache(disp);
        newVisit.init(disp.getRenderGraphState(), worldToView, 0);
        disp.setVisitor(newVisit);
        Iterator<Drawable> it = cache.iterator();
        while (it.hasNext()) {
            Drawable dr = it.next();
            postDrawCallback(dr, glState, disp);
            dr.drawAndUpdate(disp, newVisit, worldToView);
        }
        disp.setVisitor(old);
    }

    public static void deactivateTextures(GL gl, int cnt) {
        deactivateTextures(gl, cnt, GL.GL_TEXTURE_2D);
    }

    /**
	 * Deactivate Texture bindings from Texture-offset to Texture-cnt+offset
	 * 
	 * @param cnt
	 *            Number of Textures that should be deactivated
	 * @param type 
	 */
    public static void deactivateTextures(GL gl, int cnt, int type) {
        for (int i = cnt - 1; i >= 0; --i) {
            gl.glActiveTexture(GL.GL_TEXTURE0 + i);
            gl.glBindTexture(type, 0);
        }
    }

    public static void activateTextures(GL gl, int[] img, int cnt) {
        activateTextures(gl, img, cnt, GL.GL_TEXTURE_2D);
    }

    /**
	 * Bind Textures present in img-Array. Up to cnt Textures are bound
	 * 
	 * @param img
	 *            Array containing ids for OpenGL Textures.
	 * @param cnt
	 *            Maximum number of Textures that will be activated starting
	 *            with img[0].
	 * @param type 
	 */
    public static void activateTextures(GL gl, int[] img, int cnt, int type) {
        cnt = img.length > cnt ? img.length : cnt;
        for (int i = 0; i < cnt; ++i) {
            gl.glActiveTexture(GL.GL_TEXTURE0 + i);
            gl.glBindTexture(type, img[i]);
        }
    }

    public void setMatrix(OpenGLState glState, int where, Matrix4d which) {
        glState.getGL().glActiveTexture(GL.GL_TEXTURE0 + where);
        glState.loadMatrixd(which);
    }

    public void resetMatrix(OpenGLState glState, int count) {
        for (int i = count - 1; i >= 0; i--) {
            glState.getGL().glActiveTexture(GL.GL_TEXTURE0 + i);
            glState.getGL().glLoadIdentity();
        }
    }

    public static void ViewOrtho(OpenGLState glState) {
        ViewOrtho(glState, glState.width, glState.height);
    }

    /**
	 * construct an orthogranal view matrix for easing full screen quad drawing
	 */
    public static void ViewOrtho(OpenGLState glState, int width, int height) {
        GL gl = glState.getGL();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(0, width, height, 0, -1, 1);
    }

    /**
	 * change view matrix back to default
	 */
    public static void ViewPerspective(OpenGLState glState) {
        GL gl = glState.getGL();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPopMatrix();
    }

    public static void drawPrjQuad(OpenGLState glState, Camera c) {
        drawPrjQuad(glState, c, 0, 0, glState.width, glState.height);
    }

    public static void drawPrjQuad(OpenGLState glState, Camera c, float x, float y, float width, float height) {
        double angle = java.lang.Math.PI / 3.0;
        Projection prj = c.getProjection();
        if (prj instanceof PerspectiveProjection) {
            angle = PerspectiveProjection.clampFieldOfView(((PerspectiveProjection) prj).getFieldOfView());
            drawPrjQuad(glState, c, x, y, width, height, angle);
        } else if (prj instanceof ParallelProjection) {
            angle = ((ParallelProjection) prj).getWidth();
            drawPrjQuadParallel(glState, c, x, y, width, height, angle);
        } else drawPrjQuad(glState, c, x, y, width, height, angle);
    }

    /**
	 * Draw a fullscreen Quad (only useful in orthogonal mode)
	 * 
	 * @param c
	 */
    public static void drawPrjQuad(OpenGLState glState, Camera c, float x, float y, float width, float height, double angle) {
        GL gl = glState.getGL();
        double ratio = (double) height / (double) width;
        double fov = angle / 2.0;
        double halfWD = java.lang.Math.tan(fov);
        double halfHD = halfWD * ratio * c.getProjection().getAspect();
        float halfW = (float) halfWD;
        float halfH = (float) halfHD;
        gl.glBegin(GL.GL_TRIANGLE_STRIP);
        {
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, 0, 1);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, -halfW, halfH);
            gl.glVertex2f(x, y);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, 0, 0);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, -halfW, -halfH);
            gl.glVertex2f(x, y + height);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, 1, 1);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, halfW, halfH);
            gl.glVertex2f(x + width, y);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, 1, 0);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, halfW, -halfH);
            gl.glVertex2f(x + width, y + height);
        }
        gl.glEnd();
    }

    /**
	 * Draw a fullscreen Quad (only useful in orthogonal mode)
	 * 
	 * @param c
	 */
    public static void drawPrjQuadParallel(OpenGLState glState, Camera c, float x, float y, float width, float height, double projectionWidth) {
        GL gl = glState.getGL();
        double ratio = (double) height / (double) width;
        double halfWD = projectionWidth * 0.5;
        double halfHD = halfWD * ratio;
        float halfW = (float) halfWD;
        float halfH = (float) halfHD;
        gl.glBegin(GL.GL_TRIANGLE_STRIP);
        {
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, 0, 1);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, -halfW, halfH);
            gl.glVertex2f(x, y);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, 0, 0);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, -halfW, -halfH);
            gl.glVertex2f(x, y + height);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, 1, 1);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, halfW, halfH);
            gl.glVertex2f(x + width, y);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, 1, 0);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, halfW, -halfH);
            gl.glVertex2f(x + width, y + height);
        }
        gl.glEnd();
    }

    protected abstract void prologue(GLSLDisplay disp, OpenGLState glState, Object data);

    protected abstract void render(GLSLDisplay disp, OpenGLState glState, Object data);

    protected abstract void epilogue(GLSLDisplay disp, OpenGLState glState, Object data);

    public void process(GLSLDisplay disp, OpenGLState glState, Object data) {
        GLSLDisplay.printDebugInfoN(">>>>> " + this.getClass().getSimpleName() + "/" + this.hashCode() + " *****");
        prologue(disp, glState, data);
        render(disp, glState, data);
        epilogue(disp, glState, data);
        GLSLDisplay.printDebugInfoN("<<<<< " + this.getClass().getSimpleName() + "/" + this.hashCode() + " *****");
    }
}
