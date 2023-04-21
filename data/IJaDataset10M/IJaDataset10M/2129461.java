package net.sf.gridarta.var.atrinik.model.archetype;

import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.ArchetypeFactory;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ArchetypeFactory} creating Atrinik objects.
 * @author Andreas Kirschbaum
 */
public class DefaultArchetypeFactory implements ArchetypeFactory<GameObject, MapArchObject, Archetype> {

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link AnimationObjects} for looking up animations.
     */
    @NotNull
    private final AnimationObjects animationObjects;

    /**
     * Creates a new instance.
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     */
    public DefaultArchetypeFactory(@NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        this.faceObjectProviders = faceObjectProviders;
        this.animationObjects = animationObjects;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Archetype newUndefinedArchetype(@NotNull final String archetypeName) {
        return new UndefinedArchetype(archetypeName, faceObjectProviders, animationObjects);
    }
}
