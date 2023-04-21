package org.bordeauxjug.dojo.wombat.processes;

import org.bordeauxjug.dojo.wombat.Prey;
import org.bordeauxjug.dojo.wombat.Wombat;
import org.bordeauxjug.dojo.wombat.WombatException;
import org.bordeauxjug.dojo.wombat.World.CardinalPoint;
import org.bordeauxjug.dojo.wombat.World.Cell.Quantity;

/**
 *
 * @author laurent.foret
 */
public abstract class WombatProcess {

    private Wombat wombat;

    public WombatProcess(Wombat wombat) {
        this.wombat = wombat;
    }

    public abstract void eat(Prey prey, Quantity qty) throws WombatException;

    public abstract void move(CardinalPoint cp) throws WombatException;
}
