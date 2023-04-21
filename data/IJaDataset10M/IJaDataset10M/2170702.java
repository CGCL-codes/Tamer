package ru.caffeineim.protocols.icq.integration.listeners;

import java.util.EventListener;
import ru.caffeineim.protocols.icq.integration.events.MetaAckEvent;

/**
 * <p>Created by 30.03.2008
 *   @author Samolisov Pavel
 */
public interface MetaAckListener extends EventListener {

    public void onMetaAck(MetaAckEvent e);
}
