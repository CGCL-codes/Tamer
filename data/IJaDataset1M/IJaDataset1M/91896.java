package pogvue.gui.event;

import java.util.EventListener;

public interface RubberbandListener extends EventListener {

    public boolean handleRubberbandEvent(RubberbandEvent evt);
}
