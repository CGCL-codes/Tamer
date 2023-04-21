package org.geoforge.mdldatolg.ecl;

import org.geoforge.mdldatolg.GfrMdlDtaIdObjTloLclOlgAbs;
import org.geoforge.wrpbasprssynolg.ecl.GfrWrpBasTloSynOlgAre;

/**
 *
 * @author bantchao
 */
public class GfrMdlDtaIdObjTloLclAre extends GfrMdlDtaIdObjTloLclOlgAbs {

    private static GfrMdlDtaIdObjTloLclAre _INSTANCE_ = null;

    public static GfrMdlDtaIdObjTloLclAre getInstance() {
        if (GfrMdlDtaIdObjTloLclAre._INSTANCE_ == null) GfrMdlDtaIdObjTloLclAre._INSTANCE_ = new GfrMdlDtaIdObjTloLclAre();
        return GfrMdlDtaIdObjTloLclAre._INSTANCE_;
    }

    @Override
    public void rename(String strId, String strNameNew) throws Exception {
        GfrWrpBasTloSynOlgAre.getInstance().rename(strId, strNameNew);
        super.rename(strId, strNameNew);
    }

    private GfrMdlDtaIdObjTloLclAre() {
        super();
    }
}
