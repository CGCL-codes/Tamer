package ru.caffeineim.protocols.icq.metainfo;

import java.util.Date;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.caffeineim.protocols.icq.RawData;
import ru.caffeineim.protocols.icq.core.OscarConnection;
import ru.caffeineim.protocols.icq.exceptions.ConvertStringException;
import ru.caffeineim.protocols.icq.integration.events.OfflineMessageEvent;
import ru.caffeineim.protocols.icq.integration.listeners.MessagingListener;
import ru.caffeineim.protocols.icq.setting.enumerations.MessageFlagsEnum;
import ru.caffeineim.protocols.icq.setting.enumerations.MessageTypeEnum;
import ru.caffeineim.protocols.icq.tool.DateTools;
import ru.caffeineim.protocols.icq.tool.StringTools;

/**
 * <p>Created by 23.03.2008
 *   @author Samolisov Pavel
 */
public class OfflineMessageParser extends BaseMetaInfoParser {

    private static Log log = LogFactory.getLog(OfflineMessageParser.class);

    private String senderUin;

    private Date sendDate;

    private String message;

    private int type;

    private int flag;

    protected EventObject getNewEvent() {
        return new OfflineMessageEvent(this);
    }

    protected void sendMessage(EventListener listener, EventObject e) {
        log.debug("notify listener " + listener.getClass().getName() + " onOfflineMessage()");
        ((MessagingListener) listener).onOfflineMessage((OfflineMessageEvent) e);
    }

    public void parse(byte[] data, int position) throws ConvertStringException {
        RawData uin = new RawData(data, position, RawData.DWORD_LENGHT);
        uin.invertIndianness();
        senderUin = uin.toStringValue();
        position += RawData.DWORD_LENGHT;
        RawData year = new RawData(data, position, RawData.WORD_LENGHT);
        year.invertIndianness();
        position += RawData.WORD_LENGHT;
        RawData month = new RawData(data, position, RawData.BYTE_LENGHT);
        position += RawData.BYTE_LENGHT;
        RawData day = new RawData(data, position, RawData.BYTE_LENGHT);
        position += RawData.BYTE_LENGHT;
        RawData hour = new RawData(data, position, RawData.BYTE_LENGHT);
        position += RawData.BYTE_LENGHT;
        RawData minute = new RawData(data, position, RawData.BYTE_LENGHT);
        position += RawData.BYTE_LENGHT;
        sendDate = DateTools.makeDate(year.getValue(), month.getValue(), day.getValue(), hour.getValue(), minute.getValue());
        type = new RawData(data, position, RawData.BYTE_LENGHT).getValue();
        position += RawData.BYTE_LENGHT;
        flag = new RawData(data, position, RawData.BYTE_LENGHT).getValue();
        position += RawData.BYTE_LENGHT;
        RawData msgLen = new RawData(data, position, RawData.WORD_LENGHT);
        msgLen.invertIndianness();
        position += RawData.WORD_LENGHT;
        message = StringTools.byteArrayToString(data, position, msgLen.getValue() - 1);
    }

    protected List getListenersList(OscarConnection connection) {
        return connection.getMessagingListeners();
    }

    public String getSenderUin() {
        return senderUin;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public String getMessage() {
        return message;
    }

    public MessageTypeEnum getMessageType() {
        return new MessageTypeEnum(type);
    }

    public MessageFlagsEnum getMessageFlag() {
        return new MessageFlagsEnum(flag);
    }
}
