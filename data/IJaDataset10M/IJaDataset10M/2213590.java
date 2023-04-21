package ru.caffeineim.protocols.icq.integration.events;

import java.util.EventObject;
import ru.caffeineim.protocols.icq.packet.received.icbm.MessageAutoReply__4_11;
import ru.caffeineim.protocols.icq.setting.enumerations.XStatusModeEnum;

/**
 * <p>Created by 17.07.07
 *   @author Samolisov Pavel 
 */
public class XStatusResponseEvent extends EventObject {

    private static final long serialVersionUID = 3401985213523814147L;

    private XStatusModeEnum xstatus = new XStatusModeEnum(XStatusModeEnum.NONE);

    private String title = "";

    private String description = "";

    /** 
	 * Creates a new instance of XStatusResponseEvent 
	 */
    public XStatusResponseEvent(MessageAutoReply__4_11 source) {
        super(source);
        parseXStatusMessage((String) ((MessageAutoReply__4_11) getSource()).getMessage());
    }

    private void parseXStatusMessage(String message) {
        String[] strs = message.split("[&;]");
        try {
            xstatus = new XStatusModeEnum(Integer.parseInt(strs[44]));
            title = strs[52];
            description = strs[60];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getSenderID() {
        return ((MessageAutoReply__4_11) getSource()).getSenderID();
    }

    public XStatusModeEnum getXStatus() {
        return xstatus;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
