package org.geoforge.wrpbasspcdspolg.root.opr;

import java.util.logging.Logger;
import org.geoforge.basdspolg.database.opr.GfrDtbSpcDspProjectOpr;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.wrpbasspcdspogc.root.GfrWrpBasDspSpcRootOgcAbs;

/**
 *
 * @author bantchao
 */
public class GfrWrpBasDspSpcRootOgcOpr extends GfrWrpBasDspSpcRootOgcAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrWrpBasDspSpcRootOgcOpr.class.getName());

    static {
        GfrWrpBasDspSpcRootOgcOpr._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    private static GfrWrpBasDspSpcRootOgcOpr _INSTANCE_ = null;

    public static GfrWrpBasDspSpcRootOgcOpr getInstance() {
        if (GfrWrpBasDspSpcRootOgcOpr._INSTANCE_ == null) GfrWrpBasDspSpcRootOgcOpr._INSTANCE_ = new GfrWrpBasDspSpcRootOgcOpr();
        return GfrWrpBasDspSpcRootOgcOpr._INSTANCE_;
    }

    @Override
    public void open() throws Exception {
        super.open();
        if (GfrDtbSpcDspProjectOpr.s_getInstance().getUrl() != null) {
            String str = "GfrDtbSpcDspProjectOpr.s_getInstance().getUrl() != null";
            GfrWrpBasDspSpcRootOgcOpr._LOGGER_.severe(str);
            throw new Exception(str);
        }
    }

    private GfrWrpBasDspSpcRootOgcOpr() {
    }
}
