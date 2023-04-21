package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import com.aimluck.eip.cayenne.om.portlet.EipTTimelineFile;
import com.aimluck.eip.timeline.util.TimelineUtils;

/**
 * 掲示板トピックの添付ファイルのサムネイルを処理するクラスです。
 */
public class TimelineFileThumbnailScreen extends FileuploadThumbnailScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(TimelineFileThumbnailScreen.class.getName());

    /**
   * 
   * @param rundata
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata) throws Exception {
        try {
            EipTTimelineFile Timelinefile = TimelineUtils.getEipTTimelineFile(rundata);
            super.setFile(Timelinefile.getFileThumbnail());
            super.setFileName(Timelinefile.getFileName());
            super.doOutput(rundata);
        } catch (Exception e) {
            logger.error("[ERROR]", e);
        }
    }
}
