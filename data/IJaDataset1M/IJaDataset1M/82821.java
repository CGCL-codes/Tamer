package org.mobicents.media.server.spi;

import java.io.Serializable;
import org.mobicents.media.server.spi.events.NotifyEvent;

/**
 *
 * @author Oleg Kulikov
 */
public interface NotificationListener extends Serializable {

    public void update(NotifyEvent event);
}
