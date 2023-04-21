package ru.caffeineim.protocols.icq.packet.received.authorization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationSuccessEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.packet.received.ReceivedPacket;

/**
 * <p>Created by
 *   @author Egor Baranov
 */
public class UINRegistrationSuccess__23_5 extends ReceivedPacket {

    private static Log log = LogFactory.getLog(UINRegistrationSuccess__23_5.class);

    protected String uin;

    public UINRegistrationSuccess__23_5(byte array[]) {
        super(array, true);
        RawData uinData = new RawData(array, 50, RawData.DWORD_LENGHT);
        uinData.invertIndianness();
        uin = uinData.toStringValue();
    }

    public String getNewUIN() {
        return uin;
    }

    public void notifyEvent(OscarConnection connection) {
        UINRegistrationSuccessEvent e = new UINRegistrationSuccessEvent(this);
        for (int i = 0; i < connection.getMetaInfoListeners().size(); i++) {
            MetaInfoListener l = (MetaInfoListener) connection.getMessagingListeners().get(i);
            log.debug("notify listener " + l.getClass().getName() + " onRegisterNewUINSucces()");
            l.onRegisterNewUINSuccess(e);
        }
    }
}
