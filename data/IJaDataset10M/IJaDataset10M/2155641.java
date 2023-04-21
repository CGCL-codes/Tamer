package org.geoforge.wrpbasspcdatolg.project.oxp;

import java.util.logging.Logger;
import org.geoforge.basdatolg.database.oxp.GfrDtbSpcDatProjectOxp;
import org.geoforge.ioolg.util.property.oxp.PrpNamingOxp;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.wrpbasspcdatogc.project.GfrWrpBasSpcPrjDatOgcAppAbs;

/**
 *
 * @author bantchao
 */
public abstract class GfrWrpBasSpcPrjDatOgcAppOxp extends GfrWrpBasSpcPrjDatOgcAppAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrWrpBasSpcPrjDatOgcAppOxp.class.getName());

    static {
        GfrWrpBasSpcPrjDatOgcAppOxp._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    @Override
    public void close() throws Exception {
        super.close();
        GfrDtbSpcDatProjectOxp.s_getInstance().DEPRECATED_setUrl((String) null);
    }

    @Override
    protected void _open(String strPathAbsSpaceWork, String strIdSource) throws Exception {
        super._open(strPathAbsSpaceWork, strIdSource);
        String strPathAbsDtb = strPathAbsSpaceWork + java.io.File.separator + strIdSource + java.io.File.separator + PrpNamingOxp.STR_NAME_FILE_DB_SPACEPROJECT_DTA;
        GfrDtbSpcDatProjectOxp.s_getInstance().DEPRECATED_setUrl(strPathAbsDtb);
    }

    protected GfrWrpBasSpcPrjDatOgcAppOxp() {
    }
}
