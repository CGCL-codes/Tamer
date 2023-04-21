package org.geoforge.guillcolg.wwd.rlrs.prd;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import java.util.logging.Logger;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 */
public class GfrSetRlrTopMainSrfsMan extends GfrSetRlrTopMainSrfsAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrSetRlrTopMainSrfsMan.class.getName());

    static {
        GfrSetRlrTopMainSrfsMan._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public GfrSetRlrTopMainSrfsMan(WorldWindowGLCanvas cnv) {
        super(cnv);
    }
}
