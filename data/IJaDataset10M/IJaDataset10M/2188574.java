package com.luzan.app.map.processor;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import com.luzan.app.map.bean.MapOriginal;
import com.luzan.app.map.bean.MapTile;
import com.luzan.common.processor.ProcessorContext;
import com.luzan.common.processor.ProcessorException;
import com.luzan.common.processor.ProcessorTask;
import java.util.*;

/**
 * MapOriginalStartTask
 *
 * @author Alexander Bondar
 */
public class MapOriginalStartTask implements ProcessorTask<MapOriginal> {

    private static final Logger logger = Logger.getLogger(MapOriginalStartTask.class);

    public boolean preProcess(ProcessorContext<MapOriginal> context) throws InterruptedException, ProcessorException {
        logger.info("MapOriginalStartTask:preProcess");
        return true;
    }

    public int process(ProcessorContext<MapOriginal> context) throws InterruptedException, ProcessorException {
        logger.info("MapOriginalStartTask:process");
        MapOriginal map = context.getItem().getEntity();
        if (map.getState() == MapOriginal.MapState.DELETE) {
            if (!StringUtils.isEmpty(map.getMapPath())) {
                context.put("tile_index", 0);
                context.put("path", map.getMapPath());
                map.setMapPath(null);
            } else if (!StringUtils.isEmpty(map.getThumbnailPath())) {
                context.put("path", map.getThumbnailPath());
                map.setThumbnailPath(null);
            } else {
                map.setModified(new Date());
                map.setSubState(MapOriginal.MapSubState.COMPLETE);
                return TaskState.STATE_MO_FINISH;
            }
            return TaskState.STATE_MO_DELETE_FILE;
        } else if (map.getState() == MapOriginal.MapState.UPLOAD) {
            context.put("path", map.getMapPath());
            context.put("mime", map.getMimeType());
            return TaskState.STATE_MO_START;
        } else if (map.getState() == MapOriginal.MapState.CALIBRATE) {
            return TaskState.STATE_MO_TILING;
        } else {
            return TaskState.STATE_MO_FINISH;
        }
    }

    public void postProcess(ProcessorContext<MapOriginal> context) throws InterruptedException, ProcessorException {
        logger.info("MapOriginalStartTask:postProcess");
    }
}
