package org.lwjgl.util.model;

import java.io.Serializable;

/**
 * $Id: Weight.java 1245 2004-06-12 20:28:34Z matzon $
 * Describes how a bone influences a vertex.
 * @author $Author: matzon $
 * @version $Revision: 1245 $
 */
public class Weight implements Serializable {

    public static final long serialVersionUID = 1L;

    /** Bone index */
    private final int bone;

    /** Weight */
    private final float weight;

    /**
	 * C'tor
	 */
    public Weight(int bone, float weight) {
        this.bone = bone;
        this.weight = weight;
    }

    /**
	 * @return Returns the bone index.
	 */
    public int getBone() {
        return bone;
    }

    /**
	 * @return Returns the weight.
	 */
    public float getWeight() {
        return weight;
    }
}
