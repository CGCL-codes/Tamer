package com.luzan.app.map.processor;

import com.luzan.app.map.bean.user.UserMapOriginal;
import com.luzan.app.map.manager.MapManager;
import com.luzan.app.map.tool.MapTileBuilder;
import com.luzan.app.map.utils.Configuration;
import com.luzan.common.geomap.LatLngPoint;
import com.luzan.common.geomap.LatLngTetragon;
import com.luzan.common.processor.ProcessorContext;
import com.luzan.common.processor.ProcessorException;
import com.luzan.common.processor.ProcessorTask;
import com.sun.xfile.XFile;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MapTilingTask
 *
 * @author Alexander Bondar
 */
public class MapTilingTask implements ProcessorTask<UserMapOriginal> {

    private static final Logger logger = Logger.getLogger(MapTilingTask.class);

    public boolean preProcess(ProcessorContext<UserMapOriginal> context) throws InterruptedException, ProcessorException {
        logger.info("MapTilingTask:preProcess");
        return true;
    }

    public byte process(ProcessorContext<UserMapOriginal> context) throws InterruptedException, ProcessorException {
        logger.info("MapTilingTask:process");
        UserMapOriginal map = context.getItem().getEntity();
        try {
            final XFile dir = new XFile(new XFile(Configuration.getInstance().getPrivateMapStorage().toString()), map.getGuid());
            dir.mkdir();
            XFile thmbFileS = new XFile(dir, "thumbnail-s");
            XFile thmbFileM = new XFile(dir, "thumbnail-m");
            XFile thmbFileL = new XFile(dir, "thumbnail-l");
            final XFile mapFile = new XFile(new XFile(new XFile(Configuration.getInstance().getPrivateMapStorage().toString()), map.getGuid()), map.getGuid());
            MapTileBuilder.scale(mapFile, thmbFileS, 256);
            MapTileBuilder.scale(mapFile, thmbFileM, 512);
            MapTileBuilder.scale(mapFile, thmbFileL, 768);
            final Polygon polygon;
            if (!StringUtils.isEmpty(map.getRegion()) && MapManager.patternRegion.matcher(map.getRegion()).matches()) {
                Matcher m = Pattern.compile("(\\d+)\\s*:\\s*(\\d+)").matcher(map.getRegion());
                polygon = new Polygon();
                while (m.find()) {
                    polygon.addPoint(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
                }
            } else polygon = null;
            LatLngTetragon llt = new LatLngTetragon(new LatLngPoint(map.getSWLat(), map.getSWLon()), new LatLngPoint(map.getNELat(), map.getNELon()));
            MapTileBuilder.disassemble(map.getGuid(), mapFile, dir, 256, llt, polygon, (map.getDeltaLon() == null) ? 0 : map.getDeltaLon(), (map.getDeltaLat() == null) ? 0 : map.getDeltaLat());
        } catch (Throwable e) {
            logger.error("error:", e);
            throw new ProcessorException(e);
        }
        return TaskState.STATE_TILING;
    }

    public void postProcess(ProcessorContext<UserMapOriginal> context) throws InterruptedException, ProcessorException {
        logger.info("MapTilingTask:postProcess");
    }
}
