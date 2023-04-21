package org.geoforge.basdatolg.table.objects.opr;

import org.geoforge.sql.constraint.CstPrimaryKey;
import org.geoforge.basdat.table.object.GfrDtbDtaTblTloAbs;
import org.geoforge.bas.table.GfrTblBasAbs;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class GfrDtbDtaTblTloOprPip extends GfrDtbDtaTblTloAbs {

    private static GfrDtbDtaTblTloOprPip _INSTANCE_;

    private GfrDtbDtaTblTloOprPip() {
        super("pip");
        this._cstPrimaryKey_.addField(GfrTblBasAbs.FLD_INT_ID_DB);
        super.add(_cstPrimaryKey_);
    }

    public static synchronized GfrDtbDtaTblTloOprPip s_getInstance() {
        if (_INSTANCE_ == null) {
            _INSTANCE_ = new GfrDtbDtaTblTloOprPip();
        }
        return _INSTANCE_;
    }

    private CstPrimaryKey _cstPrimaryKey_ = new CstPrimaryKey("primaryKey");
}
