package net.sf.gridarta.var.atrinik.model.archetype;

import net.sf.gridarta.model.archetype.AbstractArchetypeBuilder;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link AbstractArchetypeBuilder} for Daimonin archetypes.
 * @author Andreas Kirschbaum
 */
public class DefaultArchetypeBuilder extends AbstractArchetypeBuilder<GameObject, MapArchObject, Archetype> {

    /**
     * Creates a new instance.
     * @param gameObjectFactory the game object factory for creating the new
     * archetype
     */
    public DefaultArchetypeBuilder(@NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory) {
        super(gameObjectFactory);
    }

    public void setMultiPartNr(final int multiPartNr) {
        getArchetype().setMultiPartNr(multiPartNr);
    }
}
