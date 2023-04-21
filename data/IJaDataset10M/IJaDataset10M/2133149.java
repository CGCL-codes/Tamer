package com.google.code.sagetvaddons.sagealert.server;

import gkusnick.sagetv.api.API;
import gkusnick.sagetv.api.SystemMessageAPI;
import java.util.Map;
import org.apache.log4j.Logger;
import sage.SageTVEventListener;
import com.google.code.sagetvaddons.sagealert.plugin.Plugin;
import com.google.code.sagetvaddons.sagealert.server.events.SystemMessageErrorEvent;
import com.google.code.sagetvaddons.sagealert.server.events.SystemMessageInfoEvent;
import com.google.code.sagetvaddons.sagealert.server.events.SystemMessageWarningEvent;

/**
 * @author dbattams
 *
 */
final class SystemMessageEventsListener implements SageTVEventListener {

    private static final Logger LOG = Logger.getLogger(SystemMessageEventsListener.class);

    private static final SystemMessageEventsListener INSTANCE = new SystemMessageEventsListener();

    static final SystemMessageEventsListener get() {
        return INSTANCE;
    }

    private static final String SYSMSG_KEY = "SystemMessage";

    @SuppressWarnings("unchecked")
    public void sageEvent(String arg0, Map arg1) {
        LOG.info("Event received: " + arg0 + " :: " + arg1.toString());
        Object obj = arg1.get(SYSMSG_KEY);
        if (API.apiNullUI.systemMessageAPI.IsSystemMessageObject(obj)) {
            if (CoreEventsManager.SYSMSG_POSTED.equals(arg0)) {
                SystemMessageAPI.SystemMessage msg = API.apiNullUI.systemMessageAPI.Wrap(obj);
                if (msg.GetSystemMessageRepeatCount() > 1 && Boolean.parseBoolean(API.apiNullUI.configuration.GetServerProperty(Plugin.OPT_IGNORE_REPEAT_SYS_MSGS, Plugin.OPT_IGNORE_REPEAT_SYS_MSGS_DEFAULT))) LOG.warn("Not firing event for system message '" + msg.GetSystemMessageTypeName() + "' because it is a repeated message!"); else if (msg.GetSystemMessageLevel() == 1) SageAlertEventHandlerManager.get().fire(new SystemMessageInfoEvent(msg, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.INFO_SYSMSG_POSTED))); else if (msg.GetSystemMessageLevel() == 2) SageAlertEventHandlerManager.get().fire(new SystemMessageWarningEvent(msg, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.WARN_SYSMSG_POSTED))); else if (msg.GetSystemMessageLevel() == 3) SageAlertEventHandlerManager.get().fire(new SystemMessageErrorEvent(msg, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.ERROR_SYSMSG_POSTED)));
            } else LOG.error("Unhandled event: " + arg0);
        } else LOG.error("Invalid contents in args map!  Event ignored.");
    }
}
