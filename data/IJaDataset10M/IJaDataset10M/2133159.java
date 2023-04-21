package com.rugl.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * @author ryanm
 */
public class GLProject {

    private static FloatBuffer model = BufferUtils.createFloatBuffer(16);

    private static FloatBuffer project = BufferUtils.createFloatBuffer(16);

    private static IntBuffer viewport = BufferUtils.createIntBuffer(16);

    private static float[] resultArray = new float[3];

    private static FloatBuffer result = FloatBuffer.wrap(resultArray);

    /**
	 * Use this to retrieve the matrices from opengl before using
	 * {@link #project(float, float, float)}. Note you shouldn't use
	 * this for every call to {@link #GLProject()}, just whenever the
	 * viewport or projection or modelview matrices have changed. Once
	 * per frame should do the trick
	 */
    public static void getMatrices() {
        model.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, model);
        project.clear();
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, project);
        viewport.clear();
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
    }

    /**
	 * Use this to find the screen coordinates of a 3D point. Remember
	 * to use {@link #getMatrices()} first
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return A three-element [ x, y, z ] array. The array is reused
	 *         in subsequent calls, so don't hold on to it.
	 */
    public static float[] project(float x, float y, float z) {
        result.clear();
        GLU.gluProject(x, y, z, model, project, viewport, result);
        return resultArray;
    }
}
