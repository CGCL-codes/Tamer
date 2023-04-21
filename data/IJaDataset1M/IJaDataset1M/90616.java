package net.sf.gridarta.gui.dialog.goexit;

import java.awt.Window;
import net.sf.gridarta.gui.map.mapactions.EnterMap;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

/**
 * Manager for {@link GoExitDialog} instances.
 * @author Andreas Kirschbaum
 */
public class GoExitDialogManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The parent {@link Window} for go map dialogs.
     */
    @NotNull
    private final Window parent;

    /**
     * The map manager to use.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link MapViewManager} instance.
     */
    @NotNull
    private final MapViewManager<G, A, R> mapViewManager;

    /**
     * The {@link GameObjectMatcher} for selecting exits.
     */
    @NotNull
    private final GameObjectMatcher exitGameObjectMatcher;

    /**
     * The {@link PathManager} for converting relative exit paths.
     */
    @NotNull
    private final PathManager pathManager;

    /**
     * The {@link EnterMap} instance to use.
     */
    @NotNull
    private final EnterMap<G, A, R> enterMap;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * Creates a new instance.
     * @param parent the parent window for go map dialogs
     * @param mapManager the map manager to use
     * @param mapViewManager the map view manager instance
     * @param exitGameObjectMatcher the game object matcher for selecting exits
     * @param pathManager the path manager for converting relative exit paths
     * @param enterMap the enter map instance to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public GoExitDialogManager(@NotNull final Window parent, @NotNull final MapManager<G, A, R> mapManager, @NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final GameObjectMatcher exitGameObjectMatcher, @NotNull final PathManager pathManager, @NotNull final EnterMap<G, A, R> enterMap, @NotNull final FaceObjectProviders faceObjectProviders) {
        this.parent = parent;
        this.mapManager = mapManager;
        this.mapViewManager = mapViewManager;
        this.exitGameObjectMatcher = exitGameObjectMatcher;
        this.pathManager = pathManager;
        this.enterMap = enterMap;
        this.faceObjectProviders = faceObjectProviders;
    }

    /**
     * Action method to open the "go exit" dialog.
     */
    @ActionMethod
    public void goExit() {
        final MapControl<G, A, R> mapControl = mapManager.getCurrentMap();
        if (mapControl == null) {
            return;
        }
        final MapView<G, A, R> mapView = mapViewManager.getActiveMapView();
        if (mapView == null) {
            return;
        }
        new GoExitDialog<G, A, R>(parent, mapView, exitGameObjectMatcher, pathManager, enterMap, faceObjectProviders).showDialog();
    }
}
