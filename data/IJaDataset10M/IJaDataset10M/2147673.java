package com.calefay.utils;

/** Represents an in-game entity which can be acted upon.
 * The actions which can be performed on a given entity are defined by that entity - this can be many or none.
 * An action is encapsulated in a GameEvent. This provides a type, an initiator, and some action data stored in the target object.
 * For example: Type=doDamage, Initiator=laserGun1, Target=10
 * If the GameActionable entity were to be able to be damaged by laser guns, it would have a case in it's handleAction method for
 * action type "doDamage", and that method might take 10 off it's hitpoints.
 * @author James Waddington
 *
 */
public interface GameActionable {

    /** The 'target' field of an action GameEvent represents the event data, such as amount of damage.
	 * @param action
	 * @return true if the event was successfully handled, false if not (such as if not appropriate for this type of entity)
	 */
    public boolean handleAction(GameEvent action);
}
