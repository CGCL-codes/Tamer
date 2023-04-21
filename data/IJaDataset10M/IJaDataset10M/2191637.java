package net.sf.gridarta.var.daimonin.model.archetype;

import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.AbstractArchetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.var.daimonin.model.gameobject.GameObject;
import net.sf.gridarta.var.daimonin.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * The class <code>UndefinedArchetype</code> implements an undefined Daimonin
 * archetype.
 * @author Andreas Kirschbaum
 */
public class UndefinedArchetype extends AbstractArchetype<GameObject, MapArchObject, Archetype> implements Archetype {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new undefined archetype with the given archetype name.
     * @param archetypeName the archetype name
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     */
    public UndefinedArchetype(@NotNull final String archetypeName, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        super(archetypeName, faceObjectProviders, animationObjects);
        setEditorFolder(net.sf.gridarta.model.gameobject.GameObject.EDITOR_FOLDER_INTERN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUndefinedArchetype() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public UndefinedArchetype clone() {
        return (UndefinedArchetype) super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean usesDirection() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected Archetype getThis() {
        return this;
    }
}
