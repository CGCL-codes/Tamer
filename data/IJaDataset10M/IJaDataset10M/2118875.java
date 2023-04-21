package jalview.gui.event;

import java.util.EventListener;
import jalview.gui.event.EditEvent;

public interface EditListener extends EventListener {

    public boolean handleEditEvent(EditEvent evt);
}
