package de.outstare.fortbattleplayer.player.actions;

import de.outstare.fortbattleplayer.model.Combatant;

/**
 * Aim at somebody
 * 
 * @author daniel
 */
public class AimAction extends CombatantAction {

    private final Combatant target;

    /**
	 * @param combatant
	 * @param target
	 */
    public AimAction(final Combatant combatant, final Combatant target) {
        super(combatant);
        this.target = target;
    }

    /**
	 * @see de.outstare.fortbattleplayer.player.Action#execute()
	 */
    public void execute() {
        player.aimAt(target);
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return player + " aims at " + target;
    }

    /**
	 * @see de.outstare.fortbattleplayer.player.Action#getActor()
	 */
    @Override
    public Combatant getActor() {
        return player;
    }
}
