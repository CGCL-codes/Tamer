package gameserver.controllers.movement;

import gameserver.controllers.attack.AttackStatus;

public class AttackStatusObserver extends AttackCalcObserver {

    protected int value;

    protected AttackStatus status;

    /**
	 * 
	 * @param value
	 * @param status
	 */
    public AttackStatusObserver(int value, AttackStatus status) {
        this.value = value;
        this.status = status;
    }
}
