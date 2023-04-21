package org.geoforge.mdldatolg.oxp;

import org.geoforge.mdldatolg.GfrMdlDtaIdObjTloLclOlgAbs;
import org.geoforge.wrpbasprssynolg.oxp.GfrWrpBasTloSynOlgS3d;

/**
 *
 * @author bantchao
 */
public class GfrMdlDtaIdObjTloLclS3d extends GfrMdlDtaIdObjTloLclOlgAbs {

    private static GfrMdlDtaIdObjTloLclS3d _INSTANCE_ = null;

    public static GfrMdlDtaIdObjTloLclS3d getInstance() {
        if (GfrMdlDtaIdObjTloLclS3d._INSTANCE_ == null) GfrMdlDtaIdObjTloLclS3d._INSTANCE_ = new GfrMdlDtaIdObjTloLclS3d();
        return GfrMdlDtaIdObjTloLclS3d._INSTANCE_;
    }

    @Override
    public void rename(String strId, String strNameNew) throws Exception {
        GfrWrpBasTloSynOlgS3d.getInstance().rename(strId, strNameNew);
        super.rename(strId, strNameNew);
    }

    private GfrMdlDtaIdObjTloLclS3d() {
        super();
    }
}
