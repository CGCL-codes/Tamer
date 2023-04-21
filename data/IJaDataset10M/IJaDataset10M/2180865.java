package net.virtualinfinity.atrobots.arena;

import net.virtualinfinity.atrobots.arenaobjects.CollidableArenaObject;
import net.virtualinfinity.atrobots.arenaobjects.DamageInflicter;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public abstract class TangibleArenaObject extends CollidableArenaObject {

    public abstract void inflictDamage(DamageInflicter cause, double amount);

    public abstract void collides();

    public abstract void winRound();

    public abstract void tieRound();
}
