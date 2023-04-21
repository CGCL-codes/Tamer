package megamek.common.weapons;

import java.util.Vector;
import megamek.common.Entity;
import megamek.common.IGame;
import megamek.common.Report;
import megamek.common.TargetRoll;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

/**
 * @author Andrew Hunter
 */
public class StopSwarmAttackHandler extends WeaponHandler {

    /**
     * 
     */
    private static final long serialVersionUID = 7078803294398264979L;

    /**
     * @param toHit
     * @param waa
     * @param g
     */
    public StopSwarmAttackHandler(ToHitData toHit, WeaponAttackAction waa, IGame g, Server s) {
        super(toHit, waa, g, s);
    }

    @Override
    public boolean handle(IGame.Phase phase, Vector<Report> vPhaseReport) {
        Entity entityTarget = (Entity) target;
        if (toHit.getValue() == TargetRoll.IMPOSSIBLE) {
            Report r = new Report(3105);
            r.subject = subjectId;
            r.add(toHit.getDesc());
            vPhaseReport.addElement(r);
            return false;
        }
        Report r = new Report(3110);
        r.subject = subjectId;
        vPhaseReport.addElement(r);
        if (ae.getSwarmTargetId() != target.getTargetId()) {
            Entity other = game.getEntity(ae.getSwarmTargetId());
            other.setSwarmAttackerId(Entity.NONE);
        } else {
            entityTarget.setSwarmAttackerId(Entity.NONE);
        }
        ae.setSwarmTargetId(Entity.NONE);
        return false;
    }
}
