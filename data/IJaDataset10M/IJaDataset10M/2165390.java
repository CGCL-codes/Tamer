package megamek.common.weapons;

import megamek.common.IGame;
import megamek.common.Report;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;
import java.util.Vector;

/**
 * Deric Page (deric.page@usa.net)
 */
public class PrimitiveACWeaponHandler extends ACWeaponHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -3686194077871525280L;

    /**
     * @param t
     * @param w
     * @param g
     */
    public PrimitiveACWeaponHandler(ToHitData t, WeaponAttackAction w, IGame g, Server s) {
        super(t, w, g, s);
    }

    @Override
    protected boolean doChecks(Vector<Report> vPhaseReport) {
        if (roll == 2) {
            Report r = new Report(3161);
            r.subject = subjectId;
            r.newlines = 0;
            vPhaseReport.addElement(r);
            weapon.setJammed(true);
            weapon.setHit(true);
        }
        return false;
    }
}
