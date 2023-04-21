package net.sf.gridarta.model.anim;

import net.sf.gridarta.model.data.NamedObjects;
import org.jetbrains.annotations.NotNull;

/**
 * AnimationObjects is a container for {@link AnimationObject
 * AnimationObjects}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface AnimationObjects extends NamedObjects<AnimationObject> {

    /**
     * Add an animation object.
     * @param animName name of the animation object
     * @param list list with individual frames
     * @param path the path for the animation object
     * @throws DuplicateAnimationException in case the animation was not unique
     * @throws IllegalAnimationException if the animation cannot be added
     */
    void addAnimationObject(@NotNull String animName, @NotNull String list, @NotNull String path) throws DuplicateAnimationException, IllegalAnimationException;
}
