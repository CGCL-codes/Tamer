package org.freelords.animation.uinterface;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.freelords.actions.ContextAction;
import org.freelords.actions.ContextActionProvider;
import org.freelords.actions.ContextTree;
import org.freelords.armies.Army;
import org.freelords.client.Client;
import org.freelords.entity.FreelordsMapEntity;
import org.freelords.forms.map.GameMainForm;
import org.freelords.forms.map.MainMapControl;
import org.freelords.forms.map.View;
import org.freelords.game.Game;
import org.freelords.main.FreelordsSelectionListener;
import org.freelords.main.Selection;
import org.freelords.map.TileSelection;
import org.freelords.map.paths.Path;
import org.freelords.map.paths.Route;
import org.freelords.map.paths.StackPathCalculator;
import org.freelords.player.PlayerId;
import org.freelords.server.remote.ServerArmiesHandler;
import org.freelords.util.MouseButtons;
import org.freelords.util.EventProperty;
import org.freelords.util.ResourceStringConversions;
import org.freelords.util.geom.Point;
import org.freelords.xml.XMLHelper;

public class MainMapHandler implements MouseListener, MouseMoveListener, MenuDetectListener, SelectionListener, FreelordsSelectionListener {

    /** the game main form */
    private GameMainForm gmf;

    /** a client */
    private Client client;

    /** a game map */
    private MainMapControl mainMapControl;

    /** the current tile we are over */
    private Point tileOver;

    /** the current selected path */
    private Path path;

    /** the calculator to get the path as we move the mouse over the screen. */
    private StackPathCalculator pathCalc;

    /** The xml helper */
    private XMLHelper helper;

    /** The resource bundle */
    private ResourceBundle rb;

    /** Inits the listener */
    public MainMapHandler(GameMainForm gmf, Client client, XMLHelper helper, ResourceBundle rb) {
        this.gmf = gmf;
        this.client = client;
        this.helper = helper;
        this.rb = rb;
        gmf.getSelection().addListener(this);
    }

    /** Attaches listeners to a canvas */
    public void setMainMapControl(MainMapControl mainMapControl) {
        this.mainMapControl = mainMapControl;
    }

    /** Builds the context menu containing all possible context actions */
    public void menuDetected(MenuDetectEvent e) {
        Menu menu = mainMapControl.getCanvas().getMenu();
        if (menu != null) {
            mainMapControl.getCanvas().setMenu(null);
            menu.dispose();
        }
        Game game = client.getGame();
        if (tileOver != null) {
            PlayerId activePlayer = gmf.getActivePlayer();
            TileSelection ts = game.getTileSelection(tileOver);
            if (ts.getOwner() == activePlayer) {
                ContextTree tree = ContextActionProvider.getContextTree(client, ts);
                if (tree.getGroupActions().isEmpty()) {
                    return;
                }
                final Menu popup = new Menu(mainMapControl.getCanvas());
                for (Map.Entry<ContextAction, Set<FreelordsMapEntity>> entry : tree.getGroupActions().entrySet()) {
                    MenuItem mi = new MenuItem(popup, SWT.PUSH);
                    mi.setText(entry.getKey().getDescription(rb));
                    mi.setData("action", entry.getKey());
                    mi.setData("target", entry.getValue());
                    mi.setEnabled(entry.getKey().isAvailable());
                    mi.addSelectionListener(this);
                }
                Map<FreelordsMapEntity, List<ContextAction>> actionsPerEntity = tree.getEntitiesToActions();
                if (tree.getEntitiesToActions().size() > 1) {
                    new MenuItem(popup, SWT.SEPARATOR);
                    for (FreelordsMapEntity entity : ts.getAll()) {
                        if (entity.getPlayer() == activePlayer) {
                            List<ContextAction> actions = actionsPerEntity.get(entity);
                            MenuItem entityMi = new MenuItem(popup, SWT.CASCADE);
                            String name;
                            if (entity instanceof Army) {
                                name = ResourceStringConversions.getUnitTitle(entity.getCategory(), helper);
                            } else {
                                name = entity.getCategory();
                            }
                            entityMi.setText(name);
                            Menu entityMenu = new Menu(entityMi);
                            entityMi.setMenu(entityMenu);
                            if (actions == null || actions.isEmpty()) {
                                entityMenu.setEnabled(false);
                            } else {
                                Set<FreelordsMapEntity> entities = Collections.singleton(entity);
                                for (ContextAction ca : actions) {
                                    MenuItem actionMi = new MenuItem(entityMenu, SWT.PUSH);
                                    actionMi.setText(ca.getDescription(rb));
                                    actionMi.setData("action", ca);
                                    actionMi.setData("target", entities);
                                    actionMi.addSelectionListener(this);
                                }
                            }
                        }
                    }
                }
                mainMapControl.getCanvas().setMenu(popup);
            }
        }
    }

    /** Default selection in the control..? is not processed by us */
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    /** A selection occurred (list item marked, mouse pressed/released, .. */
    @SuppressWarnings("unchecked")
    public void widgetSelected(SelectionEvent e) {
        ContextAction action = (ContextAction) e.widget.getData("action");
        Set<FreelordsMapEntity> entities = (Set<FreelordsMapEntity>) e.widget.getData("target");
        action.run(client, entities);
    }

    /** Double mouse clicks are not supported for now. */
    public void mouseDoubleClick(MouseEvent e) {
    }

    /** Mouse button is pressed */
    public void mouseDown(MouseEvent e) {
        if (gmf.isLocked()) {
            return;
        }
        View view = mainMapControl.getView();
        if (view == null) {
            return;
        }
        Game game = client.getGame();
        int mouseButton = MouseButtons.getVirtualMouseButton(e);
        if (mouseButton == 3) {
        } else if (mouseButton > 1) {
            Point tile = view.relWindowToTile(new Point(e.x, e.y));
            if (!tile.equals(gmf.getSelection().getTileSelection().getPoint())) {
                gmf.getSelection().clear();
                path = null;
                ((Control) e.widget).redraw();
            }
        } else if (view != null && game != null) {
            Point tile = view.relWindowToTile(new Point(e.x, e.y));
            if (tile != null) {
                TileSelection ts = game.getTileSelection(tile);
                Collection<FreelordsMapEntity> focusedSelect = gmf.getSelection().getFocusedSelection();
                Set<Army> entityIds = new HashSet<Army>();
                for (FreelordsMapEntity fme : focusedSelect) {
                    if (fme instanceof Army) {
                        entityIds.add((Army) fme);
                    }
                }
                if (ts.getPoint().equals(gmf.getSelection().getTileSelection().getPoint())) {
                    gmf.getSelection().clear();
                    ((Control) e.widget).redraw();
                } else if (!entityIds.isEmpty() && path != null) {
                    Route route = new Route();
                    route.addWaypoint(tile);
                    client.getInstantServerInterface(ServerArmiesHandler.class).planMove(entityIds, route);
                    for (Army army : entityIds) {
                        army.setRoute(route);
                    }
                    gmf.getSelection().clear();
                    mainMapControl.invalidateBackground();
                    mainMapControl.getCanvas().redraw();
                } else if (ts.getOwner() == client.getCurrentPlayer()) {
                    gmf.getSelection().setSelected(ts);
                    gmf.selectStack(ts);
                    ((Control) e.widget).redraw();
                } else {
                    EventProperty.setProperty(e, "clickedEmpty", Boolean.TRUE);
                }
            }
        }
    }

    /** Mouse button is released, not processed */
    public void mouseUp(MouseEvent e) {
    }

    /** Mouse moves */
    public void mouseMove(MouseEvent e) {
        if (gmf.isLocked()) {
            return;
        }
        View view = mainMapControl.getView();
        if (view == null) {
            return;
        }
        Point newTileOver = view.relWindowToTile(new Point(e.x, e.y));
        if (!newTileOver.equals(tileOver)) {
            tileOver = newTileOver;
        }
        if (gmf.getSelection().getTileSelection() != null && gmf.getSelection().getTileSelection().getPoint() != null && !gmf.getSelection().getFocusedUnits().isEmpty()) {
            Point source = gmf.getSelection().getTileSelection().getPoint();
            if (!source.equals(tileOver)) {
                if (pathCalc == null || pathCalc.getTileSelection() != gmf.getSelection().getTileSelection()) {
                    pathCalc = new StackPathCalculator(gmf.getSelection().getTileSelection(), source, client.getGame());
                }
                path = pathCalc.calculatePath(tileOver);
                ((Control) e.widget).redraw();
            } else {
                path = null;
                ((Control) e.widget).redraw();
            }
        }
    }

    /** The user has selected, so do not display the path anymore */
    public void userSelects(Selection selected, boolean focusChanged) {
        path = null;
    }

    /** Returns the path */
    public Path getPath() {
        return path;
    }
}
