package jorgan.gui.dock.spi;

import java.util.ArrayList;
import java.util.List;
import jorgan.gui.dock.OrganDockable;
import jorgan.util.PluginUtils;

public class DockableRegistry {

    public static List<OrganDockable> getDockables() {
        List<OrganDockable> dockables = new ArrayList<OrganDockable>();
        for (DockableProvider provider : PluginUtils.lookup(DockableProvider.class)) {
            dockables.addAll(provider.getDockables());
        }
        return dockables;
    }
}
