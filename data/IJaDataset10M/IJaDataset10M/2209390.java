package net.sf.gridarta.gui.map.mapactions;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.gui.dialog.mapproperties.MapPropertiesDialogFactory;
import net.sf.gridarta.gui.dialog.shrinkmapsize.ShrinkMapSizeDialogManager;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareModel;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.exitconnector.ExitMatcher;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectListener;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursorListener;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.FilterGameObjectIterator;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapmodel.TopLevelGameObjectIterator;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.mapviewsettings.MapViewSettingsListener;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.action.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages actions in the "map" menu.
 * @author Andreas Kirschbaum
 */
public class MapActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Possible directions for "enter xxx map".
     */
    @NotNull
    private final String[] directionsMap = { "enterNorthMap", "enterEastMap", "enterSouthMap", "enterWestMap", "enterNorthEastMap", "enterSouthEastMap", "enterSouthWestMap", "enterNorthWestMap" };

    /**
     * Action for "grid visible".
     */
    @NotNull
    private final ToggleAction aGridVisible = (ToggleAction) ACTION_BUILDER.createToggle(true, "gridVisible", this);

    /**
     * Action for "smoothing".
     */
    @NotNull
    private final ToggleAction aSmoothing = (ToggleAction) ACTION_BUILDER.createToggle(true, "smoothing", this);

    /**
     * Action for "draw double faces".
     */
    @NotNull
    private final ToggleAction aDoubleFaces = (ToggleAction) ACTION_BUILDER.createToggle(true, "doubleFaces", this);

    /**
     * Action for "tile show".
     */
    @NotNull
    private final ToggleAction aTileShow = (ToggleAction) ACTION_BUILDER.createToggle(true, "tileShow", this);

    /**
     * Action for "create view".
     */
    @NotNull
    private final Action aMapCreateView = ActionUtils.newAction(ACTION_BUILDER, "Map,Window", this, "mapCreateView");

    /**
     * Action for "map properties".
     */
    @NotNull
    private final Action aMapProperties = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "mapProperties");

    /**
     * Action for "autojoin".
     */
    @NotNull
    private final ToggleAction aAutoJoin = (ToggleAction) ACTION_BUILDER.createToggle(true, "autoJoin", this);

    /**
     * Action for "shrink map size".
     */
    @NotNull
    private final Action aShrinkMapSize = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "shrinkMapSize");

    /**
     * Action for "enter exit".
     */
    @NotNull
    private final Action aEnterExit = ActionUtils.newAction(ACTION_BUILDER, "Map Navigation", this, "enterExit");

    /**
     * Action for "next exit".
     */
    @NotNull
    private final Action aNextExit = ActionUtils.newAction(ACTION_BUILDER, "Map Navigation", this, "nextExit");

    /**
     * Action for "previous exit".
     */
    @NotNull
    private final Action aPrevExit = ActionUtils.newAction(ACTION_BUILDER, "Map Navigation", this, "prevExit");

    /**
     * Action for "delete unknown objects".
     */
    @NotNull
    private final Action aDeleteUnknownObjects = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "deleteUnknownObjects");

    /**
     * Action for "enter xxx map".
     */
    @NotNull
    private final Action[] aDirections = new Action[directionsMap.length];

    {
        for (int i = 0; i < directionsMap.length; i++) {
            aDirections[i] = ActionUtils.newAction(ACTION_BUILDER, "Map Navigation", this, directionsMap[i]);
        }
    }

    /**
     * The parent frame for help windows.
     */
    @NotNull
    private final JFrame helpParent;

    /**
     * The {@link ExitMatcher} for selecting exit game objects.
     */
    @NotNull
    private final ExitMatcher<G, A, R> exitMatcher;

    /**
     * The Swing file filter to use.
     */
    @NotNull
    private final FileFilter mapFileFilter;

    /**
     * The {@link SelectedSquareModel} to use.
     */
    @NotNull
    private final SelectedSquareModel<G, A, R> selectedSquareModel;

    /**
     * The {@link EnterMap} instance to use.
     */
    @NotNull
    private final EnterMap<G, A, R> enterMap;

    /**
     * The {@link ShrinkMapSizeDialogManager} instance.
     */
    @NotNull
    private final ShrinkMapSizeDialogManager<G, A, R> shrinkMapSizeDialogManager;

    /**
     * Whether exit paths may point to random maps.
     */
    private final boolean allowRandomMapParameters;

    /**
     * The {@link MapPropertiesDialogFactory} to use.
     */
    @NotNull
    private final MapPropertiesDialogFactory<G, A, R> mapPropertiesDialogFactory;

    /**
     * The {@link MapViewSettings} instance to use.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The current map view, or <code>null</code> if no map view is active.
     */
    @Nullable
    private MapView<G, A, R> currentMapView;

    /**
     * The {@link MapViewSettingsListener} attached to {@link
     * #mapViewSettings}.
     */
    @NotNull
    private final MapViewSettingsListener mapViewSettingsListener = new MapViewSettingsListener() {

        @Override
        public void gridVisibleChanged(final boolean gridVisible) {
            updateActions();
        }

        @Override
        public void smoothingChanged(final boolean smoothing) {
            updateActions();
        }

        @Override
        public void doubleFacesChanged(final boolean doubleFaces) {
            updateActions();
        }

        @Override
        public void alphaTypeChanged(final int alphaType) {
        }

        @Override
        public void editTypeChanged(final int editType) {
        }

        @Override
        public void autojoinChanged(final boolean autojoin) {
            updateActions();
        }
    };

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
     * The map view manager listener which is attached to the current map if the
     * current map is tracked. Otherwise it is unused.
     */
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

        @Override
        public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
            if (currentMapView != null) {
                currentMapView.getMapCursor().removeMapCursorListener(mapCursorListener);
                assert currentMapView != null;
                currentMapView.getMapControl().getMapModel().getMapArchObject().removeMapArchObjectListener(mapArchObjectListener);
            }
            currentMapView = mapView;
            if (currentMapView != null) {
                currentMapView.getMapCursor().addMapCursorListener(mapCursorListener);
                assert currentMapView != null;
                currentMapView.getMapControl().getMapModel().getMapArchObject().addMapArchObjectListener(mapArchObjectListener);
            }
            updateActions();
        }

        @Override
        public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
        }

        @Override
        public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
        }
    };

    /**
     * The map cursor listener which is attached to {@link #currentMapView}.
     */
    @NotNull
    private final MapCursorListener<G, A, R> mapCursorListener = new MapCursorListener<G, A, R>() {

        @Override
        public void mapCursorChangedPos(@Nullable final Point location) {
            updateActions();
        }

        @Override
        public void mapCursorChangedMode() {
        }

        @Override
        public void mapCursorChangedGameObject(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
        }
    };

    /**
     * The {@link MapArchObjectListener} attached to {@link #currentMapView}.
     */
    @NotNull
    private final MapArchObjectListener mapArchObjectListener = new MapArchObjectListener() {

        @Override
        public void mapMetaChanged() {
            updateActions();
        }

        @Override
        public void mapSizeChanged(@NotNull final Size2D mapSize) {
        }
    };

    /**
     * Create a new instance.
     * @param helpParent the parent frame for help windows
     * @param mapManager the map manager
     * @param mapViewManager the map view manager
     * @param exitMatcher the exit matcher for selecting exit game objects
     * @param mapFileFilter the Swing file filter to use
     * @param selectedSquareModel the selected square model to use
     * @param allowRandomMapParameters whether exit paths may point to random
     * maps
     * @param mapPropertiesDialogFactory the map properties dialog factory to
     * use
     * @param mapViewSettings the map view settings instance to use
     * @param mapViewsManager the map views
     * @param enterMap the enter map instance to use
     */
    public MapActions(@NotNull final JFrame helpParent, @NotNull final MapManager<G, A, R> mapManager, @NotNull final MapViewManager<G, A, R> mapViewManager, final ExitMatcher<G, A, R> exitMatcher, final FileFilter mapFileFilter, @NotNull final SelectedSquareModel<G, A, R> selectedSquareModel, final boolean allowRandomMapParameters, @NotNull final MapPropertiesDialogFactory<G, A, R> mapPropertiesDialogFactory, @NotNull final MapViewSettings mapViewSettings, @NotNull final MapViewsManager<G, A, R> mapViewsManager, @NotNull final EnterMap<G, A, R> enterMap) {
        this.helpParent = helpParent;
        this.exitMatcher = exitMatcher;
        this.mapFileFilter = mapFileFilter;
        this.selectedSquareModel = selectedSquareModel;
        this.enterMap = enterMap;
        this.allowRandomMapParameters = allowRandomMapParameters;
        this.mapPropertiesDialogFactory = mapPropertiesDialogFactory;
        this.mapViewSettings = mapViewSettings;
        this.mapViewsManager = mapViewsManager;
        shrinkMapSizeDialogManager = new ShrinkMapSizeDialogManager<G, A, R>(mapViewManager);
        mapViewSettings.addMapViewSettingsListener(mapViewSettingsListener);
        mapManager.addMapManagerListener(mapManagerListener);
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
        currentMapView = mapViewManager.getActiveMapView();
        if (currentMapView != null) {
            currentMapView.getMapCursor().addMapCursorListener(mapCursorListener);
            assert currentMapView != null;
            currentMapView.getMapControl().getMapModel().getMapArchObject().addMapArchObjectListener(mapArchObjectListener);
        }
        updateActions();
    }

    /**
     * Action method for "grid visible".
     * @return <code>true</code> if the grid is visible, or <code>false</code>
     *         if the grid is invisible
     */
    @ActionMethod
    public boolean isGridVisible() {
        return doGridVisible(false, false) && mapViewSettings.isGridVisible();
    }

    /**
     * Sets whether the grid of the current map should be visible.
     * @param gridVisible new visibility of grid in current map,
     * <code>true</code> if the grid should be visible, <code>false</code> if
     * invisible
     * @see #isGridVisible()
     */
    @ActionMethod
    public void setGridVisible(final boolean gridVisible) {
        doGridVisible(true, gridVisible);
    }

    /**
     * Action method for "smoothing".
     * @return <code>true</code> if smoothing is active, or <code>false</code>
     *         if smoothing is disabled
     */
    @ActionMethod
    public boolean isSmoothing() {
        return doSmoothing(false, false) && mapViewSettings.isSmoothing();
    }

    /**
     * Sets whether smoothing of the current map is active.
     * @param smoothing new smoothing in current map, <code>true</code> if the
     * smoothing should be active, <code>false</code> if deactivated
     * @see #isSmoothing() ()
     */
    @ActionMethod
    public void setSmoothing(final boolean smoothing) {
        doSmoothing(true, smoothing);
    }

    /**
     * Action method for "double faces".
     * @return <code>true</code> if double faces are shown, or
     *         <code>false</code> if not
     */
    @ActionMethod
    public boolean isDoubleFaces() {
        return doDoubleFaces(false, false) && mapViewSettings.isDoubleFaces();
    }

    /**
     * Sets whether double faces on the current map should be shown.
     * @param doubleFaces new value
     * @see #isDoubleFaces()
     */
    @ActionMethod
    public void setDoubleFaces(final boolean doubleFaces) {
        doDoubleFaces(true, doubleFaces);
    }

    /**
     * Action method for "tile show".
     * @return <code>true</code> if adjacent tiles are shown, or
     *         <code>false</code> if not
     * @noinspection MethodMayBeStatic, SameReturnValue
     */
    @ActionMethod
    public boolean isTileShow() {
        return false;
    }

    /**
     * Action method for "tile show".
     * @param tileShow if set, show adjacent tiles
     */
    @ActionMethod
    public void setTileShow(final boolean tileShow) {
        doTileShow(true, tileShow);
    }

    /**
     * Action method for "create view".
     */
    @ActionMethod
    public void mapCreateView() {
        doMapCreateView(true);
    }

    /**
     * Action method for "map properties".
     */
    @ActionMethod
    public void mapProperties() {
        doMapProperties(true);
    }

    /**
     * Action method for "shrink map size".
     */
    @ActionMethod
    public void shrinkMapSize() {
        doShrinkMapSize(true);
    }

    /**
     * Action method for "autojoin".
     * @return <code>true</code> if autojoining is enabled, or
     *         <code>false</code> if autojoining is disabled
     */
    @ActionMethod
    public boolean isAutoJoin() {
        return doAutoJoin(false, false) && mapViewSettings.isAutojoin();
    }

    /**
     * Action method for "autoJoin".
     * @param autoJoin if set, enable autojoining
     */
    @ActionMethod
    public void setAutoJoin(final boolean autoJoin) {
        doAutoJoin(true, autoJoin);
    }

    /**
     * Try to load the map where the selected map-exit points to.
     */
    @ActionMethod
    public void enterExit() {
        doEnterExit(true);
    }

    /**
     * Select the next exit.
     */
    @ActionMethod
    public void nextExit() {
        doNextExit(true);
    }

    /**
     * Select the previous exit.
     */
    @ActionMethod
    public void prevExit() {
        doPrevExit(true);
    }

    /**
     * Selects an exit square.
     * @param mapView the map view to operate on
     * @param direction the direction to search
     */
    private void selectExit(@NotNull final MapView<G, A, R> mapView, final int direction) {
        final TopLevelGameObjectIterator<G, A, R> gameObjectIterator = new TopLevelGameObjectIterator<G, A, R>(mapView.getMapControl().getMapModel(), mapView.getMapCursor().getLocation(), direction, true);
        final Iterator<G> exitIterator = new FilterGameObjectIterator<G, A, R>(gameObjectIterator, exitMatcher);
        if (exitIterator.hasNext()) {
            final G exit = exitIterator.next();
            final MapSquare<G, A, R> mapSquare = exit.getMapSquare();
            assert mapSquare != null;
            mapView.setCursorLocation(mapSquare.getMapLocation());
        } else {
            mapView.setCursorLocation(null);
        }
    }

    /**
     * Action method for entering the north map.
     */
    @ActionMethod
    public void enterNorthMap() {
        doEnterMap(true, Direction.NORTH);
    }

    /**
     * Action method for entering the north east map.
     */
    @ActionMethod
    public void enterNorthEastMap() {
        doEnterMap(true, Direction.NORTH_EAST);
    }

    /**
     * Action method for entering the east map.
     */
    @ActionMethod
    public void enterEastMap() {
        doEnterMap(true, Direction.EAST);
    }

    /**
     * Action method for entering the south east map.
     */
    @ActionMethod
    public void enterSouthEastMap() {
        doEnterMap(true, Direction.SOUTH_EAST);
    }

    /**
     * Action method for entering the south map.
     */
    @ActionMethod
    public void enterSouthMap() {
        doEnterMap(true, Direction.SOUTH);
    }

    /**
     * Action method for entering the south west map.
     */
    @ActionMethod
    public void enterSouthWestMap() {
        doEnterMap(true, Direction.SOUTH_WEST);
    }

    /**
     * Action method for entering the west map.
     */
    @ActionMethod
    public void enterWestMap() {
        doEnterMap(true, Direction.WEST);
    }

    /**
     * Action method for entering the north west map.
     */
    @ActionMethod
    public void enterNorthWestMap() {
        doEnterMap(true, Direction.NORTH_WEST);
    }

    /**
     * Deletes all game objects referencing unknown archetypes.
     */
    @ActionMethod
    public void deleteUnknownObjects() {
        doDeleteUnknownObjects(true);
    }

    /**
     * Enters a map.
     * @param mapFile the map file to enter
     * @param destinationPoint the desired destination point on the map or
     * <code>null</code> for default
     */
    public void enterMap(@NotNull final File mapFile, @Nullable final Point destinationPoint) {
        enterMap.enterMap(currentMapView, mapFile, destinationPoint, Direction.NORTH);
    }

    /**
     * Update the actions' state.
     */
    private void updateActions() {
        aGridVisible.setEnabled(doGridVisible(false, false));
        aGridVisible.setSelected(isGridVisible());
        aSmoothing.setEnabled(doSmoothing(false, false));
        aSmoothing.setSelected(isSmoothing());
        aDoubleFaces.setEnabled(doDoubleFaces(false, false));
        aDoubleFaces.setSelected(isDoubleFaces());
        aTileShow.setEnabled(doTileShow(false, false));
        aTileShow.setSelected(isTileShow());
        aMapCreateView.setEnabled(doMapCreateView(false));
        aMapProperties.setEnabled(doMapProperties(false));
        aAutoJoin.setEnabled(doAutoJoin(false, false));
        aAutoJoin.setSelected(isAutoJoin());
        aEnterExit.setEnabled(doEnterExit(false));
        aNextExit.setEnabled(doNextExit(false));
        aPrevExit.setEnabled(doPrevExit(false));
        aDeleteUnknownObjects.setEnabled(doDeleteUnknownObjects(false));
        for (final Direction direction : Direction.values()) {
            aDirections[direction.ordinal()].setEnabled(doEnterMap(false, direction));
        }
        aShrinkMapSize.setEnabled(doShrinkMapSize(false));
    }

    /**
     * Executes the "grid visible" action.
     * @param performAction whether the action should be performed
     * @param gridVisible whether the grid should be visible; ignored unless
     * <code>performAction</code> is set
     * @return whether the action was or can be performed
     * @noinspection SameReturnValue
     */
    private boolean doGridVisible(final boolean performAction, final boolean gridVisible) {
        if (performAction) {
            mapViewSettings.setGridVisible(gridVisible);
        }
        return true;
    }

    /**
     * Executes the "smoothing" action.
     * @param performAction whether the action should be performed
     * @param smoothing whether smoothing should be performed; ignored unless
     * <code>performAction</code> is set
     * @return whether the action was or can be performed
     */
    private boolean doSmoothing(final boolean performAction, final boolean smoothing) {
        if (performAction) {
            mapViewSettings.setSmoothing(smoothing);
        }
        return true;
    }

    /**
     * Executes the "double faces" action.
     * @param performAction whether the action should be performed
     * @param doubleFaces whether double faces should be shown; ignored unless
     * <code>performAction</code> is set
     * @return whether the action was or can be performed
     * @noinspection SameReturnValue
     */
    private boolean doDoubleFaces(final boolean performAction, final boolean doubleFaces) {
        if (performAction) {
            mapViewSettings.setDoubleFaces(doubleFaces);
        }
        return true;
    }

    /**
     * Executes the "tile show" action.
     * @param performAction whether the action should be performed
     * @param tileShow whether adjacent tiles should be shown; ignored unless
     * <code>performAction</code> is set
     * @return whether the action was or can be performed
     * @noinspection MethodMayBeStatic, UnusedDeclaration, SameReturnValue
     */
    private boolean doTileShow(final boolean performAction, final boolean tileShow) {
        return false;
    }

    /**
     * Executes the "map create view" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doMapCreateView(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }
        if (performAction) {
            mapViewsManager.newMapView(mapView.getMapControl(), mapView.getScrollPane().getViewport().getViewPosition(), null);
        }
        return true;
    }

    /**
     * Executes the "map properties" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doMapProperties(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }
        if (performAction) {
            mapPropertiesDialogFactory.showDialog(mapView.getComponent(), helpParent, mapView.getMapControl().getMapModel(), mapFileFilter);
        }
        return true;
    }

    /**
     * Executes the "auto join" action.
     * @param performAction whether the action should be performed
     * @param autoJoin whether autojoin should be enabled; ignored unless
     * <code>performAction</code> is set
     * @return whether the action was or can be performed
     * @noinspection SameReturnValue
     */
    private boolean doAutoJoin(final boolean performAction, final boolean autoJoin) {
        if (performAction) {
            mapViewSettings.setAutojoin(autoJoin);
        }
        return true;
    }

    /**
     * Executes the "enter exit" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doEnterExit(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }
        final GameObject<G, A, R> exit;
        final GameObject<G, A, R> selectedExit = exitMatcher.getValidExit(selectedSquareModel.getSelectedGameObject());
        if (selectedExit == null) {
            final GameObject<G, A, R> cursorExit = exitMatcher.getValidExit(mapView.getMapControl().getMapModel(), mapView.getMapCursor().getLocation());
            if (cursorExit == null) {
                return false;
            }
            exit = cursorExit;
        } else {
            exit = selectedExit;
        }
        if (performAction) {
            if (!enterMap.enterExit(mapView, exit, allowRandomMapParameters)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Executes the "next exit" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doNextExit(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }
        if (performAction) {
            selectExit(mapView, 1);
        }
        return true;
    }

    /**
     * Executes the "prev exit" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doPrevExit(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }
        if (performAction) {
            selectExit(mapView, -1);
        }
        return true;
    }

    /**
     * Executes the "delete unknown objects" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doDeleteUnknownObjects(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }
        if (performAction) {
            final MapControl<G, A, R> mapControl = mapView.getMapControl();
            final MapModel<G, A, R> mapModel = mapControl.getMapModel();
            final Collection<G> gameObjectsToDelete = new ArrayList<G>();
            for (final Iterable<G> mapSquare : mapModel) {
                for (final G gameObject : mapSquare) {
                    if (gameObject.isHead() && gameObject.hasUndefinedArchetype()) {
                        gameObjectsToDelete.add(gameObject);
                    }
                }
            }
            if (!gameObjectsToDelete.isEmpty()) {
                mapModel.beginTransaction("delete undefined objects");
                try {
                    for (final G gameObject : gameObjectsToDelete) {
                        mapModel.removeGameObject(gameObject, false);
                    }
                } finally {
                    mapModel.endTransaction();
                }
            }
        }
        return true;
    }

    /**
     * Executes the "enter map" action.
     * @param performAction whether the action should be performed
     * @param direction the direction to enter
     * @return whether the action was or can be performed
     */
    private boolean doEnterMap(final boolean performAction, @NotNull final Direction direction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }
        final String path = mapView.getMapControl().getMapModel().getMapArchObject().getTilePath(direction);
        if (path.length() == 0) {
            return false;
        }
        if (performAction) {
            if (!enterMap.enterMap(mapView, path, direction, null)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Executes the "shrink map size" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doShrinkMapSize(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }
        if (performAction) {
            shrinkMapSizeDialogManager.showDialog(mapView);
        }
        return true;
    }
}
