package org.openconcerto.erp.rights;

import org.openconcerto.sql.users.rights.UserRightsManager;
import org.openconcerto.sql.users.rights.UserRightsManager.RightTuple;
import java.util.ArrayList;
import java.util.List;

/**
 * Accés total à la comptabilité
 * 
 * @author Ludo
 * 
 */
public class ComptaTotalUserRight extends ComptaUserRight {

    public ComptaTotalUserRight() {
        super(ComptaUserRight.TOTAL);
    }

    @Override
    public List<RightTuple> expand(UserRightsManager mngr, String rightCode, String object, boolean haveRight) {
        final List<RightTuple> res = new ArrayList<RightTuple>();
        res.add(new RightTuple(ComptaUserRight.MENU, haveRight));
        res.add(new RightTuple(ComptaUserRight.ACCES_NOT_RESCTRICTED_TO_411, haveRight));
        return res;
    }
}
