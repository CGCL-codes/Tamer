package net.sf.gridarta.gui.map.viewaction;

import java.awt.Container;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.mapviewsettings.MapViewSettingsListener;
import net.sf.gridarta.model.match.NamedGameObjectMatcher;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages edit type actions in "view" menu.
 * @author Andreas Kirschbaum
 */
public class ViewActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The map view settings instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The view actions.
     */
    @NotNull
    private final List<ViewAction> viewActions = new LinkedList<ViewAction>();

    /**
     * The map manager listener which is attached to the current map if the
     * current map is tracked. Otherwise it is unused.
     */
    @NotNull
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            updateActions();
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
        }
    };

    /**
     * The {@link MapViewSettingsListener} attached to {@link #mapViewSettings}
     * to detect edit types changes.
     */
    @NotNull
    private final MapViewSettingsListener mapViewSettingsListener = new MapViewSettingsListener() {

        @Override
        public void gridVisibleChanged(final boolean gridVisible) {
        }

        @Override
        public void smoothingChanged(final boolean smoothing) {
        }

        @Override
        public void doubleFacesChanged(final boolean doubleFaces) {
        }

        @Override
        public void alphaTypeChanged(final int alphaType) {
        }

        @Override
        public void editTypeChanged(final int editType) {
            updateActions();
        }

        @Override
        public void autojoinChanged(final boolean autojoin) {
        }
    };

    /**
     * Create a new instance.
     * @param mapViewSettings the map view settings instance
     * @param mapManager the map manager
     */
    public ViewActions(@NotNull final MapViewSettings mapViewSettings, @NotNull final MapManager<G, A, R> mapManager) {
        this.mapViewSettings = mapViewSettings;
        ActionUtils.newAction(ACTION_BUILDER, "Map", this, "resetView");
        mapManager.addMapManagerListener(mapManagerListener);
        mapViewSettings.addMapViewSettingsListener(mapViewSettingsListener);
    }

    /**
     * The comparator for sorting menu entries.
     */
    @NotNull
    private static final Comparator<ViewAction> actionNameComparator = new Comparator<ViewAction>() {

        @Override
        public int compare(final ViewAction o1, final ViewAction o2) {
            return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
        }
    };

    /**
     * Creates the view actions.
     * @param gameObjectMatchers the game object matchers
     */
    public void init(@NotNull final Iterable<NamedGameObjectMatcher> gameObjectMatchers) {
        for (final NamedGameObjectMatcher matcher : gameObjectMatchers) {
            viewActions.add(new ViewAction(mapViewSettings, matcher));
        }
    }

    /**
     * Sets the menu to add the actions to.
     * @param viewActionsMenu the menu
     */
    public void setMenu(@Nullable final Container viewActionsMenu) {
        if (viewActionsMenu == null) {
            return;
        }
        final ViewAction[] actions = viewActions.toArray(new ViewAction[viewActions.size()]);
        Arrays.sort(actions, actionNameComparator);
        int index = 0;
        for (final ViewAction viewAction : actions) {
            viewActionsMenu.add(viewAction.getCheckBoxMenuItem(), index++);
        }
    }

    /**
     * Action method for "reset view".
     */
    @ActionMethod
    public void resetView() {
        mapViewSettings.unsetEditType(~0);
    }

    /**
     * Update the actions' state to the current edit mode.
     */
    private void updateActions() {
        for (final ViewAction viewAction : viewActions) {
            viewAction.updateAction();
        }
    }
}
