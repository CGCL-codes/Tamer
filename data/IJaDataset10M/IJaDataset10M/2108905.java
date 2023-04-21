package net.nothinginteresting.ylib.events;

import net.nothinginteresting.ylib.widgets.YControl;
import org.eclipse.swt.events.DragDetectEvent;

public class YDragDetectEvent<T extends YControl> extends YEvent<T> {

    public YDragDetectEvent(DragDetectEvent event) {
        super(event);
    }
}
