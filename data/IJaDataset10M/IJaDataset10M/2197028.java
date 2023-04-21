package net.sf.gridarta.gui.panel.connectionview;

import java.io.Serializable;
import java.util.Comparator;
import net.sf.gridarta.gui.delayedmapmodel.DelayedMapModelListenerManager;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import org.jetbrains.annotations.NotNull;

/**
 * The view of the connection view control. It holds information about the
 * connections of the selected connection value on the selected map.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class MonsterView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends View<GameObject<G, A, R>, G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link GameObjectMatcher} for matching monster objects.
     */
    @NotNull
    private final GameObjectMatcher monsterMatcher;

    /**
     * Create a MonsterView.
     * @param mapViewManager the map view manager
     * @param delayedMapModelListenerManager the delayed map model listener
     * manager to use
     * @param monsterMatcher the <code>GameObjectMatcher</code> for matching
     * monster objects
     */
    public MonsterView(@NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final DelayedMapModelListenerManager<G, A, R> delayedMapModelListenerManager, @NotNull final GameObjectMatcher monsterMatcher) {
        super(new MonsterComparator<G, A, R>(), new MonsterCellRenderer<G, A, R>(), mapViewManager, delayedMapModelListenerManager);
        this.monsterMatcher = monsterMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void scanGameObjectForConnections(@NotNull final G gameObject) {
        scanGameObject(gameObject);
        for (final GameObject<G, A, R> invObject : gameObject.recursive()) {
            scanGameObject(invObject);
        }
    }

    /**
     * Add the given game object as a connection if it is a monster object.
     * @param gameObject the game object to process
     */
    private void scanGameObject(@NotNull final GameObject<G, A, R> gameObject) {
        if (gameObject.isHead() && monsterMatcher.isMatching(gameObject)) {
            addConnection(gameObject, gameObject);
        }
    }

    /**
     * A {@link Comparator} for ordering the values of this view.
     */
    private static class MonsterComparator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Comparator<GameObject<G, A, R>>, Serializable {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(@NotNull final GameObject<G, A, R> o1, @NotNull final GameObject<G, A, R> o2) {
            final int level1 = o1.getAttributeInt(BaseObject.LEVEL);
            final int level2 = o2.getAttributeInt(BaseObject.LEVEL);
            if (level1 < level2) {
                return +1;
            }
            if (level1 > level2) {
                return -1;
            }
            final String name1 = o1.getBestName();
            final String name2 = o2.getBestName();
            return String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
        }
    }
}
