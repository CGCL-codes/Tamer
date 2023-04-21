package saga.combat;

import java.util.Iterator;
import java.util.LinkedList;
import saga.control.HeroLocation;
import saga.model.Hero;
import saga.model.map.CombatMap;

/**
 * Service class to control hero actions.
 * @author  Sandra Rennecke
 * @version $Revision: 1.1 $
 */
public class HeroController extends CombatController {

    private LinkedList actionChain;

    /**
     * Schedule next action.
     */
    public synchronized CombatAction getNextAction() {
        return popNextAction();
    }

    public void perform(CombatAction action) {
        action.doIt();
    }

    public void clearActionChain() {
        if (actionChain != null) {
            actionChain.clear();
        }
    }

    public void pushAction(CombatAction action) {
        if (actionChain == null) {
            actionChain = new LinkedList();
        }
        actionChain.addLast(action);
        flushActionChain();
    }

    protected CombatAction popNextAction() {
        if (actionChain != null && !actionChain.isEmpty()) {
            return (CombatAction) actionChain.removeFirst();
        } else {
            return null;
        }
    }

    /** Flush all chained actions when we have no more enemies. */
    protected void flushActionChain() {
        if (actionChain != null && !actionChain.isEmpty()) {
            if (hasKnownEnemies() && HeroLocation.getHeroLocation().getMap() instanceof CombatMap) {
                ((CombatMap) HeroLocation.getHeroLocation().getMap()).getEncounterTimer().resumeEncounter();
            } else {
                while (!actionChain.isEmpty()) {
                    perform(popNextAction());
                }
            }
        }
    }

    /**
     * Get the action points per round.
     */
    public int getMaxActionPoints() {
        Hero hero = HeroLocation.getHeroLocation().getHero();
        return hero != null ? hero.getActionPoints() : 0;
    }

    /** Get the remaining action points. That is, the current action points
     *  minus action points for all queued actions.
     */
    public int getRemainingActionPoints() {
        int result = getActionPoints();
        if (hasKnownEnemies()) {
            if (actionChain != null) {
                Iterator i = actionChain.iterator();
                while (i.hasNext()) {
                    result -= ((CombatAction) i.next()).getActionPoints();
                }
            }
        } else {
            result = getMaxActionPoints() * 10;
        }
        return result;
    }

    /** Clear the set of known enemies.  */
    protected void clearEnemies() {
        super.clearEnemies();
        flushActionChain();
    }

    /** Explicitly remove an enemy from the set of known enemies. Not that
     * the isEnemy() may still return true for <var>other</var>.
     */
    protected void removeEnemy(CombatController other) {
        super.removeEnemy(other);
        flushActionChain();
    }
}
