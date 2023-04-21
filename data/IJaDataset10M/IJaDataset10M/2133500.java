package org.geoforge.wrpbasspcdatogc.project;

import org.geoforge.basdatogc.database.GfrDtbSpcDatProjectOgc;
import org.geoforge.ioogc.util.property.PrpNamingOgc;
import org.geoforge.wrpbasspcdat.project.GfrWriteSpcPrjDatAbs;
import org.geoforge.wrpsql.ActDtbCreateDb;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public abstract class GfrWriteSpcPrjDatOgcAbs extends GfrWriteSpcPrjDatAbs {

    protected GfrWriteSpcPrjDatOgcAbs() {
        super();
    }

    @Override
    protected void _doJobEmpty(String strPathAbsTargetFolder) throws Exception {
        super._createFolder_(strPathAbsTargetFolder);
        String strPathDtbDtaWms = strPathAbsTargetFolder + java.io.File.separator + PrpNamingOgc.STR_NAME_FILE_DB_SPACEPROJECT_DTA;
        ActDtbCreateDb.s_getInstance().doJob("", "", strPathDtbDtaWms, GfrDtbSpcDatProjectOgc.s_getInstance());
        super.initDatabase(strPathDtbDtaWms, GfrDtbSpcDatProjectOgc.s_getInstance());
    }
}
