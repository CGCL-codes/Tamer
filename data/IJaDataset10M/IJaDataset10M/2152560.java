package ms.jasim.console;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Panel;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DefaultDockingPort;
import org.flexdock.docking.drag.effects.EffectsManager;
import org.flexdock.docking.drag.preview.GhostPreview;

public class FlexDockPanel extends Panel implements JasimDockManager {

    private static final long serialVersionUID = 1L;

    private DockingPort viewport;

    private JasimDockable mainView;

    public FlexDockPanel(TestGui testGui) {
        super(new BorderLayout());
        DockingManager.setFloatingEnabled(true);
        System.setProperty(DockingConstants.HEAVYWEIGHT_DOCKABLES, "true");
        EffectsManager.setPreview(new GhostPreview());
        viewport = new DefaultDockingPort();
    }

    @Override
    public Container getContainer() {
        return (Container) viewport;
    }

    @Override
    public void setMainView(JasimDockable view) {
        mainView = view;
        Component d = mainView.getDockableWrapper(this);
        DockingManager.dock(d, viewport, DockingConstants.CENTER_REGION);
    }

    @Override
    public void addDockable(JasimDockable view) {
        if (!dockableExists(view)) {
            Component d = view.getDockableWrapper(this);
            Component m = mainView.getDockableWrapper(this);
            DockingManager.dock((Component) view.getDockableWrapper(this), m, (String) view.getDockProperty(JasimDockable.DP_DOCK_POSITION), 0.3f);
        }
    }

    public boolean dockableExists(JasimDockable view) {
        return false;
    }
}
