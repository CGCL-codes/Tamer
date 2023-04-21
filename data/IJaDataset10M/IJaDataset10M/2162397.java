package de.fu_berlin.inf.gmanda.gui.docking;

import gnu.inet.util.BASE64;
import java.awt.Component;
import java.awt.Window;
import java.io.IOException;
import java.util.Collections;
import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.gui.DockController;
import bibliothek.gui.DockFrontend;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.ScreenDockStation;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.action.ActionGuard;
import bibliothek.gui.dock.action.DefaultDockActionSource;
import bibliothek.gui.dock.action.DockActionSource;
import bibliothek.gui.dock.action.LocationHint;
import bibliothek.gui.dock.facile.action.CloseAction;
import bibliothek.gui.dock.layout.PredefinedDockSituation;
import bibliothek.gui.dock.station.split.SplitDockGrid;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * 
 * Tooltip code was written by Benjamin Sigg from DockingFrames.
 */
public class ViewManager {

    PredefinedDockSituation dockSituation;

    SplitDockStation rootDockStation;

    ScreenDockStation screenDockStation;

    DockFrontend dockFrontend;

    SplitDockGrid reset;

    Component rootComponent;

    CloseAction closeAction;

    BiMap<DockableView, Dockable> dockables = HashBiMap.create();

    public ViewManager(DockableView[] views) {
        dockSituation = new PredefinedDockSituation();
        rootDockStation = new SplitDockStation();
        dockSituation.put("station", rootDockStation);
        closeAction = new CloseAction(null);
        for (DockableView view : views) {
            register(view);
        }
    }

    public void toggleFullscreen() {
        Dockable newFullscreenDock = rootDockStation.getFrontDockable();
        if (newFullscreenDock == null) {
            if (rootDockStation.getFullScreen() != null) rootDockStation.setFullScreen(null);
        } else {
            Dockable current = rootDockStation.getFullScreen();
            if (current == newFullscreenDock) rootDockStation.setFullScreen(null); else rootDockStation.setFullScreen(newFullscreenDock);
        }
    }

    public void register(DockableView view) {
        if (!dockables.containsKey(view)) {
            DefaultDockable dockable = new DefaultDockable(view.getComponent(), view.getTitle());
            dockable.setTitleToolTip(view.getTooltip());
            dockSituation.put(view.getId(), dockable);
            dockables.put(view, dockable);
        }
    }

    public void showDockableView(DockableView v) {
        Dockable dockable = getDockable(v);
        if (dockable != null) rootDockStation.drop(dockable);
    }

    public Component getDockStationComponent(Window w) {
        if (rootComponent != null) return rootComponent;
        dockFrontend = new DockFrontend(w);
        EclipseTheme theme = new EclipseTheme();
        DockController controller = dockFrontend.getController();
        controller.setTheme(theme);
        closeAction.setController(controller);
        final DefaultDockActionSource source = new DefaultDockActionSource(new LocationHint(LocationHint.DOCKABLE, LocationHint.RIGHT_OF_ALL));
        source.add(closeAction);
        controller.addActionGuard(new ActionGuard() {

            public DockActionSource getSource(Dockable arg0) {
                return source;
            }

            public boolean react(Dockable arg0) {
                return true;
            }
        });
        screenDockStation = new ScreenDockStation(w);
        screenDockStation.setShowing(true);
        dockFrontend.addRoot(rootDockStation, "station");
        dockFrontend.addRoot(screenDockStation, "screen");
        rootComponent = rootDockStation.getComponent();
        return rootComponent;
    }

    public void setResetLayout(SplitDockGrid grid) {
        reset = grid;
    }

    public boolean setLayout(SplitDockGrid grid) {
        rootDockStation.dropTree(grid.toTree());
        return true;
    }

    public String getLayout() {
        try {
            return new String(BASE64.encode(dockSituation.write(Collections.singletonMap("station", (DockStation) rootDockStation))));
        } catch (IOException e) {
            return null;
        }
    }

    public boolean setLayout(String s) {
        try {
            dockSituation.read(BASE64.decode(s.getBytes()));
            rootDockStation.setFullScreen(null);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void reset() {
        if (reset != null) setLayout(reset);
    }

    /**
	 * Returns the dockable previously registered with this component.
	 */
    public Dockable getDockable(DockableView view) {
        return dockables.get(view);
    }

    public void showPerspective(DockingPerspective perspective) {
        setLayout(perspective.getGrid());
    }
}
