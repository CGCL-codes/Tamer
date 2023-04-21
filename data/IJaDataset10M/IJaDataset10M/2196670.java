package net.sf.babble.plugins.dcc.events;

import java.util.EventListener;

/**
 *
 * @author  ben
 */
public interface DccErrorMessageEventHandler extends EventListener {

    /** Creates a new instance of DccErrorMessageEventHandler */
    public void onDccErrorMessage(DccErrorMessageEvent event);
}
