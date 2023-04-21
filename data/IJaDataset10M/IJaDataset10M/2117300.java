package org.geoforge.basdatolg.table.objects.oxp;

import org.geoforge.basdat.table.object.GfrDtbDtaTblMloAbs;
import org.geoforge.sql.field.sql92.FldSql92VaryingCharacter;
import org.geoforge.sql.field.sql99.FldSql99Boolean;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class GfrDtbDtaTblMloOlgExpWelllog extends GfrDtbDtaTblMloAbs {

    private static GfrDtbDtaTblMloOlgExpWelllog _INSTANCE_;

    public static FldSql99Boolean FLD_BLN_DEPTH_METER = new FldSql99Boolean("bln_depth_meter", false, true);

    public static FldSql92VaryingCharacter FLD_VAR_CHAR_TYPE = new FldSql92VaryingCharacter("type", 80, false, false);

    public static FldSql92VaryingCharacter FLD_VAR_CHAR_DATA = new FldSql92VaryingCharacter("geometry", 400, false, false);

    private GfrDtbDtaTblMloOlgExpWelllog() {
        super("well_logs", GfrDtbDtaTblTloOxpWll.s_getInstance());
        super.add(FLD_VAR_CHAR_TYPE);
        super.add(FLD_BLN_DEPTH_METER);
        super.add(FLD_VAR_CHAR_DATA);
    }

    public static synchronized GfrDtbDtaTblMloOlgExpWelllog s_getInstance() {
        if (_INSTANCE_ == null) {
            _INSTANCE_ = new GfrDtbDtaTblMloOlgExpWelllog();
        }
        return _INSTANCE_;
    }
}
