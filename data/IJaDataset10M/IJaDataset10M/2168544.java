package ru.caffeineim.protocols.icq.metainfo;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.integration.events.MetaAckEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MetaAckListener;

/**
 * <p>Created by 30.03.2008
 *   @author Samolisov Pavel
 */
public class MetaAckParser extends BaseMetaInfoParser {

    private static Log log = LogFactory.getLog(MetaAckParser.class);

    private boolean isOk;

    protected EventObject getNewEvent() {
        return new MetaAckEvent(this);
    }

    protected void sendMessage(EventListener listener, EventObject e) {
        log.debug("notify listener " + listener.getClass().getName() + " onMetaAck()");
        ((MetaAckListener) listener).onMetaAck((MetaAckEvent) e);
    }

    public void parse(byte[] data, int position) throws ConvertStringException {
        position += 2;
        int code = new RawData(data, position, RawData.BYTE_LENGHT).getValue();
        isOk = (code == 0x0A);
    }

    protected List getListenersList(OscarConnection connection) {
        return connection.getMetaAckListeners();
    }

    public boolean isOk() {
        return isOk;
    }
}
