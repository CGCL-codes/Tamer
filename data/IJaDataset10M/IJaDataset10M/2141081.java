package agonism.ch.bvr.impl;

import agonism.ce.cells.Cell;
import agonism.ce.cells.CurrentCell;
import agonism.ch.mazer.impl.ActorLifeState;
import agonism.ce.AbstractUpdateHandler;
import agonism.ce.Actor;
import agonism.ce.Debug;
import agonism.ce.GameState;
import java.util.Enumeration;

/**
 * Handles the implications of actors which are being teleported. An actor is teleported when
 * it steps onto a cell that contains a teleporter. Teleportation is instantaneous, which is to say
 * that the actor will begin a turn in a cell adjacent to the teleporter, and begin the next turn
 * at the teleporter's destination. Interesting cases:
 * <ul>
 * <li>If another actor is located in the destination cell, that actor is killed ('telefragged', to use
 * the Quake nomenclature).
 * <li>If an actor steps into a cell which contains a teleporter, and is also being fired on, that actor
 * is miraculously saved and emerges at the destination.
 * <li>If an actor comes out of a teleporter into a cell that is being fired on, that actor is killed
 * </ul>
 * Note that since any actor that steps onto a teleporter cell will always be moved away from it,
 * teleporter cells will never be owned by anyone.
 */
public class TeleportHandler extends AbstractUpdateHandler {

    public static final int TRACE_ID = Debug.getTraceID();

    public void update(GameState state) {
        Teleporters tps = Teleporters.of(state);
        CurrentCell cc = CurrentCell.of(state);
        ActorLifeState ls = ActorLifeState.of(state);
        for (Enumeration e = state.actors(); e.hasMoreElements(); ) {
            Actor actor = (Actor) e.nextElement();
            if (ls.isAlive(actor)) {
                Cell cell = cc.getCurrentCell(actor);
                Teleporter tp;
                if ((tp = tps.getTeleporter(cell)) != null) {
                    if (Debug.isTracing(TRACE_ID)) Debug.trace(tp + " teleporting " + actor);
                    Cell destination = tp.getDestination();
                    cc.setCurrentCell(actor, destination);
                }
            }
        }
    }
}
