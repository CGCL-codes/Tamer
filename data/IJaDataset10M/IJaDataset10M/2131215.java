package gestalt.impl.jogl.shape;

import javax.media.opengl.GL;
import gestalt.context.GLContext;
import gestalt.impl.jogl.context.JoglGLContext;
import gestalt.shape.Quad;
import gestalt.util.JoglUtil;

public class JoglQuad extends Quad {

    public JoglQuad() {
        material = new JoglMaterial();
    }

    public void draw(final GLContext theRenderContext) {
        final GL gl = ((JoglGLContext) theRenderContext).gl;
        material.begin(theRenderContext);
        gl.glPushMatrix();
        JoglUtil.applyTransform(gl, _myTransformMode, transform, rotation, scale);
        gl.glBegin(GL.GL_QUADS);
        JoglUtil.draw(gl, a());
        JoglUtil.draw(gl, b());
        JoglUtil.draw(gl, c());
        JoglUtil.draw(gl, d());
        gl.glEnd();
        gl.glPopMatrix();
        material.end(theRenderContext);
        drawChildren(theRenderContext);
    }
}
