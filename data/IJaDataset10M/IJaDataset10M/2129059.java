package de.grogra.ext.sunshine.kernel.bidirPT;

import static javax.media.opengl.GL.GL_TEXTURE0;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import de.grogra.ext.sunshine.SunshineSceneVisitor;
import de.grogra.ext.sunshine.kernel.InitKernel;

/**
 * @author Thomas
 *
 */
public class TraceInitKernel extends InitKernel {

    public TraceInitKernel(String name, GLAutoDrawable drawable, int[] seed, int size) {
        super(name, drawable, size);
    }

    @Override
    public void execute(GLAutoDrawable drawable, int px, int py, int sample) {
        GL gl = drawable.getGL();
        int a1Loc = getUniformLocation("a1", drawable);
        useProgram(drawable);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(texTarget, inputTextureA[1]);
        setUniformTex(a1Loc, 0, drawable);
        drawQuad(drawable, px, py);
        stopProgram(drawable);
    }

    @Override
    public void loadSource(GLAutoDrawable drawable, SunshineSceneVisitor monitor, String intermediates) {
        setSource(drawable, new String[] { loadSource(EXTENSIONS), loadSource(RANDOM), SAMPLER + "a1;\n", "void main()\n", "{\n", "	gl_FragData[0] = vec4(0.0);\n", "	gl_FragData[1] = vec4(0.0, 0.0, 0.0, getSeed());\n", "	gl_FragData[2] = vec4(0.0);\n", "	gl_FragData[3] = vec4(0.0);\n", "}\n" });
    }
}
