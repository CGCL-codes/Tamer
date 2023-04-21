package hottargui.standard;

import hottargui.framework.*;

public class AttackState implements MoveAttackState {

    public State moveAttack(MoveAttackStrategy context) {
        return null;
    }

    public State dieRolled(MoveAttackStrategy context) {
        State nextState = State.defend;
        return nextState;
    }

    public State givingUp(MoveAttackStrategy context) {
        return State.buy;
    }
}
