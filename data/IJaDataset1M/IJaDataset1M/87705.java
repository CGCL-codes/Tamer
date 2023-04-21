package vidis.ui.events;

import java.awt.event.KeyEvent;

public class KeyTypedEvent extends AKeyEvent {

    public KeyTypedEvent(KeyEvent k) {
        super(k);
    }

    public int getID() {
        return IVidisEvent.KeyTypedEvent;
    }
}
