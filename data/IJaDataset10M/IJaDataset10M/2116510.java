package net.sf.gridarta.model.gameobject;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for a {@link GameObjectFactory} that creates {@link
 * GameObject GameObjects}.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractIsoGameObjectFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractGameObjectFactory<G, A, R> {

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public G cloneGameObject(@NotNull final G gameObject) {
        return gameObject.clone();
    }
}
