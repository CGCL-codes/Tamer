package de.grogra.ext.sunshine.kernel.acceleration;

import javax.media.opengl.GLAutoDrawable;
import de.grogra.ext.sunshine.kernel.bidirPT.InterruptableKernel;

abstract class AccelerationKernel extends InterruptableKernel {

    protected final String PATH = "../shaderCode/acceleration/";

    protected static final String TREE_TEXTURE = "treeTexture";

    protected static final String TEXTURE_LOOKUP = "octreeLookUPs.frag";

    protected static final String INTERSECTION = "cellIntersection.frag";

    protected int[] treeTexture;

    public AccelerationKernel(String name, GLAutoDrawable drawable, int tileSize, int objects, int steps, int[] data) {
        super(name, drawable, tileSize, objects, steps);
        treeTexture = data;
    }
}
