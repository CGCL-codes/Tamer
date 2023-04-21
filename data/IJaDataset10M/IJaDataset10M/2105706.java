package com.realtime.crossfire.jxclient.animations;

import org.jetbrains.annotations.NotNull;

/**
 * Manages animations received from the server. Animations are uniquely
 * identified by an animation id. Each animation consists of a list of faces.
 * @author Andreas Kirschbaum
 */
public class Animation {

    /**
     * The animation ID.
     */
    private final int animationId;

    /**
     * Flags for the animation; currently unused by the server.
     */
    private final int flags;

    /**
     * The faces list of the animation.
     */
    @NotNull
    private final int[] faces;

    /**
     * Creates a new instance.
     * @param animationId the animation ID
     * @param flags flags for the animation; currently unused
     * @param faces the faces list of the animation
     */
    public Animation(final int animationId, final int flags, @NotNull final int[] faces) {
        assert faces.length > 0;
        this.animationId = animationId;
        this.flags = flags;
        this.faces = new int[faces.length];
        System.arraycopy(faces, 0, this.faces, 0, faces.length);
    }

    /**
     * Returns the animation ID.
     * @return the animation ID
     */
    public int getAnimationId() {
        return animationId;
    }

    /**
     * Returns the number of faces of this animation.
     * @return the number of faces
     */
    public int getFaces() {
        return faces.length;
    }

    /**
     * Returns one face of this animation.
     * @param index the face index
     * @return the face
     */
    public int getFace(final int index) {
        return faces[index];
    }
}
