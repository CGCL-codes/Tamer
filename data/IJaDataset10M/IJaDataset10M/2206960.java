package net.sf.gridarta.model.mapmodel;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.autojoin.AutojoinLists;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.GameObjectMatchers;
import org.jetbrains.annotations.NotNull;

/**
 * A factory for creating {@link MapModel} instances.
 * @author Andreas Kirschbaum
 */
public class MapModelFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link ArchetypeChooserModel} instance to use.
     */
    @NotNull
    private final ArchetypeChooserModel<G, A, R> archetypeChooserModel;

    /**
     * The {@link AutojoinLists} instance to use.
     */
    @NotNull
    private final AutojoinLists<G, A, R> autojoinLists;

    /**
     * The map view settings instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The {@link GameObjectFactory} for creating {@link GameObject
     * GameObjects}.
     */
    @NotNull
    private final GameObjectFactory<G, A, R> gameObjectFactory;

    /**
     * The {@link GameObjectMatchers} to use.
     */
    @NotNull
    private final GameObjectMatchers gameObjectMatchers;

    /**
     * The "topmost" {@link InsertionMode}.
     */
    @NotNull
    private final InsertionMode<G, A, R> topmostInsertionMode;

    /**
     * Creates a new instance.
     * @param archetypeChooserModel the archetype chooser model to use
     * @param autojoinLists the autojoin lists to use
     * @param mapViewSettings the map view settings instance
     * @param gameObjectFactory the game object factory for creating game
     * objects
     * @param gameObjectMatchers the game object matchers to use
     * @param topmostInsertionMode the "topmost" insertion mode
     */
    public MapModelFactory(@NotNull final ArchetypeChooserModel<G, A, R> archetypeChooserModel, @NotNull final AutojoinLists<G, A, R> autojoinLists, @NotNull final MapViewSettings mapViewSettings, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final GameObjectMatchers gameObjectMatchers, @NotNull final InsertionMode<G, A, R> topmostInsertionMode) {
        this.archetypeChooserModel = archetypeChooserModel;
        this.autojoinLists = autojoinLists;
        this.mapViewSettings = mapViewSettings;
        this.gameObjectFactory = gameObjectFactory;
        this.gameObjectMatchers = gameObjectMatchers;
        this.topmostInsertionMode = topmostInsertionMode;
    }

    /**
     * Creates a new {@link MapModel} instance.
     * @param mapArchObject the map arch object for the map model
     * @return the map model instance
     */
    @NotNull
    public MapModel<G, A, R> newMapModel(@NotNull final A mapArchObject) {
        return new DefaultMapModel<G, A, R>(autojoinLists, mapArchObject, archetypeChooserModel, mapViewSettings.getEditType(), gameObjectFactory, gameObjectMatchers, topmostInsertionMode);
    }
}
