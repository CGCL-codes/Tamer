package server.universe.mob;

import java.io.Serializable;
import server.universe.Creature;

/**
 * The MOB class represents a mobile non-player character.
 *
 * Thus quoteth The Spec:
 *
 * "MOBs can also move from room to room, and players may have complex
 * interactions with them. Though most MOBs are hostile towards the player, they
 * do not all need to be. Some MOBs may offer quests, services or games to the
 * players."
 *
 */
public abstract class MOB extends Creature implements Runnable, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private static final long SLEEP_TIME = 3000;

    private BehaviorStrategy behavior;

    private DialogStrategy dialog;

    private boolean alive;

    public MOB(String name) {
        super(name);
        this.alive = true;
        this.behavior = new NullBehavior();
        this.dialog = new NullDialog();
    }

    /**
	 * This is what the MOB does once it is spawned.
	 *
	 * Each turn, it does some mob specific behavior, possibly changing
	 * to a different behavior, then it
	 */
    public void run() {
        while (this.alive) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            takeTurn();
            behavior.doAction(this);
            if (this.getHealth() <= 0) this.alive = false;
        }
    }

    /**
	 * Do some MOB-specific behavior. This might involve changing to a different strategy.
	 */
    public abstract void takeTurn();

    /**
	 * Receive a message (from either a player or another MOB).
	 * This will be called when a user says something in the room of a MOB
	 * or tells something to a mob.
	 *
	 * Depending on the current DialogStrategy of the MOB, the MOB may ignore
	 * the message or respond in some way.
	 *
	 * @param message the text of what is said.
	 */
    public void tell(Creature sender, String message) {
        dialog.tell(sender, message);
    }

    /**
	 * Set the current behavior strategy.
	 */
    public void setBehavior(BehaviorStrategy behavior) {
        this.behavior = behavior;
    }

    /**
	 * Set the current dialog strategy.
	 */
    public void setDialog(DialogStrategy dialog) {
        this.dialog = dialog;
    }

    /**
	 * Return a copy of this MOB.
	 */
    public MOB clone() {
        try {
            return (MOB) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
