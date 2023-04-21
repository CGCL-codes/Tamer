package org.geoforge.mdldatolg.opr;

import org.geoforge.mdldatolg.GfrMdlDtaSetTlosLclOlgAbs;
import java.awt.geom.Point2D;
import org.geoforge.wrpbasprssynolg.opr.GfrWrpBasSynTopSbss;

/**
 *
 * @author bantchao
 */
public class GfrMdlDtaSetTlosDskSbs extends GfrMdlDtaSetTlosLclOlgAbs {

    private static GfrMdlDtaSetTlosDskSbs _INSTANCE_ = null;

    public static GfrMdlDtaSetTlosDskSbs getInstance() {
        if (GfrMdlDtaSetTlosDskSbs._INSTANCE_ == null) GfrMdlDtaSetTlosDskSbs._INSTANCE_ = new GfrMdlDtaSetTlosDskSbs();
        return GfrMdlDtaSetTlosDskSbs._INSTANCE_;
    }

    @Override
    public void delete(String strId) throws Exception {
        GfrWrpBasSynTopSbss.getInstance().delete(strId);
        super.delete(strId);
    }

    @Override
    public void deleteAll() throws Exception {
        String[] strsId = GfrWrpBasSynTopSbss.getInstance().getIds();
        GfrWrpBasSynTopSbss.getInstance().deleteAll();
        super._deleteAll(strsId);
    }

    public void newObject(String strName, String strDescShort, String strUrl, Point2D.Double p2d) throws Exception {
        String strId = GfrWrpBasSynTopSbss.getInstance().save(strName, strDescShort, strUrl, p2d);
        super._newObject(strId);
    }

    private GfrMdlDtaSetTlosDskSbs() {
        super();
    }
}
