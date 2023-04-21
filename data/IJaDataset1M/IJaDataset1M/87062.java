package megamek.common.weapons;

import java.util.Vector;
import megamek.common.EntityWeightClass;
import megamek.common.IGame;
import megamek.common.Mech;
import megamek.common.PilotingRollData;
import megamek.common.Report;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

/**
 * @author Andrew Hunter
 */
public class HGRHandler extends GRHandler {

    /**
     *
     */
    private static final long serialVersionUID = -6599352761593455842L;

    /**
     * @param t
     * @param w
     * @param g
     * @param s
     */
    public HGRHandler(ToHitData t, WeaponAttackAction w, IGame g, Server s) {
        super(t, w, g, s);
    }

    @Override
    protected boolean doChecks(Vector<Report> vPhaseReport) {
        if ((ae.mpUsed > 0) && (ae instanceof Mech)) {
            int nMod;
            if (ae.getWeightClass() <= EntityWeightClass.WEIGHT_LIGHT) {
                nMod = 2;
            } else if (ae.getWeightClass() <= EntityWeightClass.WEIGHT_MEDIUM) {
                nMod = 1;
            } else if (ae.getWeightClass() <= EntityWeightClass.WEIGHT_HEAVY) {
                nMod = 0;
            } else {
                nMod = -1;
            }
            PilotingRollData psr = new PilotingRollData(ae.getId(), nMod, "fired HeavyGauss unbraced", false);
            game.addPSR(psr);
        }
        return false;
    }
}
