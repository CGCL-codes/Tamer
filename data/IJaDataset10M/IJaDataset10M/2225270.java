package net.nothinginteresting.ylib.events;

import net.nothinginteresting.ylib.widgets.YControl;
import org.eclipse.swt.events.KeyEvent;

public class YKeyPressedEvent<T extends YControl> extends YKeyEvent<T> {

    public YKeyPressedEvent(KeyEvent event) {
        super(event);
    }
}
