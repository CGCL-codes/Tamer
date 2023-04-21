package de.grogra.ext.sunshine.kernel.acceleration;

import static javax.media.opengl.GL.GL_TEXTURE0;
import static javax.media.opengl.GL.GL_TEXTURE1;
import static javax.media.opengl.GL.GL_TEXTURE2;
import static javax.media.opengl.GL.GL_TEXTURE3;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import de.grogra.ext.sunshine.SunshineSceneVisitor;

public class OctreeKernel extends AccelerationKernel {

    private static final String OCTREE_MAIN = "nonintrusivOctreeTraversal.frag";

    private int octreeDepth;

    public OctreeKernel(String name, GLAutoDrawable drawable, int[] data, int tileSize, int objects, int steps, int depth) {
        super(name, drawable, tileSize, objects, steps, data);
        octreeDepth = depth;
    }

    @Override
    public void execute(GLAutoDrawable drawable, int px, int py, int i) {
        GL gl = drawable.getGL();
        int a0Loc = getUniformLocation("a0", drawable);
        int a1Loc = getUniformLocation("a1", drawable);
        int stateLoc = getUniformLocation(STATE_TEXTURE, drawable);
        int treeLoc = getUniformLocation(TREE_TEXTURE, drawable);
        useProgram(drawable);
        setUniformParameters(drawable);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(texTarget, inputTextureA[0]);
        setUniformTex(a0Loc, 0, drawable);
        gl.glActiveTexture(GL_TEXTURE1);
        gl.glBindTexture(texTarget, inputTextureA[1]);
        setUniformTex(a1Loc, 1, drawable);
        gl.glActiveTexture(GL_TEXTURE2);
        gl.glBindTexture(texTarget, inputTextureA[3]);
        setUniformTex(stateLoc, 2, drawable);
        gl.glActiveTexture(GL_TEXTURE3);
        gl.glBindTexture(texTarget, treeTexture[0]);
        setUniformTex(treeLoc, 3, drawable);
        drawQuad(drawable, px, py);
        stopProgram(drawable);
    }

    @Override
    public void loadSource(GLAutoDrawable drawable, SunshineSceneVisitor monitor, String intermediates) {
        setSource(drawable, new String[] { super.loadSource(EXTENSIONS), SAMPLER + "a0;\n", SAMPLER + "a1;\n", SAMPLER + "a2;\n", SAMPLER + SCENE_TEXTURE + ";\n", SAMPLER + "stateTexture;\n", INTERRUPTERS, intermediates, "int treeDepth = " + octreeDepth + ";", super.loadSource(STRUCTS), loadSource(TEXTURE_LOOKUP), loadSource(INTERSECT_UTILS), loadSource(INTERSECTION), "float random;", super.loadSource(INITIALISATION), loadSource(OCTREE_MAIN) });
    }

    protected String loadSource(String s) {
        return super.loadSource(s, PATH);
    }
}
