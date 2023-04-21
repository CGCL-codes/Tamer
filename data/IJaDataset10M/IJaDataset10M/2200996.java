package org.geoforge.guillcolg.wwd.util.prd;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class OurContextMenuControllerPicksManPrdLinePipOlg extends OurContextMenuControllerPicksManPrdAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(OurContextMenuControllerPicksManPrdLinePipOlg.class.getName());

    static {
        OurContextMenuControllerPicksManPrdLinePipOlg._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public OurContextMenuControllerPicksManPrdLinePipOlg(ActionListener alrController) {
        super(alrController);
    }
}
