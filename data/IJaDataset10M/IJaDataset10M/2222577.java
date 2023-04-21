package org.fenggui.event;

import org.fenggui.composite.Window;

/**
 * Class that represents the event of closing a window.
 * 
 * @author Schabby, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class WindowClosedEvent extends WidgetEvent {

    private Window window = null;

    public WindowClosedEvent(Window w) {
        super(w);
        window = w;
    }

    public Window getWindow() {
        return window;
    }
}
