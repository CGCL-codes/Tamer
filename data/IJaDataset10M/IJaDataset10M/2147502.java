package apollo.gui.event;

import java.util.EventListener;
import apollo.gui.event.RegionChangeEvent;

public interface RegionChangeListener extends EventListener {

    public boolean handleRegionChangeEvent(RegionChangeEvent evt);
}
