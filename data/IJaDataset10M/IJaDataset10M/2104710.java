package com.quikj.application.web.talk.messaging.oamp;

import com.quikj.application.web.oamp.messaging.OAMPMessageInterface;

/**
 * 
 * @author bhm
 */
public class MonitorEndPointsStopMessage implements OAMPMessageInterface {

    public static final String MESSAGE_TYPE = "monitor_endpoints_stop";

    private String errorMessage = "";

    /** Creates a new instance of MonitorEndPointsStopMessage */
    public MonitorEndPointsStopMessage() {
    }

    public String format() {
        return "<" + OAMPTalkMessageTypes.MONITOR_ENDPOINTS_STOP_NODE_NAME + "/>\n";
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String messageType() {
        return MESSAGE_TYPE;
    }

    public boolean parse(Object node_object) {
        return true;
    }
}
