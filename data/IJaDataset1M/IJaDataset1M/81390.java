package jorgan.memory.gui.dock;

import java.util.ArrayList;
import java.util.List;
import jorgan.gui.dock.OrganDockable;
import jorgan.gui.dock.spi.DockableProvider;

public class MemoryDockableProvider implements DockableProvider {

    public List<OrganDockable> getDockables() {
        ArrayList<OrganDockable> dockables = new ArrayList<OrganDockable>();
        dockables.add(new MemoryDockable());
        return dockables;
    }
}
