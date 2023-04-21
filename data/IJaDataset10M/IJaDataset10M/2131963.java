package com.aimluck.eip.services.timeline.empty;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import com.aimluck.eip.services.timeline.ALTimelineHandler;

/**
 * タイムラインを管理するクラスです。 <br />
 * 
 */
public class ALEmptyTimelineHandler extends ALTimelineHandler {

    @SuppressWarnings("unused")
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(ALEmptyTimelineHandler.class.getName());

    @Override
    public String getToken(RunData rundata) {
        return ("");
    }

    @Override
    public String pushToken(RunData rundata, String parentId) {
        return ("");
    }

    /**
   * @return
   */
    @Override
    public String getApiUrl() {
        return null;
    }
}
