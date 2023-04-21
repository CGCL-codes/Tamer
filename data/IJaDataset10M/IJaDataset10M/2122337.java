package saga.model.commands;

import saga.model.Mob;
import saga.model.SpellCaster;
import saga.model.Loc;
import saga.model.Skill;
import saga.view.SagaGui;
import saga.model.map.CombatMap;

/**
 * Command used for casting spells during combat.
 * @author  Klaus Rennecke
 * @version $Revision: 1.1 $
 */
public class SpellCommand extends HitCommand {

    /** Creates new HitCommand */
    public SpellCommand(CombatMap map, Mob villain, Mob victim) {
        super(map, villain, victim);
        map.fireSpellStart(villain, 1);
    }

    public int getRange() {
        return ((SpellCaster) getVillain()).getSpellRange();
    }

    public int getActionPoints() {
        return 50;
    }

    protected boolean isHit() {
        return true;
    }

    protected void damageDone(int dam) {
        if (dam > 0) {
            SagaGui.getSagaGui().message(getVillain().getName() + " blasts " + getVictim().getName() + " for " + dam + " damage!");
            int life = getVictim().damage(dam);
            Loc loc = getMap().getLoc(getVictim());
            getMap().fireSpellDone(loc.x, loc.y, getVillain(), getVictim(), dam);
            if (life < 0) {
                mobDied(getVictim());
            } else if (dam > Math.random() * 20) {
                getMap().fireCombatMessage(getVillain(), "Die fool!");
            }
        } else {
            SagaGui.getSagaGui().message(getVillain().getName() + " blasts but does no damage!");
        }
    }

    public String getSkillName() {
        return Skill.ATTACK;
    }

    public String toString() {
        return "SpellCommand[villain=" + getVillain() + ",victim=" + getVictim() + "]";
    }
}
