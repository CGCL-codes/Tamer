package net.sf.gridarta.gui.panel.gameobjectattributes;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.Nullable;

/**
 * Listener for {@link GameObjectAttributesModel} related events.
 * @author Andreas Kirschbaum
 */
public interface GameObjectAttributesModelListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Called whenever the selected game object has changed.
     * @param selectedGameObject the selected game object
     */
    void selectedGameObjectChanged(@Nullable G selectedGameObject);

    /**
     * Called whenever the selected game object's attributes may have changed.
     */
    void refreshSelectedGameObject();
}
