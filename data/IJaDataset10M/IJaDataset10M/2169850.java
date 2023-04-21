package common.model.interfaces;

import common.model.Fight;

/**
 * Defines objects that can take damage and can defend themselfs from an attack
 * when canCounterAttack return true an counter attack is invoked.
 *
 * @author Stefan
 * @since 2.0
 */
public interface Defender extends Damageable {

    void defend(Attacker attacker, Fight fight);

    boolean canCounterAttack();
}
